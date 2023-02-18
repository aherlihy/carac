package datalog.execution

import datalog.dsl.{Term}
import datalog.execution.ast.ASTNode
import datalog.execution.ir.*
import datalog.storage.{CollectionsStorageManager, DB, KNOWLEDGE, StorageManager}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.quoted.staging
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
class StagedSnippetExecutionEngine(override val storageManager: CollectionsStorageManager,
                                   defaultJITOptions: JITOptions = JITOptions()) extends StagedExecutionEngine(storageManager, defaultJITOptions) {
  import storageManager.EDB
  val snippetCompiler: StagedSnippetCompiler = StagedSnippetCompiler(storageManager)
  override def jitRel(irTree: IRRelOp)(using jitOptions: JITOptions): storageManager.EDB = {
    println(s"IN SNIPPET JIT REL IR, code=${irTree.code}")
    irTree match {
      case op: ScanOp if jitOptions.granularity == op.code =>
        if (op.compiledRelSnippetContinuationFn == null)
          given staging.Compiler = dedicatedDotty
          op.compiledRelSnippetContinuationFn = snippetCompiler.getCompiledSnippet(op)
        op.compiledRelSnippetContinuationFn(storageManager, Seq.empty)

      case op: ScanOp =>
        op.runRel(storageManager)

      case op: ScanEDBOp =>
        op.runRel_continuation(storageManager)

      case op: JoinOp if jitOptions.granularity == op.code =>
        if (op.compiledRelSnippetContinuationFn == null)
          given staging.Compiler = dedicatedDotty
          op.compiledRelSnippetContinuationFn = snippetCompiler.getCompiledSnippet(op)
        op.compiledRelSnippetContinuationFn(storageManager, op.ops.map(o => sm => jitRel(o)))

      case op: JoinOp =>
        op.runRel_continuation(storageManager, op.ops.map(o => sm => jitRel(o)))

      case op: ProjectOp =>
        op.runRel_continuation(storageManager, Seq(sm => jitRel(op.subOp)))

      case op: UnionOp =>
        op.runRel_continuation(storageManager, op.ops.map(o => sm => jitRel(o)))

      case op: DiffOp if jitOptions.granularity == op.code =>
        if (op.compiledRelSnippetContinuationFn == null)
          given staging.Compiler = dedicatedDotty
          op.compiledRelSnippetContinuationFn = snippetCompiler.getCompiledSnippet(op)
        op.compiledRelSnippetContinuationFn(storageManager, Seq(sm => jitRel(op.lhs), sm => jitRel(op.rhs)))

      case op: DiffOp =>
        op.runRel_continuation(storageManager, Seq(sm => jitRel(op.lhs), sm => jitRel(op.rhs)))

      case op: DebugPeek =>
        op.runRel_continuation(storageManager, Seq(sm => jitRel(op.op)))

      case _ => throw new Exception("Error: interpretRelOp called with unit operation")
    }
  }
  override def jit(irTree: IROp)(using jitOptions: JITOptions): Any = {
    println(s"IN SNIPPET IR, code=${irTree.code}")
    irTree match {
      case op: ProgramOp if jitOptions.granularity == op.code =>
        if (op.compiledSnippetContinuationFn == null)
          given staging.Compiler = dedicatedDotty
          op.compiledSnippetContinuationFn = snippetCompiler.getCompiledSnippet(op)
        op.compiledSnippetContinuationFn(storageManager, Seq(sm => jit(op.body)))

      case op: ProgramOp =>
        op.run_continuation(storageManager, Seq(sm => jit(op.body)))

      case op: DoWhileOp if jitOptions.granularity == op.code =>
        if (op.compiledSnippetContinuationFn == null)
          given staging.Compiler = dedicatedDotty
          op.compiledSnippetContinuationFn = snippetCompiler.getCompiledSnippet(op)
        op.compiledSnippetContinuationFn(storageManager, Seq(sm => jit(op.body)))

      case op: DoWhileOp =>
        op.run_continuation(storageManager, Seq(sm => jit(op.body)))

      case op: SequenceOp =>
        op.code match
          case OpCode.EVAL_SN | OpCode.EVAL_NAIVE | OpCode.LOOP_BODY if jitOptions.granularity == op.code =>
            if (op.compiledSnippetContinuationFn == null)
              given staging.Compiler = dedicatedDotty
              op.compiledSnippetContinuationFn = snippetCompiler.getCompiledSnippet(op)
            println("about to call compiled code")
            op.compiledSnippetContinuationFn(storageManager, op.ops.map(o => sm => jit(o)))
          case _ =>
            op.run_continuation(storageManager, op.ops.map(o => sm => jit(o)))

      case op: SwapAndClearOp =>
        op.run_continuation(storageManager)

      case op: InsertOp if jitOptions.granularity == op.code =>
        if (op.compiledSnippetContinuationFn == null)
          given staging.Compiler = dedicatedDotty
          op.compiledSnippetContinuationFn = snippetCompiler.getCompiledSnippet(op)
        op.compiledSnippetContinuationFn(storageManager, Seq((sm: CollectionsStorageManager) => jitRel(op.subOp)) ++ op.subOp2.map(sop => (sm: CollectionsStorageManager) => jitRel(sop)))

      case op: InsertOp =>
        op.run_continuation(storageManager, Seq((sm: CollectionsStorageManager) => jitRel(op.subOp))/* ++ op.subOp2.map(sop => (sm: CollectionsStorageManager) => jitRel(sop))*/)
      case op: DebugNode =>
        op.run_continuation(storageManager)

      case _ =>
        irTree match {
          case op: IRRelOp => jitRel(op)
          case _ =>
            throw new Exception(s"Error: unhandled node type $irTree")
        }
    }
  }
}
