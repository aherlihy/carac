package datalog.benchmarks

import datalog.dsl.{Constant, Program, Relation, Term, MODE}
import datalog.execution.{ExecutionEngine, SemiNaiveExecutionEngine, StagedExecutionEngine, ir, ConcreteStagedExecutionEngine}
import datalog.storage.CollectionsStorageManager
import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Fork, Level, Measurement, Mode, Scope, Setup, State, Warmup}
import org.openjdk.jmh.infra.Blackhole

import java.util.concurrent.TimeUnit
import scala.collection.mutable
import scala.util.Random

@Fork(staged_fork) // # of jvms that it will use
@Warmup(iterations = staged_warmup_iterations, time = staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@Measurement(iterations = staged_iterations, time = staged_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedInterpreted_full {
  var engine1: ConcreteStagedExecutionEngine = null
  var program1: Program = null
  var engine2: ConcreteStagedExecutionEngine = null
  var program2: Program = null

  var toSolve1: Relation[Constant] = null
  var toSolve2: Relation[Constant] = null
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine1 = ConcreteStagedExecutionEngine(CollectionsStorageManager())
    program1 = Program(engine1)
    engine2 = ConcreteStagedExecutionEngine(CollectionsStorageManager())
    program2 = Program(engine2)
    toSolve1 = initialize_8xJoin.pretest(program1)
    toSolve2 = initialize20x.pretest(program2)
  }
  // measure cost of tree gen, running interpreted
  @Benchmark def run_tree_8x(blackhole: Blackhole): Unit = {
    blackhole.consume(
      toSolve1.solve(MODE.Interpret)
    )
  }
  @Benchmark def run_run_8x(blackhole: Blackhole): Unit = {
    blackhole.consume(
      toSolve1.solve(MODE.InterpRun)
    )
  }

  @Benchmark def run_tree_20x(blackhole: Blackhole): Unit = {
    blackhole.consume(
      toSolve2.solve(MODE.Interpret)
    )
  }

  @Benchmark def run_run_20x(blackhole: Blackhole): Unit = {
    blackhole.consume(
      toSolve2.solve(MODE.InterpRun)
    )
  }
}
@Fork(staged_fork) // # of jvms that it will use
@Warmup(iterations = staged_warmup_iterations, time = staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@Measurement(iterations = staged_iterations, time = staged_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedInterpreted_no_tree {
  var engine1: ConcreteStagedExecutionEngine = null
  var program1: Program = null
  var engine2: ConcreteStagedExecutionEngine = null
  var program2: Program = null
  var toSolve1: Relation[Constant] = null
  var tree1: ir.IROp = null
  var ctx1: ir.InterpreterContext = null
  var toSolve2: Relation[Constant] = null
  var tree2: ir.IROp = null
  var ctx2: ir.InterpreterContext = null
//  var toSolve2: Relation[Constant] = null
//  var tree2: ir.IROp = null
//  var ctx2: ir.InterpreterContext = null

  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine1 = ConcreteStagedExecutionEngine(CollectionsStorageManager())
    program1 = Program(engine1)
    engine2 = ConcreteStagedExecutionEngine(CollectionsStorageManager())
    program2 = Program(engine2)
    toSolve1 = initialize_8xJoin.pretest(program1)
    toSolve2 = initialize20x.pretest(program2)
    val x1 = engine1.generateProgramTree(toSolve1.id)
    tree1 = x1._1
    ctx1 = x1._2

    val x2 = engine2.generateProgramTree(toSolve2.id)
    tree2 = x2._1
    ctx2 = x2._2

//    toSolve2 = initialize_8xJoin.pretest(program)
//    val x1 = engine1.generateProgramTree(toSolve1.id)
//    tree1 = x1._1
//    ctx1 = x1._2
  }
  //  measure cost of running interpreted only
  @Benchmark def run_tree_8x(blackhole: Blackhole): Unit = {
    blackhole.consume(
      engine1.solveInterpreted(tree1, ctx1)
    )
  }

  @Benchmark def run_run_8x(blackhole: Blackhole): Unit = {
    blackhole.consume(
      engine1.solveInterpreted_withRun(tree1, ctx1)
    )
  }

  @Benchmark def run_tree_20x(blackhole: Blackhole): Unit = {
    blackhole.consume(
      engine2.solveInterpreted(tree2, ctx2)
    )
  }

  @Benchmark def run_run_20x(blackhole: Blackhole): Unit = {
    blackhole.consume(
      engine2.solveInterpreted_withRun(tree2, ctx2)
    )
  }
}
