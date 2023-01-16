package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program}
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import java.nio.file.Paths
@Fork(1) // # of jvms that it will use
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class func_benchmark() extends ExampleBenchmarkGenerator("func") with func {
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
trait func {

  def pretest(program: Program): Unit = {
    val eq = program.relation[Constant]("eq")
    val succ = program.relation[Constant]("succ")
    val f = program.relation[Constant]("f")
    val arg = program.relation[Constant]("arg")
    val args = program.relation[Constant]("args")
    val a, b, v, w, i, p, k, any1, any2 = program.variable()

    
    succ("1", "2") :- ()
    succ("2", "3") :- ()
    succ("3", "4") :- ()
    
    f("x", "g") :- ()
    f("y", "f") :- ()
    
    arg("x", "1", "A") :- ()
    arg("x", "2", "B") :- ()
    arg("x", "3", "Z") :- ()
    
    arg("y", "1", "C") :- ()
    arg("y", "2", "D") :- ()
    arg("y", "3", "W") :- ()
    
    eq(a, b) :- ( f(v, a), f(w, b), args(v, w, "3") )
    
    args(v, w, i) :- ( succ(p, i), arg(v, i, k), arg(w, i, k), args(v, w, p) )
    args(v, w, "1") :- ( arg(v, "1", any1), arg(w, "1", any2) )
  }
}
