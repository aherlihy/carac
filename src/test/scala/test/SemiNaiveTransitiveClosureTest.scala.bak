package test

import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.storage.{DefaultStorageManager, VolcanoStorageManager}
import test.graphs.{Acyclic, MultiIsolatedCycle, MultiJoin, RecursivePath, SingleCycle, TopSort}

class SemiNaiveVolcanoTransitiveClosure extends munit.FunSuite {
  List(
    Acyclic(new Program(new SemiNaiveExecutionEngine(new VolcanoStorageManager()))),
    MultiIsolatedCycle(new Program(new SemiNaiveExecutionEngine(new VolcanoStorageManager()))),
    SingleCycle(new Program(new SemiNaiveExecutionEngine(new VolcanoStorageManager()))),
    RecursivePath(new Program(new SemiNaiveExecutionEngine(new VolcanoStorageManager()))),
    TopSort(new Program(new SemiNaiveExecutionEngine(new VolcanoStorageManager()))),
    MultiJoin(new Program(new SemiNaiveExecutionEngine(new VolcanoStorageManager())))
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

class SemiNaiveDefaultTransitiveClosure extends munit.FunSuite {
  List(
    Acyclic(new Program(new SemiNaiveExecutionEngine(new DefaultStorageManager()))),
    MultiIsolatedCycle(new Program(new SemiNaiveExecutionEngine(new DefaultStorageManager()))),
    SingleCycle(new Program(new SemiNaiveExecutionEngine(new DefaultStorageManager()))),
    RecursivePath(new Program(new SemiNaiveExecutionEngine(new DefaultStorageManager()))),
    TopSort(new Program(new SemiNaiveExecutionEngine(new DefaultStorageManager()))),
    MultiJoin(new Program(new SemiNaiveExecutionEngine(new DefaultStorageManager())))
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
