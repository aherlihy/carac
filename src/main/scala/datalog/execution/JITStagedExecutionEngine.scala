package datalog.execution

import datalog.dsl.{MODE, Term}
import datalog.execution.ir.*
import datalog.storage.{CollectionsStorageManager, DB, KNOWLEDGE, StorageManager}
import datalog.tools.Debug.debug

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.quoted.staging
import scala.util.{Failure, Success}

abstract class JITStagedExecutionEngine(override val storageManager: CollectionsStorageManager) extends StagedExecutionEngine(storageManager) {
  import storageManager.EDB
  def interpretIRRelOp(irTree: IRRelOp)(using ctx: InterpreterContext): storageManager.EDB = {
//    println(s"IN INTERPRET REL_IR, code=${irTree.code}")
    given CollectionsStorageManager = storageManager
    irTree match {
      case op: ScanOp =>
        op.run(storageManager)

      case op: ScanEDBOp =>
        op.run()

      case op: JoinOp =>
        op.run(op.ops.map(o => sm => interpretIRRelOp(o)))

      case op: ProjectOp =>
        op.run(sm => interpretIRRelOp(op.subOp))

      case op: UnionOp =>
        op.run(op.ops.map(o => sm => interpretIRRelOp(o)))

      case op: DiffOp =>
        op.run(sm => interpretIRRelOp(op.lhs), sm => interpretIRRelOp(op.rhs))

      case op: DebugPeek =>
        op.run(sm => interpretIRRelOp(op.op))

      case _ => throw new Exception("Error: interpretRelOp called with unit operation")
    }
  }
  override def interpretIR(irTree: IROp)(using ctx: InterpreterContext): Any = {
//    println(s"IN INTERPRET IR, code=${irTree.code} and fnCode=${irTree.fnCode}")
    given CollectionsStorageManager = storageManager
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
        op.fnCode match
          case FnLabel.EVAL_SN =>
            // test if need to compile, if so:
            if (op.compiledFn == null) { // need to start compilation
              debug("starting new compilation", () => "")
              given scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

              op.compiledFn = Future {
                given staging.Compiler = dedicatedDotty //staging.Compiler.make(getClass.getClassLoader) // TODO: new dotty per thread, maybe concat
                compiler.getCompiled(op, ctx)
              }
              // TODO: time op.run to set baseline, then time compiled and de-optimize if slower
              op.run(op.ops.map(o => sm => interpretIR(o)))
            } else {
              op.compiledFn.value match {
                case Some(Success(op)) =>
                  debug("COMPILED", () => "")
                  op(storageManager)
                case Some(Failure(e)) =>
                  throw e
                case None =>
//                  Thread.sleep(10000)
                  debug("compilation not ready yet", () => "")
                  op.run(op.ops.map(o => sm => interpretIR(o)))
              }
            }
          case _ =>
            op.run(op.ops.map(o => sm => interpretIR(o)))

      case op: InsertOp =>
        op.run(sm => interpretIRRelOp(op.subOp), op.subOp2.map(sop => sm => interpretIRRelOp(sop)))

      case op: DebugNode =>
        op.run()

      case _ =>
        irTree match {
          case op: IRRelOp => interpretIRRelOp(op)
          case _ =>
            throw new Exception(s"Error: unhandled node type $irTree")
        }
    }
  }
}
