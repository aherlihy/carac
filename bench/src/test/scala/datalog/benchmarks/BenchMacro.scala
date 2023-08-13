package datalog.benchmarks

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import datalog.{SimpleProgram, SimpleMacroCompiler as simple}

import scala.compiletime.uninitialized

import datalog.execution.ir.InterpreterContext
import datalog.execution.{Backend, Granularity, Mode, StagedExecutionEngine}
import datalog.storage.DefaultStorageManager

object BenchMacro {
  val simpleCompiled = simple.compile()
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
    simple.runCompiled(simpleCompiled)(addExtraFacts)
  }

  @Benchmark
  def simple_interpreter = {
    val engine = StagedExecutionEngine(DefaultStorageManager(), simple.jitOptions.copy(
      mode = Mode.Interpreted, granularity = Granularity.NEVER))
    val program = simple.makeProgram(engine)
    addExtraFacts(program)
    program.toSolve.solve()
  }

  @Benchmark
  def simple_lambda = {
    val engine = StagedExecutionEngine(DefaultStorageManager(), simple.jitOptions.copy(backend = Backend.Lambda))
    val program = simple.makeProgram(engine)
    addExtraFacts(program)
    program.toSolve.solve()
  }
}
