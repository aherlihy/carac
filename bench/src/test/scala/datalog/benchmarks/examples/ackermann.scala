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

  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // relational, seminaive
  @Benchmark def seminaive_collections(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }


  @Benchmark def jit_staged_spju_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedUnionSPJCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_fpj_spju_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedFPJCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_best_unsorted_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedUnionSPJS1B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_worst_unsorted_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedUnionSPJS1W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_best_best_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedUnionSPJS1BS2B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_worst_worst_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedUnionSPJS1WS2W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_fpj_bbx_tn2_tv2_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedFPJ_TN2TV2_S1BS2B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_fpj_bbb_tn2_tv2_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedFPJ_TN2TV2_S1BS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_fpj_bbx_tn0_tv1_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedFPJ_TN0TV1_S1BS2B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_fpj_bbb_tn0_tv1_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedFPJ_TN0TV1_S1BS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_fpj_bbx_tn1_tv2_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedFPJ_TN1TV2_S1BS2B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_fpj_bbb_tn1_tv2_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedFPJ_TN1TV2_S1BS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_fpj_bbx_tn1_tv5_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedFPJ_TN1TV5_S1BS2B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_fpj_bbb_tn1_tv5_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedFPJ_TN1TV5_S1BS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_fpj_bbx_tn5_tv2_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedFPJ_TN5TV2_S1BS2B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_fpj_bbb_tn5_tv2_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedFPJ_TN5TV2_S1BS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }
  @Benchmark def jit_staged_spju_fpj_bbx_tn2_tv10_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedFPJ_TN2TV10_S1BS2B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_fpj_bbb_tn2_tv10_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedFPJ_TN2TV10_S1BS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_worst_worst_worst_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedUnionSPJS1WS2WS3W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_spju_best_best_best_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedUnionSPJS1BS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_best_sortahead_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedS2B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_worst_sortahead_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedS2W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_best_sortahead_online_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_worst_sortahead_online_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedS2WS3W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_best_presort_sortahead_online_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedS1BS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_worst_presort_sortahead_online_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedS1WS2WS3W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }


  @Benchmark def interpreted_staged_spju_best_unsorted_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedS1B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_best_best_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedS1BS2B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_worst_worst_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedS1WS2W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_worst_unsorted_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedS1W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_worst_worst_worst_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedS1WS2WS3W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_best_best_best_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedS1BS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def ci_staged_compiled(blackhole: Blackhole): Unit = {
    val p = "CompiledStagedCollections"
    if(!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }
  @Benchmark def ci_staged_interpreted(blackhole: Blackhole): Unit = {
    val p = "InterpretedStagedCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

}