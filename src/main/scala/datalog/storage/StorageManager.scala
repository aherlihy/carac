package datalog.storage

import datalog.dsl.{Atom, Term, Variable, Constant}
import datalog.execution.{JoinIndexes, AllIndexes}

import scala.collection.mutable
import scala.collection.immutable

/**
 * Quick BiMap
 */
class NS() {
  private val nameToRid = mutable.Map[String, RelationId]()
  private val rIdToName = mutable.Map[RelationId, String]()
  def apply(name: String): RelationId = nameToRid(name)
  def apply(rId: RelationId): String = rIdToName(rId)
  def update(key: String, value: RelationId): Unit = {
    nameToRid(key) = value
    rIdToName(value) = key
  }
  def update(key: RelationId, value: String): Unit = {
    rIdToName(key) = value
    nameToRid(value) = key
  }
  def contains(key: String): Boolean = nameToRid.contains(key)
  def contains(key: RelationId): Boolean = rIdToName.contains(key)
  def rIds(): Iterable[RelationId] = rIdToName.keys
}

type RelationId = Int
type KnowledgeId = Int
enum DB:
  case Derived, Delta
enum KNOWLEDGE:
  case New, Known

// TODO: expand to other types
type StorageTerm = Term
type StorageVariable = Variable
type StorageConstant = Constant

trait Row[T] {
//  def toSeq: immutable.Seq[T]
//  def length: Int
//  def apply(n: Int): T
//  def applyOrElse[A1 <: Int, B1 >: T](x: A1, default: (A1) => B1): B1
//  def concat(suffix: Row[T]): Row[T]
//  def lift: Int => Option[T]
//  def mkString(start: String, sep: String, end: String): String
//  def iterator: Iterator[T]
}
trait Relation[T] extends IterableOnce[Row[T]] {
//  def addOne(elem: Row[T]): this.type // storage only
  def length: Int // everywhere
//  def clear(): Unit // storage only
//  def nonEmpty: Boolean // storage only
//  def diff(that: Relation[T]): Relation[T] // storage only
//  def prependedAll(suffix: Relation[T]): Relation[T] // storage only
//  def getSetOfSeq: Set[Seq[T]] // storage only
//  def map(f: Row[T] => Row[T]): Relation[T]
//  def filter(pred: Row[T] => Boolean): Relation[T]
//  def flatMap(f: Row[T] => IterableOnce[Row[T]]): Relation[T]
//  def apply(n: Int): Row[T]
//  def mkString(start: String, sep: String, end: String): String
//  def iterator: Iterator[Row[T]]
  def factToString: String
}

trait Database[T <: EDB] {
  def toSeq: Seq[(RelationId, T)]
}
type EDB = Relation[StorageTerm]

trait StorageManager(val ns: NS) {
  var iteration = 0
//  type StorageVariable
//  type StorageConstant
//  type Row [+T] <: Seq[T] with immutable.SeqOps[T, Row, Row[T]]
//  type Table[T] <: mutable.ArrayBuffer[T]
//  type Relation[T] <: Table[Row[T]] & mutable.ArrayBuffer[Row[T]]
//
//  type EDB = Relation[StorageTerm]
//  def EDB(c: Row[StorageTerm]*): EDB
//  type Database[K, V] <: mutable.Map[K, V]
//  type FactDatabase <: Database[RelationId, EDB] & mutable.Map[RelationId, EDB]
//
//  val derivedDB: Database[KnowledgeId, FactDatabase]
//  val deltaDB: Database[KnowledgeId, FactDatabase]
//  val edbs: FactDatabase
  var knownDbId: KnowledgeId
  var newDbId: KnowledgeId

  val allRulesAllIndexes: mutable.Map[RelationId, AllIndexes]

  val printer: Printer[this.type]

  def initRelation(rId: RelationId, name: String): Unit
  def initEvaluation(): Unit

  def insertEDB(rule: Atom): Unit
  def getEmptyEDB(): EDB
  def edbContains(rId: RelationId): Boolean
  def getEDB(rId: RelationId): EDB
  def getAllEDBS(): mutable.Map[RelationId, Any] // if you ever just want to read the EDBs as a map, used for testing

  def getKnownDerivedDB(rId: RelationId): EDB
  def getNewDerivedDB(rId: RelationId): EDB
  def getKnownDeltaDB(rId: RelationId): EDB
  def getNewDeltaDB(rId: RelationId): EDB
  def getKnownIDBResult(rId: RelationId): Set[Seq[Term]]
  def getNewIDBResult(rId: RelationId): Set[Seq[Term]]
  def getEDBResult(rId: RelationId): Set[Seq[Term]]

  def resetKnownDerived(rId: RelationId, rules: EDB, prev: EDB): Unit
  def resetNewDerived(rId: RelationId, rules: EDB, prev: EDB): Unit
  def resetNewDelta(rId: RelationId, rules: EDB): Unit
  def resetKnownDelta(rId: RelationId, rules: EDB): Unit
  def clearNewDerived(): Unit

  def swapKnowledge(): Unit
  def compareNewDeltaDBs(): Boolean
  def compareDerivedDBs(): Boolean

  def verifyEDBs(idbList: mutable.Set[RelationId]): Unit

  def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB
  def projectHelper(input: EDB, k: JoinIndexes): EDB
  def joinProjectHelper(inputs: Seq[EDB], k: JoinIndexes, sortOrder: (Int, Int, Int)): EDB
  def joinProjectHelper_withHash(inputs: Seq[EDB], rId: Int, hash: String, sortOrder: (Int, Int, Int)): EDB
  def diff(lhs: EDB, rhs: EDB): EDB
  def union(edbs: Seq[EDB]): EDB

  def SPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB
  def naiveSPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB
}
