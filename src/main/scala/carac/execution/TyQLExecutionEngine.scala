package carac.execution

import carac.dsl.StorageAtom
import carac.execution.ast.ASTNode
import carac.execution.ir.OpCode.OTHER
import tyql.{DatabaseAST, Expr, GroupByQuery, MultiRecursiveRelationOp, NaryRelationOp, QueryIRNode, RelationOp, ResultTag}
import carac.execution.ir.{IROp, InsertDeltaNewIntoDerived, InterpreterContext, OpCode, ProgramOp, ResetDeltaOp, SequenceOp, TyQLInterpreterContext, TyQLSQLNode, TyQLToIROp}
import carac.storage.{DB, DatabaseType, DuckDBEDB, DuckDBStorageManager, EDB, RelationId, StorageManager, StorageTerm}
import tyql.Query.MultiRecursive

import scala.collection.mutable

class TyQLExecutionEngine(val ddb: DuckDBStorageManager,
                          override val defaultJITOptions: JITOptions = JITOptions(mode = Mode.Interpreted)
                         ) extends StagedExecutionEngine(ddb, defaultJITOptions) {
  import tyql.TreePrettyPrinter.*
  val idbSchemas: mutable.Map[RelationId, Seq[(String, String)]] = mutable.Map.empty

  def deUnion(ast: QueryIRNode): Seq[QueryIRNode] =
    ast match
      case NaryRelationOp(children, op, _, _) if op == "UNION" || op == "UNION ALL" =>
        children.flatMap(deUnion)
      case _ => Seq(ast)
  def resultTagToDatabaseType(t: ResultTag[?]): DatabaseType =
    t match
      case ResultTag.IntTag => DatabaseType.INTEGER
      case ResultTag.StringTag => DatabaseType.TEXT
      case _ => ???

  override def insertEDB(rule: StorageAtom, schema: Option[Seq[(String, DatabaseType)]]): Unit =
    schema match
      case Some(s) =>
        ddb.declareTable(rule.rId, s)
        storageManager.insertEDB(rule)
      case None => throw new Exception(s"To use TyQL frontend must declare EDB schemas")

  def insertIDB(rId: Int, k: JoinIndexes): Unit =
    storageManager.allRulesAllIndexes.getOrElseUpdate(rId, mutable.Map[String, JoinIndexes]()).addOne(k.hash, k)
    storageManager.addConstantsToDomain(k.constIndexes.values.toSeq)
    val allLocs = k.varIndexes.flatten ++ k.constIndexes.keys

    if (allLocs.nonEmpty) {
      val relationCands = mutable.Map[RelationId, mutable.BitSet]()
      var start = 0
      for r <- k.deps.map(_._2) do {
        val schema = ddb.schema(r)
        for i <- schema.indices do {
          if (allLocs.contains(start + i))
            relationCands.getOrElseUpdate(r, mutable.BitSet()).addOne(i)
        }
        start += schema.size
      }
      if relationCands.nonEmpty then storageManager.registerIndexCandidates(relationCands) // add at once to deduplicate ahead of time and avoid repeated calls
    }

    // TODO: for now skip presort and pre-generating hashes


  def extractSchemas(ast: QueryIRNode): Map[RelationId, (String, Seq[(String, DatabaseType)], Seq[QueryIRNode])] =
    ast match
      case MultiRecursiveRelationOp(alias, queries, finalQ, _, _) =>
        // generate IDB for each recursive relation defined within stratum
        alias.zipWithIndex.map((a, i) =>
          val tempRID: RelationId = -(i + 2)
          val subqueries = deUnion(queries(i))
          val base = subqueries.head
          val tag = base.ast match
            case t: DatabaseAST[?] => t.qTag
            case e: Expr[?, ?, ?] => e.tag
            case _ => ???
          val schema = tag match
            case ResultTag.NamedTupleTag(names, types) =>
              names.zip(types).map((name, typ) => (name, resultTagToDatabaseType(typ)))
            case _ => ???
          (tempRID, (alias(i), schema, subqueries))
        ).toMap
      case _ => throw new Exception(s"Unimplemented: Currently only recursive queries are supported: $ast")

  def toCaracIR(tyqlAST: DatabaseAST[?], naive: Boolean)(using ctx: TyQLInterpreterContext): (IROp[?], IROp[EDB]) =
    val tyqlIR = tyqlAST.toQueryIR
    tyqlIR match
      case MultiRecursiveRelationOp(aliases, queries, finalQIR, _, ast) =>
        val linear = ast.asInstanceOf[MultiRecursive[?]].$linear.getOrElse(false)
        if linear then
          (SequenceOp(OTHER), TyQLSQLNode(tyqlIR.asInstanceOf[RelationOp], DB.EDB, -1))
        else
          val ruleSchemas = extractSchemas(tyqlIR)
          val ruleMap = ruleSchemas.map((rId, metadata) =>
            val (alias, schema, subqueries) = metadata
            println(s"Adding rule rId=$rId, alias=$alias")
            if storageManager.ns.contains(rId) then
              throw new Exception(s"Using RelationId $rId for $alias, but already exists in storage")
            storageManager.ns(rId) = alias
            storageManager.declareTable(rId, schema)
            (rId, subqueries)
          )
          storageManager.verifyEDBs(ruleMap.keys.toSeq, None)
          val program = TyQLToIROp().generateTopLevelProgram(ruleMap, naive)
          val finalQ = TyQLSQLNode(finalQIR, DB.Derived, ruleMap.keys.head)
          storageManager.initEvaluation()
          (program, finalQ)
      case GroupByQuery(source, groupBy, having, _, _) =>
        source match
          case MultiRecursiveRelationOp(aliases, query, finalQ, carriedSymbols, ast) =>
            val linear = ast.asInstanceOf[MultiRecursive[?]].$linear.getOrElse(false)
            if (linear)
              (SequenceOp(OTHER), TyQLSQLNode(tyqlIR.asInstanceOf[RelationOp], DB.EDB, -1))
            else
              throw new Exception(s"Unimplemented: Currently only recursive queries are supported in groupBy: $tyqlIR")
      case _ =>
        throw new Exception(s"Unimplemented: Currently only recursive queries are supported: $tyqlIR")


  def solveTyQL(tyqlAST: DatabaseAST[?], naive: Boolean = false): Set[Seq[StorageTerm]] =
    given JITOptions = defaultJITOptions
    //    println(s"jit opts==${defaultJITOptions.toBenchmark}")
//    debug("", () => s"solve $rId with options $defaultJITOptions")

    storageManager match
      case ddb: DuckDBStorageManager =>
        val toSolveRId = -1
        // use temporary context to generate tree, then later set finalSolve based on TyQL AST
        val initCtx = TyQLInterpreterContext(ddb, this, () => ???)
        val (irTree, finalNode) = toCaracIR(tyqlAST, naive)(using initCtx)

        val irCtx = TyQLInterpreterContext(ddb, this, () => finalNode.run(ddb).asInstanceOf[DuckDBEDB].execute_toSetOfSeq())

        println(s"IRTree from TyQL: ${ddb.printer.printIR(irTree)(using irCtx)}")
        println(s"FinalNode from TyQL: ${ddb.printer.printIR(finalNode)(using irCtx)}")
//        println(s"INIT STORAGE: ${storageManager.toString}")
        defaultJITOptions.mode match
          case Mode.Interpreted => solveInterpreted(irTree, irCtx)
          case Mode.Compiled => solveCompiled(irTree, irCtx)
          case Mode.JIT => solveJIT(irTree, irCtx)
      case _ => throw new Exception(s"Unimplemented: currently only DuckDBStorageManager is supported for TyQL frontend")
}