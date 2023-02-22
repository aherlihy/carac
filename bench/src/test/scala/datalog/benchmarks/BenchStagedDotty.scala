package datalog.benchmarks

import datalog.dsl.{Constant, Program, Relation}
import datalog.execution.{StagedExecutionEngine, StagedSnippetExecutionEngine, ir}
import datalog.storage.CollectionsStorageManager
import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Fork, Level, Measurement, Mode, Scope, Setup, State, TearDown, Warmup}
import org.openjdk.jmh.infra.Blackhole

import java.util.concurrent.TimeUnit
import scala.quoted.staging

inline val dotty_staged_warmup_iterations = 0
inline val dotty_staged_iterations = 5
inline val dotty_staged_warmup_time = 10
inline val dotty_staged_time = 10
inline val dotty_staged_batchSize = 50 // setup called even within batch
inline val dotty_staged_fork = 1
@Fork(dotty_staged_fork) // # of jvms that it will use
@Warmup(iterations = dotty_staged_warmup_iterations, time = dotty_staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@Measurement(iterations = dotty_staged_iterations, time = dotty_staged_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedDotty_full_cold {
  var storage: CollectionsStorageManager = null
  var engine: StagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.ProgramOp = null
  var ctx: ir.InterpreterContext = null
  var doWhile: ir.DoWhileOp = null
  var naiveEval, snEval, snEvalRule: ir.SequenceOp = null
  var join: ir.ProjectJoinFilterOp = null
  var scan: ir.ScanOp = null

  @Setup(Level.Invocation)
  def setup(): Unit = {
    storage = CollectionsStorageManager()
    engine = StagedExecutionEngine(storage)
    program = Program(engine)
    toSolve = initialize20x.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1.asInstanceOf[ir.ProgramOp]
    ctx = x1._2
    // manually pick out subtrees to compile
    val programNode = tree
    naiveEval = programNode.getSubTree(ir.OpCode.EVAL_NAIVE).asInstanceOf[ir.SequenceOp]
    doWhile = programNode.getSubTree(ir.OpCode.DOWHILE).asInstanceOf[ir.DoWhileOp]
    snEval = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp]
    snEvalRule = programNode.getSubTree(ir.OpCode.EVAL_RULE_SN).asInstanceOf[ir.SequenceOp]
    join = programNode.getSubTree(ir.OpCode.SPJ).asInstanceOf[ir.ProjectJoinFilterOp]
    scan = programNode.getSubTree(ir.OpCode.SCAN).asInstanceOf[ir.ScanOp]
  }
  @TearDown(Level.Invocation)
  def cleanup(): Unit = storage.initEvaluation()

  @Benchmark def compile_tree(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty
    blackhole.consume(
      engine.compiler.getCompiled(tree)
    )
  }
  @Benchmark def compile_naiveEval(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty
    blackhole.consume(
      engine.compiler.getCompiled(naiveEval)
    )
  }

  @Benchmark def compile_doWhile(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.compiler.getCompiled(doWhile)
    )
  }

  @Benchmark def compile_snEval(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.compiler.getCompiled(snEval)
    )
  }

  @Benchmark def compile_snEvalRule(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.compiler.getCompiled(snEvalRule)
    )
  }

  @Benchmark def compile_join(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.compiler.getCompiledRel(join)
    )
  }

  @Benchmark def compile_scan(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.compiler.getCompiledRel(scan)
    )
  }
}

