package test.examples.fib

import datalog.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}

import java.nio.file.Paths
class fib_test_optimized extends ExampleTestGenerator(
  "fib",
  Set(),
  Set(Tags.CI)
) with fib_optimized
trait fib_optimized {

  val toSolve = "f"
  def pretest(program: Program): Unit = {
    val f = program.relation[Constant]("f")
    val succ = program.namedRelation("succ")
    val plus_mod = program.namedRelation("plus_mod")

    val i, r, prev, pprev, x, y = program.variable()

    f("0", "0") :- ()
    f("1", "1") :- ()
    f(i, r) :- ( plus_mod(x, y, r), f(prev, x), succ(prev, i), succ(pprev, prev), f(pprev, y) )
  }
}
