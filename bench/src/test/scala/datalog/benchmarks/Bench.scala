package datalog.benchmarks

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import datalog.dsl.*
import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.storage.{CollectionsStorageManager, NS, RelationalStorageManager}

import scala.collection.immutable.Map
import scala.util.Random

inline val staged_warmup_iterations = 0
inline val staged_iterations = 1
inline val staged_warmup_time = 10
inline val staged_time = 30
inline val staged_batchSize = 1
inline val staged_fork = 3
///**
// * Benchmarks that are run on all modes
// */
//@Fork(1) // # of jvms that it will use
//@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
//@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
//@State(Scope.Thread)
//class Bench_ci {
////  val dummyStream = new java.io.PrintStream(_ => ())
//  val ciBenchs: Map[String, DLBenchmark] = Map("tc" -> TransitiveClosure()) // for now just 1 for CI
//  Seq("SemiNaive", "Naive", "NaiveStaged", "SemiNaiveStaged").foreach(execution =>
//    Seq("Relational", "Collections").foreach(storage =>
//      if (!(execution.contains("Staged") && storage == "Relational"))
//        ciBenchs.values.foreach(b => b.programs(s"$execution$storage") = b.initialize(s"$execution$storage"))))
//
//  def runTest(benchmark: DLBenchmark, program: Program, blackhole: Blackhole): Unit =
//    blackhole.consume(
//      benchmark.run(program, benchmark.result)
//    )
//
//  @Setup
//  def s(): Unit = ciBenchs.values.foreach(b => b.setup()) // can't add annotations to superclass, so just call
//
//  @TearDown
//  def f(): Unit = ciBenchs.values.foreach(b => b.finish())
//
//  // TODO: find way to enumerate methods? macro annot?
//
//  // relational, naive
//  @Benchmark def naive_relational(blackhole: Blackhole): Unit = {
//    val p = "NaiveRelational"
//    val b = ciBenchs("tc")
//    runTest(b, b.programs(p), blackhole)
//  }
////   relational, seminaive
//  @Benchmark def seminaive_relational(blackhole: Blackhole): Unit = {
//    val p = "SemiNaiveRelational"
//    val b = ciBenchs("tc")
//    runTest(b, b.programs(p), blackhole)
//  }
//
//  // collections, naive
//  @Benchmark def naive_collections(blackhole: Blackhole): Unit = {
//    val p = "NaiveCollections"
//    val b = ciBenchs("tc")
//    runTest(b, b.programs(p), blackhole)
//  }
//  // relational, seminaive
//  @Benchmark def seminaive_collections(blackhole: Blackhole): Unit = {
//    val p = "SemiNaiveCollections"
//    val b = ciBenchs("tc")
//    runTest(b, b.programs(p), blackhole)
//  }
//
//  // staged, naive
//  @Benchmark def naive_staged(blackhole: Blackhole): Unit = {
//    val p = "NaiveStagedCollections"
//    val b = ciBenchs("tc")
//    runTest(b, b.programs(p), blackhole)
//  }
//
//  // staged, seminaive
//  @Benchmark def seminaive_staged(blackhole: Blackhole): Unit = {
//    val p = "SemiNaiveStagedCollections"
//    val b = ciBenchs("tc")
//    runTest(b, b.programs(p), blackhole)
//  }
//}
