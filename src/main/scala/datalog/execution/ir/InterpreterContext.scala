package datalog.execution.ir

import datalog.execution.PrecedenceGraph
import datalog.storage.StorageManager
import datalog.tools.Debug.debug

class InterpreterContext(val storageManager: StorageManager, val precedenceGraph: PrecedenceGraph, val toSolve: Int) {
  var knownDbId: Relation = storageManager.initEvaluation()
  var newDbId: Relation = storageManager.initEvaluation()
  val relations: Seq[Relation] = precedenceGraph.topSort()
  var count: Int = 0
}

