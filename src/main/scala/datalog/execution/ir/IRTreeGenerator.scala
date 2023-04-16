package datalog.execution.ir

import datalog.execution.ast.*
import datalog.execution.{JITOptions, StagedCompiler}
import datalog.storage.*
import datalog.tools.Debug.debug

import scala.collection.mutable

class IRTreeGenerator(using val ctx: InterpreterContext)(using JITOptions) {

  /**
   * Generates the IR for the semi-naive evaluation strategy for one iteration
   * withing a strata.
   *
   * @param rules The rules to derive in this strata.
   * @return The IR for the semi-naive evaluation strategy.
   */
  private def semiNaiveEval(rules: mutable.Map[RelationId, ASTNode]): IROp[Any] = {
    val mapped = rules.toSeq.map((r, rule) => {
      val prev = ScanOp(r, DB.Derived, KNOWLEDGE.Known)
      val res = semiNaiveEvalRule(rule)
      val diff = DiffOp(res, prev)

      SequenceOp( // TODO: could flatten, but then potentially can't generate loop if needed
        OpCode.SEQ,
        InsertOp(r, DB.Delta, KNOWLEDGE.New, diff.asInstanceOf[IROp[Any]]),
        InsertOp(r, DB.Derived, KNOWLEDGE.New, prev.asInstanceOf[IROp[Any]], ScanOp(r, DB.Delta, KNOWLEDGE.New).asInstanceOf[IROp[Any]]),
      )
    })
    SequenceOp(OpCode.EVAL_SN, mapped: _*).asInstanceOf[IROp[Any]]
  }

  /**
   * Evaluates one rule in the naive evaluation strategy.
   *
   * @param ast the rule to evaluate.
   * @return the IR for the rule evaluation.
   */
  private def naiveEvalRule(ast: ASTNode): IROp[EDB] = {
    ast match {
      case AllRulesNode(rules, rId, edb) =>
        var allRes = rules.map(naiveEvalRule).toSeq
        if (edb)
          allRes = allRes :+ ScanEDBOp(rId)
        //        if(allRes.length == 1) allRes.head else
        UnionOp(OpCode.EVAL_RULE_NAIVE, allRes: _*)
      case RuleNode(head, _, atoms, hash) =>
        val k = ctx.storageManager.allRulesAllIndexes(atoms.head.rId)(hash)
        val r = head.asInstanceOf[LogicAtom].relation
        if (k.edb)
          ScanEDBOp(r)
        else
        // TODO : Negative join, with missing tuples, if the atom is negated
          ProjectJoinFilterOp(atoms.head.rId, hash,
            k.deps.map(r => ScanOp(r, DB.Derived, KNOWLEDGE.Known)): _*
          )
      case _ =>
        debug("AST node passed to naiveEval:", () => ctx.storageManager.printer.printAST(ast))
        throw new Exception("Wrong ASTNode received when generating naive IR")
    }
  }

