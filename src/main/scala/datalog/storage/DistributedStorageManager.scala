package datalog.storage
import datalog.dsl.{Atom, ColumnType, Constant, IntType, StringType, Term, Variable}
import datalog.execution.{AllIndexes, JoinIndexes}
import datalog.tools.Debug.debug
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DataType, DataTypes, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import DistributedCasts.*

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class DistributedStorageManager(override val ns: NS, spark: SparkSession) extends StorageManager(ns) {

  // keep track of columns so that we can create dataframes
  val headers: mutable.Map[RelationId, Seq[ColumnType]] = mutable.Map.empty

  private def columnType(col: ColumnType): DataType = col match {
    case IntType => DataTypes.IntegerType
    case StringType => DataTypes.StringType
  }

  private def schema(relationId: RelationId): StructType =
    StructType(headers(relationId).zipWithIndex.map((t, i) => StructField(i.toString, columnType(t), false)))

  private def makeEDB(rId: RelationId, c: Seq[Term]*): DistributedEDB =
    val sparkRows = c.map(org.apache.spark.sql.Row(_))
    val df = spark.createDataFrame(spark.sparkContext.parallelize(sparkRows), schema(rId))
    DistributedEDB(df)

  private type Database[K, V] = mutable.Map[K, V]
  private type FactDatabase = Database[RelationId, DistributedEDB]

  val derivedDB: Database[KnowledgeId, FactDatabase] = mutable.Map.empty
  val deltaDB: Database[KnowledgeId, FactDatabase] = mutable.Map.empty
  val edbs: FactDatabase = mutable.Map.empty
  var knownDbId: KnowledgeId = -1
  var newDbId: KnowledgeId = -1
  var dbId = 0

  val printer: Printer[this.type] = Printer[this.type](this)

  override def initRelation(rId: RelationId, name: String, columns: Seq[ColumnType]): Unit =
    headers(rId) = columns
    ns(rId) = name

  override def initEvaluation(): Unit = {
    // TODO: for now reinit with each solve()FactDatabase(), don't keep around previous discovered facts. Future work -> incremental
    dbId = 0
    knownDbId = dbId
    derivedDB.addOne(dbId, mutable.Map.empty)
    deltaDB.addOne(dbId, mutable.Map.empty)

    edbs.foreach((k, relation) => {
      deltaDB(dbId)(k) = makeEDB(k)
    }) // Delta-EDB is just empty sets
    dbId += 1

    newDbId = dbId
    derivedDB.addOne(dbId, mutable.Map.empty)
    deltaDB.addOne(dbId, mutable.Map.empty)

    edbs.foreach((k, relation) => {
      deltaDB(dbId)(k) = makeEDB(k)
    }) // Delta-EDB is just empty sets
    dbId += 1
  }

  override def insertEDB(rule: Atom): Unit =
    val initial = edbs.getOrElse(rule.rId, makeEDB(rule.rId))
    val newRow = makeEDB(rule.rId, rule.terms)
    edbs(rule.rId) = DistributedEDB(initial.df.union(newRow.df))

  override def getKnownDerivedDB(rId: RelationId): DistributedEDB =
    derivedDB(knownDbId).getOrElse(rId, edbs.getOrElse(rId, makeEDB(rId)))

  override def getNewDerivedDB(rId: RelationId): DistributedEDB =
    derivedDB(newDbId).getOrElse(rId, edbs.getOrElse(rId, makeEDB(rId)))

  override def getKnownDeltaDB(rId: RelationId): DistributedEDB =
    deltaDB(knownDbId).getOrElse(rId, edbs.getOrElse(rId, makeEDB(rId)))

  override def getNewDeltaDB(rId: RelationId): DistributedEDB =
    deltaDB(newDbId).getOrElse(rId, edbs.getOrElse(rId, makeEDB(rId)))

  override def getKnownIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug("Final IDB Result[known]: ", () => s"@$knownDbId")
    getKnownDerivedDB(rId).iterator.map(c => c.toSeq.asInstanceOf[Seq[Term]]).toSet

  override def getNewIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug("Final IDB Result[new]: ", () => s"@$newDbId")
    getNewDerivedDB(rId).iterator.map(c => c.toSeq.asInstanceOf[Seq[Term]]).toSet

  override def getEDBResult(rId: RelationId): Set[Seq[Term]] =
    edbs.getOrElse(rId, makeEDB(rId)).iterator.map(c => c.toSeq.asInstanceOf[Seq[Term]]).toSet

  override def resetKnownDerived(rId: RelationId, rules: EDB, prev: EDB): Unit =
    derivedDB(knownDbId)(rId) = DistributedEDB(asDistributedEDB(rules).df.union(asDistributedEDB(prev).df))

  override def resetNewDerived(rId: RelationId, rules: EDB, prev: EDB): Unit =
    derivedDB(newDbId)(rId) = DistributedEDB(asDistributedEDB(rules).df.union(asDistributedEDB(prev).df))

  override def resetNewDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(newDbId)(rId) = asDistributedEDB(rules)

  override def resetKnownDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(knownDbId)(rId) = asDistributedEDB(rules)

  override def clearNewDerived(): Unit =
    derivedDB(newDbId).keys.foreach(r => {
      derivedDB(newDbId)(r) = makeEDB(r)
    })

  override def swapKnowledge(): Unit = {
    val t = knownDbId
    knownDbId = newDbId
    newDbId = t
  }

  override def compareNewDeltaDBs(): Boolean =
    deltaDB(newDbId).exists((k, v) => !v.df.isEmpty)

  override def compareDerivedDBs(): Boolean =
    (derivedDB(knownDbId).keys == derivedDB(newDbId).keys) && derivedDB(knownDbId).forall((k, t1) => {
      val t2 = derivedDB(newDbId)(k)

      t1.df.except(t2.df).isEmpty && t2.df.except(t1.df).isEmpty
    })

  override def verifyEDBs(idbList: mutable.Set[RelationId]): Unit = {
    ns.rIds().foreach(rId =>
      if (!edbs.contains(rId) && !idbList.contains(rId)) // treat undefined relations as empty edbs
        edbs(rId) = makeEDB(rId)
    )
  }

  override def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB = ???

  override def projectHelper(input: EDB, k: JoinIndexes): EDB = ???

  override def joinProjectHelper(inputs: Seq[EDB], k: JoinIndexes, sortOrder: (Int, Int, Int)) = ???

  override def diff(lhs: EDB, rhs: EDB): EDB =
    DistributedEDB(asDistributedEDB(lhs).df.union(asDistributedEDB(rhs).df))

  override def union(edbs: Seq[EDB]): DistributedEDB =
    edbs.map(asDistributedEDB).reduceLeft((acc, e) => DistributedEDB(acc.df.union(e.df)))

  override def SPJU(rId: RelationId, keys: ArrayBuffer[JoinIndexes]): EDB = ???

  override def naiveSPJU(rId: RelationId, keys: ArrayBuffer[JoinIndexes]): EDB = ???

  override val allRulesAllIndexes: Database[RelationId, AllIndexes] = mutable.Map.empty

  override def getEmptyEDB(): EDB = ???

  override def edbContains(rId: RelationId): Boolean = edbs.contains(rId)

  override def getEDB(rId: RelationId): EDB = edbs(rId)

  override def getAllEDBS(): Database[RelationId, Any] = edbs.asInstanceOf[mutable.Map[RelationId, Any]]

  override def joinProjectHelper_withHash(inputs: Seq[EDB], rId: Int, hash: String, sortOrder: (Int, Int, Int)): EDB = ???
}