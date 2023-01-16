package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program, __}
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import java.nio.file.Paths
import java.util.concurrent.TimeUnit
@Fork(1) // # of jvms that it will use
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class ranpo_benchmark() extends ExampleBenchmarkGenerator (
  "ranpo",
  Set(),
  Set("CI")
) with ranpo {
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
trait ranpo {

  def pretest(program: Program): Unit = {
    val Check = program.namedRelation[Constant]("Check")

    val In = program.namedRelation[Constant]("In")

    val A = program.relation[Constant]("A")

    val i, a, b, c, d, e, f = program.variable()
    
    A(1,i) :- ( Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i) )
    A(2,i) :- ( Check(a, b, c, __, e, __), In(a, b, c, __, e, __, i) )
    A(3,i) :- ( Check(a, __, c, d, e, f), In(a, __, c, d, e, f, i) )
    A(4,i) :- ( Check(a, b, c, d, __, __), In(a, b, c, d, __, __, i) )
    A(5,i) :- ( Check(a, b, __, d, e, f), In(a, b, __, d, e, f, i) )
    A(6,i) :- ( Check(a, b, __, __, e, f), In(a, b, __, __, e, f, i) )
    A(7, i) :- ( Check(__,__, c, d, e, f), In(__, __, c, d, e, f, i) )
    A(8, i) :- ( Check(__, b, __, d, __, f), In(__, b, __, d, __, f, i) )
    A(9, i) :- ( Check(a, b, __, d, __, f), In(a, b, __, d, __, f, i) )
  }
}
