package datalog.execution

import datalog.execution.ir.*
import datalog.storage.StorageManager
/**
 * Separate out compile logic from StagedExecutionEngine
 */
abstract class StagedCompiler(storageManager: StorageManager)(using val jitOptions: JITOptions) {
  /**
   * Compile a program starting from irTree
   */
  def compile[T](irTree: IROp[T]): CompiledFn[T]
  /**
   * The following compile methods are for compiling with entry points for longer-running operations, so they return an
   * indexed compile fn so execution can begin from the correct index. Currently only for union ops.
   */
//  def compileIndexed[T](irTree: IROp[T]): CompiledFnIndexed[T]

  def compileIndexed[T](irTree: IROp[T]): CompiledFnIndexed[T] = {
    irTree match {
      case UnionSPJOp(rId, k, children: _*) =>
        val (sortedChildren, _) =
          if (jitOptions.sortOrder != SortOrder.Unordered)
            JoinIndexes.getPresort(
              children,
              jitOptions.getSortFn(storageManager),
              jitOptions.getUniqueKeysFn(storageManager),
              rId,
              k,
              storageManager
            )
          else
            (children, k)
        (sm, i) => sortedChildren.map(compile)(i)(sm)

      case UnionOp(label, children: _*) =>
        (sm, i) => children.map(compile)(i)(sm)
    }
  }

}
