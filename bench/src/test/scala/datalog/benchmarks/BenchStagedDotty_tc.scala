package datalog.benchmarks

import datalog.dsl.{Constant, Program, Relation}
import datalog.execution.{StagedExecutionEngine, StagedSnippetExecutionEngine, ir}
import datalog.storage.CollectionsStorageManager
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import java.util.concurrent.TimeUnit
import scala.quoted.staging
import scala.util.Random

object initialize_tc {
  def pretest(program: Program): Relation[Constant] = {
    val edge = program.relation[Constant]("edge1")
    val path = program.relation[Constant]("path1")
    val x, y, z = program.variable()
    path(x, y) :- edge(x, y)
    path(x, z) :- (edge(x, y), path(y, z))
    for i <- 0 until 200 do
      edge(
        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString,
        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString
      ) :- ()
    path
  }
}
@Fork(dotty_staged_fork) // # of jvms that it will use
@Warmup(iterations = dotty_staged_warmup_iterations, time = dotty_staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@Measurement(iterations = dotty_staged_iterations, time = dotty_staged_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedDotty_tc_full_cold {
  var storage: CollectionsStorageManager = null
  var engine: StagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.ProgramOp = null
  var ctx: ir.InterpreterContext = null
  var doWhile: ir.DoWhileOp = null
  var naiveEval, snEval: ir.SequenceOp = null
  var snEvalRule: ir.UnionOp = null
  var projectJoinFilter: ir.ProjectJoinFilterOp = null
  var unionSPJ: ir.UnionSPJOp = null

  @Setup(Level.Invocation)
  def setup(): Unit = {
    storage = CollectionsStorageManager()
    engine = StagedExecutionEngine(storage)
    program = Program(engine)
    toSolve = initialize_tc.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1.asInstanceOf[ir.ProgramOp]
    ctx = x1._2
    // manually pick out subtrees to compile
    val programNode = tree
    naiveEval = programNode.getSubTree(ir.OpCode.EVAL_NAIVE).asInstanceOf[ir.SequenceOp]
    doWhile = programNode.getSubTree(ir.OpCode.DOWHILE).asInstanceOf[ir.DoWhileOp]
    snEval = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp]
    snEvalRule = programNode.getSubTree(ir.OpCode.EVAL_RULE_SN).asInstanceOf[ir.UnionOp]
    projectJoinFilter = programNode.getSubTree(ir.OpCode.SPJ).asInstanceOf[ir.ProjectJoinFilterOp]
    unionSPJ = programNode.getSubTree(ir.OpCode.EVAL_RULE_BODY).asInstanceOf[ir.UnionSPJOp]
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
      engine.compiler.getCompiledRel(snEvalRule)
    )
  }

  @Benchmark def compile_projectJoinFilter(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.compiler.getCompiledRel(projectJoinFilter)
    )
  }

  @Benchmark def compile_unionSPJ(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.compiler.getCompiledRel(unionSPJ)
    )
  }
}

