package test

import carac.dsl.{Constant, Program, Relation}
import carac.execution.{JITOptions, Mode, StagedExecutionEngine, TyQLExecutionEngine}
import carac.execution.ir.{IRTreeGenerator, InterpreterContext, TyQLInterpreterContext}
import carac.storage.{DatabaseType, DuckDBStorageManager, StorageTerm}
import tyql.*

import java.nio.file.{Files, Paths, Path}
import scala.jdk.StreamConverters.*
import language.experimental.namedTuples
import scala.collection.mutable

trait TyQLFrontendTest extends munit.FunSuite with TyQLComparative {
  test(s"Interpreted") {
    val (result_tyql, result_carac) = runTest(JITOptions(mode = Mode.Interpreted))
    assertEquals(result_tyql, result_carac, s"TyQL and Carac results do not match")
//    assertEquals(result_carac, expectedFacts)
    assertEquals(result_tyql, expectedFacts, s"Expected directory does not match")
  }
}

trait TyQLComparative {
  def loadData(program: Program): Unit
  def generateCarac(program: Program): Relation[Constant]
  def generateTyQL(program: Program): DatabaseAST[?]
  val expectedFacts: Set[Seq[Constant]]

  def runTest(jitOptions: JITOptions): (Set[Seq[StorageTerm]], Set[Seq[StorageTerm]]) = {
    val storage_tyql = new DuckDBStorageManager()
    val engine_tyql = new TyQLExecutionEngine(storage_tyql, jitOptions)
    val program_tyql = Program(engine_tyql)
    loadData(program_tyql)
    val query_tyql = generateTyQL(program_tyql)
    val result_tyql = engine_tyql.solveTyQL(query_tyql)

    val storage_carac = new DuckDBStorageManager()
    val engine_carac = new StagedExecutionEngine(storage_carac)
    val program_carac = Program(engine_carac)
    loadData(program_carac)
    val toSolve_carac = generateCarac(program_carac)
    val result_carac = engine_carac.solve(toSolve_carac.id)
    (result_tyql, result_carac)
  }

  def loadExpectedFile(expectedDirectory: Path): mutable.Map[String, Set[Seq[Constant]]] =
    val expectedFacts = mutable.Map[String, Set[Seq[Constant]]]()
    if (!Files.exists(expectedDirectory)) throw new Exception(s"Missing expected directory '$expectedDirectory'")
    Files.walk(expectedDirectory, 1)
      .filter(p => Files.isRegularFile(p) && p.toString.endsWith(".csv"))
      .forEach(f => {
        val rule = f.getFileName.toString.replaceFirst("[.][^.]+$", "")
        val reader = Files.newBufferedReader(f)
        val headers = reader.readLine().split("\t")
        val expected = reader.lines()
          .map(l => l.split("\t").zipWithIndex.map((s, i) =>
            (headers(i) match {
              case "Int" => s.toInt
              case "String" => s
              case _ => throw new Exception(s"Unknown type ${headers(i)} in file ${f.getFileName}")
            }).asInstanceOf[StorageTerm]
          ).toSeq)
          .toScala(Set)
        expectedFacts(rule) = expected
        reader.close()
      })
    expectedFacts
}

