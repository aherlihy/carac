package test

import datalog.dsl.{Program, Relation}
import datalog.execution.ir.OpCode
import datalog.execution.{Backend, CompileSync, ExecutionEngine, JITOptions, NaiveStagedExecutionEngine, SortOrder, StagedExecutionEngine, ir}
import datalog.storage.{DefaultStorageManager, VolcanoStorageManager}
import test.graphs.*

class NaiveStagedCompiledTransitiveClosure extends munit.FunSuite {
  val jo = JITOptions(granularity = ir.OpCode.PROGRAM, compileSync = CompileSync.Blocking)
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
  val jo = JITOptions(granularity = OpCode.PROGRAM, compileSync = CompileSync.Blocking)
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
  val jo = JITOptions(granularity = ir.OpCode.EVAL_RULE_SN, backend = Backend.Bytecode)
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

class SemiNaiveStagedJIT_TransitiveClosure extends munit.FunSuite {
  // JIT async, SN
  val jitOptions = JITOptions(granularity = ir.OpCode.EVAL_SN, compileSync = CompileSync.Async)
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
  // JIT blocking, SN
  val jitOptions2 = JITOptions(granularity = ir.OpCode.EVAL_SN, compileSync = CompileSync.Blocking)
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

  // JIT async, Rule
  val jitOptions3 = JITOptions(granularity = ir.OpCode.EVAL_RULE_BODY, compileSync = CompileSync.Async)
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

  // JIT blocking, Rule
  val jitOptions4 = JITOptions(granularity = ir.OpCode.EVAL_RULE_BODY, compileSync = CompileSync.Blocking)
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
