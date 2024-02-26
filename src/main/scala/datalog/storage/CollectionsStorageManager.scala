package datalog.storage

import datalog.dsl.{Constant, StorageAtom, Term, Variable}
import datalog.execution.{AllIndexes, JoinIndexes}
import datalog.storage.CollectionsCasts.*
import datalog.storage.StorageTerm
import datalog.tools.Debug.debug

import scala.collection.immutable.ArraySeq
import scala.collection.mutable.ArrayBuffer
import scala.collection.{Iterator, immutable, mutable}

class CollectionsStorageManager(ns: NS = new NS()) extends StorageManager(ns) {
  // "database", i.e. relationID => Relation
  protected val edbs: CollectionsDatabase = CollectionsDatabase() // raw user-supplied EDBs from initialization.
  val edbDomain: mutable.Set[StorageTerm] = mutable.Set.empty // incrementally grow the total domain of all EDBs, used for calculating complement of negated predicates
  //  protected val discoveredFacts: CollectionsDatabase = CollectionsDatabase() // all EDBs + facts discovered in previous strata
  var knownDbId: KnowledgeId = -1
  var newDbId: KnowledgeId = -1

  // dbID => database, because we swap between read (known) and write (new) within iterations
  var dbId = 0
  protected val derivedDB: CollectionsDatabase = CollectionsDatabase()
  protected val deltaDB: Array[CollectionsDatabase] = new Array[CollectionsDatabase](2)

  val allRulesAllIndexes: mutable.Map[RelationId, AllIndexes] = mutable.Map.empty // Index => position

  def updateAliases(aliases: mutable.Map[RelationId, RelationId]): Unit = ()
  def registerIndexCandidates(cands: mutable.Map[RelationId, mutable.BitSet]): Unit = ()
  def registerRelationArity(rId: RelationId, arity: Int): Unit = ()

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
    dbId += 1
    newDbId = dbId

    derivedDB.clear()
    deltaDB(knownDbId) = CollectionsDatabase()
    deltaDB(newDbId) = CollectionsDatabase()

