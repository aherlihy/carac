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
class tc_benchmark() extends ExampleBenchmarkGenerator("tc")  with tc {
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
trait tc {

  def pretest(program: Program): Unit = {
    val base = program.namedRelation[Constant]("base")

    val tc = program.relation[Constant]("tc")

    val tcl = program.relation[Constant]("tcl")

    val tcr = program.relation[Constant]("tcr")

    val X, Y, Z = program.variable()
    
    tcl(X, Y) :- ( base(X,Y) )
    tcl(X,Y) :- ( tcl(X,Z), base(Z,Y) )
    
    tcr(X,Y) :- ( base(X,Y) )
    tcr(X,Y) :- ( base(X, Z),tcr(Z,Y) )
    
    tc(X,Y) :- ( base(X,Y) )
    tc(X,Y) :- ( tc(X,Z),tc(Z,Y) )
    
    base("a","b") :- ()
    base("b","c") :- ()
    base("c","d") :- ()
  }
}
