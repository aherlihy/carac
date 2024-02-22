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
 * Precise type for the EDB type in CollectionsStorageManager.
 * Represents one EDB relation, i.e. the set of rows of tuples in a particular EDB relation.
 * AKA mutable.ArrayBuffer[Seq[StorageTerm]]
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

type CollectionsRow = Seq[StorageTerm]
inline def CollectionsRow(s: Seq[StorageTerm]) = s

/**
 * Precise type for the Database type in CollectionsStorageManager.
 * Represents a DB containing a set of rows, i.e. tuples of terms.
 * AKA a mutable.Map[RelationId, ArrayBuffer[Seq[Term]]].
 */
case class CollectionsDatabase(wrapped: mutable.Map[RelationId, CollectionsEDB]) extends Database[CollectionsEDB] {
  export wrapped.{ apply, getOrElse, foreach, contains, update, exists, toSeq }
}
object CollectionsDatabase {
  def apply(elems: (RelationId, CollectionsEDB)*): CollectionsDatabase = new CollectionsDatabase(mutable.Map[RelationId, CollectionsEDB](elems *))
}


