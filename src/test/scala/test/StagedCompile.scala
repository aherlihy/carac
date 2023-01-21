package test

import datalog.dsl.{Constant, Program}
import datalog.execution.SemiNaiveStagedExecutionEngine
import datalog.execution.ir.{IROp, InterpreterContext, ScanEDBOp}
import datalog.storage.{CollectionsStorageManager, StorageManager}
import datalog.tools.Debug.debug

import scala.quoted.{Expr, staging}

class StagedCompile extends munit.FunSuite {
  val storageManager = new CollectionsStorageManager()
  val engine = new SemiNaiveStagedExecutionEngine(storageManager)
  val program = new Program(engine)
  val edge = program.relation[Constant]("edge")
  val oneHop = program.relation[Constant]("oneHop")
  val twoHops = program.relation[Constant]("twoHops")
  val x, y, z = program.variable()
  oneHop(x, y) :- edge(x, y)
  twoHops(x, z) :- (edge(x, y), edge(y, z))

  edge("a", "a") :- ()
  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()

  def genContains(miniprog: IROp, check: String, toSolve: Int): Unit = {
    given irCtx: InterpreterContext = InterpreterContext(storageManager, engine.precedenceGraph, toSolve)
    debug("MINI PROG\n", () => storageManager.printer.printIR(miniprog))

    given staging.Compiler = staging.Compiler.make(getClass.getClassLoader)
    val compiled: StorageManager => storageManager.EDB =
      staging.run {
        val res: Expr[StorageManager => Any] =
          '{ (stagedSm: StorageManager) => ${engine.compileIR(miniprog)(using 'stagedSm)} }
        assert(res.show.contains(check), s"${res.show} is missing $check")
        res
      }.asInstanceOf[StorageManager => storageManager.EDB]

    val res = compiled(storageManager)
  }

  test("ScanEDBOp contains") {
    val toSolve = edge.id
    val miniprog = ScanEDBOp(toSolve)
    genContains(miniprog, s"stagedSm.edbs.apply($toSolve)", toSolve)
  }
  test("ScanEDBOp does not contain") {
    val toSolve = edge.id
    val miniprog = ScanEDBOp(-1)
    genContains(miniprog, s"EDB()", -1)
  }



}
