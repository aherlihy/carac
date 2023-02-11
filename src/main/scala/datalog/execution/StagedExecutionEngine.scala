package datalog.execution

import datalog.dsl.{Atom, Constant, MODE, Term, Variable}
import datalog.execution.ast.*
import datalog.execution.ast.transform.{ASTTransformerContext, CopyEliminationPass, JoinIndexPass, Transformer}
import datalog.execution.ir.*
import datalog.storage.{CollectionsStorageManager, DB, KNOWLEDGE, StorageManager}
import datalog.tools.Debug.debug

import java.util.concurrent.atomic.AtomicReference
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
  val compiler = StagedCompiler(staging.Compiler.make(getClass.getClassLoader), storageManager) // TODO: should this be initialized async too?


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

  def interpretIR(irTree: IROp)(using ctx: InterpreterContext): Any = {
    irTree match {
      case ProgramOp(body) =>
        interpretIR(body)

      case DoWhileOp(body, toCmp) =>
        // start compile for body
//        lazy val compiledBody: AtomicReference[CollectionsStorageManager => storageManager.EDB] = getCompiled(irTree, ctx)
        while({
          interpretIR(body)
//          ctx.count += 1
          toCmp match {
            case DB.Derived =>
              !storageManager.compareDerivedDBs()
            case DB.Delta =>
              storageManager.compareNewDeltaDBs()
          }
        }) ()

      case SwapAndClearOp() =>
        storageManager.swapKnowledge()
        storageManager.clearNewDB(true)

      case SequenceOp(ops, _) =>
        ops.map(interpretIR)

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

      case UnionOp(ops, _) =>
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

  // NOTE: this method is just for testing to see how much overhead tree processing has, not used irl.
  def generateProgramTree(rId: Int): (IROp, InterpreterContext) = {
    // verify setup
    storageManager.verifyEDBs(precedenceGraph.idbs)
    if (storageManager.edbs.contains(rId) && !precedenceGraph.idbs.contains(rId)) { // if just an edb predicate then return
      debug("Returning EDB without any IDB rule: ", () => storageManager.ns(rId))
      throw new Exception("NOTE: using generateProgramTree which is only for benchmarking")
    }
    if (!precedenceGraph.idbs.contains(rId)) {
      throw new Error("Solving for rule without body")
    }
    val transformedAST = transforms.foldLeft(ast: ASTNode)((t, pass) => pass.transform(t))

    var toSolve = rId
    if (tCtx.aliases.contains(rId))
      toSolve = tCtx.aliases.getOrElse(rId, rId)
      if (storageManager.edbs.contains(toSolve) && !precedenceGraph.idbs.contains(toSolve)) { // if just an edb predicate then return
        throw new Exception("NOTE: using generateProgramTree which is only for benchmarking")
      }
    given irCtx: InterpreterContext = InterpreterContext(storageManager, precedenceGraph, toSolve)
    val irTree = createIR(transformedAST)
    (irTree, irCtx)
  }

  def solvePreCompiled(irTree: IROp, ctx: InterpreterContext): Set[Seq[Term]] = {
    given irCtx: InterpreterContext = ctx
    val compiled = compiler.getCompiled(irTree, ctx)
    compiled(storageManager.asInstanceOf[CollectionsStorageManager]) // TODO: remove cast
    storageManager.getNewIDBResult(ctx.toSolve)
  }

  def solvePreInterpreted(irTree: IROp, ctx: InterpreterContext):  Set[Seq[Term]] = {
    given irCtx: InterpreterContext = ctx
    interpretIR(irTree)
    storageManager.getNewIDBResult(ctx.toSolve)
  }

  override def solve(rId: Int, mode: MODE): Set[Seq[Term]] = {
    // verify setup
    storageManager.verifyEDBs(precedenceGraph.idbs)
    if (storageManager.edbs.contains(rId) && !precedenceGraph.idbs.contains(rId)) { // if just an edb predicate then return
      debug("Returning EDB without any IDB rule: ", () => storageManager.ns(rId))
      return storageManager.getEDBResult(rId)
    }
    if (!precedenceGraph.idbs.contains(rId)) {
      throw new Error("Solving for rule without body")
    }

    // generate and transform tree
    val transformedAST = transforms.foldLeft(ast: ASTNode)((t, pass) => pass.transform(t))
    var toSolve = rId
    if (tCtx.aliases.contains(rId)) {
      toSolve = tCtx.aliases.getOrElse(rId, rId)
      debug("aliased:", () => s"${storageManager.ns(rId)} => ${storageManager.ns(toSolve)}")
      if (storageManager.edbs.contains(toSolve) && !precedenceGraph.idbs.contains(toSolve)) { // if just an edb predicate then return
        debug("Returning EDB as IDB aliased to EDB: ", () => storageManager.ns(toSolve))
        return storageManager.getEDBResult(toSolve)
      }
    }

    given irCtx: InterpreterContext = InterpreterContext(storageManager, precedenceGraph, toSolve)
    debug("AST: ", () => storageManager.printer.printAST(ast))
    debug("TRANSFORMED: ", () => storageManager.printer.printAST(transformedAST))
    debug("PG: ", () => irCtx.sortedRelations.toString())
    val irTree = createIR(transformedAST)
    debug("IRTree: ", () => storageManager.printer.printIR(irTree))
//    TODO: go back when done w interp
//    mode match {
//      case MODE.Compile =>
//        compileAndRun(irTree, irCtx)
//      case MODE.Interpret =>
        solvePreInterpreted(irTree, irCtx)
//      case _ => throw new Exception(s"Mode $mode not yet implemented")
//    }
  }
}
