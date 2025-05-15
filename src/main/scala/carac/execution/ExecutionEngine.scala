package carac.execution

import carac.dsl.{Atom, StorageAtom}
import carac.storage.{DatabaseType, RelationId, StorageManager, StorageTerm}


trait ExecutionEngine {
  val precedenceGraph: PrecedenceGraph
  val storageManager: StorageManager // TODO: exposed for testing, for now
  def initRelation(rId: RelationId, name: String, schemaOpt: Option[Seq[(String, DatabaseType)]]): Unit

  def insertIDB(rId: RelationId, rule: Seq[Atom]): Unit
  def insertEDB(body: StorageAtom, schema: Option[Seq[(String, DatabaseType)]]): Unit

  def solve(rId: RelationId): Set[Seq[StorageTerm]]
  def get(rId: RelationId): Set[Seq[StorageTerm]]
  def get(name: String): Set[Seq[StorageTerm]]
}
