package all
import datalog.execution.{ExecutionEngine, PrecedenceGraph, SemiNaiveExecutionEngine}

import scala.collection.mutable
import datalog.dsl.Program
import datalog.storage.RelationalStorageManager

class PrecedenceGraphTest extends munit.FunSuite {
  test("transitive closure") {
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new RelationalStorageManager())
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
      engine.precedenceGraph.getTopSort,
      Seq(Seq(0), Seq(1), Seq(2))
    )
  }

  test("simple cycle") {
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new RelationalStorageManager())
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
      engine.precedenceGraph.getTopSort,
      Seq(Seq(3), Seq(0, 1, 2))
    )
  }

  test("simple cycle with inner loop") {
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new RelationalStorageManager())
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
      engine.precedenceGraph.getTopSort,
      Seq(Seq(3), Seq(0, 1, 2))
    )
  }
}
