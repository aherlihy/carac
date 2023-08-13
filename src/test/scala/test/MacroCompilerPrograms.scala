package test

import buildinfo.BuildInfo
import datalog.execution.{ExecutionEngine, MacroCompiler, SolvableProgram}
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

object SimpleMacroCompiler extends MacroCompiler(SimpleProgram(_)) {
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
object AckermannOptimizedMacroCompiler extends MacroCompiler(AckermannOptimizedProgram(_)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}

class AckermannWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with ackermann_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object AckermannWorstMacroCompiler extends MacroCompiler(AckermannWorstProgram(_)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}

//CBAExprValue
class CbaexprvalueOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with cbaexprvalue_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object CbaexprvalueOptimizedMacroCompiler extends MacroCompiler(CbaexprvalueOptimizedProgram(_)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
}

class CbaexprvalueWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with cbaexprvalue_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object CbaexprvalueWorstMacroCompiler extends MacroCompiler(CbaexprvalueWorstProgram(_)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
}

// Equal
class EqualOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with equal_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/equal/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object EqualOptimizedMacroCompiler extends MacroCompiler(EqualOptimizedProgram(_)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/equal/facts"
}

class EqualWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with equal_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/equal/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object EqualWorstMacroCompiler extends MacroCompiler(EqualWorstProgram(_)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/equal/facts"
}

// Fib
class FibOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with fib_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object FibOptimizedMacroCompiler extends MacroCompiler(FibOptimizedProgram(_)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
}

class FibWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with fib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object FibWorstMacroCompiler extends MacroCompiler(FibWorstProgram(_)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
}

// Prime
class PrimeOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with prime_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object PrimeOptimizedMacroCompiler extends MacroCompiler(PrimeOptimizedProgram(_)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
}

class PrimeWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with prime_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object PrimeWorstMacroCompiler extends MacroCompiler(PrimeWorstProgram(_)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
}

// Tastyslistlib
class TastyslistlibOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlib_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object TastyslistlibOptimizedMacroCompiler extends MacroCompiler(TastyslistlibOptimizedProgram(_)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
}

class TastyslistlibWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object TastyslistlibWorstMacroCompiler extends MacroCompiler(TastyslistlibWorstProgram(_)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
}

// Tastyslistlibinverse
class TastyslistlibinverseOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlibinverse_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object TastyslistlibinverseOptimizedMacroCompiler extends MacroCompiler(TastyslistlibinverseOptimizedProgram(_)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
}

class TastyslistlibinverseWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlibinverse_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object TastyslistlibinverseWorstMacroCompiler extends MacroCompiler(TastyslistlibinverseWorstProgram(_)) {
  inline def compile(): StorageManager => Any = ${this.compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
}

// TODO: when https://github.com/lampepfl/dotty/issues/18393 is fixed can move these into the test files

// Ackermann
class AckermannOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with ackermann_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object AckermannOptimizedMacroCompiler extends MacroCompiler(AckermannOptimizedProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}

class AckermannWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with ackermann_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object AckermannWorstMacroCompiler extends MacroCompiler(AckermannWorstProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
}

//CBAExprValue
class CbaexprvalueOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with cbaexprvalue_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object CbaexprvalueOptimizedMacroCompiler extends MacroCompiler(CbaexprvalueOptimizedProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
}

class CbaexprvalueWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with cbaexprvalue_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object CbaexprvalueWorstMacroCompiler extends MacroCompiler(CbaexprvalueWorstProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
}

// Equal
class EqualOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with equal_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/equal/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object EqualOptimizedMacroCompiler extends MacroCompiler(EqualOptimizedProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/equal/facts"
}

class EqualWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with equal_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/equal/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object EqualWorstMacroCompiler extends MacroCompiler(EqualWorstProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/equal/facts"
}

// Fib
class FibOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with fib_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object FibOptimizedMacroCompiler extends MacroCompiler(FibOptimizedProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
}

class FibWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with fib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object FibWorstMacroCompiler extends MacroCompiler(FibWorstProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/fib/facts"
}

// Prime
class PrimeOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with prime_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object PrimeOptimizedMacroCompiler extends MacroCompiler(PrimeOptimizedProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
}

class PrimeWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with prime_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object PrimeWorstMacroCompiler extends MacroCompiler(PrimeWorstProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"
}

// Tastyslistlib
class TastyslistlibOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlib_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object TastyslistlibOptimizedMacroCompiler extends MacroCompiler(TastyslistlibOptimizedProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
}

class TastyslistlibWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlib_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object TastyslistlibWorstMacroCompiler extends MacroCompiler(TastyslistlibWorstProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
}

// Tastyslistlibinverse
class TastyslistlibinverseOptimizedProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlibinverse_optimized {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object TastyslistlibinverseOptimizedMacroCompiler extends MacroCompiler(TastyslistlibinverseOptimizedProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
}

class TastyslistlibinverseWorstProgram(engine: ExecutionEngine) extends SolvableProgram(engine) with tastyslistlibinverse_worst {
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
  initializeEmptyFactsFromDir(factDir)
  pretest(this)
}
object TastyslistlibinverseWorstMacroCompiler extends MacroCompiler(TastyslistlibinverseWorstProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
  val factDir = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
}
