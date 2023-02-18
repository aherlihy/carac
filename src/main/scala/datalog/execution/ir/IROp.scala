package datalog.execution.ir

import datalog.execution.{JoinIndexes, PrecedenceGraph, StagedCompiler, ir}
import datalog.execution.ast.*
import datalog.storage.{CollectionsStorageManager, DB, KNOWLEDGE, RelationId, StorageManager}
import datalog.tools.Debug
import datalog.tools.Debug.debug

import java.util.concurrent.atomic.AtomicReference
import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.quoted.*
import scala.util.{Failure, Success}

enum OpCode:
  case PROGRAM, SWAP_CLEAR, SEQ, SCAN, SCANEDB, PROJECT, JOIN, INSERT, UNION, DIFF, DEBUG, DOWHILE,
  EVAL_RULE_NAIVE, EVAL_RULE_SN, EVAL_NAIVE, EVAL_SN, LOOP_BODY, OTHER // convenience labels for generating functions

// TODO: make general SM not collections
type CompiledFn = CollectionsStorageManager => Any
type CompiledRelFn = CollectionsStorageManager => CollectionsStorageManager#EDB
type CompiledSnippetContinuationFn = (CollectionsStorageManager, Seq[CompiledFn]) => Any
type CompiledRelSnippetContinuationFn = (CollectionsStorageManager, Seq[CompiledRelFn]) => CollectionsStorageManager#EDB
/**
 * Intermediate representation based on Souffle's RAM
 */
abstract class IROp() {
  val code: OpCode
  var compiledFn: Future[CompiledFn] = null
  var compiledSnippetContinuationFn: CompiledSnippetContinuationFn = null

  /**
   * Add continuation to revert control flow to the interpret method, which checks for optimizations/deoptimizations
   */
  def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn] = Seq.empty): Any =
    throw new Exception(s"Error: calling run on likely rel op with continuation: $code")

  /**
   * Keep control flow entirely within nodes, useful to minimize the size of the dotty-generated AST?
   */
  def run(storageManager: CollectionsStorageManager): Any =
    throw new Exception(s"Error: calling run on likely rel op: $code")
}

abstract class IRRelOp() extends IROp {
  var compiledRelFn: Future[CompiledRelFn] = null
  var compiledRelSnippetContinuationFn: CompiledRelSnippetContinuationFn = null
  def runRel_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): CollectionsStorageManager#EDB
  def runRel(storageManager: CollectionsStorageManager): CollectionsStorageManager#EDB
}

case class ProgramOp(body: IROp) extends IROp {
  val code: OpCode = OpCode.PROGRAM
  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn] = Seq.empty): Any =
    opFns.head(storageManager)
  override def run(storageManager: CollectionsStorageManager): Any =
    body.run(storageManager)

  // convenience methods to get certain subtrees
  def getSubTree(code: OpCode): IROp =
    code match
      case OpCode.PROGRAM =>
        this
      case OpCode.EVAL_NAIVE =>
        body.asInstanceOf[SequenceOp].ops.head
      case OpCode.DOWHILE =>
        body.asInstanceOf[SequenceOp].ops(1)
      case OpCode.LOOP_BODY =>
        getSubTree(OpCode.DOWHILE).asInstanceOf[DoWhileOp].body
      case OpCode.EVAL_SN =>
        getSubTree(OpCode.LOOP_BODY).asInstanceOf[SequenceOp].ops(1)
      case OpCode.EVAL_RULE_SN => // gets a bit weird here bc there are multiple of these nodes, just gets the first one. Including insert+diff
        getSubTree(OpCode.EVAL_SN).asInstanceOf[SequenceOp].ops.head
      case OpCode.JOIN => // technically project
        getSubTree(OpCode.EVAL_RULE_SN).asInstanceOf[ir.SequenceOp].ops.head.asInstanceOf[ir.InsertOp].subOp.asInstanceOf[ir.DiffOp].lhs.asInstanceOf[ir.UnionOp].ops.head
      case OpCode.SCAN =>
        getSubTree(OpCode.JOIN).asInstanceOf[ir.ProjectOp].subOp.asInstanceOf[ir.JoinOp].ops.head
      case _ =>
        throw new Exception(s"getSubTree not supported for $code, could prob just add it")
}

