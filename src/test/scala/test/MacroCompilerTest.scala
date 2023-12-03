package test

import datalog.execution.{ExecutionEngine, MacroCompiler, SolvableProgram}
import datalog.dsl.*
import datalog.execution.ir.*
import datalog.storage.StorageManager
import datalog.{AckermannWorstMacroCompiler, FibWorstMacroCompiler, PrimeWorstMacroCompiler, SimpleMacroCompiler, TastyslistlibinverseWorstMacroCompiler, TastyslistlibWorstMacroCompiler}

object MacroCompilerTest {
  val simpleCompiled = SimpleMacroCompiler.compile()
  val ackermannCompiled = AckermannWorstMacroCompiler.compile()
  val tastyslistlibinverseCompiled = TastyslistlibinverseWorstMacroCompiler.compile()
  val fibCompiled = FibWorstMacroCompiler.compile()
  val primeCompiled = PrimeWorstMacroCompiler.compile()
  val tastyslistlibCompiled = TastyslistlibWorstMacroCompiler.compile()
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
    val res = AckermannWorstMacroCompiler.runCompiled(ackermannCompiled) { program =>
      program.loadFromFactDir(program.factDir)
    }
  }
  test("fib test") {
    val res = FibWorstMacroCompiler.runCompiled(fibCompiled) { program =>
      program.loadFromFactDir(program.factDir)
    }
  }
  test("prime test") {
    val res = PrimeWorstMacroCompiler.runCompiled(primeCompiled) { program =>
      program.loadFromFactDir(program.factDir)
    }
  }
  test("tastylistlib test") {
    val res = TastyslistlibWorstMacroCompiler.runCompiled(tastyslistlibCompiled) { program =>
      program.loadFromFactDir(program.factDir)
    }
  }
  test("tastyslistlibinverse test") {
    val res = TastyslistlibinverseWorstMacroCompiler.runCompiled(tastyslistlibinverseCompiled) { program =>
      program.loadFromFactDir(program.factDir)
    }
    println(res)
  }
}
