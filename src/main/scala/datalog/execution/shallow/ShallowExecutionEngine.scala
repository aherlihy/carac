package datalog.execution

import datalog.dsl.{Atom, Constant, StorageAtom, Term, Variable}
import datalog.execution.ast.ProgramNode
import datalog.execution.ir.SwapAndClearOp
import datalog.storage.{EDB, RelationId, StorageManager, StorageTerm}
import datalog.tools.Debug.debug

import scala.collection.mutable

/**
 * Shallow embedding version of the execution engine, i.e. no AST. Used mostly to compare results and step through
 * since much easier to debug than the staged versions. This version also follows the naming convention of "Datalog and Logic Databases".
 *
 * @param storageManager
 */
class ShallowExecutionEngine(override val storageManager: StorageManager,
                             stratified: Boolean = true) extends NaiveShallowExecutionEngine(storageManager, stratified) {

  def evalRuleSN(rId: RelationId): EDB = {
    val ruleDefs = keyHashs(rId)
    val subqueries = ruleDefs.map(kHash => // for each idb rule
      val k = storageManager.allRulesAllIndexes(rId)(kHash)
      if (k.edb)
        // ScanEDBOp(k.rId).run(storageManager)
        if (storageManager.edbContains(rId))
          storageManager.getEDB(rId)
        else
          storageManager.getEmptyEDB(rId)
      else
        var idx = -1
        val subsubqueries = k.deps.map((*, d) => {
          var found = false
          storageManager.selectProjectJoinHelper(
            k.deps.zipWithIndex.map((md, i) => {
              val (typ, r) = md
              val q = if (r == d && !found && i > idx)
                found = true
                idx = i
                if (typ != PredicateType.NEGATED) // if negated then we want the complement of all facts not just the delta
                  storageManager.getKnownDeltaDB(r)
                else
                  storageManager.getKnownDerivedDB(r)
              else
                storageManager.getKnownDerivedDB(r)
              typ match
                case PredicateType.NEGATED =>
                  val arity = k.atoms(i + 1).terms.length
                  val res = storageManager.diff(storageManager.getComplement(k.atoms(i+1).rId, arity), q)
                  debug(s"found negated relation, rule=", () => s"${storageManager.printer.ruleToString(k.atoms)}\n\tarity=$arity")
                  res
                case _ => q
            }), rId, kHash, false)
        })
        storageManager.union(subsubqueries)
    ).toSeq
    storageManager.union(
      subqueries
    )
  }

  def evalSN(rId: RelationId, relations: Seq[RelationId]): Unit = {
    debug("evalSN for ", () => storageManager.ns(rId))
    relations.foreach(r => {
      val res = evalRuleSN(r)
      debug("\tevalRuleSN=", () => storageManager.printer.factToString(res))
      storageManager.setNewDelta(r, res)
//      System.gc()
//      System.gc()
//      val mb = 1024 * 1024
//      val runtime = Runtime.getRuntime
//      println(s"after SPJU for relation ${storageManager.ns(r)}, query=${storageManager.printer.snPlanToString(getOperatorKeys(rId)JoinIndexes])} results in MB")
//      println("** Used Memory:  " + (runtime.totalMemory - runtime.freeMemory) / mb)
//      println("** Free Memory:  " + runtime.freeMemory / mb)
//      println("** Total Memory: " + runtime.totalMemory / mb)
//      println("** Given memory:   " + runtime.maxMemory / mb)
    })
    storageManager.insertDeltaIntoDerived()
  }

  override def innerSolve(rId: RelationId, relations: Seq[Int]): Unit = {
    var count = 0
    debug("initial state @ -1", storageManager.toString)
    evalNaive(relations)
    var setDiff = true

    while (setDiff) {
      storageManager.swapKnowledge()
      storageManager.clearNewDeltas()

      debug(s"initial state @ $count", storageManager.toString)
      count += 1
      evalSN(rId, relations)
      setDiff = storageManager.compareNewDeltaDBs()
    }
    debug(s"final state @$count", storageManager.toString)
  }
}

class NaiveShallowExecutionEngine(val storageManager: StorageManager, stratified: Boolean = true) extends ExecutionEngine {
  //  println(s"stratified=$stratified")
  val precedenceGraph = new PrecedenceGraph(using storageManager.ns)

  // Internal representation of the IDB rules.
  val idbs: mutable.Map[RelationId, mutable.ArrayBuffer[IndexedSeq[Atom]]] = mutable.Map()
  val keyHashs: mutable.Map[RelationId, mutable.ArrayBuffer[String]] = mutable.Map()

  def initRelation(rId: RelationId, name: String): Unit = {
    storageManager.ns(rId) = name
    storageManager.initRelation(rId, name)
  }

  def get(rId: RelationId): Set[Seq[StorageTerm]] = {
    if (storageManager.knownDbId == -1)
      throw new Exception("Solve() has not yet been called")
    val edbs = storageManager.getEDBResult(rId)
    if (idbs.contains(rId))
      edbs ++ storageManager.getKnownIDBResult(rId)
    else
      edbs
  }
  def get(name: String): Set[Seq[StorageTerm]] = {
    get(storageManager.ns(name))
  }

