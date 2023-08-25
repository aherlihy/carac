package datalog.benchmarks.examples

import datalog.benchmarks.ExampleBenchmarkGenerator
import datalog.dsl.{Constant, Program}
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.ackermann.{ackermann_optimized as ackermann_optimized_test, ackermann_worst as ackermann_worst_test}

import java.util.concurrent.TimeUnit

@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_xl_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class XXackermann_optimized_fuzz1() extends ExampleBenchmarkGenerator(
  "ackermann"
) with ackermann_optimized_test {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // Naming: every benchmark is called <engine>_<storage>_<sort-order>?_<async/blocking>?_<granularity>?_<backend>?_EOL<ci?>
  // All benchmark bodies should be identical


  // jit

  // -> ALL

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__1_async_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // For now skip most async
  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_async_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__1_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__1_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> RULE

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__1_async_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_async_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__1_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__1_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> DELTA

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_DELTA_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_DELTA_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}
@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_xl_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class XXackermann_optimized_fuzz2() extends ExampleBenchmarkGenerator(
  "ackermann"
) with ackermann_optimized_test {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // Naming: every benchmark is called <engine>_<storage>_<sort-order>?_<async/blocking>?_<granularity>?_<backend>?_EOL<ci?>
  // All benchmark bodies should be identical


  // jit

  // -> ALL

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__2_async_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // For now skip most async
  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_async_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__2_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__2_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> RULE

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__2_async_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_async_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__2_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__2_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> DELTA

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_DELTA_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_DELTA_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}
@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_xl_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class XXackermann_optimized_fuzz3() extends ExampleBenchmarkGenerator("ackermann") with ackermann_optimized_test {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // Naming: every benchmark is called <engine>_<storage>_<sort-order>?_<async/blocking>?_<granularity>?_<backend>?_EOL<ci?>
  // All benchmark bodies should be identical


  // jit

  // -> ALL

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__3_async_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // For now skip most async
  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_async_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__3_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__3_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> RULE

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__3_async_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_async_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__3_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__3_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> DELTA

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_DELTA_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_DELTA_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}

@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_xl_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class XXackermann_optimized_fuzz4() extends ExampleBenchmarkGenerator("ackermann") with ackermann_optimized_test {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // Naming: every benchmark is called <engine>_<storage>_<sort-order>?_<async/blocking>?_<granularity>?_<backend>?_EOL<ci?>
  // All benchmark bodies should be identical


  // jit

  // -> ALL

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__4_async_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // For now skip most async
  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_async_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__4_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__4_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> RULE

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__4_async_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_async_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__4_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__4_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> DELTA

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_DELTA_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_DELTA_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}


@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_xl_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class XXackermann_optimized_fuzz5() extends ExampleBenchmarkGenerator("ackermann") with ackermann_optimized_test {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // Naming: every benchmark is called <engine>_<storage>_<sort-order>?_<async/blocking>?_<granularity>?_<backend>?_EOL<ci?>
  // All benchmark bodies should be identical


  // jit

  // -> ALL

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__5_async_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // For now skip most async
  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_async_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__5_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__5_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> RULE

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__5_async_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_async_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__5_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__5_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> DELTA

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_DELTA_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_DELTA_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}

@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_xl_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class XXackermann_optimized_fuzz7() extends ExampleBenchmarkGenerator("ackermann") with ackermann_optimized_test {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // Naming: every benchmark is called <engine>_<storage>_<sort-order>?_<async/blocking>?_<granularity>?_<backend>?_EOL<ci?>
  // All benchmark bodies should be identical

  // volcano, naive
  @Benchmark def shallow_volcano_naive______EOL(blackhole: Blackhole): Unit = {
    // this is rancid but otherwise have to copy the method name twice, which is typo prone. Put extra stuff for runnign with a regex after _EOL
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def shallow_volcano_seminaive______EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def shallow_default_naive______EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def shallow_default_seminaive______EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // interpreted


  @Benchmark def interpreted_default_badluck__7____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_default_unordered__7____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // compiled
  @Benchmark def compiled_default_unordered__7___quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def compiled_default_unordered__7___bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def compiled_default_unordered__7___lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }


  // jit

  // -> ALL

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__7_async_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // For now skip most async
  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_async_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__7_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__7_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> RULE

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__7_async_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_async_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__7_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__7_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> DELTA

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_DELTA_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_DELTA_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}

