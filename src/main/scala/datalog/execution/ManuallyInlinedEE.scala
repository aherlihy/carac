//package datalog.execution
//
//import datalog.dsl.{Atom, Constant, Term, Variable}
//import datalog.storage.{SimpleStorageManager, StorageManager, debug}
//
//import scala.collection.mutable
//
//class ManuallyInlinedEE(val storageManager: StorageManager) extends ExecutionEngine {
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
//
////  def evalRule(rId: Int, queryId: Int, prevQueryId: Int): EDB = {
////    val keys = storageManager.idbs(rId).filter(r => r.nonEmpty).map(r => storageManager.getOperatorKeys(r))
////    storageManager.naiveSPJU(rId, keys.toSeq, prevQueryId)
////  }
//
//  /**
//   * Take the union of each evalRule for each IDB predicate
//   */
////  def eval(rId: Int, relations: Seq[Int], queryId: Int, prevQueryId: Int): EDB = {
////    relations.foreach(r => {
////      val res = evalRule(r, queryId, prevQueryId)
////      storageManager.resetIncrEDB(r, queryId, res)
////    })
////    storageManager.getIncrementDB(rId, queryId)
////  }
//
////  def evalRuleSN(rId: Int, queryId: Int, prevQueryId: Int): EDB = {
////    val keys = storageManager.idbs(rId).map(r => storageManager.getOperatorKeys(r)).toSeq
////    storageManager.SPJU(rId, keys, prevQueryId)
////  }
//
////  def evalSN(rId: Int, relations: Seq[Int], queryId: Int, prevQueryId: Int): EDB = {
////    relations.foreach(r => {
////      val prev = storageManager.getIncrementDB(r, queryId)
////      val res = evalRuleSN(r, queryId, prevQueryId)
////      storageManager.resetIncrEDB(r, queryId, res, prev)
////      storageManager.resetDeltaEDB(r, storageManager.getDiff(res, prev), queryId)
////    })
////    storageManager.getIncrementDB(rId, queryId)
////  }
//
//  def solve(rId: Int): Set[Seq[Term]] = {
//
//    if (storageManager.edb(rId).nonEmpty) { // if just an edb predicate then return
//      return storageManager.getEDBResult(rId)
//    }
//
//  precedenceGraph.topSort() // TODO: eventually do incremental topsort?
//  val relations = precedenceGraph.sorted.flatMap(s => s.toSeq).filter(r => storageManager.idb(r).nonEmpty).toSeq
//  if (relations.isEmpty)
//      return Set()
//    val queryId = storageManager.initEvaluation()
//    val prevQueryId = storageManager.initEvaluation()
//    var count = 0
//
//    val startRId = relations.head
////     [Replacing] val res = eval(startRId, relations, pQueryId, prevQueryId)
////     === start body of eval:
//      relations.foreach(r => {
////     [Replacing] val res = evalRule(r, queryId, prevQueryId)
////      === start body of evalRule
//        val keys = storageManager.getOperatorKeys(r)
////     [Replacing] val res = storageManager.naiveSPJU(r, keys.toSeq, prevQueryId)
////      === start body of storageManager.naiveSPJU
//        val res =
//          keys.flatMap(k => // for each idb rule
//            storageManager.joinHelper( // this is inlined so can ignore for now
//              k.deps.map(r => storageManager.getIncrementDB(r, prevQueryId)), k
//            )
//            .map(t => t.zipWithIndex.filter((e, i) => k.projIndexes.contains(i)).map(_._1))
//          ).asInstanceOf[this.storageManager.EDB]
////      === end body of naiveSPJU
////      === end body of evalRule
//        storageManager.resetIncrEDB(r, queryId, res)
//      })
//      val res = storageManager.getIncrementDB(startRId, queryId)
////      println("Inline ONLY: init RES = " + res)
//  //    === end body of eval
//
//    storageManager.resetDeltaEDB(startRId, res, queryId)
//    storageManager.resetIncrEDB(startRId, queryId, res)
//    storageManager.resetDeltaEDB(startRId, res, prevQueryId)
//
//    var setDiff = true
//    while(setDiff) {
//      count += 1
////      println("SN iteration " + count)
////      println(storageManager.printer)
////    [Replacing]  val p = evalSN(rId, relations, pQueryId, prevQueryId)
////    === start evalSN
//      relations.foreach(r => {
//        val prev = storageManager.getIncrementDB(r, queryId)
////      [Replacing] val res = evalRuleSN(r, queryId, prevQueryId)
////      === start body of evalRuleSN
//        val keys = storageManager.getOperatorKeys(r)
////      [Replacing] val res = storageManager.SPJU(rId, keys, prevQueryId)
////      === start body of storageManager.SPJU
//        val res =
//          keys.flatMap(k => // for each idb rule
//            k.deps.flatMap(d =>
//              storageManager.joinHelper(
//                k.deps.map(r =>
//                  if (r == d)
//                    storageManager.deltaDB(prevQueryId)(r)
//                  else
//                    storageManager.incrementalDB(prevQueryId)(r)
//                ), k)
//                .map(t => t.zipWithIndex.filter((e, i) => k.projIndexes.contains(i)).map(_._1))
//            ).toSet
//          )
//        val res2 = res.asInstanceOf[EDB]
////        Table(plan*)
////      === end body of storageManager.SPJU
////      === end body of evalRuleSN
//        storageManager.resetIncrEDB(r, queryId, res2, prev)
//        storageManager.resetDeltaEDB(r, storageManager.getDiff(res2, prev), queryId)
//      })
//      val p = storageManager.getIncrementDB(rId, queryId)
////    === end evalSN
//      setDiff = storageManager.deltaDB(queryId).exists((k, v) => v.nonEmpty)
//      storageManager.swapDeltaDBs(prevQueryId, queryId)
//    }
//    storageManager.getResult(rId, queryId)
//  }
//}