  /**
   * Evaluates one rule in the semi-naive evaluation strategy.
   *
   * @param ast the rule to evaluate.
   * @return the IR for the rule evaluation.
   */
  private def semiNaiveEvalRule(ast: ASTNode): IROp[EDB] = {
    ast match {
      case AllRulesNode(rules, rId, edb) =>
        var allRes = rules.map(semiNaiveEvalRule).toSeq
        if (edb)
          allRes = allRes :+ ScanEDBOp(rId)
        //        if(allRes.length == 1) allRes.head else
        UnionOp(OpCode.EVAL_RULE_SN, allRes: _*) // None bc union of unions so no point in sorting
      case RuleNode(head, body, atoms, hash) =>
        val r = head.asInstanceOf[LogicAtom].relation
        val k = ctx.storageManager.allRulesAllIndexes(atoms.head.rId)(hash)
        if (k.edb)
          ScanEDBOp(r)
        else
          var idx = -1 // if dep is featured more than once, only use delta once, but at a different pos each time
          UnionSPJOp( // a single rule body
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
            }): _*
          )
      case _ =>
        debug("AST node passed to semiNaiveEval:", () => ctx.storageManager.printer.printAST(ast))
        throw new Exception("Wrong ASTNode received when generating naive IR")
    }
  }

  /**
   * A step in the strata generation. At each step, some new relations should
   * be derived, and some relations should be kept from the previous strata.
   *
   * @param toDerive The relations to derive in this step.
   * @param toKeep   The relations to keep from the previous strata.
   * @param rules    The rules to derive in this step.
   */
  private case class Step(toDerive: Set[RelationId],
                          toKeep: Set[RelationId],
                          rules: mutable.Map[Int, ASTNode])

  /**
   * Stratifies the program into a sequence of steps, where strata are rules
   * that should be derived simultaneously.
   *
   * @param ast The root of the program.
   * @return A sequence of steps for which relations should be derived.
   */
  private def stratify(ast: ASTNode): Seq[Step] = {
    ast match {
      case ProgramNode(ruleMap) =>
        val components = ctx.precedenceGraph.scc(ctx.toSolve)
        val kept = components.scanLeft(Set[Int]())(_ ++ _)
        components.zip(kept).map((ids, keep) =>
          val rules = ruleMap.filter((r, _) => ids.contains(r))
          Step(ids, keep, rules))
      case _ => throw new Exception("Non-root passed to IR Program")
    }
  }

  /**
   * Generates the IR for the naive evaluation strategy.
   *
   * @param ast The root of the program.
   * @return The IR for the naive evaluation strategy.
   */
  def generateNaive(ast: ASTNode): IROp[Any] = {
    val steps = stratify(ast).map(step =>
      val keep =
        SequenceOp(OpCode.OTHER,
          step.toKeep.toSeq.map(r =>
            InsertOp(r, DB.Derived, KNOWLEDGE.New,
              ScanOp(r, DB.Derived, KNOWLEDGE.Known)
                .asInstanceOf[IROp[Any]])
          ): _*
        )
      val insertions = SequenceOp(
        OpCode.EVAL_NAIVE,
        step.rules.map((rId, rule) =>
          val res = naiveEvalRule(rule).asInstanceOf[IROp[Any]]
          InsertOp(rId, DB.Derived, KNOWLEDGE.New, res)
        ).toSeq: _*
      )
      DoWhileOp(
        DB.Derived,
        SequenceOp(OpCode.LOOP_BODY,
          SwapAndClearOp(),
          keep,
          insertions
        )
      )
    )
    SequenceOp(
      OpCode.OTHER,
      steps: _*,
    )
  }

  /**
   * Generates the IR for the semi-naive evaluation strategy.
   *
   * @param ast The root of the program.
   * @return The IR for the semi-naive evaluation strategy.
   */
  def generateSemiNaive(ast: ASTNode): IROp[Any] = {
    val steps = stratify(ast).map(step =>
      val keep = SequenceOp(OpCode.OTHER,
        step.toKeep.toSeq.flatMap(r =>
          val scan = ScanOp(r, DB.Derived, KNOWLEDGE.Known).asInstanceOf[IROp[Any]]
          Seq(InsertOp(r, DB.Derived, KNOWLEDGE.New, scan))
        ): _*
      )
      val insertions = SequenceOp(OpCode.SEQ,
        step.rules.toSeq.flatMap((rId, rule) =>
          val eval = naiveEvalRule(rule).asInstanceOf[IROp[Any]]
          val scan = ScanOp(rId, DB.Derived, KNOWLEDGE.New).asInstanceOf[IROp[Any]]
          Seq(
            InsertOp(rId, DB.Derived, KNOWLEDGE.New, eval),
            InsertOp(rId, DB.Delta, KNOWLEDGE.New, scan),
          )
        ): _*)

      SequenceOp(OpCode.SEQ,
        insertions,
        keep,
        DoWhileOp(
          DB.Delta,
          SequenceOp(OpCode.LOOP_BODY,
            SwapAndClearOp(),
            keep,
            semiNaiveEval(step.rules)
          )
        )
      )
    )

    SequenceOp(
      OpCode.OTHER,
      steps: _*,
    )
  }
}

