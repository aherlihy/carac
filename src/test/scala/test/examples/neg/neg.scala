package test.examples.neg

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program, not}
import test.{ExampleTestGenerator, Tags}

class neg_test extends ExampleTestGenerator("neg", tags = Set(Tags.Negated)) with neg
trait neg {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/neg/facts"
  val toSolve = "c"
  def pretest(program: Program): Unit = {
    val a = program.relation[Constant]("a")
    val b = program.relation[Constant]("b")
    val c = program.relation[Constant]("c")
    val x, y = program.variable()

    a("a") :- ()
    a("b") :- ()
    a("c") :- ()
    a("d") :- ()

    b("a") :- ()
    b("b") :- ()

    c(x, y) :- (not(b(x)), not(b(y)), a(x), a(y))
  }
}
