package test

import carac.dsl.{Program, Relation}
import carac.execution.ir.OpCode
import carac.execution.{Backend, CompileSync, ExecutionEngine, Granularity, JITOptions, Mode, NaiveStagedExecutionEngine, SortOrder, StagedExecutionEngine, ir}
import carac.storage.CollectionsStorageManager
import test.graphs.*

@munit.IgnoreSuite
class NaiveStagedCompiledTransitiveClosure extends munit.FunSuite {
  val jo = JITOptions(mode = Mode.Compiled)
  List(
    Acyclic(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager(), jo))),
    MultiIsolatedCycle(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager(), jo))),
    SingleCycle(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager(), jo))),
    RecursivePath(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager(), jo))),
    TopSort(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager(), jo))),
    MultiJoin(new Program(new NaiveStagedExecutionEngine(new CollectionsStorageManager(), jo)))
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
@munit.IgnoreSuite
class NaiveStagedInterpretedTransitiveClosure extends munit.FunSuite {
  val jitOptions = JITOptions(mode = Mode.Interpreted)
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

@munit.IgnoreSuite
class SemiNaiveStagedCompiledTransitiveClosure extends munit.FunSuite {
  val jo = JITOptions(mode = Mode.Compiled)
  List(
    Acyclic(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jo))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jo))),
    SingleCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jo))),
    RecursivePath(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jo))),
    TopSort(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jo))),
    MultiJoin(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jo)))
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

@munit.IgnoreSuite
class SemiNaiveBytecodeGeneratedTransitiveClosure extends munit.FunSuite {
  val jo = JITOptions(mode = Mode.JIT, granularity = Granularity.ALL, backend = Backend.Bytecode)
  List(
    Acyclic(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jo))),
    MultiIsolatedCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jo))),
    SingleCycle(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jo))),
    RecursivePath(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jo))),
    TopSort(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jo))),
    MultiJoin(new Program(new StagedExecutionEngine(new CollectionsStorageManager(), jo)))
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
@munit.IgnoreSuite
class SemiNaiveStagedInterpretedTransitiveClosure extends munit.FunSuite {
  val jitOptions = JITOptions(mode = Mode.Interpreted)
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

@munit.IgnoreSuite
class SemiNaiveStagedJIT_TransitiveClosure extends munit.FunSuite {
  // JIT async, SN
  val jitOptions = JITOptions(mode = Mode.JIT, granularity = Granularity.ALL, compileSync = CompileSync.Async)
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
  // JIT blocking, SN
  val jitOptions2 = JITOptions(mode = Mode.JIT, granularity = Granularity.ALL, compileSync = CompileSync.Blocking)
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

  // JIT async, Rule
  val jitOptions3 = JITOptions(mode = Mode.JIT, granularity = Granularity.RULE, compileSync = CompileSync.Async)
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

  // JIT blocking, Rule
  val jitOptions4 = JITOptions(mode = Mode.JIT, granularity = Granularity.RULE, compileSync = CompileSync.Blocking)
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
