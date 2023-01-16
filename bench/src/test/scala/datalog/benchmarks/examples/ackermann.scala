package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program}

import java.nio.file.{Path, Paths}
import scala.util.Properties
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.ackermann.ackermann

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class ackermann_benchmark() extends ExampleBenchmarkGenerator(
 "ackermann",
 Set("Naive", "Relational"), // run only SemiNaiveColl
 Set("Slow", "CI")
) with ackermann {
 override def toSolve: String = super.toSolve
 @Setup
 def s(): Unit = setup() // can't add annotations to super, so just call

 @TearDown
 def f(): Unit = finish()

 // relational, seminaive
 @Benchmark def seminaive_collections(blackhole: Blackhole): Unit = {
   val p = "SemiNaiveCollections"
   if (!programs.contains(p))
     throw new Exception(f"skip test $p for current env")
   blackhole.consume(run(programs(p), result))
 }

 // staged, seminaive
 @Benchmark def ci_seminaive_staged(blackhole: Blackhole): Unit = {
  val p = "SemiNaiveStagedCollections"
  if(!programs.contains(p))
    throw new Exception(f"skip test $p for current env")
  blackhole.consume(run(programs(p), result))
 }
}