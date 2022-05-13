package all

import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, NaiveExecutionEngine}
import datalog.storage.{RelationalStorageManager, CollectionsStorageManager}
import graphs.{Acyclic, MultiIsolatedCycle, SingleCycle}

class NaiveRelationalTransitiveClosure extends munit.FunSuite {
  List(
    Acyclic(new Program(new NaiveExecutionEngine(new RelationalStorageManager()))),
    MultiIsolatedCycle(new Program(new NaiveExecutionEngine(new RelationalStorageManager()))),
    SingleCycle(new Program(new NaiveExecutionEngine(new RelationalStorageManager())))
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
class NaiveCollectionTransitiveClosure extends munit.FunSuite {
  List(
    Acyclic(new Program(new NaiveExecutionEngine(new CollectionsStorageManager()))),
    MultiIsolatedCycle(new Program(new NaiveExecutionEngine(new CollectionsStorageManager()))),
    SingleCycle(new Program(new NaiveExecutionEngine(new CollectionsStorageManager())))
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
