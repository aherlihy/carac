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
class prime_benchmark() extends ExampleBenchmarkGenerator("prime") with prime {
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
trait prime {

  def toSolve: String = "count_all"
  def pretest(program: Program): Unit = {
    val count_all = program.relation[Constant]("count_all")

    val count_second = program.relation[Constant]("count_second")

    val count_third = program.relation[Constant]("count_third")

    val succ = program.relation[Constant]("succ")

    val n, ppn, pn, pppn, x = program.variable()

    count_second("3") :- ()
    count_second(n) :- ( succ(pn, n), succ(ppn, pn), count_second(ppn) )

    count_third("3") :- ()
    count_third(n) :- ( succ(pn, n), succ(ppn, pn), succ(pppn, ppn), count_third(pppn) )

    count_all(x) :- ( count_second(x), count_third(x) )

    succ("0", "1") :- ()
    succ("1", "2") :- ()
    succ("2", "3") :- ()
    succ("3", "4") :- ()
    succ("4", "5") :- ()
    succ("5", "6") :- ()
    succ("6", "7") :- ()
    succ("7", "8") :- ()
    succ("8", "9") :- ()
    succ("9", "10") :- ()
    succ("10", "11") :- ()
    succ("11", "12") :- ()
    succ("12", "13") :- ()
    succ("13", "14") :- ()
    succ("14", "15") :- ()
    succ("15", "16") :- ()
    succ("16", "17") :- ()
    succ("17", "18") :- ()
    succ("18", "19") :- ()
    succ("19", "20") :- ()
    succ("20", "21") :- ()
  }
}
