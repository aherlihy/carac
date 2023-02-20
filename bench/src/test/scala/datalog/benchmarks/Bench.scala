package datalog.benchmarks

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import datalog.dsl.*
import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.storage.{CollectionsStorageManager, NS, RelationalStorageManager}

import scala.collection.immutable.Map
import scala.util.Random
/**
 * Benchmarks that are run on all modes
 */
@Fork(1) // # of jvms that it will use
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 1000)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 1000)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class Bench_ci {
//  val dummyStream = new java.io.PrintStream(_ => ())
  val ciBenchs: Map[String, DLBenchmark] = Map("tc" -> TransitiveClosure()) // for now just 1 for CI
  Seq("SemiNaive", "Naive", "NaiveCompiledStaged", "NaiveInterpretedStaged").foreach(execution =>
    Seq("Relational", "Collections").foreach(storage =>
      if (!(execution.contains("Staged") && storage == "Relational"))
        ciBenchs.values.foreach(b => b.programs(s"$execution$storage") = b.initialize(s"$execution$storage"))))

  def runTest(benchmark: DLBenchmark, program: Program, blackhole: Blackhole): Unit =
    blackhole.consume(
      benchmark.run(program, benchmark.result)
    )

  @Setup
  def s(): Unit = ciBenchs.values.foreach(b => b.setup()) // can't add annotations to superclass, so just call

  @TearDown
  def f(): Unit = ciBenchs.values.foreach(b => b.finish())

  // TODO: find way to enumerate methods? macro annot?

  // relational, naive
  @Benchmark def naive_relational(blackhole: Blackhole): Unit = {
    val p = "NaiveRelational"
    val b = ciBenchs("tc")
    runTest(b, b.programs(p), blackhole)
  }
//   relational, seminaive
  @Benchmark def seminaive_relational(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveRelational"
    val b = ciBenchs("tc")
    runTest(b, b.programs(p), blackhole)
  }

  // collections, naive
  @Benchmark def naive_collections(blackhole: Blackhole): Unit = {
    val p = "NaiveCollections"
    val b = ciBenchs("tc")
    runTest(b, b.programs(p), blackhole)
  }
  // relational, seminaive
  @Benchmark def seminaive_collections(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveCollections"
    val b = ciBenchs("tc")
    runTest(b, b.programs(p), blackhole)
  }

  // staged, naive
  @Benchmark def naive_staged_compiled(blackhole: Blackhole): Unit = {
    val p = "NaiveInterpretedStagedCollections"
    val b = ciBenchs("tc")
    runTest(b, b.programs(p), blackhole)
  }

  // staged, naive
  @Benchmark def naive_staged_interpreted(blackhole: Blackhole): Unit = {
    val p = "NaiveCompiledStagedCollections"
    val b = ciBenchs("tc")
    runTest(b, b.programs(p), blackhole)
  }
}
