package test

import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, StagedExecutionEngine}
import datalog.storage.{CollectionsStorageManager, RelationalStorageManager}
import test.graphs.*

class NaiveStagedTransitiveClosure extends munit.FunSuite {
  List(
    Acyclic(new Program(new StagedExecutionEngine(new CollectionsStorageManager()))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager()))),
    SingleCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager()))),
    RecursivePath(new Program(new StagedExecutionEngine(new CollectionsStorageManager()))),
    TopSort(new Program(new StagedExecutionEngine(new CollectionsStorageManager()))),
    MultiJoin(new Program(new StagedExecutionEngine(new CollectionsStorageManager())))
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