    edbs.foreach((rId, relation) => {
      deltaDB(knownDbId).addEmpty(rId, 0, mutable.BitSet(), ns(rId), mutable.BitSet())
      deltaDB(newDbId).addEmpty(rId, 0, mutable.BitSet(), ns(rId), mutable.BitSet()) // TODO: no indexes on delta.new, bc write into?

      derivedDB.addNewEDBCopy(rId, relation)
    })
    dbId += 1
  }

  // Read & Write EDBs
  override def insertEDB(rule: StorageAtom): Unit = {
    if (edbs.contains(rule.rId))
      edbs(rule.rId).addOne(CollectionsRow(rule.terms))
    else
      edbs.addEmpty(rule.rId, rule.terms.length, mutable.BitSet(), ns(rule.rId), mutable.BitSet())
      edbs(rule.rId).addOne(CollectionsRow(rule.terms))
    // edbDomain.addAll(rule.terms)
  }
  /* Call when adding an IDB rule so domain can grow incrementally */
  override def addConstantsToDomain(constants: Seq[StorageTerm]): Unit = {
    // edbDomain.addAll(constants)
  }
  // Only used when querying for a completely empty EDB that hasn't been declared/added yet.
  def getEmptyEDB(rId: RelationId): CollectionsEDB =
    CollectionsEDB.empty(0,mutable.BitSet(), ns(rId), mutable.BitSet())
  def getEDB(rId: RelationId): CollectionsEDB = edbs(rId)
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
  //    edbs.foreach((_, rows) => // avoid map or flatMap for CollectionsDatabase, CollectionRow
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
  override def getComplement(rId: RelationId, arity: Int): CollectionsEDB = {
    // short but inefficient
    val res = List.fill(arity)(edbDomain).flatten.combinations(arity).flatMap(_.permutations).toSeq
    CollectionsEDB(
      res.map(r => CollectionsRow(r.toSeq)):_*
    )
  }

  // Read intermediate results
  def getKnownDerivedDB(rId: RelationId): CollectionsEDB =
    derivedDB.getOrElseEmpty(rId, 0,mutable.BitSet(), ns(rId), mutable.BitSet())
  def getNewDerivedDB(rId: RelationId): CollectionsEDB = ??? // TODO: remove

  def getKnownDeltaDB(rId: RelationId): CollectionsEDB =
    deltaDB(knownDbId).getOrElseEmpty(rId, 0,mutable.BitSet(), ns(rId), mutable.BitSet())
  def getNewDeltaDB(rId: RelationId): CollectionsEDB =
    deltaDB(newDbId).getOrElseEmpty(rId, 0,mutable.BitSet(), ns(rId), mutable.BitSet())

  // Read final results
  def getKnownIDBResult(rId: RelationId): Set[Seq[StorageTerm]] =
    debug("Final IDB Result[known]: ", () => s"at iteration $iteration: @$knownDbId, count=${getKnownDerivedDB(rId).length}")
    getKnownDerivedDB(rId).getSetOfSeq
  def getNewIDBResult(rId: RelationId): Set[Seq[StorageTerm]] =
    derivedDB.getOrElseEmpty(rId, 0, mutable.BitSet(), ns(rId), mutable.BitSet()).getSetOfSeq
  def getEDBResult(rId: RelationId): Set[Seq[StorageTerm]] =
    edbs.getOrElseEmpty(rId, 0, mutable.BitSet(), ns(rId), mutable.BitSet()).getSetOfSeq

  def insertDeltaIntoDerived(): Unit =
    deltaDB(newDbId).foreach((rId, edb) =>
      if (derivedDB.contains(rId))
        derivedDB(rId).wrapped.addAll(edb.wrapped)
      else if (edb.wrapped.nonEmpty)
        derivedDB.addNewEDBCopy(rId, edb)
    )

  def setNewDelta(rId: RelationId, rules: EDB): Unit =
    deltaDB(newDbId).addNewEDBCopy(rId, asCollectionsEDB(rules))

  def clearNewDeltas(): Unit =
    deltaDB(newDbId).clear()

  // Write intermediate results
  def setKnownDerived(rId: RelationId, rules: EDB): Unit = ???

  def resetKnownDerived(rId: RelationId, rulesEDB: EDB, prevEDB: EDB): Unit = ???

  def setKnownDelta(rId: RelationId, rules: EDB): Unit = ???

  def setNewDerived(rId: RelationId, rules: EDB): Unit = ???

  def resetNewDerived(rId: RelationId, rulesEDB: EDB, prevEDB: EDB): Unit = ???

  def clearNewDerived(): Unit = ???
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
  def compareDerivedDBs(): Boolean = ???
  //    derivedDB(knownDbId) == derivedDB(newDbId)

  /**
   * Copy all the derived facts at the end of the current strata into discoveredFacts
   * to be fed into the next strata as EDBs
   */
  def updateDiscovered(): Unit = ???
  //    derivedDB.foreach((relation, facts) =>
  //      discoveredFacts(relation) = facts
  //    )

  def verifyEDBs(idbList: mutable.Set[RelationId]): Unit = {
    ns.rIds().foreach(rId =>
      if (!edbs.contains(rId) && !idbList.contains(rId))
        // NOTE: no longer treat undefined relations as empty edbs, instead error
        edbs.addEmpty(rId,
          0,
          mutable.BitSet(),
          ns(rId),
          mutable.BitSet()
        )
    )
  }

  def union(edbs: Seq[EDB]): EDB =
    import CollectionsEDB.{unionEDB}
    edbs.unionEDB // unionInPlace is slower!

  def diff(lhsEDB: EDB, rhsEDB: EDB): EDB = ???

  override def joinProjectHelper_withHash(inputsEDB: Seq[EDB], rId: Int, hash: String, onlineSort: Boolean): CollectionsEDB = {
    if onlineSort then throw new Exception("Unimplemented: online sort with indexes")

    val originalK = allRulesAllIndexes(rId)(hash)
    val inputs = asCollectionsSeqEDB(inputsEDB)
//         println(s"Rule: ${printer.ruleToString(originalK.atoms)}")
//         println(s"input rels: ${inputs.map(e => e.factToString).mkString("[", "*", "]")}")

    // var intermediateCardinalities = Seq[Int]()
    val fResult = if (inputs.length == 1) { // need to make a copy
      if (originalK.constIndexes.isEmpty && originalK.projIndexes.isEmpty && !derivedDB.contains(rId)) // nothing to do but copy, save rebuilding index time. TODO: never happens bc project always defined?
        val edbToCopy = inputs.head
        //        val newEDB = CollectionsEDB.empty(edbToCopy.arity, edbToCopy.indexKeys, edbToCopy.name, mutable.BitSet())
        //        newEDB.mergeEDBs(edbToCopy.indexes)
        //        newEDB
        edbToCopy
      else
        inputs.head.projectAndDiff(
          originalK.constIndexes,
          originalK.projIndexes,
          ns(rId),
         mutable.BitSet(),
          derivedDB: CollectionsDatabase,
          rId
        )
    } else { // no copy needed
      val result = inputs
        .foldLeft(
          (CollectionsEDB.empty(0, skipIndexes = mutable.BitSet()), 0, originalK) // initialize intermediate -collection
        )((combo: (CollectionsEDB, Int, JoinIndexes), innerT: CollectionsEDB) =>
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
      val outputIndex =mutable.BitSet()

      result._1.projectAndDiff(
        mutable.Map[Int, Constant](), // already did constant check
        result._3.projIndexes,
        ns(rId),
        outputIndex,
        derivedDB,
        rId
      )
    }
    fResult
  }

  // TODO: for now ignore shallow embedding
  override def joinHelper(inputEDB: Seq[EDB], k: JoinIndexes): CollectionsEDB = ???
  override def projectHelper(input: EDB, k: JoinIndexes): CollectionsEDB = ???
  override def joinProjectHelper(inputsEDB: Seq[EDB], originalK: JoinIndexes, onlineSort: Boolean): CollectionsEDB = ???
  override def SPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): CollectionsEDB = ???
  override def naiveSPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): CollectionsEDB = ???

  // Printer methods
  override def toString() = {
    def printIndexes(db: CollectionsDatabase): String = {
      ""
//      db.toSeq.map((rId, idxC) => CollectionsEDB.allIndexesToString(idxC)/* + s"(arity=${idxC.arity})"*/).mkString("$indexes: [\n  ",",\n", "]")
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
}
