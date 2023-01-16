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
class metro_benchmark() extends ExampleBenchmarkGenerator("metro") with metro {
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
trait metro {

  def pretest(program: Program): Unit = {
    val platform2_reach = program.relation[Constant]("platform2_reach")

    val St_Reachable = program.relation[Constant]("St_Reachable")
    val Li_Reachable = program.relation[Constant]("Li_Reachable")
    val link = program.relation[Constant]("link")

    val x, y, u, any, any1, z = program.variable()

    St_Reachable(x, y) :- ( link(any,x,y) )
    St_Reachable(x, y) :- ( St_Reachable(x, z), link(any, z, y) )
    Li_Reachable(x, u) :- ( St_Reachable(x, z), link(u, z, any) )
    
    platform2_reach("platform2", y) :- ( St_Reachable("platform2", y), link(any, any1, y) )
    
    link("4","platform1","platform2") :- ()
    link("4","platform2","platform3") :- ()
    link("4","platform3","platform4") :- ()
    link("1","platform4","platform5") :- ()
    link("1","platform5","platform6") :- ()
    link("1","platform7","platform8") :- ()
    link("9","platform8","platform9") :- ()
    link("9","platform9","platform10") :- ()
  }
}
