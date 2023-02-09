package datalog.execution.ir

import datalog.execution.PrecedenceGraph
import datalog.storage.{StorageManager, RelationId}
import datalog.tools.Debug.debug

class InterpreterContext(val storageManager: StorageManager, val precedenceGraph: PrecedenceGraph, val toSolve: Int) {
  storageManager.initEvaluation()
  val sortedRelations: Seq[RelationId] = precedenceGraph.topSort(toSolve)
  var count: Int = 0
}

