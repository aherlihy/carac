package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.ast.*
import datalog.execution.ast.transform.{CopyEliminationPass, JoinIndexPass, Transformer, ASTTransformerContext}
import datalog.execution.ir.*
import datalog.storage.{SimpleStorageManager, StorageManager}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

abstract class StagedExecutionEngine(val storageManager: StorageManager) extends ExecutionEngine {
  import storageManager.EDB
  val precedenceGraph = new PrecedenceGraph(using storageManager.ns)
  val prebuiltOpKeys: mutable.Map[Int, ArrayBuffer[JoinIndexes]] = mutable.Map[Int, mutable.ArrayBuffer[JoinIndexes]]()
  val ast: ProgramNode = ProgramNode()
  private var knownDbId = -1
  private val tCtx = ASTTransformerContext(using precedenceGraph)
  private val transforms: Seq[Transformer] = Seq(CopyEliminationPass()(using tCtx), JoinIndexPass()(using tCtx))

  def createIR(irTree: IRTree, ast: ASTNode): IROp

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
    val allRules = ast.rules.getOrElseUpdate(rId, AllRulesNode(ArrayBuffer.empty, rId)).asInstanceOf[AllRulesNode]

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
    val allRules = ast.rules.getOrElseUpdate(rule.rId, AllRulesNode(ArrayBuffer.empty, rule.rId)).asInstanceOf[AllRulesNode]
    allRules.edb = true
  }

  def interpretIR(irTree: IROp): Any = {
    irTree match {
      case ProgramOp(body) =>
        debug(s"precedence graph=", irTree.ctx.precedenceGraph.sortedString)
        debug(s"solving relation: ${storageManager.ns(irTree.ctx.toSolve)} order of relations=", irTree.ctx.relations.toString)
        debug("initial state @ -1", storageManager.printer.toString)
        interpretIR(body)
      case DoWhileOp(body, cond) =>
        while({
          debug(s"initial state @ ${irTree.ctx.count}", storageManager.printer.toString)
          interpretIR(body)
          irTree.ctx.count += 1
          interpretIR(cond).asInstanceOf[Boolean]
        }) ()
      case SwapOp() =>
        val t = irTree.ctx.knownDbId
        irTree.ctx.knownDbId = irTree.ctx.newDbId
        irTree.ctx.newDbId = t
        storageManager.printer.known = irTree.ctx.knownDbId
      case SequenceOp(ops) =>
        ops.map(interpretIR)
      case ClearOp() =>
        storageManager.clearDB(true, irTree.ctx.newDbId)
      case CompareOp(db: DB) =>
        db match {
          case DB.Derived =>
            !storageManager.compareDerivedDBs(irTree.ctx.newDbId, irTree.ctx.knownDbId)
          case DB.Delta =>
            storageManager.deltaDB(irTree.ctx.newDbId).exists((k, v) => v.nonEmpty)
        }
      case ScanOp(rId, db, knowledge) =>
        val k = if (knowledge == KNOWLEDGE.Known) irTree.ctx.knownDbId else irTree.ctx.newDbId
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
      case InsertOp(rId, db, knowledge, subOp, clear) =>
        val k = if (knowledge == KNOWLEDGE.Known) irTree.ctx.knownDbId else irTree.ctx.newDbId
        val res = interpretIR(subOp)
        db match {
          case DB.Derived =>
            storageManager.resetDerived(rId, k, res.asInstanceOf[EDB])
          case DB.Delta =>
            storageManager.resetDelta(rId, k, res.asInstanceOf[EDB])
        }
      case UnionOp(ops) =>
        ops.flatMap(o => interpretIR(o).asInstanceOf[EDB]).toSet.toBuffer
      case DiffOp(lhs, rhs) =>
        interpretIR(lhs).asInstanceOf[EDB] diff interpretIR(rhs).asInstanceOf[EDB]
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
    val transformedAST = transforms.foldLeft(ast.asInstanceOf[ASTNode])((t, pass) => pass.transform(t)) // TODO: need cast?

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

    val irCtx = InterpreterContext(storageManager, precedenceGraph, toSolve)
    val irTree = createIR(IRTree(using irCtx), transformedAST)

    debug("PROGRAM:\n", () => storageManager.printer.printIR(irTree))

    interpretIR(irTree)

    knownDbId = irCtx.knownDbId
    storageManager.getIDBResult(toSolve, irCtx.knownDbId)
  }
}