@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class XXackermann_worst_fuzz1() extends ExampleBenchmarkGenerator("ackermann") with ackermann_worst_test {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // Naming: every benchmark is called <engine>_<storage>_<sort-order>?_<async/blocking>?_<granularity>?_<backend>?_EOL<ci?>
  // All benchmark bodies should be identical


  // jit

  // -> ALL

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__1_async_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // For now skip most async
  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_async_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__1_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__1_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> RULE

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__1_async_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_async_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__1_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__1_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> DELTA

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_DELTA_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_DELTA_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__1_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__1_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}

@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class XXackermann_worst_fuzz2() extends ExampleBenchmarkGenerator("ackermann") with ackermann_worst_test {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // Naming: every benchmark is called <engine>_<storage>_<sort-order>?_<async/blocking>?_<granularity>?_<backend>?_EOL<ci?>
  // All benchmark bodies should be identical


  // jit

  // -> ALL

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__2_async_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // For now skip most async
  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_async_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__2_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__2_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> RULE

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__2_async_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_async_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__2_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__2_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> DELTA

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_DELTA_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_DELTA_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__2_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__2_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}


@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class XXackermann_worst_fuzz3() extends ExampleBenchmarkGenerator("ackermann") with ackermann_worst_test {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // Naming: every benchmark is called <engine>_<storage>_<sort-order>?_<async/blocking>?_<granularity>?_<backend>?_EOL<ci?>
  // All benchmark bodies should be identical


  // jit

  // -> ALL

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__3_async_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // For now skip most async
  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_async_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__3_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__3_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> RULE

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__3_async_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_async_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__3_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__3_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> DELTA

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_DELTA_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_DELTA_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__3_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__3_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}


@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class XXackermann_worst_fuzz4() extends ExampleBenchmarkGenerator("ackermann") with ackermann_worst_test {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // Naming: every benchmark is called <engine>_<storage>_<sort-order>?_<async/blocking>?_<granularity>?_<backend>?_EOL<ci?>
  // All benchmark bodies should be identical


  // jit

  // -> ALL

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__4_async_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // For now skip most async
  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_async_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__4_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__4_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> RULE

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__4_async_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_async_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__4_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__4_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> DELTA

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_DELTA_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_DELTA_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__4_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__4_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}

@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class XXackermann_worst_fuzz5() extends ExampleBenchmarkGenerator("ackermann") with ackermann_worst_test {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // Naming: every benchmark is called <engine>_<storage>_<sort-order>?_<async/blocking>?_<granularity>?_<backend>?_EOL<ci?>
  // All benchmark bodies should be identical


  // jit

  // -> ALL

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__5_async_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // For now skip most async
  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_async_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__5_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__5_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> RULE

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__5_async_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_async_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__5_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__5_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> DELTA

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_DELTA_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_DELTA_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__5_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__5_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}

@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class XXackermann_worst_fuzz7() extends ExampleBenchmarkGenerator("ackermann") with ackermann_worst_test {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // Naming: every benchmark is called <engine>_<storage>_<sort-order>?_<async/blocking>?_<granularity>?_<backend>?_EOL<ci?>
  // All benchmark bodies should be identical

  // volcano, naive
  @Benchmark def shallow_volcano_naive______EOL(blackhole: Blackhole): Unit = {
    // this is rancid but otherwise have to copy the method name twice, which is typo prone. Put extra stuff for runnign with a regex after _EOL
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def shallow_volcano_seminaive______EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def shallow_default_naive______EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def shallow_default_seminaive______EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // interpreted


  @Benchmark def interpreted_default_badluck__7____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_default_unordered__7____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // compiled
  @Benchmark def compiled_default_unordered__7___quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def compiled_default_unordered__7___bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def compiled_default_unordered__7___lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }


  // jit

  // -> ALL

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__7_async_ALL_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // For now skip most async
  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_async_ALL_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__7_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_async_ALL_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__7_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_async_ALL_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> RULE

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---> async
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__7_async_RULE_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_async_RULE_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__7_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_async_RULE_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__7_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_async_RULE_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // -> DELTA

  // ---> blocking
  // ------> quotes & splices
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_DELTA_quotes_EOL_ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_DELTA_quotes_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> bytecode gen
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_DELTA_bytecode_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ------> lambda
  // ---------> sorted
  @Benchmark def jit_default_sel__7_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  // ---------> unsorted
  @Benchmark def jit_default_unordered__7_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}
