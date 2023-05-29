package test

import datalog.dsl.{Constant, Program, __, not}
import datalog.execution.*
import datalog.storage.DefaultStorageManager

class StratifiedNegationTests extends munit.FunSuite {

  test("non-limited variable in rule throws java.lang.Exception") {
    val p = Program(new StagedExecutionEngine(new DefaultStorageManager()))
    val e = p.relation[Constant]("e")
    val t = p.relation[Constant]("t")
    val tc = p.relation[Constant]("tc")
    val x, y, z = p.variable()

    e(1, 2) :- ()
    e(2, 3) :- ()
    t(x, y) :- e(x, y)
    t(x, z) :- (t(x, y), e(y, z))

    intercept[java.lang.Exception] {
      tc(x, y) :- not(t(x, y)) // x and y are not limited.
    }
  }

  test("non-stratifiable program throws") {
    val p = Program(new StagedExecutionEngine(new DefaultStorageManager()))
    val e = p.relation[Constant]("e")
    val x, y = p.variable()

    e(1, 2) :- ()
    e(x, y) :- (e(x, y), not(e(y, x)))

    intercept[java.lang.Exception] {
      p.solve(e.id)
    }
  }
}
