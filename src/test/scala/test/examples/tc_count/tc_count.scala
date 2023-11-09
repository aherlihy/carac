package test.examples.tc_count

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program, __, groupBy, AggOp}
import test.ExampleTestGenerator

class tc_count_test extends ExampleTestGenerator("tc_count") with tc_count

trait tc_count {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tc_count/facts"
  val toSolve = "c"

  def pretest(program: Program): Unit = {
    val e = program.relation[Constant]("e")
    val tc = program.relation[Constant]("tc")
    val c = program.relation[Constant]("c")

    val x, y, z = program.variable()

    tc(x, y) :- e(x, y)
    tc(x, y) :- (e(x, z), tc(z, y))
    c(x, y) :- groupBy(tc(x, z), Seq(x), AggOp.COUNT(z) -> y)

    e("a", "b") :- ()
    e("b", "c") :- ()
    e("c", "d") :- ()
    e("d", "e") :- ()
    e("e", "f") :- ()
    e("f", "g") :- ()
  }
}
