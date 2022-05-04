//package SemiNaive
//
//package NaiveTest.SemiNaive
//import datalog.dsl.{Program, Relation}
//import datalog.execution.{ExecutionEngine, SimpleExecutionEngine}
//
//class SNMultiCycleTC extends  munit.FunSuite {
//  def initGraph(program: Program): (
//    Relation[String],
//      Relation[String],
//      Relation[String]) = {
//    given engine: ExecutionEngine = new SimpleExecutionEngine
//    val program = Program()
//
//    val e = program.relation[String]()
//    val p = program.relation[String]()
//    val path2a = program.relation[String]()
//    val path2a1 = program.relation[String]()
//    val empty1 = program.relation[String]()
//    val empty2 = program.relation[String]()
//    val edge2a = program.relation[String]()
//
//    val x, y, z = program.variable()
//
//    e("a", "b") :- ()
//    e("b", "c") :- ()
//    e("c", "d") :- ()
//    p(x, y) :- e(x, y)
//    p(x, z) :- (e(x, y), p(y, z))
//
//    path2a(x) :- p("a", x)
//    edge2a(x) :- e("a", x)
//
//    e("a1", "b1") :- ()
//    e("b1", "c1") :- ()
//    e("c1", "d1") :- ()
//    path2a1(x) :- p("a1", x)
//    (p, path2a, path2a1)
//  }
//
//  test("solve for path") {
//    given engine: ExecutionEngine = new SimpleExecutionEngine
//
//    val program = Program()
//    val p = initGraph(program)._1
//    val t1 = p.solve()
//    assertEquals(t1, Set(
//      Vector("a", "d"),
//      Vector("b", "d"),
//      Vector("b", "c"),
//      Vector("a", "b"),
//      Vector("a", "c"),
//      Vector("c", "d"),
//      Vector("a1", "d1"),
//      Vector("b1", "d1"),
//      Vector("b1", "c1"),
//      Vector("a1", "b1"),
//      Vector("a1", "c1"),
//      Vector("c1", "d1"),
//    ), "simple transitive closure on isolated cycle")
//  }
//  test("query for path to a") {
//    given engine: ExecutionEngine = new SimpleExecutionEngine
//
//    val program = Program()
//    val path2a = initGraph(program)._2
//    val t2 = path2a.solve()
//    assertEquals(t2, Set(
//      Vector("d"),
//      Vector("b"),
//      Vector("c"),
//    ), "p(a, x)")
//  }
//  test("query for path to a1") {
//    given engine: ExecutionEngine = new SimpleExecutionEngine
//
//    val program = Program()
//    val path2a1 = initGraph(program)._2
//    val t3 = path2a1.solve()
//    assertEquals(t3, Set(
//      Vector("d1"),
//      Vector("b1"),
//      Vector("c1"),
//    ), "p(a1, x)")
//  }
//}

