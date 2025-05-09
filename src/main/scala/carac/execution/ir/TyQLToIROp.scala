package carac.execution.ir

import carac.execution.ast.*
import carac.execution.{JITOptions, JoinIndexes, PredicateType, StagedCompiler, ir}
import carac.storage.{DB, DatabaseType, EDB, RelationId, StorageManager}
import carac.tools.Debug.debug
import tyql.SelectFlags.Top
import tyql.{DatabaseAST, Expr, MultiRecursiveRelationOp, NaryRelationOp, QueryIRNode, QueryIRVar, RecursiveIRVar, RelationOp, SelectAllQuery, SelectQuery, TableLeaf, ResultTag}

import java.util.function.Predicate

class TyQLToIROp(using val ctx: TyQLInterpreterContext)(using JITOptions) {

  def naiveEval(ruleMap: Map[RelationId, Seq[QueryIRNode]], sortedRelations: Seq[RelationId], copyToDelta: Boolean = false): IROp[Any] =
    val queries = ruleMap.keys.toSeq
//      .filter(ruleMap.contains)
      .map(rId =>
          ResetDeltaOp(rId, naiveEvalRule(rId, ruleMap(rId)).asInstanceOf[IROp[Any]])
      ) :+ InsertDeltaNewIntoDerived()

    SequenceOp(
      OpCode.EVAL_NAIVE,
      //      DebugNode("in eval:", () => s"rId=${ctx.storageManager.ns(rId)} relations=${ctx.relations.map(r => ctx.storageManager.ns(r)).mkString("[", ", ", "]")}  incr=${ctx.newDbId} src=${ctx.knownDbId}") +:
      queries*
    )

//  def semiNaiveEval(ruleMap: mutable.Map[RelationId, ASTNode], sortedRelations: Seq[RelationId]): IROp[Any] = {
//    val queries = sortedRelations
//      .filter(ruleMap.contains)
//      .map(r =>
//        val res = semiNaiveEvalRule(ruleMap(r))
//        ResetDeltaOp(r, res.asInstanceOf[IROp[Any]])
//      )
//
//    SequenceOp(
//      OpCode.SEQ,
//      SequenceOp(
//        OpCode.EVAL_SN,
//        queries*,
//      ),
//      InsertDeltaNewIntoDerived()
//    )
//  }

