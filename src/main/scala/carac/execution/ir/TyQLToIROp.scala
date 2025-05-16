package carac.execution.ir

import carac.dsl.{Constant, Variable, Term, Atom}
import carac.execution.ast.*
import carac.execution.{JITOptions, JoinIndexes, PredicateType, StagedCompiler, ir}
import carac.storage.{DB, DatabaseType, EDB, RelationId, StorageManager}
import carac.tools.Debug.debug
import tyql.SelectFlags.Top
import tyql.*

import scala.collection.mutable

class TyQLToIROp(using val ctx: TyQLInterpreterContext)(using JITOptions) {

  def naiveEval(ruleMap: Map[RelationId, Seq[QueryIRNode]], sortedRelations: Seq[RelationId]): IROp[Any] =
    val queries = ruleMap.keys.toSeq
      .map(rId =>
          ResetDeltaOp(rId, naiveEvalRule(rId, ruleMap(rId)).asInstanceOf[IROp[Any]])
      ) :+ InsertDeltaNewIntoDerived()

    SequenceOp(
      OpCode.EVAL_NAIVE,
      //      DebugNode("in eval:", () => s"rId=${ctx.storageManager.ns(rId)} relations=${ctx.relations.map(r => ctx.storageManager.ns(r)).mkString("[", ", ", "]")}  incr=${ctx.newDbId} src=${ctx.knownDbId}") +:
      queries*
    )

  def semiNaiveEval(ruleMap: Map[RelationId, Seq[QueryIRNode]], sortedRelations: Seq[RelationId]): IROp[Any] = {
    val queries = sortedRelations
      .map(r =>
        val res = semiNaiveEvalRule(r, ruleMap(r))
        ResetDeltaOp(r, res.asInstanceOf[IROp[Any]])
      )

    SequenceOp(
      OpCode.SEQ,
      SequenceOp(
        OpCode.EVAL_SN,
        queries*,
      ),
      InsertDeltaNewIntoDerived()
    )
  }

  def getPosition(from: Seq[RelationOp], arg: QueryIRNode, depsSchema: Seq[Seq[(String, DatabaseType)]]): (String, Constant) =
    arg match {
      case SelectExpr(attrName, src, _, _) =>
        val toFind = src match
          case QueryIRVar(toSub, name, _) =>
            toSub.alias
        val fromIdx = from.indexWhere(f => f.alias == toFind)
        val projIdx = depsSchema(fromIdx).indexWhere(_._1 == attrName)
        val position = depsSchema.slice(0, fromIdx).map(_.length).sum + projIdx
//        println(s"in SelectExpr: attrName=$attrName, from=$from, fromIdx=$fromIdx, projIdx=$projIdx, position=$position")
        ("v", position)
      case Literal(stringRep, _, _) =>
        ("c", stringRep)
    }

  def toAtomsFromK(rId: RelationId, varIndexes: Seq[Seq[Int]], constIndexes: mutable.Map[Int, Constant], projIndexes: Seq[(String, Constant)], deps: Seq[RelationId], depsSchema: Seq[Seq[(String, DatabaseType)]]): Seq[Atom] =
      val allTerms = mutable.Map[Int, Term]() // flat input position -> Variable or Constant
      val seenVars = mutable.Map[Int, Variable]() // normalized to one Var per position

      varIndexes.foreach {
        case Seq(i1, i2) =>
          val v = seenVars.getOrElseUpdate(i1, Variable(i1))
          seenVars(i2) = v
          allTerms(i1) = v
          allTerms(i2) = v
      }

      constIndexes.foreach { case (idx, const) =>
        allTerms(idx) = const
      }

      // fill in remaining positions with anonymous vars
      for i <- 0 until depsSchema.flatten.size do
        if !allTerms.contains(i) then
          allTerms(i) = Variable(i, anon = true)

      // construct head atom using projected positions
      val headTerms = projIndexes.map {
        case ("v", pos: Int) => allTerms(pos)
        case ("c", c: Constant) => c
      }

      val head = Atom(rId, collection.immutable.ArraySeq.from(headTerms), negated = false)

      // construct one atom per relation in the FROM clause
      val bodyAtoms = deps.zipWithIndex.map { case (relId, idx) =>
        val arity = depsSchema(idx).size
        val offset = depsSchema.take(idx).map(_.size).sum

        val terms = (0 until arity).map(j => allTerms(offset + j)).to(collection.immutable.ArraySeq)
        Atom(rId, terms, negated = false)
      }

      head +: bodyAtoms


  def jIdxHelper(rId: RelationId, from: Seq[RelationOp], where: Seq[QueryIRNode], project: Option[QueryIRNode], schema: ResultTag[?]): JoinIndexes =
    val deps = from.map { // Seq[RelationId]
      case TableLeaf(rName, _, _, _) =>
        if !ctx.storageManager.ns.contains(rName) then
          throw new Exception(s"Relation $rName not found in storage")
        ctx.storageManager.ns(rName)
      case RecursiveIRVar(rName, _, _, _) =>
        if !ctx.storageManager.ns.contains(rName) then
          throw new Exception(s"Relation $rName not found in storage")
        ctx.storageManager.ns(rName)
      case _ => ???
    }
    val depsSchema = deps.map(rId => // Seq[(AttrName, DatabaseType)]
      ctx.ddb.schema(rId)
    )
    val projIndexes = if (project.isEmpty)
      depsSchema.flatten.indices.map(i => ("v", i.asInstanceOf[Constant]))
    else
      project.get match
        case ProjectClause(children, _, _) =>
          children.map(c => c match
            case AttrExpr(child, _, _, _) =>
              getPosition(from, child, depsSchema)
          )

