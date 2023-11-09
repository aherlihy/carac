package test.examples.tc_count

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program, __, groupBy, AggOp}
import test.ExampleTestGenerator

class shortest_edges_test extends ExampleTestGenerator("shortest_edges") with shortest_edges

trait shortest_edges {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/shortest_edges/facts"
  val toSolve = "ShortestEdgeFromN"

  def pretest(program: Program): Unit = {
    val Edge = program.namedRelation("Edge")
    val ShortestEdgeFromN = program.relation[Constant]("ShortestEdgeFromN")

    val x, l, z = program.variable()

    ShortestEdgeFromN(x, l) :- (Edge(x, __, __), groupBy(Edge(x, __, z), Seq(x), AggOp.MIN(z) -> l))
  }
}