  def naiveEvalRule(rId: RelationId, tyqlIR: Seq[QueryIRNode]): IROp[?] =
    val allRes = tyqlIR.map {
      case SelectAllQuery(from, where, overrideAlias, ast) =>
        if where.isEmpty && from.length == 1 then
          from.head match
            case TableLeaf(rName, _, _) =>
              ScanOp(ctx.storageManager.ns(rName), DB.Derived)
            case _ => ???
        else
          ???
      case t: SelectQuery =>
        TyQLSQLNode(t, DB.Derived, rId, diff = true)
      case _ => ???
    }
    if allRes.length == 1 then
      allRes.head
    else
      UnionOp(OpCode.EVAL_RULE_NAIVE, allRes*)

//  def naiveEvalRule(ast: RelationOp): IROp[EDB] = {
//
//    ast match {
//      case AllRulesNode(rules, rId, edb) =>
//        val allRes = rules.map(naiveEvalRule).toSeq
//        UnionOp(OpCode.EVAL_RULE_NAIVE, allRes*)
//      case RuleNode(head, _, atoms, k) =>
//        val r = head.asInstanceOf[LogicAtom].relation
//        if (k.edb)
//          ScanEDBOp(r)
//        else
//          ProjectJoinFilterOp(atoms.head.rId, k,
//            k.deps.zipWithIndex.map((md, i) =>
//              val (typ, r) = md
//              val q = ScanOp(r, DB.Derived)
//              typ match
//                case PredicateType.NEGATED =>
//                  val arity = k.atoms(i + 1).terms.length
//                  val res = DiffOp(ComplementOp(k.atoms(i+1).rId, arity), q)
//                  debug(s"found negated relation, rule=", () => s"${ctx.storageManager.printer.ruleToString(k.atoms)}\n\tarity=$arity")
//                  res
//                case _ => q
//            )*
//          )
//      case _ =>
//        debug("AST node passed to naiveEval:", () => ctx.storageManager.printer.printAST(ast))
//        throw new Exception("Wrong ASTNode received when generating naive IR")
//    }
//  }

//  def semiNaiveEvalRule(ast: ASTNode): IROp[EDB] = {
//    ast match {
//      case AllRulesNode(rules, rId, edb) =>
//        var allRes = rules.map(semiNaiveEvalRule).toSeq
////        if (edb)
////          allRes = allRes :+ ScanEDBOp(rId)
////        if(allRes.length == 1) allRes.head else
//        UnionOp(OpCode.EVAL_RULE_SN, allRes*) // None bc union of unions so no point in sorting
//      case RuleNode(head, body, atoms, k) =>
//        val r = head.asInstanceOf[LogicAtom].relation
//        if (k.edb)
//          ScanEDBOp(r)
//        else
//          var idx = -1 // if dep is featured more than once, only use delta once, but at a different pos each time
//          UnionSPJOp(// a single rule body
//            atoms.head.rId,
//            k,
//            k.deps.map((*, d) => {
//              var found = false
//              ProjectJoinFilterOp(atoms.head.rId, k,
//                k.deps.zipWithIndex.map((md, i) => {
//                  val (typ, r) = md
//                  val q = if (r == d && !found && i > idx)
//                    found = true
//                    idx = i
//                    if (typ != PredicateType.NEGATED) // if negated then we want the complement of all facts not just the delta
//                      ScanOp(r, DB.Delta)
//                    else
//                      ScanOp(r, DB.Derived)
//                  else
//                    ScanOp(r, DB.Derived)
//                  typ match
//                    case PredicateType.NEGATED =>
//                      val arity = k.atoms(i + 1).terms.length
//                      val res = DiffOp(ComplementOp(k.atoms(i+1).rId, arity), q)
//                      debug(s"found negated relation, rule=", () => s"${ctx.storageManager.printer.ruleToString(k.atoms)}\n\tarity=$arity")
//                      res
//                    case _ => q
//                })*
//              )
//            })*
//          )
//      case _ =>
//        debug("AST node passed to semiNaiveEval:", () => ctx.storageManager.printer.printAST(ast))
//        throw new Exception("Wrong ASTNode received when generating naive IR")
//    }
//  }

  def generateNaive(ruleMap: Map[RelationId, Seq[QueryIRNode]], sortedRelations: Seq[RelationId]): IROp[Any] =
    DoWhileOp(
      DB.Derived,
      SequenceOp(OpCode.LOOP_BODY,
        SwapAndClearOp(),
        naiveEval(ruleMap, sortedRelations)
      )
    )

//  def generateSemiNaive(ruleMap: mutable.Map[RelationId, ASTNode], sortedRelations: Seq[RelationId]): IROp[Any] = {
//    SequenceOp(OpCode.SEQ,
//      naiveEval(ruleMap, sortedRelations, true),
//      DoWhileOp(
//        DB.Delta,
//        SequenceOp(OpCode.LOOP_BODY,
//          SwapAndClearOp(),
//          semiNaiveEval(ruleMap, sortedRelations)
//        )
//      )
//    )
//  }

//  def generateStratified(stratifiedAST: Seq[mutable.Map[RelationId, ASTNode]], naive: Boolean): IROp[Any] = {
//    SequenceOp(OpCode.SEQ,
//      stratifiedAST.zipWithIndex.map((rules, idx) =>
//        val innerP = if (naive) generateNaive(rules, rules.keys.toSeq) else generateSemiNaive(rules, rules.keys.toSeq)
//
//        if (idx < stratifiedAST.length - 1)
//          SequenceOp(OpCode.EVAL_STRATUM,
//            innerP,
//          )
//        else
//          innerP
//      )*
//    )
//  }

  def generateTopLevelProgram(ruleIRs: Map[RelationId, Seq[QueryIRNode]], naive: Boolean): IROp[Any] =
    val innerProgram =
//      if (naive)
        generateNaive(ruleIRs, ruleIRs.keys.toSeq) // order doesn't matter within stratum
//      else
//        generateSemiNaive(ruleMap, scc.flatten)
    ProgramOp(
//      SequenceOp(
//        OpCode.SEQ,
        innerProgram,
//        ResetDeltaOp(
//          ctx.toSolve,
//        )
    )
}

