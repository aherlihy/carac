package datalog.storage

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.{AllIndexes, JoinIndexes}
import CollectionsCasts.*
import datalog.tools.Debug.debug

import scala.collection.{Iterator, immutable, mutable}

abstract class CollectionsStorageManager(override val ns: NS) extends StorageManager(ns) {
  // "database", i.e. relationID => Relation
  protected val edbs: CollectionsDatabase = CollectionsDatabase() // raw user-supplied EDBs from initialization.
  protected val discoveredFacts: CollectionsDatabase = CollectionsDatabase() // all EDBs + facts discovered in previous strata
  var knownDbId: KnowledgeId = -1
  var newDbId: KnowledgeId = -1

  // dbID => database, because we swap between read (known) and write (new) within iterations
  var dbId = 0
  protected val derivedDB: mutable.Map[KnowledgeId, CollectionsDatabase] = mutable.Map[KnowledgeId, CollectionsDatabase]()
  protected val deltaDB: mutable.Map[KnowledgeId, CollectionsDatabase] = mutable.Map[KnowledgeId, CollectionsDatabase]()

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
    derivedDB.addOne(dbId, CollectionsDatabase())
    deltaDB.addOne(dbId, CollectionsDatabase())

    edbs.foreach((k, relation) => {
      deltaDB(dbId)(k) = CollectionsEDB()
      discoveredFacts(k) = relation
    }) // Delta-EDB is just empty sets
    dbId += 1

    newDbId = dbId
    derivedDB.addOne(dbId, CollectionsDatabase())
    deltaDB.addOne(dbId, CollectionsDatabase())

    edbs.foreach((k, relation) => {
      deltaDB(dbId)(k) = CollectionsEDB()
    }) // Delta-EDB is just empty sets
    dbId += 1
  }

  // Read & Write EDBs
  override def insertEDB(rule: Atom): Unit = {
    if (edbs.contains(rule.rId))
      edbs(rule.rId).addOne(CollectionsRow(rule.terms))
    else
      edbs(rule.rId) = CollectionsEDB()
      edbs(rule.rId).addOne(CollectionsRow(rule.terms))
  }
  def getEmptyEDB(): CollectionsEDB = CollectionsEDB()
  def getEDB(rId: RelationId): CollectionsEDB = edbs(rId)
  def edbContains(rId: RelationId): Boolean = edbs.contains(rId)
  def getAllEDBS(): mutable.Map[RelationId, Any] = edbs.wrapped.asInstanceOf[mutable.Map[RelationId, Any]]

  // Read intermediate results
  def getKnownDerivedDB(rId: RelationId): CollectionsEDB =
    derivedDB(knownDbId).getOrElse(rId, discoveredFacts.getOrElse(rId, CollectionsEDB()))
  def getNewDerivedDB(rId: RelationId): CollectionsEDB =
    derivedDB(newDbId).getOrElse(rId, discoveredFacts.getOrElse(rId, CollectionsEDB()))
  def getKnownDeltaDB(rId: RelationId): CollectionsEDB =
    deltaDB(knownDbId).getOrElse(rId, discoveredFacts.getOrElse(rId, CollectionsEDB()))
  def getNewDeltaDB(rId: RelationId): CollectionsEDB =
    deltaDB(newDbId).getOrElse(rId, discoveredFacts.getOrElse(rId, CollectionsEDB()))

  // Read final results
  def getKnownIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug("Final IDB Result[known]: ", () => s"at iteration $iteration: @$knownDbId, count=${getKnownDerivedDB(rId).length}")
    getKnownDerivedDB(rId).getSetOfSeq
  def getNewIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug(s"Final IDB Result[new]", () => s" at iteration $iteration: @$newDbId, count=${getNewDerivedDB(rId).length}")
    getNewDerivedDB(rId).getSetOfSeq
  def getEDBResult(rId: RelationId): Set[Seq[Term]] =
    edbs.getOrElse(rId, CollectionsEDB()).getSetOfSeq

  // Write intermediate results
  def resetKnownDerived(rId: RelationId, rulesEDB: EDB, prevEDB: EDB = CollectionsEDB()): Unit =
    val rules = asCollectionsEDB(rulesEDB)
    val prev = asCollectionsEDB(prevEDB)
    derivedDB(knownDbId)(rId) = rules.concat(prev)
  def resetKnownDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(knownDbId)(rId) = asCollectionsEDB(rules)
  def resetNewDerived(rId: RelationId, rulesEDB: EDB, prevEDB: EDB = CollectionsEDB()): Unit =
    val rules = asCollectionsEDB(rulesEDB)
    val prev = asCollectionsEDB(prevEDB)
    derivedDB(newDbId)(rId) = rules.concat(prev) // TODO: maybe use insert not concat
  def resetNewDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(newDbId)(rId) = asCollectionsEDB(rules)
  def clearNewDerived(): Unit =
    derivedDB(newDbId) = CollectionsDatabase()
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

  /**
   * Copy all the derived facts at the end of the current strata into discoveredFacts
   * to be fed into the next strata as EDBs
    */
  def updateDiscovered(): Unit =
    derivedDB(knownDbId).foreach((relation, facts) =>
      discoveredFacts(relation) = facts
    )

  def verifyEDBs(idbList: mutable.Set[RelationId]): Unit = {
    ns.rIds().foreach(rId =>
      if (!edbs.contains(rId) && !idbList.contains(rId)) // treat undefined relations as empty edbs
        edbs(rId) = CollectionsEDB()
    )
  }

  // Volcano helpers
  def union(edbs: Seq[EDB]): EDB =
    import CollectionsEDB.unionEDB
    edbs.unionEDB

  def diff(lhsEDB: EDB, rhsEDB: EDB): EDB =
    val lhs = asCollectionsEDB(lhsEDB)
    val rhs = asCollectionsEDB(rhsEDB)
    lhs diff rhs

  override def toString() = {
    def printHelperRelation(i: Int, db: CollectionsDatabase): String = {
      val name = if (i == knownDbId) "known" else if (i == newDbId) "new" else s"!!!OTHER($i)"
      "\n" + name + ": " + printer.edbToString(db)
    }

    "+++++\n" +
      "EDB:" + printer.edbToString(edbs) +
      "\nFACTS:" + printer.edbToString(discoveredFacts) +
      "\nDERIVED:" + derivedDB.map(printHelperRelation).mkString("[", ", ", "]") +
      "\nDELTA:" + deltaDB.map(printHelperRelation).mkString("[", ", ", "]") +
      "\n+++++"
  }
}