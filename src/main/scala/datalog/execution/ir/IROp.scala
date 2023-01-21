package datalog.execution.ir

import datalog.execution.{JoinIndexes, PrecedenceGraph, ir}
import datalog.execution.ast.*
import datalog.storage.StorageManager
import datalog.tools.Debug
import datalog.tools.Debug.debug

import scala.collection.mutable

type Relation = Int
enum DB:
  case Derived, Delta
enum KNOWLEDGE:
  case New, Known

/**
 * Intermediate representation based on Souffle's RAM
 */
abstract class IROp() {}

case class ProgramOp(body: IROp) extends IROp {}

case class SwapOp() extends IROp {}

case class DoWhileOp(body: IROp, cond: IROp) extends IROp {}

case class SequenceOp(ops: Seq[IROp]) extends IROp {}

case class CompareOp(db: DB) extends IROp {}

case class ClearOp() extends IROp {}

case class ScanOp(rId: Relation, db: DB, knowledge: KNOWLEDGE) extends IROp {}

case class ScanEDBOp(rId: Relation) extends IROp {}

case class ProjectOp(op: IROp, keys: JoinIndexes) extends IROp {}

case class JoinOp(ops: Seq[IROp], keys: JoinIndexes) extends IROp {} // needed?

case class InsertOp(rId: Relation, db: DB, knowledge: KNOWLEDGE, value: IROp, value2: Option[IROp] = None) extends IROp {}

case class UnionOp(ops: Seq[IROp]) extends IROp {}

case class DiffOp(lhs: IROp, rhs: IROp) extends IROp {}

case class DebugNode(prefix: String, debug: () => String) extends IROp {}
case class DebugPeek(prefix: String, debug: () => String, op: IROp) extends IROp {}
