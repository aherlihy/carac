package test

import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.storage.{DefaultStorageManager, VolcanoStorageManager}
import test.graphs.{Acyclic, MultiIsolatedCycle, MultiJoin, RecursivePath, SingleCycle, TopSort}

class NaiveVolcanoTransitiveClosure extends munit.FunSuite {
  List(
    Acyclic(new Program(new NaiveExecutionEngine(new VolcanoStorageManager()))),
    MultiIsolatedCycle(new Program(new NaiveExecutionEngine(new VolcanoStorageManager()))),
    SingleCycle(new Program(new NaiveExecutionEngine(new VolcanoStorageManager()))),
    RecursivePath(new Program(new NaiveExecutionEngine(new VolcanoStorageManager()))),
    TopSort(new Program(new NaiveExecutionEngine(new VolcanoStorageManager()))),
    MultiJoin(new Program(new NaiveExecutionEngine(new VolcanoStorageManager())))
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
    Acyclic(new Program(new NaiveExecutionEngine(new DefaultStorageManager()))),
    MultiIsolatedCycle(new Program(new NaiveExecutionEngine(new DefaultStorageManager()))),
    SingleCycle(new Program(new NaiveExecutionEngine(new DefaultStorageManager()))),
    RecursivePath(new Program(new NaiveExecutionEngine(new DefaultStorageManager()))),
    TopSort(new Program(new NaiveExecutionEngine(new DefaultStorageManager()))),
    MultiJoin(new Program(new NaiveExecutionEngine(new DefaultStorageManager())))
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
