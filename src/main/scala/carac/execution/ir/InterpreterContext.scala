package carac.execution.ir

import carac.execution.{PrecedenceGraph, TyQLExecutionEngine}
import carac.storage.{DuckDBStorageManager, EDB, RelationId, StorageManager, StorageTerm}
import carac.tools.Debug.debug

trait InterpreterContext(val storageManager: StorageManager, val finalSolve: () => Set[Seq[StorageTerm]])

class CaracInterpreterContext(sm: StorageManager,
                              val precedenceGraph: PrecedenceGraph,
                              val toSolve: RelationId) extends InterpreterContext(sm, () => sm.getIDBResult(toSolve)) {
  storageManager.initEvaluation()
  var count: Int = 0
}

class TyQLInterpreterContext(val ddb: DuckDBStorageManager,
                             val ee: TyQLExecutionEngine,
                             finalSolve: () => Set[Seq[StorageTerm]]) extends InterpreterContext(ddb, finalSolve)