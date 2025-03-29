package carac.storage

import carac.dsl.{Constant, StorageAtom, Term, Variable}
import carac.execution.AllIndexes
import carac.storage.DatabasePrefix.*
import carac.storage.StorageTerm

import java.sql.{Connection, DriverManager, ResultSet}
import scala.collection.mutable.ArrayBuffer
import scala.collection.{immutable, mutable}

enum DatabasePrefix:
  case edb, tmp, delta, derived

enum DatabaseType:
  case INTEGER, TEXT, UNKNOWN

case class DuckDBEDB(rId: RelationId, name: String, prefix: DatabasePrefix,
                     run: String => ResultSet, columnTypes: Seq[(String, DatabaseType)],
                     cmdOpt: Option[String] = None) extends EDB:
  val prefixedName: String = s"${prefix}_$name"
  val cmd: String = cmdOpt.getOrElse(s"(SELECT * FROM $prefixedName)")
  override def length: Int =
    val query = s"SELECT COUNT(*) FROM ${cmdOpt.getOrElse(prefixedName)}"
    val result = run(query)
    result.next()
    try
      result.getInt(1)
    catch
      case e: Exception =>
        throw throw new Exception(s"Error running length query: \"$query\" with ${e.getMessage}")

  def union(other: DuckDBEDB): DuckDBEDB =
    DuckDBEDB(rId, name, prefix, run, columnTypes, Some(s"$cmd UNION ${other.cmd}"))

  def execute_toSetOfSeq(): Set[Seq[StorageTerm]] =
    val result = run(cmd)
    var rows = Set[Seq[StorageTerm]]()
    while (result.next()) {
      val row = columnTypes.map(_._2).zipWithIndex.map((typ, idx) =>
        typ match {
          case typ@DatabaseType.INTEGER => result.getInt(idx + 1)
          case typ@DatabaseType.TEXT => result.getString(idx + 1)
          case _ => throw new Exception(s"Internal error: unsupported type found in schema of $prefixedName: $columnTypes")
        })
      rows = rows + row
    }
    rows

  override def factToString: String =
    val result = execute_toSetOfSeq().toSeq
    result.map(s => s.mkString("(", ", ", ")")).sorted.mkString("[", ", ", "]")

case class DuckDBDatabase(prefix: DatabasePrefix, run: String => ResultSet, update: String => Unit) extends Database[DuckDBEDB]: // TODO: handle benchmark-specific prefix to avoid dropping DB between each iteration
  var commandCache: mutable.ArrayBuffer[String] = mutable.ArrayBuffer[String]()
  val tables: mutable.Map[RelationId, DuckDBEDB] = mutable.Map[RelationId, DuckDBEDB]() // relationId => table name
  val indexCandidates: mutable.Map[RelationId, mutable.BitSet] = mutable.Map[RelationId, mutable.BitSet]() // relative position of atoms with constant or variable locations

  def contains: RelationId => Boolean = tables.contains

  def execute_cache(): Unit =
    if commandCache.nonEmpty then
      val cmds = commandCache.mkString("", "; ", ";")
      update(cmds)
      commandCache.clear()

  def initializeTable(rId: RelationId, name: String, schema: Seq[(String, DatabaseType)]): DuckDBDatabase =
    if (!contains(rId))
      val newEdb = DuckDBEDB(rId, name, prefix, run, schema)
      tables(rId) = newEdb // use name for easier debuggablity
      indexCandidates.getOrElseUpdate(rId, mutable.BitSet())
      val types = schema.map((s, t) =>
        val dbtype = t match
          case DatabaseType.UNKNOWN => throw new Exception(s"No Schema available for ${newEdb.prefixedName}: $schema") // TODO: potentially derive type from rule
          case _ => t
        s"$s $dbtype"
      ).mkString("(", ", ", ")")
      val schemaString = s"CREATE TABLE ${newEdb.prefixedName} $types"
      commandCache.addOne(schemaString)
    this

  def insertRow(rId: RelationId, terms: Seq[Term]): DuckDBDatabase =
    val values = terms.map(t => t match {
      case c: Constant => c match
        case i: String => s"'$i'"
        case s: Int => s
      case v: Variable => throw new Exception("Variable in EDB head")
    })
    commandCache.addOne(s"INSERT INTO ${tables(rId).prefixedName} VALUES (${values.mkString(", ")})")
    this

  def clear(): DuckDBDatabase =
    commandCache.addAll(tables.values.map(t => s"DELETE FROM ${t.prefixedName}"))
    this

  def insertAllFrom(other: DuckDBDatabase): DuckDBDatabase =
    other.tables.values.map(t =>
      commandCache.addOne(s"INSERT INTO ${prefix}_${t.name} ${t.cmd}")
    )
    this

  def resetTableFrom(other: DuckDBEDB): DuckDBDatabase =
    commandCache.addOne(s"DELETE FROM ${prefix}_${other.name}")
    commandCache.addOne(s"INSERT INTO ${prefix}_${other.name} ${other.cmd}")
    this

  def execute_nonEmpty(): Boolean =
    if commandCache.nonEmpty then throw new Exception(s"Executing query with non-empty cmd cache: ${commandCache.mkString(", ")}")
    val cmds = tables.values.map(_.cmd)
    cmds.map(run).exists(rs => rs.next())

  override def toSeq: Seq[(RelationId, DuckDBEDB)] = tables.toSeq

  def get(rId: RelationId, name: String): DuckDBEDB = tables.getOrElse(rId, throw new Exception(s"Table $name ($rId) not found in $prefix"))

