package datalog.execution

import datalog.dsl.Term
import datalog.execution.ast.ASTNode
import datalog.execution.ir.*
import datalog.storage.{StorageManager, DB, KNOWLEDGE, EDB}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.quoted.{Type, staging}
import scala.util.{Failure, Success}

/**
 * Instead of compiling entire subtree, compile only contents of the node and call into continue.
 * Unclear if it will ever be useful to only compile a single, mid-tier node, and go back to
 * interpretation (maybe for save points for de-optimizations?) but this is mostly just a POC
 * that it's possible.
 *
 * Alternatively, could potentially make the run methods into macros or at least generate exprs
 * so they can be cached and reused.
 */
class StagedSnippetExecutionEngine(override val storageManager: StorageManager,
                                   defaultJITOptions: JITOptions = JITOptions()) extends StagedExecutionEngine(storageManager, defaultJITOptions) {
  val snippetCompiler: StagedSnippetCompiler = StagedSnippetCompiler(storageManager)(using defaultJITOptions)
  given staging.Compiler = defaultJITOptions.dotty

  override def jit[T](irTree: IROp[T])(using jitOptions: JITOptions): T = {
    debug("", () => s"IN SNIPPET IR, code=${irTree.code}")
    irTree match {
      case op: ProgramOp if jitOptions.granularity == op.code =>
        if (op.compiledSnippetContinuationFn == null)
          op.compiledSnippetContinuationFn = snippetCompiler.getCompiledSnippet(op)
        op.compiledSnippetContinuationFn(
          storageManager,
          op.children.flatMap(o => o.children.map(o2 => (sm: StorageManager) => jit(o2)))) // or o2.run() for only interp

      case op: ProgramOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: DoWhileOp if jitOptions.granularity == op.code =>
        if (op.compiledSnippetContinuationFn == null)
          op.compiledSnippetContinuationFn = snippetCompiler.getCompiledSnippet(op)
        op.compiledSnippetContinuationFn(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: DoWhileOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: SequenceOp =>
        op.code match
          case OpCode.EVAL_SN | OpCode.EVAL_NAIVE | OpCode.LOOP_BODY if jitOptions.granularity == op.code =>
            if (op.compiledSnippetContinuationFn == null)
              op.compiledSnippetContinuationFn = snippetCompiler.getCompiledSnippet(op)
            op.compiledSnippetContinuationFn(
              storageManager,
              op.children.flatMap(o => o.children.map(o2 => (sm: StorageManager) => jit(o2)))) // or o2.run(sm) for only interp
          case _ =>
            op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: SwapAndClearOp =>
        op.run(storageManager)

      case op: InsertOp if jitOptions.granularity == op.code =>
        if (op.compiledSnippetContinuationFn == null)
          op.compiledSnippetContinuationFn = snippetCompiler.getCompiledSnippet(op)
        op.compiledSnippetContinuationFn(storageManager, op.children.map(o => (sm: StorageManager) => jit(o.asInstanceOf[IROp[EDB]])))

      case op: InsertOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o.asInstanceOf[IROp[EDB]])))
      case op: DebugNode =>
        op.run(storageManager)

      case op: ScanOp if jitOptions.granularity == op.code =>
        if (op.compiledSnippetContinuationFn == null)
          op.compiledSnippetContinuationFn = snippetCompiler.getCompiledSnippet(op)
        op.compiledSnippetContinuationFn(storageManager, Seq.empty) // TODO: weird

      case op: ScanOp =>
        op.run(storageManager)

      case op: ScanEDBOp =>
        op.run(storageManager)

      case op: ProjectJoinFilterOp if jitOptions.granularity == op.code =>
        if (op.compiledSnippetContinuationFn == null)
          op.compiledSnippetContinuationFn = snippetCompiler.getCompiledSnippet(op)
        op.compiledSnippetContinuationFn(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: ProjectJoinFilterOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: UnionOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: UnionSPJOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: DiffOp if jitOptions.granularity == op.code =>
        if (op.compiledSnippetContinuationFn == null)
          op.compiledSnippetContinuationFn = snippetCompiler.getCompiledSnippet(op)
        op.compiledSnippetContinuationFn(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: DiffOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: DebugPeek =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case _ => throw new Exception("Error: interpretRelOp called with unit operation")
    }
  }
}
