package datalog.storage

import scala.collection.mutable
import CollectionsCasts.*
import datalog.dsl.Constant
import datalog.execution.JoinIndexes

import scala.collection.mutable.ArrayBuffer

/**
 * This file defines the datatypes that the CollectionsStorageManager operatoes over. These are the simplest example and just wrap Scala collections.
 */

/* Necessary evil to avoid path dependent types. All casts go first-thing in the CollectionsStorageManager (or child)'s public methods that take an EDB as argument */
object CollectionsCasts {
  def asCollectionsEDB(to: Relation[StorageTerm]): CollectionsEDB = to.asInstanceOf[CollectionsEDB]
  def asCollectionsSeqEDB(to: Seq[Relation[StorageTerm]]): Seq[CollectionsEDB] = to.asInstanceOf[Seq[CollectionsEDB]]
  def asCollectionsRow(to: Row[StorageTerm]): CollectionsRow = to.asInstanceOf[CollectionsRow]
}

/**
 * Precise type for the EDB type in CollectionsStorageManager.
 * Represents one EDB relation, i.e. the set of rows of tuples in a particular EDB relation.
 * AKA mutable.ArrayBuffer[Seq[StorageTerm]]
 */
case class CollectionsEDB(wrapped: mutable.ArrayBuffer[CollectionsRow]) extends EDB with IterableOnce[CollectionsRow] {
  export wrapped.{ length, clear, nonEmpty, toSet, apply, mkString, iterator, contains }

  def addOne(elem: CollectionsRow): this.type =
    wrapped.addOne(elem)
    this
  def diff(that: CollectionsEDB): CollectionsEDB =
    CollectionsEDB(wrapped.diff(that.wrapped))

  def concat(suffix: CollectionsEDB): CollectionsEDB =
    CollectionsEDB(wrapped.concat(suffix.wrapped))

  def getSetOfSeq: Set[Seq[StorageTerm]] =
    wrapped.map(s => s.toSeq).toSet

  def map(f: CollectionsRow => CollectionsRow): CollectionsEDB =
    CollectionsEDB(wrapped.map(e => f(e)))

  def filter(f: CollectionsRow => Boolean): CollectionsEDB =
    CollectionsEDB(wrapped.filter(f))

  def flatMap(f: CollectionsRow => IterableOnce[CollectionsRow]): CollectionsEDB =
    CollectionsEDB(wrapped.flatMap(e => f(e)))

  def factToString: String = wrapped.map(s => s.mkString("(", ", ", ")")).mkString("[", ", ", "]")

  def mergeEDBs(edbToMerge: CollectionsEDB, deduplicate: Boolean): Unit =
    wrapped.addAll(edbToMerge.wrapped)

  private /*inline*/ def scanFilter(k: JoinIndexes, maxIdx: Int)(get: Int => StorageTerm = x => x) = {
    val vCmp = k.varIndexes.isEmpty || k.varIndexes.forall(condition =>
      if (condition.head >= maxIdx)
        true
      else
        val toCompare = get(condition.head)
          condition.drop(1).forall(idx =>
        idx >= maxIdx || get(idx) == toCompare
      )
    )
    val kCmp = k.constIndexes.isEmpty || k.constIndexes.forall((idx, const) =>
      idx >= maxIdx || get(idx) == const
    )
    vCmp && kCmp
  }

  private /*inline*/ def prefilter(consts: mutable.Map[Int, Constant], skip: Int, row: CollectionsRow): Boolean = {
    consts.isEmpty || consts.forall((idx, const) => // for each filter // TODO: make sure out of range fails
      row(idx - skip) == const
    )
  }

  private /*inline*/ def toJoinT(k: JoinIndexes, innerTuple: CollectionsRow, outerTuple: CollectionsRow): Boolean = {
    k.varIndexes.isEmpty || k.varIndexes.forall(condition =>
      if (condition.head >= innerTuple.length + outerTuple.length)
        true
      else
        val toCompare = innerTuple.applyOrElse(condition.head, j => outerTuple(j - innerTuple.length))
        condition.drop(1).forall(idx =>
          idx >= innerTuple.length + outerTuple.length ||
            innerTuple.applyOrElse(idx, j => outerTuple(j - innerTuple.length)) == toCompare
        )
    )
  }

  def joinFilterWithIndex(joinIndexes: JoinIndexes,
                          skip: Int,
                          toJoin: CollectionsEDB): CollectionsEDB = {
    var outer = this
    var inner = toJoin
//    println(s"\t2-way join($name*${toJoin.name}), allKeys=${joinIndexes.varIndexes},  relKeys=${keysInRange.mkString("[", ", ", "]")}, outerConstants=${outerConstantFilters.mkString("{", ", ", "}")}, innerConstants=${innerConstantFilters.mkString("{", ", ", "}")}")
//    println(s"\tinput rels=${wrapped.mkString("[",",","]")} * ${toJoin.wrapped.mkString("[",",","]")}")
    outer.filter(o =>
      prefilter(joinIndexes.constIndexes.filter((i, _) => i < o.length), 0, o)
    )
    .flatMap(outerTuple => {
        inner
          .filter(i =>
            prefilter(
              joinIndexes.constIndexes.filter((ind, _) => ind >= outerTuple.length && ind < (outerTuple.length + i.length)),
              outerTuple.length, i
            ) && toJoinT(joinIndexes, outerTuple, i)
          )
        .map(innerTuple =>
          outerTuple.concat(innerTuple)
        )
      })
  }

  def projectAndDiff(constIndexes: mutable.Map[Int, Constant],
                     projIndexes: Seq[(String, Constant)],
                     newName: String,
                     newIndexes: mutable.BitSet,
                     derivedDB: CollectionsDatabase,
                     rId: RelationId
                    ): CollectionsEDB = {

    val constFilter = constIndexes//.filter((ind, _) => ind < arity)
    val newWrapped = ArrayBuffer[CollectionsRow]()
    val toCopy = wrapped

    var i = 0
    while i < toCopy.length do
      val projected = toCopy(i).projectNI(projIndexes)
      //      println(s"projected=$projected checking derived(${newName})=${if derivedDB.contains(rId) then derivedDB(rId) else "X"}, constantTest=${rest != null && projected.filterConstant(rest)}")
      if
        (toCopy(i).filterConstantNI(constFilter)) && // filter constants
          (!derivedDB.contains(rId) || !derivedDB(rId).contains(projected)) // diff with derived
      then
        newWrapped.addOne(projected)
      i += 1

    CollectionsEDB(
      newWrapped.distinct,
    )
  }
}

