package datalog.execution.ir

import datalog.execution.StagedCompiler
import datalog.execution.ast.{ASTNode, AllRulesNode, LogicAtom, ProgramNode, RuleNode}
import datalog.storage.{DB, KNOWLEDGE, RelationId, CollectionsStorageManager}
import datalog.tools.Debug.debug

import scala.collection.mutable

class IRTreeGenerator(using val ctx: InterpreterContext) {
  def naiveEval(ruleMap: mutable.Map[RelationId, ASTNode], copyToDelta: Boolean = false): IROp[Any] =
    SequenceOp(
      OpCode.EVAL_NAIVE,
      //      DebugNode("in eval:", () => s"rId=${ctx.storageManager.ns(rId)} relations=${ctx.relations.map(r => ctx.storageManager.ns(r)).mkString("[", ", ", "]")}  incr=${ctx.newDbId} src=${ctx.knownDbId}") +:
      ctx.sortedRelations
        .filter(ruleMap.contains)
        .flatMap(r =>
          if (copyToDelta)
            Seq( // TODO: un-flatten to generate for loops if better
              InsertOp(r, DB.Derived, KNOWLEDGE.New, naiveEvalRule(ruleMap(r)).asInstanceOf[IROp[Any]]),
              InsertOp(r, DB.Delta, KNOWLEDGE.New, ScanOp(r, DB.Derived, KNOWLEDGE.New).asInstanceOf[IROp[Any]])
            )
          else
            Seq(InsertOp(r, DB.Derived, KNOWLEDGE.New, naiveEvalRule(ruleMap(r)).asInstanceOf[IROp[Any]]))
        ):_*
    )

  def semiNaiveEval(rId: RelationId, ruleMap: mutable.Map[RelationId, ASTNode]): IROp[Any] =
    SequenceOp(
      OpCode.EVAL_SN,
      ctx.sortedRelations
        .filter(ruleMap.contains)
//        .flatMap(r =>
        .map(r =>
          val prev = ScanOp(r, DB.Derived, KNOWLEDGE.Known)
          val res = semiNaiveEvalRule(ruleMap(r))
          val diff = DiffOp(res, prev)

          SequenceOp( // TODO: could flatten, but then potentially can't generate loop if needed
            OpCode.SEQ,
//          Seq(
            InsertOp(r, DB.Delta, KNOWLEDGE.New, diff.asInstanceOf[IROp[Any]]),
            InsertOp(r, DB.Derived, KNOWLEDGE.New, prev.asInstanceOf[IROp[Any]], ScanOp(r, DB.Delta, KNOWLEDGE.New).asInstanceOf[IROp[Any]]),
          )
        ):_*,
    )

  def naiveEvalRule(ast: ASTNode): IROp[CollectionsStorageManager#EDB] = {
    ast match {
      case AllRulesNode(rules, rId, edb) =>
        var allRes = rules.map(naiveEvalRule).toSeq
        if (edb)
          allRes = allRes :+ ScanEDBOp(rId)
//        if(allRes.size == 1) allRes.head else
        UnionOp(OpCode.EVAL_RULE_NAIVE, allRes:_*)
      case RuleNode(head, _, atoms, hash) =>
        val k = ctx.storageManager.allRulesAllIndexes(atoms.head.rId)(hash)
        val r = head.asInstanceOf[LogicAtom].relation
        if (k.edb)
          ScanEDBOp(r)
        else
          ProjectJoinFilterOp(atoms.head.rId, hash,
            k.deps.map(r => ScanOp(r, DB.Derived, KNOWLEDGE.Known)):_*
          )
      case _ =>
        debug("AST node passed to naiveEval:", () => ctx.storageManager.printer.printAST(ast))
        throw new Exception("Wrong ASTNode received when generating naive IR")
    }
  }

  def semiNaiveEvalRule(ast: ASTNode): IROp[CollectionsStorageManager#EDB] = {
    ast match {
      case AllRulesNode(rules, rId, edb) =>
        var allRes = rules.map(semiNaiveEvalRule).toSeq
        if (edb)
          allRes = allRes :+ ScanEDBOp(rId)
//        if(allRes.size == 1) allRes.head else
        UnionOp(OpCode.EVAL_RULE_SN, allRes:_*) // None bc union of unions so no point in sorting
      case RuleNode(head, body, atoms, hash) =>
        val r = head.asInstanceOf[LogicAtom].relation
        val k = ctx.storageManager.allRulesAllIndexes(atoms.head.rId)(hash)
        if (k.edb)
          ScanEDBOp(r)
        else
          var idx = -1 // if dep is featured more than once, only use delta once, but at a different pos each time
          UnionSPJOp(// a single rule body
            atoms.head.rId,
            hash,
            k.deps.map(d => {
              var found = false
              ProjectJoinFilterOp(atoms.head.rId, hash,
                k.deps.zipWithIndex.map((r, i) => {
                  if (r == d && !found && i > idx)
                    found = true
                    idx = i
                    ScanOp(r, DB.Delta, KNOWLEDGE.Known)
                  else
                    ScanOp(r, DB.Derived, KNOWLEDGE.Known)
                }): _*
              )
            }):_*
          )
      case _ =>
        debug("AST node passed to semiNaiveEval:", () => ctx.storageManager.printer.printAST(ast))
        throw new Exception("Wrong ASTNode received when generating naive IR")
    }
  }

  def generateNaive(ast: ASTNode): IROp[Any] = {
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

  def generateSemiNaive(ast: ASTNode): IROp[Any] = {
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

