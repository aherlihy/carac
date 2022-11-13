//package all
//
//import datalog.dsl.{Program, Relation}
//import datalog.execution.{ExecutionEngine, ManuallyInlinedUnrolledEE, ManuallyInlinedEE}
//import datalog.storage.{IndexedCollStorageManager, CollectionsStorageManager}
//import graphs.{Acyclic, MultiIsolatedCycle, SingleCycle}
//
//class ManuallyOptimizedInlineUnrollIndexedTransitiveClosure extends munit.FunSuite {
//  val engine: ExecutionEngine = new ManuallyInlinedUnrolledEE(new IndexedCollStorageManager())
//  List(
////    Because it's unrolled, can only match 1 dataset
////    Acyclic(new Program(new ManuallyInlinedUnrolledEE(new IndexedCollStorageManager()))),
////    MultiIsolatedCycle(new Program(new ManuallyInlinedUnrolledEE(new IndexedCollStorageManager()))),
//    SingleCycle(new Program(new ManuallyInlinedUnrolledEE(new IndexedCollStorageManager())))
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
//
//class ManuallyOptimizedInlineUnrollCollectionsTransitiveClosure extends munit.FunSuite {
//  val engine: ExecutionEngine = new ManuallyInlinedUnrolledEE(new CollectionsStorageManager())
//  List(
////    Acyclic(new Program(new ManuallyInlinedUnrolledEE(new CollectionsStorageManager()))),
////    MultiIsolatedCycle(new Program(new ManuallyInlinedUnrolledEE(new CollectionsStorageManager()))),
//    SingleCycle(new Program(new ManuallyInlinedUnrolledEE(new CollectionsStorageManager())))
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
//
//class ManuallyOptimizedInlineTransitiveClosure extends munit.FunSuite {
//  val engine: ExecutionEngine = new ManuallyInlinedEE(new IndexedCollStorageManager())
//  List(
//    Acyclic(new Program(new ManuallyInlinedEE(new CollectionsStorageManager()))),
//    MultiIsolatedCycle(new Program(new ManuallyInlinedEE(new CollectionsStorageManager()))),
//    SingleCycle(new Program(new ManuallyInlinedEE(new CollectionsStorageManager())))
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