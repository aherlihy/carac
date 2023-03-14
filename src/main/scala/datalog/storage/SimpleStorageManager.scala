package datalog.storage

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.{JoinIndexes, AllIndexes}
import datalog.tools.Debug.debug

import scala.collection.{immutable, mutable}

class SimpleEDB(val wrapped: mutable.ArrayBuffer[SimpleRow]) extends EDB {
  def this(elems: SimpleRow*) = this(mutable.ArrayBuffer[SimpleRow](elems*))
  def asSimpleEDB(to: Relation[StorageTerm]): SimpleEDB = to.asInstanceOf[SimpleEDB]
  def asSimpleRow(to: Row[StorageTerm]): SimpleRow = to.asInstanceOf[SimpleRow]

  export wrapped.{length, clear, nonEmpty, toSet}

  def addOne(elem: Row[StorageTerm]): this.type =
    wrapped.addOne(asSimpleRow(elem))
    this
  def diff(that: Relation[StorageTerm]): Relation[StorageTerm] =
    SimpleEDB(wrapped.diff(asSimpleEDB(that).wrapped))
  def prependedAll(suffix: Relation[StorageTerm]): Relation[StorageTerm] =
    SimpleEDB(wrapped.prependedAll(asSimpleEDB(suffix).wrapped))

  def getSetOfSeq: Set[Seq[StorageTerm]] = wrapped.map(s => s.toSeq).toSet

}
object SimpleEDB {
  extension (edbs: Seq[EDB])
    def unionEDB: EDB =
      SimpleEDB(edbs.flatten(using _.asInstanceOf[SimpleEDB].wrapped).distinct.to(mutable.ArrayBuffer))
}
class SimpleRow(val wrapped: Seq[StorageTerm]) extends Row[StorageTerm] {
  def toSeq = wrapped
  def length: Int = wrapped.length
}

abstract class SimpleStorageManager(override val ns: NS) extends StorageManager(ns) {
//  type StorageTerm = Term
//  type StorageVariable = Variable
//  type StorageConstant = Constant
//  type Row[+T] = Seq[T] // IndexedSeq and staging not compatible
//  def Row[T](c: T*) = Seq[T](c: _*)
//  type Table[T] = mutable.ArrayBuffer[T]
//  def Table[T](r: T*) = mutable.ArrayBuffer[T](r: _*)
//  type Relation[T] = Table[Row[T]]
//  def Relation[T](c: Row[T]*) = Table[Row[T]](c: _*)

  type Database[K, V] = mutable.Map[K, V]

  type FactDatabase = Database[RelationId, EDB]
  def FactDatabase(e: (RelationId, EDB)*) = mutable.Map[RelationId, EDB](e: _*)

//  def EDB(c: Row[StorageTerm]*) = Relation[StorageTerm](c: _*)

  // "database", i.e. relationID => Relation
  val edbs: FactDatabase = FactDatabase()
  var knownDbId: KnowledgeId = -1
  var newDbId: KnowledgeId = -1

  // dbID => database, because we swap between read (known) and write (new)
  var dbId = 0
  val derivedDB: Database[KnowledgeId, FactDatabase] = mutable.Map[KnowledgeId, FactDatabase]()
  val deltaDB: Database[KnowledgeId, FactDatabase] = mutable.Map[KnowledgeId, FactDatabase]()

  val allRulesAllIndexes: mutable.Map[RelationId, AllIndexes] = mutable.Map.empty
  val printer: Printer[this.type] = Printer[this.type](this)

  val relOps: RelationalOperators[this.type] = RelationalOperators(this)

