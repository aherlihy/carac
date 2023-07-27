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

  def solve(rId: RelationId): Set[Seq[Term]]
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
  inline def getOperatorKey(rule: Array[Atom]): JoinIndexes =
    JoinIndexes(rule, None)

  def getOperatorKeys(rId: RelationId): mutable.ArrayBuffer[JoinIndexes] =
    prebuiltOpKeys.getOrElseUpdate(rId, mutable.ArrayBuffer[JoinIndexes]())
}
