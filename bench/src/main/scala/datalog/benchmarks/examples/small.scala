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
class small_benchmark() extends ExampleBenchmarkGenerator("small") with small {
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
trait small {
  def toSolve = "ancestor"
  def pretest(program: Program): Unit = {
    val ancestor = program.relation[Constant]("ancestor")

    val father = program.relation[Constant]("father")

    val mother = program.relation[Constant]("mother")

    val parent = program.relation[Constant]("parent")

    val X, Y, Z = program.variable()
    
    parent(X,Y) :- ( mother(X,Y) )
    parent(X,Y) :- ( father(X,Y) )
    ancestor(X,Y) :- ( parent(X,Y) )
    ancestor(X,Y) :- ( parent(X,Z), ancestor(Z,Y) )
    
    mother("claudette", "ann") :- ()
    mother("jeannette", "bill") :- ()
    mother("mireille", "john") :- ()
    father("john", "ann") :- ()
    father("john", "bill") :- ()
    father("jean-jacques", "alphonse") :- ()
    father("alphonse", "mireille") :- ()
    father("brad", "john") :- ()
  }
}
