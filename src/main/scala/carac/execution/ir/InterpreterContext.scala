package carac.execution.ir

import carac.execution.PrecedenceGraph
import carac.storage.{StorageManager, RelationId}
import carac.tools.Debug.debug

class InterpreterContext(val storageManager: StorageManager, val precedenceGraph: PrecedenceGraph, val toSolve: Int) {
  storageManager.initEvaluation()
  var count: Int = 0
}

