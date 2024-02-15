package datalog.storage

import datalog.dsl.Variable

import scala.collection.mutable
import IndexedCollectionsCasts.*


/**
 * This file defines the datatypes that the IndexedCollectionsStorageManager operatoes over. These are the simplest example and just wrap Scala IndexedCollections.
 */

/* Necessary evil to avoid path dependent types. All casts go first-thing in the IndexedCollectionsStorageManager (or child)'s public methods that take an EDB as argument */
object IndexedCollectionsCasts {
  def asIndexedCollectionsEDB(to: Relation[StorageTerm]): IndexedCollectionsEDB = to.asInstanceOf[IndexedCollectionsEDB]
  def asIndexedCollectionsSeqEDB(to: Seq[Relation[StorageTerm]]): Seq[IndexedCollectionsEDB] = to.asInstanceOf[Seq[IndexedCollectionsEDB]]
  def asIndexedCollectionsRow(to: Row[StorageTerm]): IndexedCollectionsRow = to.asInstanceOf[IndexedCollectionsRow]
}

given Ordering[StorageTerm] = Ordering[StorageTerm].on {
  case x: Int => x
  case x: String => x
  case x: Variable => ???
}

/**
 * Precise type for the EDB type in IndexedCollectionsStorageManager.
 * Represents one EDB relation, i.e. the set of rows of tuples in a particular EDB relation.
 * AKA mutable.SortedMap[Term, ArrayBuffer[Seq[StorageTerm]]] for each index key, for now ignore multi-key indexes
 */
case class IndexedCollectionsEDB(wrapped: mutable.ArrayBuffer[IndexedCollectionsRow]) extends EDB with IterableOnce[IndexedCollectionsRow] {
  export wrapped.{ length, clear, nonEmpty, toSet, apply, mkString, iterator } // TODO: overwrite
  val indexes = mutable.Map[Int, mutable.SortedMap[StorageTerm, mutable.ArrayBuffer[IndexedCollectionsRow]]]()

  def registerIndex(idx: Int): this.type = {
    if (!indexes.contains(idx))
      if (indexes.isEmpty) {// first index added
        val newIndex = mutable.SortedMap[StorageTerm, mutable.ArrayBuffer[IndexedCollectionsRow]]()
        wrapped.foreach(edb => newIndex.getOrElseUpdate(edb(idx), mutable.ArrayBuffer[IndexedCollectionsRow]()).addOne(edb))
        indexes(idx) = newIndex
      } else { // new index added, but data exists, so make a copy
        val newIndex = mutable.SortedMap[StorageTerm, mutable.ArrayBuffer[IndexedCollectionsRow]]()
        indexes.head._2.valuesIterator.foreach(edbs =>
          edbs.foreach(edb =>
            newIndex.getOrElseUpdate(edb(idx), mutable.ArrayBuffer[IndexedCollectionsRow]()).addOne(edb)
          )
        )
        indexes(idx) = newIndex
      }
    this
  }
  def addAll(edbs: mutable.ArrayBuffer[IndexedCollectionsRow]): this.type = {
    wrapped.addAll(edbs)
    edbs.foreach(edb =>
      indexes.foreach((idx, index) =>
        // TODO: what to do with duplicates?
        index(edb(idx)).addOne(edb)
      )
    )
    this
  }

  def addOne(edb: IndexedCollectionsRow): this.type =
    wrapped.addOne(edb)
    indexes.foreach((idx, index) =>
      // TODO: what to do with duplicates?
      index(edb(idx)).addOne(edb)
    )
    this

  def contains(edb: IndexedCollectionsRow): Boolean =
    if (indexes.isEmpty)
      wrapped.contains(edb)
    else
      val i = indexes.head._1
      if (indexes(i).contains(edb(i))) // if there are any tuples that share this value
        indexes(i)(edb(i)).contains(edb) // loop thru
      else
        false


  def diff(that: IndexedCollectionsEDB): IndexedCollectionsEDB = // TODO: efficient diff, better to make a new set of indexes?
    indexes.foreach((idx, index) =>
      index.mapValuesInPlace((term, tuples) =>
        tuples.filterInPlace(tuple =>
          !that.contains(tuple)
        )
      )
    )
    this // TODO: do we need the relation again after the diff, or can it be in-place?

  def concat(suffix: IndexedCollectionsEDB): IndexedCollectionsEDB = // TODO: do we need both indexes? do we really need a copy here?
    val copy = IndexedCollectionsEDB()
    indexes.keys.foreach(copy.registerIndex)
    suffix.indexes.keys.foreach(copy.registerIndex)
//    copy.addAll(indexes.head._2.flatMap(_._2))
    indexes.head._2.map((t, edbs) => copy.addAll(edbs))
//    copy.addAll(suffix.indexes.head._2.flatMap(_._2))
    suffix.indexes.head._2.map((t, edbs) => copy.addAll(edbs))
    copy

  def getSetOfSeq: Set[Seq[StorageTerm]] = // used for getting a final result when we want to make sure to deduplicate results
    indexes.head._2.flatMap(_._2).map(s => s.toSeq).toSet

  def map(f: IndexedCollectionsRow => IndexedCollectionsRow): IndexedCollectionsEDB = ???
//    IndexedCollectionsEDB(wrapped.map(e => f(e)))

  def filter(f: IndexedCollectionsRow => Boolean): IndexedCollectionsEDB = ???
//    IndexedCollectionsEDB(wrapped.filter(f))

  def flatMap(f: IndexedCollectionsRow => IterableOnce[IndexedCollectionsRow]): IndexedCollectionsEDB = ???
//    IndexedCollectionsEDB(wrapped.flatMap(e => f(e)))

  def factToString: String = wrapped.map(s => s.mkString("(", ", ", ")")).mkString("[", ", ", "]")
}

