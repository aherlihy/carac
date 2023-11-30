package datalog.execution.ir

import datalog.execution.{JITOptions, PredicateType, StagedCompiler, ir}
import datalog.execution.ast.{ASTNode, AllRulesNode, LogicAtom, ProgramNode, RuleNode}
import datalog.storage.{DB, EDB, KNOWLEDGE, RelationId, StorageManager}
import datalog.tools.Debug.debug

import java.util.function.Predicate
import scala.collection.mutable

class IRTreeGenerator(using val ctx: InterpreterContext)(using JITOptions) {
  def naiveEval(ruleMap: mutable.Map[RelationId, ASTNode], sortedRelations: Seq[RelationId], copyToDelta: Boolean = false): IROp[Any] = {
    SequenceOp(
      OpCode.EVAL_NAIVE,
      //      DebugNode("in eval:", () => s"rId=${ctx.storageManager.ns(rId)} relations=${ctx.relations.map(r => ctx.storageManager.ns(r)).mkString("[", ", ", "]")}  incr=${ctx.newDbId} src=${ctx.knownDbId}") +:
      sortedRelations
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
  }

  def semiNaiveEval(ruleMap: mutable.Map[RelationId, ASTNode], sortedRelations: Seq[RelationId]): IROp[Any] = {
    SequenceOp(
      OpCode.EVAL_SN,
      sortedRelations
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
  }

  def naiveEvalRule(ast: ASTNode): IROp[EDB] = {
    ast match {
      case AllRulesNode(rules, rId, edb) =>
        var allRes = rules.map(naiveEvalRule).toSeq
        // if (edb)
        allRes = allRes :+ ScanEDBOp(rId) // TODO: potentially change this to Discovered not EDB
//        if(allRes.length == 1) allRes.head else
        UnionOp(OpCode.EVAL_RULE_NAIVE, allRes:_*)
      case RuleNode(head, _, atoms, k) =>
        val r = head.asInstanceOf[LogicAtom].relation
        if (k.edb)
          ScanEDBOp(r)
        else
          ProjectJoinFilterOp(atoms.head.rId, k,
            k.deps.zipWithIndex.map((md, i) =>
              val (typ, r) = md
              val q = ScanOp(r, DB.Derived, KNOWLEDGE.Known)
              typ match
                case PredicateType.NEGATED =>
                  val arity = k.atoms(i + 1).terms.length
                  val res = DiffOp(ComplementOp(arity), q)
                  debug(s"found negated relation, rule=", () => s"${ctx.storageManager.printer.ruleToString(k.atoms)}\n\tarity=$arity")
                  res
                case _ => q
            ):_*
          )
      case _ =>
        debug("AST node passed to naiveEval:", () => ctx.storageManager.printer.printAST(ast))
        throw new Exception("Wrong ASTNode received when generating naive IR")
    }
  }

  def semiNaiveEvalRule(ast: ASTNode): IROp[EDB] = {
    ast match {
      case AllRulesNode(rules, rId, edb) =>
        var allRes = rules.map(semiNaiveEvalRule).toSeq
        // if (edb)
        allRes = allRes :+ ScanEDBOp(rId)
//        if(allRes.length == 1) allRes.head else
        UnionOp(OpCode.EVAL_RULE_SN, allRes:_*) // None bc union of unions so no point in sorting
      case RuleNode(head, body, atoms, k) =>
        val r = head.asInstanceOf[LogicAtom].relation
        if (k.edb)
          ScanEDBOp(r)
        else
          var idx = -1 // if dep is featured more than once, only use delta once, but at a different pos each time
          UnionSPJOp(// a single rule body
            atoms.head.rId,
            k,
            k.deps.map((*, d) => {
              var found = false
              ProjectJoinFilterOp(atoms.head.rId, k,
                k.deps.zipWithIndex.map((md, i) => {
                  val (typ, r) = md
                  val q = if (r == d && !found && i > idx)
                    found = true
                    idx = i
                    if (typ != PredicateType.NEGATED) // if negated then we want the complement of all facts not just the delta
                      ScanOp(r, DB.Delta, KNOWLEDGE.Known)
                    else
                      ScanOp(r, DB.Derived, KNOWLEDGE.Known)
                  else
                    ScanOp(r, DB.Derived, KNOWLEDGE.Known)
                  typ match
                    case PredicateType.NEGATED =>
                      val arity = k.atoms(i + 1).terms.length
                      val res = DiffOp(ComplementOp(arity), q)
                      debug(s"found negated relation, rule=", () => s"${ctx.storageManager.printer.ruleToString(k.atoms)}\n\tarity=$arity")
                      res
                    case _ => q
                }): _*
              )
            }):_*
          )
      case _ =>
        debug("AST node passed to semiNaiveEval:", () => ctx.storageManager.printer.printAST(ast))
        throw new Exception("Wrong ASTNode received when generating naive IR")
    }
  }

  def generateNaive(ruleMap: mutable.Map[RelationId, ASTNode], sortedRelations: Seq[RelationId]): IROp[Any] = {
    DoWhileOp(
      DB.Derived,
      SequenceOp(OpCode.LOOP_BODY,
        SwapAndClearOp(),
        naiveEval(ruleMap, sortedRelations)
      )
    )
  }

  def generateSemiNaive(ruleMap: mutable.Map[RelationId, ASTNode], sortedRelations: Seq[RelationId]): IROp[Any] = {
    SequenceOp(OpCode.SEQ,
      naiveEval(ruleMap, sortedRelations, true),
      DoWhileOp(
        DB.Delta,
        SequenceOp(OpCode.LOOP_BODY,
          SwapAndClearOp(),
          semiNaiveEval(ruleMap, sortedRelations)
        )
      )
    )
  }

  def generateStratified(stratifiedAST: Seq[mutable.Map[RelationId, ASTNode]], naive: Boolean): IROp[Any] = {
    SequenceOp(OpCode.SEQ,
      stratifiedAST.zipWithIndex.map((rules, idx) =>
        val innerP = if (naive) generateNaive(rules, rules.keys.toSeq) else generateSemiNaive(rules, rules.keys.toSeq)

        if (idx < stratifiedAST.length - 1)
          SequenceOp(OpCode.EVAL_STRATUM,
            innerP,
            UpdateDiscoveredOp()
          )
        else
          innerP
      ): _*
    )
  }

  def generateTopLevelProgram(ast: ASTNode, naive: Boolean): IROp[Any] = {
    ast match {
      case ProgramNode(ruleMap) =>
        val scc = ctx.precedenceGraph.scc(ctx.toSolve)
        val innerProgram =
          if (scc.length <= 1) // || !stratified)
            if (naive)
              generateNaive(ruleMap, scc.flatten)
            else
              generateSemiNaive(ruleMap, scc.flatten)
          else
            val strata = scc.map(stratum => stratum.map(r => (r, ruleMap(r))).to(mutable.Map))
            generateStratified(strata, naive)
        ProgramOp(innerProgram)
      case _ => throw new Exception("Non-root AST passed to IR Generator")
    }
  }
}

