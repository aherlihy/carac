package Naive

import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, SimpleExecutionEngine}
import graphs.{Acyclic, MultiIsolatedCycle, SingleCycle}

class MultiIsoCycleTransitiveClosureNaive extends munit.FunSuite, MultiIsolatedCycle {
  given engine: ExecutionEngine = new SimpleExecutionEngine
  val program = Program()
  initGraph(program)
  queries.map((hint, query) => {
    test(query.description) {
      assertEquals(
        query.relation.solveNaive(),
        query.solution,
        hint
      )
    }
  })
}
class AcyclicTransitiveClosureNaive extends munit.FunSuite, Acyclic {
  given engine: ExecutionEngine = new SimpleExecutionEngine
  val program = Program()
  initGraph(program)
  queries.map((hint, query) => {
    test(query.description) {
      assertEquals(
        query.relation.solveNaive(),
        query.solution,
        hint
      )
    }
  })
}
class SingleCycleTransitiveClosureNaive extends munit.FunSuite, SingleCycle {
  given engine: ExecutionEngine = new SimpleExecutionEngine
  val program = Program()
  initGraph(program)
  queries.map((hint, query) => {
    test(query.description) {
      assertEquals(
        query.relation.solveNaive(),
        query.solution,
        hint
      )
    }
  })
}