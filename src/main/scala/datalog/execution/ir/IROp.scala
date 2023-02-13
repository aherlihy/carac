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
  case PROGRAM, SWAP_CLEAR, SEQ, SCAN, SCANEDB, PROJECT, JOIN, INSERT, UNION, DIFF, DEBUG, LOOP
enum FnLabel: // convenience labels for generating functions
  case EVAL_RULE_NAIVE, EVAL_RULE_SN, EVAL_NAIVE, EVAL_SN, LOOP_BODY, OTHER // TBD if more needed

// TODO: make general SM not collections
type CompiledFn = CollectionsStorageManager => Any
type CompiledRelFn = CollectionsStorageManager => CollectionsStorageManager#EDB
/**
 * Intermediate representation based on Souffle's RAM
 */
abstract class IROp() {
  val code: OpCode
  val fnLabel: FnLabel = FnLabel.OTHER
//  var compiled: AtomicReference[CompiledFn] = new AtomicReference[CompiledFn](
//  def run(using storageManager: StorageManager): Any
}

abstract class IRRelOp() extends IROp {
//  var compiledRel: AtomicReference[CompiledRelFn] = new AtomicReference[CompiledRelFn]()
}

case class ProgramOp(body: IROp) extends IROp {
  val code: OpCode = OpCode.PROGRAM
  def run(bodyFn: CompiledFn)(using storageManager:  CollectionsStorageManager): Any =
    bodyFn(storageManager)
}

case class DoWhileOp(body: IROp, toCmp: DB) extends IROp {
  val code: OpCode = OpCode.LOOP
  def run(bodyFn: CompiledFn)(using storageManager:  CollectionsStorageManager): Any =
    while ( {
      bodyFn(storageManager)
//      ctx.count += 1 // TODO: do we need this outside debugging?
      toCmp match {
        case DB.Derived =>
          !storageManager.compareDerivedDBs()
        case DB.Delta =>
          storageManager.compareNewDeltaDBs()
      }
    }) ()
}
case class SequenceOp(ops: Seq[IROp], override val fnLabel: FnLabel = FnLabel.OTHER) extends IROp {
  val code: OpCode = OpCode.SEQ
  var compiledFn: Future[CompiledFn] = null
  def run(opsFn: Seq[CompiledFn])(using storageManager:  CollectionsStorageManager): Any =
    opsFn.map(o => o(storageManager))
}

// Clear only ever happens after swap so merge nodes
case class SwapAndClearOp() extends IROp {
  val code: OpCode = OpCode.SWAP_CLEAR
  def run()(using storageManager: StorageManager): Any =
    storageManager.swapKnowledge()
    storageManager.clearNewDB(true)
}

case class InsertOp(rId: RelationId, db: DB, knowledge: KNOWLEDGE, subOp: IRRelOp, subOp2: Option[IRRelOp] = None) extends IROp {
  val code: OpCode = OpCode.INSERT
  def run(subOpFn: CompiledRelFn, subOp2Fn: Option[CompiledRelFn])(using storageManager:  CollectionsStorageManager): Any =
    val res = subOpFn(storageManager)
    val res2 = if (subOp2Fn.isEmpty) storageManager.EDB() else subOp2Fn.get(storageManager)
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

  def run(storageManager: CollectionsStorageManager): storageManager.EDB =
    db match {
      case DB.Derived =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.getKnownDerivedDB(rId, Some(storageManager.edbs.getOrElse(rId, storageManager.EDB())))
          case KNOWLEDGE.New =>
            storageManager.getNewDerivedDB(rId, Some(storageManager.edbs.getOrElse(rId, storageManager.EDB())))
        }
      case DB.Delta =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.getKnownDeltaDB(rId, Some(storageManager.edbs.getOrElse(rId, storageManager.EDB())))
          case KNOWLEDGE.New =>
            storageManager.getNewDeltaDB(rId, Some(storageManager.edbs.getOrElse(rId, storageManager.EDB())))
        }
    }
}

case class ScanEDBOp(rId: RelationId) extends IRRelOp {
  val code: OpCode = OpCode.SCANEDB

  def run()(using storageManager: CollectionsStorageManager): storageManager.EDB =
    storageManager.edbs.getOrElse(rId, storageManager.EDB())
}

case class ProjectOp(subOp: IRRelOp, keys: JoinIndexes) extends IRRelOp {
  val code: OpCode = OpCode.PROJECT

  def run(subOpFn: CompiledRelFn)(using storageManager: CollectionsStorageManager): storageManager.EDB =
    storageManager.projectHelper(subOpFn(storageManager), keys)
}

case class JoinOp(ops: Seq[IRRelOp], keys: JoinIndexes) extends IRRelOp {
  val code: OpCode = OpCode.JOIN

  def run(opsFn: Seq[CompiledRelFn])(using storageManager: CollectionsStorageManager): storageManager.EDB =
    storageManager.joinHelper(
      opsFn.map(s => s(storageManager)),
      keys
    )
}

case class UnionOp(ops: Seq[IRRelOp], override val fnLabel: FnLabel = FnLabel.OTHER) extends IRRelOp {
  val code: OpCode = OpCode.UNION
  def run(opsFn: Seq[CompiledRelFn])(using storageManager:  CollectionsStorageManager): storageManager.EDB =
    opsFn.flatMap(o => o(storageManager)).toSet.toBuffer.asInstanceOf[storageManager.EDB]
}

case class DiffOp(lhs: IRRelOp, rhs: IRRelOp) extends IRRelOp {
  val code: OpCode = OpCode.DIFF

  def run(lhsFn: CompiledRelFn, rhsFn: CompiledRelFn)(using storageManager: CollectionsStorageManager): storageManager.EDB =
    storageManager.diff(lhsFn(storageManager), rhsFn(storageManager))
}

case class DebugNode(prefix: String, dbg: () => String) extends IROp {
  val code: OpCode = OpCode.DEBUG
  def run()(using storageManager:  CollectionsStorageManager): Any =
    debug(prefix, dbg)
}
case class DebugPeek(prefix: String, dbg: () => String, op: IRRelOp) extends IRRelOp {
  val code: OpCode = OpCode.DEBUG
  def run(opFn: CompiledRelFn)(using storageManager:  CollectionsStorageManager): storageManager.EDB =
    val res = opFn(storageManager)
    debug(prefix, () => s"${dbg()} ${storageManager.printer.factToString(res)}")
    res
}
