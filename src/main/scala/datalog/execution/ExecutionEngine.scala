package datalog.execution

import datalog.dsl.Atom
import datalog.storage.StorageManager

trait ExecutionEngine {
//  given storageManager: StorageManager
//  val storageManager: StorageManager

  def initRelation(rId: Int): Unit

  def insertIDB(rId: Int, rule: Seq[Atom]): Unit
  def insertEDB(body: Atom): Unit

  def solve(rId: Int): Any
  //  def insertBulkEDB[T](relationId: Int, terms: Seq[Seq[T]]): Unit = {}
}
