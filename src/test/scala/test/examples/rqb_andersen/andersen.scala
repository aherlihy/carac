package test.examples.rqb_andersen

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program, Relation}
import test.{ExampleTestGenerator, RunTyQL, Tags, TyQLComparative, TyQLOnlyTest, TyQLComparativeTest}
import carac.storage.{DatabaseType, DuckDBStorageManager}
import tyql.Expr.IntLit
import tyql.{Ord, Query, Table}

import java.nio.file.Paths
import language.experimental.namedTuples

import java.nio.file.Paths

class TyQLAndersenTest extends TyQLComparativeTest with rqb_andersen

trait rqb_andersen extends TyQLComparative {
 val directory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/rqb_andersen"
 val toSolve = "pointsTo"

  override def loadData(program: Program): Unit =
    loadDataFromFile(program, directory)

  override val expectedFacts = loadExpectedFile(Paths.get(directory, "expected"))("pointsTo")

  override def generateCarac(program: Program) =
    val addressOf = program.namedRelation("addressOf")
    val assign = program.namedRelation("assign")
    val loadT = program.namedRelation("loadT")
    val store = program.namedRelation("store")

    val x, y, z, w = program.variable()
    val pointsTo = program.relation[Constant]("pointsTo")

    pointsTo(y, x) :- addressOf(y, x)

    pointsTo(y, x) :- (assign(y, z), pointsTo(z, x))

    pointsTo(y, w) :- (
      loadT(y, x),
      pointsTo(x, z),
      pointsTo(z, w))

    pointsTo(z, w) :- (
      store(y, x),
      pointsTo(y, z),
      pointsTo(x, w))
    pointsTo

  override def generateTyQL(program: Program) =
    type Edge = (x: String, y: String)

    val tables = (
      addressOf = Table[Edge]("addressOf"),
      assign = Table[Edge]("assign"),
      loadT = Table[Edge]("loadT"),
      store = Table[Edge]("store")
    )
    val base = tables.addressOf.map(a => (x = a.x, y = a.y).toRow)

    val query = base.unrestrictedFix(pointsTo =>
      tables.assign.flatMap(a =>
          pointsTo.filter(p => a.y == p.x).map(p =>
            (x = a.x, y = p.y).toRow))
        .union(tables.loadT.flatMap(l =>
          pointsTo.flatMap(pt1 =>
            pointsTo
              .filter(pt2 => l.y == pt1.x && pt1.y == pt2.x)
              .map(pt2 =>
                (x = l.x, y = pt2.y).toRow))))
        .union(tables.store.flatMap(s =>
          pointsTo.flatMap(pt1 =>
            pointsTo
              .filter(pt2 => s.x == pt1.x && s.y == pt2.x)
              .map(pt2 =>
                (x = pt1.y, y = pt2.y).toRow)))))
    query

 override def loadSchema(program: Program, duckDBStorageManager: DuckDBStorageManager): Unit =
   val addressOf = program.relation("addressOf")
   val assign = program.relation("assign")
   val loadT = program.relation("loadT")
   val store = program.relation("store")
   duckDBStorageManager.declareTable(addressOf.id, Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT)))
   duckDBStorageManager.declareTable(assign.id, Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT)))
   duckDBStorageManager.declareTable(loadT.id, Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT)))
   duckDBStorageManager.declareTable(store.id, Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT)))
   duckDBStorageManager.edbs.initializeTable(addressOf.id, "addressOf", Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT)))
   duckDBStorageManager.edbs.initializeTable(assign.id, "assign", Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT)))
   duckDBStorageManager.edbs.initializeTable(loadT.id, "loadT", Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT)))
   duckDBStorageManager.edbs.initializeTable(store.id, "store", Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT)))
}