package test

import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, JITOptions, NaiveStagedExecutionEngine, StagedExecutionEngine, ir}
import datalog.storage.{CollectionsStorageManager, RelationalStorageManager}
import test.graphs.*

class NaiveStagedCompiledTransitiveClosure extends munit.FunSuite {
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
class NaiveStagedInterpretedTransitiveClosure extends munit.FunSuite {
  val jitOptions = JITOptions(granularity = ir.OpCode.OTHER)
  List(
    Acyclic(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    MultiIsolatedCycle(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    SingleCycle(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    RecursivePath(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    TopSort(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    MultiJoin(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager(), jitOptions)))
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

class SemiNaiveStagedCompiledTransitiveClosure extends munit.FunSuite {
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
class SemiNaiveStagedInterpretedTransitiveClosure extends munit.FunSuite {
  val jitOptions = JITOptions(granularity = ir.OpCode.OTHER)
  List(
    Acyclic(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    SingleCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    RecursivePath(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    TopSort(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    MultiJoin(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions)))
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

class SemiNaiveStagedJITSNEvalTransitiveClosure extends munit.FunSuite {
  // ahead of time, blocking
  val jitOptions = JITOptions(granularity = ir.OpCode.EVAL_SN)
  List(
    Acyclic(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    SingleCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    RecursivePath(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    TopSort(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions))),
    MultiJoin(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions)))
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
  // online, blocking
  val jitOptions2 = JITOptions(granularity = ir.OpCode.EVAL_SN, aot = false)
  List(
    Acyclic(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions2))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions2))),
    SingleCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions2))),
    RecursivePath(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions2))),
    TopSort(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions2))),
    MultiJoin(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions2)))
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
  // online, async
  val jitOptions3 = JITOptions(granularity = ir.OpCode.EVAL_SN, aot = false, block = false)
  List(
    Acyclic(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions3))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions3))),
    SingleCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions3))),
    RecursivePath(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions3))),
    TopSort(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions3))),
    MultiJoin(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions3)))
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

  // ahead of time, async
  val jitOptions4 = JITOptions(granularity = ir.OpCode.EVAL_SN, block = false)
  List(
    Acyclic(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions4))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions4))),
    SingleCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions4))),
    RecursivePath(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions4))),
    TopSort(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions4))),
    MultiJoin(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jitOptions4)))
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
