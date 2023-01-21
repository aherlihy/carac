package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.ast.*
import datalog.execution.ast.transform.{CopyEliminationPass, JoinIndexPass, Transformer, ASTTransformerContext}
import datalog.execution.ir.*
import datalog.storage.{SimpleStorageManager, StorageManager}
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

  def createIR(ast: ASTNode)(using InterpreterContext): IROp

  def initRelation(rId: Int, name: String): Unit = {
    storageManager.ns(rId) = name
    storageManager.initRelation(rId, name)
  }

  def get(rId: Int): Set[Seq[Term]] = {
    if (knownDbId == -1)
      throw new Exception("Solve() has not yet been called")
    if (precedenceGraph.idbs.contains(rId))
      storageManager.getIDBResult(rId, knownDbId)
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

  def compileIR(irTree: IROp)(using stagedSM: Expr[StorageManager])(using ctx: InterpreterContext)(using Quotes): Expr[Any] = {
    irTree match {
      case ProgramOp(body) =>
        debug(s"precedence graph=", ctx.precedenceGraph.sortedString)
        debug(s"solving relation: ${storageManager.ns(ctx.toSolve)} order of relations=", ctx.relations.toString)
        debug("initial state @ -1", storageManager.printer.toString)
        compileIR(body)
//      case DoWhileOp(body, cond) =>
//        '{
//          while ( {
//            ${interpretIR(body)}
//            ctx.count += 1
//            interpretIR(cond).asInstanceOf[Boolean]
//          }) ()
//        }
//      case SwapOp() =>
//        val t = ctx.knownDbId
//        ctx.knownDbId = ctx.newDbId
//        ctx.newDbId = t
//        storageManager.printer.known = ctx.knownDbId
//      case SequenceOp(ops) =>
//        ops.map(interpretIR)
//      case ClearOp() =>
//        storageManager.clearDB(true, ctx.newDbId)
//      case CompareOp(db: DB) =>
//        db match {
//          case DB.Derived =>
//            !storageManager.compareDerivedDBs(ctx.newDbId, ctx.knownDbId)
//          case DB.Delta =>
//            storageManager.deltaDB(ctx.newDbId).exists((k, v) => v.nonEmpty)
//        }
      case ScanOp(rId, db, knowledge) =>
        val k = if (knowledge == KNOWLEDGE.Known) ctx.knownDbId else ctx.newDbId
        lazy val edb =
          if (storageManager.edbs.contains(rId))
            '{ ${stagedSM}.edbs(${Expr(rId)}) }
          else
            '{ ${stagedSM}.EDB() }

        db match {
          case DB.Derived =>
            if (storageManager.derivedDB.contains(rId))
              '{ ${stagedSM}.derivedDB(${Expr(k)}) }
            else
              edb
          case DB.Delta =>
            if (storageManager.deltaDB.contains(rId))
              '{ ${stagedSM}.deltaDB(${Expr(k)}) }
            else
              edb
        }
      case ScanEDBOp(rId) =>
        if (storageManager.edbs.contains(rId))
          '{ ${stagedSM}.edbs(${Expr(rId)}) }
        else
          '{ ${stagedSM}.EDB() }
//      case JoinOp(subOps, keys) =>
//        storageManager.joinHelper(
//          subOps.map(interpretIR).asInstanceOf[Seq[EDB]],
//          keys
//        )
//      case ProjectOp(subOp, keys) =>
//        interpretIR(subOp).asInstanceOf[EDB].map(t =>
//          keys.projIndexes.flatMap((typ, idx) =>
//            typ match {
//              case "v" => t.lift(idx.asInstanceOf[Int])
//              case "c" => Some(idx)
//              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
//            }
//          )
//        )
//      case InsertOp(rId, db, knowledge, subOp, subOp2) =>
//        val k = if (knowledge == KNOWLEDGE.Known) ctx.knownDbId else ctx.newDbId
//        val res = interpretIR(subOp)
//        val res2 = if (subOp2.isEmpty) EDB() else interpretIR(subOp2.get)
//        db match {
//          case DB.Derived =>
//            storageManager.resetDerived(rId, k, res.asInstanceOf[EDB], res2.asInstanceOf[EDB])
//          case DB.Delta =>
//            storageManager.resetDelta(rId, k, res.asInstanceOf[EDB])
//        }
//      case UnionOp(ops) =>
//        ops.flatMap(o => interpretIR(o).asInstanceOf[EDB]).toSet.toBuffer
//      case DiffOp(lhs, rhs) =>
//        interpretIR(lhs).asInstanceOf[EDB] diff interpretIR(rhs).asInstanceOf[EDB]
//      case DebugNode(prefix, msg) => debug(prefix, msg)
//      case DebugPeek(prefix, msg, op) =>
//        val res = interpretIR(op)
//        debug(prefix, () => s"${msg()} ${storageManager.printer.factToString(res.asInstanceOf[EDB])}")
//        res
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
      case DoWhileOp(body, cond) =>
        while({
          interpretIR(body)
          ctx.count += 1
          interpretIR(cond).asInstanceOf[Boolean]
        }) ()
      case SwapOp() =>
        val t = ctx.knownDbId
        ctx.knownDbId = ctx.newDbId
        ctx.newDbId = t
        storageManager.printer.known = ctx.knownDbId
      case SequenceOp(ops) =>
        ops.map(interpretIR)
      case ClearOp() =>
        storageManager.clearDB(true, ctx.newDbId)
      case CompareOp(db: DB) =>
        db match {
          case DB.Derived =>
            !storageManager.compareDerivedDBs(ctx.newDbId, ctx.knownDbId)
          case DB.Delta =>
            storageManager.deltaDB(ctx.newDbId).exists((k, v) => v.nonEmpty)
        }
      case ScanOp(rId, db, knowledge) =>
        val k = if (knowledge == KNOWLEDGE.Known) ctx.knownDbId else ctx.newDbId
        db match {
          case DB.Derived =>
            storageManager.derivedDB(k).getOrElse(rId, storageManager.edbs.getOrElse(rId, EDB()))
          case DB.Delta =>
            storageManager.deltaDB(k).getOrElse(rId, storageManager.edbs.getOrElse(rId, EDB()))
        }
      case ScanEDBOp(rId) =>
        storageManager.edbs.getOrElse(rId, EDB())
      case JoinOp(subOps, keys) =>
        storageManager.joinHelper(
          subOps.map(interpretIR).asInstanceOf[Seq[EDB]],
          keys
        )
      case ProjectOp(subOp, keys) =>
        interpretIR(subOp).asInstanceOf[EDB].map(t =>
          keys.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }
          )
        )
      case InsertOp(rId, db, knowledge, subOp, subOp2) =>
        val k = if (knowledge == KNOWLEDGE.Known) ctx.knownDbId else ctx.newDbId
        val res = interpretIR(subOp)
        val res2 = if (subOp2.isEmpty) EDB() else interpretIR(subOp2.get)
        db match {
          case DB.Derived =>
            storageManager.resetDerived(rId, k, res.asInstanceOf[EDB], res2.asInstanceOf[EDB])
          case DB.Delta =>
            storageManager.resetDelta(rId, k, res.asInstanceOf[EDB])
        }
      case UnionOp(ops) =>
        ops.flatMap(o => interpretIR(o).asInstanceOf[EDB]).toSet.toBuffer
      case DiffOp(lhs, rhs) =>
        interpretIR(lhs).asInstanceOf[EDB] diff interpretIR(rhs).asInstanceOf[EDB]
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
//    if (storageManager.edbs.contains(rId) && !precedenceGraph.idbs.contains(rId)) { // if just an edb predicate then return
//      debug("Returning EDB without any IDB rule: ", () => storageManager.ns(rId))
//      return storageManager.getEDBResult(rId)
//    }
//    if (!precedenceGraph.idbs.contains(rId)) {
//      throw new Error("Solving for rule without body")
//    }
    val transformedAST = transforms.foldLeft(ast: ASTNode)((t, pass) => pass.transform(t))

    var toSolve = rId
//    if (tCtx.aliases.contains(rId))
//      toSolve = tCtx.aliases.getOrElse(rId, rId)
//      debug("aliased:", () => s"${storageManager.ns(rId)} => ${storageManager.ns(toSolve)}")
//      if (storageManager.edbs.contains(toSolve) && !precedenceGraph.idbs.contains(toSolve)) { // if just an edb predicate then return
//        debug("Returning EDB as IDB aliased to EDB: ", () => storageManager.ns(toSolve))
//        return storageManager.getEDBResult(toSolve)
//      }

    debug("AST: ", () => storageManager.printer.printAST(ast))
    debug("TRANSFORMED: ", () => storageManager.printer.printAST(transformedAST))

    given irCtx: InterpreterContext = InterpreterContext(storageManager, precedenceGraph, toSolve)
    val irTree = createIR(transformedAST)

//    debug("PROGRAM:\n", () => storageManager.printer.printIR(irTree))

    val miniprog = ScanEDBOp(rId)
    debug("MINI PROG\n", () => storageManager.printer.printIR(miniprog))

    given staging.Compiler = staging.Compiler.make(getClass.getClassLoader)
    val compiled: StorageManager => EDB =
      staging.run {
        val res: Expr[StorageManager => Any] =
          '{ (stagedSm: StorageManager) => ${compileIR(miniprog)(using 'stagedSm)} }
        println(res.show)
        res
      }.asInstanceOf[StorageManager => EDB]

    val res = compiled(storageManager)
//    interpretIR(irTree)

//    knownDbId = irCtx.newDbId
//    debug(s"final state @${irCtx.count} res@${irCtx.newDbId}", storageManager.printer.toString)
//    storageManager.getIDBResult(toSolve, irCtx.newDbId)
    res.toSet.asInstanceOf[Set[Seq[Term]]]
  }
}
