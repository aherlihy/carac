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

    engine.precedenceGraph.topSort() // test against sorted to keep groups
    assertEquals(
      engine.precedenceGraph.sorted,
      mutable.Queue[mutable.Set[Int]](mutable.Set(0), mutable.Set(1), mutable.Set(2))
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

    engine.precedenceGraph.topSort()
    assertEquals(
      engine.precedenceGraph.sorted,
      mutable.Queue(mutable.Set(3), mutable.Set(0, 1, 2))
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

    engine.precedenceGraph.topSort()
    assertEquals(
      engine.precedenceGraph.sorted,
      mutable.Queue(mutable.Set(3), mutable.Set(0, 1, 2))
    )
  }

  test("souffle top order test") {
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

    engine.precedenceGraph.topSort()
    assertEquals(
      engine.precedenceGraph.sorted,
      mutable.Queue(mutable.Set(3), mutable.Set(0, 1, 2))
    )
  }
}
