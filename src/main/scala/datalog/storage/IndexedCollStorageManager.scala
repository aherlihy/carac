package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}

import scala.collection.{immutable, mutable}

class IndexedCollStorageManager(ns: NS = NS()) extends CollectionsStorageManager(ns) {
  val prebuiltOpKeys: mutable.Map[Int, Table[JoinIndexes]] = mutable.Map[Int, Table[JoinIndexes]]()

  override def insertIDB(rId: Int, rule: Seq[Atom]): Unit = {
    val row = Row(rule.map(a => StorageAtom(a.rId, a.terms))*)
    idbs.getOrElseUpdate(rId, IDB()).addOne(row)
    prebuiltOpKeys.getOrElseUpdate(rId, Table[JoinIndexes]()).addOne(getOperatorKey(row))
    // TODO: could do this in the topsort instead of as inserted
  }

  override def insertEDB(rule: Atom): Unit = {
    if (edbs.contains(rule.rId))
      edbs(rule.rId).addOne(rule.terms)
    else
      edbs(rule.rId) = EDB()
      edbs(rule.rId).addOne(rule.terms)
      prebuiltOpKeys.getOrElseUpdate(rule.rId, Table[JoinIndexes]()).addOne(JoinIndexes(IndexedSeq(), Map(), IndexedSeq(), Seq(rule.rId), true))
  }

  override def getOperatorKeys(rId: Int): Table[JoinIndexes] = {
    prebuiltOpKeys.getOrElseUpdate(rId, Table[JoinIndexes]())
  }
}