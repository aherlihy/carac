package datalog.benchmarks

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import datalog.dsl.*
import datalog.execution.{ExecutionEngine, SimpleExecutionEngine}

@Fork(1)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class Bench {
  val dummyStream = new java.io.PrintStream(_ => ())

  def transitiveClosure(isNaive: Boolean, blackhole: Blackhole): Unit = {
    given engine: ExecutionEngine = new SimpleExecutionEngine

    val program = Program()
    val e = program.relation[String]()
    val p = program.relation[String]()
    val ans1 = program.relation[String]()
    val ans2 = program.relation[String]()
    val ans3 = program.relation[String]()

    val x, y, z = program.variable()

    e("a", "b") :- ()
    e("b", "c") :- ()
    e("c", "d") :- ()
    p(x, y) :- e(x, y)
    p(x, z) :- ( e(x, y), p(y, z) )
    ans1(x) :- e("a", x)
    ans2(x) :- p("a", x)

    def solve[T <: Constant](rel: Relation[T]): Unit =
      blackhole.consume(
        if isNaive then rel.solve() else rel.solveNaive()
      )

    // FIXME: we redirect Console.out.println to a dummy stream here because
    // running the solver prints out a lot of debug information that obscures
    // the benchmarking output.
    Console.withOut(dummyStream) {
      solve(p)
      solve(ans1)
      solve(ans2)
    }
  }

  @Benchmark def transitiveClosure_naive(blackhole: Blackhole): Unit =
    transitiveClosure(isNaive = true, blackhole)

  @Benchmark def transitiveClosure_semiNaive(blackhole: Blackhole): Unit =
    transitiveClosure(isNaive = false, blackhole)
}
