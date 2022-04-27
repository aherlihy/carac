package datalog.execution

import datalog.dsl.{Atom, Constant, Variable, Term}
import datalog.storage.{SimpleStorageManager, StorageManager, debug}

import scala.collection.mutable.{ArrayBuffer, HashSet, Map}
// store dependency chart of relations
class SimpleExecutionEngine extends ExecutionEngine {
//  given storageManager: StorageManager = new SimpleStorageManager
  val storageManager = new SimpleStorageManager
  import storageManager.{Row, StorageAtom, Relation, JoinIndexes, StorageTerm, StorageConstant}
  val precedenceGraph = new PrecedenceGraph

  def initRelation(rId: Int, name: String): Unit = {
    storageManager.initRelation(rId, name)
  }

  def insertIDB(rId: Int, rule: Seq[Atom]): Unit = {
    precedenceGraph.addNode(rule)
    storageManager.insertIDB(rId, rule.map(a => StorageAtom(a.rId, a.terms)).toIndexedSeq)
  }

  def insertEDB(rule: Atom): Unit = {
    storageManager.insertEDB(StorageAtom(rule.rId, rule.terms))
  }

  /**
   * For a single rule, get (1) the indexes of repeated variables within the body,
   * (2) the indexes of constants, (3) the indexes of variables in the body present
   * with the head atom, (4) relations that this rule is dependent on.
   * #1, #4 goes to join, #2 goes to select (or also join depending on implementation),
   * #3 goes to project
   *
   * @param rule - Includes the head at idx 0
   */
  def getOperatorKeys(rule: Row[StorageAtom]): JoinIndexes = {
    val constants = Map[Int, StorageConstant]()
    var projects = IndexedSeq[Int]()

    // variable ids in the head atom
    val headVars = HashSet() ++ rule(0).terms.flatMap(t => t match {
      case v: Variable => Seq(v.oid)
      case _ => Seq()
    })

    val body = rule.drop(1)

    val deps = body.map(a => a.rId)

    val vars = body
      .flatMap(a => a.terms)
      .zipWithIndex
      .groupBy(z => z._1)
      .filter((term, matches) =>
        term match {
          case v: storageManager.StorageVariable =>
            if (headVars.contains(v.oid)) projects = projects ++ matches.map(_._2)
            matches.length >= 2
          case c: storageManager.StorageConstant =>
            matches.foreach((_, idx) => constants(idx) = c)
            false
        }
      )
      .map((term, matches) =>
        matches.map(_._2)
      )
      .toIndexedSeq
    JoinIndexes(vars, constants, projects, deps)
  }

  def evalRule(rId: Int, queryId: Int, prevQueryId: Int): Relation[StorageTerm] = {
    val keys = storageManager.idbs(rId).filter(r => r.nonEmpty).map(r => getOperatorKeys(r))
    storageManager.spju(rId, keys, prevQueryId)
  }

  /**
   * Take the union of each evalRule for each IDB predicate
   */
  def eval(rId: Int, relations: Seq[Int], queryId: Int, prevQueryId: Int): Relation[StorageTerm] = {
    relations.map(r => {
      val res = evalRule(r, queryId, prevQueryId)
      storageManager.resetIncrEDB(r, res, queryId)
    })
    storageManager.getIncrementDB(rId, queryId)
  }

  def iterateNaive(rId: Int): Relation[StorageTerm] = {
    val relations = precedenceGraph.getTopSort(rId).filter(r => storageManager.idb(r).nonEmpty) // TODO: put empty check elsewhere
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
    storageManager.getIncrementDB(rId, pQueryId)
  }

  def evalRuleSN(rId: Int, queryId: Int, prevQueryId: Int): Relation[StorageTerm] = {
    println("evalRuleSN: rId:" + rId + " queryId:" + queryId + " prevQId:" + prevQueryId)
    val keys = storageManager.idbs(rId).filter(r => r.nonEmpty).map(r => getOperatorKeys(r))
    println("evalRuleSN: keys=" + keys)
    storageManager.spjuSN(rId, keys, prevQueryId)
  }

  def evalSN(rId: Int, relations: Seq[Int], queryId: Int, prevQueryId: Int): Relation[StorageTerm] = {
    relations.foreach(r => {
      val prev = storageManager.getIncrementDB(r, queryId)
      val res = evalRuleSN(r, queryId, prevQueryId)
      val diff = res diff prev
      storageManager.resetIncrEDB(r, res ++ prev, queryId)
      storageManager.resetDeltaEDB(r, diff, queryId)
    })
    storageManager.getIncrementDB(rId, queryId)
  }

  def iterateSemiNaive(rId: Int): Relation[StorageTerm] = {
    if (storageManager.edb(rId).nonEmpty) { // if just an edb predicate then return
      return storageManager.edb(rId)
    }

    val relations = precedenceGraph.getTopSort(rId).filter(r => storageManager.idb(r).nonEmpty) // TODO: put empty check elsewhere
    val pQueryId = storageManager.initEvaluation()
    val prevQueryId = storageManager.initEvaluation()
    var count = 0

    val startRId = relations.head
    val res = evalRule(startRId, pQueryId, prevQueryId) // TODO: add filter here
    storageManager.resetDeltaEDB(startRId, res, pQueryId)
    storageManager.resetIncrEDB(startRId, res, pQueryId)
    storageManager.resetDeltaEDB(startRId, res, prevQueryId)

    var setDiff = true
    while(setDiff) {
      count += 1
      val p = evalSN(rId, relations, pQueryId, prevQueryId)
      setDiff = storageManager.deltaDB(pQueryId).exists((k, v) => v.nonEmpty)
      storageManager.swapDeltaDBs(prevQueryId, pQueryId)
    }
    storageManager.getIncrementDB(rId, pQueryId)
  }
  def solveNaive(rId: Int): Set[Seq[Term]] = {
    val res = iterateNaive(rId)
    res.toSet
  }

  def solve(rId: Int): Set[Seq[Term]] = {
    println("SM=" + storageManager.toString())
    iterateSemiNaive(rId).toSet
  }
}
