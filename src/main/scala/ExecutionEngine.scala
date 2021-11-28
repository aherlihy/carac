package datalog

import scala.collection.mutable.{ArrayBuffer, HashSet, Map}

trait ExecutionEngine {
  given storageManager: StorageManager
  def initRelation(rId: Int): Unit
  def insertIDB(head: Atom, body: IndexedSeq[Atom]): Unit
//  def insertBulkEDB[T](relationId: Int, terms: Seq[Seq[T]]): Unit = {}
  def solve(): Any
}

// store dependency chart of relations
class SimpleEE extends ExecutionEngine {
  given storageManager: StorageManager = new SimpleStorage
  def initRelation(rId: Int): Unit = {
    storageManager.initRelation(rId)
  }
  def insertIDB(head: Atom, body: IndexedSeq[Atom]): Unit = {
    // TODO: Add to precedence graph
    storageManager.insertIDB(head, body)
  }
  def insertEDB(rule: Atom): Unit = storageManager.insertEDB(rule)

  /**
   * For a single rule, get (1) the indexes of shared variables within the body,
   * (2) the indexes of constants, (3) the indexes of variables in the body shared
   * with the head atom. #1 goes to join, #2 goes to select (or also join
   * depending on implementation), and #3 goes to project
   *
   * @param rule - Includes the head at idx 0
   * @returns - a map of idx -> constant and a list of indexes that have matching variables
   */
  def getOperatorKeys(rule: IndexedSeq[Atom]): (IndexedSeq[IndexedSeq[Int]], Map[Int, Constant], IndexedSeq[Int]) = {
    var constants = Map[Int, Constant]()
    var projects = IndexedSeq[Int]()
    var headVars = HashSet() ++ rule(0).terms.flatMap(t => t match {
      case v: Variable => Seq(v.oid)
      case _ => Seq()
    })

    var vars = rule
      .drop(1)
      .flatMap(a => a.terms)
      .zipWithIndex
      .groupBy(z => z._1)
      .filter{ case (term, matches) =>
        term match {
          case v: Variable => {
            if (headVars.contains(v.oid)) projects :+ matches.map(_._2)
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
  }

  def evalRule(rule: IndexedSeq[Atom],
               keys: (
                 IndexedSeq[IndexedSeq[Int]],
                 Map[Int, Constant],
                 IndexedSeq[Int]
               )): ArrayBuffer[Row2] = {
    var variables = keys._1 // TODO: figure out tuple unpacking
    var constants = keys._2
    var project = keys._3

    var plan = Project(
      Join(
        Scan(rule(1).rId), Scan(rule(2).rId), variables, constants
      ),
      project
    )
    plan.toList()
  }

  /**
   * Take the union of each evalRule for each IDB predicate
   * @param rules - Includes the head at idx 0
   */
  def eval(rules: Seq[IndexedSeq[Atom]]): Seq[Row2] = {
    rules.flatMap(r => evalRule(r, getOperatorKeys(r)))
  }

  def iterateSolveNaive(): IndexedSeq[Atom] = {
    // TODO: start here
  }

  def solve(): Any = iterateSolveNaive()
}
