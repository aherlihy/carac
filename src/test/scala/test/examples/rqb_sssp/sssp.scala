package test.examples.rqb_sssp

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}

class rqb_sssp_test extends ExampleTestGenerator("rqb_sssp") with rqb_sssp
trait rqb_sssp {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/rqb_sssp/facts"
  val toSolve = "cost"

  def pretest(program: Program): Unit = {}

  val sqlString =
    """WITH RECURSIVE recursive1 AS
        ((SELECT c0 as dst, c1 as cost FROM edb_base as edb_base1)
        UNION
        ((SELECT edb_edge3.c1 as dst, ref1.cost + edb_edge3.c2 as cost
          FROM edb_edge as edb_edge3, recursive1 as ref1
          WHERE ref1.dst = edb_edge3.c0)))
      SELECT * FROM recursive1
    """
}