/**
 * Collections-based storage manager, index or no index.
 */
class DuckDBStorageManager(ns: NS = new NS()) extends StorageManager(ns) {
  // "database", i.e. relationID => Relation
  var connection: Connection = null
  var initialized: Boolean = false
  val schema: mutable.Map[RelationId, Seq[(String, DatabaseType)]] = mutable.Map[RelationId, Seq[(String, DatabaseType)]]() // relationId => [(column name, type)*]
  connect()

  private def connect(): Unit =
    Class.forName("org.duckdb.DuckDBDriver")
    connection = DriverManager.getConnection("jdbc:duckdb:")

  private def close() = connection.close()

  val runQuery: String => ResultSet = sqlString =>
    val lastStmt = connection.createStatement()
    //    println(s"running query: $sqlString")
    //    lastStmt.setQueryTimeout(timeout)
    try
      lastStmt.executeQuery(sqlString)
    catch
      case e: Exception =>
        throw throw new Exception(s"Error running query: \"$sqlString\" with ${e.getMessage}")

  val runUpdate: String => Unit = sqlString =>
//    println(s"running update: $sqlString")
    val lastStmt = connection.createStatement()
//    lastStmt.setQueryTimeout(timeout)
    try
      lastStmt.executeUpdate(sqlString)
    catch
      case e: Exception =>
        throw throw new Exception(s"Error running update: \"$sqlString\" with ${e.getMessage}")

//  val edbDomain: mutable.Set[StorageTerm] = mutable.Set.empty // incrementally grow the total domain of all EDBs, used for calculating complement of negated predicates

  protected val edbs: DuckDBDatabase = DuckDBDatabase(edb, runQuery, runUpdate)
  protected val tmpDB: DuckDBDatabase = DuckDBDatabase(tmp, runQuery, runUpdate)
  protected val derivedDB: DuckDBDatabase = DuckDBDatabase(derived, runQuery, runUpdate)
  protected val deltaDB: DuckDBDatabase = DuckDBDatabase(delta, runQuery, runUpdate)

  val databases: Array[DuckDBDatabase] = Array(derivedDB, deltaDB, tmpDB, edbs)

  var readDeltaIdx: Int = -1
  var writeDeltaIdx: Int = -1
  var derivedIdx: Int = -1

  val allRulesAllIndexes: mutable.Map[RelationId, AllIndexes] = mutable.Map.empty // Index => position

