package SemiNaive

import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, SimpleExecutionEngine}
import graphs.{Acyclic, MultiIsolatedCycle, SingleCycle}

class MultiIsoCycleTransitiveClosureSemiNaive extends munit.FunSuite, MultiIsolatedCycle {
  given engine: ExecutionEngine = new SimpleExecutionEngine

  val program = Program()
  initGraph(program)
  queries.map((hint, query) => {
    test(query.description) {
      assertEquals(
        query.relation.solve(),
        query.solution,
        hint
      )
    }
  })
}

class AcyclicTransitiveClosureSemiNaive extends munit.FunSuite, Acyclic {
  given engine: ExecutionEngine = new SimpleExecutionEngine

  val program = Program()
  initGraph(program)
  queries.map((hint, query) => {
    test(query.description) {
      assertEquals(
        query.relation.solve(),
        query.solution,
        hint
      )
    }
  })
}

class SingleCycleTransitiveClosureSemiNaive extends munit.FunSuite, SingleCycle {
  given engine: ExecutionEngine = new SimpleExecutionEngine

  val program = Program()
  initGraph(program)
  queries.map((hint, query) => {
    test(query.description) {
      assertEquals(
        query.relation.solve(),
        query.solution,
        hint
      )
    }
  })
}