@Fork(dotty_staged_fork) // # of jvms that it will use
@Warmup(iterations = dotty_staged_warmup_iterations, time = dotty_staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@Measurement(iterations = dotty_staged_iterations, time = dotty_staged_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedDotty_full_warm {
  var storage: CollectionsStorageManager = null
  var engine: StagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.ProgramOp = null
  var ctx: ir.InterpreterContext = null
  var doWhile: ir.DoWhileOp = null
  var naiveEval, snEval, snEvalRule: ir.SequenceOp = null
  var join: ir.ProjectJoinFilterOp = null
  var scan: ir.ScanOp = null

  @Setup(Level.Trial)
  def setup(): Unit = {
    storage = CollectionsStorageManager()
    engine = StagedExecutionEngine(storage)
    program = Program(engine)
    toSolve = initialize20x.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1.asInstanceOf[ir.ProgramOp]
    ctx = x1._2
    // manually pick out subtrees to compile
    val programNode = tree
    naiveEval = programNode.getSubTree(ir.OpCode.EVAL_NAIVE).asInstanceOf[ir.SequenceOp]
    doWhile = programNode.getSubTree(ir.OpCode.DOWHILE).asInstanceOf[ir.DoWhileOp]
    snEval = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp]
    snEvalRule = programNode.getSubTree(ir.OpCode.EVAL_RULE_SN).asInstanceOf[ir.SequenceOp]
    join = programNode.getSubTree(ir.OpCode.SPJ).asInstanceOf[ir.ProjectJoinFilterOp]
    scan = programNode.getSubTree(ir.OpCode.SCAN).asInstanceOf[ir.ScanOp]
  }

  @TearDown(Level.Invocation)
  def cleanup(): Unit = storage.initEvaluation()

  @Benchmark def compile_tree(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty
    blackhole.consume(
      engine.compiler.getCompiled(tree)
    )
  }
  @Benchmark def compile_naiveEval(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty
    blackhole.consume(
      engine.compiler.getCompiled(naiveEval)
    )
  }

  @Benchmark def compile_doWhile(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.compiler.getCompiled(doWhile)
    )
  }

  @Benchmark def compile_snEval(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.compiler.getCompiled(snEval)
    )
  }

  @Benchmark def compile_snEvalRule(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.compiler.getCompiled(snEvalRule)
    )
  }

  @Benchmark def compile_join(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.compiler.getCompiledRel(join)
    )
  }

  @Benchmark def compile_scan(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.compiler.getCompiledRel(scan)
    )
  }
}
@Fork(dotty_staged_fork) // # of jvms that it will use
@Warmup(iterations = dotty_staged_warmup_iterations, time = dotty_staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@Measurement(iterations = dotty_staged_iterations, time = dotty_staged_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedDotty_snippet_cold {
  var storage: CollectionsStorageManager = null
  var engine: StagedSnippetExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.ProgramOp = null
  var ctx: ir.InterpreterContext = null
  var doWhile: ir.DoWhileOp = null
  var naiveEval, snEval, snEvalRule: ir.SequenceOp = null
  var join: ir.ProjectJoinFilterOp = null
  var scan: ir.ScanOp = null

  @Setup(Level.Invocation)
  def setup(): Unit = {
    storage = CollectionsStorageManager()
    engine = StagedSnippetExecutionEngine(storage)
    program = Program(engine)
    toSolve = initialize20x.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1.asInstanceOf[ir.ProgramOp]
    ctx = x1._2
    val programNode = tree
    naiveEval = programNode.getSubTree(ir.OpCode.EVAL_NAIVE).asInstanceOf[ir.SequenceOp]
    doWhile = programNode.getSubTree(ir.OpCode.DOWHILE).asInstanceOf[ir.DoWhileOp]
    snEval = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp]
    snEvalRule = programNode.getSubTree(ir.OpCode.EVAL_RULE_SN).asInstanceOf[ir.SequenceOp]
    join = programNode.getSubTree(ir.OpCode.SPJ).asInstanceOf[ir.ProjectJoinFilterOp]
    scan = programNode.getSubTree(ir.OpCode.SCAN).asInstanceOf[ir.ScanOp]
  }
  @TearDown(Level.Invocation)
  def cleanup(): Unit = storage.initEvaluation()
  @Benchmark def compile_tree(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty
    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippet(tree)
    )
  }
  @Benchmark def compile_naiveEval(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty
    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippet(naiveEval)
    )
  }

  @Benchmark def compile_doWhile(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippet(doWhile)
    )
  }

  @Benchmark def compile_snEval(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippet(snEval)
    )
  }

  @Benchmark def compile_snEvalRule(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippet(snEvalRule)
    )
  }

  @Benchmark def compile_join(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippetRel(join)
    )
  }

  @Benchmark def compile_scan(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippetRel(scan)
    )
  }
}

@Fork(dotty_staged_fork) // # of jvms that it will use
@Warmup(iterations = dotty_staged_warmup_iterations, time = dotty_staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@Measurement(iterations = dotty_staged_iterations, time = dotty_staged_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedDotty_snippet_warm {
  var storage: CollectionsStorageManager = null
  var engine: StagedSnippetExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.ProgramOp = null
  var ctx: ir.InterpreterContext = null
  var doWhile: ir.DoWhileOp = null
  var naiveEval, snEval, snEvalRule: ir.SequenceOp = null
  var join: ir.ProjectJoinFilterOp = null
  var scan: ir.ScanOp = null

  @Setup(Level.Trial)
  def setup(): Unit = {
    storage = CollectionsStorageManager()
    engine = StagedSnippetExecutionEngine(storage)
    program = Program(engine)
    toSolve = initialize20x.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1.asInstanceOf[ir.ProgramOp]
    ctx = x1._2
    val programNode = tree
    naiveEval = programNode.getSubTree(ir.OpCode.EVAL_NAIVE).asInstanceOf[ir.SequenceOp]
    doWhile = programNode.getSubTree(ir.OpCode.DOWHILE).asInstanceOf[ir.DoWhileOp]
    snEval = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp]
    snEvalRule = programNode.getSubTree(ir.OpCode.EVAL_RULE_SN).asInstanceOf[ir.SequenceOp]
    join = programNode.getSubTree(ir.OpCode.SPJ).asInstanceOf[ir.ProjectJoinFilterOp]
    scan = programNode.getSubTree(ir.OpCode.SCAN).asInstanceOf[ir.ScanOp]
  }
  @TearDown(Level.Invocation)
  def cleanup(): Unit = storage.initEvaluation()
  @Benchmark def compile_tree(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty
    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippet(tree)
    )
  }
  @Benchmark def compile_naiveEval(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty
    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippet(naiveEval)
    )
  }

  @Benchmark def compile_doWhile(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippet(doWhile)
    )
  }

  @Benchmark def compile_snEval(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippet(snEval)
    )
  }

  @Benchmark def compile_snEvalRule(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippet(snEvalRule)
    )
  }

  @Benchmark def compile_join(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippetRel(join)
    )
  }

  @Benchmark def compile_scan(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippetRel(scan)
    )
  }
}
