package datalog.storage
import datalog.dsl.{Atom, ColumnType, Constant, IntType, StringType, Term, Variable}
import datalog.execution.JoinIndexes
import datalog.tools.Debug.debug
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DataType, DataTypes, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class DistributedStorageManager(ns: NS, spark: SparkSession) extends StorageManager(ns) {
  type StorageTerm = Term
  type StorageVariable = Variable
  type StorageConstant = Constant
  type Row[+T] = Seq[T]
  type Table[T] = Iterable[T]

  // keep track of columns so that we can create dataframes
  val headers: mutable.Map[RelationId, Seq[ColumnType]] = mutable.Map.empty

  class DistributedRelation[T](val df: DataFrame) extends Iterable[Seq[T]] {
    override def iterator: Iterator[Seq[T]] = df.collect().iterator.map(r => r.toSeq.asInstanceOf[Seq[T]])
  }

  type Relation[T] = DistributedRelation[T]

  def columnType(col: ColumnType): DataType = col match {
    case IntType => DataTypes.IntegerType
    case StringType => DataTypes.StringType
  }

  def schema(relationId: RelationId): StructType =
    StructType(headers(relationId).zipWithIndex.map((t, i) => StructField(i.toString, columnType(t), false)))

  def EDB(rId: RelationId, c: Row[StorageTerm]*): EDB =
    val sparkRows = c.map(org.apache.spark.sql.Row(_))
    val df = spark.createDataFrame(spark.sparkContext.parallelize(sparkRows), schema(rId))
    DistributedRelation(df)

  type Database[K, V] = mutable.Map[K, V]
  type FactDatabase = Database[RelationId, EDB]

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
      deltaDB(dbId)(k) = EDB(k)
    }) // Delta-EDB is just empty sets
    dbId += 1

    newDbId = dbId
    derivedDB.addOne(dbId, mutable.Map.empty)
    deltaDB.addOne(dbId, mutable.Map.empty)

    edbs.foreach((k, relation) => {
      deltaDB(dbId)(k) = EDB(k)
    }) // Delta-EDB is just empty sets
    dbId += 1
  }

  override def insertEDB(rule: Atom): Unit =
    val initial = edbs.getOrElse(rule.rId, EDB(rule.rId))
    val newRow = EDB(rule.rId, rule.terms)
    edbs(rule.rId) = DistributedRelation(initial.df.union(newRow.df))

  override def edb(rId: RelationId): EDB = edbs(rId)

  override def getKnownDerivedDB(rId: RelationId): EDB =
    derivedDB(knownDbId).getOrElse(rId, edbs.getOrElse(rId, EDB(rId)))

  override def getNewDerivedDB(rId: RelationId): EDB =
    derivedDB(newDbId).getOrElse(rId, edbs.getOrElse(rId, EDB(rId)))

  override def getKnownDeltaDB(rId: RelationId): EDB =
    deltaDB(knownDbId).getOrElse(rId, edbs.getOrElse(rId, EDB(rId)))

  override def getNewDeltaDB(rId: RelationId): EDB =
    deltaDB(newDbId).getOrElse(rId, edbs.getOrElse(rId, EDB(rId)))

  override def getKnownIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug("Final IDB Result[known]: ", () => s"@$knownDbId")
    getKnownDerivedDB(rId).map(s => s.toSeq).toSet

  override def getNewIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug("Final IDB Result[new]: ", () => s"@$newDbId")
    getNewDerivedDB(rId).map(s => s.toSeq).toSet

  override def getEDBResult(rId: RelationId): Set[Seq[Term]] =
    edbs.getOrElse(rId, EDB(rId)).map(s => s.toSeq).toSet

  override def resetKnownDerived(rId: RelationId, rules: EDB, prev: EDB): Unit =
    derivedDB(knownDbId)(rId) = DistributedRelation(rules.df.union(prev.df))

  override def resetNewDerived(rId: RelationId, rules: EDB, prev: EDB): Unit =
    derivedDB(newDbId)(rId) = DistributedRelation(rules.df.union(prev.df))

  override def resetNewDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(newDbId)(rId) = rules

  override def resetKnownDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(knownDbId)(rId) = rules

  override def clearNewDerived(): Unit =
    derivedDB(newDbId).keys.foreach(r => {
      derivedDB(newDbId)(r) = EDB(r)
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
        edbs(rId) = EDB(rId)
    )
  }

  override def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB = ???

  override def projectHelper(input: EDB, k: JoinIndexes): EDB = ???

  override def joinProjectHelper(inputs: Seq[EDB], k: JoinIndexes): EDB = ???

  override def diff(lhs: EDB, rhs: EDB): EDB =
    DistributedRelation(lhs.df.except(rhs.df))

  override def union(edbs: Seq[EDB]): EDB =
    edbs.reduceLeft((acc, e) => DistributedRelation(acc.df.union(e.df)))

  override def SPJU(rId: RelationId, keys: ArrayBuffer[JoinIndexes]): EDB = ???

  override def naiveSPJU(rId: RelationId, keys: ArrayBuffer[JoinIndexes]): EDB = ???
}