object IndexedCollectionsEDB {
  extension (edbs: Seq[EDB])
    def unionEDB: EDB = ???
//      IndexedCollectionsEDB(edbs.flatten(using e => asIndexedCollectionsEDB(e).wrapped).distinct.to(mutable.ArrayBuffer))
  def apply(elems: IndexedCollectionsRow*): IndexedCollectionsEDB = ???
//    new IndexedCollectionsEDB(mutable.ArrayBuffer[IndexedCollectionsRow](elems*))

}

/**
 * Precise type for the Row (aka Tuple) type in IndexedCollectionsStorageManager.
 * Represents a single tuple within a relation, either EDB or IDB.
 * AKA a Seq[StorageTerm]
 */
case class IndexedCollectionsRow(wrapped: Seq[StorageTerm]) extends Row[StorageTerm] { // TODO: update from Seq to ArrayBuffer?
  def toSeq = wrapped
  def length: Int = wrapped.length
  def concat(suffix: Row[StorageTerm]): IndexedCollectionsRow =
    IndexedCollectionsRow(wrapped.concat(asIndexedCollectionsRow(suffix).wrapped))
  export wrapped.{ apply, iterator, lift, mkString }
  // Inlined and specialized applyOrElse to avoid significant boxing overhead.
  inline def applyOrElse(i: Int, inline default: Int => StorageTerm): StorageTerm =
    if i >= length then default(i) else apply(i)
}

/**
 * Precise type for the Database type in IndexedCollectionsStorageManager.
 * Represents a DB containing a set of rows, i.e. tuples of terms.
 * AKA a mutable.Map[RelationId, ArrayBuffer[Seq[Term]]].
 */
case class IndexedCollectionsDatabase(wrapped: mutable.Map[RelationId, IndexedCollectionsEDB]) extends Database[IndexedCollectionsEDB] {
  export wrapped.{ apply, getOrElse, foreach, contains, update, exists, toSeq }
}
object IndexedCollectionsDatabase {
  def apply(elems: (RelationId, IndexedCollectionsEDB)*): IndexedCollectionsDatabase = new IndexedCollectionsDatabase(mutable.Map[RelationId, IndexedCollectionsEDB](elems *))
}


