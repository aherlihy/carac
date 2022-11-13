package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.storage.{SimpleStorageManager, StorageManager}
import datalog.tools.Debug.debug

import scala.collection.mutable

class SemiNaiveExecutionEngine(override val storageManager: StorageManager) extends NaiveExecutionEngine(storageManager) {
  import storageManager.EDB

  def evalRuleSN(rId: Int, newDbId: Int, knownDbId: Int): EDB = {
    storageManager.SPJU(rId, storageManager.getOperatorKeys(rId), knownDbId)
  }

  def evalSN(rId: Int, relations: Seq[Int], newDbId: Int, knownDbId: Int): EDB = {
    relations.foreach(r => {
      val prev = storageManager.getDerivedDB(r, knownDbId)
      debug("derived[known]=", () => storageManager.printer.factToString(prev))
      val res = evalRuleSN(r, newDbId, knownDbId)
      debug("evalRuleSN=", () => storageManager.printer.factToString(res))
      val diff = storageManager.getDiff(res, prev)
      storageManager.resetDerived(r, newDbId, res, prev) // set derived[new] to derived[new]+delta[new]
      storageManager.resetDelta(r, newDbId, diff)
      debug("diff, i.e. delta[new]=", () => storageManager.printer.factToString(storageManager.deltaDB(newDbId)(r)))
      debug("all, i.e. derived[new]=", () => storageManager.printer.factToString(storageManager.derivedDB(newDbId)(r)))
    })
    storageManager.getDerivedDB(rId, newDbId)
  }

  override def solve(rId: Int): Set[Seq[Term]] = {
    if (storageManager.edbs.contains(rId)) { // if just an edb predicate then return
      return storageManager.getEDBResult(rId)
    }
    // TODO: if a IDB predicate without vars, then solve all and test contains result?
    //    if (relations.isEmpty)
    //      return Set()
    val relations = precedenceGraph.topSort().filter(r => !storageManager.edbs.contains(r))
    var knownDbId = storageManager.initEvaluation()
    var newDbId = storageManager.initEvaluation()
    var count = 0

    val startRId = relations.head
    val res = eval(startRId, relations, newDbId, knownDbId) // this fills derived[new]
    storageManager.resetDelta(startRId, newDbId, res) // copy delta[new] = derived[new]

    var setDiff = true
    var iteration = 0
    while(setDiff) {
      val t = knownDbId
      knownDbId = newDbId
      newDbId = t
      storageManager.clearDB(true, newDbId)
      storageManager.printer.known = knownDbId // TODO: get rid of

      debug("initial state @ " + iteration, storageManager.printer.toString)
      count += 1
      val p = evalSN(rId, relations, newDbId, knownDbId)
      setDiff = storageManager.deltaDB(newDbId).exists((k, v) => v.nonEmpty)
      debug("state after evalSN " + count, storageManager.printer.toString)
      iteration += 1
    }
    storageManager.getIDBResult(rId, newDbId)
  }
}
