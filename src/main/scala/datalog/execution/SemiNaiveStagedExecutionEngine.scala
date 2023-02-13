package datalog.execution

import datalog.dsl.{MODE, Term}
import datalog.execution.ast.ASTNode
import datalog.execution.ir.*
import datalog.storage.{CollectionsStorageManager, StorageManager}
import datalog.tools.Debug.debug

class NaiveStagedExecutionEngine(storageManager: StorageManager) extends StagedExecutionEngine(storageManager) {
  import storageManager.EDB
  override def createIR(ast: ASTNode)(using InterpreterContext): IROp = IRTreeGenerator().generateNaive(ast)
}
class SemiNaiveStagedExecutionEngine(storageManager: StorageManager) extends StagedExecutionEngine(storageManager) {
  import storageManager.EDB
}

