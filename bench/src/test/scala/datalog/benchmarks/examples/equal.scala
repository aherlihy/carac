package datalog.benchmarks.examples
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.equal.equal

import datalog.dsl.{Constant, Program}

import java.nio.file.Paths
@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = examples_iterations, time = examples_time, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class equal_benchmark() extends ExampleBenchmarkGenerator("equal") with equal {
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
  if(!programs.contains(p))
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
trait equal {
  def toSolve: String = "isEqual"

  def pretest(program: Program): Unit = {
    val equal = program.relation[Constant]("equal")

    val isEqual = program.relation[Constant]("isEqual")

    val succ = program.relation[Constant]("succ")

    val m, n, r, pn, pm = program.variable()


    equal("0", "0", "1") :- ()
    equal(m, n, r) :- ( succ(pm, m) , succ(pn, n), equal(pm, pn, r) )

    isEqual(r) :- equal("5", "7", r)

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
  }
}