  // Update metadata if aliases are discovered
  def updateAliases(aliases: mutable.Map[RelationId, RelationId]): Unit = {
//    aliases.foreach((k, v) =>
//      if (relationSchema.contains(k) && relationSchema.contains(v) && relationSchema(k) != relationSchema(v))
//        throw new Exception(s"Error: registering relations ${ns(k)} and ${ns(v)} as aliases but have different arity (${relationSchema(k)} vs. ${relationSchema(v)})")
//      relationSchema.getOrElseUpdate(k, relationSchema.getOrElse(v, throw new Exception(s"No arity available for either ${ns(k)} or ${ns(v)}")))
//      indexCandidates(k) = indexCandidates.getOrElseUpdate(k, mutable.BitSet()).addAll(indexCandidates.getOrElse(v, mutable.BitSet()))
//      indexCandidates(v) = mutable.BitSet().addAll(indexCandidates(k))
//    )
  }

  // Store relative positions of shared variables as candidates for potential indexes
  def registerIndexCandidates(cands: mutable.Map[RelationId, mutable.BitSet]): Unit = {
    cands.foreach((rId, idxs) =>
      databases.foreach(_.indexCandidates.getOrElseUpdate(rId, mutable.BitSet()).addAll(idxs))
    )
  }

  def generateSchema(terms: Seq[Term], throwOnVar: Boolean): Seq[(String, DatabaseType)] =
    terms.zipWithIndex.map((t, i) => (s"c$i", t match {
      case c: Constant => c match
        case _: Int => DatabaseType.INTEGER
        case _: String => DatabaseType.TEXT
      case _: Variable =>
        if throwOnVar then
          throw new Exception("Variable declared in EDB head")
        else DatabaseType.UNKNOWN
    }))

  override def declareTable(rId: RelationId, s: Seq[(String, DatabaseType)]): Unit =
    if schema.contains(rId) then
      val precise = s.zipWithIndex.map((next, i) =>
        val previous = schema(rId)(i)
        val pIdx = previous._1
        val pType = previous._2
        val nIdx = next._1
        val nType = next._2
        if pIdx != nIdx then throw new Exception(s"Derived relation $rId declared with schema $s but previously declared with schema ${schema(rId)}")
        if pType == DatabaseType.UNKNOWN then
          (pIdx, nType)
        else if nType == DatabaseType.UNKNOWN then
          (pIdx, pType)
        else if pType != nType then
          throw new Exception(s"Derived relation $rId declared with schema $s but previously declared with schema ${schema(rId)}")
        else
          (pIdx, pType)
      )
      schema(rId) = precise
    else
      schema(rId) = s

  // Derive relation schema. In the future can require it to be declared, but for now derived using inference.
  def registerRelationSchema(rId: RelationId, terms: Seq[Term], hashOpt: Option[String]): Unit =
    val s = generateSchema(terms, throwOnVar = false)
    declareTable(rId, s)
    hashOpt.foreach(hash =>
      try {
        inferSchema(rId, hash)
      } catch {
        case e: Exception => {} // will try again after all IDBs and EDBs are declared
      }
    )

  def inferSchema(rId: RelationId, ruleHash: String): Unit =
    if (schema(rId).map(_._2).contains(DatabaseType.UNKNOWN))
      val k = allRulesAllIndexes
        .getOrElse(rId, throw new Exception(s"Internal error: no JoinIndexes for ${ns(rId)}"))
        .getOrElse(ruleHash, throw new Exception(s"Internal error: no JoinIndexes for ${ns(rId)} with hash $ruleHash"))

      val depTypes: Seq[DatabaseType] = k.deps.flatMap { case (_, depRId) =>
        schema(depRId).map(_._2)
      }

      def getTypeFromIdx(varIdx: Int, matches: Seq[Int]): DatabaseType =
        depTypes.lift(varIdx) match {
          case None => throw new Exception(s"Invalid variable index $varIdx in $k")
          case types if types.exists(_ != DatabaseType.UNKNOWN) =>
            types.find(_ != DatabaseType.UNKNOWN).get
          case _ =>
            matches.flatMap(idx => Some(getTypeFromIdx(idx, matches.filter(_ != idx))))
              .find(_ != DatabaseType.UNKNOWN)
              .getOrElse(DatabaseType.UNKNOWN)
        }

