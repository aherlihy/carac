//package datalog.execution
//
//import datalog.dsl.{Atom, Constant, Term, Variable}
//import datalog.storage.{SimpleStorageManager, StorageManager, debug}
//
//import scala.collection.mutable
//import scala.quoted.*
//given staging.Compiler = staging.Compiler.make(getClass.getClassLoader)
//
//class StagedExecutionEngine(override val storageManager: StorageManager) extends NaiveExecutionEngine(storageManager) {
//  import storageManager.EDB
//
//  def evalRuleSN(rId: Int, queryIdh: Int, prevQueryId: Int): EDB = {
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
//    val deps = precedenceGraph.getTopSort
//    val relations = deps.flatten.filter(r => storageManager.idb(r).nonEmpty)
//    println("relations=" + relations)
//    println("deps=" + deps)
//    if (relations.isEmpty)
//      return Set()
//    val pQueryId = storageManager.initEvaluation()
//    val prevQueryId = storageManager.initEvaluation()
//    var count = 0
//
//    val startRId = relations.head
//    val res = eval(startRId, relations, pQueryId, prevQueryId)
//
//    storageManager.resetDeltaEDB(startRId, res, pQueryId)
//    storageManager.resetIncrEDB(startRId, pQueryId, res)
//    storageManager.resetDeltaEDB(startRId, res, prevQueryId)
//
//    var setDiff = true
//    while(setDiff) {
//      println(storageManager.printer)
//      count += 1
//      val p = evalSN(rId, relations, pQueryId, prevQueryId)
//      setDiff = storageManager.deltaDB(pQueryId).exists((k, v) => v.nonEmpty)
//      storageManager.swapDeltaDBs(prevQueryId, pQueryId)
//    }
//    storageManager.getResult(rId, pQueryId)
//  }
//}
