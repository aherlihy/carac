package datalog.benchmarks

import datalog.benchmarks.andersen.AndersenBenchmark
import datalog.dsl.*
import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.storage.{CollectionsStorageManager, NS, RelationalStorageManager}
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import java.util.concurrent.TimeUnit
import scala.collection.mutable
import scala.util.{Properties, Random}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class ExampleBench() {
  val dummyStream = new java.io.PrintStream(_ => ())
  var result: mutable.Map[String, Set[Seq[Term]]] = mutable.Map()
  var bench: ExampleBenchmarkGenerator = AndersenBenchmark()
  var programs: mutable.Map[String, Program] = mutable.Map()

  Seq("SemiNaive", "Naive", "NaiveStaged", "SemiNaiveStaged").foreach(execution =>
    Seq("Relational", "Collections").foreach(storage =>
      if (
        (execution.contains("Staged") && storage == "Relational") ||
        bench.skip.contains(execution) || bench.skip.contains(storage) ||
          (bench.tags ++ Set(execution, storage)).flatMap(t => Properties.envOrNone(t.toUpperCase())).nonEmpty
      ) {}
      else {
        programs(s"$execution$storage") = bench.initialize(s"$execution$storage")
      }))


  @Setup
  def init(): Unit = {
    result.clear()
  }

  @TearDown
  def finish(): Unit = {
    assert(result.nonEmpty && result(bench.toSolve).nonEmpty)
    assert(result(bench.toSolve) == bench.expectedFacts(bench.toSolve))
  }

  // relational, naive
  @Benchmark def naive_relational(blackhole: Blackhole): Unit = {
    blackhole.consume(
      bench.run(programs("NaiveRelational"), result)
    )
  }
  // relational, seminaive
  @Benchmark def seminaive_relational(blackhole: Blackhole): Unit = {
    blackhole.consume(
      bench.run(programs("SemiNaiveRelational"), result)
    )
  }
}
