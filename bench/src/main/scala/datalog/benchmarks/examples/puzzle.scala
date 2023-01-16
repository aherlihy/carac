package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program, __}

import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
@Fork(1) // # of jvms that it will use
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class puzzle_benchmark() extends ExampleBenchmarkGenerator("puzzle") with puzzle {
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
trait puzzle {

  def pretest(program: Program): Unit = {
    val opp = program.relation[Constant]("opp")

    val safe = program.relation[Constant]("safe")

    val state = program.relation[Constant]("state")

    val X, U, V, X1, Y = program.variable()

    state("n","n","n","n") :- ()

    state(X,X,U,V) :- (
      safe(X,X,U,V),
      opp(X,X1),
      state(X1,X1,U,V) )
    state(X,Y,X,V) :- (
      safe(X,Y,X,V),
      opp(X,X1),
      state(X1,Y,X1,V) )
    state(X,Y,U,X) :- (
      safe(X,Y,U,X),
      opp(X,X1),
      state(X1,Y,U,X1) )
    state(X,Y,U,V) :- (
      safe(X,Y,U,V),
      opp(X,X1),
      state(X1,Y,U,V) )
    
    opp("n","s") :- ()
    opp("s","n") :- ()

    safe("n","s","n","s") :- ()
    safe("n","n","n","n") :- ()
    safe("n","s","n","n") :- ()
    safe("n","n","n","s") :- ()
    safe("s","s","s","s") :- ()
    safe("s","n","s","n") :- ()
    safe("s","s","s","n") :- ()
    safe("s","n","s","s") :- ()
    
    safe(X,X,X1,X) :- opp(X,X1)
  }
}
