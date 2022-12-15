package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.storage.{SimpleStorageManager, StorageManager}
import datalog.tools.Debug.debug

import scala.collection.mutable

class NaiveExecutionEngine(val storageManager: StorageManager) extends ExecutionEngine {
  import storageManager.EDB
  val precedenceGraph = new PrecedenceGraph(storageManager.ns)

  def initRelation(rId: Int, name: String): Unit = {
    storageManager.ns(rId) = name
    storageManager.initRelation(rId, name)
  }

  def insertIDB(rId: Int, rule: Seq[Atom]): Unit = {
    precedenceGraph.addNode(rule)
    storageManager.insertIDB(rId, rule)
  }

  def insertEDB(rule: Atom): Unit = {
    storageManager.insertEDB(rule)
  }

  def evalRule(rId: Int, knownDbId: Int):  EDB = {
    storageManager.naiveSPJU(rId, storageManager.getOperatorKeys(rId), knownDbId)
  }

  /**
   * Take the union of each evalRule for each IDB predicate
   */
  def eval(rId: Int, relations: Seq[Int], newDbId: Int, knownDbId: Int): Unit = {
    debug("in eval: ", () => "rId=" + storageManager.ns(rId) + " relations=" + relations.map(r => storageManager.ns(r)).mkString("[", ", ", "]") + " incr=" + newDbId + " src=" + knownDbId)
    relations.foreach(r => {
      val res = evalRule(r, knownDbId)
      debug("result of evalRule=", () => storageManager.printer.factToString(res))
      storageManager.resetDerived(r, newDbId, res) // overwrite res to the derived DB
    })
  }

  def solve(rId: Int): Set[Seq[Term]] = {
    if (storageManager.edbs.contains(rId) && !storageManager.idbs.contains(rId)) { // if just an edb predicate then return
      return storageManager.getEDBResult(rId)
    }
    if (!storageManager.idbs.contains(rId)) {
      throw new Error("Solving for rule without body")
    }
    val relations = precedenceGraph.topSort().filter(r => storageManager.idbs.contains(r))
    var knownDbId = storageManager.initEvaluation() // facts discovered in the previous iteration
    var newDbId = storageManager.initEvaluation() // place to store new facts
    var count = 0

    debug("solving relation: " + storageManager.ns(rId) + " order of relations=", relations.toString)
    var setDiff = true
    while (setDiff) {
      val t = knownDbId
      knownDbId = newDbId
      newDbId = t
      storageManager.clearDB(true, newDbId)
      storageManager.printer.known = knownDbId // TODO: get rid of
      debug("initial state @ " + count, storageManager.printer.toString)
      count += 1
      eval(rId, relations, newDbId, knownDbId)
//      debug("state after eval=", storageManager.printer.toString)

      setDiff = !storageManager.compareDerivedDBs(newDbId, knownDbId)

    }
    storageManager.getIDBResult(rId, knownDbId)
  }
}
