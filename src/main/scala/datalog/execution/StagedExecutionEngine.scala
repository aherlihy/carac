package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.ast.*
import datalog.execution.ast.transform.{ASTTransformerContext, CopyEliminationPass, JoinIndexPass, Transformer}
import datalog.execution.ir.*
import datalog.storage.{CollectionsStorageManager, SimpleStorageManager, StorageManager, DB, KNOWLEDGE}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.quoted.*

abstract class StagedExecutionEngine(val storageManager: StorageManager) extends ExecutionEngine {
  import storageManager.EDB
  val precedenceGraph = new PrecedenceGraph(using storageManager.ns)
  val prebuiltOpKeys: mutable.Map[Int, mutable.ArrayBuffer[JoinIndexes]] = mutable.Map[Int, mutable.ArrayBuffer[JoinIndexes]]()
  val ast: ProgramNode = ProgramNode()
  private var knownDbId = -1
  private val tCtx = ASTTransformerContext(using precedenceGraph)
  private val transforms: Seq[Transformer] = Seq(CopyEliminationPass(using tCtx), JoinIndexPass(using tCtx))

  given ToExpr[Constant] with {
    def apply(x: Constant)(using Quotes) = {
      x match {
        case i: Int => Expr(i)
        case s: String => Expr(s)
      }
    }
  }
  given ToExpr[JoinIndexes] with {
    def apply(x: JoinIndexes)(using Quotes) = {
      '{ JoinIndexes(${Expr(x.varIndexes)}, ${Expr(x.constIndexes)}, ${Expr(x.projIndexes)}, ${Expr(x.deps)}, ${Expr(x.edb)}) }
    }
  }

  def createIR(ast: ASTNode)(using InterpreterContext): IROp

  def initRelation(rId: Int, name: String): Unit = {
    storageManager.ns(rId) = name
    storageManager.initRelation(rId, name)
  }

  def get(rId: Int): Set[Seq[Term]] = {
    if (storageManager.knownDbId == -1)
      throw new Exception("Solve() has not yet been called")
    if (precedenceGraph.idbs.contains(rId))
      storageManager.getKnownIDBResult(rId)
    else
      storageManager.getEDBResult(rId)
  }

  def get(name: String): Set[Seq[Term]] = {
    get(storageManager.ns(name))
  }

  def insertIDB(rId: Int, rule: Seq[Atom]): Unit = {
    precedenceGraph.idbs.addOne(rId)
    val allRules = ast.rules.getOrElseUpdate(rId, AllRulesNode(mutable.ArrayBuffer.empty, rId)).asInstanceOf[AllRulesNode]

    allRules.rules.append(
      RuleNode(
        LogicAtom(
          rule.head.rId,
          rule.head.terms.map {
            case x: Variable => VarTerm(x)
            case x: Constant => ConstTerm(x)
          }),
        rule.drop(1).map(b =>
          LogicAtom(b.rId, b.terms.map {
            case x: Variable => VarTerm(x)
            case x: Constant => ConstTerm(x)
          })),
      ))
  }

  def insertEDB(rule: Atom): Unit = {
    storageManager.insertEDB(rule)
    val allRules = ast.rules.getOrElseUpdate(rule.rId, AllRulesNode(mutable.ArrayBuffer.empty, rule.rId)).asInstanceOf[AllRulesNode]
    allRules.edb = true
  }

