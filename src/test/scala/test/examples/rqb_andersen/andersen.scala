package test.examples.rqb_andersen

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import carac.storage.{DatabaseType, DuckDBStorageManager}
import test.ExampleTestGenerator

import java.nio.file.Paths

trait rqb_andersen {
 val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/rqb_andersen/facts"
 val toSolve = "pointsTo"
 def pretest(program: Program): Unit = {
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
 }

 def loadSchema(program: Program, duckDBStorageManager: DuckDBStorageManager): Unit =
   val addressOf = program.relation("addressOf")
   val assign = program.relation("assign")
   val loadT = program.relation("loadT")
   val store = program.relation("store")
   duckDBStorageManager.declareTable(addressOf.id, Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
   duckDBStorageManager.declareTable(assign.id, Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
   duckDBStorageManager.declareTable(loadT.id, Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
   duckDBStorageManager.declareTable(store.id, Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
   duckDBStorageManager.edbs.initializeTable(addressOf.id, "addressOf", Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
   duckDBStorageManager.edbs.initializeTable(assign.id, "assign", Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
   duckDBStorageManager.edbs.initializeTable(loadT.id, "loadT", Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
   duckDBStorageManager.edbs.initializeTable(store.id, "store", Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
}

class rqb_andersen_test() extends ExampleTestGenerator("rqb_andersen") with rqb_andersen