case class DoWhileOp(body: IROp, toCmp: DB) extends IROp {
  val code: OpCode = OpCode.DOWHILE
  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn] = Seq.empty): Any =
    while ( {
      opFns.head(storageManager)
//      ctx.count += 1 // TODO: do we need this outside debugging?
      toCmp match {
        case DB.Derived =>
          !storageManager.compareDerivedDBs()
        case DB.Delta =>
          storageManager.compareNewDeltaDBs()
      }
    }) ()
  override def run(storageManager: CollectionsStorageManager): Any =
    while ( {
      body.run(storageManager)
      toCmp match {
        case DB.Derived =>
          !storageManager.compareDerivedDBs()
        case DB.Delta =>
          storageManager.compareNewDeltaDBs()
      }
    }) ()
}
case class SequenceOp(ops: Seq[IROp], override val code: OpCode = OpCode.SEQ) extends IROp {
  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn] = Seq.empty): Any =
    opFns.map(o => o(storageManager))
  override def run(storageManager: CollectionsStorageManager): Any =
    ops.map(o => o.run(storageManager))
}

// Clear only ever happens after swap so merge nodes
case class SwapAndClearOp() extends IROp {
  val code: OpCode = OpCode.SWAP_CLEAR
  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn] = Seq.empty): Any =
    storageManager.swapKnowledge()
    storageManager.clearNewDB(true)
  override def run(storageManager: CollectionsStorageManager): Any =
    storageManager.swapKnowledge()
    storageManager.clearNewDB(true)
}

case class InsertOp(rId: RelationId, db: DB, knowledge: KNOWLEDGE, subOp: IRRelOp, subOp2: Option[IRRelOp] = None) extends IROp {
  val code: OpCode = OpCode.INSERT
  override def run_continuation(storageManager:  CollectionsStorageManager, opFns: Seq[CompiledFn] = Seq.empty): Any =
    val res = opFns.head.asInstanceOf[CompiledRelFn](storageManager)
    val res2 = if (opFns.size == 1) storageManager.EDB() else opFns(1).asInstanceOf[CompiledRelFn](storageManager)
    db match {
      case DB.Derived =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.resetKnownDerived(rId, res, res2)
          case KNOWLEDGE.New =>
            storageManager.resetNewDerived(rId, res, res2)
        }
      case DB.Delta =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.resetKnownDelta(rId, res)
          case KNOWLEDGE.New =>
            storageManager.resetNewDelta(rId, res)
        }
    }
  override def run(storageManager: CollectionsStorageManager): Any =
    val res = subOp.runRel(storageManager)
    val res2 = if (subOp2.isEmpty) storageManager.EDB() else subOp2.get.runRel(storageManager)
    db match {
      case DB.Derived =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.resetKnownDerived(rId, res, res2)
          case KNOWLEDGE.New =>
            storageManager.resetNewDerived(rId, res, res2)
        }
      case DB.Delta =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.resetKnownDelta(rId, res)
          case KNOWLEDGE.New =>
            storageManager.resetNewDelta(rId, res)
        }
    }
}

case class ScanOp(rId: RelationId, db: DB, knowledge: KNOWLEDGE) extends IRRelOp {
  val code: OpCode = OpCode.SCAN

  def runRel_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): storageManager.EDB =
    db match {
      case DB.Derived =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.getKnownDerivedDB(rId)
          case KNOWLEDGE.New =>
            storageManager.getNewDerivedDB(rId)
        }
      case DB.Delta =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.getKnownDeltaDB(rId)
          case KNOWLEDGE.New =>
            storageManager.getNewDeltaDB(rId)
        }
    }
  def runRel(storageManager: CollectionsStorageManager): storageManager.EDB =
