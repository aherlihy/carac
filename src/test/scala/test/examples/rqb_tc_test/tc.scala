package test

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program, Relation}
import carac.execution.{StagedExecutionEngine, TyQLExecutionEngine}
import carac.execution.ir.{IRTreeGenerator, InterpreterContext, TyQLInterpreterContext}
import carac.storage.{DatabaseType, DuckDBStorageManager}
import tyql.*

import java.nio.file.Paths
import language.experimental.namedTuples

class TestTC_NonLinear_TyQL extends TyQLFrontendTest with TC_Nonlinear

trait TC_Nonlinear extends TyQLComparative {
  override def loadData(program: Program): Unit =
    val edges = program.relation[Constant]("edges", Some(Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT))))
    edges("a", "b") :- ()
    edges("b", "c") :- ()
    edges("c", "d") :- ()
    edges("z", "z") :- ()

  override def generateCarac(program: Program): Relation[Constant] =
    val edges = program.namedRelation("edges")
    val X, Y, Z = program.variable()
    val tc = program.relation[Constant]("tc")
    tc(X, Y) :- (edges(X, Y))
    tc(X, Y) :- (tc(X, Z), tc(Z, Y))
    tc

  override def generateTyQL(program: Program) =
    type Edge = (x: String, y: String)
    type TCDB = (edges: Edge)

    val tables = (
      edges = Table[Edge]("edges"),
      edges2 = Table[Edge]("edges2")
    )

    val path = tables.edges
    val queryT = Query.dispatchedFix(Tuple1(path))(tPathRec =>
      val pathRec = tPathRec._1
      //    val query = path.fix(pathRec =>
      val ret = pathRec.flatMap(p =>
        pathRec
          .filter(e => p.y == e.x)
          .map(e => (x = p.x, y = e.y).toRow)
      ).distinct
      Tuple1(ret)
    )
    val query = queryT._1
    query

  override val expectedFacts: Set[Seq[Constant]] =
    Set(Seq("a", "d"), Seq("b", "d"), Seq("b", "c"), Seq("a", "b"), Seq("z", "z"), Seq("a", "c"), Seq("c", "d"))
}

class TestTC_Linear_TyQL extends TyQLFrontendTest with TC_Linear

trait TC_Linear extends TyQLComparative {
  override def loadData(program: Program): Unit =
    val edges = program.relation[Constant]("edges", Some(Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT))))
    edges("a", "b") :- ()
    edges("b", "c") :- ()
    edges("c", "d") :- ()
    edges("z", "z") :- ()

  override def generateCarac(program: Program): Relation[Constant] =
    val edges = program.namedRelation("edges")
    val X, Y, Z = program.variable()
    val tc = program.relation[Constant]("tc")
    tc(X, Y) :- (edges(X, Y))
    tc(X, Y) :- (tc(X, Z), edges(Z, Y))
    tc

  override def generateTyQL(program: Program) =
    type Edge = (x: String, y: String)
    type TCDB = (edges: Edge)

    val tables = (
      edges = Table[Edge]("edges"),
      edges2 = Table[Edge]("edges2")
    )

    val path = tables.edges
    val queryT = Query.dispatchedFix(Tuple1(path))(tPathRec =>
      val pathRec = tPathRec._1
      val ret = pathRec.flatMap(p =>
        tables.edges
          .filter(e => p.y == e.x)
          .map(e => (x = p.x, y = e.y).toRow)
      ).distinct
      Tuple1(ret)
    )
    val query = queryT._1
    query

  override val expectedFacts: Set[Seq[Constant]] =
    Set(Seq("a", "d"), Seq("b", "d"), Seq("b", "c"), Seq("a", "b"), Seq("z", "z"), Seq("a", "c"), Seq("c", "d"))
}


class TestTC_NonLinear_TyQL_Load extends TyQLFrontendTest with TC_Nonlinear_Load

trait TC_Nonlinear_Load extends TyQLComparative {
  val directory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/rqb_tc_test"

  def loadSchema(program: Program, storage: DuckDBStorageManager): Unit =
    val edges_schema = Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT))
    val edges = program.relation("edges", Some(edges_schema))
    storage.declareTable(edges.id, edges_schema)
    storage.edbs.initializeTable(edges.id, "edges", edges_schema)

  override def loadData(program: Program): Unit =
    val factDirectory = Paths.get(directory, "facts")
    program.ee.storageManager match
      case ddb: DuckDBStorageManager =>
        ddb.cleanup()
        loadSchema(program, ddb)
        ddb.loadFacts(factDirectory.toString)
      case _ =>
        program.loadFromFactDir(factDirectory.toString)

  override val expectedFacts = loadExpectedFile(Paths.get(directory, "expected"))("tc")

  override def generateCarac(program: Program): Relation[Constant] =
    val edges = program.namedRelation("edges")
    val X, Y, Z = program.variable()
    val tc = program.relation[Constant]("tc")
    tc(X, Y) :- (edges(X, Y))
    tc(X, Y) :- (tc(X, Z), tc(Z, Y))
    tc

  override def generateTyQL(program: Program) =
    type Edge = (x: String, y: String)
    type TCDB = (edges: Edge)

    val tables = (
      edges = Table[Edge]("edges"),
      edges2 = Table[Edge]("edges2")
    )

    val path = tables.edges
    val queryT = Query.dispatchedFix(Tuple1(path))(tPathRec =>
      val pathRec = tPathRec._1
      //    val query = path.fix(pathRec =>
      val ret = pathRec.flatMap(p =>
        pathRec
          .filter(e => p.y == e.x)
          .map(e => (x = p.x, y = e.y).toRow)
      ).distinct
      Tuple1(ret)
    )
    val query = queryT._1
    query
}