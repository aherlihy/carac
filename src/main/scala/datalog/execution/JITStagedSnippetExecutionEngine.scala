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

class JITStagedSnippetExecutionEngine(override val storageManager: CollectionsStorageManager, granularity: OpCode, aot: Boolean, block: Boolean) extends StagedExecutionEngine(storageManager) {
  import storageManager.EDB
  val trees: mutable.Queue[ProgramOp] = mutable.Queue.empty
  override def solve(rId: Int, mode: MODE): Set[Seq[Term]] = super.solve(rId, MODE.Interpret)
  def interpretIRRelOp(irTree: IRRelOp)(using ctx: InterpreterContext): storageManager.EDB = {
    println(s"IN INTERPRET REL_IR, code=${irTree.code}")
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
        op.runRel(storageManager, op.ops.map(o => sm => interpretIRRelOp(o)))

      case op: ProjectOp =>
        op.runRel(storageManager, Seq(sm => interpretIRRelOp(op.subOp)))

      case op: UnionOp =>
        op.runRel(storageManager, op.ops.map(o => sm => interpretIRRelOp(o)))

      case op: DiffOp =>
        op.runRel(storageManager, Seq(sm => interpretIRRelOp(op.lhs), sm => interpretIRRelOp(op.rhs)))

      case op: DebugPeek =>
        op.runRel(storageManager, Seq(sm => interpretIRRelOp(op.op)))

      case _ => throw new Exception("Error: interpretRelOp called with unit operation")
    }
  }
  override def interpretIR(irTree: IROp)(using ctx: InterpreterContext): Any = {
    println(s"IN INTERPRET IR, code=${irTree.code}")
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
  // TODO: this could potentially go as a tree transform phase
  def aotCompile(tree: ProgramOp)(using ctx: InterpreterContext): Unit = {
    val subTree = tree.getSubTree(granularity)
    debug("", () => s"ahead-of-time compiling ${subTree.code}")
    given scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
    given staging.Compiler = dedicatedDotty

    subTree.compiledFn = Future {
      compiler.getCompiled(subTree, ctx)
    }
  }

  def waitForAll(): Unit = {
    debug(s"awaiting in aot=$aot gran=$granularity block=$block", () => trees.map(t => t.code).mkString("[", ", ", "]"))
    trees.foreach(t =>
      val subTree = t.getSubTree(granularity)
      if (subTree.compiledFn != null)
        try {
          Await.result(subTree.compiledFn, Duration.Inf)
        } catch {
          case e  => throw new Exception(s"Exception cleaning up compiler: ${e.getCause}")
        }
    )
    trees.clear()
  }
}