class TyQLFrontendNaiveTest extends munit.FunSuite {
  test("TC linear") {
    val storage_tyql = new DuckDBStorageManager()
    val engine_tyql = new TyQLExecutionEngine(storage_tyql)
    val program_tyql = Program(engine_tyql)
    val base_tyql = program_tyql.relation[Constant]("edges", Some(Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT))))
    base_tyql("a", "b") :- ()
    base_tyql("b", "c") :- ()
    base_tyql("c", "d") :- ()
    base_tyql("z", "z") :- ()

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

    val result_tyql = engine_tyql.solveTyQL(query, naive = true)

    val storage_carac = new DuckDBStorageManager()
    val engine_carac = new StagedExecutionEngine(storage_carac)
    val program_carac = Program(engine_carac)
    val base_carac = program_carac.relation[Constant]("edges")
    base_carac("a", "b") :- ()
    base_carac("b", "c") :- ()
    base_carac("c", "d") :- ()
    base_carac("z", "z") :- ()
    val tc = program_carac.relation[Constant]("tc")
    val X, Y, Z = program_carac.variable()
    tc(X,Y) :- ( base_carac(X,Y) )
    tc(X,Y) :- ( tc(X,Z),tc(Z,Y) )

    val result_carac = engine_carac.solve(tc.id)
    val expected = Set(Seq("a", "d"), Seq("b", "d"), Seq("b", "c"), Seq("a", "b"), Seq("z", "z"), Seq("a", "c"), Seq("c", "d"))

    assertEquals(result_tyql, result_carac)
    assertEquals(result_tyql.asInstanceOf[Set[Seq[String]]], expected)
  }

  test("TC nonlinear") {
    val storage_tyql = new DuckDBStorageManager()
    val engine_tyql = new TyQLExecutionEngine(storage_tyql)
    val program_tyql = Program(engine_tyql)
    val base_tyql = program_tyql.relation[Constant]("edges", Some(Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT))))
    base_tyql("a","b") :- ()
    base_tyql("b","c") :- ()
    base_tyql("c","d") :- ()
    base_tyql("z", "z") :- ()

    // TyQL program
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

    val result_tyql = engine_tyql.solveTyQL(query, naive = true)

    val storage_carac = new DuckDBStorageManager()
    val engine_carac = new StagedExecutionEngine(storage_carac)
    val program_carac = Program(engine_carac)
    val base_carac = program_carac.relation[Constant]("edges")
    base_carac("a", "b") :- ()
    base_carac("b", "c") :- ()
    base_carac("c", "d") :- ()
    base_carac("z", "z") :- ()
    val X, Y, Z = program_carac.variable()
    val tc = program_carac.relation[Constant]("tc")
    tc(X, Y) :- (base_carac(X, Y))
    tc(X, Y) :- (tc(X, Z), tc(Z, Y))
    val result_carac = engine_carac.solve(tc.id)

    val expected = Set(Seq("a", "d"), Seq("b", "d"), Seq("b", "c"), Seq("a", "b"), Seq("z", "z"), Seq("a", "c"), Seq("c", "d"))

    assertEquals(result_tyql, result_carac)
    assertEquals(result_tyql.asInstanceOf[Set[Seq[String]]], expected)
  }
  test("TC nonlinear final filter") {
    val storage_tyql = new DuckDBStorageManager()
    val engine_tyql = new TyQLExecutionEngine(storage_tyql)
    val program_tyql = Program(engine_tyql)
    val base_tyql = program_tyql.relation[Constant]("edges", Some(Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT))))
    base_tyql("a","b") :- ()
    base_tyql("b","c") :- ()
    base_tyql("c","d") :- ()
    base_tyql("z", "z") :- ()

    // TyQL program
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
    val query = queryT._1.filter(t => t.x == "a")

    val result_tyql = engine_tyql.solveTyQL(query, naive = true)

    val expected = Set(Seq("a", "d"), Seq("a", "b"), Seq("a", "c"))

    assertEquals(result_tyql.asInstanceOf[Set[Seq[String]]], expected)
  }

  test("TC n-ary nonlinear") {
    val storage_tyql = new DuckDBStorageManager()
    val engine_tyql = new TyQLExecutionEngine(storage_tyql)
    val program_tyql = Program(engine_tyql)
    val base_tyql = program_tyql.relation[Constant]("edges", Some(Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT))))
    base_tyql("a", "b") :- ()
    base_tyql("b", "c") :- ()
    base_tyql("c", "d") :- ()
    base_tyql("z", "z") :- ()
    val base_tyql2 = program_tyql.relation[Constant]("edges2", Some(Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT))))
    base_tyql2("a", "b") :- ()


    // TyQL program
    type Edge = (x: String, y: String)
    type TCDB = (edges: Edge)

    val tables = (
      edges1 = Table[Edge]("edges"), // must match the names of the Datalog program EDBs
      edges2 = Table[Edge]("edges2")
    )

    val path1 = tables.edges1
    val path2 = tables.edges2
    val queryT = Query.dispatchedFix((path1, path2))(tPathRec =>
      val pathRec1 = tPathRec._1
      val pathRec2 = tPathRec._2
      val ret1 = pathRec1.flatMap(p =>
        pathRec1
          .filter(e => p.y == e.x)
          .map(e => (x = p.x, y = e.y).toRow)
      ).distinct
      val ret2 = pathRec2.flatMap(p =>
        pathRec2
          .filter(e => p.y == e.x)
          .map(e => (x = p.x, y = e.y).toRow)
      ).distinct
      (ret1, ret2)
    )

    val result_tyql = engine_tyql.solveTyQL(queryT._1, naive = true)
