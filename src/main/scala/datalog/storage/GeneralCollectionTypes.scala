package datalog.storage

import datalog.dsl.Constant

import scala.collection.mutable
import IndexedCollectionsCasts.*
import datalog.execution.JoinIndexes

import scala.collection.immutable.ArraySeq

/**
 * General database types for indexed and unindexed collections-based storage.
 */
given Ordering[StorageTerm] with
  def compare(x: StorageTerm, y: StorageTerm): Int = x match
    case x: Int =>
      y match
        case y: Int =>
          Ordering[Int].compare(x, y)
        case y: String =>
          -1
    case x: String =>
      y match
        case y: String =>
          Ordering[String].compare(x, y)
        case y: Int =>
          1

object GeneralCollectionCasts {
  def asGeneralCollectionsEDB(to: Relation[StorageTerm]): GeneralCollectionsEDB = to.asInstanceOf[GeneralCollectionsEDB]
}

abstract class GeneralCollectionsEDB extends EDB with IterableOnce[CollectionsRow]:
  val arity: Int
  val name: String
  def addAll(edbs: mutable.ArrayBuffer[CollectionsRow]): this.type
  def addAndDeduplicate(edbs: mutable.ArrayBuffer[CollectionsRow]): this.type
  def addOne(edb: CollectionsRow): this.type
  def bulkRegisterIndex(idxs: mutable.BitSet): this.type
  def clear(): Unit
  def contains(edb: CollectionsRow): Boolean
  def emptyCopy: GeneralCollectionsEDB
  def getSetOfSeq: Set[Seq[StorageTerm]]
  def joinFilter(indexes: JoinIndexes, skip: Int, toJoin: GeneralCollectionsEDB): GeneralCollectionsEDB
  def length: Int
  def mergeEDBs(edb: GeneralCollectionsEDB): Unit
  def nonEmpty: Boolean
  def projectAndDiff(constIndexes: mutable.Map[Int, Constant],
                     projIndexes: Seq[(String, Constant)],
                     newName: String,
                     newIndexes: mutable.BitSet,
                     derivedDB: CollectionsDatabase,
                     rId: RelationId): GeneralCollectionsEDB
  def registerIndex(idx: Int): this.type
  def diff(edb: EDB): GeneralCollectionsEDB
//  def filter(p: CollectionsRow => Boolean): GeneralCollectionsEDB
//  def flatMap(f: CollectionsRow => GeneralCollectionsEDB): GeneralCollectionsEDB
//  def map(f: CollectionsRow => CollectionsRow): GeneralCollectionsEDB

/**
 * Generic collections-based database.
 */
case class CollectionsDatabase(wrappedOpt: Option[mutable.Map[RelationId, GeneralCollectionsEDB]],
                               empty: (Int, String, mutable.BitSet, mutable.BitSet) => GeneralCollectionsEDB)
  extends Database[GeneralCollectionsEDB] {
  val wrapped = wrappedOpt.getOrElse(mutable.Map[RelationId, GeneralCollectionsEDB]())
  val definedRelations: mutable.BitSet = mutable.BitSet() ++ wrapped.keys

  def clear(): Unit = {
    wrapped.foreach((rId, edb) =>
      edb.clear()
    )
    definedRelations.clear()
  }

  def contains(c: RelationId): Boolean = definedRelations.contains(c)

  // Take edbToCopy and manually copy the indexes to a new EDB to avoid rebuilding indexes, then add it to the DB
  def addNewEDBCopy(rId: RelationId, edbToCopy: GeneralCollectionsEDB): Unit =
    definedRelations.addOne(rId)
    val newEDB = wrapped.getOrElseUpdate(rId,
      edbToCopy.emptyCopy
    )
    newEDB.mergeEDBs(edbToCopy)
    wrapped(rId) = newEDB

  def assignEDBDirect(rId: RelationId, edb: GeneralCollectionsEDB): Unit =
    definedRelations.addOne(rId)
    wrapped(rId) = edb

  def foreach[U](f: ((RelationId, GeneralCollectionsEDB)) => U): Unit = wrapped.filter((k, v) => definedRelations.contains(k)).foreach(f)

  def exists(p: ((RelationId, GeneralCollectionsEDB)) => Boolean): Boolean = wrapped.filter((k, v) => definedRelations.contains(k)).exists(p)

  def forall(p: ((RelationId, GeneralCollectionsEDB)) => Boolean): Boolean = wrapped.filter((k, v) => definedRelations.contains(k)).forall(p)

  def toSeq: Seq[(RelationId, GeneralCollectionsEDB)] = wrapped.filter((k, v) => definedRelations.contains(k)).toSeq

  def getOrElseEmpty(rId: RelationId, arity: Int, indexCandidates: mutable.BitSet, name: String, indexToSkip: mutable.BitSet): GeneralCollectionsEDB =
    wrapped.getOrElseUpdate(rId,
      empty(arity, name, indexCandidates, indexToSkip)
    )
  def addEmpty(rId: RelationId, arity: Int, indexCandidates: mutable.BitSet, name: String, indexToSkip: mutable.BitSet): GeneralCollectionsEDB =
    definedRelations.addOne(rId)
    getOrElseEmpty(rId, arity, indexCandidates, name, indexToSkip)

  def apply(key: RelationId): GeneralCollectionsEDB = wrapped.getOrElse(key, throw new Exception(s"Relation $key not found in EDB database"))
}

inline def CollectionsRow(s: ArraySeq[StorageTerm]) = s
type CollectionsRow = ArraySeq[StorageTerm]
//implicit val collectionsRowOrdering: Ordering[CollectionsRow] = Ordering.by(_.toList)
extension (seq: ArraySeq[StorageTerm])
  def project(projIndexes: Seq[(String, Constant)]): CollectionsRow = // make a copy
    if seq.isInstanceOf[ArraySeq.ofInt] then
      projectInto(new Array[Int](projIndexes.length), projIndexes)
    else
      projectInto(new Array[StorageTerm](projIndexes.length), projIndexes)
  inline def projectInto[T <: StorageTerm](arr: Array[T], projIndexes: Seq[(String, Constant)]): CollectionsRow =
    var i = 0
    while i < arr.length do
      val elem = projIndexes(i)
      elem._1 match
        case "v" => arr(i) = seq(elem._2.asInstanceOf[Int]).asInstanceOf[T]
        case "c" => arr(i) = elem._2.asInstanceOf[T]
        case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
      i += 1
    end while
    ArraySeq.unsafeWrapArray(arr)

  /* Equality constraint $1 == $2 */
  inline def filterConstraint(keys: Seq[Int]): Boolean =
    keys.size <= 1 || // there is no self-constraint, OR
      keys.drop(1).forall(idxToMatch => // all the self constraints hold
        seq(keys.head) == seq(idxToMatch)
      )

  /* Constant filter $1 = c */
  inline def filterConstant(consts: mutable.Map[Int, Constant]): Boolean =
    consts.isEmpty || consts.forall((idx, const) => // for each filter
      seq(idx) == const
    )

end extension

