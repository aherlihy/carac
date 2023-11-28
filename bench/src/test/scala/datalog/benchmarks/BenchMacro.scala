package datalog.benchmarks

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.{Mode as JmhMode, *}
import org.openjdk.jmh.infra.Blackhole
import datalog.{AckermannWorstMacroCompiler as AckermannMacroCompiler, AckermannWorstMacroCompilerNoFacts as AckermannMacroCompilerNoFacts, AckermannOptimizedMacroCompiler}

import scala.compiletime.uninitialized
import datalog.execution.ir.InterpreterContext
import datalog.execution.{Backend, CompileSync, Granularity, JITOptions, Mode, SortOrder, StagedExecutionEngine}
import datalog.storage.DefaultStorageManager

import java.nio.file.Paths

object BenchMacro {
  val ackermannCompiled = AckermannMacroCompiler.compile()
  val ackermannNoFactsCompiled = AckermannMacroCompilerNoFacts.compile()
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
  def ackermann_macro_aot_offline = {
    val facts = Paths.get(AckermannMacroCompiler.factDir)
    val res = AckermannMacroCompiler.runCompiled(ackermannCompiled)(
      program => {} // facts already loaded at compile-time
    )
    println(s"macro AOT offline results =${res.size}")
  }

  /**
   * Only rules available at compile-time, no online optimization
   */
  @Benchmark
  def ackermann_macro_runtimefacts_offline = {
    val facts = Paths.get(AckermannMacroCompilerNoFacts.factDir)
    val res = AckermannMacroCompilerNoFacts.runCompiled(ackermannNoFactsCompiled)(
      program => program.loadFromFactDir(facts.toString)
    )
    println(s"macro runtimefacts offline, results =${res.size}")
  }

  /**
   * Both facts + rules available at compile-time, online optimization
   */
  @Benchmark
  def ackermann_macro_aot_online = {
    val facts = Paths.get(AckermannMacroCompiler.factDir)
    val res = AckermannMacroCompiler.runCompiled(ackermannCompiled)(
      program => {} // facts already loaded at compile-time
    )
    println(s"macro AOT online, results =${res.size}")
  }

  /**
   * Only rules available at compile-time, online optimization
   */
  @Benchmark
  def ackermann_macro_runtimefacts_online = {
    val facts = Paths.get(AckermannMacroCompilerNoFacts.factDir)
    val res = AckermannMacroCompilerNoFacts.runCompiled(ackermannNoFactsCompiled)(
      program => program.loadFromFactDir(facts.toString)
    )
    println(s"macro runtimefacts online, results =${res.size}")
  }

  /**
   * Nothing available at compile-time, runtime optimization, lambda
   */
  @Benchmark
  def ackermann_jit_lambda_online = {
    val facts = Paths.get(AckermannMacroCompiler.factDir)
    val engine = StagedExecutionEngine(DefaultStorageManager(), JITOptions(
      backend = Backend.Lambda,
      mode = Mode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel
    ))
    val program = AckermannMacroCompiler.makeProgram(engine)
    program.loadFromFactDir(facts.toString)
    val res = program.namedRelation(program.toSolve).solve()
    println(s"lambda results =${res.size}")
  }

  /**
   * Baseline, interp no optimization
   */
  @Benchmark
  def ackermann_interpreter_worst_baseline_offline = {
    val facts = Paths.get(AckermannMacroCompiler.factDir)
    val engine = StagedExecutionEngine(DefaultStorageManager(), JITOptions(
      mode = Mode.Interpreted, granularity = Granularity.NEVER, sortOrder = SortOrder.Unordered
    ))
    val program = AckermannMacroCompiler.makeProgram(engine)
    program.loadFromFactDir(facts.toString)
    val res = program.namedRelation(program.toSolve).solve()
    println(s"baseline results =${res.size}")
  }

  @Benchmark
  def ackermann_interpreter_best_baseline_offline = {
    val facts = Paths.get(AckermannOptimizedMacroCompiler.factDir)
    val engine = StagedExecutionEngine(DefaultStorageManager(), JITOptions(
      mode = Mode.Interpreted, granularity = Granularity.NEVER, sortOrder = SortOrder.Unordered
    ))
    val program = AckermannOptimizedMacroCompiler.makeProgram(engine)
    program.loadFromFactDir(facts.toString)
    val res = program.namedRelation(program.toSolve).solve()
    println(s"baseline hand-optimized results =${res.size}")
  }

}
