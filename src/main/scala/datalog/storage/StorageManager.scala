package datalog.storage

import datalog.dsl.Atom

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, Map}
import scala.collection.immutable.IndexedSeqOps

trait StorageManager {
  /* A bit repetitive to have these types also defined in dsl but good to separate
   * user-facing class with internal storage */
  type StorageVariable
  type StorageConstant
  type StorageTerm = StorageVariable | StorageConstant
  type StorageAtom
  type Row [+T] <: IndexedSeq[T] with IndexedSeqOps[T, Row, Row[T]]
  type Table[T] <: ArrayBuffer[T]
  type Relation[T] <: Table[Row[T]]

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

  def idb(rId: Int): Relation[StorageAtom]

  def edb(rId: Int): Relation[StorageTerm]

  def spju(rId: Int, keys: Table[JoinIndexes], sourceQueryId: Int): Relation[StorageTerm]

  def getIncrementDB(rId: Int, queryId: Int): Relation[StorageTerm]

  def swapIncrDBs(qId1: Int, qId2: Int): Unit
  def swapDeltaDBs(qId1: Int, qId2: Int): Unit

  def tableToString[T](r: Relation[T]): String

  def resetIncrEDB(rId: Int, rules: Relation[StorageTerm], queryId: Int): Unit
  def resetDeltaEDB(rId: Int, rules: Relation[StorageTerm], queryId: Int): Unit
  def compareDeltaDBs(qId1: Int, qId2: Int): Boolean
  def compareIncrDBs(qId1: Int, qId2: Int): Boolean
}
