package datalog.execution

import datalog.execution.ast.ASTNode
import datalog.execution.ir.{ClearOp, DoWhileOp, IROp, IRTreeGenerator, InsertOp, InterpreterContext, JoinOp, ProgramOp, ProjectOp, ScanOp, SequenceOp, SwapOp, UnionOp}
import datalog.storage.StorageManager
import datalog.tools.Debug.debug

class NaiveStagedExecutionEngine(storageManager: StorageManager) extends StagedExecutionEngine(storageManager) {
  import storageManager.EDB
  override def createIR(ast: ASTNode)(using InterpreterContext): IROp = IRTreeGenerator().generateNaive(ast)
}
