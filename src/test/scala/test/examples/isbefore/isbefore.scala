package test.examples.isbefore

import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths

class isbefore_test extends ExampleTestGenerator("isbefore") with isbefore
trait isbefore {
  val toSolve = "is_before"
  def pretest(program: Program): Unit = {
    val edge = program.namedRelation("edge")
    val isBefore = program.relation[Constant]("is_before")
    val isAfter = program.relation[Constant]("is_after")
    val x, y, z = program.variable()

    isBefore(x, y) :- edge(x, y)
    isBefore(x, y) :- (isBefore(x, z), isBefore(z, y))

    isAfter(x, y) :- edge(y, x)
    isAfter(x, y) :- (isAfter(z, x), isAfter(y, z))
  }
}
