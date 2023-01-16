package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program, __}
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.anon_var.anon_var

import java.nio.file.Paths
@Fork(1) // # of jvms that it will use
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class anon_var_benchmark extends ExampleBenchmarkGenerator(
  "anon_var",
  Set("Naive", "Relational"), // run only SemiNaiveCollections
  Set("Slow", "CI")
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
}

import test.examples.anon_var.anon_var
