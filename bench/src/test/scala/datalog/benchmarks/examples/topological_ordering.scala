package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program}

import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.topological_ordering.topological_ordering

@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = examples_iterations, time = examples_time, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class topological_ordering_benchmark() extends ExampleBenchmarkGenerator("topological_ordering")  with topological_ordering {
  override def toSolve: String = super.toSolve
  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown
  def f(): Unit = finish()

  // relational, naive
  @Benchmark def naive_relational(blackhole: Blackhole): Unit = {
    val p = "NaiveRelational"
    if(!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }
  // relational, seminaive
  @Benchmark def seminaive_relational(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveRelational"
    if(!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  // collections, naive
  @Benchmark def naive_collections(blackhole: Blackhole): Unit = {
    val p = "NaiveCollections"
    if(!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }
  // relational, seminaive
  @Benchmark def seminaive_collections(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveCollections"
    if(!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }


  // staged, naive
  @Benchmark def naive_staged(blackhole: Blackhole): Unit = {
    val p = "NaiveStagedCollections"
    if(!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  // staged, seminaive
  @Benchmark def ci_seminaive_staged_interpreted(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveStagedCollections"
    if(!programs.contains(p))
      throw new Exception(f"skip test $p for current env")

    blackhole.consume(run(programs(p), result))
  }
  @Benchmark def ci_seminaive_staged(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveInterpretedStagedCollections"
    if(!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }
  @Benchmark def ci_seminaive_staged_jit(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveJITStagedCollections"
    if(!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }
}
import test.examples.topological_ordering.topological_ordering
