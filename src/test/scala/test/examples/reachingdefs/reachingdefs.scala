package test.examples.reachingdefs

import datalog.dsl.{Constant, Program, not}
import test.ExampleTestGenerator

class reachingdefs_test extends ExampleTestGenerator("reachingdefs") with reachingdefs
trait reachingdefs {
  val toSolve = "o"
  def pretest(program: Program): Unit = {
    val o = program.relation[Constant]("o")
    val i = program.relation[Constant]("i")
    val gen = program.relation[Constant]("gen")
    val kill = program.relation[Constant]("kill")
    val s = program.relation[Constant]("dependency")

    val n, m, v, idx = program.variable()

    s("1", "2") :- ()
    s("2", "3") :- ()
    s("3", "4") :- ()
    s("4", "5") :- ()
    s("5", "6") :- ()
    s("6", "4") :- ()
    s("6", "7") :- ()

    gen("1", "x") :- ()
    gen("2", "y") :- ()
    gen("3", "z") :- ()
    gen("4", "x") :- ()
    gen("5", "z") :- ()

    kill(n, v) :- gen(n, v)

    i(n, v, idx) :- (o(m, v, idx), s(m, n))
    o(n, v, n) :- gen(n, v)
    o(n, v, idx) :- (i(n, v, idx), not(kill(n, v)))
  }
}
