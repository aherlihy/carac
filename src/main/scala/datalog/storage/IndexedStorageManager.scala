package datalog.storage

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.{AllIndexes, JoinIndexes}
import datalog.storage.IndexedCollectionsCasts.*
import datalog.tools.Debug.debug

import scala.collection.mutable.ArrayBuffer
import scala.collection.{Iterator, immutable, mutable}

class IndexedStorageManager(ns: NS = new NS()) extends StorageManager(ns) {
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
  val indexCandidates: mutable.Map[RelationId, mutable.Set[Int]] = mutable.Map[RelationId, mutable.Set[Int]]() // relative position of atoms with constant or variable locations
  val relationArity: mutable.Map[RelationId, Int] = mutable.Map[RelationId, Int]()

  // Update metadata if aliases are discovered
  def updateAliases(aliases: mutable.Map[RelationId, RelationId]): Unit = {
    aliases.foreach((k, v) =>
      if (relationArity.contains(k) && relationArity.contains(v) && relationArity(k) != relationArity(v))
        throw new Exception(s"Error: registering relations ${ns(k)} and ${ns(v)} as aliases but have different arity (${relationArity(k)} vs. ${relationArity(v)})")
      relationArity.getOrElseUpdate(k, relationArity.getOrElse(v, throw new Exception(s"No arity available for either ${ns(k)} or ${ns(v)}")))
      indexCandidates(k) = indexCandidates.getOrElseUpdate(k, mutable.Set[Int]()).addAll(indexCandidates.getOrElse(v, mutable.Set[Int]()))
      indexCandidates(v) = indexCandidates(k)
    )
  }

  // Store relative positions of shared variables as candidates for potential indexes
  def registerIndexCandidates(cands: mutable.Map[RelationId, mutable.Set[Int]]): Unit = {
    cands.foreach((rId, idxs) =>
      // adds indexes to any the EDBs
      if edbs.contains(rId) then edbs(rId).bulkRegisterIndex(idxs)
      // tells intermediate relations to build indexes
      indexCandidates.getOrElseUpdate(rId, mutable.Set[Int]()).addAll(idxs)
    )
  }
  // Store relation arity. In the future can require it to be declared, but for now derived.
  def registerRelationArity(rId: RelationId, arity: Int): Unit =
    if relationArity.contains(rId) then if arity != relationArity(rId) then throw new Exception(s"Derived relation $rId (${ns(rId)}) declared with arity $arity but previously declared with arity ${relationArity(rId)}")
    relationArity(rId) = arity

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
    derivedDB.addOne(knownDbId, IndexedCollectionsDatabase())
    deltaDB.addOne(knownDbId, IndexedCollectionsDatabase())

    edbs.foreach((rId, relation) => {
      deltaDB(knownDbId)(rId) = IndexedCollectionsEDB.empty(relation.arity, indexCandidates(rId), ns(rId), mutable.Set())
      discoveredFacts(rId) = relation
    }) // Delta-EDB is just empty sets
    dbId += 1

    newDbId = dbId
    derivedDB.addOne(newDbId, IndexedCollectionsDatabase())
    deltaDB.addOne(newDbId, IndexedCollectionsDatabase())

