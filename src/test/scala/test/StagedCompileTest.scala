package test

import datalog.dsl.{Constant, Program, Term}
import datalog.execution.SemiNaiveStagedExecutionEngine
import datalog.execution.ast.ASTNode
import datalog.execution.ir.*
import datalog.storage.{CollectionsStorageManager, StorageManager}
import datalog.tools.Debug.debug

import scala.quoted.{Expr, staging}

class StagedCompileTest extends munit.FunSuite {
  val storageManager = new CollectionsStorageManager()
  val engine = new SemiNaiveStagedExecutionEngine(storageManager)
  val program = new Program(engine)
  val edge = program.relation[Constant]("edge")
  val idb = program.relation[Constant]("idb")
//  idb(x, z) :- (edge(x, y), edge(y, z))

  edge("a", "a") :- ()
  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()

  given irCtx: InterpreterContext = InterpreterContext(storageManager, engine.precedenceGraph, -1)
  storageManager.resetDerived(idb.id, irCtx.knownDbId, storageManager.EDB(Vector("KnownDerived")))
  storageManager.resetDelta(idb.id, irCtx.knownDbId, storageManager.EDB(Vector("KnownDelta")))
  storageManager.resetDerived(idb.id, irCtx.newDbId, storageManager.EDB(Vector("NewDerived")))
  storageManager.resetDelta(idb.id, irCtx.newDbId, storageManager.EDB(Vector("NewDelta")))

  // TODO: string compare prob too brittle but ok for dev
  def genContains(miniprog: IROp, checks: String*): Any = {
    debug("MINI PROG\n", () => storageManager.printer.printIR(miniprog))

    given staging.Compiler = staging.Compiler.make(getClass.getClassLoader)
    val compiled: CollectionsStorageManager => storageManager.EDB =
      staging.run {
        val res: Expr[CollectionsStorageManager => Any] =
          '{ (stagedSm: CollectionsStorageManager) => ${engine.compileIR[collection.mutable.ArrayBuffer[IndexedSeq[Term]]](miniprog)(using 'stagedSm)} }
        val strRes = res.show
        println(strRes)
        val order = checks.map(c =>
          val idx = strRes.indexOf(c)
          assert(idx != -1, s"$strRes is missing $c")
          idx
        )
        assert(order == order.sorted, "Checks did not happen in the right order")
        res
      }.asInstanceOf[CollectionsStorageManager => storageManager.EDB]

    val result = compiled(storageManager)
    println("RES=" + result)
    result
  }

  test("ScanEDBOp") {
    genContains(ScanEDBOp(edge.id), s"stagedSm.edbs.apply(${edge.id})")
    genContains(ScanEDBOp(-1), s"EDB()")
  }
  test("ScanOp") {
    genContains(ScanOp(idb.id, DB.Derived, KNOWLEDGE.Known), s".derivedDB.apply(${irCtx.knownDbId}).getOrElse[scala.Any](${idb.id}, stagedSm.EDB()")
    genContains(ScanOp(idb.id, DB.Delta, KNOWLEDGE.Known), s".deltaDB.apply(${irCtx.knownDbId}).getOrElse[scala.Any](${idb.id}")
    genContains(ScanOp(idb.id, DB.Derived, KNOWLEDGE.New), s".derivedDB.apply(${irCtx.newDbId}).getOrElse[scala.Any](${idb.id}")
    genContains(ScanOp(idb.id, DB.Delta, KNOWLEDGE.New), s".deltaDB.apply(${irCtx.newDbId}).getOrElse[scala.Any](${idb.id}")
    genContains(ScanOp(edge.id, DB.Derived, KNOWLEDGE.New), s", stagedSm.edbs.apply(${edge.id})")
  }
  test("SeqOp") {
    genContains(
      SequenceOp(Seq(
        ScanOp(idb.id, DB.Derived, KNOWLEDGE.Known),
        ScanOp(idb.id, DB.Delta, KNOWLEDGE.Known)
      )),
      "test"
    )
  }
  test("DiffOp") {
    genContains(
      DiffOp(
        ScanOp(idb.id, DB.Derived, KNOWLEDGE.Known),
        ScanOp(idb.id, DB.Delta, KNOWLEDGE.Known)
      ),
      "test"
    )
  }
  test("UnionOp") {
    genContains(
      UnionOp(Seq(
        ScanEDBOp(edge.id),
        ScanOp(idb.id, DB.Delta, KNOWLEDGE.Known)
      )),
      s"test"
    )
  }
  test("ClearOp") {
    genContains(ClearOp(), s".clearDB(true, ${irCtx.newDbId}")
  }
  test("DoWhileOp") {
    genContains(
      DoWhileOp(
        ScanOp(idb.id, DB.Derived, KNOWLEDGE.Known),
        DB.Derived
      )
    )
  }
  test("SwapOp") {
    val oldKnown = irCtx.knownDbId
    val oldNew = irCtx.newDbId
    genContains(
      SequenceOp(Seq(
        ScanOp(idb.id, DB.Derived, KNOWLEDGE.Known),
        SwapOp(),
        ScanOp(idb.id, DB.Derived, KNOWLEDGE.Known)
      )),
      s".derivedDB.apply($oldKnown)...; {.derivedDB.apply($oldNew)}; "
    )
  }
}
