package datalog.execution

import datalog.execution.ast.ASTNode
import datalog.execution.ir.*
import datalog.storage.{StorageManager, CollectionsStorageManager}
import datalog.tools.Debug.debug

class NaiveStagedExecutionEngine(storageManager: StorageManager) extends StagedExecutionEngine(storageManager) {
  import storageManager.EDB
  override def createIR(ast: ASTNode)(using InterpreterContext): IROp = IRTreeGenerator().generateNaive(ast)
}
class SemiNaiveStagedExecutionEngine(storageManager: StorageManager) extends StagedExecutionEngine(storageManager) {
  import storageManager.EDB
  override def createIR(ast: ASTNode)(using InterpreterContext): IROp = IRTreeGenerator().generateSemiNaive(ast)
}
class SemiNaiveJITStagedExecutionEngine(storageManager: CollectionsStorageManager) extends JITStagedExecutionEngine(storageManager) {
  import storageManager.EDB
  override def createIR(ast: ASTNode)(using InterpreterContext): IROp = IRTreeGenerator().generateSemiNaive(ast)
}
