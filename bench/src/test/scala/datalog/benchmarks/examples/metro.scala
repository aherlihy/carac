package datalog.benchmarks.examples

import datalog.benchmarks.ExampleBenchmarkGenerator
import datalog.dsl.{Constant, Program}

import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.metro.metro

@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_batchsize)
@Measurement(iterations = examples_iterations, time = examples_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class metro_benchmark() extends ExampleBenchmarkGenerator("metro") with metro {

  @Setup
  def s(): Unit = setup() // can't add annotations to super, so just call

  @TearDown(Level.Invocation)
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
  @Benchmark def ci_seminaive_collections(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveCollections"
    if(!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def ci_staged_compiled(blackhole: Blackhole): Unit = {
    val p = "CompiledStagedCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def staged_interpreted(blackhole: Blackhole): Unit = {
    val p = "InterpretedStagedCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_AOTNaiveEvalBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedAOTNaiveEvalBlockingCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_AOTSemiNaiveEvalBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedAOTSemiNaiveEvalBlockingCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_AOTLoopBodyBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedAOTLoopBodyBlockingCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_AOTSemiNaiveEvalNonBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedAOTSemiNaiveEvalNonBlockingCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_AOTLoopBodyNonBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedAOTLoopBodyNonBlockingCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_SemiNaiveEvalOnline(blackhole: Blackhole): Unit = {
    val p = "JITStagedSemiNaiveEvalOnlineCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_LoopBodyOnline(blackhole: Blackhole): Unit = {
    val p = "JITStagedLoopBodyOnlineCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_JITStagedSemiNaiveEvalBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedSemiNaiveEvalBlockingCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_JITStagedProgramBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedProgramBlockingCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_JITStagedJoinBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedJoinBlockingCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_JITStagedSnippetSemiNaiveEvalBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedSnippetSemiNaiveEvalBlockingCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_JITStagedSnippetProgramBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedSnippetProgramBlockingCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_staged_JITStagedSnippetJoinBlocking(blackhole: Blackhole): Unit = {
    val p = "JITStagedSnippetJoinBlockingCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }
}