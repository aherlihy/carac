package datalog.storage

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.JoinIndexes
import datalog.tools.Debug.debug

import scala.collection.{immutable, mutable}

abstract class SimpleStorageManager(override val ns: NS) extends StorageManager(ns) {
  type StorageTerm = Term
  type StorageVariable = Variable
  type StorageConstant = Constant
  type Row[+T] = IndexedSeq[T]
  def Row[T](c: T*) = IndexedSeq[T](c: _*)
  type Table[T] = mutable.ArrayBuffer[T]
  def Table[T](r: T*) = mutable.ArrayBuffer[T](r: _*)
  type Relation[T] = Table[Row[T]]
  def Relation[T](c: Row[T]*) = Table[Row[T]](c: _*)

  type Database[K, V] = mutable.Map[K, V]

  type FactDatabase = Database[Int, EDB]
  def FactDatabase(e: (Int, EDB)*) = mutable.Map[Int, EDB](e: _*)
  type RuleDatabase = Database[Int, IDB]
  def RuleDatabase(e: (Int, IDB)*) = mutable.Map[Int, IDB](e: _*)

  def EDB(c: Row[StorageTerm]*) = Relation[StorageTerm](c: _*)
  def IDB(c: Row[StorageAtom]*) = Relation[StorageAtom](c: _*)

  // "database", i.e. relationID => Relation
  val edbs: FactDatabase = FactDatabase()
  val idbs: RuleDatabase = RuleDatabase()

  // dbID => database, because we swap between read (known) and write (new)
  var dbId = 0
  val derivedDB: Database[Int, FactDatabase] = mutable.Map[Int, FactDatabase]()
  val deltaDB: Database[Int, FactDatabase] = mutable.Map[Int, FactDatabase]()

  def idb(rId: Int): IDB = idbs(rId)
  def edb(rId: Int): EDB = edbs(rId)
  val printer: Printer[this.type] = Printer[this.type](this)

  val relOps: RelationalOperators[this.type] = RelationalOperators(this)

  override def insertIDB(rId: Int, rule: Seq[Atom]): Unit = {
    val row = Row(rule.map(a => StorageAtom(a.rId, a.terms))*)
    idbs.getOrElseUpdate(rId, IDB()).addOne(row)
    // TODO: could do this in the topsort instead of as inserted
  }

  override def insertEDB(rule: Atom): Unit = {
    if (edbs.contains(rule.rId))
      edbs(rule.rId).addOne(rule.terms)
    else
      edbs(rule.rId) = EDB()
      edbs(rule.rId).addOne(rule.terms)
  }

  def initRelation(rId: Int, name: String): Unit = {
    ns(rId) = name
  }

  def verifyEDBs(): Unit = {
    ns.rIds().foreach(rId =>
      if (!edbs.contains(rId) && !idbs.contains(rId)) // treat undefined relations as empty edbs
        edbs(rId) = EDB()
    )
  }

  def verifyEDBs(idbList: mutable.Set[Int]): Unit = {
    ns.rIds().foreach(rId =>
      if (!edbs.contains(rId) && !idbList.contains(rId)) // treat undefined relations as empty edbs
        edbs(rId) = EDB()
    )
  }
  /**
   * Initialize derivedDB to clone EDBs, initialize deltaDB to empty
   *
   * @return
   */
  def initEvaluation(): Int = {
    derivedDB.addOne(dbId, FactDatabase())
    deltaDB.addOne(dbId, FactDatabase())

    idbs.foreach((k, relation) => { // TODO: is ignored in staged, mb remove
      derivedDB(dbId)(k) = EDB()
      deltaDB(dbId)(k) = EDB()
    })
    edbs.foreach((k, relation) => {
      deltaDB(dbId)(k) = EDB()
    }) // Delta-EDB is just empty sets
    dbId += 1
    dbId - 1
  }

  def clearDB(derived: Boolean, dbId: Int): Unit =
    if (derived)
      derivedDB(dbId).foreach((i, e) => e.clear())
    else
      deltaDB(dbId).foreach((i, e) => e.clear())

  def getIDBResult(rId: Int, dbId: Int): Set[Seq[Term]] =
    debug("Final IDB Result: ", () => s"@$dbId")
    getDerivedDB(rId, dbId).map(s => s.toSeq).toSet
  def getEDBResult(rId: Int): Set[Seq[Term]] = edbs.getOrElse(rId, EDB()).map(s => s.toSeq).toSet

  def getDerivedDB(rId: Int, dbId: Int): EDB = derivedDB(dbId)(rId)
  def getDeltaDB(rId: Int, dbId: Int): EDB = deltaDB(dbId)(rId)

  def resetDerived(rId: Int, dbId: Int, rules: Relation[StorageTerm], prev: Relation[StorageTerm] = Relation[StorageTerm]()): Unit =
    derivedDB(dbId)(rId) = rules ++ prev
  def resetDelta(rId: Int, dbId: Int, rules: Relation[StorageTerm]): Unit =
    deltaDB(dbId)(rId) = rules

  def compareDeltaDBs(dbId1: Int): Boolean =
    deltaDB(dbId1).exists((k, v) => v.nonEmpty)
  def compareDerivedDBs(dbId1: Int, dbId2: Int): Boolean =
    derivedDB(dbId1) == derivedDB(dbId2)

  def union(edbs: Seq[EDB]): EDB =
    edbs.flatten.distinct.to(mutable.ArrayBuffer)

  def getDiff(lhs: EDB, rhs: EDB): EDB =
    lhs diff rhs
}