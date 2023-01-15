package datalog.benchmarks

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import datalog.dsl.*
import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.storage.{CollectionsStorageManager, RelationalStorageManager, NS}

import scala.collection.mutable
import scala.util.Random

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class Bench {
  val dummyStream = new java.io.PrintStream(_ => ())

  def runTest(init: DLBenchmark, program: Program, blackhole: Blackhole): Unit = {
    val r = init.run(program)
    blackhole.consume(
        r.solve()
    )
  }

//  // relational, naive
  @Benchmark def multiJoin_naive_relational(blackhole: Blackhole): Unit =
    given engine: ExecutionEngine = new NaiveExecutionEngine(new RelationalStorageManager())
//    val p = new Program(engine)
//    runTest(MultiJoin(), p, blackhole)
//
//  // relational, seminaive
//  @Benchmark def multiJoin_semiNaive_relational(blackhole: Blackhole): Unit =
//    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new RelationalStorageManager())
//    val p = new Program(engine)
//    runTest(MultiJoin(), p, blackhole)
//
//  // seminaive, coll
//  @Benchmark def multiJoin_semiNaive_collections(blackhole: Blackhole): Unit =
//    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new CollectionsStorageManager())
//    val p = new Program(engine)
//    runTest(MultiJoin(), p, blackhole)
//
//  // naive, coll
//  @Benchmark def multiJoin_naive_collections(blackhole: Blackhole): Unit =
//    given engine: ExecutionEngine = new NaiveExecutionEngine(new CollectionsStorageManager())
//    val p = new Program(engine)
//    runTest(MultiJoin(), p, blackhole)
}
