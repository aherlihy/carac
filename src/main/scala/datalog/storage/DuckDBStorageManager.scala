package datalog.storage

import datalog.dsl.{Constant, StorageAtom, Term, Variable}
import datalog.execution.{AllIndexes, JoinIndexes}
import datalog.storage.StorageTerm
import datalog.tools.Debug.debug

import scala.collection.immutable.ArraySeq
import scala.collection.mutable.ArrayBuffer
import scala.collection.{Iterator, immutable, mutable}

/**
 * Collections-based storage manager, index or no index.
 */
class DuckDBStorageManager(ns: NS = new NS()) extends StorageManager(ns) {
  // "database", i.e. relationID => Relation
  protected val edbs: CollectionsDatabase = CollectionsDatabase(None, empty) // raw user-supplied EDBs from initialization.
  val edbDomain: mutable.Set[StorageTerm] = mutable.Set.empty // incrementally grow the total domain of all EDBs, used for calculating complement of negated predicates

  var knownDbId: KnowledgeId = -1
  var newDbId: KnowledgeId = -1

  var dbId = 0
  protected val derivedDB: CollectionsDatabase = CollectionsDatabase(None, empty)
  protected val deltaDB: Array[CollectionsDatabase] = new Array[CollectionsDatabase](2)

  val allRulesAllIndexes: mutable.Map[RelationId, AllIndexes] = mutable.Map.empty // Index => position
  val indexCandidates: mutable.Map[RelationId, mutable.BitSet] = mutable.Map[RelationId, mutable.BitSet]() // relative position of atoms with constant or variable locations
  val relationArity: mutable.Map[RelationId, Int] = mutable.Map[RelationId, Int]()

  // Update metadata if aliases are discovered
  def updateAliases(aliases: mutable.Map[RelationId, RelationId]): Unit = {
    aliases.foreach((k, v) =>
      if (relationArity.contains(k) && relationArity.contains(v) && relationArity(k) != relationArity(v))
        throw new Exception(s"Error: registering relations ${ns(k)} and ${ns(v)} as aliases but have different arity (${relationArity(k)} vs. ${relationArity(v)})")
      relationArity.getOrElseUpdate(k, relationArity.getOrElse(v, throw new Exception(s"No arity available for either ${ns(k)} or ${ns(v)}")))
      indexCandidates(k) = indexCandidates.getOrElseUpdate(k, mutable.BitSet()).addAll(indexCandidates.getOrElse(v, mutable.BitSet()))
      indexCandidates(v) = mutable.BitSet().addAll(indexCandidates(k))
    )
  }

  // Store relative positions of shared variables as candidates for potential indexes
  def registerIndexCandidates(cands: mutable.Map[RelationId, mutable.BitSet]): Unit = {
    cands.foreach((rId, idxs) =>
      // adds indexes to any the EDBs
      if edbs.contains(rId) then edbs(rId).bulkRegisterIndex(idxs)
      // tells intermediate relations to build indexes
      indexCandidates.getOrElseUpdate(rId, mutable.BitSet()).addAll(idxs)
    )
  }
  // Store relation arity. In the future can require it to be declared, but for now derived.
  def registerRelationArity(rId: RelationId, arity: Int): Unit =
    if relationArity.contains(rId) then if arity != relationArity(rId) then throw new Exception(s"Derived relation $rId (${ns(rId)}) declared with arity $arity but previously declared with arity ${relationArity(rId)}")
    relationArity(rId) = arity

  val printer: Printer[this.type] = Printer[this.type](this)

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
    dbId += 1
    newDbId = dbId

    derivedDB.clear()
    deltaDB(knownDbId) = CollectionsDatabase(None, empty)
    deltaDB(newDbId) = CollectionsDatabase(None, empty)

