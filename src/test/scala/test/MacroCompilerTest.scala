package test

import datalog.execution.{ExecutionEngine, SolvableProgram, MacroCompiler}
import datalog.dsl.*
import datalog.execution.ir.*
import datalog.storage.{DefaultStorageManager, StorageManager}

import datalog.SimpleMacroCompiler

object MacroCompilerTest {
  val simpleCompiled = SimpleMacroCompiler.compile()
}
import MacroCompilerTest.*

class MacroCompilerTest extends munit.FunSuite {
  test("can add facts at runtime to macro-compiled program") {
     val res = SimpleMacroCompiler.runCompiled(simpleCompiled) { program =>
       program.edge("b", "c") :- ()
     }
     assertEquals(res, Set(Seq("b"), Seq("c"), Seq("d")))
  }
}