object CollectionsEDB {
  extension (edbs: Seq[EDB])
    def unionEDB: EDB =
      CollectionsEDB(edbs.flatten(using e => asCollectionsEDB(e).wrapped).distinct.to(mutable.ArrayBuffer))
  def apply(elems: CollectionsRow*): CollectionsEDB = new CollectionsEDB(mutable.ArrayBuffer[CollectionsRow](elems*))
  def empty(arity: Int, preIndexes: mutable.BitSet = mutable.BitSet(), rName: String = "ANON", skipIndexes: mutable.BitSet): CollectionsEDB =
    CollectionsEDB()
  def allIndexesToString(edb: CollectionsEDB): String = ""
}

type CollectionsRow = Seq[StorageTerm]
inline def CollectionsRow(s: Seq[StorageTerm]) = s
extension(seq: Seq[StorageTerm])
  def projectNI(projIndexes: Seq[(String, Constant)]): CollectionsRow = // make a copy
    CollectionsRow(projIndexes.flatMap((typ, idx) =>
      typ match {
        case "v" => seq.lift(idx.asInstanceOf[Int])
        case "c" => Some(idx)
        case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
      }))

//  /* Equality constraint $1 == $2 */
//  inline def filterConstraint(keys: Seq[Int]): Boolean =
//    keys.size <= 1 || // there is no self-constraint, OR
//      keys.drop(1).forall(idxToMatch => // all the self constraints hold
//        seq(keys.head) == seq(idxToMatch)
//      )
//
  /* Constant filter $1 = c */
  inline def filterConstantNI(consts: mutable.Map[Int, Constant]): Boolean =
    consts.isEmpty || consts.forall((idx, const) => // for each filter
      seq(idx) == const
    )

/**
 * Precise type for the Database type in CollectionsStorageManager.
 * Represents a DB containing a set of rows, i.e. tuples of terms.
 * AKA a mutable.Map[RelationId, ArrayBuffer[Seq[Term]]].
 */
case class CollectionsDatabase(wrapped: mutable.Map[RelationId, CollectionsEDB]) extends Database[CollectionsEDB] {
  def addEmpty(rId: RelationId, arity: Int, indexCandidates: mutable.BitSet, name: String, indexToSkip: mutable.BitSet): CollectionsEDB =
    wrapped.addOne(rId, CollectionsEDB())
    wrapped(rId)

  def getOrElseEmpty(rId: RelationId, arity: Int, indexCandidates: mutable.BitSet, name: String, indexToSkip: mutable.BitSet): CollectionsEDB =
    wrapped.getOrElseUpdate(rId, CollectionsEDB())

  def addNewEDBCopy(rId: RelationId, edbToCopy: CollectionsEDB): Unit =
    wrapped(rId) = CollectionsEDB(mutable.ArrayBuffer.from(edbToCopy.wrapped))

  export wrapped.{ apply, getOrElse, foreach, contains, update, exists, toSeq, clear }
}
object CollectionsDatabase {
  def apply(elems: (RelationId, CollectionsEDB)*): CollectionsDatabase = new CollectionsDatabase(mutable.Map[RelationId, CollectionsEDB](elems *))
}


