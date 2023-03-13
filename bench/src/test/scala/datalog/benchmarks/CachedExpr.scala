// Commented out until caching allowed by Dotty
//package datalog.benchmarks
//
//import datalog.dsl.{Constant, Program, Relation}
//import datalog.execution.ir.OpCode
//import datalog.execution.{CompiledStagedExecutionEngine, ir}
//import datalog.storage.CollectionsStorageManager
//import org.openjdk.jmh.annotations.{Benchmark, BenchmarkMode, Fork, Level, Measurement, Mode, Scope, Setup, State, TearDown, Warmup}
//import org.openjdk.jmh.infra.Blackhole
//
//import java.util.concurrent.TimeUnit
//import scala.quoted.staging
//
//@Fork(dotty_staged_fork) // # of jvms that it will use
//@Warmup(iterations = dotty_staged_warmup_iterations, time = dotty_staged_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
//@Measurement(iterations = dotty_staged_iterations, time = dotty_staged_time, timeUnit = TimeUnit.SECONDS, batchSize = dotty_staged_batchSize)
//@State(Scope.Thread)
//@BenchmarkMode(Array(Mode.AverageTime))
//class BenchStagedCachedExpr {
//  var storage: CollectionsStorageManager = null
//  var engine: CompiledStagedExecutionEngine = null
//  var program: Program = null
//  var toSolve: Relation[Constant] = null
//  var tree: ir.IROp = null
//  var ctx: ir.InterpreterContext = null
//  var programN, doWhile, naiveEval, snEval, snEvalRule, join, scan: ir.IROp = null
//
//  def generateCachedTree(engine: CompiledStagedExecutionEngine, subTree: ir.OpCode): ir.IROp =
//    val x1 = engine.generateProgramTree(toSolve.id)
//    tree = x1._1
//    given staging.Compiler = engine.dedicatedDotty
//    val programNode = tree.asInstanceOf[ir.ProgramOp]
//    val sub = subTree match
//      case OpCode.PROGRAM => tree
//      case OpCode.SCAN =>
//        val scan = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp].ops.head.asInstanceOf[ir.SequenceOp].ops.head.asInstanceOf[ir.InsertOp].subOp.asInstanceOf[ir.DiffOp].lhs.asInstanceOf[ir.UnionOp].ops.head.asInstanceOf[ir.ProjectOp].subOp.asInstanceOf[ir.JoinOp].ops.head
//        assert(scan.isInstanceOf[ir.ScanOp])
//        scan
//      case OpCode.JOIN =>
//        val join = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp].ops.head.asInstanceOf[ir.SequenceOp].ops.head.asInstanceOf[ir.InsertOp].subOp.asInstanceOf[ir.DiffOp].lhs.asInstanceOf[ir.UnionOp].ops.head
//        assert(join.isInstanceOf[ir.ProjectOp])
//        join
//      case OpCode.EVAL_RULE_SN =>
//        val snEvalRule = programNode.getSubTree(ir.OpCode.EVAL_SN).asInstanceOf[ir.SequenceOp].ops.head
//        assert(snEvalRule.isInstanceOf[ir.SequenceOp])
//        snEvalRule
//      case _ =>
//        programNode.getSubTree(subTree)
//
//    engine.compiler.getCompiled(sub)
//    tree
//
//
//  @Setup(Level.Trial)
//  def setup(): Unit = {
//    engine = CompiledStagedExecutionEngine(CollectionsStorageManager())
//    program = Program(engine)
//    toSolve = initialize20x.pretest(program)
//
//    tree = engine.generateProgramTree(toSolve.id)._1
//    programN = generateCachedTree(engine, ir.OpCode.PROGRAM)
//    naiveEval = generateCachedTree(engine, ir.OpCode.EVAL_NAIVE)
//    doWhile = generateCachedTree(engine, ir.OpCode.LOOP)
//    snEval = generateCachedTree(engine, ir.OpCode.EVAL_SN)
//    snEvalRule = generateCachedTree(engine. ir.OpCode.EVAL_RULE_SN)
//    join = generateCachedTree(engine, ir.OpCode.JOIN)
//    scan = generateCachedTree(engine, ir.OpCode.SCAN)
//  }
//
//  @TearDown(Level.Invocation)
//  def cleanup(): Unit = storage.initEvaluation()
//
//  @Benchmark def compile_noCache(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.dedicatedDotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(tree)
//    )
//  }
//
//  @Benchmark def compile_program(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.dedicatedDotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(programN)
//    )
//  }
//
//  @Benchmark def compile_naiveEval(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.dedicatedDotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(naiveEval)
//    )
//  }
//
//  @Benchmark def compile_doWhile(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.dedicatedDotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(doWhile)
//    )
//  }
//
//  @Benchmark def compile_snEval(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.dedicatedDotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(snEval)
//    )
//  }
//
//  @Benchmark def compile_snEvalRule(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.dedicatedDotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(snEvalRule)
//    )
//  }
//
//  @Benchmark def compile_join(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.dedicatedDotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(join)
//    )
//  }
//
//  @Benchmark def compile_scan(blackhole: Blackhole): Unit = {
//    given staging.Compiler = engine.dedicatedDotty
//
//    blackhole.consume(
//      engine.compiler.getCompiled(scan)
//    )
//  }
//}
