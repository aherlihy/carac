package datalog.execution

import datalog.execution.ast.ASTNode
import datalog.execution.ir.{ClearOp, DiffOp, DoWhileOp, FilterOp, IROp, IRTree, InsertOp, JoinOp, ProgramOp, ProjectOp, SequenceOp, SwapOp, UnionOp}
import datalog.storage.StorageManager
import datalog.tools.Debug.debug

class NaiveStagedExecutionEngine(storageManager: StorageManager) extends StagedExecutionEngine(storageManager) {
  import storageManager.EDB
  override def createIR(irTree: IRTree, ast: ASTNode): IROp = irTree.initialize(ast)
}
