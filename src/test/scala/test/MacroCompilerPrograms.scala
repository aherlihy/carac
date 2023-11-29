package datalog

import buildinfo.BuildInfo
import datalog.execution.{Backend, ExecutionEngine, JITOptions, MacroCompiler, SolvableProgram, SortOrder}
import datalog.dsl.*
import datalog.execution.ir.*
import datalog.storage.{DefaultStorageManager, StorageManager}
import test.examples.ackermann.*
import test.examples.cbaexprvalue.*
import test.examples.equal.*
import test.examples.fib.*
import test.examples.prime.*
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
  println("Ackermann WITH FACTS OFFLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}
class AckermannWorstProgramOnline(engine: ExecutionEngine) extends SolvableProgram(engine) with ackermann_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object AckermannWorstMacroCompilerOnline extends MacroCompiler(AckermannWorstProgramOnline(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  println("Ackermann NO FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}

class AckermannWorstProgramWithFactsOnline(engine: ExecutionEngine) extends SolvableProgram(engine) with ackermann_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
  loadFromFactDir(factDir)
  pretest(this)
}
object AckermannWorstMacroCompilerWithFactsOnline extends MacroCompiler(AckermannWorstProgramWithFactsOnline(_), JITOptions(backend = Backend.MacroQuotes, sortOrder = SortOrder.IntMax, runtimeSort = SortOrder.Sel)) {
  println("Ackermann WITH FACTS ONLINE")
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}
