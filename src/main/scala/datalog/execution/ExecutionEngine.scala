package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable, StorageAtom}
import datalog.storage.{RelationId, StorageManager, StorageTerm}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

trait ExecutionEngine {
  val precedenceGraph: PrecedenceGraph
  val storageManager: StorageManager // TODO: exposed for testing, for now
  def initRelation(rId: RelationId, name: String): Unit

  def insertIDB(rId: RelationId, rule: Seq[Atom]): Unit
  def insertEDB(body: StorageAtom): Unit

  def solve(rId: RelationId): Set[Seq[StorageTerm]]
  def get(rId: RelationId): Set[Seq[StorageTerm]]
  def get(name: String): Set[Seq[StorageTerm]]
}
