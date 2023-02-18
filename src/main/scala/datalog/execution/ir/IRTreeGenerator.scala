package datalog.execution.ir

import datalog.execution.StagedCompiler
import datalog.execution.ast.{ASTNode, AllRulesNode, LogicAtom, ProgramNode, RuleNode}
import datalog.storage.{DB, KNOWLEDGE, RelationId}
import datalog.tools.Debug.debug

import scala.collection.mutable

class IRTreeGenerator(using val ctx: InterpreterContext) {
  def naiveEval(ruleMap: mutable.Map[RelationId, ASTNode], copyToDelta: Boolean = false): IROp =
    SequenceOp(
      OpCode.EVAL_NAIVE,
      //      DebugNode("in eval:", () => s"rId=${ctx.storageManager.ns(rId)} relations=${ctx.relations.map(r => ctx.storageManager.ns(r)).mkString("[", ", ", "]")}  incr=${ctx.newDbId} src=${ctx.knownDbId}") +:
      ctx.sortedRelations
        .filter(ruleMap.contains)
        .flatMap(r =>
          if (copyToDelta)
            Seq( // TODO: un-flatten to generate for loops if better
              InsertOp(r, DB.Derived, KNOWLEDGE.New, naiveEvalRule(ruleMap(r))),
              InsertOp(r, DB.Delta, KNOWLEDGE.New, ScanOp(r, DB.Derived, KNOWLEDGE.New))
            )
          else
            Seq(InsertOp(r, DB.Derived, KNOWLEDGE.New, naiveEvalRule(ruleMap(r))))
        ):_*
    )

  def semiNaiveEval(rId: RelationId, ruleMap: mutable.Map[RelationId, ASTNode]): IROp =
    SequenceOp(
      OpCode.EVAL_SN,
      ctx.sortedRelations
        .filter(ruleMap.contains)
        .flatMap(r =>
//        .map(r =>
          val prev = ScanOp(r, DB.Derived, KNOWLEDGE.Known)
          val res = semiNaiveEvalRule(ruleMap(r))
          val diff = DiffOp(res, prev)

//          SequenceOp( // TODO: could flatten, but then potentially can't generate loop if needed
//            OpCode.SEQ,
          Seq(
            InsertOp(r, DB.Delta, KNOWLEDGE.New, diff),
            InsertOp(r, DB.Derived, KNOWLEDGE.New, prev, ScanOp(r, DB.Delta, KNOWLEDGE.New)),
          )
        ):_*,
    )

  def naiveEvalRule(ast: ASTNode): IRRelOp = {
    ast match {
      case AllRulesNode(rules, rId, edb) =>
        var allRes = rules.map(naiveEvalRule).toSeq
        if (edb)
          allRes = allRes :+ ScanEDBOp(rId)
        //        DebugPeek("NaiveSPJU: ", () => s"r=${ctx.storageManager.ns(rId)} keys=${ctx.storageManager.printer.printIR(res).replace("\n", " ")} knownDBId ${ctx.knownDbId} \nresult of evalRule: ", res)
        if(allRes.size == 1) allRes.head else UnionOp(OpCode.EVAL_RULE_NAIVE, allRes:_*)
      case RuleNode(head, _, joinIdx) =>
        val r = head.asInstanceOf[LogicAtom].relation
        joinIdx match {
          case Some(k) =>
            if (k.edb)
              ScanEDBOp(r)
            else
              ProjectOp(k,
                JoinOp(k,
                  k.deps.map(r => ScanOp(r, DB.Derived, KNOWLEDGE.Known)):_*
                )
              )
          case _ => throw new Exception("Trying to solve without joinIndexes calculated yet")
        }
      case _ =>
        debug("AST node passed to naiveEval:", () => ctx.storageManager.printer.printAST(ast))
        throw new Exception("Wrong ASTNode received when generating naive IR")
    }
  }

  def semiNaiveEvalRule(ast: ASTNode): IRRelOp = {
    ast match {
      case AllRulesNode(rules, rId, edb) =>
        var allRes = rules.map(semiNaiveEvalRule).toSeq
        if (edb)
          allRes = allRes :+ ScanEDBOp(rId)
        if(allRes.size == 1) allRes.head else UnionOp(OpCode.EVAL_RULE_SN, allRes:_*)
        //        DebugPeek("SPJU: ", () => s"r=${ctx.storageManager.ns(rId)} keys=${ctx.storageManager.printer.printIR(res).replace("\n", " ")} knownDBId ${ctx.knownDbId} \nevalRuleSN ", res)
      case RuleNode(head, _, joinIdx) =>
        val r = head.asInstanceOf[LogicAtom].relation
        joinIdx match {
          case Some(k) =>
            if (k.edb)
              ScanEDBOp(r)
            else
              var idx = -1 // if dep is featured more than once, only use delta once, but at a different pos each time
              UnionOp(OpCode.UNION,
                k.deps.map(d => {
                  var found = false
                  ProjectOp(k,
                    JoinOp(k,
                      k.deps.zipWithIndex.map((r, i) => {
                        if (r == d && !found && i > idx)
                          found = true
                          idx = i
                          ScanOp(r, DB.Delta, KNOWLEDGE.Known)
                        else
                          ScanOp(r, DB.Derived, KNOWLEDGE.Known)
                      }):_*
                    )
                  )
                }):_*
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
          DB.Derived,
          SequenceOp(OpCode.LOOP_BODY,
            SwapAndClearOp(),
            naiveEval(ruleMap)
          )
        )
      case _ => throw new Exception("Non-root passed to IR Program")
    }
  }

  def generateSemiNaive(ast: ASTNode): IROp = {
    ast match {
      case ProgramNode(ruleMap) =>
        ProgramOp(SequenceOp(OpCode.SEQ,
          naiveEval(ruleMap, true),
          DoWhileOp(
            DB.Delta,
            SequenceOp(OpCode.LOOP_BODY,
              SwapAndClearOp(),
              semiNaiveEval(ctx.toSolve, ruleMap)
            )
          )
        ))
      case _ => throw new Exception("Non-root passed to IR Program")
    }
  }
}

