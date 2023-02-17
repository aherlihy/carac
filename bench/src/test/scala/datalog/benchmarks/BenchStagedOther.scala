package datalog.benchmarks

import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.ir.CompiledFn
import datalog.execution.{ExecutionEngine, JITOptions, SemiNaiveExecutionEngine, StagedExecutionEngine, ir}
import datalog.storage.CollectionsStorageManager
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.andersen.andersen

import java.nio.file.{Files, Path, Paths}
import java.util.concurrent.TimeUnit
import scala.collection.immutable.Set
import scala.collection.mutable
import scala.util.Random
import scala.io.Source
import scala.jdk.StreamConverters.*
import scala.util.Properties

inline val staged_warmup_iterations = 10
inline val staged_iterations = 5
inline val staged_warmup_time = 10
inline val staged_time = 10
inline val staged_batchSize = 10
inline val staged_fork = 1

trait OtherBench extends andersen {
  val inputFacts: mutable.Map[String, Seq[Seq[Term]]] = mutable.Map()
  val expectedFacts: mutable.Map[String, Set[Seq[Term]]] = mutable.Map()
  var result: mutable.Map[String, Set[Seq[Term]]] = mutable.Map()
  val directory = Paths.get("..", "src", "test", "scala", "test", "examples", "andersen")

  private val factdir = Paths.get(directory.toString, "facts")
  if (Files.exists(factdir))
    Files.walk(factdir, 1)
      .filter(p => Files.isRegularFile(p))
      .forEach(f => {
        val edbName = f.getFileName.toString.replaceFirst("[.][^.]+$", "")
        val reader = Files.newBufferedReader(f)
        val headers = reader.readLine().split("\t")
        val edbs = reader.lines()
          .map(l => {
            val factInput = l
              .split("\t")
              .zipWithIndex.map((s, i) =>
              (headers(i) match {
                case "Int" => s.toInt
                case "String" => s
                case _ => throw new Exception(s"Unknown type ${headers(i)}")
              }).asInstanceOf[Term]
            ).toSeq
            if (factInput.length != headers.size)
              throw new Exception(s"Input data for fact of length ${factInput.length} but should be ${headers.mkString("[", ", ", "]")}. Line='$l'")
            factInput
          }).toScala(Seq)
        reader.close()
        inputFacts(edbName) = edbs
      })
  // Generate expected
  private val expDir = Paths.get(directory.toString, "expected")
  if (!Files.exists(expDir)) throw new Exception(s"Missing expected directory '$expDir'")
  Files.walk(expDir, 1)
    .filter(p => Files.isRegularFile(p) && p.toString.endsWith(".csv"))
    .forEach(f => {
      val rule = f.getFileName.toString.replaceFirst("[.][^.]+$", "")
      val reader = Files.newBufferedReader(f)
      val headers = reader.readLine().split("\t")
      val expected = reader.lines()
        .map(l => l.split("\t").zipWithIndex.map((s, i) =>
          (headers(i) match {
            case "Int" => s.toInt
            case "String" => s
            case _ => throw new Exception(s"Unknown type ${headers(i)}")
          }).asInstanceOf[Term]
        ).toSeq)
        .toScala(Set)
      expectedFacts(rule) = expected
      reader.close()
    })

  override def pretest(program: Program): Unit = {
    inputFacts.foreach((edbName, factInput) =>
      val fact = program.relation[Constant](edbName)
      factInput.foreach(f => fact(f: _*) :- ())
      if (factInput.isEmpty) {
        val edbs = program.ee.storageManager.edbs.asInstanceOf[mutable.Map[Int, Any]]
      }
    )
    super.pretest(program)
  }
  def finish(): Unit = {
    assert(result.nonEmpty)
    if (toSolve != "_") { // solve for one relation, check all expected
      assert(result(toSolve) == expectedFacts(toSolve))
    } else { // solve all relations for their expected
      expectedFacts.foreach((fact, expected) => {
        assert(result(fact) == expectedFacts(fact))
      })
    }
  }
}

