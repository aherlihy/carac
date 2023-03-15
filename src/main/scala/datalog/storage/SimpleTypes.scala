package datalog.storage

import scala.collection.mutable
import SimpleCasts.*

/**
 * This file defines the datatypes that the SimpleStorageManager operatoes over. These are the simplest example and just wrap Scala collections.
 */

/* Necessary evil to avoid path dependent types. All casts go first-thing in the SimpleStorageManager (or child)'s public methods that take an EDB as argument */
object SimpleCasts {
  def asSimpleEDB(to: Relation[StorageTerm]): SimpleEDB = to.asInstanceOf[SimpleEDB]
  def asSimpleSeqEDB(to: Seq[Relation[StorageTerm]]): Seq[SimpleEDB] = to.asInstanceOf[Seq[SimpleEDB]]
  def asSimpleRow(to: Row[StorageTerm]): SimpleRow = to.asInstanceOf[SimpleRow]
}

/**
 * Precise type for the EDB type in SimpleStorageManager, essentially just wraps an ArrayBuffer of SimpleRow
 */
case class SimpleEDB(wrapped: mutable.ArrayBuffer[SimpleRow]) extends EDB with IterableOnce[SimpleRow] {
  export wrapped.{ length, clear, nonEmpty, toSet, apply, mkString, iterator }

  def addOne(elem: SimpleRow): this.type =
    wrapped.addOne(elem)
    this
  def diff(that: SimpleEDB): SimpleEDB =
    SimpleEDB(wrapped.diff(that.wrapped))

  def concat(suffix: SimpleEDB): SimpleEDB =
    SimpleEDB(wrapped.concat(suffix.wrapped))

  def getSetOfSeq: Set[Seq[StorageTerm]] =
    wrapped.map(s => s.toSeq).toSet

  def map(f: SimpleRow => SimpleRow): SimpleEDB =
    SimpleEDB(wrapped.map(e => f(e)))

  def filter(f: SimpleRow => Boolean): SimpleEDB =
    SimpleEDB(wrapped.filter(f))

  def flatMap(f: SimpleRow => IterableOnce[SimpleRow]): SimpleEDB =
    SimpleEDB(wrapped.flatMap(e => f(e)))

  def factToString: String = wrapped.map(s => s.mkString("(", ", ", ")")).mkString("[", ", ", "]")
}

object SimpleEDB {
  extension (edbs: Seq[EDB])
    def unionEDB: EDB =
      SimpleEDB(edbs.flatten(using e => asSimpleEDB(e).wrapped).distinct.to(mutable.ArrayBuffer))
  def apply(elems: SimpleRow*): SimpleEDB = new SimpleEDB(mutable.ArrayBuffer[SimpleRow](elems*))
}

/**
 * Precise type for the Row (aka Tuple) type in SimpleStorageManager, essentially just wraps an Seq of StorageTerms
 */
case class SimpleRow(wrapped: Seq[StorageTerm]) extends Row[StorageTerm] {
  def toSeq = wrapped
  def length: Int = wrapped.length
  def concat(suffix: Row[StorageTerm]): SimpleRow =
    SimpleRow(wrapped.concat(asSimpleRow(suffix).wrapped))
  export wrapped.{ apply, applyOrElse, iterator, lift, mkString }
}

/**
 * Precise type for the Database type in SimpleStoragemanager, essentially just wraps a mutable.Map of SimpleEDB
 */
case class SimpleDatabase(wrapped: mutable.Map[RelationId, SimpleEDB]) extends Database[SimpleEDB] {
  export wrapped.{ apply, getOrElse, foreach, contains, update, exists, toSeq }
}
object SimpleDatabase {
  def apply(elems: (RelationId, SimpleEDB)*): SimpleDatabase = new SimpleDatabase(mutable.Map[RelationId, SimpleEDB](elems *))
}


