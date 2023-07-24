package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.storage.{RelationId, CollectionsStorageManager, StorageManager, EDB}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class NaiveExecutionEngine(val storageManager: StorageManager) extends ExecutionEngine {
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

  def insertIDB(rId: RelationId, ruleSeq: Seq[Atom]): Unit = {
    val rule = ruleSeq.toArray
    precedenceGraph.addNode(rule)
    precedenceGraph.idbs.addOne(rId)
    idbs.getOrElseUpdate(rId, mutable.ArrayBuffer[IndexedSeq[Atom]]()).addOne(rule.toIndexedSeq)
    prebuiltOpKeys.getOrElseUpdate(rId, mutable.ArrayBuffer[JoinIndexes]()).addOne(getOperatorKey(rule))
  }

  def insertEDB(rule: Atom): Unit = {
    if (!storageManager.edbContains(rule.rId))
      prebuiltOpKeys.getOrElseUpdate(rule.rId, mutable.ArrayBuffer[JoinIndexes]()).addOne(JoinIndexes(IndexedSeq(), Map(), IndexedSeq(), Seq(rule.rId), Array(rule), true))
    storageManager.insertEDB(rule)
  }

  def evalRuleNaive(rId: RelationId):  EDB = {
    storageManager.naiveSPJU(rId, getOperatorKeys(rId))
  }

  /**
   * Take the union of each evalRule for each IDB predicate
   */
  def evalNaive(relations: Seq[RelationId], copyToDelta: Boolean = false): Unit = {
    debug("in eval: ", () => s"relations=${relations.map(r => storageManager.ns(r)).mkString("[", ", ", "]")}")
    relations.foreach(r => {
      val res = evalRuleNaive(r)
      debug("result of evalRule=", () => storageManager.printer.factToString(res))
      storageManager.resetNewDerived(r, res, storageManager.getEmptyEDB()) // overwrite res to the new derived DB
      if (copyToDelta) {
        storageManager.resetNewDelta(r, res) // copy delta[new] = derived[new], if this is called from SN
      }
    })
  }

  def solve(toSolve: RelationId): Set[Seq[Term]] = {
    storageManager.verifyEDBs(idbs.keys.to(mutable.Set))
    if (storageManager.edbContains(toSolve) && !idbs.contains(toSolve)) { // if just an edb predicate then return
      return storageManager.getEDBResult(toSolve)
    }
    if (!idbs.contains(toSolve)) {
      throw new Exception("Solving for rule without body")
    }
    val relations = precedenceGraph.topSort(toSolve)
    storageManager.initEvaluation() // facts discovered in the previous iteration
    var count = 0

    debug(s"solving relation: ${storageManager.ns(toSolve)} order of relations=", relations.toString)
    var setDiff = true
    while (setDiff) {
      storageManager.swapKnowledge()
      storageManager.clearNewDerived()

      debug(s"initial state @ $count", storageManager.toString)
      count += 1
      evalNaive(relations)

      setDiff = !storageManager.compareDerivedDBs()
    }
    storageManager.getKnownIDBResult(toSolve)
  }
}
