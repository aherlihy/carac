package test

import datalog.execution.{ExecutionEngine, MacroCompiler, SolvableProgram}
import datalog.dsl.*
import datalog.execution.ir.*
import datalog.storage.StorageManager
import datalog.{AckermannOptimizedMacroCompiler, SimpleMacroCompiler, TastyslistlibinverseOptimizedMacroCompiler}

object MacroCompilerTest {
  val simpleCompiled = SimpleMacroCompiler.compile()
  val ackermannCompiled = AckermannOptimizedMacroCompiler.compile()
  val tastyslistlibinverseOptimizedMacroCompiler = TastyslistlibinverseOptimizedMacroCompiler.compile()
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
  test("tastyslistlibinverse test") {
    val res = TastyslistlibinverseOptimizedMacroCompiler.runCompiled(tastyslistlibinverseOptimizedMacroCompiler) { program =>
      program.loadFromFactDir(program.factDir)
    }
    println(res)
  }
}
