package datalog.benchmarks

import datalog.execution.ir.InterpreterContext
import datalog.execution.{Mode, *}
import datalog.dsl.Program
import datalog.storage.DefaultStorageManager

import org.openjdk.jmh.annotations.{Mode as JmhMode, *}
import org.openjdk.jmh.infra.Blackhole

import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import scala.compiletime.uninitialized

import test.examples.ackermann.ackermann_worst
import test.examples.ackermann.ackermann_optimized
import test.examples.fib.fib_worst
import test.examples.fib.fib_optimized
import test.examples.prime.prime_worst
import test.examples.prime.prime_optimized
import test.examples.tastyslistlib.tastyslistlib_worst
import test.examples.tastyslistlib.tastyslistlib_optimized
import test.examples.tastyslistlibinverse.tastyslistlibinverse_worst
import test.examples.tastyslistlibinverse.tastyslistlibinverse_optimized

object AckermannWorst extends ackermann_worst
object AckermannOptimized extends ackermann_optimized
object FibWorst extends fib_worst
object FibOptimized extends fib_optimized
object PrimeWorst extends prime_worst
object PrimeOptimized extends prime_optimized
object TastyslistlibWorst extends tastyslistlib_worst
object TastyslistlibOptimized extends tastyslistlib_optimized
object TastyslistlibinverseWorst extends tastyslistlibinverse_worst
object TastyslistlibinverseOptimized extends tastyslistlibinverse_optimized

@Fork(1)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 10)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 10)
@State(Scope.Thread)
@BenchmarkMode(Array(JmhMode.AverageTime))
class BenchMacroBaseline {
  /**-----------------Ackermann-----------------**/
  @Benchmark
  def ackermann_jit_lambda_online(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(AckermannWorst.factDirectory)
    AckermannWorst.pretest(program)

    blackhole.consume(
      program.namedRelation(AckermannWorst.toSolve).solve()
    )
  }

  @Benchmark
  def ackermann_interpreter_optimized_offline(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.Interpreted)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(AckermannOptimized.factDirectory)
    AckermannOptimized.pretest(program)

    blackhole.consume(
      program.namedRelation(AckermannOptimized.toSolve).solve()
    )
  }

  @Benchmark
  def zzzackermann_interpreter_worst_offline(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.Interpreted)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(AckermannWorst.factDirectory)
    AckermannWorst.pretest(program)

    blackhole.consume(
      program.namedRelation(AckermannWorst.toSolve).solve()
    )
  }

  /** -----------------Fib-----------------* */
  @Benchmark
  def fib_jit_lambda_online(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(FibWorst.factDirectory)
    FibWorst.pretest(program)

    blackhole.consume(
      program.namedRelation(FibWorst.toSolve).solve()
    )
  }

  @Benchmark
  def fib_interpreter_optimized_offline(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.Interpreted)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(FibOptimized.factDirectory)
    FibOptimized.pretest(program)

    blackhole.consume(
      program.namedRelation(FibOptimized.toSolve).solve()
    )
  }

  @Benchmark
  def zzzfib_interpreter_worst_offline(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.Interpreted)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(FibWorst.factDirectory)
    FibWorst.pretest(program)

    blackhole.consume(
      program.namedRelation(FibWorst.toSolve).solve()
    )
  }

  /** -----------------Prime-----------------* */
  @Benchmark
  def prime_jit_lambda_online(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(PrimeWorst.factDirectory)
    PrimeWorst.pretest(program)

    blackhole.consume(
      program.namedRelation(PrimeWorst.toSolve).solve()
    )
  }

  @Benchmark
  def prime_interpreter_optimized_offline(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.Interpreted)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(PrimeOptimized.factDirectory)
    PrimeOptimized.pretest(program)

    blackhole.consume(
      program.namedRelation(PrimeOptimized.toSolve).solve()
    )
  }

  @Benchmark
  def zzzprime_interpreter_worst_offline(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.Interpreted)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(PrimeWorst.factDirectory)
    PrimeWorst.pretest(program)

    blackhole.consume(
      program.namedRelation(PrimeWorst.toSolve).solve()
    )
  }

  /** -----------------Tastyslistlib-----------------* */
  @Benchmark
  def tastyslistlib_jit_lambda_online(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(TastyslistlibWorst.factDirectory)
    TastyslistlibWorst.pretest(program)

    blackhole.consume(
      program.namedRelation(TastyslistlibWorst.toSolve).solve()
    )
  }

  @Benchmark
  def tastyslistlib_interpreter_optimized_offline(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.Interpreted)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(TastyslistlibOptimized.factDirectory)
    TastyslistlibOptimized.pretest(program)

    blackhole.consume(
      program.namedRelation(TastyslistlibOptimized.toSolve).solve()
    )
  }

  @Benchmark
  def zzztastyslistlib_interpreter_worst_offline(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.Interpreted)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(TastyslistlibWorst.factDirectory)
    TastyslistlibWorst.pretest(program)

    blackhole.consume(
      program.namedRelation(TastyslistlibWorst.toSolve).solve()
    )
  }

  /** -----------------Tastyslistlibinverse-----------------* */
  @Benchmark
  def tastyslistlibinverse_jit_lambda_online(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(TastyslistlibinverseWorst.factDirectory)
    TastyslistlibinverseWorst.pretest(program)

    blackhole.consume(
      program.namedRelation(TastyslistlibinverseWorst.toSolve).solve()
    )
  }

  @Benchmark
  def tastyslistlibinverse_interpreter_optimized_offline(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.Interpreted)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(TastyslistlibinverseOptimized.factDirectory)
    TastyslistlibinverseOptimized.pretest(program)

    blackhole.consume(
      program.namedRelation(TastyslistlibinverseOptimized.toSolve).solve()
    )
  }

  @Benchmark
  def zzztastyslistlibinverse_interpreter_worst_offline(blackhole: Blackhole) = {
    val jo = JITOptions(mode = Mode.Interpreted)
    val engine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(TastyslistlibinverseWorst.factDirectory)
    TastyslistlibinverseWorst.pretest(program)

    blackhole.consume(
      program.namedRelation(TastyslistlibinverseWorst.toSolve).solve()
    )
  }
}