//    val result_tyql = engine_tyql.solveTyQL(queryT._2)

    val storage_carac = new DuckDBStorageManager()
    val engine_carac = new StagedExecutionEngine(storage_carac)
    val program_carac = Program(engine_carac)
    val base_carac = program_carac.relation[Constant]("edges")
    base_carac("a", "b") :- ()
    base_carac("b", "c") :- ()
    base_carac("c", "d") :- ()
    base_carac("z", "z") :- ()
    val X, Y, Z = program_carac.variable()
    val tc1 = program_carac.relation[Constant]("tc1")
    tc1(X, Y) :- (base_carac(X, Y))
    tc1(X, Y) :- (tc1(X, Z), tc1(Z, Y))
    val tc2 = program_carac.relation[Constant]("tc2")
    tc2(X, Y) :- (base_carac(X, Y))
    tc2(X, Y) :- (tc2(X, Z), tc2(Z, Y))

    val result_carac = engine_carac.solve(tc1.id)
//    val result_carac = engine_carac.solve(tc2.id)

    val expected = Set(Seq("a", "d"), Seq("b", "d"), Seq("b", "c"), Seq("a", "b"), Seq("z", "z"), Seq("a", "c"), Seq("c", "d"))

    assertEquals(result_tyql, result_carac)
    assertEquals(result_tyql.asInstanceOf[Set[Seq[String]]], expected)
  }
}

