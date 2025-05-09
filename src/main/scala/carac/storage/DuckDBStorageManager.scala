package carac.storage

import carac.dsl.{Constant, StorageAtom, Term, Variable}
import carac.execution.AllIndexes
import carac.storage.DatabasePrefix.*
import carac.storage.StorageTerm
import tyql.SelectFlags.{ExprLevel, Top}
import tyql.{NaryRelationOp, QueryIRNode, RecursiveIRVar, RelationOp, SelectAllQuery, SelectQuery, TableLeaf, MultiRecursiveRelationOp}

import scala.jdk.CollectionConverters.*
import java.nio.file.{Files, Path, Paths}
import java.sql.{Connection, DriverManager, ResultSet, ResultSetMetaData}
import scala.collection.mutable.ArrayBuffer
import scala.collection.{immutable, mutable}

enum DatabasePrefix:
  case edb, tmp, delta, derived

enum DatabaseType:
  case INTEGER, TEXT, UNKNOWN

case class DuckDBEDB(rId: RelationId,
                     name: String,
                     prefix: DatabasePrefix,
                     run: String => ResultSet,
                     columnTypes: Option[Seq[(String, DatabaseType)]],
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

  def getWithColumnTypes(result: ResultSet): Set[Seq[StorageTerm]] =
    var rows = Set[Seq[StorageTerm]]()
    while (result.next()) {
      val row = columnTypes.get.map(_._2).zipWithIndex.map((typ, idx) =>
        typ match {
          case typ@DatabaseType.INTEGER => result.getInt(idx + 1)
          case typ@DatabaseType.TEXT => result.getString(idx + 1)
          case _ => throw new Exception(s"Internal error: unsupported type found in schema of $prefixedName: $columnTypes")
        })
      rows = rows + row
    }
    rows

  def getWithMD(rs: ResultSet): Set[Seq[StorageTerm]] =
    val meta: ResultSetMetaData = rs.getMetaData
    val columnCount = meta.getColumnCount
    val columnTypes = (1 to columnCount).map(meta.getColumnType)

    var rows = Set[Seq[StorageTerm]]()
    while (rs.next()) {
      val row = (0 until columnCount).map { i =>
        columnTypes(i) match {
          case java.sql.Types.INTEGER => rs.getInt(i + 1)
          case java.sql.Types.VARCHAR => rs.getString(i + 1)
          case _ => ???
        }
      }
      rows = rows + row
    }
    rows

  def execute_toSetOfSeq(): Set[Seq[StorageTerm]] =
    val result = run(cmd)
    if columnTypes.isEmpty then
      getWithMD(result)
    else
      getWithColumnTypes(result)

  override def factToString: String =
    val result = execute_toSetOfSeq().toSeq
    result.map(s => s.mkString("(", ", ", ")")).sorted.mkString("[", ", ", "]")

case class DuckDBDatabase(prefix: DatabasePrefix, run: String => ResultSet, update: String => Unit) extends Database[DuckDBEDB]: // TODO: handle benchmark-specific prefix to avoid dropping DB between each iteration
  var commandCache: mutable.ArrayBuffer[String] = mutable.ArrayBuffer[String]()
  val tables: mutable.Map[RelationId, DuckDBEDB] = mutable.Map[RelationId, DuckDBEDB]() // relationId => table name
  val indexCandidates: mutable.Map[RelationId, mutable.BitSet] = mutable.Map[RelationId, mutable.BitSet]() // relative position of atoms with constant or variable locations
  val idxTODO: mutable.Map[RelationId, mutable.BitSet] = mutable.Map[RelationId, mutable.BitSet]()

  def contains: RelationId => Boolean = tables.contains

  def execute_cache(): Unit =
    if commandCache.nonEmpty then
      val cmds = commandCache.mkString("", "; ", ";")
      update(cmds)
      commandCache.clear()

  def createIndex(rId: RelationId, positions: mutable.BitSet): Unit =
    val currentIdx = indexCandidates.getOrElseUpdate(rId, mutable.BitSet())
    positions.foreach(i =>
      if !currentIdx.contains(i) then
        currentIdx.addOne(i)
        // for now add single-column indexes
        if tables.contains(rId) then
          val idx = s"CREATE INDEX ${tables(rId).prefixedName}_c${i}_idx ON ${tables(rId).prefixedName} (c$i)"
//          println(s"creating index: $idx")
          commandCache.addOne(idx)
        else
          idxTODO.getOrElseUpdate(rId, mutable.BitSet()).addOne(i)
    )
    indexCandidates(rId) = currentIdx

  def initializeTable(rId: RelationId, name: String, schema: Seq[(String, DatabaseType)]): DuckDBDatabase =
    if (!contains(rId))
      val newEdb = DuckDBEDB(rId, name, prefix, run, Some(schema))
      tables(rId) = newEdb // use name for easier debuggablity
      val types = schema.map((s, t) =>
        val dbtype = t match
          case DatabaseType.UNKNOWN => throw new Exception(s"No Schema available for ${newEdb.prefixedName}: $schema") // TODO: potentially derive type from rule
          case _ => t
        s"$s $dbtype"
      ).mkString("(", ", ", ")")
      val schemaString = s"CREATE TABLE ${newEdb.prefixedName} $types"
      commandCache.addOne(schemaString)
      if idxTODO.contains(rId) then
        val idx = idxTODO(rId)
        idx.foreach(i =>
          val idx = s"CREATE INDEX ${newEdb.prefixedName}_c${i}_idx ON ${newEdb.prefixedName} (c$i)"
//          println(s"creating index: $idx")
          commandCache.addOne(idx)
        )
        idxTODO.remove(rId)
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

  def resetTableFrom(name: String, other: DuckDBEDB): DuckDBDatabase =
    commandCache.addOne(s"DELETE FROM ${prefix}_${name}")
    commandCache.addOne(s"INSERT INTO ${prefix}_${name} ${other.cmd}")
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
class DuckDBStorageManager(ns: NS = new NS(), indexed: Boolean = true) extends StorageManager(ns) {
  // "database", i.e. relationID => Relation
  var connection: Connection = null
  var initialized: Boolean = false
  val schema: mutable.Map[RelationId, Seq[(String, DatabaseType)]] = mutable.Map[RelationId, Seq[(String, DatabaseType)]]() // relationId => [(column name, type)*]
  connect()

  def getCSVFiles(directoryPath: String): Seq[Path] =
    val dirPath = Paths.get(directoryPath)
    if (Files.isDirectory(dirPath)) {
      Files.list(dirPath)
        .iterator()
        .asScala
        .filter(path => Files.isRegularFile(path) && path.toString.endsWith(".facts"))
        .toSeq
    } else {
      throw new Exception(s"$directoryPath is not a directory")
    }

  def loadFacts(datadir: String): Unit =
//    runUpdate(ddl)
    val allCSV = getCSVFiles(datadir)
    allCSV.foreach(csv =>
      val table = csv.getFileName().toString.replace(".facts", "")
      edbs.commandCache.addOne(s"COPY edb_$table FROM '$csv' (HEADER)")
      edbs.execute_cache()
      // print ok:
//      val checkQ = runQuery(s"SELECT COUNT(*) FROM edb_$table")
//      checkQ.next()
//      println(s"LOADED into edb_$table: ${checkQ.getInt(1)}")
    )

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

  val edbs: DuckDBDatabase = DuckDBDatabase(edb, runQuery, runUpdate)
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
  def registerIndexCandidates(cands: mutable.Map[RelationId, mutable.BitSet]): Unit =
    if indexed then cands.foreach((rId, idxs) =>
      databases.foreach(_.createIndex(rId, idxs))
    )

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
    val s = schema.getOrElse(rId, generateSchema(terms, throwOnVar = false))
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

  def initRelation(rId: RelationId, name: String, schemaOpt: Option[Seq[(String, DatabaseType)]]): Unit = {
    ns(rId) = name
    schemaOpt.foreach(s => schema(rId) = s)
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

  def cleanup(clearEdbs: Boolean = true): Unit = {
    if clearEdbs then
      databases.foreach(_.clear().execute_cache())
    else
      databases.drop(1).foreach(_.clear().execute_cache())
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
  def verifyEDBs(idbList: Seq[RelationId], ruleHashes: Option[mutable.Map[RelationId, mutable.ArrayBuffer[String]]]): Unit = {
    ns.rIds().foreach(rId =>
      if (!edbs.contains(rId) && !idbList.contains(rId))
        if (!schema.contains(rId))
          throw new Exception(s"Error: using EDB $rId (${ns(rId)}) but no known schema")
        edbs.initializeTable(rId, ns(rId), schema(rId)) // initialize empty table
    )
    ruleHashes.foreach(inferTypes)

    ns.rIds().foreach(rId =>
      if schema(rId).map(_._2).contains(DatabaseType.UNKNOWN) then throw new Exception(s"Error: could not infer schema of IDB $rId (${ns(rId)}): ${schema(rId)}")
      val allButEDBs = databases.dropRight(1) // all but EDB
      allButEDBs.foreach(db =>
        if !db.contains(rId) then db.initializeTable(rId, ns(rId), schema(rId))
      )
    )
  }
  def initializeIDBFromTyQL(rId: RelationId, schema: Seq[(String, DatabaseType)]): Unit =
    databases.dropRight(1).foreach(_.initializeTable(rId, ns(rId), schema))

  // Read & Write EDBs
  override def insertEDB(rule: StorageAtom): Unit = {
    val edbSchema = schema.getOrElse(rule.rId, generateSchema(rule.terms, throwOnVar = true))
    declareTable(rule.rId, edbSchema)
    if (!edbs.contains(rule.rId))
      databases.foreach(_.initializeTable(rule.rId, ns(rule.rId), schema(rule.rId)))
    edbs.insertRow(rule.rId, rule.terms).execute_cache() // for now greedily insert.
//    edbDomain.addAll(rule.terms)
  }
  def getEDB(rId: RelationId): DuckDBEDB =
    val r = edbs.get(rId, ns(rId))
    r

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
    databases(writeDeltaIdx).resetTableFrom(ns(rId), rules.asInstanceOf[DuckDBEDB]).execute_cache()

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
    val res = ddbedbs.reduceLeft((a: DuckDBEDB, b: DuckDBEDB) => a.union(b))
    res

  override def selectProjectJoinHelper(inputsEDB: Seq[EDB], rId: Int, hash: String, onlineSort: Boolean): DuckDBEDB =
    if onlineSort then throw new Exception("Unimplemented: online sort with DuckDB")

    val originalK = allRulesAllIndexes(rId)(hash)
    val inputs = inputsEDB.map(e => e.asInstanceOf[DuckDBEDB])
    //    println(s"Rule: ${printer.ruleToString(originalK.atoms)}")
    //    println(s"input rels: ${inputs.map(e => e.factToString).mkString("[", "*", "]")}")

    val k = originalK

    // (EDB index, EDB instance, relative column name, relative position, type)
    val edbColumnMapping: Seq[(Int, DuckDBEDB, String, Int, DatabaseType)] = inputs.zipWithIndex.flatMap { case (edb, edbIdx) =>
      edb.columnTypes match
        case Some(value) =>
          value.zipWithIndex.map { case ((colName, colType), colIdx) =>
            (edbIdx, edb, colName, colIdx, colType)
          }
        case None => throw new Exception(s"Cannot run SPJU on raw SQL EDB")
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

    DuckDBEDB(rId, ns(rId), tmp, runQuery, Some(newSchema), Some(plan))

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

  def execute_tyQLSQL(ir: RelationOp, db: DB, into: RelationId, diff: Boolean, translate: Boolean): EDB =
    val prefix = db match
      case DB.Derived => databases(derivedIdx).prefix
      case DB.Delta => databases(readDeltaIdx).prefix
      case DB.EDB => edb
    def translateSource(ir: RelationOp): RelationOp =
      val t = ir match
        case MultiRecursiveRelationOp(aliases, query, finalQ, carriedSymbols, ast) =>
          val translatedQuery = query.map(translateSource)
          val translatedFinalQ = translateSource(finalQ)
          MultiRecursiveRelationOp(aliases, translatedQuery, translatedFinalQ, carriedSymbols, ast)
        case RecursiveIRVar(ptA, a, ast) =>
          if db == DB.Derived || db == DB.Delta then RecursiveIRVar(s"${prefix}_$ptA", a, ast) else RecursiveIRVar(ptA, a, ast)
        case SelectAllQuery(from, where, overrideAlias, ast) => SelectAllQuery(from.map(translateSource), where, overrideAlias, ast)
        case SelectQuery(project, from, where, overrideAlias, ast) => SelectQuery(project, from.map(translateSource), where, overrideAlias, ast)
        case NaryRelationOp(children, op, _, ast) => NaryRelationOp(children.map( c => c match
          case r: RelationOp => translateSource(r)
          case _ => c
        ), op, Some(ir.alias), ast)
        case TableLeaf(tableName, _, ast) =>
          if db == DB.EDB then TableLeaf(s"${prefix}_$tableName", Some(ir.alias), ast) else TableLeaf(tableName, Some(ir.alias), ast)
        case _ => ir
      t.appendFlags(ir.flags)

    val sql = (if translate then translateSource(ir) else ir).appendFlag(ExprLevel).toSQLString().replaceAll("\"", "'")
    val diffedSQL = if diff then s"($sql) EXCEPT (SELECT * FROM ${databases(derivedIdx).get(into, ns(into)).prefixedName})" else sql

    DuckDBEDB(into, ns(into), prefix, runQuery, None, Some(diffedSQL))
}
