package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.storage.{SimpleStorageManager, StorageManager}
import datalog.tools.Debug.debug

import scala.collection.mutable

class SemiNaiveExecutionEngine(override val storageManager: StorageManager) extends NaiveExecutionEngine(storageManager) {
  import storageManager.{EDB, Table}

  def evalRuleSN(rId: Int, newDbId: Int, knownDbId: Int): EDB = {
    storageManager.SPJU(rId, getOperatorKeys(rId), knownDbId)
  }

  def evalSN(rId: Int, relations: Seq[Int], newDbId: Int, knownDbId: Int): Unit = {
    debug("evalSN for ", () => storageManager.ns(rId))
    relations.foreach(r => {
      debug("\t=>iterating@", () => storageManager.ns(r))
      val prev = storageManager.getDerivedDB(r, knownDbId)
      debug(s"\tderived[known][${storageManager.ns(r)}] =", () => storageManager.printer.factToString(prev))
      val res = evalRuleSN(r, newDbId, knownDbId)
      debug("\tevalRuleSN=", () => storageManager.printer.factToString(res))
      val diff = storageManager.getDiff(res, prev)
      storageManager.resetDerived(r, newDbId, diff, prev) // set derived[new] to derived[new]+delta[new]
      storageManager.resetDelta(r, newDbId, diff)
      debug(s"\tdiff, i.e. delta[new][${storageManager.ns(r)}] =", () => storageManager.printer.factToString(storageManager.deltaDB(newDbId)(r)))
      debug(s"\tall, i.e. derived[new][${storageManager.ns(r)}] =", () => storageManager.printer.factToString(storageManager.derivedDB(newDbId)(r)))
      /* storageManager.resetDelta(r, newDbId, storageManager.getDiff(
        evalRuleSN(r, newDbId, knownDbId),
        storageManager.getDerivedDB(r, knownDbId)
      ))
      storageManager.resetDerived(r, newDbId, storageManager.getDeltaDB(r, newDbId), storageManager.getDerivedDB(r, knownDbId)) // set derived[new] to derived[known]+delta[new] */
    })
  }

  override def solve(rId: Int): Set[Seq[Term]] = {
    storageManager.verifyEDBs()
    if (storageManager.edbs.contains(rId) && !storageManager.idbs.contains(rId)) { // if just an edb predicate then return
      return storageManager.getEDBResult(rId)
    }
    if (!storageManager.idbs.contains(rId)) {
      throw new Error("Solving for rule without body")
    }
    // TODO: if a IDB predicate without vars, then solve all and test contains result?
    //    if (relations.isEmpty)
    //      return Set()
    val relations = precedenceGraph.topSort().filter(r => storageManager.idbs.contains(r))
    debug(s"precedence graph=", precedenceGraph.sortedString)
    debug(s"solving relation: ${storageManager.ns(rId)} order of relations=", relations.toString)
    knownDbId = storageManager.initEvaluation()
    var newDbId = storageManager.initEvaluation()
    var count = 0

    debug("initial state @ -1", storageManager.printer.toString)
    val startRId = relations.head
    relations.foreach(rel => {
      eval(rel, relations, newDbId, knownDbId) // this fills derived[new]
      storageManager.resetDelta(rel, newDbId, storageManager.getDerivedDB(rel, newDbId)) // copy delta[new] = derived[new]
    })

    var setDiff = true
    while(setDiff) {
      val t = knownDbId
      knownDbId = newDbId
      newDbId = t // swap new and known DBs
      storageManager.clearDB(true, newDbId)
      storageManager.printer.known = knownDbId // TODO: get rid of

      debug(s"initial state @ $count", storageManager.printer.toString)
      count += 1
      evalSN(rId, relations, newDbId, knownDbId)
      setDiff = storageManager.deltaDB(newDbId).exists((k, v) => v.nonEmpty)
    }
    storageManager.getIDBResult(rId, newDbId)
  }
}
