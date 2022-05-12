package all

import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, SimpleExecutionEngine}
import graphs.{Acyclic, MultiIsolatedCycle, SingleCycle}

class NaiveTransitiveClosure extends munit.FunSuite {
  val engine: ExecutionEngine = new SimpleExecutionEngine
  List(
    Acyclic(new Program()),
    MultiIsolatedCycle(new Program()),
    SingleCycle(new Program())
  ).map(graph =>
  graph.queries.map((hint, query) => {
    test(graph.description + "." + query.description) {
      assertEquals(
        query.relation.solveNaive(),
        query.solution,
        hint
      )
    }
  }))
}