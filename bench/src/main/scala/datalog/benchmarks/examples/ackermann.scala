package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program}
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import java.nio.file.{Path, Paths}
import java.util.concurrent.TimeUnit
import scala.util.Properties

trait ackermann {
  def pretest(program: Program): Unit = {
    val succ = program.namedRelation("succ")
    val greaterThanZ = program.namedRelation("greaterThanZ")
    val ack = program.relation[Constant]("ack")
    val N, M, X, Y, Ans, Ans2 = program.variable()

    ack("0", N, Ans) :- succ(N, Ans)

    ack(M, "0", Ans) :- ( greaterThanZ(M), succ(X, M), ack(X, "1", Ans) )

    ack(M, N, Ans) :- (
      greaterThanZ(M),
      greaterThanZ(N),
      succ(X, M),
      succ(Y, N),
      ack(M, Y, Ans2),
      ack(X, Ans2, Ans))
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class ackermann_benchmark() extends ExampleBenchmarkGenerator(
 "ackermann",
 Set("Naive", "Relational"), // run only SemiNaiveColl
 Set("Slow", "CI")
) with ackermann {
 override def toSolve: String = super.toSolve
 @Setup
 def s(): Unit = setup() // can't add annotations to super, so just call

 @TearDown
 def f(): Unit = finish()

 // relational, seminaive
 @Benchmark def seminaive_collections(blackhole: Blackhole): Unit = {
  blackhole.consume(
   run(programs("SemiNaiveCollections"), result)
  )
 }

 // staged, seminaive
 @Benchmark def seminaive_staged(blackhole: Blackhole): Unit = {
  blackhole.consume(
   run(programs("SemiNaiveStagedCollections"), result)
  )
 }
}