package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program}
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import java.nio.file.Paths

@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_batchsize)
@Measurement(iterations = examples_iterations, time = examples_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class clique_benchmark() extends ExampleBenchmarkGenerator("clique") with clique {
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
 @Benchmark def ci_seminaive_staged_interpreted(blackhole: Blackhole): Unit = {
   val p = "SemiNaiveStagedCollections"
   if (!programs.contains(p))
     throw new Exception(f"skip test $p for current env")
   blackhole.consume(run(programs(p), result))
 }

 @Benchmark def ci_seminaive_staged(blackhole: Blackhole): Unit = {
  val p = "SemiNaiveInterpretedStagedCollections"
  if(!programs.contains(p))
    throw new Exception(f"skip test $p for current env")
  blackhole.consume(run(programs(p), result))
 }
 @Benchmark def ci_seminaive_staged_jit(blackhole: Blackhole): Unit = {
  val p = "SemiNaiveJITStagedCollections"
  if(!programs.contains(p))
    throw new Exception(f"skip test $p for current env")
  blackhole.consume(run(programs(p), result))
 }
}

trait clique {
  def toSolve = "same_clique"
  def pretest(program: Program): Unit = {
    val edge = program.namedRelation("edge")
    val reachable = program.relation[Constant]("reachable")
    val same_clique = program.relation[Constant]("same_clique")
    val x, y, z = program.variable()

    reachable(x, y) :- edge(x, y)
    reachable(x, y) :- ( edge(x, z), reachable(z, y) )
    same_clique(x, y) :- ( reachable(x, y), reachable(y, x) )
  }
 }