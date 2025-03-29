package test.examples.strata

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.ExampleTestGenerator

class strata_test extends ExampleTestGenerator("strata") with strata

trait strata {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/strata/facts"
  val toSolve = "_"

  def pretest(program: Program): Unit = {
    val b = program.relation[Constant]("e")
    val p1 = program.relation[Constant]("p1")
    val p2 = program.relation[Constant]("p2")
    val p3 = program.relation[Constant]("p3")
    val q = program.relation[Constant]("q")
    val r = program.relation[Constant]("r")
    val x, y, z = program.variable()

    // p1, p2 and p3 are in the same stratum
    p1(x, y, z) :- b(x, y, z)
    p1(x, y, z) :- p2(y, z, x)
    p2(x, y, z) :- b(x, y, z)
    p2(x, y, z) :- p3(y, z, x)
    p3(x, y, z) :- b(x, y, z)
    p3(x, y, z) :- p1(y, z, x)

    q(x, y, z) :- (p1(x, y, z), p2(x, y, z), p3(x, y, z))

    r(x, y, z) :- (q(x, y, z), p1(x, y, z), p2(x, y, z), p3(x, y, z))


    b("a", "b", "c") :- ()
    b("x", "y", "z") :- ()
  }
}
