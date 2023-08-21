package test.examples.anonvar

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program, __}
import test.{ExampleTestGenerator, Tags}

import java.nio.file.Paths
class anonvar_test extends ExampleTestGenerator(
  "anonvar",
  Set(Tags.Naive, Tags.Volcano),
  Set(Tags.Slow, Tags.CI)
) with anonvar

trait anonvar {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/anonvar/facts"
  val toSolve = "_"
  def pretest(program: Program): Unit = {
    val Check = program.namedRelation[Constant]("Check")

    val In = program.namedRelation[Constant]("In")

    val A1 = program.relation[Constant]("A1")

    val a, b, c, d, e, f, i = program.variable()

    A1(1,i) :- ( Check(__, b, c, d, e, f), In(__, b, c, d, e, f, i) )
    A1(2,i) :- ( Check(a, __, c, d, e, f), In(a, __, c, d, e, f, i) )
    A1(3,i) :- ( Check(a, b, __, d, e, f), In(a, b, __, d, e, f, i) )
    A1(4,i) :- ( Check(a, b, c, __, e, f), In(a, b, c, __, e, f, i) )
    A1(5,i) :- ( Check(a, b, c, d, __, f), In(a, b, c, d, __, f, i) )
    A1(6,i) :- ( Check(a, b, c, d, e, __), In(a, b, c, d, e, __, i) )

    A1(7, i) :- ( Check(__, __, c, d, e, f), In(__, __, c, d, e, f, i) )
    A1(8, i) :- ( Check(a, __, __, d, e, f), In(a, __, __, d, e, f, i) )
    A1(9, i) :- ( Check(a, b, __, __, e, f), In(a, b, __, __, e, f, i) )
    A1(10, i) :- ( Check(a, b, c, __, __, f), In(a, b, c, __, __, f, i) )
    A1(11, i) :- ( Check(a, b, c, d, __, __), In(a, b, c, d, __, __, i) )

    A1(12, i) :- ( Check(__, __, __, d, e, f), In(__, __, __, d, e, f, i) )
    A1(13, i) :- ( Check(a, __, __, __, e, f), In(a, __, __, __, e, f, i) )
    A1(14, i) :- ( Check(a, b, __, __, __, f), In(a, b, __, __, __, f, i) )
    A1(15, i) :- ( Check(a, b, c, __, __, __), In(a, b, c, __, __, __, i) )

    A1(16, i) :- ( Check(__, __, __, __, e, f), In(__, __, __, __, e, f, i) )
    A1(17, i) :- ( Check(a, __, __, __, __, f), In(a, __, __, __, __, f, i) )
    A1(18, i) :- ( Check(a, b, __, __, __, __), In(a, b, __, __, __, __, i) )

    A1(19, i) :- ( Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i) )

