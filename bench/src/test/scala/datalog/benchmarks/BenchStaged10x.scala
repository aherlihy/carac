package datalog.benchmarks

import datalog.dsl.{Constant, Program, Relation, Term, MODE}
import datalog.execution.{ExecutionEngine, SemiNaiveExecutionEngine, CompiledStagedExecutionEngine, ir}
import datalog.storage.CollectionsStorageManager
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import java.util.concurrent.TimeUnit
import scala.collection.mutable
import scala.util.Random

object initialize20x {
  def pretest(program: Program): Relation[Constant] = {
    val edge = program.relation[Constant]("edge")
    val path = program.relation[Constant]("path")
    val hops1 = program.relation[Constant]("hops1")
    val hops2 = program.relation[Constant]("hops2")
    val hops3 = program.relation[Constant]("hops3")
    val hops4 = program.relation[Constant]("hops4")
    val hops5 = program.relation[Constant]("hops5")
    val hops6 = program.relation[Constant]("hops6")
    val hops7 = program.relation[Constant]("hops7")
    val hops8 = program.relation[Constant]("hops8")
    val hops9 = program.relation[Constant]("hops9")
    val hops10 = program.relation[Constant]("hops10")
    val hops11 = program.relation[Constant]("hops11")
    val hops12 = program.relation[Constant]("hops12")
    val hops13 = program.relation[Constant]("hops13")
    val hops14 = program.relation[Constant]("hops14")
    val hops15 = program.relation[Constant]("hops15")
    val hops16 = program.relation[Constant]("hops16")
    val hops17 = program.relation[Constant]("hops17")
    val hops18 = program.relation[Constant]("hops18")
    val hops19 = program.relation[Constant]("hops19")
    val hops20 = program.relation[Constant]("hops20")

    val x, y, z, w, q = program.variable()

    path(x, y) :- edge(x, y)
    path(x, z) :- (edge(x, y), path(y, z))

    hops1(x, y) :- edge(x, y)
    hops2(x, y) :- (hops1(x, z), hops1(z, y))
    hops3(x, y) :- (hops1(x, z), hops2(z, y))
    hops4(x, y) :- (hops1(x, z), hops3(z, y))
    hops5(x, y) :- (hops1(x, z), hops4(z, y))
    hops6(x, y) :- (hops1(x, z), hops5(z, y))
    hops7(x, y) :- (hops1(x, z), hops6(z, y))
    hops8(x, y) :- (hops1(x, z), hops7(z, y))
    hops9(x, y) :- (hops1(x, z), hops8(z, y))
    hops10(x, y) :- (hops1(x, z), hops9(z, y))
    hops11(x, y) :- (hops1(x, z), hops10(z, y))
    hops12(x, y) :- (hops1(x, z), hops11(z, y))
    hops13(x, y) :- (hops1(x, z), hops12(z, y))
    hops14(x, y) :- (hops1(x, z), hops13(z, y))
    hops15(x, y) :- (hops1(x, z), hops14(z, y))
    hops16(x, y) :- (hops1(x, z), hops15(z, y))
    hops17(x, y) :- (hops1(x, z), hops16(z, y))
    hops18(x, y) :- (hops1(x, z), hops17(z, y))
    hops19(x, y) :- (hops1(x, z), hops18(z, y))
    hops20(x, y) :- (hops1(x, z), hops19(z, y))

    for i <- 0 until 200 do
      edge(
        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString,
        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString
      ) :- ()
    hops20
  }
}

@Fork(staged_fork) // # of jvms that it will use
@Warmup(iterations = staged_warmup_iterations, time = staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@Measurement(iterations = staged_iterations, time = staged_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStaged10x_full_compiled {
  var engine: CompiledStagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = CompiledStagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    toSolve = initialize20x.pretest(program)
  }
  // measure cost of tree gen, compiling, running
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      toSolve.solve(MODE.Compile)
    )
  }
}
@Fork(staged_fork) // # of jvms that it will use
@Warmup(iterations = staged_warmup_iterations, time = staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@Measurement(iterations = staged_iterations, time = staged_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStaged10x_compile_and_run {
  var engine: CompiledStagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.IROp = null
  var ctx: ir.InterpreterContext = null
  // measure cost of tree gen, compiling, running
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = CompiledStagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    toSolve = initialize20x.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1
    ctx = x1._2
  }
  //  measure cost of compiling, running
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      engine.solveCompiled(tree, ctx)
    )
  }
}
@Fork(staged_fork) // # of jvms that it will use
@Warmup(iterations = staged_warmup_iterations, time = staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@Measurement(iterations = staged_iterations, time = staged_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStaged10x_run_only_compiled {
  var engine: CompiledStagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.IROp = null
  var ctx: ir.InterpreterContext = null
  var compiled: CollectionsStorageManager => CollectionsStorageManager#EDB = null

  // measure cost of tree gen, compiling, running
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = CompiledStagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    toSolve = initialize20x.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1
    ctx = x1._2
    compiled = engine.preCompile(tree, ctx)
  }

  // measure cost of running compiled code
  @Benchmark def run(blackhole: Blackhole): Unit = {
    val e = engine
    blackhole.consume(
      e.solvePreCompiled(compiled, ctx)
    )
  }
}
@Fork(staged_fork) // # of jvms that it will use
@Warmup(iterations = staged_warmup_iterations, time = staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@Measurement(iterations = staged_iterations, time = staged_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStaged10x_full_interpreted {
  var engine: CompiledStagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = CompiledStagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    toSolve = initialize20x.pretest(program)
  }
  // measure cost of tree gen, running interpreted
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      toSolve.solve(MODE.Interpret)
    )
  }
}
@Fork(staged_fork) // # of jvms that it will use
@Warmup(iterations = staged_warmup_iterations, time = staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@Measurement(iterations = staged_iterations, time = staged_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStaged10x_run_only_interpreted {
  var engine: CompiledStagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.IROp = null
  var ctx: ir.InterpreterContext = null
  // measure cost of tree gen, compiling, running
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = CompiledStagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    toSolve = initialize20x.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1
    ctx = x1._2
  }
  //  measure cost of running interpreted only
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      engine.solveInterpreted(tree, ctx)
    )
  }
}
@Fork(staged_fork) // # of jvms that it will use
@Warmup(iterations = staged_warmup_iterations, time = staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@Measurement(iterations = staged_iterations, time = staged_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStaged10x_seminaive_collections {
  var engine: SemiNaiveExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = SemiNaiveExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    toSolve = initialize20x.pretest(program)
  }
  // measure cost of old solve
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      toSolve.solve()
    )
  }
}
