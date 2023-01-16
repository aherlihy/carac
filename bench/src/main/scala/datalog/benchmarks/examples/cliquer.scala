package datalog.benchmarks.examples
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import datalog.dsl.{Constant, Program}

import java.nio.file.Paths
@Fork(1) // # of jvms that it will use
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class cliquer_benchmark() extends ExampleBenchmarkGenerator("cliquer") with cliquer {
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
trait cliquer {
  def toSolve = "reachable"
  def pretest(program: Program): Unit = {
    val edge = program.relation[Constant]("edge")

    val leg = program.relation[Constant]("leg")

    val reachable = program.relation[Constant]("reachable")

    val same_clique = program.relation[Constant]("same_clique")

    val X, Y, Z = program.variable()

    leg(X,Z) :- ( edge(X,Y), edge(Y,Z) )

    reachable(X, Y) :- edge(X, Y)
    reachable(X, Y) :- ( edge(X, Z), reachable(Z, Y) )
    same_clique(X, Y) :- ( reachable(X, Y), reachable(Y, X) )

    edge("a", "b") :- ()
    edge("b", "c") :- ()
    edge("c", "d") :- ()
    edge("d", "a") :- ()

    reachable("e","f") :- ()
  }
}
