package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.storage.{RelationId, StorageManager, EDB}
import datalog.tools.Debug.debug

import scala.collection.mutable

/**
 * Shallow embedding version of the execution engine, i.e. no AST. Used mostly to compare results and step through
 * since much easier to debug than the staged versions.
 *
 * @param storageManager
 */
class SemiNaiveExecutionEngine(override val storageManager: StorageManager, stratified: Boolean = true /* TODO: temp remove */) extends NaiveExecutionEngine(storageManager) {
//  println(s"stratified=$stratified")
  def evalRuleSN(rId: RelationId): EDB = {
    storageManager.SPJU(rId, getOperatorKeys(rId))
  }

  def evalSN(rId: RelationId, relations: Seq[RelationId]): Unit = {
    debug("evalSN for ", () => storageManager.ns(rId))
    relations.foreach(r => {
      val prev = storageManager.getKnownDerivedDB(r)
      debug(s"\tderived[known][${storageManager.ns(r)}] =", () => storageManager.printer.factToString(prev))
      val res = evalRuleSN(r)
      debug("\tevalRuleSN=", () => storageManager.printer.factToString(res))
      val diff = storageManager.diff(res, prev)
      storageManager.resetNewDelta(r, diff)
      storageManager.resetNewDerived(r, storageManager.getKnownDerivedDB(r), storageManager.getNewDeltaDB(r)) // set derived[new] to derived[known]+delta[new]
      debug(s"\tdiff, i.e. delta[new][${storageManager.ns(r)}] =", () => storageManager.printer.factToString(storageManager.getNewDeltaDB(r)))
      debug(s"\tall, i.e. derived[new][${storageManager.ns(r)}] =", () => storageManager.printer.factToString(storageManager.getNewDerivedDB(r)))
      /* storageManager.resetDelta(r, newDbId, storageManager.getDiff(
        evalRuleSN(r, newDbId, knownDbId),
        storageManager.getDerivedDB(r, knownDbId)
      ))
      storageManager.resetDerived(r, newDbId, storageManager.getDeltaDB(r, newDbId), storageManager.getDerivedDB(r, knownDbId)) // set derived[new] to derived[known]+delta[new] */
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
  }

  override def innerSolve(rId: RelationId, relations: Seq[Int]): Unit = {
    var count = 0
    debug("initial state @ -1", storageManager.toString)
    evalNaive(relations, true) // this fills derived[new] and and delta[new]

    var setDiff = true
    while (setDiff) {
      storageManager.swapKnowledge()
      storageManager.clearNewDerived()

      debug(s"initial state @ $count", storageManager.toString)
      count += 1
      evalSN(rId, relations)
      setDiff = storageManager.compareNewDeltaDBs()
    }
    debug(s"final state @$count", storageManager.toString)
  }

  override def solve(toSolve: RelationId): Set[Seq[Term]] = {
    storageManager.verifyEDBs(idbs.keys.to(mutable.Set))
    if (storageManager.edbContains(toSolve) && !idbs.contains(toSolve)) { // if just an edb predicate then return
      return storageManager.getEDBResult(toSolve)
    }
    if (!idbs.contains(toSolve)) {
      throw new Exception("Solving for rule without body")
    }
    // TODO: if a IDB predicate without vars, then solve all and test contains result?
    //    if (relations.isEmpty)
    //      return Set()
    val strata = precedenceGraph.scc(toSolve)
    storageManager.initEvaluation() // facts previously derived

    debug(s"solving relation: ${storageManager.ns(toSolve)} sCount=${strata.length} order of strata=", () => strata.map(r => r.map(storageManager.ns.apply).mkString("(", ", ", ")")).mkString("{", ", ", "}"))

    if(strata.length <= 1 || !stratified)
      innerSolve(toSolve, strata.flatten)
    else
      // for each stratum
      strata.zipWithIndex.foreach((relations, idx) =>
        debug(s"**STRATA@$idx, rels=", () => relations.map(storageManager.ns.apply).mkString("(", ", ", ")"))
        innerSolve(toSolve, relations.toSeq)
        if (idx < strata.length - 1)
          storageManager.updateDiscovered()
      )
    storageManager.getNewIDBResult(toSolve)
  }
}