  def initRelation(rId: RelationId, name: String): Unit = {
    ns(rId) = name
  }
  /**
   * Initialize derivedDB to clone EDBs, initialize deltaDB to empty for both new and known
   *
   * @return
   */
  def initEvaluation(): Unit = {
    // TODO: for now reinit with each solve(), don't keep around previous discovered facts. Future work -> incremental
    iteration = 0
    dbId = 0
    knownDbId = dbId
    derivedDB.addOne(dbId, FactDatabase())
    deltaDB.addOne(dbId, FactDatabase())

    edbs.foreach((k, relation) => {
      deltaDB(dbId)(k) = SimpleEDB()
    }) // Delta-EDB is just empty sets
    dbId += 1

    newDbId = dbId
    derivedDB.addOne(dbId, FactDatabase())
    deltaDB.addOne(dbId, FactDatabase())

    edbs.foreach((k, relation) => {
      deltaDB(dbId)(k) = SimpleEDB()
    }) // Delta-EDB is just empty sets
    dbId += 1
  }

  override def insertEDB(rule: Atom): Unit = {
    if (edbs.contains(rule.rId))
      edbs(rule.rId).addOne(SimpleRow(rule.terms))
    else
      edbs(rule.rId) = SimpleEDB()
      edbs(rule.rId).addOne(SimpleRow(rule.terms))
  }
  def getEmptyEDB(): EDB = SimpleEDB()

  def edb(rId: RelationId): EDB = edbs(rId)

  def getKnownDerivedDB(rId: RelationId): EDB =
    derivedDB(knownDbId).getOrElse(rId, edbs.getOrElse(rId, SimpleEDB()))
  def getNewDerivedDB(rId: RelationId): EDB =
    derivedDB(newDbId).getOrElse(rId, edbs.getOrElse(rId, SimpleEDB()))
  def getKnownDeltaDB(rId: RelationId): EDB =
    deltaDB(knownDbId).getOrElse(rId, edbs.getOrElse(rId, SimpleEDB()))
  def getNewDeltaDB(rId: RelationId): EDB =
    deltaDB(newDbId).getOrElse(rId, edbs.getOrElse(rId, SimpleEDB()))
  def getKnownIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug("Final IDB Result[known]: ", () => s"at iteration $iteration: @$knownDbId, count=${getKnownDerivedDB(rId).length}")
    getKnownDerivedDB(rId).getSetOfSeq
  def getNewIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug(s"Final IDB Result[new]", () => s" at iteration $iteration: @$newDbId, count=${getNewDerivedDB(rId).length}")
    getNewDerivedDB(rId).getSetOfSeq
  def getEDBResult(rId: RelationId): Set[Seq[Term]] =
    edbs.getOrElse(rId, SimpleEDB()).getSetOfSeq
  def resetKnownDerived(rId: RelationId, rules: EDB, prev: EDB = SimpleEDB()): Unit =
    derivedDB(knownDbId)(rId) = rules.prependedAll(prev)
  def resetKnownDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(knownDbId)(rId) = rules
  def resetNewDerived(rId: RelationId, rules: EDB, prev: EDB = SimpleEDB()): Unit =
    derivedDB(newDbId)(rId) = rules.prependedAll(prev)
  def resetNewDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(newDbId)(rId) = rules
  def clearNewDerived(): Unit =
    derivedDB(newDbId).foreach((i, e) => e.clear())

  def swapKnowledge(): Unit = {
    iteration += 1
    val t = knownDbId
    knownDbId = newDbId
    newDbId = t
  }
  def compareNewDeltaDBs(): Boolean =
    deltaDB(newDbId).exists((k, v) => v.nonEmpty)
  def compareDerivedDBs(): Boolean =
    derivedDB(knownDbId) == derivedDB(newDbId)

  def verifyEDBs(idbList: mutable.Set[RelationId]): Unit = {
    ns.rIds().foreach(rId =>
      if (!edbs.contains(rId) && !idbList.contains(rId)) // treat undefined relations as empty edbs
        edbs(rId) = SimpleEDB()
    )
  }

  def union(edbs: Seq[EDB]): EDB =
//    edbs.head.union(edbs.drop(1))
    import SimpleEDB.unionEDB
    edbs.unionEDB

  def diff(lhs: EDB, rhs: EDB): EDB =
    lhs diff rhs
}