  def insertIDB(rId: RelationId, rule: Seq[Atom]): Unit = {
    precedenceGraph.addNode(rule)

    val k = JoinIndexes(rule, None)
    keyHashs.getOrElseUpdate(rId, mutable.ArrayBuffer[String]()).append(k.hash)
    storageManager.allRulesAllIndexes.getOrElseUpdate(rId, mutable.Map[String, JoinIndexes]()).addOne(k.hash, k)

    val allLocs = k.varIndexes.flatten ++ k.constIndexes.keys
    if (allLocs.nonEmpty) {
      val relationCands = mutable.Map[RelationId, mutable.BitSet]()
      var start = 0
      for atom <- rule.drop(1) do {
        for i <- atom.terms.indices do {
          if (allLocs.contains(start + i))
            relationCands.getOrElseUpdate(atom.rId, mutable.BitSet()).addOne(i)
        }
        start += atom.terms.size
      }
      if relationCands.nonEmpty then storageManager.registerIndexCandidates(relationCands) // add at once to deduplicate ahead of time and avoid repeated calls
    }
    rule.foreach(r => storageManager.registerRelationSchema(r.rId, r.terms))

//    if (rule.length <= heuristics.max_length_cache)
//      val allK = JoinIndexes.allOrders(rule)
//      storageManager.allRulesAllIndexes(rId) ++= allK

    idbs.getOrElseUpdate(rId, mutable.ArrayBuffer[IndexedSeq[Atom]]()).addOne(rule.toIndexedSeq)
    storageManager.addConstantsToDomain(k.constIndexes.values.toSeq)
  }

  def insertEDB(rule: StorageAtom): Unit = {
    storageManager.insertEDB(rule)
  }

  def evalRuleNaive(rId: RelationId):  EDB = {
    val ruleDefs = keyHashs(rId)
    val subqueries = ruleDefs.map(kHash => // for each idb rule
      val k = storageManager.allRulesAllIndexes(rId)(kHash)
      if (k.edb)
        // ScanEDBOp(k.rId).run(storageManager)
        if (storageManager.edbContains(rId))
          storageManager.getEDB(rId)
        else
          storageManager.getEmptyEDB(rId)
      else
        storageManager.selectProjectJoinHelper(
          k.deps.zipWithIndex.map((md, i) =>
            val (typ, r) = md
            val q = storageManager.getKnownDerivedDB(r)
            typ match
              case PredicateType.NEGATED =>
                val arity = k.atoms(i + 1).terms.length
                val res = storageManager.diff(storageManager.getComplement(k.atoms(i+1).rId, arity), q)
                debug(s"found negated relation, rule=", () => s"${storageManager.printer.ruleToString(k.atoms)}\n\tarity=$arity")
                res
              case _ => q
          ),
          rId,
          kHash,
          false
        )
    ).toSeq
    storageManager.union(
      subqueries
    )
  }

  /**
   * Take the union of each evalRule for each IDB predicate
   */
  def evalNaive(relations: Seq[RelationId], copyToDelta: Boolean = false): Unit = {
    debug("in eval: ", () => s"relations=${relations.map(r => storageManager.ns(r)).mkString("[", ", ", "]")}")
    relations.foreach(r => {
      val res = evalRuleNaive(r)
      debug("result of evalRule=", () => storageManager.printer.factToString(res))
      storageManager.setNewDelta(r, res)
      storageManager.insertDeltaIntoDerived()
    })
  }

  def innerSolve(rId: RelationId, relations: Seq[Int]): Unit = {
    var count = 0
    var setDiff = true
    while (setDiff) {
//      SwapAndClearOp(storageManager).run()
      storageManager.swapKnowledge()
      storageManager.clearNewDeltas()

      debug(s"initial state @ $count", storageManager.toString)
      count += 1
      evalNaive(relations)

      setDiff = storageManager.compareNewDeltaDBs()
    }
  }

  def solve(toSolve: RelationId): Set[Seq[StorageTerm]] = {
    storageManager.verifyEDBs(precedenceGraph.idbs)
    if (storageManager.edbContains(toSolve) && !idbs.contains(toSolve)) { // if just an edb predicate then return
      return storageManager.getEDBResult(toSolve)
    }
    if (!precedenceGraph.idbs.contains(toSolve)) {
      throw new Exception("Solving for rule without body")
    }
    val strata = precedenceGraph.scc(toSolve)
    storageManager.initEvaluation() // facts discovered in the previous iteration

    debug(s"solving relation: ${storageManager.ns(toSolve)} order of strata=", () => strata.map(r => r.map(storageManager.ns.apply).mkString("(", ", ", ")")).mkString("{", ", ", "}"))

    if (strata.length <= 1 || !stratified)
      innerSolve(toSolve, strata.flatten)
    else
      // for each strata
      strata.zipWithIndex.foreach((relations, idx) =>
        debug(s"**STRATA@$idx, rels=", () => relations.map(storageManager.ns.apply).mkString("(", ", ", ")"))
        innerSolve(toSolve, relations.toSeq)
      )
    storageManager.getKnownIDBResult(toSolve)
  }
}


