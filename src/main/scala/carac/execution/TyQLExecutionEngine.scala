package carac.execution

import carac.dsl.StorageAtom
import carac.execution.ast.ASTNode
import carac.execution.ir.OpCode.OTHER
import tyql.{DatabaseAST, Expr, GroupByQuery, MultiRecursiveRelationOp, NaryRelationOp, QueryIRNode, RelationOp, ResultTag, SelectAllQuery, TableLeaf, RecursiveIRVar}
import carac.execution.ir.{IROp, InsertDeltaNewIntoDerived, InterpreterContext, OpCode, ProgramOp, ResetDeltaOp, SequenceOp, TyQLSQLNode, TyQLToIROp}
import carac.storage.{DB, DatabaseType, DuckDBEDB, DuckDBStorageManager, EDB, RelationId, StorageManager, StorageTerm}
import tyql.Query.MultiRecursive

import scala.collection.mutable

class TyQLExecutionEngine(override val storageManager: StorageManager,
                          override val defaultJITOptions: JITOptions = JITOptions(mode = Mode.Interpreted)
                         ) extends StagedExecutionEngine(storageManager, defaultJITOptions) {
  import tyql.TreePrettyPrinter.*
  val idbSchemas: mutable.Map[RelationId, Seq[(String, String)]] = mutable.Map.empty

  def deUnion(tyqlIR: QueryIRNode): Seq[QueryIRNode] =
    tyqlIR match
      case NaryRelationOp(children, op, _, _, _) if op == "UNION" || op == "UNION ALL" =>
        children.flatMap(deUnion)
      case _ => Seq(tyqlIR)
  def resultTagToDatabaseType(t: ResultTag[?]): DatabaseType =
    t match
      case ResultTag.IntTag => DatabaseType.INTEGER
      case ResultTag.StringTag => DatabaseType.TEXT
      case _ => ???

  override def insertEDB(rule: StorageAtom, schema: Option[Seq[(String, DatabaseType)]]): Unit =
    if storageManager.schema.contains(rule.rId) then
      storageManager.insertEDB(rule)
    else
      schema match
        case Some(s) =>
          storageManager.declareTable(rule.rId, s)
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
        val schema = storageManager.schema(r)
        for i <- schema.indices do {
          if (allLocs.contains(start + i))
            relationCands.getOrElseUpdate(r, mutable.BitSet()).addOne(i)
        }
        start += schema.size
      }
      if relationCands.nonEmpty then storageManager.registerIndexCandidates(relationCands) // add at once to deduplicate ahead of time and avoid repeated calls
    }

    // TODO: for now skip presort and pre-generating hashes


  def extractSchemas(tyqlIR: QueryIRNode): Map[RelationId, (String, Seq[(String, DatabaseType)], Seq[QueryIRNode])] =
    tyqlIR match
      case MultiRecursiveRelationOp(alias, queries, finalQ, _, linear, schema, _) =>
        // generate IDB for each recursive relation defined within stratum
        alias.zipWithIndex.map((a, i) =>
          relCounter += 1
          val tempRID: RelationId = relCounter
          val subqueries = deUnion(queries(i))
          val base = subqueries.head
          // TODO: Move type tag into IR not just AST
          val schema = base.schema match
            case ResultTag.NamedTupleTag(names, types) =>
              names.zip(types).map((name, typ) => (name, resultTagToDatabaseType(typ)))
            case _ => ???
          (tempRID, (alias(i), schema, subqueries))
        ).toMap
      case _ => throw new Exception(s"Unimplemented: Currently only recursive queries are supported: $tyqlIR")

  // Generate IROPs for a backend that supports pushing SQL directly to the storage later
  def toCaracIR_sqlCompat(tyqlAST: DatabaseAST[?], naive: Boolean)(using ctx: InterpreterContext): (IROp[?], IROp[EDB]) =
    val tyqlIR = tyqlAST.toQueryIR
    tyqlIR match
      case MultiRecursiveRelationOp(aliases, queries, finalQIR, _, linearIR, _, _) =>
        val linear = linearIR.getOrElse(false)
        if linear then
          (SequenceOp(OTHER), TyQLSQLNode(tyqlIR.asInstanceOf[RelationOp], DB.EDB, -1))
        else
          val ruleSchemas = extractSchemas(tyqlIR)
          val ruleMap = ruleSchemas.map((rId, metadata) =>
            val (alias, schema, subqueries) = metadata
            if storageManager.ns.contains(rId) then
              throw new Exception(s"Using RelationId $rId for $alias, but already exists in storage")
            storageManager.ns(rId) = alias
            storageManager.declareTable(rId, schema)
            (rId, subqueries)
          )
          storageManager.verifyEDBs(ruleMap.keys.toSeq, None)
          val program = TyQLToIROp(true).generateTopLevelProgram(ruleMap, naive)
          val finalQ = TyQLSQLNode(finalQIR, DB.Derived, ruleMap.keys.head)
          storageManager.initEvaluation()
          (program, finalQ)
      case GroupByQuery(source, groupBy, having, _, _, _) =>
        source match
          case MultiRecursiveRelationOp(aliases, query, finalQ, carriedSymbols, linearIR, _, _) =>
            val linear = linearIR.getOrElse(false)
            if (linear)
              (SequenceOp(OTHER), TyQLSQLNode(tyqlIR.asInstanceOf[RelationOp], DB.EDB, -1))
            else
              throw new Exception(s"Unimplemented: Currently only recursive queries are supported in groupBy: $tyqlIR")
      case _ =>
        throw new Exception(s"Unimplemented: Currently only recursive queries are supported: $tyqlIR")
  def toCaracIR_sqlNonCompat(tyqlAST: DatabaseAST[?], naive: Boolean)(using ctx: InterpreterContext): (IROp[?], String) =
    val tyqlIR = tyqlAST.toQueryIR
    tyqlIR match
      case MultiRecursiveRelationOp(aliases, queries, finalQIR, _, linearIR, _, _) =>
        val linear = linearIR.getOrElse(false)
        if linear then
          ???
        else
          val ruleSchemas = extractSchemas(tyqlIR)
          val ruleMap = ruleSchemas.map((rId, metadata) =>
            val (alias, schema, subqueries) = metadata
            if storageManager.ns.contains(rId) then
              throw new Exception(s"Using RelationId $rId for $alias, but already exists in storage")
            storageManager.ns(rId) = alias
            storageManager.declareTable(rId, schema)
            (rId, subqueries)
          )
          storageManager.verifyEDBs(ruleMap.keys.toSeq, None)
          val program = TyQLToIROp(false).generateTopLevelProgram(ruleMap, naive)

          val toSolve = finalQIR match
            case SelectAllQuery(from, where, _, _, _) if from.size == 1 && where.isEmpty =>
              from.head match
                case TableLeaf(tableName, _, _, _) => tableName
                case RecursiveIRVar(pointsToAlias, _, _, _) => pointsToAlias
                case _ => ???
            case _ => ???
          storageManager.initEvaluation()
          (program, toSolve)
      case _ =>
        throw new Exception(s"Unimplemented: For non-SQL compat backends, only recursive queries are supported: $tyqlIR")


  def solveTyQL(tyqlAST: DatabaseAST[?], naive: Boolean = false): Set[Seq[StorageTerm]] =
    given JITOptions = defaultJITOptions
    //    println(s"jit opts==${defaultJITOptions.toBenchmark}")
