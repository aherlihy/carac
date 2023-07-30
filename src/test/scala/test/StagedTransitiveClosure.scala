package test

import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, JITOptions, NaiveStagedExecutionEngine, StagedExecutionEngine, ir}
import datalog.storage.{DefaultStorageManager, VolcanoStorageManager}
import test.graphs.*

class NaiveStagedCompiledTransitiveClosure extends munit.FunSuite {
  val jo = JITOptions()
  List(
    Acyclic(new Program(new NaiveStagedExecutionEngine(new DefaultStorageManager(), jo))),
    MultiIsolatedCycle(new Program(new NaiveStagedExecutionEngine(new DefaultStorageManager(), jo))),
    SingleCycle(new Program(new NaiveStagedExecutionEngine(new DefaultStorageManager(), jo))),
    RecursivePath(new Program(new NaiveStagedExecutionEngine(new DefaultStorageManager(), jo))),
    TopSort(new Program(new NaiveStagedExecutionEngine(new DefaultStorageManager(), jo))),
    MultiJoin(new Program(new NaiveStagedExecutionEngine(new DefaultStorageManager(), jo)))
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
    Acyclic(new Program(new NaiveStagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    MultiIsolatedCycle(new Program(new NaiveStagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    SingleCycle(new Program(new NaiveStagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    RecursivePath(new Program(new NaiveStagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    TopSort(new Program(new NaiveStagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    MultiJoin(new Program(new NaiveStagedExecutionEngine(new DefaultStorageManager(), jitOptions)))
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
  val jo = JITOptions()
  List(
    Acyclic(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jo))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jo))),
    SingleCycle(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jo))),
    RecursivePath(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jo))),
    TopSort(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jo))),
    MultiJoin(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jo)))
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

class SemiNaiveBytecodeGeneratedTransitiveClosure extends munit.FunSuite {
  val jo = JITOptions(granularity = ir.OpCode.PROGRAM, aot = true, block = true,
    useBytecodeGenerator = true)
  List(
    Acyclic(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jo))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jo))),
    SingleCycle(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jo))),
    RecursivePath(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jo))),
    TopSort(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jo))),
    MultiJoin(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jo)))
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
    Acyclic(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    SingleCycle(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    RecursivePath(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    TopSort(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    MultiJoin(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions)))
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
    Acyclic(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    SingleCycle(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    RecursivePath(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    TopSort(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions))),
    MultiJoin(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions)))
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
    Acyclic(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions2))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions2))),
    SingleCycle(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions2))),
    RecursivePath(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions2))),
    TopSort(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions2))),
    MultiJoin(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions2)))
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
    Acyclic(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions3))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions3))),
    SingleCycle(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions3))),
    RecursivePath(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions3))),
    TopSort(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions3))),
    MultiJoin(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions3)))
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
    Acyclic(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions4))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions4))),
    SingleCycle(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions4))),
    RecursivePath(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions4))),
    TopSort(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions4))),
    MultiJoin(new Program(new StagedExecutionEngine(new DefaultStorageManager(), jitOptions4)))
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
