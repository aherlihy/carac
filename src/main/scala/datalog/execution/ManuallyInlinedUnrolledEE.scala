package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.storage.{SimpleStorageManager, StorageManager, debug}

import scala.collection.mutable

class ManuallyInlinedUnrolledEE(val storageManager: StorageManager) extends ExecutionEngine {
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

//  def evalRule(rId: Int, queryId: Int, prevQueryId: Int): EDB = {
//    val keys = storageManager.idbs(rId).filter(r => r.nonEmpty).map(r => storageManager.getOperatorKeys(r))
//    storageManager.naiveSPJU(rId, keys.toSeq, prevQueryId)
//  }

  /**
   * Take the union of each evalRule for each IDB predicate
   */
//  def eval(rId: Int, relations: Seq[Int], queryId: Int, prevQueryId: Int): EDB = {
//    relations.foreach(r => {
//      val res = evalRule(r, queryId, prevQueryId)
//      storageManager.resetIncrEDB(r, queryId, res)
//    })
//    storageManager.getIncrementDB(rId, queryId)
//  }

//  def evalRuleSN(rId: Int, queryId: Int, prevQueryId: Int): EDB = {
//    val keys = storageManager.idbs(rId).map(r => storageManager.getOperatorKeys(r)).toSeq
//    storageManager.SPJU(rId, keys, prevQueryId)
//  }

//  def evalSN(rId: Int, relations: Seq[Int], queryId: Int, prevQueryId: Int): EDB = {
//    relations.foreach(r => {
//      val prev = storageManager.getIncrementDB(r, queryId)
//      val res = evalRuleSN(r, queryId, prevQueryId)
//      storageManager.resetIncrEDB(r, queryId, res, prev)
//      storageManager.resetDeltaEDB(r, storageManager.getDiff(res, prev), queryId)
//    })
//    storageManager.getIncrementDB(rId, queryId)
//  }

