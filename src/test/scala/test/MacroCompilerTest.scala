package test

import datalog.execution.{ExecutionEngine, MacroCompiler, SolvableProgram}
import datalog.dsl.*
import datalog.execution.ir.*
import datalog.storage.StorageManager
import datalog.{AckermannOptimizedMacroCompiler, FibOptimizedMacroCompiler, PrimeOptimizedMacroCompiler, SimpleMacroCompiler, TastyslistlibinverseOptimizedMacroCompiler, TastyslistlibOptimizedMacroCompiler}

object MacroCompilerTest {
  val simpleCompiled = SimpleMacroCompiler.compile()
  val ackermannCompiled = AckermannOptimizedMacroCompiler.compile()
  val tastyslistlibinverseCompiled = TastyslistlibinverseOptimizedMacroCompiler.compile()
  val fibCompiled = FibOptimizedMacroCompiler.compile()
  val primeCompiled = PrimeOptimizedMacroCompiler.compile()
  val tastyslistlibCompiled = TastyslistlibOptimizedMacroCompiler.compile()
}
import MacroCompilerTest.*

class MacroCompilerTest extends munit.FunSuite {
  test("can add facts at runtime to macro-compiled program") {
     val res = SimpleMacroCompiler.runCompiled(simpleCompiled) { program =>
       program.edge("b", "c") :- ()
     }
     assertEquals(res, Set(Seq("b"), Seq("c"), Seq("d")))
  }
  test("ackermann test") {
    val res = AckermannOptimizedMacroCompiler.runCompiled(ackermannCompiled) { program =>
      program.loadFromFactDir(program.factDir)
    }
  }
  test("fib test") {
    val res = FibOptimizedMacroCompiler.runCompiled(fibCompiled) { program =>
      program.loadFromFactDir(program.factDir)
    }
  }
  test("prime test") {
    val res = PrimeOptimizedMacroCompiler.runCompiled(primeCompiled) { program =>
      program.loadFromFactDir(program.factDir)
    }
  }
  test("tastylistlib test") {
    val res = TastyslistlibOptimizedMacroCompiler.runCompiled(tastyslistlibCompiled) { program =>
      program.loadFromFactDir(program.factDir)
    }
  }
  test("tastyslistlibinverse test") {
    val res = TastyslistlibinverseOptimizedMacroCompiler.runCompiled(tastyslistlibinverseCompiled) { program =>
      program.loadFromFactDir(program.factDir)
    }
    println(res)
  }
}
