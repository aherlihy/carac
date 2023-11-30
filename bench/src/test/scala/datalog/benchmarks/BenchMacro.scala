package datalog.benchmarks

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.{Mode as JmhMode, *}
import org.openjdk.jmh.infra.Blackhole
import datalog.{
  AckermannWorstMacroCompiler as AckermannMacroCompiler,
  AckermannWorstMacroCompilerWithFacts as AckermannMacroCompilerWithFacts,
  AckermannWorstMacroCompilerWithFactsOnline as AckermannMacroCompilerWithFactsOnline,
  AckermannWorstMacroCompilerOnline as AckermannMacroCompilerOnline,
  FibWorstMacroCompiler as FibMacroCompiler,
  FibWorstMacroCompilerWithFacts as FibMacroCompilerWithFacts,
  FibWorstMacroCompilerWithFactsOnline as FibMacroCompilerWithFactsOnline,
  FibWorstMacroCompilerOnline as FibMacroCompilerOnline,
  PrimeWorstMacroCompiler as PrimeMacroCompiler,
  PrimeWorstMacroCompilerWithFacts as PrimeMacroCompilerWithFacts,
  PrimeWorstMacroCompilerWithFactsOnline as PrimeMacroCompilerWithFactsOnline,
  PrimeWorstMacroCompilerOnline as PrimeMacroCompilerOnline,
  TastyslistlibWorstMacroCompiler as TastyslistlibMacroCompiler,
  TastyslistlibWorstMacroCompilerWithFacts as TastyslistlibMacroCompilerWithFacts,
  TastyslistlibWorstMacroCompilerWithFactsOnline as TastyslistlibMacroCompilerWithFactsOnline,
  TastyslistlibWorstMacroCompilerOnline as TastyslistlibMacroCompilerOnline,
  TastyslistlibinverseWorstMacroCompiler as TastyslistlibinverseMacroCompiler,
  TastyslistlibinverseWorstMacroCompilerWithFacts as TastyslistlibinverseMacroCompilerWithFacts,
  TastyslistlibinverseWorstMacroCompilerWithFactsOnline as TastyslistlibinverseMacroCompilerWithFactsOnline,
  TastyslistlibinverseWorstMacroCompilerOnline as TastyslistlibinverseMacroCompilerOnline,
}

import scala.compiletime.uninitialized
import datalog.execution.ir.InterpreterContext
import datalog.execution.{Backend, CompileSync, Granularity, JITOptions, Mode, SortOrder, StagedExecutionEngine}
import datalog.storage.DefaultStorageManager

import java.nio.file.Paths

object BenchMacro {
  val ackermannCompiled = AckermannMacroCompiler.compile()
  val ackermannWithFactsCompiled = AckermannMacroCompilerWithFacts.compile()
  val ackermannOnlineCompiled = AckermannMacroCompilerOnline.compile()
  val ackermannWithFactsOnlineCompiled = AckermannMacroCompilerWithFactsOnline.compile()
  val fibCompiled = FibMacroCompiler.compile()
  val fibWithFactsCompiled = FibMacroCompilerWithFacts.compile()
  val fibOnlineCompiled = FibMacroCompilerOnline.compile()
  val fibWithFactsOnlineCompiled = FibMacroCompilerWithFactsOnline.compile()
  val primeCompiled = PrimeMacroCompiler.compile()
  val primeWithFactsCompiled = PrimeMacroCompilerWithFacts.compile()
  val primeOnlineCompiled = PrimeMacroCompilerOnline.compile()
  val primeWithFactsOnlineCompiled = PrimeMacroCompilerWithFactsOnline.compile()
  val tastyslistlibCompiled = TastyslistlibMacroCompiler.compile()
  val tastyslistlibWithFactsCompiled = TastyslistlibMacroCompilerWithFacts.compile()
  val tastyslistlibOnlineCompiled = TastyslistlibMacroCompilerOnline.compile()
  val tastyslistlibWithFactsOnlineCompiled = TastyslistlibMacroCompilerWithFactsOnline.compile()
  val tastyslistlibinverseCompiled = TastyslistlibinverseMacroCompiler.compile()
  val tastyslistlibinverseWithFactsCompiled = TastyslistlibinverseMacroCompilerWithFacts.compile()
  val tastyslistlibinverseOnlineCompiled = TastyslistlibinverseMacroCompilerOnline.compile()
  val tastyslistlibinverseWithFactsOnlineCompiled = TastyslistlibinverseMacroCompilerWithFactsOnline.compile()
}
import BenchMacro.*

