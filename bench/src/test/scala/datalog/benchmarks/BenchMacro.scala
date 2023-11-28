package datalog.benchmarks

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.{Mode as JmhMode, *}
import org.openjdk.jmh.infra.Blackhole
import datalog.{AckermannWorstMacroCompiler as AckermannMacroCompiler, AckermannWorstMacroCompilerWithFacts as AckermannMacroCompilerWithFacts, AckermannOptimizedMacroCompiler}

import scala.compiletime.uninitialized
import datalog.execution.ir.InterpreterContext
import datalog.execution.{Backend, CompileSync, Granularity, JITOptions, Mode, SortOrder, StagedExecutionEngine}
import datalog.storage.DefaultStorageManager

import java.nio.file.Paths

object BenchMacro {
  val ackermannCompiled = AckermannMacroCompiler.compile()
  val ackermannWithFactsCompiled = AckermannMacroCompilerWithFacts.compile()
  val ackermannOptimizedCompiled = AckermannOptimizedMacroCompiler.compile()
}
import BenchMacro.*

@Fork(1)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 100)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 100)
@State(Scope.Thread)
@BenchmarkMode(Array(JmhMode.AverageTime))
class BenchMacro {
  /**
   * Both facts + rules available at compile-time, no online optimization
   */
  @Benchmark
  def ackermann_macro_aot_offline(blackhole: Blackhole) = {
    val facts = Paths.get(AckermannMacroCompilerWithFacts.factDir)
    val res = AckermannMacroCompilerWithFacts.runCompiled(ackermannWithFactsCompiled)( // facts already loaded at compile-time
      program => ()//// println(s"size succ = ${program.namedRelation("succ").get().size}")
    )
    blackhole.consume(res)
    // println(s"macro AOT offline results =${res.size}")
  }

  /**
   * Only rules available at compile-time, no online optimization
   */
  @Benchmark
  def ackermann_macro_runtimefacts_offline(blackhole: Blackhole) = {
    val facts = Paths.get(AckermannMacroCompiler.factDir)
    val res = AckermannMacroCompiler.runCompiled(ackermannCompiled)(
      program =>
//        // println(s"size succ = ${program.namedRelation("succ").get().size}")
        program.loadFromFactDir(facts.toString)
    )
    blackhole.consume(res)
    // println(s"macro runtimefacts offline, results =${res.size}")
  }

  /**
   * Both facts + rules available at compile-time, online optimization
   */
  @Benchmark
  def ackermann_macro_aot_online(blackhole: Blackhole) = {
    val facts = Paths.get(AckermannMacroCompilerWithFacts.factDir)
    val res = AckermannMacroCompilerWithFacts.runCompiled(ackermannWithFactsCompiled)(
      program => {} // facts already loaded at compile-time
    )
    blackhole.consume(res)
    // println(s"macro AOT online, results =${res.size}")
  }

  /**
   * Only rules available at compile-time, online optimization
   */
  @Benchmark
  def ackermann_macro_runtimefacts_online(blackhole: Blackhole) = {
    val facts = Paths.get(AckermannMacroCompiler.factDir)
    val res = AckermannMacroCompiler.runCompiled(ackermannCompiled)(
      program => program.loadFromFactDir(facts.toString)
    )
    blackhole.consume(res)
    // println(s"macro runtimefacts online, results =${res.size}")
  }

  /**
   * Nothing available at compile-time, runtime optimization, lambda
   */
  @Benchmark
  def ackermann_jit_lambda_online(blackhole: Blackhole) = {
    val facts = Paths.get(AckermannMacroCompiler.factDir)
    val engine = StagedExecutionEngine(DefaultStorageManager(), JITOptions(
      backend = Backend.Lambda,
      mode = Mode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel
    ))
    val program = AckermannMacroCompiler.makeProgram(engine)
    program.loadFromFactDir(facts.toString)
    val res = program.namedRelation(program.toSolve).solve()
    blackhole.consume(res)
    // println(s"lambda results =${res.size}")
  }

  /**
   * Baseline, interp no optimization
   */
  @Benchmark
  def ackermann_interpreter_worst_baseline_offline(blackhole: Blackhole) = {
    val facts = Paths.get(AckermannMacroCompiler.factDir)
    val engine = StagedExecutionEngine(DefaultStorageManager(), JITOptions(
      mode = Mode.Interpreted, granularity = Granularity.NEVER, sortOrder = SortOrder.Unordered
    ))
    val program = AckermannMacroCompiler.makeProgram(engine)
    program.loadFromFactDir(facts.toString)
    val res = program.namedRelation(program.toSolve).solve()
    blackhole.consume(res)
    // println(s"baseline results =${res.size}")
  }

  @Benchmark
  def ackermann_interpreter_best_baseline_offline(blackhole: Blackhole) = {
    val facts = Paths.get(AckermannOptimizedMacroCompiler.factDir)
    val engine = StagedExecutionEngine(DefaultStorageManager(), JITOptions(
      mode = Mode.Interpreted, granularity = Granularity.NEVER, sortOrder = SortOrder.Unordered
    ))
    val program = AckermannOptimizedMacroCompiler.makeProgram(engine)
    program.loadFromFactDir(facts.toString)
    val res = program.namedRelation(program.toSolve).solve()
    blackhole.consume(res)
    // println(s"baseline hand-optimized results =${res.size}")
  }
}
