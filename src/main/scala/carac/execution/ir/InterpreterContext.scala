package carac.execution.ir

import carac.execution.PrecedenceGraph
import carac.storage.{DuckDBStorageManager, RelationId, StorageManager, StorageTerm, EDB}
import carac.tools.Debug.debug

trait InterpreterContext(val storageManager: StorageManager, val finalSolve: () => Set[Seq[StorageTerm]])

class CaracInterpreterContext(sm: StorageManager,
                              val precedenceGraph: PrecedenceGraph,
                              val toSolve: RelationId) extends InterpreterContext(sm, () => sm.getIDBResult(toSolve)) {
  storageManager.initEvaluation()
  var count: Int = 0
}

class TyQLInterpreterContext(sm: DuckDBStorageManager,
                             finalSolve: () => Set[Seq[StorageTerm]]) extends InterpreterContext(sm, finalSolve)