@Fork(dotty_staged_fork) // # of jvms that it will use
@Warmup(iterations = dotty_staged_warmup_iterations, time = dotty_staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@Measurement(iterations = dotty_staged_iterations, time = dotty_staged_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedDotty_tc_full_warm {
  var storage: CollectionsStorageManager = null
  var engine: StagedExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.ProgramOp = null
  var ctx: ir.InterpreterContext = null
  var doWhile: ir.DoWhileOp = null
  var naiveEval, snEval: ir.SequenceOp = null
  var snEvalRule: ir.UnionOp = null
  var projectJoinFilter: ir.ProjectJoinFilterOp = null
  var unionSPJ: ir.UnionSPJOp = null

  @Setup(Level.Trial)
  def setup(): Unit = {
    storage = CollectionsStorageManager()
    engine = StagedExecutionEngine(storage)
    program = Program(engine)
    toSolve = initialize_tc.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1.asInstanceOf[ir.ProgramOp]
    ctx = x1._2
    // manually pick out subtrees to compile
    val programNode = tree
    naiveEval = programNode.getSubTree(ir.OpCode.EVAL_NAIVE).asInstanceOf[ir.SequenceOp]
    doWhile = programNode.getSubTree(ir.OpCode.DOWHILE).asInstanceOf[ir.DoWhileOp]
    snEval = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp]
    snEvalRule = programNode.getSubTree(ir.OpCode.EVAL_RULE_SN).asInstanceOf[ir.UnionOp]
    projectJoinFilter = programNode.getSubTree(ir.OpCode.SPJ).asInstanceOf[ir.ProjectJoinFilterOp]
    unionSPJ = programNode.getSubTree(ir.OpCode.EVAL_RULE_BODY).asInstanceOf[ir.UnionSPJOp]
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
      engine.compiler.getCompiledRel(snEvalRule)
    )
  }

  @Benchmark def compile_projectJoinFilter(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.compiler.getCompiledRel(projectJoinFilter)
    )
  }

  @Benchmark def compile_unionSPJ(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.compiler.getCompiledRel(unionSPJ)
    )
  }
}
@Fork(dotty_staged_fork) // # of jvms that it will use
@Warmup(iterations = dotty_staged_warmup_iterations, time = dotty_staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@Measurement(iterations = dotty_staged_iterations, time = dotty_staged_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedDotty_tc_snippet_cold {
  var storage: CollectionsStorageManager = null
  var engine: StagedSnippetExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.ProgramOp = null
  var ctx: ir.InterpreterContext = null
  var doWhile: ir.DoWhileOp = null
  var naiveEval, snEval: ir.SequenceOp = null
  var snEvalRule: ir.UnionOp = null
  var projectJoinFilter: ir.ProjectJoinFilterOp = null
  var unionSPJ: ir.UnionSPJOp = null

  @Setup(Level.Invocation)
  def setup(): Unit = {
    storage = CollectionsStorageManager()
    engine = StagedSnippetExecutionEngine(storage)
    program = Program(engine)
    toSolve = initialize_tc.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1.asInstanceOf[ir.ProgramOp]
    ctx = x1._2
    val programNode = tree
    naiveEval = programNode.getSubTree(ir.OpCode.EVAL_NAIVE).asInstanceOf[ir.SequenceOp]
    doWhile = programNode.getSubTree(ir.OpCode.DOWHILE).asInstanceOf[ir.DoWhileOp]
    snEval = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp]
    snEvalRule = programNode.getSubTree(ir.OpCode.EVAL_RULE_SN).asInstanceOf[ir.UnionOp]
    projectJoinFilter = programNode.getSubTree(ir.OpCode.SPJ).asInstanceOf[ir.ProjectJoinFilterOp]
    unionSPJ = programNode.getSubTree(ir.OpCode.EVAL_RULE_BODY).asInstanceOf[ir.UnionSPJOp]
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
      engine.snippetCompiler.getCompiledSnippetRel(snEvalRule)
    )
  }

  @Benchmark def compile_projectJoinFilter(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippetRel(projectJoinFilter)
    )
  }

  @Benchmark def compile_unionSPJ(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippetRel(unionSPJ)
    )
  }
}

@Fork(dotty_staged_fork) // # of jvms that it will use
@Warmup(iterations = dotty_staged_warmup_iterations, time = dotty_staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@Measurement(iterations = dotty_staged_iterations, time = dotty_staged_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStagedDotty_tc_snippet_warm {
  var storage: CollectionsStorageManager = null
  var engine: StagedSnippetExecutionEngine = null
  var program: Program = null
  var toSolve: Relation[Constant] = null
  var tree: ir.ProgramOp = null
  var ctx: ir.InterpreterContext = null
  var doWhile: ir.DoWhileOp = null
  var naiveEval, snEval: ir.SequenceOp = null
  var snEvalRule: ir.UnionOp = null
  var projectJoinFilter: ir.ProjectJoinFilterOp = null
  var unionSPJ: ir.UnionSPJOp = null

  @Setup(Level.Trial)
  def setup(): Unit = {
    storage = CollectionsStorageManager()
    engine = StagedSnippetExecutionEngine(storage)
    program = Program(engine)
    toSolve = initialize_tc.pretest(program)
    val x1 = engine.generateProgramTree(toSolve.id)
    tree = x1._1.asInstanceOf[ir.ProgramOp]
    ctx = x1._2
    val programNode = tree
    naiveEval = programNode.getSubTree(ir.OpCode.EVAL_NAIVE).asInstanceOf[ir.SequenceOp]
    doWhile = programNode.getSubTree(ir.OpCode.DOWHILE).asInstanceOf[ir.DoWhileOp]
    snEval = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp]
    snEvalRule = programNode.getSubTree(ir.OpCode.EVAL_RULE_SN).asInstanceOf[ir.UnionOp]
    projectJoinFilter = programNode.getSubTree(ir.OpCode.SPJ).asInstanceOf[ir.ProjectJoinFilterOp]
    unionSPJ = programNode.getSubTree(ir.OpCode.EVAL_RULE_BODY).asInstanceOf[ir.UnionSPJOp]
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
      engine.snippetCompiler.getCompiledSnippetRel(snEvalRule)
    )
  }

  @Benchmark def compile_projectJoinFilter(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippetRel(projectJoinFilter)
    )
  }

  @Benchmark def compile_unionSPJ(blackhole: Blackhole): Unit = {
    given staging.Compiler = engine.dedicatedDotty

    blackhole.consume(
      engine.snippetCompiler.getCompiledSnippetRel(unionSPJ)
    )
  }
}
