package datalog.execution.ir

import datalog.execution.PrecedenceGraph
import datalog.storage.StorageManager
import datalog.tools.Debug.debug

class Context(val storageManager: StorageManager, val precedenceGraph: PrecedenceGraph, val toSolve: Int) {
  var knownDbId: Relation = storageManager.initEvaluation()
  var newDbId: Relation = storageManager.initEvaluation()
  val relations: Seq[Relation] = precedenceGraph.topSort()
  debug(s"precedence graph=", precedenceGraph.sortedString)
}

