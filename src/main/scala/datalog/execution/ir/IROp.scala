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

class IRTree(using val ctx: InterpreterContext) {
  def naiveEval(rId: Relation, ruleMap: mutable.Map[Relation, ASTNode]): IROp =
    SequenceOp(
//      DebugNode("in eval:", () => s"rId=${ctx.storageManager.ns(rId)} relations=${ctx.relations.map(r => ctx.storageManager.ns(r)).mkString("[", ", ", "]")}  incr=${ctx.newDbId} src=${ctx.knownDbId}") +:
      ctx.relations
        .filter(ruleMap.contains)
        .map(r =>
          InsertOp(r, DB.Derived, KNOWLEDGE.New, naiveEvalRule(ruleMap(r)))
        )
    )

  def semiNaiveEval(rId: Relation, ruleMap: mutable.Map[Relation, ASTNode]): IROp =
    SequenceOp(
//      DebugNode("initial state ", () => s"@ ${ctx.count} ${ctx.storageManager.printer.toString()}") +:
//      DebugNode("evalSN for ", () => ctx.storageManager.ns(rId)) +:
      ctx.relations
        .filter(ruleMap.contains)
        .map(r =>
          val prev = ScanOp(r, DB.Derived, KNOWLEDGE.Known)
          val res = semiNaiveEvalRule(ruleMap(r))
          val diff = DiffOp(res, prev)

          SequenceOp(Seq(
            InsertOp(r, DB.Delta, KNOWLEDGE.New, diff),
            InsertOp(r, DB.Derived, KNOWLEDGE.New, prev, Some(diff)),
//            DebugPeek("\tdiff, i.e. delta[new]", () => s"${ctx.storageManager.ns(r)}] = ", ScanOp(r, DB.Delta, KNOWLEDGE.New)),
//            DebugPeek("\tall, i.e. derived[new]", () => s"${ctx.storageManager.ns(r)}] = ", ScanOp(r, DB.Derived, KNOWLEDGE.New)),
          ))
        )
    )

  def naiveEvalRule(ast: ASTNode): IROp = {
    ast match {
      case AllRulesNode(rules, rId, edb) =>
        var allRes = rules.map(naiveEvalRule).toSeq
        if (edb)
          allRes = allRes :+ ScanEDBOp(rId)
        val res = UnionOp(allRes)
//        DebugPeek("NaiveSPJU: ", () => s"r=${ctx.storageManager.ns(rId)} keys=${ctx.storageManager.printer.printIR(res).replace("\n", " ")} knownDBId ${ctx.knownDbId} \nresult of evalRule: ", res)
        res
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
        var allRes = rules.map(semiNaiveEvalRule).toSeq
        if (edb)
          allRes = allRes :+ ScanEDBOp(rId)
        val res = UnionOp(allRes)
//        DebugPeek("SPJU: ", () => s"r=${ctx.storageManager.ns(rId)} keys=${ctx.storageManager.printer.printIR(res).replace("\n", " ")} knownDBId ${ctx.knownDbId} \nevalRuleSN ", res)
        res
      case RuleNode(head, _, joinIdx) =>
        val r = head.asInstanceOf[LogicAtom].relation
        joinIdx match {
          case Some(k) =>
            if (k.edb)
              ScanEDBOp(r)
            else
              var idx = -1 // if dep is featured more than once, only use delta once, but at a different pos each time
              UnionOp(
                k.deps.map(d => {
                  var found = false
                  ProjectOp(
                    JoinOp(
                      k.deps.zipWithIndex.map((r, i) => {
                        if (r == d && !found && i > idx)
                          found = true
                          idx = i
                          ScanOp(r, DB.Delta, KNOWLEDGE.Known)
                        else
                          ScanOp(r, DB.Derived, KNOWLEDGE.Known)
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

  def generateNaive(ast: ASTNode): IROp = {
    ast match {
      case ProgramNode(ruleMap) =>
        DoWhileOp(
          SequenceOp(Seq(
            SwapOp(),
            ClearOp(),
            naiveEval(ctx.toSolve, ruleMap)
          )),
          CompareOp(DB.Derived)
        )
      case _ => throw new Exception("Non-root passed to IR Program")
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
                    naiveEval(r, ruleMap),
                    InsertOp(r, DB.Delta, KNOWLEDGE.New,
                      ScanOp(r, DB.Derived, KNOWLEDGE.New))
                  ))
                )
            ),
            DoWhileOp(
            SequenceOp(Seq(
              SwapOp(),
              ClearOp(),
              semiNaiveEval(ctx.toSolve, ruleMap)
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

case class InsertOp(rId: Relation, db: DB, knowledge: KNOWLEDGE, value: IROp, value2: Option[IROp] = None)(using InterpreterContext) extends IROp {}

case class UnionOp(ops: Seq[IROp])(using InterpreterContext) extends IROp {}

case class DiffOp(lhs: IROp, rhs: IROp)(using InterpreterContext) extends IROp {}

case class DebugNode(prefix: String, debug: () => String)(using InterpreterContext) extends IROp {}
case class DebugPeek(prefix: String, debug: () => String, op: IROp)(using InterpreterContext) extends IROp {}
