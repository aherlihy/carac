package datalog.execution

import datalog.dsl.{MODE, Term}
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
class JITStagedSnippetExecutionEngine(override val storageManager: CollectionsStorageManager,
                                       granularity: OpCode,
                                       aot: Boolean,
                                       block: Boolean) extends JITStagedExecutionEngine(storageManager, granularity, aot, block) {
  import storageManager.EDB
  val snippetCompiler: StagedSnippetCompiler = StagedSnippetCompiler(storageManager)
  override def solve(rId: Int, mode: MODE): Set[Seq[Term]] = super.solve(rId, MODE.Interpret)
  override def interpretIRRelOp(irTree: IRRelOp)(using ctx: InterpreterContext): storageManager.EDB = {
//    println(s"IN INTERPRET REL_IR, code=${irTree.code}")
    irTree match {
      case op: ScanOp =>
        op.runRel(storageManager)
//        if (op.compiledRelSnippetFn == null)
//          given staging.Compiler = dedicatedDotty
//          op.compiledRelSnippetFn = snippetCompiler.getCompiledRelSnippet(op, ctx, Seq.empty)
//        op.compiledRelSnippetFn(storageManager, Seq.empty)

      case op: ScanEDBOp =>
        op.runRel(storageManager)

      case op: JoinOp =>
//        op.runRel(storageManager, op.ops.map(o => sm => interpretIRRelOp(o)))
        if (op.compiledRelSnippetFn == null)
          given staging.Compiler = dedicatedDotty
          op.compiledRelSnippetFn = snippetCompiler.getCompiledRelSnippet(op, ctx, Seq.empty)
        op.compiledRelSnippetFn(storageManager, op.ops.map(o => sm => interpretIRRelOp(o)))

      case op: ProjectOp =>
        op.runRel(storageManager, Seq(sm => interpretIRRelOp(op.subOp)))

      case op: UnionOp =>
        op.runRel(storageManager, op.ops.map(o => sm => interpretIRRelOp(o)))

      case op: DiffOp =>
//        op.runRel(storageManager, Seq(sm => interpretIRRelOp(op.lhs), sm => interpretIRRelOp(op.rhs)))
        if (op.compiledRelSnippetFn == null)
          given staging.Compiler = dedicatedDotty
          op.compiledRelSnippetFn = snippetCompiler.getCompiledRelSnippet(op, ctx, Seq.empty)

        op.compiledRelSnippetFn(storageManager, Seq(sm => interpretIRRelOp(op.lhs), sm => interpretIRRelOp(op.rhs)))

      case op: DebugPeek =>
        op.runRel(storageManager, Seq(sm => interpretIRRelOp(op.op)))

      case _ => throw new Exception("Error: interpretRelOp called with unit operation")
    }
  }
  override def interpretIR(irTree: IROp)(using ctx: InterpreterContext): Any = {
//    println(s"IN INTERPRET IR, code=${irTree.code}")
    irTree match {
      case op: ProgramOp =>
        op.run(storageManager, Seq(sm => interpretIR(op.body)))

      case op: DoWhileOp =>
        op.run(storageManager, Seq(sm => interpretIR(op.body)))

      case op: SequenceOp =>
        op.run(storageManager, op.ops.map(o => sm => interpretIR(o)))

      case op: SwapAndClearOp =>
        op.run(storageManager)

      case op: InsertOp =>
        op.run(storageManager, Seq((sm: CollectionsStorageManager) => interpretIRRelOp(op.subOp)) ++ op.subOp2.map(sop => (sm: CollectionsStorageManager) => interpretIRRelOp(sop)))

      case op: DebugNode =>
        op.run(storageManager)

      case _ =>
        irTree match {
          case op: IRRelOp => interpretIRRelOp(op)
          case _ =>
            throw new Exception(s"Error: unhandled node type $irTree")
        }
    }
  }
}