//    throw new Exception()
    db match {
      case DB.Derived =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.getKnownDerivedDB(rId)
          case KNOWLEDGE.New =>
            storageManager.getNewDerivedDB(rId)
        }
      case DB.Delta =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.getKnownDeltaDB(rId)
          case KNOWLEDGE.New =>
            storageManager.getNewDeltaDB(rId)
        }
    }
}

case class ScanEDBOp(rId: RelationId) extends IRRelOp {
  val code: OpCode = OpCode.SCANEDB

  def runRel_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): storageManager.EDB =
    storageManager.edbs.getOrElse(rId, storageManager.EDB())
  def runRel(storageManager: CollectionsStorageManager): storageManager.EDB =
    storageManager.edbs.getOrElse(rId, storageManager.EDB())
}

case class ProjectOp(subOp: IRRelOp, keys: JoinIndexes) extends IRRelOp {
  val code: OpCode = OpCode.PROJECT

  def runRel_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): storageManager.EDB =
    storageManager.projectHelper(opFns.head(storageManager), keys)
  def runRel(storageManager: CollectionsStorageManager): storageManager.EDB =
    storageManager.projectHelper(subOp.runRel(storageManager), keys)
}

case class JoinOp(ops: Seq[IRRelOp], keys: JoinIndexes) extends IRRelOp {
  val code: OpCode = OpCode.JOIN

  def runRel_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): storageManager.EDB =
    storageManager.joinHelper(
      opFns.map(s => s(storageManager)),
      keys
    )
  def runRel(storageManager: CollectionsStorageManager): storageManager.EDB =
    storageManager.joinHelper(
      ops.map(s => s.runRel(storageManager)),
      keys
    )
}

case class UnionOp(ops: Seq[IRRelOp], override val code: OpCode = OpCode.UNION) extends IRRelOp {
  def runRel_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): storageManager.EDB =
//    opFns.flatMap(o => o(storageManager)).toSet.toBuffer.asInstanceOf[storageManager.EDB]
    storageManager.union(opFns.map(o => o(storageManager)))
  def runRel(storageManager: CollectionsStorageManager): storageManager.EDB =
//    ops.flatMap(o => o.runRel(storageManager)).toSet.toBuffer.asInstanceOf[storageManager.EDB]
    storageManager.union(ops.map(o => o.runRel(storageManager)))
}

case class DiffOp(lhs: IRRelOp, rhs: IRRelOp) extends IRRelOp {
  val code: OpCode = OpCode.DIFF

  def runRel_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): storageManager.EDB =
    storageManager.diff(opFns(0)(storageManager), opFns(1)(storageManager))
  def runRel(storageManager: CollectionsStorageManager): storageManager.EDB =
    storageManager.diff(lhs.runRel(storageManager), rhs.runRel(storageManager))
}

case class DebugNode(prefix: String, dbg: () => String) extends IROp {
  val code: OpCode = OpCode.DEBUG
  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn] = Seq.empty): Any =
    debug(prefix, dbg)
  override def run(storageManager: CollectionsStorageManager): Any =
    debug(prefix, dbg)
}
case class DebugPeek(prefix: String, dbg: () => String, op: IRRelOp) extends IRRelOp {
  val code: OpCode = OpCode.DEBUG
  def runRel_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): storageManager.EDB =
    val res = opFns.head(storageManager)
    debug(prefix, () => s"${dbg()} ${storageManager.printer.factToString(res)}")
    res
  def runRel(storageManager: CollectionsStorageManager): storageManager.EDB =
    val res = op.runRel(storageManager)
    debug(prefix, () => s"${dbg()} ${storageManager.printer.factToString(res)}")
    res
}