    edbs.foreach((rId, relation) => {
      deltaDB(newDbId)(rId) = IndexedCollectionsEDB.empty(relation.arity, indexCandidates(rId), ns(rId), mutable.Set()) // TODO: no indexes on delta.new, bc write into?
    }) // Delta-EDB is just empty sets
    dbId += 1
  }

  // Read & Write EDBs
  override def insertEDB(rule: Atom): Unit = {
    if (edbs.contains(rule.rId))
      if (rule.terms.length != relationArity(rule.rId)) throw new Exception(s"Inserted arity not equal to expected arity for relation #${rule.rId} ${ns(rule.rId)}")
      edbs(rule.rId).addOne(IndexedCollectionsRow(rule.terms))
    else
      relationArity(rule.rId) = rule.terms.length
      indexCandidates.getOrElseUpdate(rule.rId, mutable.Set[Int]())
      edbs(rule.rId) = IndexedCollectionsEDB.empty(rule.terms.length, indexCandidates(rule.rId), ns(rule.rId), mutable.Set())
      edbs(rule.rId).addOne(IndexedCollectionsRow(rule.terms))
    edbDomain.addAll(rule.terms)
  }
  /* Call when adding an IDB rule so domain can grow incrementally */
  override def addConstantsToDomain(constants: Seq[StorageTerm]): Unit = {
    edbDomain.addAll(constants)
  }
  // Only used when querying for a completely empty EDB that hasn't been declared/added yet.
  def getEmptyEDB(rId: RelationId): IndexedCollectionsEDB =
    if (!relationArity.contains(rId))
      throw new Exception(s"Getting empty relation $rId (${ns(rId)}) but undefined")
    IndexedCollectionsEDB.empty(relationArity(rId), indexCandidates.getOrElse(rId, mutable.Set[Int]()), ns(rId), mutable.Set())
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
  override def getComplement(rId: RelationId, arity: Int): IndexedCollectionsEDB = {
    // short but inefficient
    val res = List.fill(arity)(edbDomain).flatten.combinations(arity).flatMap(_.permutations).toSeq
    IndexedCollectionsEDB(
      mutable.ArrayBuffer.from(res.map(r => IndexedCollectionsRow(r.toSeq))), indexCandidates(rId), ns(rId), arity, mutable.Set()
    )
  }

  // Read intermediate results
  def getKnownDerivedDB(rId: RelationId): IndexedCollectionsEDB =
    if !relationArity.contains(rId) then throw new Exception(s"Internal error: relation $rId (${ns(rId)}) has no arity")
    derivedDB(knownDbId).getOrElse(rId, discoveredFacts.getOrElse(rId, IndexedCollectionsEDB.empty(relationArity(rId), indexCandidates(rId), ns(rId), mutable.Set())))
  def getNewDerivedDB(rId: RelationId): IndexedCollectionsEDB =
    if !relationArity.contains(rId) then throw new Exception(s"Internal error: relation $rId (${ns(rId)}) has no arity")
    derivedDB(newDbId).getOrElse(rId, discoveredFacts.getOrElse(rId, IndexedCollectionsEDB.empty(relationArity(rId), indexCandidates(rId), ns(rId), mutable.Set())))
  def getKnownDeltaDB(rId: RelationId): IndexedCollectionsEDB =
    if !relationArity.contains(rId) then throw new Exception(s"Internal error: relation $rId (${ns(rId)}) has no arity")
    deltaDB(knownDbId).getOrElse(rId, discoveredFacts.getOrElse(rId, IndexedCollectionsEDB.empty(relationArity(rId), indexCandidates(rId), ns(rId), mutable.Set())))
  def getNewDeltaDB(rId: RelationId): IndexedCollectionsEDB =
    if !relationArity.contains(rId) then throw new Exception(s"Internal error: relation $rId (${ns(rId)}) has no arity")
    deltaDB(newDbId).getOrElse(rId, discoveredFacts.getOrElse(rId, IndexedCollectionsEDB.empty(relationArity(rId), indexCandidates(rId), ns(rId), mutable.Set())))

  // Read final results
  def getKnownIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug("Final IDB Result[known]: ", () => s"at iteration $iteration: @$knownDbId, count=${getKnownDerivedDB(rId).length}")
    getKnownDerivedDB(rId).getSetOfSeq
  def getNewIDBResult(rId: RelationId): Set[Seq[Term]] =
    debug(s"Final IDB Result[new]", () => s" at iteration $iteration: @$newDbId, count=${getNewDerivedDB(rId).length}")
    getNewDerivedDB(rId).getSetOfSeq
  def getEDBResult(rId: RelationId): Set[Seq[Term]] =
    edbs.getOrElse(rId, IndexedCollectionsEDB.empty(relationArity.getOrElse(rId, 0), skipIndexes = mutable.Set())).getSetOfSeq

  // Write intermediate results
  def setKnownDerived(rId: RelationId, rules: EDB): Unit =
    derivedDB(knownDbId)(rId) = asIndexedCollectionsEDB(rules)

  def resetKnownDerived(rId: RelationId, rulesEDB: EDB, prevEDB: EDB): Unit =
    val rules = asIndexedCollectionsEDB(rulesEDB)
    val prev = asIndexedCollectionsEDB(prevEDB)
    // TODO: maybe use insert not copy, except problem is that rules is delta.new, and prev is derived.known. Delta cannot mutate because needed for the end-of-iteration check, and derived cannot mutate (?) because needed to be read by other rules potentially
    derivedDB(knownDbId)(rId) = rules.copyAndAdd(prev)

  def setKnownDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(knownDbId)(rId) = asIndexedCollectionsEDB(rules)

  def setNewDerived(rId: RelationId, rules: EDB): Unit =
    derivedDB(newDbId)(rId) = asIndexedCollectionsEDB(rules)

  def resetNewDerived(rId: RelationId, rulesEDB: EDB, prevEDB: EDB): Unit =
    val rules = asIndexedCollectionsEDB(rulesEDB)
    val prev = asIndexedCollectionsEDB(prevEDB)
    // TODO: maybe use insert not copy, except problem is that rules is delta.new, and prev is derived.known. Delta cannot mutate because needed for the end-of-iteration check, and derived cannot mutate (?) because needed to be read by other rules potentially
    derivedDB(newDbId)(rId) = rules.copyAndAdd(prev)

  def setNewDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(newDbId)(rId) = asIndexedCollectionsEDB(rules)

  def clearNewDerived(): Unit =
    derivedDB(newDbId) = IndexedCollectionsDatabase()
  // Compare & Swap
  def swapKnowledge(): Unit = {
    iteration += 1
    val t = knownDbId
    knownDbId = newDbId
    newDbId = t
    // rebuild indexes on all derived
//    derivedDB(knownDbId).foreach((rId, edb) => edb.bulkRebuildIndex())
//    derivedDB(newDbId).foreach((rId, edb) => edb.bulkRebuildIndex())
    deltaDB(knownDbId).foreach((rId, edb) => edb.bulkRebuildIndex())
//    deltaDB(newDbId).foreach((rId, edb) => edb.bulkRebuildIndex())
  }
  def compareNewDeltaDBs(): Boolean =
    deltaDB(newDbId).exists((k, v) => v.nonEmpty())
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
      if (!edbs.contains(rId) && !idbList.contains(rId))
        // NOTE: no longer treat undefined relations as empty edbs, instead error
        if (!relationArity.contains(rId))
          throw new Exception(s"Error: using EDB $rId (${ns(rId)}) but no known arity")
        edbs(rId) = IndexedCollectionsEDB.empty(
          relationArity(rId),
          indexCandidates.getOrElseUpdate(rId, mutable.Set()),
          ns(rId),
          mutable.Set()
        )
    )
  }

  // Volcano helpers
  def union(edbs: Seq[EDB]): EDB =
    import IndexedCollectionsEDB.{unionEDB, unionInPlace}
    edbs.unionEDB // unionInPlace is slower!

  def diff(lhsEDB: EDB, rhsEDB: EDB): EDB =
    val lhs = asIndexedCollectionsEDB(lhsEDB)
    val rhs = asIndexedCollectionsEDB(rhsEDB)
    lhs.diff(rhs) // diffInPlace is slower, also needs indexes to be updated!

  override def joinProjectHelper_withHash(inputsEDB: Seq[EDB], rId: Int, hash: String, onlineSort: Boolean): IndexedCollectionsEDB = {
    if onlineSort then throw new Exception("Unimplemented: online sort with indexes")

    val originalK = allRulesAllIndexes(rId)(hash)
    val inputs = asIndexedCollectionsSeqEDB(inputsEDB)
//     println(s"Rule: ${printer.ruleToString(originalK.atoms)}")
//     println(s"input rels: ${inputs.map(e => e.factToString).mkString("[", "*", "]")}")

    // var intermediateCardinalities = Seq[Int]()
    val fResult = if (inputs.length == 1) { // just filter + project
      inputs.head.projectFilterWithIndex(
        originalK.constIndexes,
        originalK.projIndexes,
        ns(rId),
        indexCandidates.getOrElse(rId, mutable.Set())
      )
    } else {
      val result = inputs
        .foldLeft(
          (IndexedCollectionsEDB.empty(0, skipIndexes = mutable.Set()), 0, originalK) // initialize intermediate indexed-collection
        )((combo: (IndexedCollectionsEDB, Int, JoinIndexes), innerT: IndexedCollectionsEDB) =>
          val outerT = combo._1
          val atomI = combo._2
          var k = combo._3 // not currently used bc not online sorting
          if (atomI == 0) // not a monad :(
            (innerT, atomI + 1, k)
          else
            //            val (inner, outer) = // on the fly swapping of join order
            //              if (atomI > 1 && onlineSort && outerT.length > innerT.length)
            //                val body = k.atoms.drop(1)
            //                val newerHash = JoinIndexes.getRuleHash(Seq(k.atoms.head, body(atomI)) ++ body.dropRight(body.length - atomI) ++ body.drop(atomI + 1))
            //                k = allRulesAllIndexes(rId).getOrElseUpdate(newerHash, JoinIndexes(originalK.atoms.head +: body, Some(originalK.cxns)))
            //                (outerT, innerT)
            //              else
            //                (innerT, outerT)
            val edbResult = outerT.joinFilterWithIndex(k, atomI, innerT)
            //            intermediateCardinalities = intermediateCardinalities :+ edbResult.length
            (edbResult, atomI + 1, k)
        )
      val indexesToIgnore = indexCandidates.getOrElse(rId, mutable.Set())
      IndexedCollectionsEDB(
        result._1.wrapped.mapInPlace(_.project(result._3.projIndexes)),
        indexesToIgnore,
        ns(rId),
        result._3.projIndexes.length,
        indexesToIgnore // remove all indices after proj/ going into union
      )
    }
    fResult
  }

  // TODO: for now ignore shallow embedding
  override def joinHelper(inputEDB: Seq[EDB], k: JoinIndexes): IndexedCollectionsEDB = ???
  override def projectHelper(input: EDB, k: JoinIndexes): IndexedCollectionsEDB = ???
  override def joinProjectHelper(inputsEDB: Seq[EDB], originalK: JoinIndexes, onlineSort: Boolean): IndexedCollectionsEDB = ???
  override def SPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): IndexedCollectionsEDB = ???
  override def naiveSPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): IndexedCollectionsEDB = ???

  // Printer methods
  override def toString() = {
    def printIndexes(db: IndexedCollectionsDatabase): String = {
      db.toSeq.map((rId, idxC) => IndexedCollectionsEDB.indexSizeToString(ns(rId), idxC)/* + s"(arity=${idxC.arity})"*/).mkString("$indexes: [", ", ", "]")
    }
    def printHelperRelation(i: Int, db: IndexedCollectionsDatabase): String = {
      val indexes = printIndexes(db)
      val name = if (i == knownDbId) "known" else if (i == newDbId) "new" else s"!!!OTHER($i)"
      s"\n $name : \n  $indexes ${printer.edbToString(db)}"
    }

    "+++++\n" +
      "EDB:\n  " + printIndexes(edbs) + "  " + printer.edbToString(edbs) +
      "\nDISCOV:\n  " + printIndexes(discoveredFacts) + "  " + printer.edbToString(discoveredFacts) +
      "\nDERIVED:" + derivedDB.map(printHelperRelation).mkString("[", ", ", "]") +
      "\nDELTA:" + deltaDB.map(printHelperRelation).mkString("[", ", ", "]") +
      "\n+++++"
  }
}