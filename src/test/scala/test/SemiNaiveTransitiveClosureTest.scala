package test

import datalog.dsl.{Program, Relation}
import datalog.execution.ShallowExecutionEngine
import datalog.storage.CollectionsStorageManager
import test.graphs.{Acyclic, MultiIsolatedCycle, MultiJoin, RecursivePath, SingleCycle, TopSort}

//class SemiNaiveVolcanoTransitiveClosure extends munit.FunSuite {
//  val volcano = PullBasedSPJU()
//  List(
//    Acyclic(new Program(new ShallowExecutionEngine(new CollectionsStorageManager(), volcano))),
//    MultiIsolatedCycle(new Program(new ShallowExecutionEngine(new CollectionsStorageManager(), volcano))),
//    SingleCycle(new Program(new ShallowExecutionEngine(new CollectionsStorageManager(), volcano))),
//    RecursivePath(new Program(new ShallowExecutionEngine(new CollectionsStorageManager(), volcano))),
//    TopSort(new Program(new ShallowExecutionEngine(new CollectionsStorageManager(), volcano))),
//    MultiJoin(new Program(new ShallowExecutionEngine(new CollectionsStorageManager(), volcano)))
//  ).map(graph =>
//    graph.queries.map((hint, query) => {
//      test(graph.description + "." + query.description) {
//        assertEquals(
//          query.relation.solve(),
//          query.solution,
//          hint
//        )
//      }
//    }))
//}

class SemiNaiveTransitiveClosureTest extends munit.FunSuite {
  List(
    Acyclic(new Program(new ShallowExecutionEngine(new CollectionsStorageManager()))),
    MultiIsolatedCycle(new Program(new ShallowExecutionEngine(new CollectionsStorageManager()))),
    SingleCycle(new Program(new ShallowExecutionEngine(new CollectionsStorageManager()))),
    RecursivePath(new Program(new ShallowExecutionEngine(new CollectionsStorageManager()))),
    TopSort(new Program(new ShallowExecutionEngine(new CollectionsStorageManager()))),
    MultiJoin(new Program(new ShallowExecutionEngine(new CollectionsStorageManager())))
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
