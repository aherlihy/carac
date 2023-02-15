package test.examples.is_after

import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths

class is_after_test extends ExampleTestGenerator("is_after") with is_after
trait is_after {
  val toSolve = "is_after"
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