    val varIndexes = mutable.ListBuffer[Seq[Int]]()
    val constIndexes = mutable.Map[Int, Constant]()

    def calculatePos(lhs: QueryIRNode, rhs: QueryIRNode) =
      val lhsPos = getPosition(from, lhs, depsSchema)
      val rhsPos = getPosition(from, rhs, depsSchema)
      if lhsPos._1 == "v" && rhsPos._1 == "v" then
        varIndexes.append(Seq(lhsPos._2, rhsPos._2).asInstanceOf[Seq[Int]])
      else if lhsPos._1 == "c" && rhsPos._1 == "v" then
        constIndexes(rhsPos._2.asInstanceOf[Int]) = lhsPos._2
      else if lhsPos._1 == "v" && rhsPos._1 == "c" then
        constIndexes(lhsPos._2.asInstanceOf[Int]) = rhsPos._2
      else
        ???

    def mapBinOp(c: QueryIRNode): Unit =
      c match {
        case BinExprOp(lhs, rhs, op, _, ast) =>
          ast match
            case Expr.Eq(_, _) =>
              calculatePos(lhs, rhs)
            case Expr.And(_, _) =>
              mapBinOp(lhs)
              mapBinOp(rhs)
            case _ =>
              throw new Exception(s"Unsupported operation in WHERE clause: $ast")
      }

    where.map(wc => wc match {
      case WhereClause(children, ast) =>
        children.map(mapBinOp)
    })

    val isEdb = varIndexes.isEmpty && constIndexes.isEmpty && deps.forall(ctx.storageManager.edbContains)
    val ruleAtoms = toAtomsFromK(rId, varIndexes.toSeq, constIndexes, projIndexes, deps, depsSchema)
    //    println(s"deps=$deps, depsSchema=$depsSchema, projIndexes=$projIndexes")
    JoinIndexes(
      varIndexes.toSeq,
      constIndexes,
      projIndexes,
      deps.map(d => (PredicateType.POSITIVE, d)),
      ruleAtoms,
      mutable.Map(),
      edb = isEdb
    )


  def queryIRToJoinIndex(rId: RelationId, queryIR: QueryIRNode): JoinIndexes =
    queryIR match
      case SelectAllQuery(from, where, _, schema, _) =>
        jIdxHelper(rId, from, where, None, schema)
      case SelectQuery(project, from, where, _, schema, _) =>
        jIdxHelper(rId, from, where, Some(project), schema)
      case _ => ???

  def semiNaiveEvalRule(rId: RelationId, tyqlIR: Seq[QueryIRNode]): IROp[?] =
    val allRes = tyqlIR.map(subquery =>
//      println(s"For query: ${subquery.toSQLString()}:")
      val k = queryIRToJoinIndex(rId, subquery)
//      println(s"\tk=${k.toStringWithNS(ctx.ddb.ns)}}")
      ctx.ee.insertIDB(rId, k)

      var idx = -1 // if dep is featured more than once, only use delta once, but at a different pos each time
      UnionSPJOp( // a single rule body
        rId,
        k,
        k.deps.map((*, d) => {
          var found = false
          ProjectJoinFilterOp(rId, k,
            k.deps.zipWithIndex.map((md, i) => {
              val (typ, r) = md
              if (r == d && !found && i > idx)
                found = true
                idx = i
//                if (typ != PredicateType.NEGATED) // if negated then we want the complement of all facts not just the delta
                ScanOp(r, DB.Delta)
//                else
//                  ScanOp(r, DB.Derived)
              else
                ScanOp(r, DB.Derived)
//              typ match
//                case PredicateType.NEGATED =>
//                  val arity = k.atoms(i + 1).terms.length
//                  val res = DiffOp(ComplementOp(k.atoms(i+1).rId, arity), q)
//                  debug(s"found negated relation, rule=", () => s"${ctx.storageManager.printer.ruleToString(k.atoms)}\n\tarity=$arity")
//                  res
//                case _ => q
            })*
          )
        })*
      )
    )
    if allRes.length == 1 then
      allRes.head
    else
      UnionOp(OpCode.EVAL_RULE_SN, allRes *)

  def naiveEvalRule(rId: RelationId, tyqlIR: Seq[QueryIRNode]): IROp[?] =
    val allRes = tyqlIR.map {
      case SelectAllQuery(from, where, _, _, _) =>
        if where.isEmpty && from.length == 1 then
          from.head match
            case TableLeaf(rName, _, _, _) =>
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

  def generateNaive(ruleMap: Map[RelationId, Seq[QueryIRNode]], sortedRelations: Seq[RelationId]): IROp[Any] =
    DoWhileOp(
      DB.Derived,
      SequenceOp(OpCode.LOOP_BODY,
        SwapAndClearOp(),
        naiveEval(ruleMap, sortedRelations)
      )
    )

  def generateSemiNaive(ruleMap: Map[RelationId, Seq[QueryIRNode]], sortedRelations: Seq[RelationId]): IROp[Any] = {
    SequenceOp(OpCode.SEQ,
      naiveEval(ruleMap, sortedRelations),
      DoWhileOp(
        DB.Delta,
        SequenceOp(OpCode.LOOP_BODY,
          SwapAndClearOp(),
          semiNaiveEval(ruleMap, sortedRelations)
        )
      )
    )
  }

  def generateTopLevelProgram(ruleIRs: Map[RelationId, Seq[QueryIRNode]], naive: Boolean): IROp[Any] =
    val innerProgram =
      if (naive)
        generateNaive(ruleIRs, ruleIRs.keys.toSeq) // order doesn't matter within stratum
      else
        generateSemiNaive(ruleIRs, ruleIRs.keys.toSeq) // By TyQL definition, all within a single stratum, so don't need to sort
    ProgramOp(innerProgram)
}

