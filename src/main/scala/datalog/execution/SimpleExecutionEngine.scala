package datalog.execution

import datalog.dsl.{Variable, Atom, Constant}
import datalog.storage.{SimpleStorageManager, StorageManager}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, HashSet, Map, Set}
// store dependency chart of relations
class SimpleExecutionEngine extends ExecutionEngine {
//  given storageManager: StorageManager = new SimpleStorageManager
  val storageManager = new SimpleStorageManager
  import storageManager.{Row, StorageAtom, Table}

  def initRelation(rId: Int): Unit = {
    storageManager.initRelation(rId)
  }

  def insertIDB(rId: Int, rule: Seq[Atom]): Unit = {
    // TODO: Add to precedence graph
    storageManager.insertIDB(rId, rule.map(a => StorageAtom(a.rId, a.terms)).toIndexedSeq)
  }

  def insertEDB(rule: Atom): Unit = {
    storageManager.insertEDB(StorageAtom(rule.rId, rule.terms))
  }

  /**
   * For a single rule, get (1) the indexes of repeated variables within the body,
   * (2) the indexes of constants, (3) the indexes of variables in the body present
   * with the head atom. #1 goes to join, #2 goes to select (or also join
   * depending on implementation), and #3 goes to project
   *
   * @param rule - Includes the head at idx 0
   * @returns - a map of idx -> constant and a list of indexes that have matching variables
   */
  def getOperatorKeys(rule: IndexedSeq[Atom]): (IndexedSeq[IndexedSeq[Int]], Map[Int, Constant], Seq[Int]) = {
    var constants = Map[Int, Constant]()
    var projects = Seq[Int]()

    // variable ids in the head atom
    var headVars = HashSet() ++ rule(0).terms.flatMap(t => t match {
      case v: Variable => Seq(v.oid)
      case _ => Seq()
    })

    val body = rule.drop(1)

    var vars = body
      .flatMap(a => a.terms)
      .zipWithIndex
      .groupBy(z => z._1)
      .filter{ case (term, matches) =>
        term match {
          case v: Variable => {// TODO: change to storage type
            if (headVars.contains(v.oid)) projects = projects ++ matches.map(_._2)
            matches.length >= 2
          }
          case c: Constant => {
            matches.foreach{ case (_, idx) => constants(idx) = c }
            false
          }
        }
      }
      .map{ case (term, matches) =>
        matches.map(_._2)
      }
      .toIndexedSeq
    (vars, constants, projects)
    //      .foreach(println)
    //    (IndexedSeq(IndexedSeq()), constants, projects)
  }

  def evalRule(rule: Row[StorageAtom],
               keys: (
                 IndexedSeq[IndexedSeq[Int]],
                   Map[Int, Constant],
                   Seq[Int]
                 )): ArrayBuffer[Row[Any]] = {
    var variables = keys._1 // TODO: figure out tuple unpacking
    var constants = keys._2
    var project = keys._3

    //    println("rule=" + rule)
    //    println("vars=" + variables)
    //    println("consts=" + constants)
    //    println("project=" + project)

//    var plan =
//      Project(
//        Join(
//          Scan(rule(0).rId), Scan(rule(1).rId), variables, constants
//        ),
//        project
//      )
//    plan.toList()
    ArrayBuffer(IndexedSeq())
  }

  /**
   * Take the union of each evalRule for each IDB predicate
   * @param rules - Includes the head at idx 0
   */
  def eval(rules: Table[StorageAtom]): Any = {
    //    rules.flatMap(r => {
    //      var res = evalRule(r, getOperatorKeys(r))
    //      // TODO: start here! add derived EDBS somewhere before iterating again
    //    })
  }

  def iterateSolveNaive(): IndexedSeq[Atom] = {
    val m = storageManager.idbs.size
    val p = Vector.fill(m)(Set[Row[Any]]())
    val prevs = Vector.fill(m)(Set[Row[Any]]())
    //    while(true) {
    //      prevs
    //    }

    IndexedSeq()
  }

  /**
   * Get the topological order of the dependency graph starting at i
   * @param i
   * @return
   */
  def getTopOrder(i: Int): Seq[Int] = { // TODO: Stratify for cycles, right now just manual
    Seq(0, 1, 2, 3)
  }

  def solve(rId: Int): Any = { // TODO [NOW]: solve what exactly?
    //    val rules = storageManager.idb(rId)
    println(storageManager)
    //    eval(storageManager.idb(1))
  }
}
