package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.storage.{SimpleStorageManager, StorageManager}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class NaiveExecutionEngine(val storageManager: StorageManager) extends ExecutionEngine {
  import storageManager.EDB
  val precedenceGraph = new PrecedenceGraph(using storageManager.ns)
  val prebuiltOpKeys: mutable.Map[Int, ArrayBuffer[JoinIndexes]] = mutable.Map[Int, mutable.ArrayBuffer[JoinIndexes]]()
  var knownDbId = -1

  def initRelation(rId: Int, name: String): Unit = {
    storageManager.ns(rId) = name
    storageManager.initRelation(rId, name)
  }

  def get(rId: Int): Set[Seq[Term]] = {
    if (knownDbId == -1)
      throw new Exception("Solve() has not yet been called")
    val edbs = storageManager.getEDBResult(rId)
    if (storageManager.idbs.contains(rId))
      edbs ++ storageManager.getIDBResult(rId, knownDbId)
    else
      edbs
  }
  def get(name: String): Set[Seq[Term]] = {
    get(storageManager.ns(name))
  }

  def insertIDB(rId: Int, rule: Seq[Atom]): Unit = {
    precedenceGraph.addNode(rule)
    precedenceGraph.idbs.addOne(rId)
    storageManager.insertIDB(rId, rule)
    prebuiltOpKeys.getOrElseUpdate(rId, mutable.ArrayBuffer[JoinIndexes]()).addOne(getOperatorKey(rule))
  }

  def insertEDB(rule: Atom): Unit = {
    if (!storageManager.edbs.contains(rule.rId))
      prebuiltOpKeys.getOrElseUpdate(rule.rId, mutable.ArrayBuffer[JoinIndexes]()).addOne(JoinIndexes(IndexedSeq(), Map(), IndexedSeq(), Seq(rule.rId), true))
    storageManager.insertEDB(rule)
  }

  def evalRule(rId: Int, knownDbId: Int):  EDB = {
    storageManager.naiveSPJU(rId, getOperatorKeys(rId).asInstanceOf[storageManager.Table[JoinIndexes]], knownDbId)
  }

  /**
   * Take the union of each evalRule for each IDB predicate
   */
  def eval(rId: Int, relations: Seq[Int], newDbId: Int, knownDbId: Int): Unit = {
    debug("in eval: ", () => s"rId=${storageManager.ns(rId)} relations=${relations.map(r => storageManager.ns(r)).mkString("[", ", ", "]")}  incr=$newDbId src=$knownDbId")
    relations.foreach(r => {
      val res = evalRule(r, knownDbId)
      debug("result of evalRule=", () => storageManager.printer.factToString(res))
      storageManager.resetDerived(r, newDbId, res) // overwrite res to the derived DB
    })
  }

  def solve(toSolve: Int): Set[Seq[Term]] = {
    storageManager.verifyEDBs()
    if (storageManager.edbs.contains(toSolve) && !storageManager.idbs.contains(toSolve)) { // if just an edb predicate then return
      return storageManager.getEDBResult(toSolve)
    }
    if (!storageManager.idbs.contains(toSolve)) {
      throw new Error("Solving for rule without body")
    }
    val relations = precedenceGraph.topSort()
    knownDbId = storageManager.initEvaluation() // facts discovered in the previous iteration
    var newDbId = storageManager.initEvaluation() // place to store new facts
    var count = 0

    debug(s"solving relation: ${storageManager.ns(toSolve)} order of relations=", relations.toString)
    var setDiff = true
    while (setDiff) {
      val t = knownDbId
      knownDbId = newDbId
      newDbId = t
      storageManager.clearDB(true, newDbId)
      storageManager.printer.known = knownDbId // TODO: get rid of
      storageManager.printer.newId = newDbId

      debug(s"initial state @ $count", storageManager.printer.toString)
      count += 1
      eval(toSolve, relations, newDbId, knownDbId)

      setDiff = !storageManager.compareDerivedDBs(newDbId, knownDbId)

    }
    storageManager.getIDBResult(toSolve, knownDbId)
  }
}