  def compileIR[T](irTree: IROp)(using stagedSM: Expr[StorageManager { type EDB = T}], t: Type[T])(using ctx: InterpreterContext)(using Quotes): Expr[T] = { // TODO: Instead of parameterizing, use staged path dependent type: i.e. stagedSM.EDB
    val noop = '{$stagedSM.EDB()} // TODO: better way to noop?
    irTree match {
      case ProgramOp(body) =>
        compileIR(body)

      case DoWhileOp(body, toCmp) =>
        val cond = toCmp match {
          case DB.Derived =>
            '{ !$stagedSM.compareDerivedDBs() }
          case DB.Delta =>
            '{ $stagedSM.compareNewDeltaDBs() }
        }
        '{
          while ( {
            ${compileIR(DebugNode("Start iteration, debug node", () => ""))}
            ${compileIR(body)};
            $cond;
          }) ()
          $noop
        }

      case SwapOp() =>
        '{ $stagedSM.swapKnowledge(); $noop }

      case SequenceOp(ops) =>
        val cOps = ops.map(compileIR)
        cOps.reduceLeft((acc, next) => // TODO[future]: make a block w reflection instead of reduceLeft for efficiency
          '{ $acc; $next }
        )

      case ClearOp() =>
        '{ $stagedSM.clearNewDB(${Expr(true)}); $noop }

      case ScanOp(rId, db, knowledge) =>
        lazy val edb =
          if (storageManager.edbs.contains(rId))
            '{ Some($stagedSM.edbs(${Expr(rId)})) }
          else
            '{ Some($stagedSM.EDB()) }

        db match { // TODO[future]: Since edb is accessed upon first iteration, potentially optimize away getOrElse
          case DB.Derived =>
            knowledge match {
              case KNOWLEDGE.New =>
                '{ $stagedSM.getNewDerivedDB(${Expr(rId)}, $edb) }
              case KNOWLEDGE.Known =>
                '{ $stagedSM.getKnownDerivedDB(${Expr(rId)}, $edb) }
            }
          case DB.Delta =>
            knowledge match {
              case KNOWLEDGE.New =>
                '{ $stagedSM.getNewDeltaDB(${Expr(rId)}, $edb) }
              case KNOWLEDGE.Known =>
                '{ $stagedSM.getKnownDeltaDB(${Expr(rId)}, $edb) }
            }
        }

      case ScanEDBOp(rId) =>
        if (storageManager.edbs.contains(rId))
          '{ $stagedSM.edbs(${Expr(rId)}) }
        else
          '{ $stagedSM.EDB() }

      case JoinOp(subOps, keys) =>
        val compiledOps = Expr.ofSeq(subOps.map(compileIR))
        // TODO[future]: inspect keys and optimize join algo
        '{
          $stagedSM.joinHelper(
            $compiledOps,
            ${Expr(keys)}
          )
        }

      case ProjectOp(subOp, keys) =>
        '{ $stagedSM.projectHelper(${compileIR(subOp)}, ${Expr(keys)}) }

      case InsertOp(rId, db, knowledge, subOp, subOp2) =>  // TODO: need to cast Expr[Any] to Expr[EDB] pass to resetDerived
        val res = compileIR(subOp)
        val res2 = if (subOp2.isEmpty) '{ $stagedSM.EDB() } else compileIR(subOp2.get)
        db match {
          case DB.Derived =>
            knowledge match {
              case KNOWLEDGE.New =>
                '{ $stagedSM.resetNewDerived(${Expr(rId)}, $res, $res2); $noop }
              case KNOWLEDGE.Known =>
                '{ $stagedSM.resetKnownDerived(${Expr(rId)}, $res, $res2); $noop }
            }
          case DB.Delta =>
            knowledge match {
              case KNOWLEDGE.New =>
                '{ $stagedSM.resetNewDelta(${Expr(rId)}, $res); $noop }
              case KNOWLEDGE.Known =>
                '{ $stagedSM.resetKnownDelta(${Expr(rId)}, $res); $noop }
            }
        }

      case UnionOp(ops) =>
        val compiledOps = Expr.ofSeq(ops.map(compileIR))
        '{ $stagedSM.union($compiledOps) }

      case DiffOp(lhs, rhs) =>
        val clhs = compileIR(lhs)
        val crhs = compileIR(rhs)
        '{ $stagedSM.diff($clhs, $crhs) } // TODO: need to cast Expr[Any] to Expr[EDB] in order to diff

      case DebugNode(prefix, msg) =>
        '{ debug(${Expr(prefix)}, () => $stagedSM.printer.toString()); $noop }

