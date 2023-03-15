package datalog.storage

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.{AllIndexes, JoinIndexes}
import SimpleCasts.*
import datalog.tools.Debug.debug

import scala.collection.{Iterator, immutable, mutable}

abstract class SimpleStorageManager(override val ns: NS) extends StorageManager(ns) {
  // "database", i.e. relationID => Relation
  protected val edbs: SimpleDatabase = SimpleDatabase()
  var knownDbId: KnowledgeId = -1
  var newDbId: KnowledgeId = -1

  // dbID => database, because we swap between read (known) and write (new)
  var dbId = 0
  protected val derivedDB: mutable.Map[KnowledgeId, SimpleDatabase] = mutable.Map[KnowledgeId, SimpleDatabase]()
  protected val deltaDB: mutable.Map[KnowledgeId, SimpleDatabase] = mutable.Map[KnowledgeId, SimpleDatabase]()

  val allRulesAllIndexes: mutable.Map[RelationId, AllIndexes] = mutable.Map.empty
  val printer: Printer[this.type] = Printer[this.type](this)

  val relOps: VolcanoOperators[this.type] = VolcanoOperators(this)

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
    derivedDB.addOne(dbId, SimpleDatabase())
    deltaDB.addOne(dbId, SimpleDatabase())

    edbs.foreach((k, relation) => {
      deltaDB(dbId)(k) = SimpleEDB()
    }) // Delta-EDB is just empty sets
    dbId += 1

    newDbId = dbId
    derivedDB.addOne(dbId, SimpleDatabase())
    deltaDB.addOne(dbId, SimpleDatabase())

    edbs.foreach((k, relation) => {
      deltaDB(dbId)(k) = SimpleEDB()
    }) // Delta-EDB is just empty sets
    dbId += 1
  }

  // Read & Write EDBs
  override def insertEDB(rule: Atom): Unit = {
    if (edbs.contains(rule.rId))
      edbs(rule.rId).addOne(SimpleRow(rule.terms))
    else
      edbs(rule.rId) = SimpleEDB()
      edbs(rule.rId).addOne(SimpleRow(rule.terms))
  }
  def getEmptyEDB(): SimpleEDB = SimpleEDB()
  def getEDB(rId: RelationId): SimpleEDB = edbs(rId)
  def edbContains(rId: RelationId): Boolean = edbs.contains(rId)
  def getAllEDBS(): mutable.Map[RelationId, Any] = edbs.wrapped.asInstanceOf[mutable.Map[RelationId, Any]]

  // Read intermediate results
  def getKnownDerivedDB(rId: RelationId): SimpleEDB =
    derivedDB(knownDbId).getOrElse(rId, edbs.getOrElse(rId, SimpleEDB()))
  def getNewDerivedDB(rId: RelationId): SimpleEDB =
    derivedDB(newDbId).getOrElse(rId, edbs.getOrElse(rId, SimpleEDB()))
  def getKnownDeltaDB(rId: RelationId): SimpleEDB =
    deltaDB(knownDbId).getOrElse(rId, edbs.getOrElse(rId, SimpleEDB()))
  def getNewDeltaDB(rId: RelationId): SimpleEDB =
    deltaDB(newDbId).getOrElse(rId, edbs.getOrElse(rId, SimpleEDB()))

  // Read final results
  def getKnownIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug("Final IDB Result[known]: ", () => s"at iteration $iteration: @$knownDbId, count=${getKnownDerivedDB(rId).length}")
    getKnownDerivedDB(rId).getSetOfSeq
  def getNewIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug(s"Final IDB Result[new]", () => s" at iteration $iteration: @$newDbId, count=${getNewDerivedDB(rId).length}")
    getNewDerivedDB(rId).getSetOfSeq
  def getEDBResult(rId: RelationId): Set[Seq[Term]] =
    edbs.getOrElse(rId, SimpleEDB()).getSetOfSeq

  // Write intermediate results
  def resetKnownDerived(rId: RelationId, rulesEDB: EDB, prevEDB: EDB = SimpleEDB()): Unit =
    val rules = asSimpleEDB(rulesEDB)
    val prev = asSimpleEDB(prevEDB)
    derivedDB(knownDbId)(rId) = rules.concat(prev)
  def resetKnownDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(knownDbId)(rId) = asSimpleEDB(rules)
  def resetNewDerived(rId: RelationId, rulesEDB: EDB, prevEDB: EDB = SimpleEDB()): Unit =
    val rules = asSimpleEDB(rulesEDB)
    val prev = asSimpleEDB(prevEDB)
    derivedDB(newDbId)(rId) = rules.concat(prev) // TODO: maybe use insert not concat
  def resetNewDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(newDbId)(rId) = asSimpleEDB(rules)
  def clearNewDerived(): Unit =
    derivedDB(newDbId).foreach((i, e) => e.clear())

  // Compare & Swap
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

  // Volcano helpers
  def union(edbs: Seq[EDB]): EDB =
    import SimpleEDB.unionEDB
    edbs.unionEDB

  def diff(lhsEDB: EDB, rhsEDB: EDB): EDB =
    val lhs = asSimpleEDB(lhsEDB)
    val rhs = asSimpleEDB(rhsEDB)
    lhs diff rhs

  override def toString() = {
    def printHelperRelation(i: Int, db: SimpleDatabase): String = {
      val name = if (i == knownDbId) "known" else if (i == newDbId) "new" else s"!!!OTHER($i)"
      "\n" + name + ": " + printer.edbToString(db)
    }

    "+++++\n" +
      "EDB:" + printer.edbToString(edbs) +
      "\nDERIVED:" + derivedDB.map(printHelperRelation).mkString("[", ", ", "]") +
      "\nDELTA:" + deltaDB.map(printHelperRelation).mkString("[", ", ", "]") +
      "\n+++++"
  }
}