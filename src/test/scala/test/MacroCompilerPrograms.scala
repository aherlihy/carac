package datalog

import buildinfo.BuildInfo
import datalog.execution.{Backend, ExecutionEngine,
  JITOptions, MacroCompiler, SolvableProgram, SortOrder}
import datalog.dsl.*
import datalog.execution.ir.*
import datalog.storage.{DefaultStorageManager, StorageManager}
import test.examples.ackermann.*
import test.examples.fib.*
import test.examples.prime.*
import test.examples.cbaexprvalue.*
import test.examples.equal.*
import test.examples.tastyslistlib.*
import test.examples.tastyslistlibinverse.*

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

object SimpleMacroCompiler extends MacroCompiler(
  SimpleProgram(_),
  JITOptions(backend = Backend.MacroQuotes)) {
  // Currently crashes without the `this.` prefix: https://github.com/lampepfl/dotty/issues/18434
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
}

// TODO: move these into the test files

/** ---------- Ackermann ---------- **/
class AckermannWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with ackermann_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
class AckermannWorstProgramWithFacts(engine: ExecutionEngine) extends SolvableProgram(engine) with ackermann_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
  loadFromFactDir(factDir)
  pretest(this)
}

object AckermannWorstMacroCompiler extends MacroCompiler(
  AckermannWorstProgram(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
//  println("Ackermann NO FACTS")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}

object AckermannWorstMacroCompilerWithFacts extends MacroCompiler(
  AckermannWorstProgramWithFacts(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
//  println("Ackermann WITH FACTS OFFLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}

object AckermannWorstMacroCompilerOnline extends MacroCompiler(
  AckermannWorstProgram(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
//  println("Ackermann NO FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}

object AckermannWorstMacroCompilerWithFactsOnline extends MacroCompiler(
  AckermannWorstProgramWithFacts(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
//  println("Ackermann WITH FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}

/** ---------- Fib ---------- **/
class FibWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with fib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
class FibWorstProgramWithFacts(engine: ExecutionEngine) extends SolvableProgram(engine) with fib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
  loadFromFactDir(factDir)
  pretest(this)
}

object FibWorstMacroCompiler extends MacroCompiler(
  FibWorstProgram(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  //  println("Fib NO FACTS")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
}

object FibWorstMacroCompilerWithFacts extends MacroCompiler(
  FibWorstProgramWithFacts(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  //  println("Fib WITH FACTS OFFLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
}

object FibWorstMacroCompilerOnline extends MacroCompiler(
  FibWorstProgram(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  //  println("Fib NO FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
}

object FibWorstMacroCompilerWithFactsOnline extends MacroCompiler(
  FibWorstProgramWithFacts(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  //  println("Fib WITH FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
}

/** ---------- Prime ---------- **/
class PrimeWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with prime_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
class PrimeWorstProgramWithFacts(engine: ExecutionEngine) extends SolvableProgram(engine) with prime_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
  loadFromFactDir(factDir)
  pretest(this)
}

object PrimeWorstMacroCompiler extends MacroCompiler(
  PrimeWorstProgram(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  //  println("Prime NO FACTS")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
}

object PrimeWorstMacroCompilerWithFacts extends MacroCompiler(
  PrimeWorstProgramWithFacts(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  //  println("Prime WITH FACTS OFFLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
}

object PrimeWorstMacroCompilerOnline extends MacroCompiler(
  PrimeWorstProgram(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  //  println("Prime NO FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
}

object PrimeWorstMacroCompilerWithFactsOnline extends MacroCompiler(
  PrimeWorstProgramWithFacts(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  //  println("Prime WITH FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
}

/** ---------- Equal ---------- **/
class EqualWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with equal_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/equal/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
class EqualWorstProgramWithFacts(engine: ExecutionEngine) extends SolvableProgram(engine) with equal_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/equal/facts"
  loadFromFactDir(factDir)
  pretest(this)
}

object EqualWorstMacroCompiler extends MacroCompiler(
  EqualWorstProgram(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  //  println("Equal NO FACTS")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/equal/facts"
}

object EqualWorstMacroCompilerWithFacts extends MacroCompiler(
  EqualWorstProgramWithFacts(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  //  println("Equal WITH FACTS OFFLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/equal/facts"
}

object EqualWorstMacroCompilerOnline extends MacroCompiler(
  EqualWorstProgram(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  //  println("Equal NO FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/equal/facts"
}

object EqualWorstMacroCompilerWithFactsOnline extends MacroCompiler(
  EqualWorstProgramWithFacts(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  //  println("Equal WITH FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/equal/facts"
}

/** ---------- Cbaexprvalue ---------- **/
class CbaexprvalueWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with cbaexprvalue_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
class CbaexprvalueWorstProgramWithFacts(engine: ExecutionEngine) extends SolvableProgram(engine) with cbaexprvalue_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
  loadFromFactDir(factDir)
  pretest(this)
}

object CbaexprvalueWorstMacroCompiler extends MacroCompiler(
  CbaexprvalueWorstProgram(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  //  println("Cbaexprvalue NO FACTS")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
}

object CbaexprvalueWorstMacroCompilerWithFacts extends MacroCompiler(
  CbaexprvalueWorstProgramWithFacts(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  //  println("Cbaexprvalue WITH FACTS OFFLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
}

object CbaexprvalueWorstMacroCompilerOnline extends MacroCompiler(
  CbaexprvalueWorstProgram(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  //  println("Cbaexprvalue NO FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
}

object CbaexprvalueWorstMacroCompilerWithFactsOnline extends MacroCompiler(
  CbaexprvalueWorstProgramWithFacts(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  //  println("Cbaexprvalue WITH FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
}

/** ---------- Tastyslistlib ---------- **/
class TastyslistlibWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
class TastyslistlibWorstProgramWithFacts(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
  loadFromFactDir(factDir)
  pretest(this)
}

object TastyslistlibWorstMacroCompiler extends MacroCompiler(
  TastyslistlibWorstProgram(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  //  println("Tastyslistlib NO FACTS")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
}

object TastyslistlibWorstMacroCompilerWithFacts extends MacroCompiler(
  TastyslistlibWorstProgramWithFacts(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  //  println("Tastyslistlib WITH FACTS OFFLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
}

object TastyslistlibWorstMacroCompilerOnline extends MacroCompiler(
  TastyslistlibWorstProgram(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  //  println("Tastyslistlib NO FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
}

object TastyslistlibWorstMacroCompilerWithFactsOnline extends MacroCompiler(
  TastyslistlibWorstProgramWithFacts(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  //  println("Tastyslistlib WITH FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
}

/** ---------- Tastyslistlibinverse ---------- **/
class TastyslistlibinverseWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlibinverse_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
class TastyslistlibinverseWorstProgramWithFacts(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlibinverse_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
  loadFromFactDir(factDir)
  pretest(this)
}

object TastyslistlibinverseWorstMacroCompiler extends MacroCompiler(
  TastyslistlibinverseWorstProgram(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  //  println("Tastyslistlibinverse NO FACTS")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
}

object TastyslistlibinverseWorstMacroCompilerWithFacts extends MacroCompiler(
  TastyslistlibinverseWorstProgramWithFacts(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax)) {
  //  println("Tastyslistlibinverse WITH FACTS OFFLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
}

object TastyslistlibinverseWorstMacroCompilerOnline extends MacroCompiler(
  TastyslistlibinverseWorstProgram(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  //  println("Tastyslistlibinverse NO FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
}

object TastyslistlibinverseWorstMacroCompilerWithFactsOnline extends MacroCompiler(
  TastyslistlibinverseWorstProgramWithFacts(_),
  JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  //  println("Tastyslistlibinverse WITH FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
}
