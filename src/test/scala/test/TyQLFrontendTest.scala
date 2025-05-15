package test

import carac.dsl.{Constant, Program, Relation}
import carac.execution.{StagedExecutionEngine, TyQLExecutionEngine}
import carac.execution.ir.{IRTreeGenerator, InterpreterContext, TyQLInterpreterContext}
import carac.storage.{DatabaseType, DuckDBStorageManager}
import tyql.*

import language.experimental.namedTuples

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
    val engine_carac = new TyQLExecutionEngine(storage_carac)
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
    val base_tyql = program_tyql.relation[Constant]("edges")
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
    val engine_carac = new TyQLExecutionEngine(storage_carac)
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
    val base_tyql = program_tyql.relation[Constant]("edges")
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
    val engine_carac = new TyQLExecutionEngine(storage_carac)
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
