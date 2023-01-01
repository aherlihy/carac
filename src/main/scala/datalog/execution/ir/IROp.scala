package datalog.execution.ir

import datalog.execution.{JoinIndexes, PrecedenceGraph}
import datalog.execution.ast.*
import datalog.storage.StorageManager
import datalog.tools.Debug.debug

type Relation = Int

class IRTree(using val ctx: Context) {
  def initialize(ast: ASTNode): IROp = {
    ast match {
      case ProgramNode(ruleMap) =>
        DoWhileOp(
          SequenceOp(Seq(
            SwapOp(),
            ClearOp(),
            SequenceOp(ctx.relations.filter(ruleMap.contains).map(r => initialize(ruleMap(r))))
          )),
          DiffOp()
        )
      case AllRulesNode(rules, rId) =>
        InsertOp(rId, UnionOp(rules.map(initialize).toSeq))
      case RuleNode(_, _, joinIdx) =>
        joinIdx match {
          case Some(j) =>
            ProjectOp(
              JoinOp(j.deps.map(r =>
                FilterOp(r, j)), j), j)
          case _ => throw new Exception("Trying to solve without joinIndexes calculated yet")
        }
      case _ => throw new Exception("Non-root passed to IR Program")
    }
  }
}

/**
 * Intermediate representation based on Souffle's RAM
 */
abstract class IROp(using val ctx: Context) {}

case class ProgramOp(body: IROp)(using Context) extends IROp {}

case class SwapOp()(using Context) extends IROp {}

case class DoWhileOp(body: IROp, cond: IROp)(using Context) extends IROp {}

case class SequenceOp(ops: Seq[IROp])(using Context) extends IROp {}

case class DiffOp()(using Context) extends IROp {}

case class ClearOp()(using Context) extends IROp {}

case class FilterOp(rId: Relation, cond: JoinIndexes)(using Context) extends IROp {}

case class ProjectOp(op: IROp, cond: JoinIndexes)(using Context) extends IROp {}

case class JoinOp(ops: Seq[IROp], cond: JoinIndexes)(using Context) extends IROp {} // needed?

case class InsertOp(rId: Relation, value: IROp)(using Context) extends IROp {}

case class UnionOp(ops: Seq[IROp])(using Context) extends IROp {}