//class NaiveSingleCycleTC extends munit.FunSuite {
//
//  def initEngine(): (Relation[String], Relation[String], Relation[String]) = {
//    given engine: ExecutionEngine = new SimpleExecutionEngine
//    val program = Program()
//    val e = program.relation[String]()
//    val p = program.relation[String]()
//    val ans1 = program.relation[String]()
//    val ans2 = program.relation[String]()
//    val ans3 = program.relation[String]()
//
//    val x, y, z = program.variable()
//
//    e("a", "b") :- ()
//    e("b", "c") :- ()
//    e("c", "d") :- ()
//    p(x, y) :- e(x, y)
//    p(x, z) :- ( e(x, y), p(y, z) )
//
//    ans1(x) :- e("a", x)
//    ans2(x) :- p("a", x)
//    (p, ans1, ans2)
//  }
//
//  test("transitiveClosureQueryP") {
//    val p = initEngine()
//    val t3 = p._3.solveNaive()
//    assertEquals(t3, Set(
//      Vector("d"),
//      Vector("b"),
//      Vector("c"),
//    ), "p(a, x)")
//  }
//  test("transitiveClosure") {
//    val p = initEngine()
//
//    val t1 = p._1.solveNaive()
//    assertEquals(t1, Set(
//      Vector("a", "d"),
//      Vector("b", "d"),
//      Vector("b", "c"),
//      Vector("a", "b"),
//      Vector("a", "c"),
//      Vector("c", "d"),
//    ), "simple transitive closure")
//  }
//  test("transitiveClosureQueryE") {
//    val p = initEngine()
//
//    val t2 = p._2.solveNaive()
//    assertEquals(t2, Set(
//      Vector("b")
//    ), "e(a, x)")
//  }
//
//  test("nonRecursive") {
//    given engine: ExecutionEngine = new SimpleExecutionEngine
//
//    val program = Program()
//    val edge = program.relation[String](" e")
//    val oneHop = program.relation[String]("oh")
//    val twoHops = program.relation[String]("th")
//    val ans1 = program.relation[String]("ans1")
//    val ans2 = program.relation[String]("ans2")
//
//    val x, y, z = program.variable()
//
//    edge("a", "a") :- ()
//    edge("a", "b") :- ()
//    edge("b", "c") :- ()
//    edge("c", "d") :- ()
//    oneHop(x, y) :- edge(x, y)
//    twoHops(x, z) :- ( edge(x, y), oneHop(y, z) )
//    ans1(x) :- oneHop("a", x)
//    ans1(x) :- twoHops("a", x)
//    ans2(x) :- (oneHop("a", x), oneHop(x, "c"))
//
//    val t1 = ans1.solveNaive()
//    assertEquals(t1, Set(
//      Vector("a"),
//      Vector("b"),
//      Vector("c")
//    ))
//
//    val t2 = ans2.solveNaive()
//    assertEquals(t2, Set(
//      Vector("b", "b") // TODO: should this be (b, b) or (b)?
//    ))
//  }
//
//}

//package SemiNaive
//
//import datalog.dsl.{Program, Relation}
//import datalog.execution.{ExecutionEngine, SimpleExecutionEngine}
//
//class SNSingleCycleTC extends  munit.FunSuite {
//  def initGraph(program: Program): (
//    Relation[String],
//    Relation[String],
//    Relation[String],
//    Relation[String]) = {
//    val e = program.relation[String]()
//    val p = program.relation[String]()
//    val path2a = program.relation[String]()
//    val empty1 = program.relation[String]()
//    val empty2 = program.relation[String]()
//    val edge2a = program.relation[String]()
//
//    val x, y, z = program.variable()
//
//    e("a", "b") :- ()
//    e("b", "c") :- ()
//    e("c", "d") :- ()
//    p(x, y) :- e(x, y)
//    p(x, z) :- ( e(x, y), p(y, z) )
//
//    path2a(x) :- p("a", x)
//    edge2a(x) :- e("a", x)
//    (e, p, path2a, edge2a)
//  }
//
//  test("solve for edge") {
//    given engine: ExecutionEngine = new SimpleExecutionEngine
//    val program = Program()
//    val e  = initGraph(program)._1
//
//    val t1 = e.solve()
//    assertEquals(t1, Set(
//      Vector("a", "b"),
//      Vector("b", "c"),
//      Vector("c", "d")
//    ), "solve for an edb")
//  }
//  test("solve for path") {
//    given engine: ExecutionEngine = new SimpleExecutionEngine
//    val program = Program()
//    val p = initGraph(program)._2
//
//    val t1 = p.solve()
//    assertEquals(t1, Set(
//      Vector("a", "d"),
//      Vector("b", "d"),
//      Vector("b", "c"),
//      Vector("a", "b"),
//      Vector("a", "c"),
//      Vector("c", "d"),
//    ), "simple transitive closure full idb")
//  }
//
//  test("query for edge to a") {
//    given engine: ExecutionEngine = new SimpleExecutionEngine
//    val program = Program()
//    val edge2a = initGraph(program)._4
//
//    val t2 = edge2a.solve()
//    assertEquals(t2, Set(
//      Vector("b")
//    ), "e(a, x)")
//  }
//
//  test("query for path to a") {
//    given engine: ExecutionEngine = new SimpleExecutionEngine
//    val program = Program()
//    val path2a = initGraph(program)._3
//    val t3 = path2a.solve()
//    assertEquals(t3, Set(
//      Vector("d"),
//      Vector("b"),
//      Vector("c"),
//    ), "p(a, x)")
//  }
//}
