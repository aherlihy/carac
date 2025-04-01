package test.examples.rqb_sssp

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}
import carac.storage.{DatabaseType, DuckDBStorageManager}

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
  def loadSchema(program: Program, storage: DuckDBStorageManager): Unit =
    val base = program.relation("base")
    val baseS = Seq(("c0", DatabaseType.INTEGER), ("c1", DatabaseType.INTEGER))
    storage.declareTable(base.id, baseS)
    storage.edbs.initializeTable(base.id, "base", baseS)
    val edge = program.relation("edge")
    val edgeS = Seq(("c0", DatabaseType.INTEGER), ("c1", DatabaseType.INTEGER), ("c2", DatabaseType.INTEGER))
    storage.declareTable(edge.id, edgeS)
    storage.edbs.initializeTable(edge.id, "edge", edgeS)
}
