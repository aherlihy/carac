package test

import datalog.dsl.{Constant, Program, __, not, groupBy, AggOp}
import datalog.execution.*
import datalog.storage.DefaultStorageManager

class StratifiedNegationTests extends munit.FunSuite {

  test("free variable in rule throws") {
    val p = Program(new NaiveExecutionEngine(new DefaultStorageManager()))
    val e = p.relation[Constant]("e")
    val t = p.relation[Constant]("t")
    val tc = p.relation[Constant]("tc")
    val x, y, z = p.variable()

    e(1, 2) :- ()
    e(2, 3) :- ()
    t(x, y) :- e(x, y)
    t(x, z) :- (t(x, y), e(y, z))

    interceptMessage[java.lang.Exception]("Variable with varId 0 appears only in negated atoms (and possibly in aggregated positions of grouping atoms)") {
      tc(x, y) :- not(t(x, y)) // x and y are not limited.
    }
  }

  test("non-limited variable in rule throws") {
    val p = Program(new NaiveExecutionEngine(new DefaultStorageManager()))
    val e = p.relation[Constant]("e")
    val t = p.relation[Constant]("t")
    val tc = p.relation[Constant]("tc")
    val x, y, z = p.variable()

    e(1, 2) :- ()
    e(2, 3) :- ()
    t(x, y) :- e(x, y)
    t(x, z) :- (t(x, y), e(y, z))

    interceptMessage[java.lang.Exception]("Variable with varId 2 appears only in negated atoms (and possibly in aggregated positions of grouping atoms)") {
      tc(x, y) :- (e(x, y), !e(x, z), !e(x, z))
    }
  }

  test("non-stratifiable program throws") {
    val p = Program(new StagedExecutionEngine(new DefaultStorageManager()))
    val e = p.relation[Constant]("e")
    val x, y = p.variable()

    e(1, 2) :- ()
    e(x, y) :- (e(x, y), not(e(y, x)))

    interceptMessage[java.lang.Exception]("Negative or grouping cycle detected in input program") {
      p.solve(e.id)
    }
  }

  test("stratified negation with aggregation") {
    val p = Program(new StagedExecutionEngine(new DefaultStorageManager()))
    val a = p.relation[Constant]("a")
    val b = p.relation[Constant]("b")
    val x, y = p.variable()

    a("A", 1) :- ()
    a("B", 2) :- ()

    interceptMessage[java.lang.Exception]("Variable with varId 0 appears only in negated atoms (and possibly in aggregated positions of grouping atoms)") {
      b(x) :- (!a(__, x), groupBy(a(__, y), Seq(), AggOp.SUM(y) -> x))
    }
  }
}
