package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.ast.*
import datalog.storage.{SimpleStorageManager, StorageManager}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class StagedExecutionEngine(val storageManager: StorageManager) extends ExecutionEngine {
  import storageManager.EDB
  val precedenceGraph = new PrecedenceGraph(storageManager.ns)
  val tree: ProgramNode = ProgramNode()
  private var knownDbId = -1

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
    val allRules = tree.rules.getOrElseUpdate(rId, AllRulesNode(ArrayBuffer.empty)).asInstanceOf[AllRulesNode]
    allRules.rules.append(
      RuleNode(
        LogicAtom(
          rule.head.rId,
          rule.head.terms.map {
            case x: Variable => VarTerm(x)
            case x: Constant => ConstTerm(x)
          }),
        rule.drop(1).map(b =>
          LogicAtom(b.rId, b.terms.map {
            case x: Variable => VarTerm(x)
            case x: Constant => ConstTerm(x)
          }))
      ))
  }

  def insertEDB(rule: Atom): Unit = {
    storageManager.insertEDB(rule)
  }

  def evalRule(rId: Int, knownDbId: Int):  EDB = {
    storageManager.naiveSPJU(rId, storageManager.getOperatorKeys(rId), knownDbId)
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

  def evalRuleSN(rId: Int, newDbId: Int, knownDbId: Int): EDB = {
    storageManager.SPJU(rId, storageManager.getOperatorKeys(rId), knownDbId)
  }

  def evalSN(rId: Int, relations: Seq[Int], newDbId: Int, knownDbId: Int): Unit = {
    debug("evalSN for ", () => storageManager.ns(rId))
    relations.foreach(r => {
      debug("\t=>iterating@", () => storageManager.ns(r))
      val prev = storageManager.getDerivedDB(r, knownDbId)
      debug(s"\tderived[known][${storageManager.ns(r)}] =", () => storageManager.printer.factToString(prev))
      val res = evalRuleSN(r, newDbId, knownDbId)
      debug("\tevalRuleSN=", () => storageManager.printer.factToString(res))
      val diff = storageManager.getDiff(res, prev)
      storageManager.resetDerived(r, newDbId, diff, prev) // set derived[new] to derived[new]+delta[new]
      storageManager.resetDelta(r, newDbId, diff)
      debug(s"\tdiff, i.e. delta[new][${storageManager.ns(r)}] =", () => storageManager.printer.factToString(storageManager.deltaDB(newDbId)(r)))
      debug(s"\tall, i.e. derived[new][${storageManager.ns(r)}] =", () => storageManager.printer.factToString(storageManager.derivedDB(newDbId)(r)))
    })
  }

  override def solve(rId: Int): Set[Seq[Term]] = {
    storageManager.verifyEDBs()
    if (storageManager.edbs.contains(rId) && !storageManager.idbs.contains(rId)) { // if just an edb predicate then return
      return storageManager.getEDBResult(rId)
    }
    if (!storageManager.idbs.contains(rId)) {
      throw new Error("Solving for rule without body")
    }
    // TODO: if a IDB predicate without vars, then solve all and test contains result?
    //    if (relations.isEmpty)
    //      return Set()
    val relations = precedenceGraph.topSort().filter(r => storageManager.idbs.contains(r))
    debug(s"precedence graph=", precedenceGraph.sortedString)
    debug(s"solving relation: ${storageManager.ns(rId)} order of relations=", relations.toString)
    knownDbId = storageManager.initEvaluation()
    var newDbId = storageManager.initEvaluation()
    var count = 0

    debug("initial state @ -1", storageManager.printer.toString)
    val startRId = relations.head
    relations.foreach(rel => {
      eval(rel, relations, newDbId, knownDbId) // this fills derived[new]
      storageManager.resetDelta(rel, newDbId, storageManager.getDerivedDB(rel, newDbId)) // copy delta[new] = derived[new]
    })

    var setDiff = true
    while(setDiff) {
      val t = knownDbId
      knownDbId = newDbId
      newDbId = t // swap new and known DBs
      storageManager.clearDB(true, newDbId)
      storageManager.printer.known = knownDbId // TODO: get rid of

      debug(s"initial state @ $count", storageManager.printer.toString)
      count += 1
      evalSN(rId, relations, newDbId, knownDbId)
      setDiff = storageManager.deltaDB(newDbId).exists((k, v) => v.nonEmpty)
    }
    storageManager.getIDBResult(rId, newDbId)
  }
}
