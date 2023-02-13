package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program, __}
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.anon_var.anon_var

import java.nio.file.Paths
@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_batchsize)
@Measurement(iterations = examples_iterations, time = examples_xl_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class anon_var_benchmark extends ExampleBenchmarkGenerator(
  "anon_var"
) with anon_var {
 @Setup
 def s(): Unit = setup() // can't add annotations to super, so just call

 @TearDown
 def f(): Unit = finish()

  // collections, seminaive
  @Benchmark def seminaive_collections(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveCollections"
    if(!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def ci_seminaive_staged_interpreted(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveStagedCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def ci_seminaive_staged(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveInterpretedStagedCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def ci_seminaive_staged_jit(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveJITStagedCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

}

import test.examples.anon_var.anon_var
