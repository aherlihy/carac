package test

import datalog.dsl.{Constant, Program}
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
  def genContains(miniprog: IROp, check: String): Any = {
    debug("MINI PROG\n", () => storageManager.printer.printIR(miniprog))

    given staging.Compiler = staging.Compiler.make(getClass.getClassLoader)
    val compiled: StorageManager => storageManager.EDB =
      staging.run {
        val res: Expr[StorageManager => Any] =
          '{ (stagedSm: StorageManager) => ${engine.compileIR(miniprog)(using 'stagedSm)} }
        println(res.show)
        assert(res.show.contains(check), s"${res.show} is missing $check")
        res
      }.asInstanceOf[StorageManager => storageManager.EDB]

    compiled(storageManager)
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
//  test("UnionOp") {
//    genContains(
//      UnionOp(Seq(
//        ScanEDBOp(edge.id),
//        ScanOp(idb.id, DB.Delta, KNOWLEDGE.Known)
//      )),
//      s"test"
//    )
//  }
  test("ClearOp") {
    genContains(ClearOp(), s".clearDB(true, ${irCtx.newDbId}")
  }
//  test("CompareOp") {
//    genContains(CompareOp(DB.Derived), s".compareDerivedDBs(${irCtx.newDbId}, ${irCtx.knownDbId})")
//    genContains(CompareOp(DB.Delta), s".deltaDB(${irCtx.newDbId}).exists((k, v) => v.nonEmpty)")
//  }
//  test("DoWhileOp") {
//    genContains(
//      DoWhileOp(
//        ScanOp(idb.id, DB.Derived, KNOWLEDGE.Known),
//        CompareOp(irCtx.newDbId)
//      )
//    )
//  }
//  test("SwapOp") {
//    val oldKnown = irCtx.knownDbId
//    val oldNew = irCtx.newDbId
//    genContains(
//      SequenceOp(Seq(
//        ScanOp(idb.id, DB.Derived, KNOWLEDGE.Known),
//        SwapOp(),
//        ScanOp(idb.id, DB.Derived, KNOWLEDGE.Known)
//      )),
//      s".derivedDB.apply($oldKnown)...; {.derivedDB.apply($oldNew)}; "
//    )
//  }


}
