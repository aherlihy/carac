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
}

case class ProgramOp(body: IROp) extends IROp {
  val code: OpCode = OpCode.PROGRAM
}
case class DoWhileOp(body: IROp, toCmp: DB) extends IROp {
  val code: OpCode = OpCode.LOOP
}
case class SequenceOp(ops: Seq[IROp]) extends IROp {
  val code: OpCode = OpCode.SEQ
}

// Clear only ever happens after swap so merge nodes
case class SwapAndClearOp() extends IROp {
  val code: OpCode = OpCode.SWAP_CLEAR
}

case class ScanOp(rId: RelationId, db: DB, knowledge: KNOWLEDGE) extends IROp {
  val code: OpCode = OpCode.SCAN
}

case class ScanEDBOp(rId: RelationId) extends IROp {
  val code: OpCode = OpCode.SCANEDB
}

case class ProjectOp(op: IROp, keys: JoinIndexes) extends IROp {
  val code: OpCode = OpCode.PROJECT
}

case class JoinOp(ops: Seq[IROp], keys: JoinIndexes) extends IROp {
  val code: OpCode = OpCode.JOIN
}

case class InsertOp(rId: RelationId, db: DB, knowledge: KNOWLEDGE, value: IROp, value2: Option[IROp] = None) extends IROp {
  val code: OpCode = OpCode.INSERT
}

case class UnionOp(ops: Seq[IROp]) extends IROp {
  val code: OpCode = OpCode.UNION
}

case class DiffOp(lhs: IROp, rhs: IROp) extends IROp {
  val code: OpCode = OpCode.DIFF
}

case class DebugNode(prefix: String, debug: () => String) extends IROp {
  val code: OpCode = OpCode.DEBUG
}
case class DebugPeek(prefix: String, debug: () => String, op: IROp) extends IROp {
  val code: OpCode = OpCode.DEBUG
}
