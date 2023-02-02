package datalog.benchmarks

import datalog.dsl.{Constant, Program, Relation, Term, MODE}
import datalog.execution.{ExecutionEngine, SemiNaiveExecutionEngine, SemiNaiveStagedExecutionEngine, ir}
import datalog.storage.CollectionsStorageManager
import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Fork, Level, Measurement, Mode, Scope, Setup, State, Warmup}
import org.openjdk.jmh.infra.Blackhole

import java.util.concurrent.TimeUnit
import scala.collection.mutable
import scala.util.Random

object initialize {
  def pretest(program: Program): Relation[Constant] = {
    val edge = program.relation[Constant]("edge")
    val path = program.relation[Constant]("path")
    val hops1 = program.relation[Constant]("hops1")
    val hops2_join = program.relation[Constant]("hops2_join")
    val hops3_join = program.relation[Constant]("hops3_join")
    val hops4_join = program.relation[Constant]("hops4_join")
    val hops5_join = program.relation[Constant]("hops5_join")
    //    val hops6_join = program.relation[Constant]("hops6_join")
    //    val hops7_join = program.relation[Constant]("hops7_join")
    //    val hops8_join = program.relation[Constant]("hops8_join")
    //    val hops9_join = program.relation[Constant]("hops9_join")
    //    val hops10_join = program.relation[Constant]("hops10_join")

    val x, y, z, w, q = program.variable()
    val a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11 = program.variable()

    path(x, y) :- edge(x, y)
    path(x, z) :- (edge(x, y), path(y, z))

    hops1(x, y) :- edge(x, y)
    hops2_join(a1, a3) :-   (hops1(a1, a2), hops1(a2, a3))
    hops3_join(a1, a4) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4))
    hops4_join(a1, a5) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5))
    hops5_join(a1, a6) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6))
    //    hops6_join(a1, a7) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7))
    //    hops7_join(a1, a8) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8))
    //    hops8_join(a1, a9) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8), hops1(a8, a9))
    //    hops9_join(a1, a10) :-  (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8), hops1(a8, a9), hops1(a9, a10))
    //    hops10_join(a1, a11) :- (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8), hops1(a8, a9), hops1(a9, a10), hops1(a10, a11))


    for i <- 0 until 200 do
      edge(
        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString,
        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString
      ) :- ()
    hops5_join
  }
}

@Fork(fork) // # of jvms that it will use
@Warmup(iterations = warmup_iterations, time = warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = batchSize)
@Measurement(iterations = iterations, time = time, timeUnit = TimeUnit.SECONDS, batchSize = batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStaged5xJoin_full_compiled {
  var engine: SemiNaiveStagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = SemiNaiveStagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    toSolve = initialize.pretest(program)
    Thread.sleep(10000)
  }
  // measure cost of tree gen, compiling, running
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      toSolve.solve(MODE.Compile)
    )
  }
}
@Fork(fork) // # of jvms that it will use
@Warmup(iterations = warmup_iterations, time = warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = batchSize)
@Measurement(iterations = iterations, time = time, timeUnit = TimeUnit.SECONDS, batchSize = batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStaged5xJoin_compile_and_run {
  var engine: SemiNaiveStagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.IROp = null
  var ctx: ir.InterpreterContext = null
  // measure cost of tree gen, compiling, running
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = SemiNaiveStagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    toSolve = initialize.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1
    ctx = x1._2
  }
  //  measure cost of compiling, running
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      engine.compileAndRun(tree, ctx)
    )
  }
}
@Fork(fork) // # of jvms that it will use
@Warmup(iterations = warmup_iterations, time = warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = batchSize)
@Measurement(iterations = iterations, time = time, timeUnit = TimeUnit.SECONDS, batchSize = batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStaged5xJoin_run_only_compiled {
  var engine: SemiNaiveStagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.IROp = null
  var ctx: ir.InterpreterContext = null
  var compiled: CollectionsStorageManager => Any = null

  // measure cost of tree gen, compiling, running
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = SemiNaiveStagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    toSolve = initialize.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1
    ctx = x1._2
    compiled = engine.getCompiled(tree, ctx)
  }

  // measure cost of running compiled code
  @Benchmark def run(blackhole: Blackhole): Unit = {
    val e = engine
    blackhole.consume(
      e.solvePreCompiled(compiled.asInstanceOf[CollectionsStorageManager => e.storageManager.EDB], ctx)
    )
  }
}
@Fork(fork) // # of jvms that it will use
@Warmup(iterations = warmup_iterations, time = warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = batchSize)
@Measurement(iterations = iterations, time = time, timeUnit = TimeUnit.SECONDS, batchSize = batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStaged5xJoin_full_interpreted {
  var engine: SemiNaiveStagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = SemiNaiveStagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    toSolve = initialize.pretest(program)
  }
  // measure cost of tree gen, running interpreted
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      toSolve.solve(MODE.Interpret)
    )
  }
}
@Fork(fork) // # of jvms that it will use
@Warmup(iterations = warmup_iterations, time = warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = batchSize)
@Measurement(iterations = iterations, time = time, timeUnit = TimeUnit.SECONDS, batchSize = batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStaged5xJoin_run_only_interpreted {
  var engine: SemiNaiveStagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.IROp = null
  var ctx: ir.InterpreterContext = null
  // measure cost of tree gen, compiling, running
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = SemiNaiveStagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    toSolve = initialize.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1
    ctx = x1._2
  }
  //  measure cost of running interpreted only
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      engine.solvePreInterpreted(tree, ctx)
    )
  }
}
@Fork(fork) // # of jvms that it will use
@Warmup(iterations = warmup_iterations, time = warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = batchSize)
@Measurement(iterations = iterations, time = time, timeUnit = TimeUnit.SECONDS, batchSize = batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStaged5xJoin_old {
  var engine: SemiNaiveExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = SemiNaiveExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    toSolve = initialize.pretest(program)
  }
  // measure cost of old solve
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      toSolve.solve()
    )
  }
}