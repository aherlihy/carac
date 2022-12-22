package examples.topological_ordering

import datalog.dsl.{Constant, Program}
import tools.GraphGenerator

import java.nio.file.Paths

class topological_ordering extends GraphGenerator(
  Paths.get("src", "test", "scala", "examples", "topological_ordering") // TODO: use pwd
) {
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
