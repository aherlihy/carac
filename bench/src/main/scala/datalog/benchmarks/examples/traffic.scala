package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program}

import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
@Fork(1) // # of jvms that it will use
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class traffic_benchmark() extends ExampleBenchmarkGenerator("traffic") with traffic {
 override def toSolve: String = super.toSolve
 @Setup
 def s(): Unit = setup() // can't add annotations to super, so just call

 @TearDown
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
 @Benchmark def seminaive_collections(blackhole: Blackhole): Unit = {
  val p = "SemiNaiveCollections"
  if(!programs.contains(p))
    throw new Exception(f"skip test $p for current env")
  blackhole.consume(run(programs(p), result))
 }

 // staged, naive
 @Benchmark def naive_staged(blackhole: Blackhole): Unit = {
  val p = "NaiveStagedCollections"
  if(!programs.contains(p))
    throw new Exception(f"skip test $p for current env")
  blackhole.consume(run(programs(p), result))
 }

 // staged, seminaive
 @Benchmark def seminaive_staged(blackhole: Blackhole): Unit = {
  val p = "SemiNaiveStagedCollections"
  if(!programs.contains(p))
    throw new Exception(f"skip test $p for current env")
  blackhole.consume(run(programs(p), result))
 }
}
trait traffic {
  def toSolve: String = "crashes"
  def pretest(program: Program): Unit = {
    val crashable = program.relation[Constant]("crashable")
    val crashes = program.relation[Constant]("crashes")
    val intersect = program.relation[Constant]("intersect")
    val greenSignal = program.relation[Constant]("greenSignal")
    val hasTraffic = program.relation[Constant]("hasTraffic")

    val X, Y = program.variable()
    
    crashable(X, Y) :- ( intersect(X, Y), greenSignal(X), greenSignal(Y) )
    crashes(X) :- ( hasTraffic(X), crashable(X, Y), hasTraffic(Y) )
    crashes(X) :- ( hasTraffic(X), crashable(Y, X), hasTraffic(Y) )
    
    greenSignal("Abercrombie St") :- ()
    greenSignal("Cleveland St") :- ()
    greenSignal("Shepard St") :- ()
    greenSignal("Elizabeth St") :- ()
    greenSignal("Goulburn St") :- ()
    
    hasTraffic("Abercrombie St") :- ()
    hasTraffic("Lawson St") :- ()
    hasTraffic("Elizabeth St") :- ()
    hasTraffic("Goulburn St") :- ()
    
    intersect("Abercrombie St", "Lawson St") :- ()
    intersect("Cleveland St", "Shepard St") :- ()
    intersect("Elizabeth St", "Goulburn St") :- ()
  }
}