//    debug("", () => s"solve $rId with options $defaultJITOptions")
    val (irTree, irCtx) = storageManager match
      case ddb: DuckDBStorageManager => // SQL pushdown compatible
        // use temporary context to generate tree, then later set finalSolve based on TyQL AST
        val initCtx = InterpreterContext(storageManager, this, None, () => ???)
        val (irTree, finalNode) = toCaracIR_sqlCompat(tyqlAST, naive)(using initCtx)
        val finalQ = () => finalNode.run(storageManager).asInstanceOf[DuckDBEDB].execute_toSetOfSeq()
        val irCtx = InterpreterContext(storageManager, this, None, finalQ)
        (irTree, irCtx)

      case _ =>
        // use temporary context to generate tree, then later set finalSolve based on TyQL AST
        val initCtx = InterpreterContext(storageManager, this, None, () => ???)
        val (irTree, toSolveName) = toCaracIR_sqlNonCompat(tyqlAST, naive)(using initCtx)
        val finalQ = () => storageManager.getIDBResult(storageManager.ns(toSolveName))
        val irCtx = InterpreterContext(storageManager, this, None, finalQ)
        (irTree, irCtx)

//        println(s"IRTree from TyQL: ${ddb.printer.printIR(irTree)(using irCtx)}")
//        println(s"FinalNode from TyQL: ${ddb.printer.printIR(finalNode)(using irCtx)}")
//        println(s"Schemas: ${ddb.schema.map((rId, s) => (ddb.ns(rId), s)).mkString("[\n\t", ",\n\t", "\n]")}")
//        println(s"INIT STORAGE: ${storageManager.toString}")
      val res = defaultJITOptions.mode match
        case Mode.Interpreted => solveInterpreted(irTree, irCtx)
        case Mode.Compiled => solveCompiled(irTree, irCtx)
        case Mode.JIT => solveJIT(irTree, irCtx)
      res
}