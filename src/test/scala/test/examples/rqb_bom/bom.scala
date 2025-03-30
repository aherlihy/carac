package test.examples.rqb_bom

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}

trait rqb_bom {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/rqb_bom/facts"
  val toSolve = "waitFor"

  def pretest(program: Program): Unit = {}

  val sqlString =
    """
      WITH RECURSIVE recursive1 AS
        ((SELECT c0 as part, c1 as days FROM edb_basic as edb_basic1)
        UNION ALL
        ((SELECT edb_assbl3.c0 as part, ref1.days as days
          FROM edb_assbl as edb_assbl3, recursive1 as ref1
          WHERE edb_assbl3.c1 = ref1.part)))
      SELECT * FROM recursive1
    """
    // SELECT recref0.part as part, MAX(recref0.days) as max FROM recursive1 as recref0 GROUP BY recref0.part
}
