//package datalog.execution.old_manual_opt
//
//import datalog.dsl.{Atom, Constant, Term, Variable}
//import datalog.execution.{ExecutionEngine, PrecedenceGraph}
//import datalog.storage.{SimpleStorageManager, StorageManager, debug}
//
//import scala.collection.mutable
//
//class ManuallyCollapseParent(val storageManager: StorageManager) extends ExecutionEngine {
//  import storageManager.EDB
//  val precedenceGraph = new PrecedenceGraph(storageManager.ns)
//
//  def initRelation(rId: Int, name: String): Unit = {
//    storageManager.ns(rId) = name
//    storageManager.initRelation(rId, name)
//  }
//
//  def insertIDB(rId: Int, rule: Seq[Atom]): Unit = {
//    precedenceGraph.addNode(rule)
//    storageManager.insertIDB(rId, rule)
//  }
//
//  def insertEDB(rule: Atom): Unit = {
//    storageManager.insertEDB(rule)
//  }
//  def evalRule(rId: Int, queryId: Int, prevQueryId: Int): EDB = {
//    val keys = storageManager.getOperatorKeys(rId)
//    storageManager.naiveSPJU(rId, keys, prevQueryId)
//  }
//
//  /**
//   * Take the union of each evalRule for each IDB predicate
//   */
//  def eval(rId: Int, relations: Seq[Int], queryId: Int, prevQueryId: Int): EDB = {
//    relations.foreach(r => {
//      val res = evalRule(r, queryId, prevQueryId)
//      storageManager.resetIncrEDB(r, queryId, res)
//    })
//    storageManager.getIncrementDB(rId, queryId)
//  }
//
//  def evalRuleSN(rId: Int, queryId: Int, prevQueryId: Int): EDB = {
//    val keys = storageManager.getOperatorKeys(rId)
//    storageManager.SPJU(rId, keys, prevQueryId)
//  }
//
//  def evalSN(rId: Int, relations: Seq[Int], queryId: Int, prevQueryId: Int): EDB = {
//    relations.foreach(r => {
//      val prev = storageManager.getIncrementDB(r, queryId)
//      val res = evalRuleSN(r, queryId, prevQueryId)
//      storageManager.resetIncrEDB(r, queryId, res, prev)
//      storageManager.resetDeltaEDB(r, storageManager.getDiff(res, prev), queryId)
//    })
//    storageManager.getIncrementDB(rId, queryId)
//  }
//
//  override def solve(rId: Int): Set[Seq[Term]] = {
//    if (storageManager.edb(rId).nonEmpty) { // if just an edb predicate then return
//      return storageManager.getEDBResult(rId)
//    }
//
//    val relations = precedenceGraph.getTopSort.flatten.filter(r => storageManager.idb(r).nonEmpty)
////    println("relations=" + relations)
//    if (relations.isEmpty)
//      return Set()
//    val pQueryId = storageManager.initEvaluation()
//    val prevQueryId = storageManager.initEvaluation()
//    var count = 0
//
//    val startRId = relations.head
//    val res = eval(startRId, relations, pQueryId, prevQueryId)
////    println("COLLAPSE PARENT initial val = " + res)
//
//    storageManager.resetDeltaEDB(startRId, res, pQueryId)
//    storageManager.resetIncrEDB(startRId, pQueryId, res)
//    storageManager.resetDeltaEDB(startRId, res, prevQueryId)
//
//    var setDiff = true
//    while(setDiff) {
////      println("SN iterate " + count)
////      println(storageManager.printer)
//      count += 1
//      val p = evalSN(rId, relations, pQueryId, prevQueryId)
//      setDiff = storageManager.deltaDB(pQueryId).exists((k, v) => v.nonEmpty)
//      storageManager.swapDeltaDBs(prevQueryId, pQueryId)
//    }
//    storageManager.getResult(rId, pQueryId)
//  }
//}
