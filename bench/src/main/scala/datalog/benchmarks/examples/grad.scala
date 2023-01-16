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
class grad_benchmark() extends ExampleBenchmarkGenerator("grad") with grad {
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
trait grad {
  def toSolve: String = "grad"
  def pretest(program: Program): Unit = {
    val course = program.relation[Constant]("course")

    val grad = program.relation[Constant]("grad")

    val pre = program.relation[Constant]("pre")

    val student = program.relation[Constant]("student")

    val take = program.relation[Constant]("take")

    val Pre, Post, X, S = program.variable()

    student("adam") :- ()
    student("bob") :- ()
    student("pete") :- ()
    student("scott") :- ()
    student("tony") :- ()
    
    course("eng") :- ()
    course("his") :- ()
    course("lp") :- ()
    
    take("adam","eng") :- ()
    take("pete","his") :- ()
    take("pete","eng") :- ()
    take("scott","his") :- ()
    take("scott","lp") :- ()
    take("tony","his") :- ()
    
    pre("eng","lp") :- ()
    pre("hist","eng") :- ()
    
    pre(Pre,Post) :- ( pre(Pre,X), pre(X,Post) )
    
    grad(S) :- ( take(S,"his"), take(S,"eng") )
  }
}
