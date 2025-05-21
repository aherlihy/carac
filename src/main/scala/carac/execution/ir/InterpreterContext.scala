package carac.execution.ir

import carac.execution.{ExecutionEngine, PrecedenceGraph, TyQLExecutionEngine}
import carac.storage.{DuckDBStorageManager, EDB, RelationId, StorageManager, StorageTerm}
import carac.tools.Debug.debug

class InterpreterContext(val storageManager: StorageManager,
                         val ee: ExecutionEngine,
                         val precedenceGraph: Option[PrecedenceGraph],
                         val finalSolve: () => Set[Seq[StorageTerm]])