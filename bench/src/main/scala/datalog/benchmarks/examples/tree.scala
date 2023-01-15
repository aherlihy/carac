package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program, __}
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import java.nio.file.Paths
@Fork(1) // # of jvms that it will use
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class tree_benchmark() extends ExampleBenchmarkGenerator("tree") with tree {
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
trait tree {
  def toSolve: String = "S"
  def pretest(program: Program): Unit = {
    val S = program.relation[Constant]("S")

    val T = program.relation[Constant]("T")

    val R = program.relation[Constant]("R")

    val x1, x2, x3, x4, a = program.variable()

    S (x1, x3) :- ( T(x1, x2), R(x2, __, x3) )
    T (x1, x4) :- ( R(x1, __, x2), R(x2, __, x3), T(x3, x4) )
    T (x1, x3) :- ( R(x1, a, x2), R(x2, a, x3) )
    
    R("1", "a", "2") :- ()
    R("2", "b", "3") :- ()
    R("3", "a", "4") :- ()
    R("4", "a", "5") :- ()
    R("5", "a", "6") :- ()
  }
}
