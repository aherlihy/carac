package datalog.storage

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.{AllIndexes, JoinIndexes}
import datalog.storage.IndexedCollectionsCasts.*
import datalog.tools.Debug.debug

import scala.collection.mutable.ArrayBuffer
import scala.collection.{Iterator, immutable, mutable}

abstract class IndexedCollectionsStorageManager(override val ns: NS) extends StorageManager(ns) {
  // "database", i.e. relationID => Relation
  protected val edbs: IndexedCollectionsDatabase = IndexedCollectionsDatabase() // raw user-supplied EDBs from initialization.
  val edbDomain: mutable.Set[StorageTerm] = mutable.Set.empty // incrementally grow the total domain of all EDBs, used for calculating complement of negated predicates
  protected val discoveredFacts: IndexedCollectionsDatabase = IndexedCollectionsDatabase() // all EDBs + facts discovered in previous strata
  var knownDbId: KnowledgeId = -1
  var newDbId: KnowledgeId = -1

  // dbID => database, because we swap between read (known) and write (new) within iterations
  var dbId = 0
  protected val derivedDB: mutable.Map[KnowledgeId, IndexedCollectionsDatabase] = mutable.Map[KnowledgeId, IndexedCollectionsDatabase]()
  protected val deltaDB: mutable.Map[KnowledgeId, IndexedCollectionsDatabase] = mutable.Map[KnowledgeId, IndexedCollectionsDatabase]()

  val allRulesAllIndexes: mutable.Map[RelationId, AllIndexes] = mutable.Map.empty // Index => position
  val indexCandidates: mutable.Map[RelationId, mutable.Set[Int]] = mutable.Map[RelationId, mutable.Set[Int]]().withDefaultValue(mutable.Set[Int]()) // relative position of atoms with constant or variable locations

  def registerIndexCandidates(cands: mutable.Map[RelationId, mutable.Set[Int]]): Unit = {
    edbs.foreach((rId, edb) => edb.bulkRegisterIndex(cands(rId)))
    cands.foreach((rId, cs) => indexCandidates(rId).addAll(cs))
  }
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
    derivedDB.addOne(dbId, IndexedCollectionsDatabase())
    deltaDB.addOne(dbId, IndexedCollectionsDatabase())

    edbs.foreach((rId, relation) => {
      deltaDB(dbId)(rId) = IndexedCollectionsEDB.empty(indexCandidates(rId), ns(rId))
      discoveredFacts(rId) = relation
    }) // Delta-EDB is just empty sets
    dbId += 1

    newDbId = dbId
    derivedDB.addOne(dbId, IndexedCollectionsDatabase())
    deltaDB.addOne(dbId, IndexedCollectionsDatabase())

    edbs.foreach((rId, relation) => {
      deltaDB(dbId)(rId) = IndexedCollectionsEDB.empty(indexCandidates(rId), ns(rId))
    }) // Delta-EDB is just empty sets
    dbId += 1
  }

  // Read & Write EDBs
  override def insertEDB(rule: Atom): Unit = {
    if (edbs.contains(rule.rId))
      edbs(rule.rId).addOne(IndexedCollectionsRow(rule.terms))
    else
      edbs(rule.rId) = IndexedCollectionsEDB.empty(indexCandidates(rule.rId), ns(rule.rId))
      edbs(rule.rId).addOne(IndexedCollectionsRow(rule.terms))
    edbDomain.addAll(rule.terms)
  }
  /* Call when adding an IDB rule so domain can grow incrementally */
  override def addConstantsToDomain(constants: Seq[StorageTerm]): Unit = {
    edbDomain.addAll(constants)
  }
  def getEmptyEDB(): IndexedCollectionsEDB = IndexedCollectionsEDB.empty()
  def getEDB(rId: RelationId): IndexedCollectionsEDB = edbs(rId)
  def edbContains(rId: RelationId): Boolean = edbs.contains(rId)
  def getAllEDBS(): mutable.Map[RelationId, Any] = edbs.wrapped.asInstanceOf[mutable.Map[RelationId, Any]]

  /**
   * Used for computing DOM(k) of a negated relation. Returns the (unchanging) set of possible EDB values +
   * constants in all IDB rules. Currently unused because we incrementally add elements to the domain but may
   * be useful if we want a domain containing only predicates from <= strata.
   */
// Comment out until we can track domain in something other than indexes
//  def computeDomain(): Set[StorageTerm] = {
//    val constants = mutable.Set[StorageTerm]()
//    edbs.foreach((_, rows) => // avoid map or flatMap for IndexedCollectionsDatabase, CollectionRow
//      rows.foreach(row =>
//        constants.addAll(row.toSeq)
//      )
//    )
//    constants.addAll(allRulesAllIndexes.flatMap((_, allIndexes) =>
//      allIndexes.head._2.constIndexes.values
//    ))
//    constants.toSet
//  }

  /**
   * Compute Dom * Dom * ... arity # times
   */
  override def getComplement(arity: Int): IndexedCollectionsEDB = ??? //{
    // short but inefficient
