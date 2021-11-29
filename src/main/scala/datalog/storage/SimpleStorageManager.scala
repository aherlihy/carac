package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, Map}

class SimpleStorageManager extends StorageManager {
  type StorageVariable = Variable
  type StorageConstant = Constant
  type StorageTerm = StorageVariable | StorageConstant
  case class StorageAtom(rId: Int, terms: IndexedSeq[StorageTerm])
  type Row[+T] = IndexedSeq[T]
  type Table[T] = ArrayBuffer[Row[T]]

  val edbs = Map[Int, Table[StorageTerm]]()
  val idbs = Map[Int, Table[StorageAtom]]()

  //  val edbs = Map[Int, ArrayBuffer[Seq[Term]]]()
  //  val idbs = Map[Int, ArrayBuffer[(Atom, Seq[Atom])]]()

  def idb(rId: Int): Table[StorageAtom] = idbs(rId)
  def edb(rId: Int): Table[StorageTerm] = edbs(rId)

  // store all relations
  def initRelation(rId: Int): Unit = {
    edbs.addOne(rId, ArrayBuffer[Row[StorageTerm]]())
    idbs.addOne(rId, ArrayBuffer[Row[StorageAtom]]())
  }
  // TODO: For now store IDB and EDB separately
  def insertEDB(rule: StorageAtom): Unit = {
    edbs(rule.rId).addOne(rule.terms)
  }
  //  def bulkInsertEDB(rId: Int, rules: EDBRow): Unit = {
  //    edbs(rId).appendAll(rules)
  //  }
  def insertIDB(rId: Int, rule: Row[StorageAtom]): Unit = {
    idbs(rId).addOne(rule)
  }

  override def toString = {
    "EDB:" +
      edbs
        .map{case (k, v) => (k, v.map(s => s.mkString("Rule{", ", ", "}")).mkString("[", ", ", "]"))}
        .mkString("[\n  ", ",\n  ", "]") +
      "\nIDB:" +
      idbs
        .map{case (k, v) => (k, v.map(s => s.mkString("Rule{ ", ", ", " }")).mkString("[ ", ", ", "]"))}
        .mkString("[\n  ", ",\n  ", "]")
  }
}
