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
abstract class IROp(using val ctx: InterpreterContext) {}

case class ProgramOp(body: IROp)(using InterpreterContext) extends IROp {}

case class SwapOp()(using InterpreterContext) extends IROp {}

case class DoWhileOp(body: IROp, cond: IROp)(using InterpreterContext) extends IROp {}

case class SequenceOp(ops: Seq[IROp])(using InterpreterContext) extends IROp {}

case class CompareOp(db: DB)(using InterpreterContext) extends IROp {}

case class ClearOp()(using InterpreterContext) extends IROp {}

case class ScanOp(rId: Relation, db: DB, knowledge: KNOWLEDGE)(using InterpreterContext) extends IROp {}
case class ScanEDBOp(rId: Relation)(using InterpreterContext) extends IROp {}

case class ProjectOp(op: IROp, keys: JoinIndexes)(using InterpreterContext) extends IROp {}

case class JoinOp(ops: Seq[IROp], keys: JoinIndexes)(using InterpreterContext) extends IROp {} // needed?

case class InsertOp(rId: Relation, db: DB, knowledge: KNOWLEDGE, value: IROp, value2: Option[IROp] = None)(using InterpreterContext) extends IROp {}

case class UnionOp(ops: Seq[IROp])(using InterpreterContext) extends IROp {}

case class DiffOp(lhs: IROp, rhs: IROp)(using InterpreterContext) extends IROp {}

case class DebugNode(prefix: String, debug: () => String)(using InterpreterContext) extends IROp {}
case class DebugPeek(prefix: String, debug: () => String, op: IROp)(using InterpreterContext) extends IROp {}
