package datalog.execution.ir

import datalog.execution.PrecedenceGraph
import datalog.storage.{StorageManager, RelationId}
import datalog.tools.Debug.debug

class InterpreterContext(val storageManager: StorageManager, val precedenceGraph: PrecedenceGraph, val toSolve: Int) {
  storageManager.initEvaluation()
  val relations: Seq[RelationId] = precedenceGraph.topSort()
  var count: Int = 0
}

