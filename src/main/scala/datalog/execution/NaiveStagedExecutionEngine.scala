package datalog.execution

import datalog.execution.ast.ASTNode
import datalog.execution.ir.{IROp, IRTreeGenerator, InterpreterContext}
import datalog.storage.StorageManager
import datalog.tools.Debug.debug

class NaiveStagedExecutionEngine(storageManager: StorageManager) extends StagedExecutionEngine(storageManager) {
  import storageManager.EDB
  override def createIR(ast: ASTNode)(using InterpreterContext): IROp = IRTreeGenerator().generateNaive(ast)
}
