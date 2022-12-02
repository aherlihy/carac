package datalog.execution

import datalog.dsl.{Atom, Term}
import datalog.storage.StorageManager

import scala.collection.mutable.ArrayBuffer

trait ExecutionEngine {
  val precedenceGraph: PrecedenceGraph
  val storageManager: StorageManager // TODO: exposed for testing, for now
  def initRelation(rId: Int, name: String): Unit

  def insertIDB(rId: Int, rule: Seq[Atom]): Unit
  def insertEDB(body: Atom): Unit

  def solve(rId: Int): Set[Seq[Term]]
}