@Fork(1)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 10)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 10)
@State(Scope.Thread)
@BenchmarkMode(Array(JmhMode.AverageTime))
class BenchMacro {
  /**
   * Both facts + rules available at compile-time, no online optimization
   */
  @Benchmark
  def ackermann_macro_aot_offline(blackhole: Blackhole) = {
    val expectedSize = 54
    val facts = Paths.get(AckermannMacroCompilerWithFacts.factDir)
    val res = AckermannMacroCompilerWithFacts.runCompiled(ackermannWithFactsCompiled)( // facts already loaded at compile-time
      program => ()//println(s"size succ = ${program.namedRelation("succ").get().size}")
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro AOT offline results =${res.size}")
  }

  /**
   * Only rules available at compile-time, no online optimization
   */
  @Benchmark
  def ackermann_macro_rules_offline(blackhole: Blackhole) = {
    val expectedSize = 54

    val facts = Paths.get(AckermannMacroCompiler.factDir)
    val res = AckermannMacroCompiler.runCompiled(ackermannCompiled)(
      program =>
        //println(s"size succ = ${program.namedRelation("succ").get().size}")
        program.loadFromFactDir(facts.toString)
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro rules offline, results =${res.size}")
  }

  /**
   * Both facts + rules available at compile-time, online optimization
   */
  @Benchmark
  def ackermann_macro_aot_online(blackhole: Blackhole) = {
    val expectedSize = 54

    val facts = Paths.get(AckermannMacroCompilerWithFactsOnline.factDir)
    val res = AckermannMacroCompilerWithFactsOnline.runCompiled(ackermannWithFactsOnlineCompiled)(
      program => {} // facts already loaded at compile-time
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro AOT online, results =${res.size}")
  }

  /**
   * Only rules available at compile-time, online optimization
   */
  @Benchmark
  def ackermann_macro_rules_online(blackhole: Blackhole) = {
    val expectedSize = 54

    val facts = Paths.get(AckermannMacroCompilerOnline.factDir)
    val res = AckermannMacroCompilerOnline.runCompiled(ackermannOnlineCompiled)(
      program => program.loadFromFactDir(facts.toString)
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro rules online, results =${res.size}")
  }

  /** ---------- Fib ---------- **/
  /**
   * Both facts + rules available at compile-time, no online optimization
   */
  @Benchmark
  def fib_macro_aot_offline(blackhole: Blackhole) = {
    val expectedSize = 21

    val facts = Paths.get(FibMacroCompilerWithFacts.factDir)
    val res = FibMacroCompilerWithFacts.runCompiled(fibWithFactsCompiled)( // facts already loaded at compile-time
      program => () //println(s"size succ = ${program.namedRelation("succ").get().size}")
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro AOT offline results =${res.size}")
  }

  /**
   * Only rules available at compile-time, no online optimization
   */
  @Benchmark
  def fib_macro_rules_offline(blackhole: Blackhole) = {
    val expectedSize = 21

    val facts = Paths.get(FibMacroCompiler.factDir)
    val res = FibMacroCompiler.runCompiled(fibCompiled)(
      program =>
        //println(s"size succ = ${program.namedRelation("succ").get().size}")
        program.loadFromFactDir(facts.toString)
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro rules offline, results =${res.size}")
  }

  /**
   * Both facts + rules available at compile-time, online optimization
   */
  @Benchmark
  def fib_macro_aot_online(blackhole: Blackhole) = {
    val expectedSize = 21

    val facts = Paths.get(FibMacroCompilerWithFactsOnline.factDir)
    val res = FibMacroCompilerWithFactsOnline.runCompiled(fibWithFactsOnlineCompiled)(
      program => {} // facts already loaded at compile-time
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro AOT online, results =${res.size}")
  }

  /**
   * Only rules available at compile-time, online optimization
   */
  @Benchmark
  def fib_macro_rules_online(blackhole: Blackhole) = {
    val expectedSize = 21

    val facts = Paths.get(FibMacroCompilerOnline.factDir)
    val res = FibMacroCompilerOnline.runCompiled(fibOnlineCompiled)(
      program => program.loadFromFactDir(facts.toString)
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro rules online, results =${res.size}")
  }

  /** ---------- Prime ---------- * */

  /**
   * Both facts + rules available at compile-time, no online optimization
   */
  @Benchmark
  def prime_macro_aot_offline(blackhole: Blackhole) = {
    val expectedSize = 4

    val facts = Paths.get(PrimeMacroCompilerWithFacts.factDir)
    val res = PrimeMacroCompilerWithFacts.runCompiled(primeWithFactsCompiled)( // facts already loaded at compile-time
      program => () //println(s"size succ = ${program.namedRelation("succ").get().size}")
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro AOT offline results =${res.size}")
  }

  /**
   * Only rules available at compile-time, no online optimization
   */
  @Benchmark
  def prime_macro_rules_offline(blackhole: Blackhole) = {
    val expectedSize = 4

    val facts = Paths.get(PrimeMacroCompiler.factDir)
    val res = PrimeMacroCompiler.runCompiled(primeCompiled)(
      program =>
        //println(s"size succ = ${program.namedRelation("succ").get().size}")
        program.loadFromFactDir(facts.toString)
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro rules offline, results =${res.size}")
  }

  /**
   * Both facts + rules available at compile-time, online optimization
   */
  @Benchmark
  def prime_macro_aot_online(blackhole: Blackhole) = {
    val expectedSize = 4

    val facts = Paths.get(PrimeMacroCompilerWithFactsOnline.factDir)
    val res = PrimeMacroCompilerWithFactsOnline.runCompiled(primeWithFactsOnlineCompiled)(
      program => {} // facts already loaded at compile-time
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro AOT online, results =${res.size}")

  }

  /**
   * Only rules available at compile-time, online optimization
   */
  @Benchmark
  def prime_macro_rules_online(blackhole: Blackhole) = {
    val expectedSize = 4

    val facts = Paths.get(PrimeMacroCompilerOnline.factDir)
    val res = PrimeMacroCompilerOnline.runCompiled(primeOnlineCompiled)(
      program => program.loadFromFactDir(facts.toString)
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro rules online, results =${res.size}")

  }

  /** ---------- Tastyslistlib ---------- * */

  /**
   * Both facts + rules available at compile-time, no online optimization
   */
  @Benchmark
  def tastyslistlib_macro_aot_offline(blackhole: Blackhole) = {
    val expectedSize = 224

    val facts = Paths.get(TastyslistlibMacroCompilerWithFacts.factDir)
    val res = TastyslistlibMacroCompilerWithFacts.runCompiled(tastyslistlibWithFactsCompiled)( // facts already loaded at compile-time
      program => () //println(s"size succ = ${program.namedRelation("succ").get().size}")
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro AOT offline results =${res.size}")

  }

  /**
   * Only rules available at compile-time, no online optimization
   */
  @Benchmark
  def tastyslistlib_macro_rules_offline(blackhole: Blackhole) = {
    val expectedSize = 224

    val facts = Paths.get(TastyslistlibMacroCompiler.factDir)
    val res = TastyslistlibMacroCompiler.runCompiled(tastyslistlibCompiled)(
      program =>
        val edbs = program.ee.storageManager.getAllEDBS()
        program.loadFromFactDir(facts.toString)
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro rules offline, results =${res.size}")

  }

  /**
   * Both facts + rules available at compile-time, online optimization
   */
  @Benchmark
  def tastyslistlib_macro_aot_online(blackhole: Blackhole) = {
    val expectedSize = 224

    val facts = Paths.get(TastyslistlibMacroCompilerWithFactsOnline.factDir)
    val res = TastyslistlibMacroCompilerWithFactsOnline.runCompiled(tastyslistlibWithFactsOnlineCompiled)(
      program => {} // facts already loaded at compile-time
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro AOT online, results =${res.size}")

  }

  /**
   * Only rules available at compile-time, online optimization
   */
  @Benchmark
  def tastyslistlib_macro_rules_online(blackhole: Blackhole) = {
    val expectedSize = 224

    val facts = Paths.get(TastyslistlibMacroCompilerOnline.factDir)
    val res = TastyslistlibMacroCompilerOnline.runCompiled(tastyslistlibOnlineCompiled)(
      program => program.loadFromFactDir(facts.toString)
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro rules online, results =${res.size}")

  }

  /** ---------- Tastyslistlibinverse ---------- * */

  /**
   * Both facts + rules available at compile-time, no online optimization
   */
  @Benchmark
  def tastyslistlibinverse_macro_aot_offline(blackhole: Blackhole) = {
    val expectedSize = 1

    val facts = Paths.get(TastyslistlibinverseMacroCompilerWithFacts.factDir)
    val res = TastyslistlibinverseMacroCompilerWithFacts.runCompiled(tastyslistlibinverseWithFactsCompiled)( // facts already loaded at compile-time
      program => () //println(s"size succ = ${program.namedRelation("succ").get().size}")
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro AOT offline results =${res.size}")

  }

  /**
   * Only rules available at compile-time, no online optimization
   */
  @Benchmark
  def tastyslistlibinverse_macro_rules_offline(blackhole: Blackhole) = {
    val expectedSize = 1

    val facts = Paths.get(TastyslistlibinverseMacroCompiler.factDir)
    val res = TastyslistlibinverseMacroCompiler.runCompiled(tastyslistlibinverseCompiled)(
      program =>
        //println(s"size succ = ${program.namedRelation("succ").get().size}")
        program.loadFromFactDir(facts.toString)
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro rules offline, results =${res.size}")

  }

  /**
   * Both facts + rules available at compile-time, online optimization
   */
  @Benchmark
  def tastyslistlibinverse_macro_aot_online(blackhole: Blackhole) = {
    val expectedSize = 1

    val facts = Paths.get(TastyslistlibinverseMacroCompilerWithFactsOnline.factDir)
    val res = TastyslistlibinverseMacroCompilerWithFactsOnline.runCompiled(tastyslistlibinverseWithFactsOnlineCompiled)(
      program => {} // facts already loaded at compile-time
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro AOT online, results =${res.size}")

  }

  /**
   * Only rules available at compile-time, online optimization
   */
  @Benchmark
  def tastyslistlibinverse_macro_rules_online(blackhole: Blackhole) = {
    val expectedSize = 1

    val facts = Paths.get(TastyslistlibinverseMacroCompilerOnline.factDir)
    val res = TastyslistlibinverseMacroCompilerOnline.runCompiled(tastyslistlibinverseOnlineCompiled)(
      program => program.loadFromFactDir(facts.toString)
    )
    assert(res.size==expectedSize)
    blackhole.consume(res)
    // println(s"macro rules online, results =${res.size}")

  }
}
