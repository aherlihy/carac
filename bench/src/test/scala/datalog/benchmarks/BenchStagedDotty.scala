package datalog.benchmarks

import datalog.dsl.{Constant, Program, Relation}
import datalog.execution.{SemiNaiveStagedExecutionEngine, ir}
import datalog.storage.CollectionsStorageManager
import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Fork, Level, Measurement, Mode, Scope, Setup, State, Warmup}
import org.openjdk.jmh.infra.Blackhole

import java.util.concurrent.TimeUnit
import scala.quoted.staging

inline val dotty_staged_warmup_iterations = 0
inline val dotty_staged_iterations = 1
inline val dotty_staged_warmup_time = 10
inline val dotty_staged_time = 10
inline val dotty_staged_batchSize = 1
inline val dotty_staged_fork = 1
@Fork(dotty_staged_fork) // # of jvms that it will use
@Warmup(iterations = dotty_staged_warmup_iterations, time = dotty_staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@Measurement(iterations = dotty_staged_iterations, time = dotty_staged_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedDotty_cold {
  var engine: SemiNaiveStagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.IROp = null
  var ctx: ir.InterpreterContext = null
  var doWhile, naiveEval, snEval, snEvalRule, join, scan: ir.IROp = null

  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = SemiNaiveStagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    toSolve = initialize2.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1
    ctx = x1._2
    // manually pick out subtrees to compile
    naiveEval = tree.asInstanceOf[ir.ProgramOp].body.asInstanceOf[ir.SequenceOp].ops.head // insert + n-SPJU for all rels
    assert(naiveEval.fnLabel == ir.FnLabel.EVAL_NAIVE)
    doWhile = tree.asInstanceOf[ir.ProgramOp].body.asInstanceOf[ir.SequenceOp].ops(1) // main loop
    assert(doWhile.asInstanceOf[ir.DoWhileOp].body.fnLabel == ir.FnLabel.LOOP_BODY)
    snEval = doWhile.asInstanceOf[ir.DoWhileOp].body.asInstanceOf[ir.SequenceOp].ops(1) // insert + SPJU for all rels
    assert(snEval.fnLabel == ir.FnLabel.EVAL_SN)
    snEvalRule = snEval.asInstanceOf[ir.SequenceOp].ops.head // a insert SPJU + copy
    assert(snEvalRule.isInstanceOf[ir.SequenceOp])
    join = snEvalRule.asInstanceOf[ir.SequenceOp].ops.head.asInstanceOf[ir.InsertOp].subOp.asInstanceOf[ir.DiffOp].lhs.asInstanceOf[ir.UnionOp].ops.head // project+join
    assert(join.isInstanceOf[ir.ProjectOp])
    scan = join.asInstanceOf[ir.ProjectOp].subOp.asInstanceOf[ir.JoinOp].ops.head
    assert(scan.isInstanceOf[ir.ScanOp])

    given ir.InterpreterContext = ctx
    println("subTree=" + engine.storageManager.printer.printIR(snEvalRule))

  }
  @Benchmark def compile_all(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty
    blackhole.consume(
      engine.compiler.getCompiled(tree, ctx)
    )
  }
}