    edbs.foreach((rId, relation) => {
      // All relations get at least 1 index
      if indexCandidates(rId).isEmpty && relation.arity > 0 then
        relation.registerIndex(0)
        indexCandidates(rId) = mutable.BitSet(0)
      else
        relation.bulkRegisterIndex(indexCandidates(rId)) // build indexes on EDBs

      deltaDB(knownDbId).addEmpty(rId, relation.arity, indexCandidates(rId), ns(rId), mutable.BitSet())
      deltaDB(newDbId).addEmpty(rId, relation.arity, indexCandidates(rId), ns(rId), mutable.BitSet())

      derivedDB.addNewEDBCopy(rId, relation)
    })
    dbId += 1
  }

  // Read & Write EDBs
  override def insertEDB(rule: StorageAtom): Unit = {
    if (edbs.contains(rule.rId))
      if (rule.terms.length != relationArity(rule.rId)) throw new Exception(s"Inserted arity not equal to expected arity for relation #${rule.rId} ${ns(rule.rId)}")
      edbs(rule.rId).addOne(CollectionsRow(rule.terms))
    else
      relationArity(rule.rId) = rule.terms.length
      indexCandidates.getOrElseUpdate(rule.rId, mutable.BitSet())
      edbs.addEmpty(rule.rId, rule.terms.length, indexCandidates(rule.rId), ns(rule.rId), mutable.BitSet())
      edbs(rule.rId).addOne(CollectionsRow(rule.terms))
    edbDomain.addAll(rule.terms)
  }
  // Only used when querying for a completely empty EDB that hasn't been declared/added yet.
  def getEmptyEDB(rId: RelationId): GeneralCollectionsEDB =
    if (!relationArity.contains(rId))
      throw new Exception(s"Getting empty relation $rId (${ns(rId)}) but undefined")
    empty(relationArity(rId), ns(rId), indexCandidates.getOrElse(rId, mutable.BitSet()), mutable.BitSet())
  def getEDB(rId: RelationId): GeneralCollectionsEDB = edbs(rId)
  def edbContains(rId: RelationId): Boolean = edbs.contains(rId)
  def getAllEDBS(): mutable.Map[RelationId, Any] = edbs.wrapped.asInstanceOf[mutable.Map[RelationId, Any]]

  // Read intermediate results
  def getKnownDerivedDB(rId: RelationId): GeneralCollectionsEDB =
    if !relationArity.contains(rId) then throw new Exception(s"Internal error: relation $rId (${ns(rId)}) has no arity")
    derivedDB.getOrElseEmpty(rId, relationArity(rId), indexCandidates.getOrElseUpdate(rId, mutable.BitSet(0)), ns(rId), mutable.BitSet())

  def getKnownDeltaDB(rId: RelationId): GeneralCollectionsEDB =
    if !relationArity.contains(rId) then throw new Exception(s"Internal error: relation $rId (${ns(rId)}) has no arity")
    deltaDB(knownDbId).getOrElseEmpty(rId, relationArity(rId), indexCandidates.getOrElseUpdate(rId, mutable.BitSet(0)), ns(rId), mutable.BitSet())
  def getNewDeltaDB(rId: RelationId): GeneralCollectionsEDB =
    if !relationArity.contains(rId) then throw new Exception(s"Internal error: relation $rId (${ns(rId)}) has no arity")
    deltaDB(newDbId).getOrElseEmpty(rId, relationArity(rId), indexCandidates.getOrElseUpdate(rId, mutable.BitSet(0)), ns(rId), mutable.BitSet())

  // Read final results
  def getKnownIDBResult(rId: RelationId): Set[Seq[StorageTerm]] =
    debug("Final IDB Result[known]: ", () => s"at iteration $iteration: @$knownDbId, count=${getKnownDerivedDB(rId).length}")
    getKnownDerivedDB(rId).getSetOfSeq
  def getNewIDBResult(rId: RelationId): Set[Seq[StorageTerm]] =
    derivedDB.getOrElseEmpty(rId, relationArity.getOrElse(rId, 0), mutable.BitSet(), ns(rId), mutable.BitSet()).getSetOfSeq
  def getEDBResult(rId: RelationId): Set[Seq[StorageTerm]] =
    edbs.getOrElseEmpty(rId, relationArity.getOrElse(rId, 0), mutable.BitSet(), ns(rId), mutable.BitSet()).getSetOfSeq

  def insertDeltaIntoDerived(): Unit =
    deltaDB(newDbId).foreach((rId, edb) =>
      if (derivedDB.contains(rId))
        derivedDB(rId).mergeEDBs(edb)
      else if (edb.nonEmpty)
        derivedDB.addNewEDBCopy(rId, edb)
    )

  def setNewDelta(rId: RelationId, rules: EDB): Unit =
    val toAdd = if indexed then IndexedCollectionsCasts.asIndexedCollectionsEDB(rules) else CollectionsCasts.asCollectionsEDB(rules)
    deltaDB(newDbId).addNewEDBCopy(rId, toAdd)

  def clearNewDeltas(): Unit =
    deltaDB(newDbId).clear()

  // Compare & Swap
  def swapKnowledge(): Unit = {
    iteration += 1
    val t = knownDbId
    knownDbId = newDbId
    newDbId = t
    //    deltaDB(knownDbId).foreach((rId, edb) =>
    //      edb.bulkRebuildIndex()
    //    )
  }
  def compareNewDeltaDBs(): Boolean =
    deltaDB(newDbId).exists((k, v) => v.nonEmpty)

  def verifyEDBs(idbList: mutable.Set[RelationId]): Unit = {
    ns.rIds().foreach(rId =>
      if (!edbs.contains(rId) && !idbList.contains(rId))
        // NOTE: no longer treat undefined relations as empty edbs, instead error
        if (!relationArity.contains(rId))
          throw new Exception(s"Error: using EDB $rId (${ns(rId)}) but no known arity")
        edbs.addEmpty(rId,
          relationArity(rId),
          indexCandidates.getOrElseUpdate(rId, mutable.BitSet()),
          ns(rId),
          mutable.BitSet()
        )
    )
  }

  def union(edbs: Seq[EDB]): EDB =
    import CollectionsEDB.unionEDB as collectionsUnion
    import IndexedCollectionsEDB.unionEDB as indexedUnion
    if indexed then edbs.indexedUnion else edbs.collectionsUnion

  override def selectProjectJoinHelper(inputsEDB: Seq[EDB], rId: Int, hash: String, onlineSort: Boolean): GeneralCollectionsEDB = {
    if onlineSort then throw new Exception("Unimplemented: online sort with indexes")

    val originalK = allRulesAllIndexes(rId)(hash)
    val inputs = if indexed then IndexedCollectionsCasts.asIndexedCollectionsSeqEDB(inputsEDB) else CollectionsCasts.asCollectionsSeqEDB(inputsEDB)
    //    println(s"Rule: ${printer.ruleToString(originalK.atoms)}")
    //    println(s"input rels: ${inputs.map(e => e.factToString).mkString("[", "*", "]")}")

    var intermediateCardinalities = Seq[String]()
    val fResult = if (inputs.length == 1) { // need to make a copy
      if (originalK.constIndexes.isEmpty && originalK.projIndexes.isEmpty && !derivedDB.contains(rId)) // nothing to do but copy, save rebuilding index time. TODO: never happens bc project always defined?
        val edbToCopy = inputs.head
        //        val newEDB = CollectionsEDB.empty(edbToCopy.arity, edbToCopy.indexKeys, edbToCopy.name, mutable.BitSet())
        //        newEDB.mergeEDBs(edbToCopy.indexes)
        //        newEDB
        edbToCopy
      else
        val res = inputs.head.projectAndDiff(
          originalK.constIndexes,
          originalK.projIndexes,
          ns(rId),
          indexCandidates.getOrElseUpdate(rId, mutable.BitSet(0)),
          derivedDB,
          rId
        )
        res
    } else { // no copy needed
      val result = inputs
        .foldLeft(
          (empty(0, "ANON", mutable.BitSet(), mutable.BitSet()), 0, originalK) // initialize intermediate indexed-collection
        )((combo: (GeneralCollectionsEDB, Int, JoinIndexes), innerT: GeneralCollectionsEDB) =>
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
            val edbResult = outerT.joinFilter(k, atomI, innerT)

            intermediateCardinalities = intermediateCardinalities :+ s"\t${outerT.name}(${outerT.length})*${innerT.name}(${innerT.length})= ${edbResult.length}"
            (edbResult, atomI + 1, k)
        )
      val outputIndex = indexCandidates.getOrElseUpdate(rId, mutable.BitSet(0))

      val res = result._1.projectAndDiff(
        mutable.Map[Int, Constant](), // already did constant check
        result._3.projIndexes,
        ns(rId),
        outputIndex,
        derivedDB,
        rId
      )
      res
    }
    fResult
  }

  // Printer methods
  override def toString() = {
    def printIndexes(db: CollectionsDatabase): String = {
      if (indexed)
        db.toSeq.map((rId, idxC) => IndexedCollectionsEDB.allIndexesToString(idxC)/* + s"(arity=${idxC.arity})"*/).mkString("{$indexes: [\n  ",",\n  ", "\n  ]}\n")
      else
        ""
    }
    def printHelperRelation(db: CollectionsDatabase, i: Int): String = {
      val indexes = printIndexes(db)
      val name = if (i == knownDbId) "known" else if (i == newDbId) "new" else s"!!!OTHER($i)"
      s"\n $name : \n  $indexes ${printer.edbToString(db)}"
    }

    "+++++\n" +
      "EDB:\n  " + printIndexes(edbs) + "  " + printer.edbToString(edbs) +
      "\nDERIVED:" + printIndexes(derivedDB) + "  " + printer.edbToString(derivedDB) +
      "\nDELTA:" + deltaDB.zipWithIndex.map(printHelperRelation).mkString("[", ", ", "]") +
      "\n+++++"
  }

  /**
   * Compute Dom * Dom * ... arity # times
   */
  override def getComplement(rId: RelationId, arity: Int): GeneralCollectionsEDB = {
    // short but inefficient
    val res = List.fill(arity)(edbDomain).flatten.combinations(arity).flatMap(_.permutations).toSeq
    empty(arity, ns(rId), indexCandidates(rId), mutable.BitSet()).addAll(
      mutable.ArrayBuffer.from(res.map(r => CollectionsRow(ArraySeq.from(r))))
    )
  }
  override def addConstantsToDomain(constants: Seq[StorageTerm]): Unit = {
    edbDomain.addAll(constants)
  }
  // Only used with negation, otherwise merged with project.
  override def diff(lhsEDB: EDB, rhsEDB: EDB): EDB =
    val lhs = if indexed then IndexedCollectionsCasts.asIndexedCollectionsEDB(lhsEDB) else CollectionsCasts.asCollectionsEDB(lhsEDB)
    lhs.diff(rhsEDB)
}
