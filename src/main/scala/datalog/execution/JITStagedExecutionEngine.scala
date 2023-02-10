package datalog.execution

import datalog.dsl.{MODE, Term}
import datalog.execution.ir.*
import datalog.storage.{DB, KNOWLEDGE, StorageManager}
import datalog.tools.Debug.debug

abstract class JITStagedExecutionEngine(override val storageManager: StorageManager) extends StagedExecutionEngine(storageManager) {
  import storageManager.EDB
  override def interpretIR(irTree: IROp)(using ctx: InterpreterContext): Any = {
    given StorageManager = storageManager
    irTree match {
      case ProgramOp(body) =>
        interpretIR(body) // don't really need to call run since it doesn't do anything

      case op: DoWhileOp =>
        // start compile for body
        //        lazy val compiledBody: AtomicReference[CollectionsStorageManager => storageManager.EDB] = getCompiled(irTree, ctx)
        op.run(sm => interpretIR(op.body))

      case op: SwapAndClearOp =>
        op.run()

      case op: SequenceOp =>
        op.run(op.ops.map(o => sm => interpretIR(o)))

      case op: ScanOp =>
        op.run()

      case op: ScanEDBOp =>
        op.run()

      case op: JoinOp =>
        op.run(op.ops.map(o => sm => interpretIR(o)))

      case op: ProjectOp =>
        op.run(sm => interpretIR(op.subOp))

      case op: InsertOp =>
        op.run(sm => interpretIR(op.subOp), op.subOp2.map(sop => sm => interpretIR(sop)))

      case op: UnionOp =>
        op.run(op.ops.map(o => sm => interpretIR(o)))

      case op: DiffOp =>
        op.run(sm => interpretIR(op.lhs), sm => interpretIR(op.rhs))

      case op: DebugNode =>
        op.run()

      case op: DebugPeek =>
        op.run(sm => interpretIR(op.op))
    }
  }
}
