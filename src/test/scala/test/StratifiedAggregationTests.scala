package test

import datalog.dsl.{Constant, Program, __, groupBy, AggOp}
import datalog.execution.*
import datalog.storage.DefaultStorageManager

class StratifiedAggregationTests extends munit.FunSuite {

  test("malformed grouping atom throws") {
    val p = Program(new NaiveExecutionEngine(new DefaultStorageManager()))
    val f = p.relation[Constant]("f")
    val r = p.relation[Constant]("r")
    val w, x, y, z = p.variable()

    f(1, 1) :- ()
    f(1, 2) :- ()
    f(2, 3) :- ()
    f(2, 4) :- ()

    interceptMessage[java.lang.Exception]("The grouping predicate cannot be negated") {
      r(x, y) :- groupBy(!f(x, z), Seq(x), AggOp.SUM(z) -> y) // grouping predicate is negated.
    }

    interceptMessage[java.lang.Exception]("The grouping variables cannot be repeated") {
      r(x, y) :- groupBy(f(x, z), Seq(x, x), AggOp.SUM(z) -> y) // grouping variables are repeated.
    }

    interceptMessage[java.lang.Exception]("The aggregation variables cannot be repeated") {
      r(x, y) :- groupBy(f(x, z), Seq(x), AggOp.SUM(z) -> y, AggOp.COUNT(z) -> y) // aggregation variables are repeated.
    }

    interceptMessage[java.lang.Exception]("No aggregation variable must not occur in the grouping predicate") {
      r(x, y) :- groupBy(f(x, z), Seq(x), AggOp.SUM(z) -> z) // aggregation variable occurr in the grouping predicate.
    }

    interceptMessage[java.lang.Exception]("The aggregated variables and the grouping variables must occurr in the grouping predicate") {
      r(x, y) :- groupBy(f(x, z), Seq(y), AggOp.SUM(z) -> y) // grouping variable does not occurr in the grouping predicate.
    }

    interceptMessage[java.lang.Exception]("The aggregated variables and the grouping variables must occurr in the grouping predicate") {
      r(x, y) :- groupBy(f(x, z), Seq(x), AggOp.SUM(y) -> y) // aggregated variable does not occurr in the grouping predicate.
    }
  }

  test("non-stratifiable program throws") {
    val p = Program(new StagedExecutionEngine(new DefaultStorageManager()))
    val e = p.relation[Constant]("e")
    val x, y, z = p.variable()

    e(1, 2) :- ()
    e(x, y) :- groupBy(e(x, z), Seq(x), AggOp.SUM(z) -> y)

    interceptMessage[java.lang.Exception]("Negative or grouping cycle detected in input program") {
      p.solve(e.id)
    }
  }
}
