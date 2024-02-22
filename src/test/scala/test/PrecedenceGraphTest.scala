package test
import datalog.execution.{ExecutionEngine, PrecedenceGraph, SemiNaiveExecutionEngine}

import scala.collection.immutable.ArraySeq
import scala.collection.mutable
import datalog.dsl.{Program, Constant, Atom, __}
import datalog.storage.{NS, VolcanoStorageManager}

class PrecedenceGraphTest extends munit.FunSuite {
  test("tarjan with tarjan example") {
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new VolcanoStorageManager())
    val program = Program(engine)
    val t0 = program.relation[Constant]("t0")
    val t1 = program.relation[Constant]("t1")
    val t2 = program.relation[Constant]("t2")
    val t3 = program.relation[Constant]("t3")
    val t4 = program.relation[Constant]("t4")
    val t5 = program.relation[Constant]("t5")
    val t6 = program.relation[Constant]("t6")
    val t7 = program.relation[Constant]("t7")
    val t8 = program.relation[Constant]("t8")
    val t9 = program.relation[Constant]("t9")
    val t10 = program.relation[Constant]("t10")
    val x = program.variable()

    t0(x) :- t1(x)
    t1(x) :- (t4(x), t6(x), t7(x))
    t2(x) :- (t4(x), t6(x), t7(x))
    t3(x) :- (t4(x), t6(x), t7(x))
    t4(x) :- (t2(x), t3(x))
    t5(x) :- (t2(x), t3(x))
    t6(x) :- (t5(x), t8(x))
    t7(x) :- (t5(x), t8(x))
    t10(x) :- t10(x)