@Fork(staged_fork) // # of jvms that it will use
@Warmup(iterations = staged_warmup_iterations, time = staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@Measurement(iterations = staged_iterations, time = staged_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedOther_full_compiled extends OtherBench {
  var engine: StagedExecutionEngine = null
  var program: Program = null
  var toSolveR: Relation[Constant] = null
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = StagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    pretest(program)
    toSolveR = program.namedRelation(toSolve)
  }
  @TearDown
  def f(): Unit = finish()

  // measure cost of tree gen, compiling, running
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      result(toSolve) = toSolveR.solve()
    )
  }
}
@Fork(staged_fork) // # of jvms that it will use
@Warmup(iterations = staged_warmup_iterations, time = staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@Measurement(iterations = staged_iterations, time = staged_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedOther_compile_and_run extends OtherBench {
  var engine: StagedExecutionEngine = null
  var program: Program = null
  var toSolveR: Relation[Constant] = null
  var tree: ir.IROp = null
  var ctx: ir.InterpreterContext = null
  // measure cost of tree gen, compiling, running
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = StagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    pretest(program)
    toSolveR = program.namedRelation(toSolve)
    val x1 = engine.generateProgramTree(toSolveR.id)
    tree = x1._1
    ctx = x1._2
  }

  @TearDown
  def f(): Unit = finish()
  //  measure cost of compiling, running
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      result(toSolve) = engine.solveCompiled(tree, ctx)
    )
  }
}
@Fork(staged_fork) // # of jvms that it will use
@Warmup(iterations = staged_warmup_iterations, time = staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@Measurement(iterations = staged_iterations, time = staged_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedOther_run_only_compiled extends OtherBench {
  var engine: StagedExecutionEngine = null
  var program: Program = null
  var toSolveR: Relation[Constant] = null
  var tree: ir.IROp = null
  var ctx: ir.InterpreterContext = null
  var compiled: CompiledFn = null

  // measure cost of tree gen, compiling, running
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = StagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    pretest(program)
    toSolveR = program.namedRelation(toSolve)

    val x1 = engine.generateProgramTree(toSolveR.id)
    tree = x1._1
    ctx = x1._2
    compiled = engine.preCompile(tree)
  }

  @TearDown
  def f(): Unit = finish()

  // measure cost of running compiled code
  @Benchmark def run(blackhole: Blackhole): Unit = {
    val e = engine
    blackhole.consume(
      result(toSolve) = e.solvePreCompiled(compiled, ctx)
    )
  }
}
@Fork(staged_fork) // # of jvms that it will use
@Warmup(iterations = staged_warmup_iterations, time = staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@Measurement(iterations = staged_iterations, time = staged_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedOther_full_interpreted extends OtherBench {
  var engine: StagedExecutionEngine = null
  var program: Program = null
  var toSolveR: Relation[Constant] = null
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = StagedExecutionEngine(CollectionsStorageManager(), JITOptions(ir.OpCode.OTHER))
    program = Program(engine)
    pretest(program)
    toSolveR = program.namedRelation(toSolve)
  }

  @TearDown
  def f(): Unit = finish()
  // measure cost of tree gen, running interpreted
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      result(toSolve) = toSolveR.solve()
    )
  }
}
@Fork(staged_fork) // # of jvms that it will use
@Warmup(iterations = staged_warmup_iterations, time = staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@Measurement(iterations = staged_iterations, time = staged_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedOther_run_only_interpreted extends OtherBench {
  var engine: StagedExecutionEngine = null
  var program: Program = null
  var toSolveR: Relation[Constant] = null
  var tree: ir.IROp = null
  var ctx: ir.InterpreterContext = null
  // measure cost of tree gen, compiling, running
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = StagedExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    pretest(program)
    toSolveR = program.namedRelation(toSolve)
    val x1 = engine.generateProgramTree(toSolveR.id)
    tree = x1._1
    ctx = x1._2
  }

  @TearDown
  def f(): Unit = finish()
  //  measure cost of running interpreted only
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      result(toSolve) = engine.solveInterpreted(tree, ctx)
    )
  }
}
@Fork(staged_fork) // # of jvms that it will use
@Warmup(iterations = staged_warmup_iterations, time = staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@Measurement(iterations = staged_iterations, time = staged_time, timeUnit = TimeUnit.SECONDS, batchSize = staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedOther_seminaive_collections extends OtherBench {
  var engine: SemiNaiveExecutionEngine = null
  var program: Program = null
  var toSolveR: Relation[Constant] = null
  @Setup(Level.Invocation)
  def setup(): Unit = {
    engine = SemiNaiveExecutionEngine(CollectionsStorageManager())
    program = Program(engine)
    pretest(program)
    toSolveR = program.namedRelation(toSolve)
  }

  @TearDown
  def f(): Unit = finish()
  // measure cost of old solve
  @Benchmark def run(blackhole: Blackhole): Unit = {
    blackhole.consume(
      result(toSolve) = toSolveR.solve()
    )
  }
}
