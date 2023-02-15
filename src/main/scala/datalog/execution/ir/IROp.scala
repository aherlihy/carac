package datalog.execution.ir

import datalog.execution.{JITStagedExecutionEngine, JoinIndexes, PrecedenceGraph, StagedCompiler, ir}
import datalog.execution.ast.*
import datalog.storage.{CollectionsStorageManager, DB, KNOWLEDGE, RelationId, StorageManager}
import datalog.tools.Debug
import datalog.tools.Debug.debug

import java.util.concurrent.atomic.AtomicReference
import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.concurrent.{Future, Await}
import scala.util.{Failure, Success}

enum OpCode:
  case PROGRAM, SWAP_CLEAR, SEQ, SCAN, SCANEDB, PROJECT, JOIN, INSERT, UNION, DIFF, DEBUG, LOOP,
  EVAL_RULE_NAIVE, EVAL_RULE_SN, EVAL_NAIVE, EVAL_SN, LOOP_BODY, OTHER // convenience labels for generating functions

// TODO: make general SM not collections
type CompiledFn = CollectionsStorageManager => Any
type CompiledRelFn = CollectionsStorageManager => CollectionsStorageManager#EDB
type CompiledSnippetFn = (CollectionsStorageManager, Seq[CompiledFn]) => Any
type CompiledRelSnippetFn = (CollectionsStorageManager, Seq[CompiledRelFn]) => CollectionsStorageManager#EDB
/**
 * Intermediate representation based on Souffle's RAM
 */
abstract class IROp() {
  val code: OpCode
  var compiledFn: Future[CompiledFn] = null
  def run(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn] = Seq.empty): Any =
    throw new Exception(s"Error: calling run on likely rel op: $code")
//  var compiled: AtomicReference[CompiledFn] = new AtomicReference[CompiledFn](
//  def run(using storageManager: StorageManager): Any
  var compiledSnippetFn: CompiledSnippetFn = null
}

abstract class IRRelOp() extends IROp {
//  var compiledRel: AtomicReference[CompiledRelFn] = new AtomicReference[CompiledRelFn]()
  def runRel(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): CollectionsStorageManager#EDB
  var compiledRelSnippetFn: CompiledRelSnippetFn = null
}

case class ProgramOp(body: IROp) extends IROp {
  val code: OpCode = OpCode.PROGRAM
  override def run(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn] = Seq.empty): Any =
    opFns.head(storageManager)

  // convenience methods to get certain subtrees
  def getSubTree(code: OpCode): IROp =
    code match
      case OpCode.EVAL_NAIVE =>
        body.asInstanceOf[SequenceOp].ops.head
      case OpCode.LOOP =>
        body.asInstanceOf[SequenceOp].ops(1)
      case OpCode.LOOP_BODY =>
        body.asInstanceOf[SequenceOp].ops(1).asInstanceOf[DoWhileOp].body
      case OpCode.EVAL_SN =>
        body.asInstanceOf[SequenceOp].ops(1).asInstanceOf[DoWhileOp].body.asInstanceOf[SequenceOp].ops(1)
      case _ => // TODO: for non-unique nodes, compile with fresh dotty on separate threads?
        throw new Exception(s"getSubTree given non-unique subtree code $code")

}

case class DoWhileOp(body: IROp, toCmp: DB) extends IROp {
  val code: OpCode = OpCode.LOOP
  override def run(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn] = Seq.empty): Any =
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
}
case class SequenceOp(ops: Seq[IROp], override val code: OpCode = OpCode.SEQ) extends IROp {
  override def run(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn] = Seq.empty): Any =
    opFns.map(o => o(storageManager))
}

// Clear only ever happens after swap so merge nodes
case class SwapAndClearOp() extends IROp {
  val code: OpCode = OpCode.SWAP_CLEAR
  override def run(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn] = Seq.empty): Any =
    storageManager.swapKnowledge()
    storageManager.clearNewDB(true)
}

case class InsertOp(rId: RelationId, db: DB, knowledge: KNOWLEDGE, subOp: IRRelOp, subOp2: Option[IRRelOp] = None) extends IROp {
  val code: OpCode = OpCode.INSERT
  override def run(storageManager:  CollectionsStorageManager, opFns: Seq[CompiledFn] = Seq.empty): Any =
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
}

case class ScanOp(rId: RelationId, db: DB, knowledge: KNOWLEDGE) extends IRRelOp {
  val code: OpCode = OpCode.SCAN
//  lazy val compiledRelFn: AtomicReference[CompiledRelFn] =
//    println("in lazy val")
//    lazySet()
//    AtomicReference(run) // run

  def runRel(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): storageManager.EDB =
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

  def runRel(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): storageManager.EDB =
    storageManager.edbs.getOrElse(rId, storageManager.EDB())
}

case class ProjectOp(subOp: IRRelOp, keys: JoinIndexes) extends IRRelOp {
  val code: OpCode = OpCode.PROJECT

  def runRel(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): storageManager.EDB =
    storageManager.projectHelper(opFns.head(storageManager), keys)
}

case class JoinOp(ops: Seq[IRRelOp], keys: JoinIndexes) extends IRRelOp {
  val code: OpCode = OpCode.JOIN

  def runRel(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): storageManager.EDB =
    storageManager.joinHelper(
      opFns.map(s => s(storageManager)),
      keys
    )
}

case class UnionOp(ops: Seq[IRRelOp], override val code: OpCode = OpCode.UNION) extends IRRelOp {
  def runRel(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): storageManager.EDB =
    opFns.flatMap(o => o(storageManager)).toSet.toBuffer.asInstanceOf[storageManager.EDB]
}

case class DiffOp(lhs: IRRelOp, rhs: IRRelOp) extends IRRelOp {
  val code: OpCode = OpCode.DIFF

  def runRel(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): storageManager.EDB =
    storageManager.diff(opFns(0)(storageManager), opFns(1)(storageManager))
}

case class DebugNode(prefix: String, dbg: () => String) extends IROp {
  val code: OpCode = OpCode.DEBUG
  override def run(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn] = Seq.empty): Any =
    debug(prefix, dbg)
}
case class DebugPeek(prefix: String, dbg: () => String, op: IRRelOp) extends IRRelOp {
  val code: OpCode = OpCode.DEBUG
  def runRel(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn] = Seq.empty): storageManager.EDB =
    val res = opFns.head(storageManager)
    debug(prefix, () => s"${dbg()} ${storageManager.printer.factToString(res)}")
    res
}
