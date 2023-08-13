package datalog.benchmarks

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import datalog.{
  AckermannWorstMacroCompiler, AckermannOptimizedMacroCompiler,
  SimpleMacroCompiler, SimpleProgram
}

import scala.compiletime.uninitialized
import datalog.execution.ir.InterpreterContext
import datalog.execution.{Backend, Granularity, Mode, StagedExecutionEngine}
import datalog.storage.DefaultStorageManager

import java.nio.file.Paths

object BenchMacro {
  val simpleCompiled = SimpleMacroCompiler.compile()
  val ackermannOptCompiled = AckermannOptimizedMacroCompiler.compile()
  val ackermannWorstCompiled = AckermannWorstMacroCompiler.compile()
}
import BenchMacro.*

@Fork(1)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 100)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 100)
@State(Scope.Thread)
// @BenchmarkMode(Array(Mode.AverageTime))
class BenchMacro {
  /** Add extra facts at runtime. */
  def addExtraFacts(program: SimpleProgram): Unit =
    program.edge("b", "c") :- ()
    program.edge("d", "e") :- ()
    program.edge("e", "f") :- ()
    program.edge("f", "g") :- ()
    program.edge("g", "h") :- ()
    program.edge("h", "i") :- ()
    program.edge("i", "j") :- ()
    program.edge("j", "k") :- ()
    program.edge("k", "l") :- ()
    program.edge("l", "m") :- ()

  @Benchmark
  def simple_macro = {
    SimpleMacroCompiler.runCompiled(simpleCompiled)(addExtraFacts)
  }

  @Benchmark
  def ackermann_opt_macro = {
    val facts = Paths.get(AckermannOptimizedMacroCompiler.factDir)
    val res = AckermannOptimizedMacroCompiler.runCompiled(ackermannOptCompiled)(
      program => program.loadFromFactDir(facts.toString)
    )
  }

  @Benchmark
  def ackermann_worst_macro = {
    val facts = Paths.get(AckermannWorstMacroCompiler.factDir)
    val res = AckermannWorstMacroCompiler.runCompiled(ackermannWorstCompiled)(
      program => program.loadFromFactDir(facts.toString)
    )
    // println(res)
  }

  @Benchmark
  def ackermann_worst_lambda = {
    val engine = StagedExecutionEngine(DefaultStorageManager(), AckermannWorstMacroCompiler.jitOptions.copy(backend = Backend.Lambda))
    val facts = Paths.get(AckermannWorstMacroCompiler.factDir)
    val program = AckermannWorstMacroCompiler.makeProgram(engine)
    program.loadFromFactDir(facts.toString)
    val res = program.namedRelation(program.toSolve).solve()
    // println(res)
  }

  @Benchmark
  def ackermann_opt_lambda = {
    val engine = StagedExecutionEngine(DefaultStorageManager(), AckermannOptimizedMacroCompiler.jitOptions.copy(backend = Backend.Lambda))
    val facts = Paths.get(AckermannOptimizedMacroCompiler.factDir)
    val program = AckermannOptimizedMacroCompiler.makeProgram(engine)
    program.loadFromFactDir(facts.toString)
    val res = program.namedRelation(program.toSolve).solve()
    // println(res)
  }

  @Benchmark
  def simple_interpreter = {
    val engine = StagedExecutionEngine(DefaultStorageManager(), SimpleMacroCompiler.jitOptions.copy(
      mode = Mode.Interpreted, granularity = Granularity.NEVER))
    val program = SimpleMacroCompiler.makeProgram(engine)
    addExtraFacts(program)
    program.namedRelation(program.toSolve).solve()
  }

  @Benchmark
  def simple_lambda = {
    val engine = StagedExecutionEngine(DefaultStorageManager(), SimpleMacroCompiler.jitOptions.copy(backend = Backend.Lambda))
    val program = SimpleMacroCompiler.makeProgram(engine)
    addExtraFacts(program)
    program.namedRelation(program.toSolve).solve()
  }
}
