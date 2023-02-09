package datalog.execution

import datalog.dsl.{Atom, Constant, MODE, Term, Variable}
import datalog.storage.{RelationId, SimpleStorageManager, StorageManager}
import datalog.tools.Debug.debug

import scala.collection.mutable

class SemiNaiveExecutionEngine(override val storageManager: StorageManager) extends NaiveExecutionEngine(storageManager) {
  import storageManager.{EDB, Table}

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
      storageManager.resetNewDerived(r, diff, prev) // set derived[new] to derived[new]+delta[new]
      storageManager.resetNewDelta(r, diff)
      debug(s"\tdiff, i.e. delta[new][${storageManager.ns(r)}] =", () => storageManager.printer.factToString(storageManager.getNewDeltaDB(r)))
      debug(s"\tall, i.e. derived[new][${storageManager.ns(r)}] =", () => storageManager.printer.factToString(storageManager.getNewDerivedDB(r)))
      /* storageManager.resetDelta(r, newDbId, storageManager.getDiff(
        evalRuleSN(r, newDbId, knownDbId),
        storageManager.getDerivedDB(r, knownDbId)
      ))
      storageManager.resetDerived(r, newDbId, storageManager.getDeltaDB(r, newDbId), storageManager.getDerivedDB(r, knownDbId)) // set derived[new] to derived[known]+delta[new] */
      val mb = 1024 * 1024
      val runtime = Runtime.getRuntime
      println(s"after SPJU for relation ${storageManager.ns(r)}, query=${storageManager.printer.snPlanToString(getOperatorKeys(rId).asInstanceOf[this.storageManager.Table[JoinIndexes]])} results in MB")
      println("** Used Memory:  " + (runtime.totalMemory - runtime.freeMemory) / mb)
      println("** Free Memory:  " + runtime.freeMemory / mb)
      println("** Total Memory: " + runtime.totalMemory / mb)
      println("** Given memory:   " + runtime.maxMemory / mb)
    })
  }

  override def solve(rId: RelationId, mode: MODE): Set[Seq[Term]] = {
    storageManager.verifyEDBs(idbs.keys.to(mutable.Set))
    if (storageManager.edbs.contains(rId) && !idbs.contains(rId)) { // if just an edb predicate then return
      return storageManager.getEDBResult(rId)
    }
    if (!idbs.contains(rId)) {
      throw new Error("Solving for rule without body")
    }
    // TODO: if a IDB predicate without vars, then solve all and test contains result?
    //    if (relations.isEmpty)
    //      return Set()
    val relations = precedenceGraph.topSort()
    debug(s"precedence graph=", precedenceGraph.sortedString)
    debug(s"solving relation: ${storageManager.ns(rId)} order of relations=", relations.toString)
    storageManager.initEvaluation()
    var count = 0

    debug("initial state @ -1", storageManager.printer.toString)
    val startRId = relations.head
    relations.foreach(rel => {
      eval(rel, relations) // this fills derived[new]
      storageManager.resetNewDelta(rel, storageManager.getNewDerivedDB(rel)) // copy delta[new] = derived[new]
    })

    var setDiff = true
    while(setDiff) {
      storageManager.swapKnowledge()
      storageManager.clearNewDB(true)

      debug(s"initial state @ $count", storageManager.printer.toString)
      count += 1
      evalSN(rId, relations)
      setDiff = storageManager.compareNewDeltaDBs()
      val mb = 1024*1024
      val runtime = Runtime.getRuntime
      println(s"END ITERATION iteration $count, results in MB")
      println("** Used Memory:  " + (runtime.totalMemory - runtime.freeMemory) / mb)
      println("** Free Memory:  " + runtime.freeMemory / mb)
      println("** Total Memory: " + runtime.totalMemory / mb)
      println("** Max Memory:   " + runtime.maxMemory / mb)
    }
    debug(s"final state @$count", storageManager.printer.toString)
    storageManager.getNewIDBResult(rId)
  }
}
