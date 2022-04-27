package datalog

import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, SimpleExecutionEngine}

class SemiNaiveTest extends  munit.FunSuite {

  def initEngineTC(): Tuple4[
    Relation[String],
    Relation[String],
    Relation[String],
    Relation[String]] = {
    given engine: ExecutionEngine = new SimpleExecutionEngine
    val program = Program()
    val e = program.relation[String]()
    val p = program.relation[String]()
    val ans2 = program.relation[String]()
    val ans3 = program.relation[String]()
    val ans4 = program.relation[String]()
    val ans5 = program.relation[String]()

    val x, y, z = program.variable()

    e("a", "b") :- ()
    e("b", "c") :- ()
    e("c", "d") :- ()
    p(x, y) :- e(x, y)
    p(x, z) :- ( e(x, y), p(y, z) )

    ans2(x) :- p("a", x)
    ans5(x) :- e("a", x) // TODO: special cased in topsort
    return (e, p, ans2, ans5)
  }

  test("edges") {
    val p = initEngineTC()

    val t1 = p._1.solve()
    assertEquals(t1, Set(
      Vector("a", "b"),
      Vector("b", "c"),
      Vector("c", "d")
    ), "solve for an edb")
  }
  test("transitiveClosure") {
    val p = initEngineTC()

    val t1 = p._2.solve()
    assertEquals(t1, Set(
      Vector("a", "d"),
      Vector("b", "d"),
      Vector("b", "c"),
      Vector("a", "b"),
      Vector("a", "c"),
      Vector("c", "d"),
    ), "simple transitive closure full idb")
  }
  test("transitiveClosureQueryE") {
    val p = initEngineTC()

    val t2 = p._4.solve()
    assertEquals(t2, Set(
      Vector("b")
    ), "e(a, x)")
  }
  test("transitiveClosureQueryP") {
    val p = initEngineTC()
    val t3 = p._3.solve()
    assertEquals(t3, Set(
      Vector("d"),
      Vector("b"),
      Vector("c"),
    ), "p(a, x)")
  }
  test("transitiveClosureIsolated") {
    given engine: ExecutionEngine = new SimpleExecutionEngine

    val program = Program()
    val e = program.relation[String]()
    val p = program.relation[String]()
    val ans2 = program.relation[String]()
    val ans3 = program.relation[String]()

    val x = program.variable()
    val y = program.variable()
    val z = program.variable()

    e("a", "b") :- ()
    e("b", "c") :- ()
    e("c", "d") :- ()
    e("a1", "b1") :- ()
    e("b1", "c1") :- ()
    e("c1", "d1") :- ()
    p(x, y) :- e(x, y)
    p(x, z) :- ( e(x, y), p(y, z) )
    ans2(x) :- p("a", x)
    ans3(x) :- p("a1", x)

    val t1 = p.solve()
    assertEquals(t1, Set(
      Vector("a", "d"),
      Vector("b", "d"),
      Vector("b", "c"),
      Vector("a", "b"),
      Vector("a", "c"),
      Vector("c", "d"),
      Vector("a1", "d1"),
      Vector("b1", "d1"),
      Vector("b1", "c1"),
      Vector("a1", "b1"),
      Vector("a1", "c1"),
      Vector("c1", "d1"),
    ), "simple transitive closure on isolated cycle")

    val t2 = ans2.solve()
    assertEquals(t2, Set(
      Vector("d"),
      Vector("b"),
      Vector("c"),
    ), "p(a, x)")

    val t3 = ans3.solve()
    assertEquals(t3, Set(
      Vector("d1"),
      Vector("b1"),
      Vector("c1"),
    ), "p(a1, x)")
  }

  test("nonRecursive") {
    given engine: ExecutionEngine = new SimpleExecutionEngine

    val program = Program()
    val edge = program.relation[String](" e")
    val oneHop = program.relation[String]("oh")
    val twoHops = program.relation[String]("th")
    val ans3 = program.relation[String]("ans1")
    val ans4 = program.relation[String]("ans2")

    val x, y, z = program.variable()

    edge("a", "a") :- ()
    edge("a", "b") :- ()
    edge("b", "c") :- ()
    edge("c", "d") :- ()
    oneHop(x, y) :- edge(x, y)
    twoHops(x, z) :- ( edge(x, y), oneHop(y, z) )
    ans3(x) :- oneHop("a", x)
    ans3(x) :- twoHops("a", x)
    ans4(x) :- (oneHop("a", x), oneHop(x, "c"))

    val t1 = ans3.solve()
    assertEquals(t1, Set(
      Vector("a"),
      Vector("b"),
      Vector("c")
    ))

    val t2 = ans4.solve()
    assertEquals(t2, Set(
      Vector("b", "b") // TODO: should this be (b, b) or (b)?
    ))
  }
}
