package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program}
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import java.nio.file.Paths
import java.util.concurrent.TimeUnit
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
  blackhole.consume(
   run(programs("NaiveRelational"), result)
  )
 }
 // relational, seminaive
 @Benchmark def seminaive_relational(blackhole: Blackhole): Unit = {
  blackhole.consume(
   run(programs("SemiNaiveRelational"), result)
  )
 }

 // collections, naive
 @Benchmark def naive_collections(blackhole: Blackhole): Unit = {
  blackhole.consume(
   run(programs("NaiveCollections"), result)
  )
 }
 // relational, seminaive
 @Benchmark def seminaive_collections(blackhole: Blackhole): Unit = {
  blackhole.consume(
   run(programs("SemiNaiveCollections"), result)
  )
 }

 // staged, naive
 @Benchmark def naive_staged(blackhole: Blackhole): Unit = {
  blackhole.consume(
   run(programs("NaiveStagedCollections"), result)
  )
 }

 // staged, seminaive
 @Benchmark def seminaive_staged(blackhole: Blackhole): Unit = {
  blackhole.consume(
   run(programs("SemiNaiveStagedCollections"), result)
  )
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