    assertEquals(
      engine.precedenceGraph.tarjan(),
      Seq(Set(t8.id), Set(t2.id, t3.id, t4.id, t5.id, t6.id, t7.id), Set(t1.id), Set(t0.id), Set(t10.id))
    )
    assertEquals(
      engine.precedenceGraph.tarjan(Some(t10.id)),
      Seq(Set(t10.id), Set(t8.id), Set(t2.id, t3.id, t4.id, t5.id, t6.id, t7.id), Set(t1.id), Set(t0.id))
    )
    assertEquals(
      engine.precedenceGraph.dropIrrelevant(
        engine.precedenceGraph.tarjan(Some(t10.id)),
        Some(t10.id)
      ).flatten,
      Seq(t10.id),
    )
    // There's a single component with the 6 nodes
    assertEquals(
      engine.precedenceGraph.dropIrrelevant(
        engine.precedenceGraph.tarjan(Some(t4.id)),
        Some(t4.id)
      ).flatten.toSet,
      Set(t2.id, t3.id, t4.id, t5.id, t6.id, t7.id)
    )
    assertEquals(
      engine.precedenceGraph.dropIrrelevant(
        engine.precedenceGraph.tarjan(Some(t8.id)),
        Some(t8.id)
      ).flatten,
      Seq() // TODO: empty
    )
  }
  test("negated tarjan example") {
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new VolcanoStorageManager())

    val program = Program(engine)
    val t0 = program.relation[Constant]("t0")
    val t1 = program.relation[Constant]("t1")
    val t2 = program.relation[Constant]("t2")
    val t3 = program.relation[Constant]("t3")
    val t4 = program.relation[Constant]("t4")
    val t5 = program.relation[Constant]("t5")
    val t6 = program.relation[Constant]("t6")
    val t7 = program.relation[Constant]("t7")
    val t8 = program.relation[Constant]("t8")
    val t9 = program.relation[Constant]("t9")
    val t10 = program.relation[Constant]("t10")
    val x = program.variable()

    t0("x") :- !t1(__)
    t1(x) :- (!t4(x), t6(x), t7(x))
    t2(x) :- (t4(x), t6(x), t7(x))
    t3(x) :- (t4(x), t6(x), t7(x))
    t4(x) :- (t2(x), t3(x))
    t5(x) :- (t2(x), t3(x))
    t6(x) :- (t5(x), !t8(x))
    t7(x) :- (t5(x), t8(x))
    t10("x") :- !t1(__)
    t10(x) :- t10(x)
    t10("x") :- !t0(__)

    // ullman
    assertEquals(
      engine.precedenceGraph.ullman(),
      Seq(Set(t8.id), Set(t2.id, t3.id, t4.id, t5.id, t6.id, t7.id), Set(t1.id), Set(t0.id), Set(t10.id))
    )
    assertEquals(
      engine.precedenceGraph.dropIrrelevant(
        engine.precedenceGraph.ullman(),
        Some(t0.id)
      ),
      Seq(Set(5, 6, 2, 7, 3, 4), Set(1), Set(0)), // remove 8 bc IDB, then remove 10 because later in dependency
    )
    // There's a single component with the 6 nodes
    assertEquals(
      engine.precedenceGraph.dropIrrelevant(
        engine.precedenceGraph.ullman(Some(t4.id)),
        Some(t4.id)
      ).flatten.toSet,
      Set(t2.id, t3.id, t4.id, t5.id, t6.id, t7.id)
    )
    assertEquals(
      engine.precedenceGraph.dropIrrelevant(
        engine.precedenceGraph.ullman(Some(t8.id)),
        Some(t8.id)
      ).flatten,
      Seq() // Empty bc 8 is EDB
    )

    // tarjan
    assertEquals(
      engine.precedenceGraph.tarjan(),
      Seq(Set(t8.id), Set(t2.id, t3.id, t4.id, t5.id, t6.id, t7.id), Set(t1.id), Set(t0.id), Set(t10.id))
    )
    assertEquals(
      engine.precedenceGraph.dropIrrelevant(
        engine.precedenceGraph.tarjan(),
        Some(t0.id)
      ),
      Seq(Set(5, 6, 2, 7, 3, 4), Set(1), Set(0)), // remove 8 bc IDB, then remove 10 because later in dependency
    )
    // There's a single component with the 6 nodes
    assertEquals(
      engine.precedenceGraph.dropIrrelevant(
        engine.precedenceGraph.tarjan(Some(t4.id)),
        Some(t4.id)
      ),
      Seq(Set(t2.id, t3.id, t4.id, t5.id, t6.id, t7.id))
    )
    assertEquals(
      engine.precedenceGraph.dropIrrelevant(
        engine.precedenceGraph.tarjan(Some(t8.id)),
        Some(t8.id)
      ).flatten,
      Seq() // Empty bc 8 is EDB
    )
  }
  test("tarjan tc with isolated cycles") {
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new VolcanoStorageManager())

    val program = Program(engine)
    val e = program.relation[String]("e")
    val p = program.relation[String]("p")
    val other = program.relation[String]("other")
    val e2 = program.relation[String]("e2")
    val p2 = program.relation[String]("p2")
    val other2 = program.relation[String]("other2")
    val x, y, z = program.variable()

    e("a", "b") :- ()
    e("b", "c") :- ()
    e("c", "d") :- ()
    p(x, y) :- e(x, y)
    p(x, z) :- (e(x, y), p(y, z))
    other(x) :- p("a", x)

    e2("a", "b") :- ()
    e2("b", "c") :- ()
    e2("c", "d") :- ()
    p2(x, y) :- e2(x, y)
    p2(x, z) :- (e2(x, y), p2(y, z))
    other2(x) :- p2("a", x)

    assertEquals(
      engine.precedenceGraph.tarjan().toSet,
      Set(Set(e.id), Set(p.id), Set(other.id), Set(e2.id), Set(p2.id), Set(other2.id))
    )
    assertEquals(
      engine.precedenceGraph.dropIrrelevant(engine.precedenceGraph.tarjan(Some(other.id)), Some(other.id)).flatten,
      Seq(p.id, other.id)
    )
    assertEquals(
        engine.precedenceGraph.dropIrrelevant(engine.precedenceGraph.tarjan(Some(other2.id)), Some(other2.id)).flatten,
      Seq(p2.id, other2.id)
    )
  }
  test("negated tc with isolated cycles") {
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new VolcanoStorageManager())

    val program = Program(engine)
    val e = program.relation[String]("e") // 0
    val p = program.relation[String]("p") // 1
    val other = program.relation[String]("other") // 2
    val e2 = program.relation[String]("e2") // 3
    val p2 = program.relation[String]("p2") // 4
    val other2 = program.relation[String]("other2") // 5
    val x, y, z = program.variable()

    e("a", "b") :- ()
    e("b", "c") :- ()
    e("c", "d") :- ()
    p("a", "b") :- !e(__, __)
    p(x, z) :- (e(x, y), p(y, z))
    other("x") :- !p("a", "x")

    e2("a", "b") :- ()
    e2("b", "c") :- ()
    e2("c", "d") :- ()
    p2("x", "y") :- !e2("x", "y")
    p2(x, z) :- (e2(x, y), p2(y, z))
    other2("x") :- !p2("a", "x")

    // ullman
    assertEquals(
      engine.precedenceGraph.ullman(),
      Seq(Set(e.id, e2.id), Set(p.id, p2.id), Set(other2.id, other.id))
    )
    assertEquals(
      engine.precedenceGraph.dropIrrelevant(engine.precedenceGraph.ullman(Some(other.id)), Some(other.id)),
      Seq(Set(p.id, p2.id), Set(other.id, other2.id))
    )
    assertEquals(
      engine.precedenceGraph.dropIrrelevant(engine.precedenceGraph.ullman(Some(p2.id)), Some(p2.id)),
      Seq(Set(p2.id, p.id))
    )
    // tarjan has a slightly different stratification
    assertEquals(
      engine.precedenceGraph.tarjan(),
      Seq(Set(e.id), Set(p.id), Set(other.id), Set(e2.id), Set(p2.id), Set(other2.id))
    )
    assertEquals(
      engine.precedenceGraph.dropIrrelevant(engine.precedenceGraph.tarjan(Some(other.id)), Some(other.id)),
      Seq(Set(p.id), Set(other.id))
    )
    assertEquals(
      engine.precedenceGraph.dropIrrelevant(engine.precedenceGraph.tarjan(Some(p2.id)), Some(p2.id)),
      Seq(Set(p2.id))
    )
  }
  test("tarjan transitive closure") {
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new VolcanoStorageManager())
    val program = Program(engine)
    val e = program.relation[String]("e")
    val p = program.relation[String]("p")
    val other = program.relation[String]("other")
    val x, y, z = program.variable()

    e("a", "b") :- ()
    e("b", "c") :- ()
    e("c", "d") :- ()
    p(x, y) :- e(x, y)
    p(x, z) :- ( e(x, y), p(y, z) )
    other(x) :- p("a", x)

    assertEquals(
      engine.precedenceGraph.tarjan(),
      Seq(Set(e.id), Set(p.id), Set(other.id))
    )
  }

  test("simple positive cycle") {
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new VolcanoStorageManager())
    val program = Program(engine)
    val a = program.relation[String]("a")
    val b = program.relation[String]("b")
    val c = program.relation[String]("c")
    val other = program.relation[String]("other")

    a() :- b()
    b() :- c()
    c() :- a()
    a() :- other()

    assertEquals(
      engine.precedenceGraph.tarjan(),
      Seq(Set(other.id), Set(a.id, b.id, c.id))
    )
    assertEquals(
      engine.precedenceGraph.ullman(),
      Seq(Set(other.id, a.id, b.id, c.id)) // no negation, 1 strata
    )
  }
  test("simple negative cycle") {
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new VolcanoStorageManager())

    val program = Program(engine)
    val a = program.relation[String]("a")
    val b = program.relation[String]("b")
    val c = program.relation[String]("c")

    a() :- !b()
    b() :- !c()
    c() :- !a()

    interceptMessage[java.lang.Exception]("Negative cycle detected in input program") {
      engine.precedenceGraph.tarjan()
    }
    interceptMessage[java.lang.Exception]("Negative cycle detected in input program") {
      engine.precedenceGraph.ullman()
    }
  }
  test("simple self negative cycle") {
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new VolcanoStorageManager())

    val program = Program(engine)
    val a = program.relation[String]("a")
    val b = program.relation[String]("b")
    val c = program.relation[String]("c")
    val d = program.relation[String]("d")

    a() :- b()
    b() :- c()
    c() :- a()
    d() :- !d()

    interceptMessage[java.lang.Exception]("Negative cycle detected in input program") {
      engine.precedenceGraph.tarjan()
    }
    interceptMessage[java.lang.Exception]("Negative cycle detected in input program") {
      engine.precedenceGraph.ullman()
    }
  }
  test("simple cycle with 1 negative strata") {
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new VolcanoStorageManager())

    val program = Program(engine)
    val a = program.relation[String]("a")
    val b = program.relation[String]("b")
    val c = program.relation[String]("c")
    val other = program.relation[String]("other")

    a() :- b()
    b() :- c()
    c() :- a()
    a() :- !other()

    assertEquals(
      engine.precedenceGraph.tarjan(),
      Seq(Set(other.id), Set(a.id, b.id, c.id))
    )
    assertEquals(
      engine.precedenceGraph.ullman(),
      Seq(Set(other.id), Set(a.id, b.id, c.id))
    )
  }

  test("tarjan simple cycle with inner loop") {
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new VolcanoStorageManager())
    val program = Program(engine)
    val a = program.relation[String]("a")
    val b = program.relation[String]("b")
    val c = program.relation[String]("c")
    val other = program.relation[String]("other")

    a() :- b()
    a() :- (a(), b())
    b() :- c()
    c() :- a()
    a() :- other()

    assertEquals(
      engine.precedenceGraph.tarjan(),
      Seq(Set(other.id), Set(a.id, b.id, c.id))
    )
  }

  test("tarjan souffle top order test") {
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new VolcanoStorageManager())
    val program = Program(engine)
    val a = program.relation[String]("a")
    val b = program.relation[String]("b")
    val c = program.relation[String]("c")
    val other = program.relation[String]("other")

    a() :- b()
    a() :- (a(), b())
    b() :- c()
    c() :- a()
    a() :- other()

    assertEquals(
      engine.precedenceGraph.tarjan(Some(a.id)),
      Seq(Set(other.id), Set(a.id, b.id, c.id))
    )
  }

  test("simple alias removal positive") {
    val adjacency = Map(
      0 -> Seq(),
      1 -> Seq(0),
      2 -> Seq(1),
    )

    val graph = new PrecedenceGraph(using new NS())
    for ((node, deps) <- adjacency) {
      graph.addNode(Atom(node, ArraySeq.empty, false) +: deps.map(d => Atom(d, ArraySeq.empty, false)))
    }

    assertEquals(
      graph.scc(2).flatten,
      Seq(0, 1, 2),
    )
    graph.updateNodeAlias(mutable.Map(1 -> 0))
    assertEquals(
      graph.scc(2).flatten,
      Seq(0, 2),
    )
  }

  test("consecutive aliases removal positive") {
    val adjacencyList = Map(
      0 -> Seq(),
      1 -> Seq(0),
      2 -> Seq(1),
      3 -> Seq(2),
    )

    val graph = new PrecedenceGraph(using new NS())
    for ((node, deps) <- adjacencyList) {
      graph.addNode(Atom(node, ArraySeq.empty, false) +: deps.map(d => Atom(d, ArraySeq.empty, false)))
    }

    assertEquals(
      graph.scc(3).flatten,
      Seq(0, 1, 2, 3),
    )
    graph.updateNodeAlias(mutable.Map(2 -> 1, 1 -> 0))
    assertEquals(
      graph.scc(3).flatten,
      Seq(0, 3),
    )
  }
  test("simple alias removal negative") {
    val negAdjacency = Map(
      0 -> Seq(),
      2 -> Seq(1),
    )
    val posAdjacency = Map(
      1 -> Seq(0),
    )

    val graph = new PrecedenceGraph(using new NS())
    for ((node, deps) <- negAdjacency) {
      graph.addNode(Atom(node, ArraySeq.empty, false) +: deps.map(d => Atom(d, ArraySeq.empty, true)))
    }
    for ((node, deps) <- posAdjacency) {
      graph.addNode(Atom(node, ArraySeq.empty, false) +: deps.map(d => Atom(d, ArraySeq.empty, false)))
    }

    assertEquals(
      graph.scc(2).flatten,
      Seq(0, 1, 2),
    )
    graph.updateNodeAlias(mutable.Map(1 -> 0))
    assertEquals(
      graph.scc(2).flatten,
      Seq(0, 2),
    )
  }

  test("alias removal causes negative cycle") {
    val adjacency = Map(
      0 -> Seq(),
      1 -> Seq(0),
      2 -> Seq(1),
    )

    val graph = new PrecedenceGraph(using new NS())
    for ((node, deps) <- adjacency) {
      graph.addNode(Atom(node, ArraySeq.empty, false) +: deps.map(d => Atom(d, ArraySeq.empty, true)))
    }

    assertEquals(
      graph.ullman().flatten,
      Seq(0, 1, 2),
    )
    graph.updateNodeAlias(mutable.Map(1 -> 0))

    interceptMessage[java.lang.Exception]("Negative cycle detected in input program") {
      graph.ullman()
    }
    interceptMessage[java.lang.Exception]("Negative cycle detected in input program") {
      graph.tarjan()
    }
  }

  test("consecutive aliases removal negative") {
    val negAdjacency = Map(
      0 -> Seq(),
      3 -> Seq(2),
    )
    val posAdjacency = Map(
      1 -> Seq(0),
      2 -> Seq(1),
    )

    val graph = new PrecedenceGraph(using new NS())
    for ((node, deps) <- negAdjacency) {
      graph.addNode(Atom(node, ArraySeq.empty, false) +: deps.map(d => Atom(d, ArraySeq.empty, true)))
    }
    for ((node, deps) <- posAdjacency) {
      graph.addNode(Atom(node, ArraySeq.empty, false) +: deps.map(d => Atom(d, ArraySeq.empty, false)))
    }

    assertEquals(
      graph.scc(3).flatten,
      Seq(0, 1, 2, 3),
    )
    graph.updateNodeAlias(mutable.Map(2 -> 1, 1 -> 0))
    assertEquals(
      graph.scc(3).flatten,
      Seq(0, 3),
    )
  }
}
