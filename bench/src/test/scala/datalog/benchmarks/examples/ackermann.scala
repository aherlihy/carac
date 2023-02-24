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

  @Benchmark def seminaive_collections_reduce_view(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveCollectionsReduceView"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def seminaive_collections_reduce_noview(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveCollectionsReduceNoView"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def seminaive_collections_fold_view(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveCollectionsFoldView"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def seminaive_collections_fold_noview(blackhole: Blackhole): Unit = {
    val p = "SemiNaiveCollectionsFoldNoView"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }


  @Benchmark def erb_staged_spju_collections(blackhole: Blackhole): Unit = {
    val p = "ERBStagedSPJUCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def erb_staged_spju_best_unsorted_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "ERBStagedSPJUBestUnsortedUnsorted"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def erb_staged_spju_worst_unsorted_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "ERBStagedSPJUWorstUnsortedUnsorted"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def erb_staged_spju_best_best_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "ERBStagedSPJUBestBestUnsorted"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def erb_staged_spju_worst_worst_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "ERBStagedSPJUWorstWorstUnsorted"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def erb_staged_spju_worst_worst_worst_collections(blackhole: Blackhole): Unit = {
    val p = "ERBStagedSPJUWorstWorstWorst"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def erb_staged_spju_best_best_best_collections(blackhole: Blackhole): Unit = {
    val p = "ERBStagedSPJUBestBestBest"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedStagedSPJUCollections"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_best_unsorted_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedStagedSPJUBestUnsortedUnsorted"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_best_best_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedStagedSPJUBestBestUnsorted"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_worst_worst_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedStagedSPJUWorstWorstUnsorted"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_worst_unsorted_unsorted_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedStagedSPJUWorstUnsortedUnsorted"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_worst_worst_worst_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedStagedSPJUWorstWorstWorst"
    if (!programs.contains(p))
      throw new Exception(f"skip test $p for current env")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_staged_spju_best_best_best_collections(blackhole: Blackhole): Unit = {
    val p = "InterpretedStagedSPJUBestBestBest"
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