package all

import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.storage.{CollectionsStorageManager, IndexedCollStorageManager, RelationalStorageManager}
import graphs.{Acyclic, MultiIsolatedCycle, RecursivePath, SingleCycle, TopSort}

class NaiveRelationalTransitiveClosure extends munit.FunSuite {
  List(
    Acyclic(new Program(new NaiveExecutionEngine(new RelationalStorageManager()))),
    MultiIsolatedCycle(new Program(new NaiveExecutionEngine(new RelationalStorageManager()))),
    SingleCycle(new Program(new NaiveExecutionEngine(new RelationalStorageManager()))),
    RecursivePath(new Program(new NaiveExecutionEngine(new RelationalStorageManager()))),
    TopSort(new Program(new NaiveExecutionEngine(new RelationalStorageManager())))
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
    SingleCycle(new Program(new NaiveExecutionEngine(new CollectionsStorageManager()))),
    RecursivePath(new Program(new NaiveExecutionEngine(new CollectionsStorageManager()))),
    TopSort(new Program(new NaiveExecutionEngine(new CollectionsStorageManager())))
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
