package datalog.execution.ir

import datalog.execution.PrecedenceGraph
import datalog.storage.{CollectionsStorageManager, RelationId, StorageManager}
import datalog.tools.Debug.debug

class InterpreterContext(val storageManager: CollectionsStorageManager, val precedenceGraph: PrecedenceGraph, val toSolve: Int) {
  storageManager.initEvaluation()
  val sortedRelations: Seq[RelationId] = precedenceGraph.topSort(toSolve)
  var count: Int = 0
}

