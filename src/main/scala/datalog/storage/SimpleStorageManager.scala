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

  type FactDatabase = Database[RelationId, EDB]
  def FactDatabase(e: (RelationId, EDB)*) = mutable.Map[RelationId, EDB](e: _*)
  type RuleDatabase = Database[RelationId, IDB]
  def RuleDatabase(e: (RelationId, IDB)*) = mutable.Map[RelationId, IDB](e: _*)

  def EDB(c: Row[StorageTerm]*) = Relation[StorageTerm](c: _*)
  def IDB(c: Row[StorageAtom]*) = Relation[StorageAtom](c: _*)

  // "database", i.e. relationID => Relation
  val edbs: FactDatabase = FactDatabase()
  val idbs: RuleDatabase = RuleDatabase()
  var knownDbId: KnowledgeId = -1
  var newDbId: KnowledgeId = -1

  // dbID => database, because we swap between read (known) and write (new)
  var dbId = 0
  val derivedDB: Database[KnowledgeId, FactDatabase] = mutable.Map[KnowledgeId, FactDatabase]()
  val deltaDB: Database[KnowledgeId, FactDatabase] = mutable.Map[KnowledgeId, FactDatabase]()

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
    knownDbId = dbId
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

    newDbId = dbId
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
  }

  override def insertEDB(rule: Atom): Unit = {
    if (edbs.contains(rule.rId))
      edbs(rule.rId).addOne(rule.terms)
    else
      edbs(rule.rId) = EDB()
      edbs(rule.rId).addOne(rule.terms)
  }
  override def insertIDB(rId: RelationId, rule: Seq[Atom]): Unit = {
    val row = Row(rule.map(a => StorageAtom(a.rId, a.terms))*)
    idbs.getOrElseUpdate(rId, IDB()).addOne(row)
    // TODO: could do this in the topsort instead of as inserted
  }

  def idb(rId: RelationId): IDB = idbs(rId)
  def edb(rId: RelationId): EDB = edbs(rId)

  def getHelper(db: FactDatabase, rId: RelationId, orElse: Option[EDB], whichDb: DB, knowledgeId: KNOWLEDGE): EDB =
    orElse match {
      case Some(value) =>
        db.getOrElse(rId, value)
      case _ =>
        if (!db.contains(rId)) // annoying but good for error reporting
          throw new Exception(s"DB $whichDb[$knowledgeId] does not contain relation ${ns(rId)} (#$rId)")
        else
          db(rId)
    }

  def getKnownDerivedDB(rId: RelationId, orElse: Option[EDB]): EDB =
    getHelper(
      derivedDB(knownDbId),
      rId,
      orElse,
      DB.Derived, KNOWLEDGE.Known
    )
  def getNewDerivedDB(rId: RelationId, orElse: Option[EDB]): EDB =
    getHelper(
      derivedDB(newDbId),
      rId,
      orElse,
      DB.Derived, KNOWLEDGE.New
    )
  def getKnownDeltaDB(rId: RelationId, orElse: Option[EDB]): EDB =
    getHelper(
      deltaDB(knownDbId),
      rId,
      orElse,
      DB.Delta, KNOWLEDGE.Known
    )
  def getNewDeltaDB(rId: RelationId, orElse: Option[EDB]): EDB =
    getHelper(
      deltaDB(newDbId),
      rId,
      orElse,
      DB.Delta, KNOWLEDGE.New
    )
  def getKnownIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug("Final IDB Result[known]: ", () => s"@$knownDbId")
    getKnownDerivedDB(rId).map(s => s.toSeq).toSet
  def getNewIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug("Final IDB Result[known]: ", () => s"@$newDbId")
    getNewDerivedDB(rId).map(s => s.toSeq).toSet
  def getEDBResult(rId: RelationId): Set[Seq[Term]] = edbs.getOrElse(rId, EDB()).map(s => s.toSeq).toSet

  def resetKnownDerived(rId: RelationId, rules: Relation[StorageTerm], prev: Relation[StorageTerm] = Relation[StorageTerm]()): Unit =
    derivedDB(knownDbId)(rId) = rules ++ prev
  def resetKnownDelta(rId: RelationId, rules: Relation[StorageTerm]): Unit =
    deltaDB(knownDbId)(rId) = rules
  def resetNewDerived(rId: RelationId, rules: Relation[StorageTerm], prev: Relation[StorageTerm] = Relation[StorageTerm]()): Unit =
    derivedDB(newDbId)(rId) = rules ++ prev
  def resetNewDelta(rId: RelationId, rules: Relation[StorageTerm]): Unit =
    deltaDB(newDbId)(rId) = rules
  def clearNewDB(derived: Boolean): Unit =
    if (derived)
      derivedDB(newDbId).foreach((i, e) => e.clear())
    else
      deltaDB(newDbId).foreach((i, e) => e.clear())

  def swapKnowledge(): Unit = {
    val t = knownDbId
    knownDbId = newDbId
    newDbId = t
  }
  def compareNewDeltaDBs(): Boolean =
    deltaDB(newDbId).exists((k, v) => v.nonEmpty)
  def compareDerivedDBs(): Boolean =
    derivedDB(knownDbId) == derivedDB(newDbId)

  def verifyEDBs(): Unit = {
    ns.rIds().foreach(rId =>
      if (!edbs.contains(rId) && !idbs.contains(rId)) // treat undefined relations as empty edbs
        edbs(rId) = EDB()
    )
  }
  def verifyEDBs(idbList: mutable.Set[RelationId]): Unit = {
    ns.rIds().foreach(rId =>
      if (!edbs.contains(rId) && !idbList.contains(rId)) // treat undefined relations as empty edbs
        edbs(rId) = EDB()
    )
  }

  def union(edbs: Seq[EDB]): EDB =
    edbs.flatten.distinct.to(mutable.ArrayBuffer)
  def diff(lhs: EDB, rhs: EDB): EDB =
    lhs diff rhs
}