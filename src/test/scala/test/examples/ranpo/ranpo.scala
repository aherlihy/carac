package test.examples.ranpo

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program, __}
import test.{ExampleTestGenerator, Tags}

import java.nio.file.Paths
class ranpo_test extends ExampleTestGenerator(
  "ranpo",
  Set(),
  Set(Tags.CI)
) with ranpo
trait ranpo {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ranpo/facts"

  val toSolve = "A"
  def pretest(program: Program): Unit = {
    val Check = program.namedRelation[Constant]("Check")

    val In = program.namedRelation[Constant]("In")

    val A = program.relation[Constant]("A")

    val i, a, b, c, d, e, f = program.variable()
    
    A(1,i) :- ( Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i) )
    A(2,i) :- ( Check(a, b, c, __, e, __), In(a, b, c, __, e, __, i) )
    A(3,i) :- ( Check(a, __, c, d, e, f), In(a, __, c, d, e, f, i) )
    A(4,i) :- ( Check(a, b, c, d, __, __), In(a, b, c, d, __, __, i) )
    A(5,i) :- ( Check(a, b, __, d, e, f), In(a, b, __, d, e, f, i) )
    A(6,i) :- ( Check(a, b, __, __, e, f), In(a, b, __, __, e, f, i) )
    A(7, i) :- ( Check(__,__, c, d, e, f), In(__, __, c, d, e, f, i) )
    A(8, i) :- ( Check(__, b, __, d, __, f), In(__, b, __, d, __, f, i) )
    A(9, i) :- ( Check(a, b, __, d, __, f), In(a, b, __, d, __, f, i) )
  }
}
