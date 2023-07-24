package datalog.storage

import scala.collection.mutable
import CollectionsCasts.*

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
 * Precise type for the EDB type in CollectionsStorageManager, essentially just wraps an ArrayBuffer of CollectionsRow
 */
case class CollectionsEDB(wrapped: mutable.ArrayBuffer[CollectionsRow]) extends EDB with IterableOnce[CollectionsRow] {
  export wrapped.{ length, clear, nonEmpty, toSet, apply, mkString, iterator }

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
}

object CollectionsEDB {
  extension (edbs: Seq[EDB])
    def unionEDB: EDB =
      CollectionsEDB(edbs.flatten(using e => asCollectionsEDB(e).wrapped).distinct.to(mutable.ArrayBuffer))
  def apply(elems: CollectionsRow*): CollectionsEDB = new CollectionsEDB(mutable.ArrayBuffer[CollectionsRow](elems*))
}

/**
 * Precise type for the Row (aka Tuple) type in CollectionsStorageManager, essentially just wraps an Seq of StorageTerms
 */
case class CollectionsRow(wrapped: Seq[StorageTerm]) extends Row[StorageTerm] {
  def toSeq = wrapped
  def length: Int = wrapped.length
  def concat(suffix: Row[StorageTerm]): CollectionsRow =
    CollectionsRow(wrapped.concat(asCollectionsRow(suffix).wrapped))
  export wrapped.{ apply, applyOrElse, iterator, lift, mkString }
}

/**
 * Precise type for the Database type in CollectionsStorageManager, essentially just wraps a mutable.Map of CollectionsEDB
 */
case class CollectionsDatabase(wrapped: mutable.Map[RelationId, CollectionsEDB]) extends Database[CollectionsEDB] {
  export wrapped.{ apply, getOrElse, foreach, contains, update, exists, toSeq }
}
object CollectionsDatabase {
  def apply(elems: (RelationId, CollectionsEDB)*): CollectionsDatabase = new CollectionsDatabase(mutable.Map[RelationId, CollectionsEDB](elems *))
}


