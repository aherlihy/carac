package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.storage.{RelationId, StorageManager}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

trait ExecutionEngine {
  val precedenceGraph: PrecedenceGraph
  val storageManager: StorageManager // TODO: exposed for testing, for now
  val prebuiltOpKeys: mutable.Map[RelationId, mutable.ArrayBuffer[JoinIndexes]]
  def initRelation(rId: RelationId, name: String): Unit

  def insertIDB(rId: RelationId, rule: Seq[Atom]): Unit
  def insertEDB(body: Atom): Unit

  def solve(rId: RelationId, jitOptions: JITOptions = JITOptions()): Set[Seq[Term]]
  def get(rId: RelationId): Set[Seq[Term]]
  def get(name: String): Set[Seq[Term]]

  /**
   * For a single rule, get (1) the indexes of repeated variables within the body,
   * (2) the indexes of constants, (3) the indexes of variables in the body present
   * with the head atom, (4) relations that this rule is dependent on.
   * #1, #4 goes to join, #2 goes to select (or also join depending on implementation),
   * #3 goes to project
   *
   * @param rule - Includes the head at idx 0
   */
  inline def getOperatorKey(rule: Seq[Atom]): JoinIndexes = {
    val constants = mutable.Map[Int, Constant]() // position => constant
    val variables = mutable.Map[Variable, Int]() // v.oid => position

    val body = rule.drop(1)

    val deps = body.map(a => a.rId) // TODO: should this be a set?

    val bodyVars = body
      .flatMap(a => a.terms)
      .zipWithIndex // terms, position
      .groupBy(z => z._1)
      .filter((term, matches) => // matches = Seq[(var, pos1), (var, pos2), ...]
        term match {
          case v: Variable =>
            variables(v) = matches.head._2 // first idx for a variable
            !v.anon && matches.size >= 2
          case c: Constant =>
            matches.foreach((_, idx) => constants(idx) = c)
            false
        }
      )
      .map((term, matches) => // get rid of groupBy elem in result tuple
        matches.map(_._2).toIndexedSeq
      )
      .toIndexedSeq

    // variable ids in the head atom
    val projects = rule(0).terms.map {
      case v: Variable =>
        if (!variables.contains(v)) throw new Exception(f"Free variable in rule head with varId $v.oid")
        if (v.anon) throw new Exception("Anonymous variable ('__') not allowed in head of rule")
        ("v", variables(v))
      case c: Constant => ("c", c)
    }
    JoinIndexes(bodyVars, constants.toMap, projects, deps)
  }

  def getOperatorKeys(rId: RelationId): mutable.ArrayBuffer[JoinIndexes] = {
    prebuiltOpKeys.getOrElseUpdate(rId, mutable.ArrayBuffer[JoinIndexes]())
  }
}
