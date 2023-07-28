package datalog.benchmarks.examples

import datalog.benchmarks.ExampleBenchmarkGenerator
import datalog.dsl.{Constant, Program}
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.tastyslistlibinverse.tastyslistlibinverse as tastyslistlibinverse_test

import java.nio.file.Paths
import java.util.concurrent.TimeUnit

@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class tastyslistlibinverse() extends ExampleBenchmarkGenerator("tastyslistlibinverse") with tastyslistlibinverse_test {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // volcano, naive
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

  // interpreted
  @Benchmark def interpreted_default_unordered__ci(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  // compiled
//  @Benchmark def compiled_default_unordered__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def interpreted_default_bestsel13__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def interpreted_default_worstsel13__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def interpreted_default_bestsel1__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def interpreted_default_worstsel1__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def interpreted_default_bestsel3__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def interpreted_default_worstsel3__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def interpreted_default_bestcrd13__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def interpreted_default_worstcrd13__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def interpreted_default_bestcrd1__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def interpreted_default_worstcrd1__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }

  // jit
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

  @Benchmark def jit_default_unordered_async_EVALRULEBODY_aot__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

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

  @Benchmark def jit_default_bestsel13_async_EVALRULEBODY_aot__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

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

  @Benchmark def jit_default_bestsel1_async_EVALRULEBODY_aot__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  //  @Benchmark def jit_default_worstsel13_blocking_EVALRULEBODY__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def jit_default_worstsel13_async_EVALRULEBODY__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }

//  @Benchmark def jit_default_bestsel13_blocking_EVALRULEBODY_aot__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }

//  @Benchmark def jit_default_worstsel13_blocking_EVALRULEBODY_aot__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def jit_default_worstsel13_async_EVALRULEBODY_aot__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }

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

  @Benchmark def jit_default_unordered_async_EVALRULESN_aot__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

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

  @Benchmark def jit_default_bestsel13_async_EVALRULESN_aot__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

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

  @Benchmark def jit_default_bestsel1_async_EVALRULESN_aot__(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }


  //  @Benchmark def jit_default_worstsel13_blocking_EVALRULESN__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def jit_default_worstsel13_async_EVALRULESN__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }

//  @Benchmark def jit_default_bestsel13_blocking_EVALRULESN_aot__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }

//  @Benchmark def jit_default_worstsel13_blocking_EVALRULESN_aot__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def jit_default_worstsel13_async_EVALRULESN_aot__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }

  //  @Benchmark def jit_SPJ_blocking_bestsel13__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def jit_SPJ_blocking_bestsel1__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }

//  @Benchmark def jit_SPJ_blocking_worstsel1__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }

//  @Benchmark def jit_SPJ_async_bestsel13__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def jit_SPJ_async_bestsel1__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }

//  @Benchmark def jit_SPJ_async_worstsel123__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }

//  @Benchmark def jit_EVALRULESN_blocking_bestsel13__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def jit_EVALRULESN_async_bestsel13__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def jit_EVALRULESN_blocking_worstsel123__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def jit_EVALRULESN_async_worstsel123__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def jit_EVALRULESN_blocking_bestsel23__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def jit_EVALRULESN_async_bestsel23__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    if (!programs.contains(p))
//      throw new Exception(f"Error: program for '$p' not found")
//    blackhole.consume(run(programs(p), result))
//  }
}