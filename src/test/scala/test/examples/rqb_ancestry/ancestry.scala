package test.examples.rqb_ancestry

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}
import carac.storage.{DatabaseType, DuckDBStorageManager}

class rqb_ancestry_test extends ExampleTestGenerator("rqb_ancestry") with rqb_ancestry
trait rqb_ancestry {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/rqb_ancestry/facts"
  val toSolve = "generation"

  def pretest(program: Program): Unit = {
    val parents = program.namedRelation[Int]("parents")
  }

  val sqlString = //"""SELECT edb_parents1.c1 as name, 1 as gen FROM edb_parents as edb_parents1 WHERE edb_parents1.c0 = '1'"""
    """WITH RECURSIVE recursive1 AS
        ((SELECT edb_parents1.c1 as name, 1 as gen FROM edb_parents as edb_parents1 WHERE edb_parents1.c0 = '1') UNION ((SELECT edb_parents3.c1 as name, ref3.gen + 1 as gen FROM edb_parents as edb_parents3, recursive1 as ref3 WHERE edb_parents3.c0 = ref3.name)))
        SELECT recref0.name as name FROM recursive1 as recref0 WHERE recref0.gen = 2
    """
  def loadSchema(program: Program, storage: DuckDBStorageManager): Unit =
    val parents = program.relation("parents")
    val parentsS = Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT))
    storage.declareTable(parents.id, parentsS)
    storage.edbs.initializeTable(parents.id, "parents", parentsS)
}
