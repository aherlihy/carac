package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.storage.{SimpleStorageManager, StorageManager, debug}

import scala.collection.mutable

class NaiveExecutionEngine(val storageManager: StorageManager) extends ExecutionEngine {
  val ns: mutable.Map[Int, String] = mutable.Map[Int, String]()
  given namespace: mutable.Map[Int, String] = ns
  import storageManager.{EDB}
  val precedenceGraph = new PrecedenceGraph

  def initRelation(rId: Int, name: String): Unit = {
    ns(rId) = name
    storageManager.initRelation(rId, name)
  }

  def insertIDB(rId: Int, rule: Seq[Atom]): Unit = {
    precedenceGraph.addNode(rule)
    storageManager.insertIDB(rId, rule)
  }

  def insertEDB(rule: Atom): Unit = {
    storageManager.insertEDB(rule)
  }

  def evalRule(rId: Int, queryId: Int, prevQueryId: Int): EDB = {
    val keys = storageManager.idbs(rId).filter(r => r.nonEmpty).map(r => storageManager.getOperatorKeys(r))
    storageManager.naiveSPJU(rId, keys.toSeq, prevQueryId)
  }

  /**
   * Take the union of each evalRule for each IDB predicate
   */
  def eval(rId: Int, relations: Seq[Int], queryId: Int, prevQueryId: Int): EDB = {
    relations.foreach(r => {
      val res = evalRule(r, queryId, prevQueryId)
      storageManager.resetIncrEDB(r, queryId, res)
    })
    storageManager.getIncrementDB(rId, queryId)
  }

  def solve(rId: Int): Set[Seq[Term]] = {
    val relations = precedenceGraph.getTopSort(rId).filter(r => storageManager.idb(r).nonEmpty)
    val pQueryId = storageManager.initEvaluation()
    val prevQueryId = storageManager.initEvaluation()
    var count = 0

    var setDiff = true
    while (setDiff) {
      count += 1
      val p = eval(rId, relations, pQueryId, prevQueryId)

      setDiff = !storageManager.compareIncrDBs(pQueryId, prevQueryId)
      storageManager.swapIncrDBs(prevQueryId, pQueryId)
    }
    storageManager.getResult(rId, pQueryId)
  }
}
