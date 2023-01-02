package test

import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, NaiveStagedExecutionEngine}
import datalog.storage.{CollectionsStorageManager, RelationalStorageManager}
import test.graphs.*

class NaiveStagedTransitiveClosure extends munit.FunSuite {
  List(
    Acyclic(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager()))),
    MultiIsolatedCycle(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager()))),
    SingleCycle(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager()))),
    RecursivePath(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager()))),
    TopSort(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager()))),
    MultiJoin(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager())))
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