      val inferredTypes = k.projIndexes.map {
        case ("v", varIdx: Int) =>
          val matchingIdx = k.varIndexes.find(_.contains(varIdx)).getOrElse(Seq()).filter(_ != varIdx)
          getTypeFromIdx(varIdx, matchingIdx)
        case ("c", constValue) =>
          constValue match
            case _: Int => DatabaseType.INTEGER
            case _: String => DatabaseType.TEXT
        case _ => throw new Exception(s"Internal error: invalid projection type in ${k.projIndexes}")
      }
      declareTable(rId, inferredTypes.zipWithIndex.map((t, i) => (s"c$i", t)))

  val printer: Printer[this.type] = Printer[this.type](this)

  def initRelation(rId: RelationId, name: String): Unit = {
    ns(rId) = name
  }
  /**
   * Initialize derivedDB to clone EDBs, initialize deltaDB to empty for both new and known
   *
   * @return
   */
  def initEvaluation(): Unit = {
    databases.foreach(_.execute_cache())

    iteration = 0

    writeDeltaIdx = 1     // relation to write
    readDeltaIdx = 2    // delta relations (e.g. bases)
    derivedIdx = 0  // derived relations
    initialized = true

    databases(derivedIdx).clear().execute_cache()
    databases(readDeltaIdx).clear().execute_cache()
    databases(writeDeltaIdx).clear().execute_cache()

    databases(derivedIdx).insertAllFrom(edbs).execute_cache()

//    println(s"${
//      databases.map(db => db.prefix.toString + ": " +
//        db.tables.values.map(edb => s"${edb.name}: ${edb.columnTypes.mkString("=")}").mkString("[", ", ", "]")
//      ).mkString("", ",\n", "")
//    }")
  }

  def inferTypes(ruleHashes: mutable.Map[RelationId, mutable.ArrayBuffer[String]]): Unit = {
    // Quick and dirty type inference
    var fixPoint = true
    var it = 0
    while (fixPoint)
      it += 1

      val typesBefore = schema.values.flatMap(_.map(_._2))
      ruleHashes.foreach((rId, hashes) =>
        hashes.foreach(hash =>
          inferSchema(rId, hash)
        )
      )
      val typesAfter = schema.values.flatMap(_.map(_._2))
      fixPoint = typesBefore != typesAfter
  }

  /**
   * Verify that all EDBs are initialized, and if not, initialize them.
   * @param idbList
   */
  def verifyEDBs(ruleHashes: mutable.Map[RelationId, mutable.ArrayBuffer[String]]): Unit = {
    val idbList = ruleHashes.keys.to(mutable.Set)
    ns.rIds().foreach(rId =>
      if (!edbs.contains(rId) && !idbList.contains(rId))
        if (!schema.contains(rId))
          throw new Exception(s"Error: using EDB $rId (${ns(rId)}) but no known schema")
        edbs.initializeTable(rId, ns(rId), schema(rId)) // initialize empty table
    )
    inferTypes(ruleHashes)

    ns.rIds().foreach(rId =>
      if schema(rId).map(_._2).contains(DatabaseType.UNKNOWN) then throw new Exception(s"Error: could not infer schema of IDB $rId (${ns(rId)}): ${schema(rId)}")
      val allButEDBs = databases.dropRight(1) // all but EDB
      allButEDBs.foreach(db =>
        if !db.contains(rId) then db.initializeTable(rId, ns(rId), schema(rId))
      )
    )
  }

  // Read & Write EDBs
  override def insertEDB(rule: StorageAtom): Unit = {
    val edbSchema = generateSchema(rule.terms, throwOnVar = true)
    declareTable(rule.rId, edbSchema)
    if (!edbs.contains(rule.rId))
      databases.foreach(_.initializeTable(rule.rId, ns(rule.rId), schema(rule.rId)))
    edbs.insertRow(rule.rId, rule.terms).execute_cache() // for now greedily insert.
//    edbDomain.addAll(rule.terms)
  }
  def getEDB(rId: RelationId): DuckDBEDB = edbs.get(rId, ns(rId))
  def edbContains(rId: RelationId): Boolean = edbs.contains(rId)
  def getAllEDBS(): mutable.Map[RelationId, Any] = edbs.tables.asInstanceOf[mutable.Map[RelationId, Any]]

