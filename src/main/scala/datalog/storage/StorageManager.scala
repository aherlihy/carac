package datalog.storage

import datalog.dsl.Atom

import scala.collection.mutable
import scala.collection.immutable

trait StorageManager {
  /* A bit repetitive to have these types also defined in dsl but good to separate
   * user-facing API class with internal storage */
  type StorageVariable
  type StorageConstant
  type StorageAtom
  type Row [+T] <: IndexedSeq[T] with immutable.IndexedSeqOps[T, Row, Row[T]]
  type Table[T] <: mutable.ArrayBuffer[T]
  type Relation[T] <: Table[Row[T]]

  type StorageTerm = StorageVariable | StorageConstant
  type EDB = Relation[StorageTerm]
  type IDB = Relation[StorageAtom]
  type Database[K, V] <: mutable.Map[K, V]
  type FactDatabase <: Database[Int, EDB]
  type RuleDatabase <: Database[Int, IDB]

  val incrementalDB: Database[Int, FactDatabase]
  val deltaDB: Database[Int, FactDatabase]
  val edbs: FactDatabase
  val idbs: RuleDatabase


  /**
   * Wrapper object for join keys for IDB rules
   *
   * @param varIndexes - indexes of repeated variables within the body
   * @param constIndexes - indexes of constants within the body
   * @param projIndexes - indexes of variables within the body that are present in the head
   * @param deps - set of relations directly depended upon by this rule
   */
  case class JoinIndexes(varIndexes: IndexedSeq[IndexedSeq[Int]],
                         constIndexes: Map[Int, StorageConstant],
                         projIndexes: IndexedSeq[Int],
                         deps: Seq[Int]) {
    override def toString: String =
      "{ variables:" + varIndexes.map(s => s.mkString("(", ", ", ")")).mkString("[", ", ", "]") +
        ", consts:" + constIndexes.mkString("[", ", ", "]") +
        ", project:" + projIndexes.mkString("[", ", ", "]") +
        ", srcEDB:" + deps.mkString("[", ", ", "]") + " }"
  }

  def initRelation(rId: Int, name: String): Unit

  def insertEDB(rule: StorageAtom): Unit

  def insertIDB(rId: Int, rule: Row[StorageAtom]): Unit

  def idb(rId: Int): IDB

  def edb(rId: Int): EDB

  def spju(rId: Int, keys: Seq[JoinIndexes], sourceQueryId: Int): EDB

  def getIncrementDB(rId: Int, queryId: Int): EDB

  def swapIncrDBs(qId1: Int, qId2: Int): Unit
  def swapDeltaDBs(qId1: Int, qId2: Int): Unit

//  def tableToString[T](r: Relation[T]): String

  def resetIncrEDB(rId: Int, rules: EDB, queryId: Int): Unit
  def resetDeltaEDB(rId: Int, rules: EDB, queryId: Int): Unit
  def compareDeltaDBs(qId1: Int, qId2: Int): Boolean
  def compareIncrDBs(qId1: Int, qId2: Int): Boolean
}
