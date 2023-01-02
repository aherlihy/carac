package datalog.execution.ir

import datalog.execution.{JoinIndexes, PrecedenceGraph}
import datalog.execution.ast.*
import datalog.storage.StorageManager
import datalog.tools.Debug.debug

type Relation = Int

class IRTree(using val ctx: InterpreterContext) {
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
      case AllRulesNode(rules, rId, edb) =>
        var res = rules.map(initialize).toSeq
        if (edb)
          debug("Found IDB with EDB defined: ", () => ctx.storageManager.ns(rId))
          res = res :+ FilterOp(rId, JoinIndexes(IndexedSeq.empty, Map.empty, IndexedSeq.empty, Seq.empty, true))
        InsertOp(rId, UnionOp(res))
      case RuleNode(head, _, joinIdx) =>
        val r = head.asInstanceOf[LogicAtom].relation
        joinIdx match {
          case Some(j) =>
            if (j.edb)
              FilterOp(r, j)
            else
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
abstract class IROp(using val ctx: InterpreterContext) {}

case class ProgramOp(body: IROp)(using InterpreterContext) extends IROp {}

case class SwapOp()(using InterpreterContext) extends IROp {}

case class DoWhileOp(body: IROp, cond: IROp)(using InterpreterContext) extends IROp {}

case class SequenceOp(ops: Seq[IROp])(using InterpreterContext) extends IROp {}

case class DiffOp()(using InterpreterContext) extends IROp {}

case class ClearOp()(using InterpreterContext) extends IROp {}

case class FilterOp(rId: Relation, keys: JoinIndexes)(using InterpreterContext) extends IROp {}

case class ProjectOp(op: IROp, keys: JoinIndexes)(using InterpreterContext) extends IROp {}

case class JoinOp(ops: Seq[IROp], keys: JoinIndexes)(using InterpreterContext) extends IROp {} // needed?

case class InsertOp(rId: Relation, value: IROp)(using InterpreterContext) extends IROp {}

case class UnionOp(ops: Seq[IROp])(using InterpreterContext) extends IROp {}