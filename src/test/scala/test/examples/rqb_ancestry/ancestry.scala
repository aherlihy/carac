package test.examples.rqb_ancestry

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program, Relation}
import test.{ExampleTestGenerator, RunTyQL, Tags, TyQLComparative, TyQLOnlyTest}
import carac.storage.{DatabaseType, StorageManager}
import tyql.Expr.IntLit
import tyql.{Ord, Query, Table}

import java.nio.file.Paths
import language.experimental.namedTuples

class TyQLAncestryTest extends TyQLOnlyTest with rqb_ancestry
trait rqb_ancestry extends RunTyQL {
  val directory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/rqb_ancestry"
  val toSolve = "generation"
  val linear = true

  override def loadData(program: Program): Unit =
    loadDataFromFile(program, directory)

  override val expectedFacts = loadExpectedFile(Paths.get(directory, "expected"))("result")

  override def generateTyQL() =
    val parentName = "1"
    type Parent = (parent: String, child: String)
    val tyqlDB = (
      parents = Table[Parent]("parents")
    )
    val base = tyqlDB.parents.filter(p => p.parent == parentName).map(e => (name = e.child, gen = IntLit(1)).toRow)
    val queryT = Query.dispatchedFix(Tuple1(base))(tGen =>
        val gen = tGen._1
        val ret = tyqlDB.parents.flatMap(parent =>
          gen
            .filter(g => parent.parent == g.name)
            .map(g => (name = parent.child, gen = g.gen + 1).toRow)
        ).distinct
        Tuple1(ret)
      )
    val query1 = queryT._1.filter(g => g.gen == 2).map(e => (name = e.name).toRow)
    query1

  val sqlString = //"""SELECT edb_parents1.c1 as name, 1 as gen FROM edb_parents as edb_parents1 WHERE edb_parents1.c0 = '1'"""
    """WITH RECURSIVE recursive1 AS
        ((SELECT edb_parents1.child as name, 1 as gen FROM edb_parents as edb_parents1 WHERE edb_parents1.parent = '1') UNION ((SELECT edb_parents3.child as name, ref3.gen + 1 as gen FROM edb_parents as edb_parents3, recursive1 as ref3 WHERE edb_parents3.parent = ref3.name)))
        SELECT recref0.name as name FROM recursive1 as recref0 WHERE recref0.gen = 2
    """
  override def loadSchema(program: Program, storage: StorageManager): Unit =
    val parents = program.relation("parents")
    val parentsS = Seq(("parent", DatabaseType.TEXT), ("child", DatabaseType.TEXT))
    storage.registerRelationSchema(parents.id, parentsS)
}
