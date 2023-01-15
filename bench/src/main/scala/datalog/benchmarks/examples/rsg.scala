package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program}

import java.nio.file.Path
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class rsg_benchmark() extends ExampleBenchmarkGenerator("rsg") with rsg {
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
trait rsg {
  def toSolve: String = "RSG"
  def pretest(program: Program): Unit = {
    val DOWN = program.relation[Constant]("DOWN")

    val FLAT = program.relation[Constant]("FLAT")

    val RSG = program.relation[Constant]("RSG")

    val UP = program.relation[Constant]("UP")

    val x, y, a, b = program.variable()
    
    RSG(x,y) :- FLAT(x,y)
    RSG(x,y) :- ( UP(x, a), RSG(b, a), DOWN(b, y) )
    
    UP("a","e") :- ()
    UP("a","f") :- ()
    UP("f","m") :- ()
    UP("g","n") :- ()
    UP("h","n") :- ()
    UP("i","o") :- ()
    UP("j","o") :- ()
    
    FLAT("g","f") :- ()
    FLAT("m","n") :- ()
    FLAT("m","o") :- ()
    FLAT("p","m") :- ()
    
    DOWN("l","f") :- ()
    DOWN("m","f") :- ()
    DOWN("g","b") :- ()
    DOWN("h","c") :- ()
    DOWN("i","d") :- ()
    DOWN("p","k") :- ()
  }
}
