package test

import carac.dsl.{Program, Relation}
import carac.execution.{ExecutionEngine, NaiveShallowExecutionEngine, ShallowExecutionEngine}
import carac.storage.CollectionsStorageManager
import test.graphs.{Acyclic, MultiIsolatedCycle, MultiJoin, RecursivePath, SingleCycle, TopSort}

//class NaiveVolcanoTransitiveClosure extends munit.FunSuite {
//  val volcano = PullBasedSPJU()
//  List(
//    Acyclic(new Program(new NaiveShallowExecutionEngine(storageManager = new CollectionsStorageManager(), volcano))),
//    MultiIsolatedCycle(new Program(new NaiveShallowExecutionEngine(storageManager = new CollectionsStorageManager(), volcano))),
//    SingleCycle(new Program(new NaiveShallowExecutionEngine(storageManager = new CollectionsStorageManager(), volcano))),
//    RecursivePath(new Program(new NaiveShallowExecutionEngine(storageManager = new CollectionsStorageManager(), volcano))),
//    TopSort(new Program(new NaiveShallowExecutionEngine(storageManager = new CollectionsStorageManager(), volcano))),
//    MultiJoin(new Program(new NaiveShallowExecutionEngine(storageManager = new CollectionsStorageManager(), volcano)))
//  ).map(graph =>
//  graph.queries.map((hint, query) => {
//    test(graph.description + "." + query.description) {
//      assertEquals(
//        query.relation.solve(),
//        query.solution,
//        hint
//      )
//    }
//  }))
//}
class NaiveTransitiveClosureTest extends munit.FunSuite {
  List(
    Acyclic(new Program(new NaiveShallowExecutionEngine(new CollectionsStorageManager()))),
    MultiIsolatedCycle(new Program(new NaiveShallowExecutionEngine(new CollectionsStorageManager()))),
    SingleCycle(new Program(new NaiveShallowExecutionEngine(new CollectionsStorageManager()))),
    RecursivePath(new Program(new NaiveShallowExecutionEngine(new CollectionsStorageManager()))),
    TopSort(new Program(new NaiveShallowExecutionEngine(new CollectionsStorageManager()))),
    MultiJoin(new Program(new NaiveShallowExecutionEngine(new CollectionsStorageManager())))
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
