package test.examples.tc_comp

import datalog.dsl.{Constant, Program, __, not}
import test.ExampleTestGenerator

class tc_comp_test extends ExampleTestGenerator("tc_comp") with tc_comp

trait tc_comp {
  val toSolve = "tc"

  def pretest(program: Program): Unit = {
    val e = program.relation[Constant]("e")
    val v = program.relation[Constant]("v")
    val t = program.relation[Constant]("t")
    val tc = program.relation[Constant]("tc")

    val x, y, z = program.variable()

    v(x) :- e(x, __)
    v(y) :- e(__, y)

    t(x, y) :- e(x, y)
    t(x, y) :- (e(x, z), t(z, y))
    tc(x, y) :- (not(t(x, y)), v(x), v(y))

    e("a", "b") :- ()
    e("b", "c") :- ()
    e("c", "d") :- ()
  }
}
