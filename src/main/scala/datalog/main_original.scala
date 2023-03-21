package datalog

import datalog.dsl.{Constant, Program, __}
import datalog.execution.ast.transform.CopyEliminationPass
import datalog.execution.ir.InterpreterContext
import datalog.execution.*
import datalog.storage.{DefaultStorageManager, NS, VolcanoStorageManager}

import scala.collection.mutable
import scala.quoted.*
import scala.util.Random

@main def main2 = {
  //  val engine = new SemiNaiveStagedExecutionEngine(new DefaultStorageManager())
  //  val program = Program(engine)
  //  println("staged")
  //  run(program)
  //  reversible(program, engine)
  //  val run = multiJoin
//  println("OLD N")
//  given engine0: ExecutionEngine = new NaiveExecutionEngine(new DefaultStorageManager())
//  val program0 = Program(engine0)
//  func(program0)
//  println("\n\n_______________________\n\n")

  val sort = 0
  println(s"OLD SN: $sort")

  given engine1a: ExecutionEngine = new SemiNaiveExecutionEngine(new DefaultStorageManager())

  val program1a = Program(engine1a)
  func(program1a)
  println("\n\n_______________________\n\n")

  //  val jo = JITOptions(ir.OpCode.OTHER, aot = true, block = true)
//  println("COMPILED")
//  given engine3: ExecutionEngine = new StagedExecutionEngine(new DefaultStorageManager(), jo)
//  val program3 = Program(engine3)
//  acyclic(program3)
//  println("\n\n_______________________\n\n")

//  println("JIT Snippet")
//  val engine4: ExecutionEngine = new StagedSnippetExecutionEngine(new DefaultStorageManager(), jo)
//  val program4 = Program(engine4)
//  tc(program4)
//  println("\n\n_______________________\n\n")

//  println("JIT STAGED: aot EvalSN")
//  val engine5: ExecutionEngine = new JITStagedExecutionEngine(new DefaultStorageManager(), ir.OpCode.EVAL_SN, true, true)
//  val program5 = Program(engine5)
//  manyRelations(program5)
//  println("\n\n_______________________\n\n")
  //  println("JIT STAGED")
//
//  given engine3: ExecutionEngine = new CompiledStagedExecutionEngine(new DefaultStorageManager())//, ir.OpCode.LOOP_BODY, false, false)
//  val program3 = Program(engine3)
//  tc(program3)
//  println("\n\n_______________________\n\n")
}
