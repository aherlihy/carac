package datalog.execution.old_manual_opt

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.{ExecutionEngine, PrecedenceGraph}
import datalog.storage.{SimpleStorageManager, StorageManager, debug}

import scala.collection.mutable

class ManuallyInlinedExternal(val storageManager: StorageManager) extends ExecutionEngine {
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
  def evalRule(rId: Int, queryId: Int, prevQueryId: Int): EDB = {
    val keys = storageManager.getOperatorKeys(rId)
    storageManager.naiveSPJU(rId, keys, prevQueryId)
//  [Replacing]  storageManager.naiveSPJU(rId, keys.toSeq, prevQueryId)
//  === start body of storageManager.naiveSPJU
    keys.flatMap(k => // for each idb rule
      storageManager.joinHelper( // this is inlined so can ignore for now
        k.deps.map(r => storageManager.getIncrementDB(r, prevQueryId)), k
      )
        .map(t => t.zipWithIndex.filter((e, i) => k.projIndexes.contains(i)).map(_._1))
    ).asInstanceOf[this.storageManager.EDB]
//      === end body of naiveSPJU
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

  def evalRuleSN(rId: Int, queryId: Int, prevQueryId: Int): EDB = {
    val keys = storageManager.getOperatorKeys(rId)
    storageManager.SPJU(rId, keys, prevQueryId)
//  [Replacing]  storageManager.SPJU(rId, keys, prevQueryId)
//  === start body of storageManager.SPJU
//    val res =
    keys.flatMap(k => // for each idb rule
      k.deps.flatMap(d =>
        storageManager.joinHelper(
          k.deps.map(r =>
            if (r == d)
              storageManager.deltaDB(prevQueryId)(r)
            else
              storageManager.incrementalDB(prevQueryId)(r)
          ), k)
          .map(t => t.zipWithIndex.filter((e, i) => k.projIndexes.contains(i)).map(_._1))
      ).toSet
    ).asInstanceOf[EDB]
//    EDB(res*)
//  === end body of storageManager.SPJU
  }

  def evalSN(rId: Int, relations: Seq[Int], queryId: Int, prevQueryId: Int): EDB = {
    relations.foreach(r => {
      val prev = storageManager.getIncrementDB(r, queryId)
      val res = evalRuleSN(r, queryId, prevQueryId)
      storageManager.resetIncrEDB(r, queryId, res, prev)
      storageManager.resetDeltaEDB(r, storageManager.getDiff(res, prev), queryId)
    })
    storageManager.getIncrementDB(rId, queryId)
  }

  override def solve(rId: Int): Set[Seq[Term]] = {
    if (storageManager.edb(rId).nonEmpty) { // if just an edb predicate then return
      return storageManager.getEDBResult(rId)
    }

    val relations = precedenceGraph.getTopSort.flatten.filter(r => storageManager.idb(r).nonEmpty)
    if (relations.isEmpty)
      return Set()
    val pQueryId = storageManager.initEvaluation()
    val prevQueryId = storageManager.initEvaluation()
    var count = 0

    val startRId = relations.head
    val res = eval(startRId, relations, pQueryId, prevQueryId)

    storageManager.resetDeltaEDB(startRId, res, pQueryId)
    storageManager.resetIncrEDB(startRId, pQueryId, res)
    storageManager.resetDeltaEDB(startRId, res, prevQueryId)

    var setDiff = true
    while(setDiff) {
            println("SN iterate " + count)
            println(storageManager.printer)
      count += 1
      val p = evalSN(rId, relations, pQueryId, prevQueryId)
      setDiff = storageManager.deltaDB(pQueryId).exists((k, v) => v.nonEmpty)
      storageManager.swapDeltaDBs(prevQueryId, pQueryId)
    }
    storageManager.getResult(rId, pQueryId)
  }
}
