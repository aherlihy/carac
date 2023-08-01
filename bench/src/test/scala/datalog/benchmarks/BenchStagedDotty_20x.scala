//package datalog.benchmarks
//
//import datalog.dsl.{Constant, Program, Relation}
//import datalog.execution.{StagedExecutionEngine, StagedSnippetExecutionEngine, ir}
//import datalog.storage.DefaultStorageManager
//import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Fork, Level, Measurement, Mode, Scope, Setup, State, TearDown, Warmup}
//import org.openjdk.jmh.infra.Blackhole
//
//import java.util.concurrent.TimeUnit
//import scala.quoted.staging
//
//object initializeAckermann {
//  def pretest(program: Program): Relation[Constant] = {
//    val succ = program.relation[Constant]("succ")
//    val greaterThanZ = program.relation[Constant]("greaterThanZ")
//    val ack = program.relation[Constant]("ack")
//    val N, M, X, Y, Ans, Ans2 = program.variable()
//
//    succ("0", "1") :- ()
//    succ("1", "2") :- ()
//    succ("2", "3") :- ()
//    succ("3", "4") :- ()
//    succ("4", "5") :- ()
//    succ("5", "6") :- ()
//    succ("6", "7") :- ()
//    succ("7", "8") :- ()
//    succ("8", "9") :- ()
//    succ("9", "10") :- ()
//    succ("10", "11") :- ()
//    succ("11", "12") :- ()
//    succ("12", "13") :- ()
//    succ("13", "14") :- ()
//    succ("14", "15") :- ()
//    succ("15", "16") :- ()
//    succ("16", "17") :- ()
//    succ("17", "18") :- ()
//    succ("18", "19") :- ()
//    succ("19", "20") :- ()
//    succ("20", "21") :- ()
//
//    greaterThanZ("1") :- ()
//    greaterThanZ("2") :- ()
//    greaterThanZ("3") :- ()
//    greaterThanZ("4") :- ()
//    greaterThanZ("5") :- ()
//    greaterThanZ("6") :- ()
//    greaterThanZ("7") :- ()
//    greaterThanZ("8") :- ()
//    greaterThanZ("9") :- ()
//    greaterThanZ("10") :- ()
//    greaterThanZ("11") :- ()
//    greaterThanZ("12") :- ()
//    greaterThanZ("13") :- ()
//    greaterThanZ("14") :- ()
//    greaterThanZ("15") :- ()
//    greaterThanZ("16") :- ()
//    greaterThanZ("17") :- ()
//    greaterThanZ("18") :- ()
//    greaterThanZ("19") :- ()
//    greaterThanZ("20") :- ()
//
//    ack("0", N, Ans) :- succ(N, Ans)
//
//    ack(M, "0", Ans) :- (greaterThanZ(M), succ(X, M), ack(X, "1", Ans))
//
//    ack(M, N, Ans) :- (
//      greaterThanZ(M),
//      greaterThanZ(N),
//      succ(X, M),
//      succ(Y, N),
//      ack(M, Y, Ans2),
//      ack(X, Ans2, Ans))
//
//    ack
//  }
//}
//
//inline val dotty_staged_warmup_iterations = 5
//inline val dotty_staged_iterations = 5
//inline val dotty_staged_warmup_time = 10
//inline val dotty_staged_time = 10
//inline val dotty_staged_batchSize = 50 // setup called even within batch
//inline val dotty_staged_fork = 1
//@Fork(dotty_staged_fork) // # of jvms that it will use
//@Warmup(iterations = dotty_staged_warmup_iterations, time = dotty_staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
//@Measurement(iterations = dotty_staged_iterations, time = dotty_staged_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
//@State(Scope.Thread)
//@BenchmarkMode(Array(Mode.AverageTime))
//class BenchStagedDotty_20x_full_cold {
//  var storage: DefaultStorageManager = null
//  var engine: StagedExecutionEngine = null
//  var program: Program = null
//  var toSolve: Relation[Constant] = null
//  var tree: ir.ProgramOp = null
//  var ctx: ir.InterpreterContext = null
//  /*var doWhile: ir.DoWhileOp = null
//  var naiveEval, snEval: ir.SequenceOp = null*/
//  var snEvalRule: ir.UnionOp = null
//  var loopBody: ir.SequenceOp = null
//  var projectJoinFilter: ir.ProjectJoinFilterOp = null
//  var unionSPJ: ir.UnionSPJOp = null
//
//  @Setup(Level.Invocation)
//  def setup(): Unit = {
//    storage = DefaultStorageManager()
//    engine = StagedExecutionEngine(storage)
//    program = Program(engine)
//    toSolve = initialize20x.pretest(program)
//    val x1 = engine.generateProgramTree(toSolve.id)
//    tree = x1._1.asInstanceOf[ir.ProgramOp]
//    ctx = x1._2
//    // manually pick out subtrees to compile
//    val programNode = tree
//    /*naiveEval = programNode.getSubTree(ir.OpCode.EVAL_NAIVE).asInstanceOf[ir.SequenceOp]
//    doWhile = programNode.getSubTree(ir.OpCode.DOWHILE).asInstanceOf[ir.DoWhileOp]
//    snEval = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp]  */
//    snEvalRule = programNode.getSubTree(ir.OpCode.EVAL_RULE_SN).asInstanceOf[ir.UnionOp]
//    loopBody = programNode.getSubTree(ir.OpCode.LOOP_BODY).asInstanceOf[ir.SequenceOp]
//    projectJoinFilter = programNode.getSubTree(ir.OpCode.SPJ).asInstanceOf[ir.ProjectJoinFilterOp]
//    unionSPJ = programNode.getSubTree(ir.OpCode.EVAL_RULE_BODY).asInstanceOf[ir.UnionSPJOp]
//  }
//  @TearDown(Level.Invocation)
//  def cleanup(): Unit = storage.initEvaluation()
//
////  @Benchmark def compile_tree(blackhole: Blackhole): Unit = {
////    given staging.Compiler = engine.defaultJITOptions.dotty
////    blackhole.consume(
////      engine.compiler.getCompiled(tree)
////    )
////  }
////  @Benchmark def compile_naiveEval(blackhole: Blackhole): Unit = {
////    given staging.Compiler = engine.defaultJITOptions.dotty
////    blackhole.consume(
////      engine.compiler.getCompiled(naiveEval)
////    )
////  }
////
////  @Benchmark def compile_doWhile(blackhole: Blackhole): Unit = {
////    given staging.Compiler = engine.defaultJITOptions.dotty
////
////    blackhole.consume(
////      engine.compiler.getCompiled(doWhile)
////    )
////  }
////
////  @Benchmark def compile_snEval(blackhole: Blackhole): Unit = {
////    given staging.Compiler = engine.defaultJITOptions.dotty
////
////    blackhole.consume(
////      engine.compiler.getCompiled(snEval)
////    )
////  }
//
//  @Benchmark def compile_snEvalRule(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(snEvalRule)
//    )
//  }
//
//  @Benchmark def compile_snEvalRuleArray(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.compiler.getCompiledIndexed(snEvalRule)
//    )
//  }
//
//  //  @Benchmark def compile_loopBody(blackhole: Blackhole): Unit = {
////    given staging.Compiler = engine.defaultJITOptions.dotty
////
////    blackhole.consume(
////      engine.compiler.getCompiled(loopBody)
////    )
////  }
//
//  @Benchmark def compile_projectJoinFilter(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(projectJoinFilter)
//    )
//  }
//
//  @Benchmark def compile_unionSPJ(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(unionSPJ)
//    )
//  }
//
//  @Benchmark def compile_unionSPJArray(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.compiler.getCompiledIndexed(unionSPJ)
//    )
//  }
//}
//
//@Fork(dotty_staged_fork) // # of jvms that it will use
//@Warmup(iterations = dotty_staged_warmup_iterations, time = dotty_staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
//@Measurement(iterations = dotty_staged_iterations, time = dotty_staged_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
//@State(Scope.Thread)
//@BenchmarkMode(Array(Mode.AverageTime))
//class BenchStagedDotty_20x_full_warm {
//  var storage: DefaultStorageManager = null
//  var engine: StagedExecutionEngine = null
//  var program: Program = null
//  var toSolve: Relation[Constant] = null
//  var tree: ir.ProgramOp = null
//  var ctx: ir.InterpreterContext = null
//  /*var doWhile: ir.DoWhileOp = null
//  var naiveEval, snEval: ir.SequenceOp = null*/
//  var snEvalRule: ir.UnionOp = null
//  var loopBody: ir.SequenceOp = null
//  var projectJoinFilter: ir.ProjectJoinFilterOp = null
//  var unionSPJ: ir.UnionSPJOp = null
//
//  @Setup(Level.Trial)
//  def setup(): Unit = {
//    storage = DefaultStorageManager()
//    engine = StagedExecutionEngine(storage)
//    program = Program(engine)
//    toSolve = initialize20x.pretest(program)
//    val x1 = engine.generateProgramTree(toSolve.id)
//    tree = x1._1.asInstanceOf[ir.ProgramOp]
//    ctx = x1._2
//    // manually pick out subtrees to compile
//    val programNode = tree
//    /*naiveEval = programNode.getSubTree(ir.OpCode.EVAL_NAIVE).asInstanceOf[ir.SequenceOp]
//    doWhile = programNode.getSubTree(ir.OpCode.DOWHILE).asInstanceOf[ir.DoWhileOp]
//    snEval = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp]  */
//    snEvalRule = programNode.getSubTree(ir.OpCode.EVAL_RULE_SN).asInstanceOf[ir.UnionOp]
//    loopBody = programNode.getSubTree(ir.OpCode.LOOP_BODY).asInstanceOf[ir.SequenceOp]
//    projectJoinFilter = programNode.getSubTree(ir.OpCode.SPJ).asInstanceOf[ir.ProjectJoinFilterOp]
//    unionSPJ = programNode.getSubTree(ir.OpCode.EVAL_RULE_BODY).asInstanceOf[ir.UnionSPJOp]
//  }
//
//  @TearDown(Level.Invocation)
//  def cleanup(): Unit = storage.initEvaluation()
//
//  @Benchmark def compile_unionSPJArray(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.compiler.getCompiledIndexed(unionSPJ)
//    )
//  }
//
//  @Benchmark def compile_snEvalRuleArray(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.compiler.getCompiledIndexed(snEvalRule)
//    )
//  }
//
//  //  @Benchmark def compile_tree(blackhole: Blackhole): Unit = {
////    given staging.Compiler = engine.defaultJITOptions.dotty
////    blackhole.consume(
////      engine.compiler.getCompiled(tree)
////    )
////  }
////  @Benchmark def compile_naiveEval(blackhole: Blackhole): Unit = {
////    given staging.Compiler = engine.defaultJITOptions.dotty
////    blackhole.consume(
////      engine.compiler.getCompiled(naiveEval)
////    )
////  }
////
////  @Benchmark def compile_doWhile(blackhole: Blackhole): Unit = {
////    given staging.Compiler = engine.defaultJITOptions.dotty
////
////    blackhole.consume(
////      engine.compiler.getCompiled(doWhile)
////    )
////  }
////
////  @Benchmark def compile_snEval(blackhole: Blackhole): Unit = {
////    given staging.Compiler = engine.defaultJITOptions.dotty
////
////    blackhole.consume(
////      engine.compiler.getCompiled(snEval)
////    )
////  }
////  @Benchmark def compile_loopBody(blackhole: Blackhole): Unit = {
////    given staging.Compiler = engine.defaultJITOptions.dotty
////
////    blackhole.consume(
////      engine.compiler.getCompiled(loopBody)
////    )
////  }
//
//  @Benchmark def compile_snEvalRule(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(snEvalRule)
//    )
//  }
//
//  @Benchmark def compile_projectJoinFilter(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(projectJoinFilter)
//    )
//  }
//
//  @Benchmark def compile_unionSPJ(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(unionSPJ)
//    )
//  }
//}
//@Fork(dotty_staged_fork) // # of jvms that it will use
//@Warmup(iterations = dotty_staged_warmup_iterations, time = dotty_staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
//@Measurement(iterations = dotty_staged_iterations, time = dotty_staged_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
//@State(Scope.Thread)
//@BenchmarkMode(Array(Mode.AverageTime))
//class BenchStagedDotty_20x_snippet_cold {
//  var storage: DefaultStorageManager = null
//  var engine: StagedSnippetExecutionEngine = null
//  var program: Program = null
//  var toSolve: Relation[Constant] = null
//  var tree: ir.ProgramOp = null
//  var ctx: ir.InterpreterContext = null
//  var doWhile: ir.DoWhileOp = null
//  var naiveEval, snEval: ir.SequenceOp = null
//  var snEvalRule: ir.UnionOp = null
//  var loopBody: ir.SequenceOp = null
//  var projectJoinFilter: ir.ProjectJoinFilterOp = null
//  var unionSPJ: ir.UnionSPJOp = null
//
//  @Setup(Level.Invocation)
//  def setup(): Unit = {
//    storage = DefaultStorageManager()
//    engine = StagedSnippetExecutionEngine(storage)
//    program = Program(engine)
//    toSolve = initialize20x.pretest(program)
//    val x1 = engine.generateProgramTree(toSolve.id)
//    tree = x1._1.asInstanceOf[ir.ProgramOp]
//    ctx = x1._2
//    val programNode = tree
//    naiveEval = programNode.getSubTree(ir.OpCode.EVAL_NAIVE).asInstanceOf[ir.SequenceOp]
//    doWhile = programNode.getSubTree(ir.OpCode.DOWHILE).asInstanceOf[ir.DoWhileOp]
//    snEval = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp]
//    snEvalRule = programNode.getSubTree(ir.OpCode.EVAL_RULE_SN).asInstanceOf[ir.UnionOp]
//    loopBody = programNode.getSubTree(ir.OpCode.LOOP_BODY).asInstanceOf[ir.SequenceOp]
//    projectJoinFilter = programNode.getSubTree(ir.OpCode.SPJ).asInstanceOf[ir.ProjectJoinFilterOp]
//    unionSPJ = programNode.getSubTree(ir.OpCode.EVAL_RULE_BODY).asInstanceOf[ir.UnionSPJOp]
//  }
//  @TearDown(Level.Invocation)
//  def cleanup(): Unit = storage.initEvaluation()
//  @Benchmark def compile_tree(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//    blackhole.consume(
//      engine.snippetCompiler.getCompiledSnippet(tree)
//    )
//  }
////  @Benchmark def compile_naiveEval(blackhole: Blackhole): Unit = {
////    given staging.Compiler = engine.defaultJITOptions.dotty
////    blackhole.consume(
////      engine.snippetCompiler.getCompiledSnippet(naiveEval)
////    )
////  }
////
////  @Benchmark def compile_doWhile(blackhole: Blackhole): Unit = {
////    given staging.Compiler = engine.defaultJITOptions.dotty
////
////    blackhole.consume(
////      engine.snippetCompiler.getCompiledSnippet(doWhile)
////    )
////  }
////
////  @Benchmark def compile_snEval(blackhole: Blackhole): Unit = {
////    given staging.Compiler = engine.defaultJITOptions.dotty
////
////    blackhole.consume(
////      engine.snippetCompiler.getCompiledSnippet(snEval)
////    )
////  }
//  @Benchmark def compile_loopBody(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(loopBody)
//    )
//  }
//
//  @Benchmark def compile_snEvalRule(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.snippetCompiler.getCompiledSnippet(snEvalRule)
//    )
//  }
//
//  @Benchmark def compile_projectJoinFilter(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.snippetCompiler.getCompiledSnippet(projectJoinFilter)
//    )
//  }
//
//  @Benchmark def compile_unionSPJ(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.snippetCompiler.getCompiledSnippet(unionSPJ)
//    )
//  }
//}
//
//@Fork(dotty_staged_fork) // # of jvms that it will use
//@Warmup(iterations = dotty_staged_warmup_iterations, time = dotty_staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
//@Measurement(iterations = dotty_staged_iterations, time = dotty_staged_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
//@State(Scope.Thread)
//@BenchmarkMode(Array(Mode.AverageTime))
//class BenchStagedDotty_20x_snippet_warm {
//  var storage: DefaultStorageManager = null
//  var engine: StagedSnippetExecutionEngine = null
//  var program: Program = null
//  var toSolve: Relation[Constant] = null
//  var tree: ir.ProgramOp = null
//  var ctx: ir.InterpreterContext = null
//  var doWhile: ir.DoWhileOp = null
//  var naiveEval, snEval: ir.SequenceOp = null
//  var snEvalRule: ir.UnionOp = null
//  var loopBody: ir.SequenceOp = null
//  var projectJoinFilter: ir.ProjectJoinFilterOp = null
//  var unionSPJ: ir.UnionSPJOp = null
//
//  @Setup(Level.Trial)
//  def setup(): Unit = {
//    storage = DefaultStorageManager()
//    engine = StagedSnippetExecutionEngine(storage)
//    program = Program(engine)
//    toSolve = initialize20x.pretest(program)
//    val x1 = engine.generateProgramTree(toSolve.id)
//    tree = x1._1.asInstanceOf[ir.ProgramOp]
//    ctx = x1._2
//    val programNode = tree
//    naiveEval = programNode.getSubTree(ir.OpCode.EVAL_NAIVE).asInstanceOf[ir.SequenceOp]
//    doWhile = programNode.getSubTree(ir.OpCode.DOWHILE).asInstanceOf[ir.DoWhileOp]
//    snEval = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp]
//    snEvalRule = programNode.getSubTree(ir.OpCode.EVAL_RULE_SN).asInstanceOf[ir.UnionOp]
//    loopBody = programNode.getSubTree(ir.OpCode.LOOP_BODY).asInstanceOf[ir.SequenceOp]
//    projectJoinFilter = programNode.getSubTree(ir.OpCode.SPJ).asInstanceOf[ir.ProjectJoinFilterOp]
//    unionSPJ = programNode.getSubTree(ir.OpCode.EVAL_RULE_BODY).asInstanceOf[ir.UnionSPJOp]
//  }
//  @TearDown(Level.Invocation)
//  def cleanup(): Unit = storage.initEvaluation()
//  @Benchmark def compile_tree(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//    blackhole.consume(
//      engine.snippetCompiler.getCompiledSnippet(tree)
//    )
//  }
//  @Benchmark def compile_naiveEval(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//    blackhole.consume(
//      engine.snippetCompiler.getCompiledSnippet(naiveEval)
//    )
//  }
//
//  @Benchmark def compile_doWhile(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.snippetCompiler.getCompiledSnippet(doWhile)
//    )
//  }
//
//  @Benchmark def compile_snEval(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.snippetCompiler.getCompiledSnippet(snEval)
//    )
//  }
//
//  @Benchmark def compile_snEvalRule(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.snippetCompiler.getCompiledSnippet(snEvalRule)
//    )
//  }
//
//  @Benchmark def compile_projectJoinFilter(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.snippetCompiler.getCompiledSnippet(projectJoinFilter)
//    )
//  }
//
//  @Benchmark def compile_unionSPJ(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.defaultJITOptions.dotty
//
//    blackhole.consume(
//      engine.snippetCompiler.getCompiledSnippet(unionSPJ)
//    )
//  }
//}
