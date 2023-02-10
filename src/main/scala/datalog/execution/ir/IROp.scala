package datalog.execution.ir

import datalog.execution.{JoinIndexes, PrecedenceGraph, ir}
import datalog.execution.ast.*
import datalog.storage.{StorageManager, RelationId, DB, KNOWLEDGE}
import datalog.tools.Debug
import datalog.tools.Debug.debug

import scala.collection.mutable

enum OpCode:
  case PROGRAM, SWAP_CLEAR, SEQ, SCAN, SCANEDB, PROJECT, JOIN, INSERT, UNION, DIFF, DEBUG, LOOP

/**
 * Intermediate representation based on Souffle's RAM
 */
abstract class IROp() {
  val code: OpCode
//  def run)(using storageManager: StorageManager): Any
}

// TODO: need IRRelOp class?

case class ProgramOp(body: IROp) extends IROp {
  val code: OpCode = OpCode.PROGRAM
  def run(bodyFn: StorageManager => Any)(using storageManager:  StorageManager): Any =
    bodyFn(storageManager)
}
case class DoWhileOp(body: IROp, toCmp: DB) extends IROp {
  val code: OpCode = OpCode.LOOP
  def run(bodyFn: StorageManager => Any)(using storageManager:  StorageManager): Any =
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
case class SequenceOp(ops: Seq[IROp]) extends IROp {
  val code: OpCode = OpCode.SEQ
  def run(opsFn: Seq[StorageManager => Any])(using storageManager:  StorageManager): Any =
    opsFn.map(o => o(storageManager))
}

// Clear only ever happens after swap so merge nodes
case class SwapAndClearOp() extends IROp {
  val code: OpCode = OpCode.SWAP_CLEAR
  def run()(using storageManager: StorageManager): Any =
    storageManager.swapKnowledge()
    storageManager.clearNewDB(true)
}

case class ScanOp(rId: RelationId, db: DB, knowledge: KNOWLEDGE) extends IROp {
  val code: OpCode = OpCode.SCAN
  def run()(using storageManager:  StorageManager): Any =
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

case class ScanEDBOp(rId: RelationId) extends IROp {
  val code: OpCode = OpCode.SCANEDB
  def run()(using storageManager:  StorageManager): Any =
    storageManager.edbs.getOrElse(rId, storageManager.EDB())
}

case class ProjectOp(subOp: IROp, keys: JoinIndexes) extends IROp {
  val code: OpCode = OpCode.PROJECT
  def run(subOpFn: StorageManager => Any)(using storageManager:  StorageManager): Any =
    storageManager.projectHelper(subOpFn(storageManager).asInstanceOf[storageManager.EDB], keys)
}

case class JoinOp(ops: Seq[IROp], keys: JoinIndexes) extends IROp {
  val code: OpCode = OpCode.JOIN
  def run(opsFn: Seq[StorageManager => Any])(using storageManager:  StorageManager): Any =
    storageManager.joinHelper(
      opsFn.map(s => s(storageManager)).asInstanceOf[Seq[storageManager.EDB]],
      keys
    )
}

case class InsertOp(rId: RelationId, db: DB, knowledge: KNOWLEDGE, subOp: IROp, subOp2: Option[IROp] = None) extends IROp {
  val code: OpCode = OpCode.INSERT
  def run(subOpFn: StorageManager => Any, subOp2Fn: Option[StorageManager => Any])(using storageManager:  StorageManager): Any =
    val res = subOpFn(storageManager)
    val res2 = if (subOp2Fn.isEmpty) storageManager.EDB() else subOp2Fn.get(storageManager)
    db match {
      case DB.Derived =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.resetKnownDerived(rId, res.asInstanceOf[storageManager.EDB], res2.asInstanceOf[storageManager.EDB])
          case KNOWLEDGE.New =>
            storageManager.resetNewDerived(rId, res.asInstanceOf[storageManager.EDB], res2.asInstanceOf[storageManager.EDB])
        }
      case DB.Delta =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.resetKnownDelta(rId, res.asInstanceOf[storageManager.EDB])
          case KNOWLEDGE.New =>
            storageManager.resetNewDelta(rId, res.asInstanceOf[storageManager.EDB])
        }
    }
}

case class UnionOp(ops: Seq[IROp]) extends IROp {
  val code: OpCode = OpCode.UNION
  def run(opsFn: Seq[StorageManager => Any])(using storageManager:  StorageManager): Any =
    opsFn.flatMap(o => o(storageManager).asInstanceOf[storageManager.EDB]).toSet.toBuffer
}

case class DiffOp(lhs: IROp, rhs: IROp) extends IROp {
  val code: OpCode = OpCode.DIFF
  def run(lhsFn: StorageManager => Any, rhsFn: StorageManager => Any)(using storageManager:  StorageManager): Any =
    storageManager.diff(lhsFn(storageManager).asInstanceOf[storageManager.EDB], rhsFn(storageManager).asInstanceOf[storageManager.EDB])
}

case class DebugNode(prefix: String, dbg: () => String) extends IROp {
  val code: OpCode = OpCode.DEBUG
  def run()(using storageManager:  StorageManager): Any =
    debug(prefix, dbg)
}
case class DebugPeek(prefix: String, dbg: () => String, op: IROp) extends IROp {
  val code: OpCode = OpCode.DEBUG
  def run(opFn: StorageManager => Any)(using storageManager:  StorageManager): Any =
    val res = opFn(storageManager)
    debug(prefix, () => s"${dbg()} ${storageManager.printer.factToString(res.asInstanceOf[storageManager.EDB])}")
    res
}
