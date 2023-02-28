package datalog.benchmarks.examples

import datalog.benchmarks.ExampleBenchmarkGenerator
import datalog.dsl.{Constant, Program}

import java.nio.file.{Path, Paths}
import scala.util.Properties
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.ackermann.ackermann

@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_xl_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class ackermann_benchmark() extends ExampleBenchmarkGenerator(
  "ackermann"
) with ackermann {
  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

//  @Setup(Level.Invocation)
//  def s2(): Unit =
//    for i <- 0 until 200 do programs("JITStagedEvalRuleS1BS2BS3B").namedRelation(toSolve).solve()


  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  @Benchmark def interpreted(blackhole: Blackhole): Unit = {
    val p = "InterpretedCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_best_sortahead(blackhole: Blackhole): Unit = {
    val p = "InterpretedS2B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_worst_sortahead(blackhole: Blackhole): Unit = {
    val p = "InterpretedS2W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_best_sortahead_online(blackhole: Blackhole): Unit = {
    val p = "InterpretedS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }


  @Benchmark def interpreted_worst_presort_sortahead_online(blackhole: Blackhole): Unit = {
    val p = "InterpretedS1WS2WS3W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }
  
  // JIT
  @Benchmark def jit_evalRule(blackhole: Blackhole): Unit = {
    val p = "JITStagedEvalRuleCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_evalRule_worst_sortahead(blackhole: Blackhole): Unit = {
    val p = "JITStagedEvalRuleS2W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }


  @Benchmark def jit_unionSPJ_best_sortahead_online(blackhole: Blackhole): Unit = {
    val p = "JITStagedUnionSPJS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_fpj_best_sortahead_online(blackhole: Blackhole): Unit = {
    val p = "JITStagedFPJS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_evalRule_worst_sortahead_online(blackhole: Blackhole): Unit = {
    val p = "JITStagedEvalRuleS2WS3W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_evalRule_worst_presort_sortahead_online(blackhole: Blackhole): Unit = {
    val p = "JITStagedEvalRuleS1WS2WS3W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }


  @Benchmark def jit_evalRule_best_presort_sortahead_online_x(blackhole: Blackhole): Unit = {
    val p = "JITStagedEvalRuleS1BS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_evalRule_best_presort_sortahead_online_async_aot_x(blackhole: Blackhole): Unit = {
    val p = "JITStagedAsyncAOTEvalRuleS1BS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))

    @Benchmark def jit_evalRule_best_presort_sortahead_online_sync_aot_x(blackhole: Blackhole): Unit = {
      val p = "JITStagedAOTEvalRuleS1BS2BS3B"
      if (!programs.contains(p))
        throw new Exception(f"skip test $p for current env")
      blackhole.consume(run(programs(p), result))
    }
  }

  //  @Benchmark def jit_evalRule_best_sortahead_online_x(blackhole: Blackhole): Unit = {
//    val p = "JITStagedAsyncEvalRuleS2BS3B"
//    if (!programs.contains(p))
//      throw new Exception(f"skip test $p for current env")
//    blackhole.consume(run(programs(p), result))
//  }

//  @Benchmark def jit_evalRule_best_sortahead_x(blackhole: Blackhole): Unit = {
//    val p = "JITStagedEvalRuleS2B"
//    if (!programs.contains(p))
//      throw new Exception(f"skip test $p for current env")
//    blackhole.consume(run(programs(p), result))
//  }

//  @Benchmark def jit_unionSPJ_best_sortahead_online_x(blackhole: Blackhole): Unit = {
//    val p = "JITStagedUnionSPJS2BS3B"
//    if (!programs.contains(p))
//      throw new Exception(f"skip test $p for current env")
//    blackhole.consume(run(programs(p), result))
//  }
//
//  @Benchmark def jit_unionSPJ_best_sortahead_online_async_x(blackhole: Blackhole): Unit = {
//    val p = "JITStagedAsyncUnionSPJS2BS3B"
//    if (!programs.contains(p))
//      throw new Exception(f"skip test $p for current env")
//    blackhole.consume(run(programs(p), result))
//  }

  @Benchmark def interpreted_worst_sortahead_online_x(blackhole: Blackhole): Unit = {
    val p = "InterpretedS2WS3W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_best_presort_sortahead_online(blackhole: Blackhole): Unit = {
    val p = "InterpretedS1BS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

}