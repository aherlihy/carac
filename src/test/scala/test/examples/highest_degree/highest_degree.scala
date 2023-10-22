package test.examples.tc_count

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program, __, groupBy, AggOp}
import test.ExampleTestGenerator

class highest_degree_test extends ExampleTestGenerator("highest_degree") with highest_degree

trait highest_degree {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/highest_degree/facts"
  val toSolve = "_"

  def pretest(program: Program): Unit = {
    val Edge = program.namedRelation("Edge")
    val OutDegree = program.relation[Constant]("OutDegree")
    val InDegree = program.relation[Constant]("InDegree")
    val HighestOutDegree = program.relation[Constant]("HighestOutDegree")
    val HighestInDegree = program.relation[Constant]("HighestInDegree")

    val x, l, d = program.variable()

    OutDegree(x, l) :- (Edge(x, __, __), groupBy(Edge(x, __, __), Seq(x), AggOp.SUM(1) -> l))
    InDegree(x, l) :- (Edge(__, x, __), groupBy(Edge(__, x, __), Seq(x), AggOp.SUM(1) -> l))

    HighestOutDegree(x) :- (OutDegree(__, __), groupBy(OutDegree(__, d), Seq(), AggOp.MAX(d) -> x))
    HighestInDegree(x) :- (InDegree(__, __), groupBy(InDegree(__, d), Seq(), AggOp.MAX(d) -> x))
  }
}
