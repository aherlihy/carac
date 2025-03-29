package test.examples.livevariables

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program, not}
import test.{ExampleTestGenerator, Tags}

class livevariables_test extends ExampleTestGenerator("livevariables", tags = Set(Tags.Negated)) with livevariables
trait livevariables {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/livevariables/facts"
  val toSolve = "i"
  def pretest(program: Program): Unit = {
    val v = program.relation[Constant]("v")
    val o = program.relation[Constant]("o")
    val i = program.relation[Constant]("i")
    val gen = program.relation[Constant]("gen")
    val kill = program.relation[Constant]("kill")
    val s = program.relation[Constant]("dependency")

    val x, n, y = program.variable()

    // Possible variables.
    v("x") :- ()
    v("y") :- ()
    v("z") :- ()

    // Instruction dependencies.
    s("1", "2") :- ()
    s("2", "3") :- ()
    s("3", "4") :- ()
    s("3", "5") :- ()
    s("4", "6") :- ()
    s("5", "6") :- ()

    // Gen and kill sets.
    gen("3", "x") :- ()
    gen("3", "y") :- ()
    gen("4", "x") :- ()
    gen("5", "y") :- ()
    gen("6", "z") :- ()

    kill("1", "x") :- ()
    kill("2", "y") :- ()
    kill("4", "z") :- ()
    kill("5", "z") :- ()

    // Out and in sets.
    i(x, n) :- gen(x, n)
    i(x, n) :- (o(x, n), not(kill(x, n)))
    o(x, n) :- (s(x, y), i(y, n))
  }
}
