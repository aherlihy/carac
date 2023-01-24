package datalog.execution.ir

import datalog.execution.{JoinIndexes, PrecedenceGraph, ir}
import datalog.execution.ast.*
import datalog.storage.{StorageManager, RelationId, DB, KNOWLEDGE}
import datalog.tools.Debug
import datalog.tools.Debug.debug

import scala.collection.mutable

/**
 * Intermediate representation based on Souffle's RAM
 */
abstract class IROp() {}

case class ProgramOp(body: IROp) extends IROp {}

case class SwapOp() extends IROp {}

case class DoWhileOp(body: IROp, toCmp: DB) extends IROp {}

case class SequenceOp(ops: Seq[IROp]) extends IROp {}

case class ClearOp() extends IROp {}

case class ScanOp(rId: RelationId, db: DB, knowledge: KNOWLEDGE) extends IROp {}

case class ScanEDBOp(rId: RelationId) extends IROp {}

case class ProjectOp(op: IROp, keys: JoinIndexes) extends IROp {}

case class JoinOp(ops: Seq[IROp], keys: JoinIndexes) extends IROp {} // needed?

case class InsertOp(rId: RelationId, db: DB, knowledge: KNOWLEDGE, value: IROp, value2: Option[IROp] = None) extends IROp {}

case class UnionOp(ops: Seq[IROp]) extends IROp {}

case class DiffOp(lhs: IROp, rhs: IROp) extends IROp {}

case class DebugNode(prefix: String, debug: () => String) extends IROp {}
case class DebugPeek(prefix: String, debug: () => String, op: IROp) extends IROp {}
