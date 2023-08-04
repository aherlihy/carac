//package datalog.benchmarks
//
//import java.util.concurrent.TimeUnit
//import org.openjdk.jmh.annotations.*
//import org.openjdk.jmh.infra.Blackhole
//import datalog.dsl.*
//import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
//import datalog.storage.{DefaultStorageManager, NS, VolcanoStorageManager}
//
//import scala.collection.immutable.Map
//import scala.util.Random
///**
// * Benchmarks that are run on all modes
// */
//@Fork(1) // # of jvms that it will use
//@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 100)
//@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 100)
//@State(Scope.Thread)
//@BenchmarkMode(Array(Mode.AverageTime))
//class Bench_ci {
//  //  val dummyStream = new java.io.PrintStream(_ => ())
//  val ciBenchs: Map[String, DLBenchmark] = Map("tc" -> TransitiveClosure()) // for now just 1 for CI
//  ciBenchs.values.foreach(b =>
//    b.initAllEngines()
//    b.programs.values.foreach(p => b.loadData(p))
//  )
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
//  @Benchmark def naive_volcano__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    val b = ciBenchs("tc")
//    runTest(b, b.programs(p), blackhole)
//  }
//
//  //   relational, seminaive
//  @Benchmark def seminaive_volcano__ci(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    val b = ciBenchs("tc")
//    runTest(b, b.programs(p), blackhole)
//  }
//
//  // collections, naive
//  @Benchmark def naive_default__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    val b = ciBenchs("tc")
//    runTest(b, b.programs(p), blackhole)
//  }
//
//  // relational, seminaive
//  @Benchmark def seminaive_default__ci(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    val b = ciBenchs("tc")
//    runTest(b, b.programs(p), blackhole)
//  }
//
//  // staged, naive
//  @Benchmark def compiled_default_unordered__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    val b = ciBenchs("tc")
//    runTest(b, b.programs(p), blackhole)
//  }
//
//  // staged, naive
//  @Benchmark def interpreted_default_unordered__ci(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    val b = ciBenchs("tc")
//    runTest(b, b.programs(p), blackhole)
//  }
//
//  // jit
//  @Benchmark def jit_default_unordered_blocking_EVALRULESN__ci(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    val b = ciBenchs("tc")
//    runTest(b, b.programs(p), blackhole)
//  }
//
//  @Benchmark def jit_default_unordered_async_EVALRULESN__(blackhole: Blackhole): Unit = {
//    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("__").head}"
//    val b = ciBenchs("tc")
//    runTest(b, b.programs(p), blackhole)
//  }
//
//}
