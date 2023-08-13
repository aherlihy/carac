package test

import datalog.execution.{ExecutionEngine, MacroCompiler, SolvableProgram}
import datalog.dsl.*
import datalog.execution.ir.*
import datalog.storage.{StorageManager}
import datalog.{SimpleMacroCompiler, AckermannOptimizedMacroCompiler}

object MacroCompilerTest {
  val simpleCompiled = SimpleMacroCompiler.compile()
  val ackermannCompiled = AckermannOptimizedMacroCompiler.compile()
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
    println(res)
//    assertEquals(res, Set(Seq("b"), Seq("c"), Seq("d")))
  }
}
