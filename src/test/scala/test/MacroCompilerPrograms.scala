package test

import datalog.execution.{ExecutionEngine, SolvableProgram, MacroCompiler}
import datalog.dsl.*
import datalog.execution.ir.*
import datalog.storage.{DefaultStorageManager, StorageManager}

/// Used in MacroCompilerTest

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

  override val toSolve = pathFromA
}

object SimpleMacroCompiler extends MacroCompiler(SimpleProgram(_)) {
  inline def compile(): StorageManager => Any = ${compileImpl()}
}
