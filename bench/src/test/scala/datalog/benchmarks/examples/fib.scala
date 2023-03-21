package datalog.benchmarks.examples

import datalog.benchmarks.ExampleBenchmarkGenerator
import datalog.dsl.{Constant, Program}

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.fib.fib

import java.nio.file.Paths
@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_batchsize)
@Measurement(iterations = examples_iterations, time = examples_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class fib_benchmark() extends ExampleBenchmarkGenerator("fib", Set(), Set("CI")) with fib {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @Setup(Level.Invocation)
  def s2(): Unit = {
    for i <- 0 until 80 do
      programs("JITStagedEvalRuleS1BS2BS3B").namedRelation(toSolve).solve()
  }
  @TearDown(Level.Invocation)
  def f(): Unit = finish()

  // relational, naive
  @Benchmark def naive_relational(blackhole: Blackhole): Unit = {
    val p = "NaiveVolcano"
    if(!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }
  // relational, seminaive
  @Benchmark def seminaive_relational(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveVolcano"
    if(!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  // collections, naive
  @Benchmark def naive_collections(blackhole: Blackhole): Unit = {
    val p = "NaiveDefault"
    if(!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  // collections, seminaive
  @Benchmark def seminaive_collections(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }



  @Benchmark def jit_staged_spju_collections(blackhole: Blackhole): Unit = {
    val p = "JITStagedUnionSPJDefault"
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
    val p = "InterpretedDefault"
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
    val p = "CompiledStagedDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def ci_staged_interpreted(blackhole: Blackhole): Unit = {
    val p = "InterpretedStagedDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_AOTNaiveEvalBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedAOTNaiveEvalBlockingDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_AOTSemiNaiveEvalBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedAOTSemiNaiveEvalBlockingDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_AOTLoopBodyBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedAOTLoopBodyBlockingDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_AOTSemiNaiveEvalNonBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedAOTSemiNaiveEvalNonBlockingDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_AOTLoopBodyNonBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedAOTLoopBodyNonBlockingDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_SemiNaiveEvalOnline(blackhole: Blackhole): Unit = {
    val p = "JITStagedSemiNaiveEvalOnlineDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_LoopBodyOnline(blackhole: Blackhole): Unit = {
    val p = "JITStagedLoopBodyOnlineDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_JITStagedSemiNaiveEvalBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedSemiNaiveEvalBlockingDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_JITStagedProgramBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedProgramBlockingDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_JITStagedJoinBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedJoinBlockingDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_JITStagedSnippetSemiNaiveEvalBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedSnippetSemiNaiveEvalBlockingDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_JITStagedSnippetProgramBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedSnippetProgramBlockingDefault"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_JITStagedSnippetJoinBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedSnippetJoinBlockingDefault"
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
  }

  @Benchmark def interpreted_worst_sortahead_online_x(blackhole: Blackhole): Unit = {
    val p = "InterpretedS2WS3W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_worst_presort_sortahead_online_x(blackhole: Blackhole): Unit = {
    val p = "InterpretedS1WS2WS3W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_worst_sortahead_x(blackhole: Blackhole): Unit = {
    val p = "InterpretedS2W"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }


  @Benchmark def jit_evalRule_best_sortahead_online_x(blackhole: Blackhole): Unit = {
    val p = "JITStagedEvalRuleS2BS3B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_evalRule_best_sortahead_x(blackhole: Blackhole): Unit = {
    val p = "JITStagedEvalRuleS2B"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }
}