//    val res = List.fill(arity)(edbDomain).flatten.combinations(arity).flatMap(_.permutations).toSeq
//    IndexedCollectionsEDB( // TODO: fix
//      res.map(r => IndexedCollectionsRow(r.toSeq)):_*
//    )
//  }

  // Read intermediate results
  def getKnownDerivedDB(rId: RelationId): IndexedCollectionsEDB =
    derivedDB(knownDbId).getOrElse(rId, discoveredFacts.getOrElse(rId, IndexedCollectionsEDB.empty(indexCandidates(rId), ns(rId))))
  def getNewDerivedDB(rId: RelationId): IndexedCollectionsEDB =
    derivedDB(newDbId).getOrElse(rId, discoveredFacts.getOrElse(rId, IndexedCollectionsEDB.empty(indexCandidates(rId), ns(rId))))
  def getKnownDeltaDB(rId: RelationId): IndexedCollectionsEDB =
    deltaDB(knownDbId).getOrElse(rId, discoveredFacts.getOrElse(rId, IndexedCollectionsEDB.empty(indexCandidates(rId), ns(rId))))
  def getNewDeltaDB(rId: RelationId): IndexedCollectionsEDB =
    deltaDB(newDbId).getOrElse(rId, discoveredFacts.getOrElse(rId, IndexedCollectionsEDB.empty(indexCandidates(rId), ns(rId))))

  // Read final results
  def getKnownIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug("Final IDB Result[known]: ", () => s"at iteration $iteration: @$knownDbId, count=${getKnownDerivedDB(rId).length}")
    getKnownDerivedDB(rId).getSetOfSeq
  def getNewIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug(s"Final IDB Result[new]", () => s" at iteration $iteration: @$newDbId, count=${getNewDerivedDB(rId).length}")
    getNewDerivedDB(rId).getSetOfSeq
  def getEDBResult(rId: RelationId): Set[Seq[Term]] =
    edbs.getOrElse(rId, IndexedCollectionsEDB.empty()).getSetOfSeq

  // Write intermediate results
  def resetKnownDerived(rId: RelationId, rulesEDB: EDB, prevEDB: EDB = IndexedCollectionsEDB.empty()): Unit = // TODO: verify it's ok there's no indexes on rev
    val rules = asIndexedCollectionsEDB(rulesEDB)
    val prev = asIndexedCollectionsEDB(prevEDB)
    prev.name = ns(rId)
    derivedDB(knownDbId)(rId) = rules.concat(prev)
  def resetKnownDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(knownDbId)(rId) = asIndexedCollectionsEDB(rules)
  def resetNewDerived(rId: RelationId, rulesEDB: EDB, prevEDB: EDB = IndexedCollectionsEDB.empty()): Unit =
    val rules = asIndexedCollectionsEDB(rulesEDB)
    val prev = asIndexedCollectionsEDB(prevEDB)
    prev.name = ns(rId)
    derivedDB(newDbId)(rId) = rules.concat(prev) // TODO: maybe use insert not concat
  def resetNewDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(newDbId)(rId) = asIndexedCollectionsEDB(rules)
  def clearNewDerived(): Unit =
    derivedDB(newDbId) = IndexedCollectionsDatabase()
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
        edbs(rId) = IndexedCollectionsEDB.empty(indexCandidates(rId), ns(rId))
    )
  }

  // Volcano helpers
  def union(edbs: Seq[EDB]): EDB =
    import IndexedCollectionsEDB.unionEDB
    edbs.unionEDB

  def diff(lhsEDB: EDB, rhsEDB: EDB): EDB =
    val lhs = asIndexedCollectionsEDB(lhsEDB)
    val rhs = asIndexedCollectionsEDB(rhsEDB)
    lhs diff rhs

  override def toString() = {
    def printHelperRelation(i: Int, db: IndexedCollectionsDatabase): String = {
      val indexes = db.toSeq.map((rId, idxC) => printer.indexToString(ns(rId), idxC.indexes)).mkString("$indexes: [", ", ", "]")
      val name = if (i == knownDbId) "known" else if (i == newDbId) "new" else s"!!!OTHER($i)"
      s"\n $name : \n\t$indexes ${printer.edbToString(db)}"
    }

    "+++++\n" +
      "EDB:" + printer.edbToString(edbs) +
      "\nDISCOV:" + printer.edbToString(discoveredFacts) +
      "\nDERIVED:" + derivedDB.map(printHelperRelation).mkString("[", ", ", "]") +
      "\nDELTA:" + deltaDB.map(printHelperRelation).mkString("[", ", ", "]") +
      "\n+++++"
  }
}