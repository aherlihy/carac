package datalog

import buildinfo.BuildInfo
import datalog.execution.{Backend, ExecutionEngine, JITOptions, MacroCompiler, SolvableProgram, SortOrder}
import datalog.dsl.*
import datalog.execution.ir.*
import datalog.storage.{DefaultStorageManager, StorageManager}
import test.examples.ackermann.*
import test.examples.fib.*
import test.examples.prime.*
import test.examples.fib.*
import test.examples.prime.*
import test.examples.tastyslistlib.*
import test.examples.tastyslistlibinverse.*
import test.examples.prime.*

import java.nio.file.Paths

/// Used in MacroCompilerTest + MacroBench

class SimpleProgram(engine: ExecutionEngine) extends SolvableProgram(engine) {
  val edge = relation[Constant]("edge")
  val path = relation[Constant]("path")
  val pathFromA = relation[Constant]("pathFromA")

  val x, y, z = variable()

  edge("a", "b") :- ()

  edge("c", "d") :- ()

  path(x, y) :- edge(x, y)
  path(x, z) :- (edge(x, y), path(y, z))

  pathFromA(x) :- path("a", x)

  override val toSolve = pathFromA.name
}

object SimpleMacroCompiler extends MacroCompiler(SimpleProgram(_), JITOptions(backend = Backend.MacroQuotes)) {
  // Currently crashes without the `this.` prefix: https://github.com/lampepfl/dotty/issues/18434
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
}

// TODO: move these into the test files

// Ackermann
class AckermannOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with ackermann_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object AckermannOptimizedMacroCompiler extends MacroCompiler(AckermannOptimizedProgram(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}

class AckermannWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with ackermann_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object AckermannWorstMacroCompiler extends MacroCompiler(AckermannWorstProgram(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
//  println("Ackermann NO FACTS")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}

class AckermannWorstProgramWithFacts(engine: ExecutionEngine) extends SolvableProgram(engine) with ackermann_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
  loadFromFactDir(factDir)
  pretest(this)
}
object AckermannWorstMacroCompilerWithFacts extends MacroCompiler(AckermannWorstProgramWithFacts(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
//  println("Ackermann WITH FACTS OFFLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}
class AckermannWorstProgramOnline(engine: ExecutionEngine) extends SolvableProgram(engine) with ackermann_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object AckermannWorstMacroCompilerOnline extends MacroCompiler(AckermannWorstProgramOnline(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
//  println("Ackermann NO FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}

class AckermannWorstProgramWithFactsOnline(engine: ExecutionEngine) extends SolvableProgram(engine) with ackermann_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
  loadFromFactDir(factDir)
  pretest(this)
}
object AckermannWorstMacroCompilerWithFactsOnline extends MacroCompiler(AckermannWorstProgramWithFactsOnline(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
//  println("Ackermann WITH FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}

/** ---------- Fib ---------- **/
class FibOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with fib_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object FibOptimizedMacroCompiler extends MacroCompiler(FibOptimizedProgram(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
}

class FibWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with fib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object FibWorstMacroCompiler extends MacroCompiler(FibWorstProgram(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
}

class FibWorstProgramWithFacts(engine: ExecutionEngine) extends SolvableProgram(engine) with fib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
  loadFromFactDir(factDir)
  pretest(this)
}
object FibWorstMacroCompilerWithFacts extends MacroCompiler(FibWorstProgramWithFacts(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
}
class FibWorstProgramOnline(engine: ExecutionEngine) extends SolvableProgram(engine) with fib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object FibWorstMacroCompilerOnline extends MacroCompiler(FibWorstProgramOnline(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
}

class FibWorstProgramWithFactsOnline(engine: ExecutionEngine) extends SolvableProgram(engine) with fib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
  loadFromFactDir(factDir)
  pretest(this)
}
object FibWorstMacroCompilerWithFactsOnline extends MacroCompiler(FibWorstProgramWithFactsOnline(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
}

/** ---------- Prime ---------- **/
class PrimeOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with prime_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object PrimeOptimizedMacroCompiler extends MacroCompiler(PrimeOptimizedProgram(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
}

class PrimeWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with prime_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object PrimeWorstMacroCompiler extends MacroCompiler(PrimeWorstProgram(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
}

class PrimeWorstProgramWithFacts(engine: ExecutionEngine) extends SolvableProgram(engine) with prime_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
  loadFromFactDir(factDir)
  pretest(this)
}
object PrimeWorstMacroCompilerWithFacts extends MacroCompiler(PrimeWorstProgramWithFacts(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
}
class PrimeWorstProgramOnline(engine: ExecutionEngine) extends SolvableProgram(engine) with prime_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object PrimeWorstMacroCompilerOnline extends MacroCompiler(PrimeWorstProgramOnline(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
}

class PrimeWorstProgramWithFactsOnline(engine: ExecutionEngine) extends SolvableProgram(engine) with prime_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
  loadFromFactDir(factDir)
  pretest(this)
}
object PrimeWorstMacroCompilerWithFactsOnline extends MacroCompiler(PrimeWorstProgramWithFactsOnline(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
}

/** ---------- Tastyslistlib ---------- **/
class TastyslistlibOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlib_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object TastyslistlibOptimizedMacroCompiler extends MacroCompiler(TastyslistlibOptimizedProgram(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
}

class TastyslistlibWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object TastyslistlibWorstMacroCompiler extends MacroCompiler(TastyslistlibWorstProgram(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
}

class TastyslistlibWorstProgramWithFacts(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
  loadFromFactDir(factDir)
  pretest(this)
}
object TastyslistlibWorstMacroCompilerWithFacts extends MacroCompiler(TastyslistlibWorstProgramWithFacts(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
}
class TastyslistlibWorstProgramOnline(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object TastyslistlibWorstMacroCompilerOnline extends MacroCompiler(TastyslistlibWorstProgramOnline(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
}

class TastyslistlibWorstProgramWithFactsOnline(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
  loadFromFactDir(factDir)
  pretest(this)
}
object TastyslistlibWorstMacroCompilerWithFactsOnline extends MacroCompiler(TastyslistlibWorstProgramWithFactsOnline(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
}

/** ---------- Tastyslistlibinverse ---------- **/
class TastyslistlibinverseOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlibinverse_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object TastyslistlibinverseOptimizedMacroCompiler extends MacroCompiler(TastyslistlibinverseOptimizedProgram(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
}

class TastyslistlibinverseWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlibinverse_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object TastyslistlibinverseWorstMacroCompiler extends MacroCompiler(TastyslistlibinverseWorstProgram(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
}

class TastyslistlibinverseWorstProgramWithFacts(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlibinverse_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
  loadFromFactDir(factDir)
  pretest(this)
}
object TastyslistlibinverseWorstMacroCompilerWithFacts extends MacroCompiler(TastyslistlibinverseWorstProgramWithFacts(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
}
class TastyslistlibinverseWorstProgramOnline(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlibinverse_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object TastyslistlibinverseWorstMacroCompilerOnline extends MacroCompiler(TastyslistlibinverseWorstProgramOnline(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
}

class TastyslistlibinverseWorstProgramWithFactsOnline(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlibinverse_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
  loadFromFactDir(factDir)
  pretest(this)
}
object TastyslistlibinverseWorstMacroCompilerWithFactsOnline extends MacroCompiler(TastyslistlibinverseWorstProgramWithFactsOnline(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
}