  // Read intermediate results
  def getDerivedDB(rId: RelationId): DuckDBEDB =
    if !schema.contains(rId) then throw new Exception(s"Internal error: relation $rId (${ns(rId)}) has no schema")
    databases(derivedIdx).get(rId, ns(rId))

  def getDeltaDB(rId: RelationId): DuckDBEDB =
    if !schema.contains(rId) then throw new Exception(s"Internal error: relation $rId (${ns(rId)}) has no schema")
    databases(readDeltaIdx).get(rId, ns(rId))

  // Read final results
  def getIDBResult(rId: RelationId): Set[Seq[StorageTerm]] =
    databases(derivedIdx).get(rId, ns(rId)).execute_toSetOfSeq()
  def getEDBResult(rId: RelationId): Set[Seq[StorageTerm]] =
    if (edbs.contains(rId))
      edbs.get(rId, ns(rId)).execute_toSetOfSeq()
    else
      Set()

  def insertDeltaIntoDerived(): Unit =
    databases(derivedIdx).insertAllFrom(databases(writeDeltaIdx)).execute_cache()

  def writeNewDelta(rId: RelationId, rules: EDB): Unit =
    databases(writeDeltaIdx).resetTableFrom(rules.asInstanceOf[DuckDBEDB]).execute_cache()

  def clearPreviousDeltas(): Unit =
    databases(writeDeltaIdx).clear().execute_cache()

  def swapReadWriteDeltas(): Unit = {
    iteration += 1
    val t = readDeltaIdx
    readDeltaIdx = writeDeltaIdx
    writeDeltaIdx = t
  }
  def deltasEmpty(): Boolean =
    databases(writeDeltaIdx).execute_nonEmpty()

  def union(edbs: Seq[EDB]): EDB =
    val ddbedbs = edbs.map(e => e.asInstanceOf[DuckDBEDB])
    ddbedbs.reduceLeft((a: DuckDBEDB, b: DuckDBEDB) => a.union(b))

  override def selectProjectJoinHelper(inputsEDB: Seq[EDB], rId: Int, hash: String, onlineSort: Boolean): DuckDBEDB =
    if onlineSort then throw new Exception("Unimplemented: online sort with DuckDB")

    val originalK = allRulesAllIndexes(rId)(hash)
    val inputs = inputsEDB.map(e => e.asInstanceOf[DuckDBEDB])
    //    println(s"Rule: ${printer.ruleToString(originalK.atoms)}")
    //    println(s"input rels: ${inputs.map(e => e.factToString).mkString("[", "*", "]")}")

    val k = originalK

    // (EDB index, EDB instance, relative column name, relative position, type)
    val edbColumnMapping: Seq[(Int, DuckDBEDB, String, Int, DatabaseType)] = inputs.zipWithIndex.flatMap { case (edb, edbIdx) =>
      edb.columnTypes.zipWithIndex.map { case ((colName, colType), colIdx) =>
        (edbIdx, edb, colName, colIdx, colType)
      }
    }

    val projectAliasesTypes = k.projIndexes.map {
      case ("v", varIdx: Int) =>
        edbColumnMapping.lift(varIdx)
          .map { case (edbIdx, edb, colName, colPos, colType) =>
            if colType == DatabaseType.UNKNOWN then throw new Exception(s"Cannot derive type for var pos $varIdx ${edb.name}$edbIdx.$colName")
            (s"${edb.name}$edbIdx.$colName", colType)
          }
          .getOrElse(throw new Exception(s"Invalid variable index $varIdx in $k"))

      case ("c", constValue) =>
        constValue match
          case _: Int => (constValue.toString, DatabaseType.INTEGER)
          case _: String => (s"'$constValue'", DatabaseType.TEXT)
      case _ => throw new Exception(s"Internal error: invalid projection type in ${k.projIndexes}")
    }
    val projectClause = projectAliasesTypes.map(_._1).mkString(", ")