      case DebugPeek(prefix, msg, op) =>
        val res = compileIR(op)
          '{ debug(${Expr(prefix)}, () => s"${${Expr(msg())}}"); $res }
      case _ => throw new Exception("Not implemented yet")
    }
  }

  def interpretIR(irTree: IROp)(using ctx: InterpreterContext): Any = {
    irTree match {
      case ProgramOp(body) =>
        debug(s"precedence graph=", ctx.precedenceGraph.sortedString)
        debug(s"solving relation: ${storageManager.ns(ctx.toSolve)} order of relations=", ctx.relations.toString)
        debug("initial state @ -1", storageManager.printer.toString)
        interpretIR(body)

      case DoWhileOp(body, toCmp) =>
        while({
          interpretIR(body)
          ctx.count += 1
          toCmp match {
            case DB.Derived =>
              !storageManager.compareDerivedDBs()
            case DB.Delta =>
              storageManager.compareNewDeltaDBs()
          }
        }) ()

      case SwapOp() =>
       storageManager.swapKnowledge()

      case SequenceOp(ops) =>
        ops.map(interpretIR)

      case ClearOp() =>
        storageManager.clearNewDB(true)

      case ScanOp(rId, db, knowledge) =>
        db match {
          case DB.Derived =>
            knowledge match {
              case KNOWLEDGE.Known =>
                storageManager.getKnownDerivedDB(rId, Some(storageManager.edbs.getOrElse(rId, EDB())))
              case KNOWLEDGE.New =>
                storageManager.getNewDerivedDB(rId, Some(storageManager.edbs.getOrElse(rId, EDB())))
            }
          case DB.Delta =>
            knowledge match {
              case KNOWLEDGE.Known =>
                storageManager.getKnownDeltaDB(rId, Some(storageManager.edbs.getOrElse(rId, EDB())))
              case KNOWLEDGE.New =>
                storageManager.getNewDeltaDB(rId, Some(storageManager.edbs.getOrElse(rId, EDB())))
            }
        }

      case ScanEDBOp(rId) =>
        storageManager.edbs.getOrElse(rId, EDB())

      case JoinOp(subOps, keys) =>
        storageManager.joinHelper(
          subOps.map(interpretIR).asInstanceOf[Seq[EDB]],
          keys
        )

      case ProjectOp(subOp, keys) =>
        storageManager.projectHelper(interpretIR(subOp).asInstanceOf[EDB], keys)

      case InsertOp(rId, db, knowledge, subOp, subOp2) =>
        val res = interpretIR(subOp)
        val res2 = if (subOp2.isEmpty) EDB() else interpretIR(subOp2.get)
        db match {
          case DB.Derived =>
            knowledge match {
              case KNOWLEDGE.Known =>
                storageManager.resetKnownDerived(rId, res.asInstanceOf[EDB], res2.asInstanceOf[EDB])
              case KNOWLEDGE.New =>
                storageManager.resetNewDerived(rId, res.asInstanceOf[EDB], res2.asInstanceOf[EDB])
            }
          case DB.Delta =>
            knowledge match {
              case KNOWLEDGE.Known =>
                storageManager.resetKnownDelta(rId, res.asInstanceOf[EDB])
              case KNOWLEDGE.New =>
                storageManager.resetNewDelta(rId, res.asInstanceOf[EDB])
            }
        }

      case UnionOp(ops) =>
        ops.flatMap(o => interpretIR(o).asInstanceOf[EDB]).toSet.toBuffer

      case DiffOp(lhs, rhs) =>
        storageManager.diff(interpretIR(lhs).asInstanceOf[EDB], interpretIR(rhs).asInstanceOf[EDB])

      case DebugNode(prefix, msg) => debug(prefix, msg)

      case DebugPeek(prefix, msg, op) =>
        val res = interpretIR(op)
        debug(prefix, () => s"${msg()} ${storageManager.printer.factToString(res.asInstanceOf[EDB])}")
        res
    }
  }

  override def solve(rId: Int): Set[Seq[Term]] = {
    // verify setup
    storageManager.verifyEDBs(precedenceGraph.idbs)
    if (storageManager.edbs.contains(rId) && !precedenceGraph.idbs.contains(rId)) { // if just an edb predicate then return
      debug("Returning EDB without any IDB rule: ", () => storageManager.ns(rId))
      return storageManager.getEDBResult(rId)
    }
    if (!precedenceGraph.idbs.contains(rId)) {
      throw new Error("Solving for rule without body")
    }
    val transformedAST = transforms.foldLeft(ast: ASTNode)((t, pass) => pass.transform(t))

    var toSolve = rId
    if (tCtx.aliases.contains(rId))
      toSolve = tCtx.aliases.getOrElse(rId, rId)
      debug("aliased:", () => s"${storageManager.ns(rId)} => ${storageManager.ns(toSolve)}")
      if (storageManager.edbs.contains(toSolve) && !precedenceGraph.idbs.contains(toSolve)) { // if just an edb predicate then return
        debug("Returning EDB as IDB aliased to EDB: ", () => storageManager.ns(toSolve))
        return storageManager.getEDBResult(toSolve)
      }

    debug("AST: ", () => storageManager.printer.printAST(ast))
    debug("TRANSFORMED: ", () => storageManager.printer.printAST(transformedAST))

    given irCtx: InterpreterContext = InterpreterContext(storageManager, precedenceGraph, toSolve)
    val irTree = createIR(transformedAST)

    debug("PROGRAM:\n", () => storageManager.printer.printIR(irTree))

    given staging.Compiler = staging.Compiler.make(getClass.getClassLoader)
    val compiled: CollectionsStorageManager => storageManager.EDB =
      staging.run {
        val res: Expr[CollectionsStorageManager => Any] =
          '{ (stagedSm: CollectionsStorageManager) => ${compileIR[CollectionsStorageManager#EDB](irTree)(using 'stagedSm)} }
        println(res.show)
        res
      }.asInstanceOf[CollectionsStorageManager => storageManager.EDB]
    compiled(storageManager.asInstanceOf[CollectionsStorageManager]) // TODO: remove cast

    debug(s"final state @${irCtx.count}", storageManager.printer.toString)
    storageManager.getNewIDBResult(toSolve)
  }
}
