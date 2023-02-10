package datalog.execution

import datalog.dsl.{Atom, Constant, MODE, Term, Variable}
import datalog.storage.{RelationId, SimpleStorageManager, StorageManager}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class NaiveExecutionEngine(val storageManager: StorageManager) extends ExecutionEngine {
  import storageManager.EDB
  val precedenceGraph = new PrecedenceGraph(using storageManager.ns)
  val prebuiltOpKeys: mutable.Map[RelationId, ArrayBuffer[JoinIndexes]] = mutable.Map[RelationId, mutable.ArrayBuffer[JoinIndexes]]()

  /* internal representation of IDBs. Previously used the StorageEngine types to represent IDBs but couldn't think of a
     good enough reason to deal with more path-dependent types */
  val idbs: mutable.Map[RelationId, mutable.ArrayBuffer[IndexedSeq[Atom]]] = mutable.Map()

  def initRelation(rId: RelationId, name: String): Unit = {
    storageManager.ns(rId) = name
    storageManager.initRelation(rId, name)
  }

  def get(rId: RelationId): Set[Seq[Term]] = {
    if (storageManager.knownDbId == -1)
      throw new Exception("Solve() has not yet been called")
    val edbs = storageManager.getEDBResult(rId)
    if (idbs.contains(rId))
      edbs ++ storageManager.getKnownIDBResult(rId)
    else
      edbs
  }
  def get(name: String): Set[Seq[Term]] = {
    get(storageManager.ns(name))
  }

  def insertIDB(rId: RelationId, rule: Seq[Atom]): Unit = {
    precedenceGraph.addNode(rule)
    precedenceGraph.idbs.addOne(rId)
    idbs.getOrElseUpdate(rId, mutable.ArrayBuffer[IndexedSeq[Atom]]()).addOne(rule.toIndexedSeq)
    prebuiltOpKeys.getOrElseUpdate(rId, mutable.ArrayBuffer[JoinIndexes]()).addOne(getOperatorKey(rule))
  }

  def insertEDB(rule: Atom): Unit = {
    if (!storageManager.edbs.contains(rule.rId))
      prebuiltOpKeys.getOrElseUpdate(rule.rId, mutable.ArrayBuffer[JoinIndexes]()).addOne(JoinIndexes(IndexedSeq(), Map(), IndexedSeq(), Seq(rule.rId), true))
    storageManager.insertEDB(rule)
  }

  def evalRule(rId: RelationId):  EDB = {
    storageManager.naiveSPJU(rId, getOperatorKeys(rId).asInstanceOf[storageManager.Table[JoinIndexes]])
  }

  /**
   * Take the union of each evalRule for each IDB predicate
   */
  def eval(rId: RelationId, relations: Seq[RelationId]): Unit = {
    debug("in eval: ", () => s"rId=${storageManager.ns(rId)} relations=${relations.map(r => storageManager.ns(r)).mkString("[", ", ", "]")}")
//    relations.foreach(r => { // NOTE: moved union into solve as to not double-union in semi-naive
      val res = evalRule(rId)
      debug("result of evalRule=", () => storageManager.printer.factToString(res))
      storageManager.resetNewDerived(rId, res) // overwrite res to the new derived DB
//    })
  }

  def solve(toSolve: RelationId, mode: MODE): Set[Seq[Term]] = {
    storageManager.verifyEDBs(idbs.keys.to(mutable.Set))
    if (storageManager.edbs.contains(toSolve) && !idbs.contains(toSolve)) { // if just an edb predicate then return
      return storageManager.getEDBResult(toSolve)
    }
    if (!idbs.contains(toSolve)) {
      throw new Error("Solving for rule without body")
    }
    val relations = precedenceGraph.topSort(toSolve)
    storageManager.initEvaluation() // facts discovered in the previous iteration
    var count = 0

    debug(s"solving relation: ${storageManager.ns(toSolve)} order of relations=", relations.toString)
    var setDiff = true
    while (setDiff) {
      storageManager.swapKnowledge()
      storageManager.clearNewDB(true)

      debug(s"initial state @ $count", storageManager.printer.toString)
      count += 1
      relations.foreach(r =>
        eval(r, relations)
      )

      setDiff = !storageManager.compareDerivedDBs()

    }
    storageManager.getKnownIDBResult(toSolve)
  }
}
