package all

import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, SimpleExecutionEngine}
import graphs.{Acyclic, MultiIsolatedCycle, SingleCycle}

class SemiNaiveTransitiveClosure extends munit.FunSuite {
  List(
    Acyclic(new Program()),
    MultiIsolatedCycle(new Program()),
    SingleCycle(new Program())
  ).map(graph =>
    graph.queries.map((hint, query) => {
      test(graph.description + "." + query.description) {
        assertEquals(
          query.relation.solve(),
          query.solution,
          hint
        )
      }
    }))
}