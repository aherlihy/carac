package datalog.benchmarks.examples

import datalog.benchmarks.ExampleBenchmarkGenerator
import datalog.dsl.{Constant, Program}
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.tastyslistlib.tastyslistlib as tastyslistlib_test

import java.nio.file.Paths
import java.util.concurrent.TimeUnit

@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class tastyslistlib() extends ExampleBenchmarkGenerator("tastyslistlib") with tastyslistlib_test {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // Mode: volcano, naive
  //  @Benchmark def naive_volcano__(blackhole: Blackhole): Unit = {
  //    // this is rancid but otherwise have to copy the method name twice, which is typo prone. Put extra stuff for runnign with a regex after __
  //    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
  //    if(!programs.contains(p))
  //      throw new Exception(f"Error: program for '$p' not found")
  //    blackhole.consume(run(programs(p), result))
  //  }
  //  @Benchmark def seminaive_volcano__ci(blackhole: Blackhole): Unit = {
  //    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
  //    if(!programs.contains(p))
  //      throw new Exception(f"Error: program for '$p' not found")
  //    blackhole.consume(run(programs(p), result))
  //  }
  //
  //  @Benchmark def naive_default__(blackhole: Blackhole): Unit = {
  //    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
  //    if(!programs.contains(p))
  //      throw new Exception(f"Error: program for '$p' not found")
  //    blackhole.consume(run(programs(p), result))
  //  }
  //  @Benchmark def seminaive_default__ci(blackhole: Blackhole): Unit = {
  //    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
  //    if (!programs.contains(p))
  //      throw new Exception(f"Error: program for '$p' not found")
  //    blackhole.consume(run(programs(p), result))
  //  }

  // Mode: compiled
  //  @Benchmark def compiled_default_unordered__(blackhole: Blackhole): Unit = {
  //    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
  //    if (!programs.contains(p))
  //      throw new Exception(f"Error: program for '$p' not found")
  //    blackhole.consume(run(programs(p), result))
  //  }


  // Mode: interpreted
  // Sort: unordered
  @Benchmark def interpreted_default_unordered__ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // Mode: interpreted
  // Sort: best 1 + 3
  @Benchmark def interpreted_default_bestsel13__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_default_bestintmax13__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_default_bestmixed13__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // Mode: jit

  // Granularity: EvalRuleBody

  // Sort: unordered
  @Benchmark def jit_default_unordered_blocking_EVALRULEBODY__ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_default_unordered_async_EVALRULEBODY__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // Sort: best 1 + 3
  @Benchmark def jit_default_bestsel13_blocking_EVALRULEBODY__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_default_bestsel13_async_EVALRULEBODY__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // Sort: best 1
  @Benchmark def jit_default_bestsel1_blocking_EVALRULEBODY__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_default_bestsel1_async_EVALRULEBODY__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // Granularity: EvalRule SEMI NAIVE

  // Sort: unordered
  @Benchmark def jit_default_unordered_blocking_EVALRULESN__ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_default_unordered_async_EVALRULESN__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // Sort: "best" 1 + 3
  @Benchmark def jit_default_bestsel13_blocking_EVALRULESN__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_default_bestsel13_async_EVALRULESN__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_default_bestintmax13_blocking_EVALRULESN__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_default_bestmixed13_blocking_EVALRULESN__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // Sort "best" 1
  @Benchmark def jit_default_bestsel1_blocking_EVALRULESN__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_default_bestsel1_async_EVALRULESN__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}