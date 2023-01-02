package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.ast.*
import datalog.execution.ast.transform.{CopyEliminationPass, JoinIndexPass, Transformer, ASTTransformerContext}
import datalog.execution.ir.*
import datalog.storage.{SimpleStorageManager, StorageManager}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class StagedExecutionEngine(val storageManager: StorageManager) extends ExecutionEngine {
  import storageManager.EDB
  val precedenceGraph = new PrecedenceGraph(storageManager.ns)
  val ast: ProgramNode = ProgramNode()
  private var knownDbId = -1
  private val tCtx = ASTTransformerContext()
  private val transforms: Seq[Transformer] = Seq(/*CopyEliminationPass()(using tCtx),*/ JoinIndexPass()(using tCtx))

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
    precedenceGraph.addNode(rule)
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
          }))
      ))
  }

  def insertEDB(rule: Atom): Unit = {
    storageManager.insertEDB(rule)
    val allRules = ast.rules.getOrElseUpdate(rule.rId, AllRulesNode(ArrayBuffer.empty, rule.rId)).asInstanceOf[AllRulesNode]
    allRules.edb = true
  }

  def naiveInterpretIR(irTree: IROp): Any = {
    irTree match {
      case ProgramOp(body) =>
        naiveInterpretIR(body)
        debug(s"solving relation: ${storageManager.ns(irTree.ctx.toSolve)} order of relations=", irTree.ctx.relations.toString)
      case DoWhileOp(body, cond) =>
        while({
          naiveInterpretIR(body)
          irTree.ctx.count += 1
          naiveInterpretIR(cond).asInstanceOf[Boolean]
        }) ()
      case SwapOp() =>
        val t = irTree.ctx.knownDbId
        irTree.ctx.knownDbId = irTree.ctx.newDbId
        irTree.ctx.newDbId = t
        storageManager.printer.known = irTree.ctx.knownDbId
      case SequenceOp(ops) =>
        ops.map(naiveInterpretIR)
      case ClearOp() =>
        storageManager.clearDB(true, irTree.ctx.newDbId)
        debug(s"initial state @ ${irTree.ctx.count}", storageManager.printer.toString)
      case DiffOp() =>
        !storageManager.compareDerivedDBs(irTree.ctx.newDbId, irTree.ctx.knownDbId)
      case FilterOp(rId, keys) =>
        if (keys.edb)
          storageManager.edbs.getOrElse(rId, EDB())
        else
          storageManager.derivedDB(irTree.ctx.knownDbId).getOrElse(rId, storageManager.edbs.getOrElse(rId, EDB()))
      case JoinOp(subOps, keys) =>
        storageManager.joinHelper(
          subOps.map(naiveInterpretIR).asInstanceOf[Seq[EDB]],
          keys
        )
      case ProjectOp(subOp, keys) =>
        naiveInterpretIR(subOp).asInstanceOf[EDB].map(t =>
          keys.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }
          )
        )
      case InsertOp(rId, subOp) =>
        debug("in eval: ", () => s"rId=${storageManager.ns(rId)} relations=${irTree.ctx.relations.map(r => storageManager.ns(r)).mkString("[", ", ", "]")}  incr=${irTree.ctx.newDbId} src=${irTree.ctx.knownDbId}")
        val res = naiveInterpretIR(subOp)
        debug("result of evalRule=", () => storageManager.printer.factToString(res.asInstanceOf[EDB]))
        storageManager.resetDerived(rId, irTree.ctx.newDbId, res.asInstanceOf[EDB])
      case UnionOp(ops) =>
        ops.flatMap(o => naiveInterpretIR(o).asInstanceOf[EDB]).toSet.toBuffer
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
    val irTree = IRTree(using irCtx).initialize(transformedAST)

    debug("PROGRAM:\n", () => storageManager.printer.printIR(irTree))

    naiveInterpretIR(irTree)

    knownDbId = irCtx.knownDbId
    storageManager.getIDBResult(toSolve, irCtx.knownDbId)
  }
}