    // po2
    val A2 = program.relation[Constant]("A2")
    A2(1,i) :- ( Check(__, b, c, d, e, __), In(__, b, c, d, e, __, i) )
    A2(2,i) :- ( Check(a, __, c, d, e, __), In(a, __, c, d, e, __, i) )
    A2(3,i) :- ( Check(a, b, __, __, e, f), In(a, b, __, __, e, f, i) )
    A2(4,i) :- ( Check(__, __, c, d, __, __), In(__, __, c, d, __, __, i) )
    A2(5,i) :- ( Check(a, __, __, __, __, f), In(a, __, __, __, __, f, i) )
    A2(6,i) :- ( Check(__, b, c, d, __, f), In(__, b, c, d, __, f, i) )
    A2(7, i) :- ( Check(__, b, c, __, e, f), In(__, b, c, __, e, f, i) )
    A2(8, i) :- ( Check(__, b, __, d, e, f), In(__, b, __, d, e, f, i) )
    A2(9, i) :- ( Check(__, __, c, d, e, f), In(__, __, c, d, e, f, i) )
    A2(10, i) :- ( Check(__, b, c, d, __, __), In(__, b, c, d, __, __, i) )
    A2(11, i) :- ( Check(__, b, c, __, __, f), In(__, b, c, __, __, f, i) )
    A2(12, i) :- ( Check(__, b, __, __, e, f), In(__, b, __, __, e, f, i) )
    A2(13, i) :- ( Check(__, __, __, d, e, f), In(__, __, __, d, e, f, i) )
    A2(14, i) :- ( Check(__, b, c, __, __, __), In(__, b, c, __, __, __, i) )
    A2(15, i) :- ( Check(__, b, __, __, __, f), In(__, b, __, __, __, f, i) )
    A2(16, i) :- ( Check(__, __, __, __, e, f), In(__, __, __, __, e, f, i) )
    A2(17, i) :- ( Check(__, b, c, d, e, f), In(__, b, c, d, e, f, i) )
    A2(18, i) :- ( Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i) )

    // po3
    val A3 = program.relation[Constant]("A3")
    A3(1,i) :- ( Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i) )
    A3(2,i) :- ( Check(a, __, c, d, e, f), In(a, __, c, d, e, f, i) )
    A3(3,i) :- ( Check(a, __, __, d, e, f), In(a, __, __, d, e, f, i) )
    A3(4,i) :- ( Check(a, __, __, __, e, f), In(a, __, __, __, e, f, i) )
    A3(5,i) :- ( Check(a, __, __, __, __, f), In(a, __, __, __, __, f, i) )
    A3(6,i) :- ( Check(a, __, __, __, __, __), In(a, __, __, __, __, __, i) )
    A3(7, i) :- ( Check(a, b, __, d, e, f), In(a, b, __, d, e, f, i) )
    A3(8, i) :- ( Check(a, b, __, __, e, f), In(a, b, __, __, e, f, i) )
    A3(9, i) :- ( Check(a, b, __, __, __, f), In(a, b, __, __, __, f, i) )
    A3(10, i) :- ( Check(a, b, __, __, __, __), In(a, b, __, __, __, __, i) )
    A3(11, i) :- ( Check(a, b, c, __, e, f), In(a, b, c, __, e, f, i) )
    A3(12, i) :- ( Check(a, b, c, __, __, f), In(a, b, c, __, __, f, i) )
    A3(13, i) :- ( Check(a, b, c, __, __, __), In(a, b, c, __, __, __, i) )
    A3(14, i) :- ( Check(a, b, c, d, __, f), In(a, b, c, d, __, f, i) )
    A3(15, i) :- ( Check(a, b, c, d, __, __), In(a, b, c, d, __, __, i) )

    // po4
    val A4 = program.relation[Constant]("A4")
    A4(1,i) :- ( Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i) )
    A4(2,i) :- ( Check(a, b, c, d, __, __), In(a, b, c, d, __, __, i) )
    A4(3,i) :- ( Check(a, __, c, d, __, __), In(a, __, c, d, __, __, i) )
    A4(4,i) :- ( Check(a, b, __, d, __, __), In(a, b, __, d, __, __, i) )
    A4(5,i) :- ( Check(a, b, c, __, __, __), In(a, b, c, __, __, __, i) )
    A4(6,i) :- ( Check(__, __, c, d, __, __), In(__, __, c, d, __, __, i) )
    A4(7, i) :- ( Check(a, b, __, __, __, __), In(a, b, __, __, __, __, i) )
    A4(8, i) :- ( Check(a, __, __, __, __, __), In(a, __, __, __, __, __, i) )
    A4(9, i) :- ( Check(__, b, __, __, __, __), In(__, b, __, __, __, __, i) )

    // po5
    val A5 = program.relation[Constant]("A5")
    A5(1,i) :- ( Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i) )
    A5(2,i) :- ( Check(a, __, __, d, e, f), In(a, __, __, d, e, f, i) )
    A5(3,i) :- ( Check(a, b, __, d, e, f), In(a, b, __, d, e, f, i) )
    A5(4,i) :- ( Check(a, __, __, __, e, f), In(a, __, __, __, e, f, i) )
    A5(5,i) :- ( Check(a, b, __, __, __, f), In(a, b, __, __, __, f, i) )
    A5(6,i) :- ( Check(a, __, __, __, __, __), In(a, __, __, __, __, __, i) )
    A5(7, i) :- ( Check(a, b, __, d, e, f), In(a, b, __, d, e, f, i) )
    A5(8, i) :- ( Check(a, b, __, __, e, f), In(a, b, __, __, e, f, i) )
    A5(9, i) :- ( Check(a, b, __, __, __, f), In(a, b, __, __, __, f, i) )
    A5(10, i) :- ( Check(a, b, __, __, __, __), In(a, b, __, __, __, __, i) )
    A5(11, i) :- ( Check(a, b, c, __, e, f), In(a, b, c, __, e, f, i) )
    A5(12, i) :- ( Check(a, __, c, __, __, f), In(a, __, c, __, __, f, i) )
    A5(13, i) :- ( Check(a, b, c, __, __, __), In(a, b, c, __, __, __, i) )
    A5(14, i) :- ( Check(a, b, c, d, __, f), In(a, b, c, d, __, f, i) )
    A5(15, i) :- ( Check(a, __, c, d, __, __), In(a, __, c, d, __, __, i) )
  }
}
