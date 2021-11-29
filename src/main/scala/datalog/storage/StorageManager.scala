package datalog.storage

import datalog.dsl.Atom

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, Map}
import scala.collection.immutable.IndexedSeqOps

trait StorageManager {
  // A bit repetitive to have also defined in dsl but good to separate user-facing class with internal storage
  type StorageVariable
  type StorageConstant
  type StorageTerm = StorageVariable | StorageConstant
  type StorageAtom
  type Row [+T] <: IndexedSeq[T] with IndexedSeqOps[T, Row, Row[T]]
  type Table[T] <: ArrayBuffer[Row[T]]

  def initRelation(rId: Int): Unit

  def insertEDB(rule: StorageAtom): Unit

  def insertIDB(rId: Int, rule: Row[StorageAtom]): Unit

  def idb(rId: Int): Table[StorageAtom]

  def edb(rId: Int): Table[StorageTerm]

//  val edbs: Map[Int, Table[StorageTerm]] // TODO: maybe abstract Map too
//  val idbs: Map[Int, Table[StorageAtom]]
  //  def insertBulkEDB(rId: Int, terms: Seq[Seq[Any]]): Unit = {}
}
