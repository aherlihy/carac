package test

import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, NaiveCompiledStagedExecutionEngine, CompiledStagedExecutionEngine}
import datalog.storage.{CollectionsStorageManager, RelationalStorageManager}
import test.graphs.*

class NaiveStagedTransitiveClosure extends munit.FunSuite {
  List(
    Acyclic(new Program(new NaiveCompiledStagedExecutionEngine(new CollectionsStorageManager()))),
    MultiIsolatedCycle(new Program(new NaiveCompiledStagedExecutionEngine(new CollectionsStorageManager()))),
    SingleCycle(new Program(new NaiveCompiledStagedExecutionEngine(new CollectionsStorageManager()))),
    RecursivePath(new Program(new NaiveCompiledStagedExecutionEngine(new CollectionsStorageManager()))),
    TopSort(new Program(new NaiveCompiledStagedExecutionEngine(new CollectionsStorageManager()))),
    MultiJoin(new Program(new NaiveCompiledStagedExecutionEngine(new CollectionsStorageManager())))
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

class SemiNaiveStagedTransitiveClosure extends munit.FunSuite {
  List(
    Acyclic(new Program(new CompiledStagedExecutionEngine(new CollectionsStorageManager()))),
    MultiIsolatedCycle(new Program(new CompiledStagedExecutionEngine(new CollectionsStorageManager()))),
    SingleCycle(new Program(new CompiledStagedExecutionEngine(new CollectionsStorageManager()))),
    RecursivePath(new Program(new CompiledStagedExecutionEngine(new CollectionsStorageManager()))),
    TopSort(new Program(new CompiledStagedExecutionEngine(new CollectionsStorageManager()))),
    MultiJoin(new Program(new CompiledStagedExecutionEngine(new CollectionsStorageManager())))
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
