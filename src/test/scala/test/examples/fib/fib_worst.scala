package test.examples.fib

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}

import java.nio.file.Paths
class fib_test_worst extends ExampleTestGenerator(
  "fib",
  Set(),
  Set(Tags.CI)
) with fib_worst
trait fib_worst {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"

  val toSolve = "f"
  def pretest(program: Program): Unit = {
    val f = program.relation[Constant]("f")
    val succ = program.namedRelation("succ")
    val plus_mod = program.namedRelation("plus_mod")

    val i, r, prev, pprev, x, y = program.variable()

    f("0", "0") :- ()
    f("1", "1") :- ()
    f(i, r) :- ( succ(pprev, prev), plus_mod(x, y, r), succ(prev, i), f(pprev, y), f(prev, x))
  }
}