    val fromClause = inputs.zipWithIndex.map { case (edb, i) =>
      edb.cmdOpt.getOrElse(edb.prefixedName) + s" AS ${edb.name}$i"
    }.mkString(", ")

    val joinClause = if k.varIndexes.nonEmpty then
      k.varIndexes.flatMap { indexes =>
        indexes.sliding(2).map { case Seq(a, b) =>
          (edbColumnMapping.lift(a), edbColumnMapping.lift(b)) match
            case (Some((edbIdxA, edbA, colA, _, _)), Some((edbIdxB, edbB, colB, _, _))) =>
              s"${edbA.name}$edbIdxA.$colA = ${edbB.name}$edbIdxB.$colB"
            case _ => throw new Exception(s"Invalid variable indexes $indexes in $k")
        }
      } else Seq()
    val constClause = if k.constIndexes.nonEmpty then
      k.constIndexes.map { case (pos, constValue) =>
        edbColumnMapping.lift(pos) match
          case Some((edbIdx, edb, colName, _, _)) =>
            val constStr = constValue match
              case _: Int => constValue.toString
              case _: String => s"'$constValue'"
            s"${edb.name}$edbIdx.$colName = $constStr"
          case None => throw new Exception(s"Invalid constant index $pos in $k")
      } else Seq()

    val whereClause = if k.constIndexes.isEmpty && k.varIndexes.isEmpty then "" else s" WHERE ${(constClause ++ joinClause).mkString(" AND ")}"

    val plan = s"(SELECT $projectClause FROM $fromClause$whereClause EXCEPT (SELECT * FROM ${databases(derivedIdx).get(rId, ns(rId)).prefixedName}))"

    val newSchema = projectAliasesTypes.map(_._2).zipWithIndex.map((s, i) => (s"c$i", s))

    DuckDBEDB(rId, ns(rId), tmp, runQuery, newSchema, Some(plan))

  def resultSetToString(resultSet: ResultSet, meta: String = ""): String =
    val metaData = resultSet.getMetaData
    val columnCount = metaData.getColumnCount

    val header = (1 to columnCount).map(metaData.getColumnName).mkString("|")
    var rows = Seq[String]()

    while (resultSet.next()) {
      val row = (1 to columnCount).map(resultSet.getString).mkString("(", ", ", ")")
      rows = rows :+ row
    }
    val res = s"ResultSet $meta [$header]: ${rows.mkString("{", ",", "}")}"
    resultSet.close()
    res

  // Printer methods
  override def toString() = {
    def printHelperRelation(db: DuckDBDatabase, prefix: DatabasePrefix): String = {
      s"\n $prefix : \n  ${printer.edbToString(db)}"
    }

    "+++++\n" +
      "EDB:\n  " +  printer.edbToString(edbs) +
      "\nDERIVED:" + printer.edbToString(databases(derivedIdx)) +
      "\nDELTA:" + printer.edbToString(databases(readDeltaIdx)) +
      "\nNEXT:" + printer.edbToString(databases(writeDeltaIdx)) +
      "\n+++++"
  }

  /**
   * Compute Dom * Dom * ... arity # times
   */
  override def getComplement(rId: RelationId, arity: Int): DuckDBEDB = ??? /*{
    // short but inefficient
    val res = List.fill(arity)(edbDomain).flatten.combinations(arity).flatMap(_.permutations).toSeq
    empty(arity, ns(rId), indexCandidates(rId), mutable.BitSet()).addAll(
      mutable.ArrayBuffer.from(res.map(r => CollectionsRow(ArraySeq.from(r))))
    )
  }*/
  override def addConstantsToDomain(constants: Seq[StorageTerm]): Unit = {
//    edbDomain.addAll(constants)
  }
  // Only used with negation, otherwise merged with project.
  override def diff(lhsEDB: EDB, rhsEDB: EDB): EDB = ???
//    val lhs = if indexed then IndexedCollectionsCasts.asIndexedCollectionsEDB(lhsEDB) else CollectionsCasts.asCollectionsEDB(lhsEDB)
//    lhs.diff(rhsEDB)
}