  def solve(rId: Int): Set[Seq[Term]] = {

//    if (storageManager.edb(rId).nonEmpty) { // if just an edb predicate then return
//      return storageManager.getEDBResult(rId)
//    }

//    val relations = List(1, 2, 3) // precedenceGraph.getTopSort.flatten.filter(r => storageManager.idb(r).nonEmpty)
//    println(relations)
//    if (relations.isEmpty)
//      return Set()
    val queryId = storageManager.initEvaluation()
    val prevQueryId = storageManager.initEvaluation()
    var count = 0

//    val startRId = 1//relations.head
//     [Inlining] val res = eval(startRId, relations, pQueryId, prevQueryId)
//     === start body of eval:
//     [Unrolled] relations.foreach(r_it => {
//     *** LOOP 1
//     [Inlining] val res = evalRule(r, queryId, prevQueryId)
//      === start body of evalRule
//        val keys_1 = storageManager.idbs(1).filter(r => r.nonEmpty).map(r => storageManager.getOperatorKeys(r))
        val keys_1 = storageManager.getOperatorKeys(1)
//     [Inlining] val res = storageManager.naiveSPJU(r, keys.toSeq, prevQueryId)
//      === start body of storageManager.naiveSPJU
        val res_1 =
          keys_1.flatMap(k => // for each idb rule
            storageManager.joinHelper( // this is inlined so can ignore for now
              k.deps.map(r => storageManager.getIncrementDB(r, prevQueryId)), k
            )
            .map(t => t.zipWithIndex.filter((e, i) => k.projIndexes.contains(i)).map(_._1))
          ).asInstanceOf[this.storageManager.EDB]
//      === end body of naiveSPJU
//      === end body of evalRule
        storageManager.resetIncrEDB(1, queryId, res_1)
//      *** END LOOP 1

//     *** LOOP 2
//     [Inlining] val res = evalRule(r, queryId, prevQueryId)
//      === start body of evalRule
        val keys_2 = storageManager.getOperatorKeys(2)
//     [Inlining] val res = storageManager.naiveSPJU(r, keys.toSeq, prevQueryId)
//      === start body of storageManager.naiveSPJU
        val res_2 =
          keys_2.flatMap(k => // for each idb rule
            storageManager.joinHelper( // this is inlined so can ignore for now
              k.deps.map(r => storageManager.getIncrementDB(r, prevQueryId)), k
            )
            .map(t => t.zipWithIndex.filter((e, i) => k.projIndexes.contains(i)).map(_._1))
          ).asInstanceOf[this.storageManager.EDB]
//      === end body of naiveSPJU
//      === end body of evalRule
        storageManager.resetIncrEDB(2, queryId, res_2)
//      *** END LOOP 2
//     *** LOOP 3
//     [Inlining] val res = evalRule(r, queryId, prevQueryId)
//      === start body of evalRule
        val keys_3 = storageManager.getOperatorKeys(4)
//     [Inlining] val res = storageManager.naiveSPJU(r, keys.toSeq, prevQueryId)
//      === start body of storageManager.naiveSPJU
        val res_3 =
          keys_3.flatMap(k => // for each idb rule
            storageManager.joinHelper( // this is inlined so can ignore for now
              k.deps.map(r => storageManager.getIncrementDB(r, prevQueryId)), k
            )
            .map(t => t.zipWithIndex.filter((e, i) => k.projIndexes.contains(i)).map(_._1))
          ).asInstanceOf[this.storageManager.EDB]
//      === end body of naiveSPJU
//      === end body of evalRule
        storageManager.resetIncrEDB(4, queryId, res_3)
//      *** END LOOP 3
//      })
  

      val res = storageManager.getIncrementDB(1, queryId)
//    println("Inline+Unroll: init RES = " + res)
//    === end body of eval

    storageManager.resetDeltaEDB(1, res, queryId)
    storageManager.resetIncrEDB(1, queryId, res)
    storageManager.resetDeltaEDB(1, res, prevQueryId)

    var setDiff = true
    while(setDiff) {
      count += 1
//      println("SN iteration " + count)
//      println(storageManager.printer)
//    [Inlining]  val p = evalSN(rId, relations, pQueryId, prevQueryId)
//    === start evalSN
//    [Unrolled]  relations.foreach(r_iter => {
//      ***START LOOP 1
        val prev_1 = storageManager.getIncrementDB(1, queryId)
//      [Inlining] val res = evalRuleSN(r, queryId, prevQueryId)
//      === start body of evalRuleSN
        val keys_1 = storageManager.getOperatorKeys(1)
//      [Inlining] val res = storageManager.SPJU(rId, keys, prevQueryId)
//      === start body of storageManager.SPJU
        val res_1 =
          keys_1.flatMap(k => // for each idb rule
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
//        Table(plan*)
//      === end body of storageManager.SPJU
//      === end body of evalRuleSN
        storageManager.resetIncrEDB(1, queryId, res_1, prev_1)
        storageManager.resetDeltaEDB(1, storageManager.getDiff(res_1, prev_1), queryId)
//      **END LOOP 1
//      ***START LOOP 2
        val prev_2 = storageManager.getIncrementDB(2, queryId)
//      [Inlining] val res = evalRuleSN(r, queryId, prevQueryId)
//      === start body of evalRuleSN
        val keys_2 = storageManager.getOperatorKeys(2)
//      [Inlining] val res = storageManager.SPJU(rId, keys, prevQueryId)
//      === start body of storageManager.SPJU
        val res_2 =
          keys_2.flatMap(k => // for each idb rule
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
//        Table(plan*)
//      === end body of storageManager.SPJU
//      === end body of evalRuleSN
        storageManager.resetIncrEDB(2, queryId, res_2, prev_2)
        storageManager.resetDeltaEDB(2, storageManager.getDiff(res_2, prev_2), queryId)
//      **END LOOP 2
//      ***START LOOP 3
        val prev_3 = storageManager.getIncrementDB(4, queryId)
//      [Inlining] val res = evalRuleSN(r, queryId, prevQueryId)
//      === start body of evalRuleSN
        val keys_3 = storageManager.getOperatorKeys(4)
//      [Inlining] val res = storageManager.SPJU(rId, keys, prevQueryId)
//      === start body of storageManager.SPJU
        val res_3 =
          keys_3.flatMap(k => // for each idb rule
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
//        Table(plan*)
//      === end body of storageManager.SPJU
//      === end body of evalRuleSN
        storageManager.resetIncrEDB(4, queryId, res_3, prev_3)
        storageManager.resetDeltaEDB(4, storageManager.getDiff(res_3, prev_3), queryId)
//      **END LOOP 3
//      })
      val p = storageManager.getIncrementDB(rId, queryId)
//    === end evalSN
      setDiff = storageManager.deltaDB(queryId).exists((k, v) => v.nonEmpty)
      storageManager.swapDeltaDBs(prevQueryId, queryId)
    }
    storageManager.getResult(rId, queryId)
  }
}
