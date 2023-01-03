package datalog.execution.ir

import datalog.execution.{JoinIndexes, PrecedenceGraph}
import datalog.execution.ast.*
import datalog.storage.StorageManager
import datalog.tools.Debug.debug

import scala.collection.mutable

type Relation = Int
enum DB:
  case Derived, Delta
enum KNOWLEDGE:
  case New, Known

class IRTree(using val ctx: InterpreterContext) {
  def generateNaive(ast: ASTNode): IROp = {
    ast match {
      case ProgramNode(ruleMap) =>
        DoWhileOp(
          SequenceOp(Seq(
            SwapOp(),
            ClearOp(),
            naiveEval(ruleMap)
          )),
          CompareOp(DB.Derived)
        )
      case _ => throw new Exception("Non-root passed to IR Program")
    }
  }

  def naiveEval(ruleMap: mutable.Map[Int, ASTNode]): IROp =
    SequenceOp(
      ctx.relations
        .filter(ruleMap.contains)
        .map(r =>
          InsertOp(r, DB.Derived, KNOWLEDGE.New, naiveEvalRule(ruleMap(r)), true)
        )
    )

  def semiNaiveEval(ruleMap: mutable.Map[Int, ASTNode]): IROp =
    SequenceOp(
      ctx.relations
        .filter(ruleMap.contains)
        .map(r =>
          SequenceOp(Seq(
            InsertOp(r, DB.Delta, KNOWLEDGE.New,
              DiffOp(semiNaiveEvalRule(ruleMap(r)), ScanOp(r, DB.Derived, KNOWLEDGE.Known)), true),
            InsertOp(r, DB.Derived, KNOWLEDGE.New,
              ScanOp(r, DB.Delta, KNOWLEDGE.New), true),
            InsertOp(r, DB.Derived, KNOWLEDGE.New,
              ScanOp(r, DB.Derived, KNOWLEDGE.Known))
          ))
        )
    )

  def naiveEvalRule(ast: ASTNode): IROp = {
    ast match {
      case AllRulesNode(rules, rId, edb) =>
        var res = rules.map(naiveEvalRule).toSeq
        if (edb)
          res = res :+ ScanEDBOp(rId)
        UnionOp(res)
      case RuleNode(head, _, joinIdx) =>
        val r = head.asInstanceOf[LogicAtom].relation
        joinIdx match {
          case Some(k) =>
            if (k.edb)
              ScanEDBOp(r)
            else
              ProjectOp(
                JoinOp(k.deps.map(r =>
                  ScanOp(r, DB.Derived, KNOWLEDGE.Known)), k), k)
          case _ => throw new Exception("Trying to solve without joinIndexes calculated yet")
        }
      case _ =>
        debug("AST node passed to naiveEval:", () => ctx.storageManager.printer.printAST(ast))
        throw new Exception("Wrong ASTNode received when generating naive IR")
    }
  }

  def semiNaiveEvalRule(ast: ASTNode): IROp = {
    ast match {
      case AllRulesNode(rules, rId, edb) =>
        var res = rules.map(semiNaiveEvalRule).toSeq
        if (edb)
          res = res :+ ScanEDBOp(rId)
        UnionOp(res)
      case RuleNode(head, _, joinIdx) =>
        val r = head.asInstanceOf[LogicAtom].relation
        joinIdx match {
          case Some(k) =>
            if (k.edb)
              ScanEDBOp(r)
            else
              var idx = -1 // if dep is featured more than once, only us delta once, but at a different pos each time
              UnionOp(
                k.deps.map(d => {
                  var found = false
                  ProjectOp(
                    JoinOp(
                      k.deps.zipWithIndex.map((r, i) => {
                        if (r == d && !found && i > idx)
                          found = true
                          idx = i
                          ScanOp(r, DB.Delta, KNOWLEDGE.Known) //Scan(deltaDB(knownDbId).getOrElse(r, EDB()), r)
                        else
                          ScanOp(r, DB.Derived, KNOWLEDGE.Known) //Scan(derivedDB(knownDbId).getOrElse(r, edbs.getOrElse(r, EDB())), r)
                      }),
                      k
                    ),
                    k
                  )
                })
            )
          case _ => throw new Exception("Trying to solve without joinIndexes calculated yet")
        }
      case _ =>
        debug("AST node passed to semiNaiveEval:", () => ctx.storageManager.printer.printAST(ast))
        throw new Exception("Wrong ASTNode received when generating naive IR")
    }
  }

  def generateSemiNaive(ast: ASTNode): IROp = {
    ast match {
      case ProgramNode(ruleMap) =>
        ProgramOp(SequenceOp(Seq(
            SequenceOp(
              ctx.relations
                .filter(ruleMap.contains)
                .map(r =>
                  SequenceOp(Seq(
                    naiveEval(ruleMap),
                    InsertOp(r, DB.Delta, KNOWLEDGE.New,
                      ScanOp(r, DB.Derived, KNOWLEDGE.New), true)
                  ))
                )
            ),
            DoWhileOp(
            SequenceOp(Seq(
              SwapOp(),
              ClearOp(),
              semiNaiveEval(ruleMap)
            )),
            CompareOp(DB.Delta)
          )
        )))
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

case class CompareOp(db: DB)(using InterpreterContext) extends IROp {}

case class ClearOp()(using InterpreterContext) extends IROp {}

case class ScanOp(rId: Relation, db: DB, knowledge: KNOWLEDGE)(using InterpreterContext) extends IROp {}
case class ScanEDBOp(rId: Relation)(using InterpreterContext) extends IROp {}

case class ProjectOp(op: IROp, keys: JoinIndexes)(using InterpreterContext) extends IROp {}

case class JoinOp(ops: Seq[IROp], keys: JoinIndexes)(using InterpreterContext) extends IROp {} // needed?

case class InsertOp(rId: Relation, db: DB, knowledge: KNOWLEDGE, value: IROp, clear: Boolean = false)(using InterpreterContext) extends IROp {}

case class UnionOp(ops: Seq[IROp])(using InterpreterContext) extends IROp {}

case class DiffOp(lhs: IROp, rhs: IROp)(using InterpreterContext) extends IROp {}

case class DebugNode(debug: () => Unit)(using InterpreterContext) extends IROp {}