class TyQLFrontendSemiNaiveTest extends munit.FunSuite {
  test("TC nonlinear") {
    val storage_carac = new DuckDBStorageManager()
    val engine_carac = new StagedExecutionEngine(storage_carac)
    val program_carac = Program(engine_carac)
    val base_carac = program_carac.relation[Constant]("edges")
    base_carac("a", "b") :- ()
    base_carac("b", "c") :- ()
    base_carac("c", "d") :- ()
    base_carac("z", "z") :- ()
    val X, Y, Z = program_carac.variable()
    val tc = program_carac.relation[Constant]("tc")
    tc(X, Y) :- (base_carac(X, Y))
    tc(X, Y) :- (tc(X, Z), tc(Z, Y))
    val result_carac = engine_carac.solve(tc.id)

    val storage_tyql = new DuckDBStorageManager()
    val engine_tyql = new TyQLExecutionEngine(storage_tyql)
    val program_tyql = Program(engine_tyql)
    val base_tyql = program_tyql.relation[Constant]("edges", Some(Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT))))
    base_tyql("a","b") :- ()
    base_tyql("b","c") :- ()
    base_tyql("c","d") :- ()
    base_tyql("z", "z") :- ()

    // TyQL program
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

    val result_tyql = engine_tyql.solveTyQL(query)

    val expected = Set(Seq("a", "d"), Seq("b", "d"), Seq("b", "c"), Seq("a", "b"), Seq("z", "z"), Seq("a", "c"), Seq("c", "d"))

    assertEquals(result_tyql, result_carac)
    assertEquals(result_tyql.asInstanceOf[Set[Seq[String]]], expected)
  }
  test("TC nonlinear final filter") {
    val storage_tyql = new DuckDBStorageManager()
    val engine_tyql = new TyQLExecutionEngine(storage_tyql)
    val program_tyql = Program(engine_tyql)
    val base_tyql = program_tyql.relation[Constant]("edges", Some(Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT))))
    base_tyql("a","b") :- ()
    base_tyql("b","c") :- ()
    base_tyql("c","d") :- ()
    base_tyql("z", "z") :- ()

    // TyQL program
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
    val query = queryT._1.filter(t => t.x == "a")

    val result_tyql = engine_tyql.solveTyQL(query)

    val expected = Set(Seq("a", "d"), Seq("a", "b"), Seq("a", "c"))

    assertEquals(result_tyql.asInstanceOf[Set[Seq[String]]], expected)
  }

  test("TC n-ary nonlinear") {
    val storage_tyql = new DuckDBStorageManager()
    val engine_tyql = new TyQLExecutionEngine(storage_tyql)
    val program_tyql = Program(engine_tyql)
    val base_tyql = program_tyql.relation[Constant]("edges", Some(Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT))))
    base_tyql("a", "b") :- ()
    base_tyql("b", "c") :- ()
    base_tyql("c", "d") :- ()
    base_tyql("z", "z") :- ()
    val base_tyql2 = program_tyql.relation[Constant]("edges2", Some(Seq(("x", DatabaseType.TEXT), ("y", DatabaseType.TEXT))))
    base_tyql2("a", "b") :- ()


    // TyQL program
    type Edge = (x: String, y: String)
    type TCDB = (edges: Edge)

    val tables = (
      edges1 = Table[Edge]("edges"), // must match the names of the Datalog program EDBs
      edges2 = Table[Edge]("edges2")
    )

    val path1 = tables.edges1
    val path2 = tables.edges2
    val queryT = Query.dispatchedFix((path1, path2))(tPathRec =>
      val pathRec1 = tPathRec._1
      val pathRec2 = tPathRec._2
      val ret1 = pathRec1.flatMap(p =>
        pathRec1
          .filter(e => p.y == e.x)
          .map(e => (x = p.x, y = e.y).toRow)
      ).distinct
      val ret2 = pathRec2.flatMap(p =>
        pathRec2
          .filter(e => p.y == e.x)
          .map(e => (x = p.x, y = e.y).toRow)
      ).distinct
      (ret1, ret2)
    )

    val result_tyql = engine_tyql.solveTyQL(queryT._1)
    //    val result_tyql = engine_tyql.solveTyQL(queryT._2)

    val storage_carac = new DuckDBStorageManager()
    val engine_carac = new StagedExecutionEngine(storage_carac)
    val program_carac = Program(engine_carac)
    val base_carac = program_carac.relation[Constant]("edges")
    base_carac("a", "b") :- ()
    base_carac("b", "c") :- ()
    base_carac("c", "d") :- ()
    base_carac("z", "z") :- ()
    val X, Y, Z = program_carac.variable()
    val tc1 = program_carac.relation[Constant]("tc1")
    tc1(X, Y) :- (base_carac(X, Y))
    tc1(X, Y) :- (tc1(X, Z), tc1(Z, Y))
    val tc2 = program_carac.relation[Constant]("tc2")
    tc2(X, Y) :- (base_carac(X, Y))
    tc2(X, Y) :- (tc2(X, Z), tc2(Z, Y))

    val result_carac = engine_carac.solve(tc1.id)
    //    val result_carac = engine_carac.solve(tc2.id)

    val expected = Set(Seq("a", "d"), Seq("b", "d"), Seq("b", "c"), Seq("a", "b"), Seq("z", "z"), Seq("a", "c"), Seq("c", "d"))

    assertEquals(result_tyql, result_carac)
    assertEquals(result_tyql.asInstanceOf[Set[Seq[String]]], expected)
  }
}
