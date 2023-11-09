package test.examples.tc_count

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program, __, groupBy, AggOp}
import test.ExampleTestGenerator

class degree_test extends ExampleTestGenerator("degree") with degree

trait degree {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/degree/facts"
  val toSolve = "_"

  def pretest(program: Program): Unit = {
    val Edge = program.namedRelation("Edge")
    val OutDegree = program.relation[Constant]("OutDegree")
    val InDegree = program.relation[Constant]("InDegree")

    val x, l = program.variable()

    OutDegree(x, l) :- (Edge(x, __, __), groupBy(Edge(x, __, __), Seq(x), AggOp.SUM(1) -> l))
    InDegree(x, l) :- (Edge(__, x, __), groupBy(Edge(__, x, __), Seq(x), AggOp.SUM(1) -> l))
  }
}
