package test.examples.rqb_sssp

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program, Relation}
import test.{ExampleTestGenerator, RunTyQL, Tags, TyQLComparative, TyQLOnlyTest}
import carac.storage.{DatabaseType, StorageManager}
import tyql.Expr.{IntLit, min}
import tyql.{Ord, Query, Table}

import java.nio.file.Paths
import language.experimental.namedTuples

import java.nio.file.Paths

class TyQLSSSPTest extends TyQLOnlyTest with rqb_sssp

trait rqb_sssp extends RunTyQL {
  val directory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/rqb_sssp"
  val toSolve = "cost"
  val linear = true

  override def loadData(program: Program): Unit =
    loadDataFromFile(program, directory)

  override val expectedFacts = loadExpectedFile(Paths.get(directory, "expected"))("cost")

  override def generateTyQL(program: Program) =
    type WeightedEdge = (src: Int, dst: Int, cost: Int)
    type ResultEdge = (dst: Int, cost: Int)
    type WeightedGraphDB = (edge: WeightedEdge, base: ResultEdge)
    val tyqlDB = (
      edge = Table[WeightedEdge]("edge"),
      base = Table[ResultEdge]("base")
    )

    val base = tyqlDB.base
    val queryT = Query.dispatchedFix(Tuple1(base))(spT =>
      val sp = spT._1
      val res = tyqlDB.edge.flatMap(edge =>
        sp
          .filter(s => s.dst == edge.src)
          .map(s => (dst = edge.dst, cost = s.cost + edge.cost).toRow)
      ).distinct
      Tuple1(res)
    )
    val query = queryT._1
      .aggregate(s => (dst = s.dst, cost = min(s.cost)).toGroupingRow)
      .groupBySource(s => (dst = s._1.dst).toRow)

    query

  val sqlString =
    """WITH RECURSIVE recursive1 AS
        ((SELECT dst as dst, cost as cost FROM edb_base as edb_base1)
        UNION
        ((SELECT edb_edge3.dst as dst1, ref1.cost + edb_edge3.cost as cost1
          FROM edb_edge as edb_edge3, recursive1 as ref1
          WHERE ref1.dst = edb_edge3.src)))
      SELECT * FROM recursive1
    """
  override def loadSchema(program: Program, storage: StorageManager): Unit =
    val base = program.relation("base")
    val baseS = Seq(("dst", DatabaseType.INTEGER), ("cost", DatabaseType.INTEGER))
    storage.registerRelationSchema(base.id, baseS)
    val edge = program.relation("edge")
    val edgeS = Seq(("src", DatabaseType.INTEGER), ("dst", DatabaseType.INTEGER), ("cost", DatabaseType.INTEGER))
    storage.registerRelationSchema(edge.id, edgeS)
}
