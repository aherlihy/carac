package datalog.execution

import datalog.execution.ir.*
import datalog.storage.StorageManager
/**
 * Separate out compile logic from StagedExecutionEngine
 */
abstract class StagedCompiler(storageManager: StorageManager) {
  /**
   * Compile a program starting from irTree
   */
  def compile[T](irTree: IROp[T]): CompiledFn[T]
  /**
   * The following compile methods are for compiling with entry points for longer-running operations, so they return an
   * indexed compile fn so execution can begin from the correct index. Currently only for union ops.
   */
  def compileIndexed[T](irTree: IROp[T]): CompiledFnIndexed[T]
}
