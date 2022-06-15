package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}

import scala.collection.{immutable, mutable}

class IndexedCollStorageManager(ns: mutable.Map[Int, String] = mutable.Map[Int, String]()) extends CollectionsStorageManager(ns) {
  val prebuiltOpKeys: mutable.Map[Int, Table[JoinIndexes]] = mutable.Map[Int, Table[JoinIndexes]]()

  override def insertIDB(rId: Int, rule: Seq[Atom]): Unit = {
    val row = Row(rule.map(a => StorageAtom(a.rId, a.terms))*)
    idbs(rId).addOne(row)
    prebuiltOpKeys.getOrElseUpdate(rId, Table[JoinIndexes]()).addOne(getOperatorKey(row))
  }

  override def getOperatorKeys(rId: Int): Table[JoinIndexes] = {
    prebuiltOpKeys.getOrElseUpdate(rId, Table[JoinIndexes]())
  }
}