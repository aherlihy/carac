package datalog.storage

import datalog.dsl.{Atom, Term, Variable}

import scala.collection.mutable
import scala.collection.immutable

/**
 * Quick BiMap
 */
class NS() {
  private val nameToRid = mutable.Map[String, Int]()
  private val rIdToName = mutable.Map[Int, String]()
  def apply(name: String): Int = nameToRid(name)
  def apply(rId: Int): String = rIdToName(rId)
  def update(key: String, value: Int): Unit = {
    nameToRid(key) = value
    rIdToName(value) = key
  }
  def update(key: Int, value: String): Unit = {
    rIdToName(key) = value
    nameToRid(value) = key
  }
  def contains(key: String): Boolean = nameToRid.contains(key)
  def contains(key: Int): Boolean = rIdToName.contains(key)
}

trait StorageManager(val ns: NS) {
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
  def EDB(c: Row[StorageTerm]*): EDB
  type IDB = Relation[StorageAtom]
  type Database[K, V] <: mutable.Map[K, V]
  type FactDatabase <: Database[Int, EDB]
  type RuleDatabase <: Database[Int, IDB]

  val derivedDB: Database[Int, FactDatabase]
  val deltaDB: Database[Int, FactDatabase]
  val edbs: FactDatabase
  val idbs: RuleDatabase

  val printer: Printer[this.type]

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
        ", deps:" + deps.mkString("[", ", ", "]") + " }"
  }
  def getOperatorKeys(rId: Int): Table[JoinIndexes]

  def initRelation(rId: Int, name: String): Unit
  def initEvaluation(): Int

  def insertEDB(rule: Atom): Unit

  def insertIDB(rId: Int, rule: Seq[Atom]): Unit

  def idb(rId: Int): IDB

  def edb(rId: Int): EDB

  def SPJU(rId: Int, keys: Table[JoinIndexes], knownDbId: Int): EDB
  def naiveSPJU(rId: Int, keys: Table[JoinIndexes], knownDbId: Int): EDB

  def getDerivedDB(rId: Int, dbId: Int): EDB
  def getIDBResult(rId: Int, dbId: Int): Set[Seq[Term]]
  def getEDBResult(rId: Int): Set[Seq[Term]]

  def swapDerivedDBs(dbId1: Int, dbId2: Int): Unit
  def swapDeltaDBs(dbId1: Int, dbId2: Int): Unit

//  def tableToString[T](r: Relation[T]): String
  def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB

  def getDiff(lhs: EDB, rhs: EDB): EDB
  def resetDerived(rId: Int, dbId: Int, rules: EDB, prev: EDB = EDB()): Unit
  def resetDelta(rId: Int, dbId: Int, rules: EDB): Unit
  def compareDeltaDBs(dbId1: Int, dbId2: Int): Boolean
  def compareDerivedDBs(dbId1: Int, dbId2: Int): Boolean
  def clearDB(derived: Boolean, dbId: Int): Unit

}
