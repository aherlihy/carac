package test

import datalog.dsl.{Constant, Program, Term}
import datalog.execution.{JoinIndexes, StagedExecutionEngine, StagedCompiler}
import datalog.execution.ast.ASTNode
import datalog.execution.ir.*
import datalog.storage.{CollectionsStorageManager, DB, KNOWLEDGE, StorageManager}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.quoted.{Expr, Quotes, staging}
import scala.util.matching.Regex

/**
 * TODO: replace regex with reflection
 */
class StagedCompileTest extends munit.FunSuite {
  val storageManager = new CollectionsStorageManager()
  val engine = new StagedExecutionEngine(storageManager)
  val program = new Program(engine)
  val edge = program.relation[Constant]("edge")
  val edb = program.relation[Constant]("edb")
  val idb = program.relation[Constant]("idb")
  edb("1") :- ()
  val x, y, z = program.variable()
  edb(x, z) :- (edge(x, y), edge(y, z))

  edge("a", "a") :- ()
  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()
  edge("a", "1") :- ()

  engine.precedenceGraph.addNode(edb.id, Seq(edge.id)) // tree transforms usually populate precedenceGraph
  val ctx: InterpreterContext = new InterpreterContext(storageManager, engine.precedenceGraph, edb.id)
  given irCtx: InterpreterContext = ctx
  storageManager.edbs.foreach((k, relation) => { // necessary so that state is as if it was solved already
    storageManager.derivedDB(storageManager.knownDbId)(k) = storageManager.EDB()
    storageManager.derivedDB(storageManager.newDbId)(k) = storageManager.EDB()
  })
  val sVar = "stagedSm"
  val any = "[\\s\\S]*?"
  val anyCapture = "([\\s\\S]*?)"

  // TODO: string compare prob too brittle but ok for dev
  def compileCheck(miniprog: IROp[Any], check: (String => String)*): CompiledFn = {
    given staging.Compiler = staging.Compiler.make(getClass.getClassLoader)
    staging.run {
      val res: Expr[CompiledFn] =
        '{ (stagedSm: CollectionsStorageManager) => ${ engine.compiler.compileIR(miniprog)(using 'stagedSm) } }
      debug("generated code: ", () => res.show)
      val strRes = res.show
      check.foldLeft(strRes)((generatedString, op) =>
        op(generatedString)
      )
      res
    }
  }

  def compileCheckRel(miniprog: IROp[CollectionsStorageManager#EDB], check: (String => String)*): CompiledRelFn = {
    given staging.Compiler = staging.Compiler.make(getClass.getClassLoader)

    staging.run {
      val res: Expr[CompiledRelFn] =
        '{ (stagedSm: CollectionsStorageManager) => ${ engine.compiler.compileIRRelOp(miniprog)(using 'stagedSm) } }
      debug("generated code: ", () => res.show)
      val strRes = res.show
      check.foldLeft(strRes)((generatedString, op) =>
        op(generatedString)
      )
      res
    }
  }
  def generalMatch(test: Regex)(generatedString: String): String =
    generatedString match {
      case test(rest, _*) =>
        rest
      case _ =>
        assert(false, s"generated code '$generatedString' missing expr '$test")
        generatedString
    }

  def whileMatch(cond: String)(generatedString: String): String =
    val whileR = s"$any while \\(\\{$anyCapture\\}\\) \\(\\)$any".r
    generatedString match {
      case whileR(body) =>
        assert(body.trim().endsWith(cond), s"generated code '${body.trim()}' missing cond $cond")
        body
      case _ =>
        assert(false, s"generated code '$generatedString' missing while statement")
        generatedString
    }

  def deepClone(toClone: mutable.Map[Int, storageManager.FactDatabase]): mutable.Map[Int, storageManager.FactDatabase] =
    val derivedClone = mutable.Map[Int, storageManager.FactDatabase]()
    toClone.foreach((k, factdb) => {
      derivedClone(k) = storageManager.FactDatabase()
      factdb.foreach((edbId, edb) =>
        derivedClone(k)(edbId) = storageManager.EDB()
        edb.foreach(e => derivedClone(k)(edbId).append(e))
      )
    })
    derivedClone

  test("DoWhile Derived") {
    val derived = deepClone(storageManager.derivedDB)
    val delta = deepClone(storageManager.deltaDB)
    storageManager.resetNewDerived(edb.id, storageManager.EDB(Vector("1"), Vector("1"), Vector("1")))
    assertEquals(storageManager.getKnownDerivedDB(edb.id), ArrayBuffer())

    val toRun = compileCheck(
      DoWhileOp(
        DB.Derived,
        InsertOp(edb.id, DB.Derived, KNOWLEDGE.Known, ScanOp(edb.id, DB.Derived, KNOWLEDGE.Known).asInstanceOf[IROp[Any]], ScanEDBOp(edb.id).asInstanceOf[IROp[Any]]), // empty => "1"
      ),
      whileMatch(s"$sVar.compareDerivedDBs().unary_!")
    )
    toRun(storageManager)
    assertEquals(storageManager.getKnownDerivedDB(edb.id), ArrayBuffer(Vector("1"), Vector("1"), Vector("1")))

    storageManager.derivedDB.clear()
    storageManager.deltaDB.clear()
    derived.foreach((k, v) => storageManager.derivedDB(k) = v)
    delta.foreach((k, v) => storageManager.deltaDB(k) = v)
  }

  test("DoWhile Delta") {
    val derived = deepClone(storageManager.derivedDB)
    val delta = deepClone(storageManager.deltaDB)
    storageManager.resetNewDelta(edb.id, storageManager.EDB(Vector("1"), Vector("1"), Vector("1")))
    assertEquals(storageManager.getNewDeltaDB(edb.id), ArrayBuffer(Vector("1"), Vector("1"), Vector("1")))

    val toRun = compileCheck(
      DoWhileOp(
        DB.Delta,
        InsertOp(edb.id, DB.Delta, KNOWLEDGE.New, ScanEDBOp(idb.id).asInstanceOf[IROp[Any]]) // empty => "1"
      ),
      whileMatch(s"$sVar.compareNewDeltaDBs()")
    )
    toRun(storageManager)
    assertEquals(storageManager.getNewDeltaDB(edb.id), ArrayBuffer())

    storageManager.derivedDB.clear()
    storageManager.deltaDB.clear()
    derived.foreach((k, v) => storageManager.derivedDB(k) = v)
    delta.foreach((k, v) => storageManager.deltaDB(k) = v)
  }

  test("SwapAndClearOp") {
    val derived = deepClone(storageManager.derivedDB)
    val delta = deepClone(storageManager.deltaDB)

    storageManager.resetNewDerived(idb.id, storageManager.EDB(Vector("NewDerived")))
    storageManager.resetNewDelta(idb.id, storageManager.EDB(Vector("NewDelta")))
    storageManager.resetKnownDerived(idb.id, storageManager.EDB(Vector("KnownDerived")))
    storageManager.resetKnownDelta(idb.id, storageManager.EDB(Vector("KnownDelta")))

    val oldKnown = storageManager.knownDbId
    val oldNew = storageManager.newDbId
    val toRun = compileCheck(
      SequenceOp(OpCode.SEQ,
        ScanOp(idb.id, DB.Derived, KNOWLEDGE.Known).asInstanceOf[IROp[Any]],
        SwapAndClearOp(),
        ScanOp(idb.id, DB.Derived, KNOWLEDGE.Known).asInstanceOf[IROp[Any]]
      ),
      generalMatch(s"$any$sVar.getKnownDerivedDB\\(${idb.id}$anyCapture".r),
      generalMatch(s"$any$sVar.swapKnowledge\\(\\)$anyCapture".r),
      generalMatch(s"$any$sVar.clearNewDerived\\(\\)$anyCapture".r),
      generalMatch(s"$any$sVar.getKnownDerivedDB\\(${idb.id}$anyCapture".r)
    )
    toRun(storageManager)
    assertNotEquals(oldNew, storageManager.newDbId)
    assertNotEquals(oldKnown, storageManager.knownDbId)
    assertEquals(storageManager.getKnownDerivedDB(idb.id), ArrayBuffer(Vector("NewDerived")))
    assertEquals(storageManager.getKnownDeltaDB(idb.id), ArrayBuffer(Vector("NewDelta")))
    assertEquals(storageManager.getNewDerivedDB(idb.id), ArrayBuffer.empty)
    assertEquals(storageManager.getNewDeltaDB(idb.id), ArrayBuffer(Vector("KnownDelta")))

    storageManager.derivedDB.clear()
    storageManager.deltaDB.clear()
    derived.foreach((k, v) => storageManager.derivedDB(k) = v)
    delta.foreach((k, v) => storageManager.deltaDB(k) = v)
  }

  test("SeqOp") {
    val derived = deepClone(storageManager.derivedDB)
    val delta = deepClone(storageManager.deltaDB)
    val toRun = compileCheck(
      SequenceOp(OpCode.SEQ,
        InsertOp(idb.id, DB.Derived, KNOWLEDGE.New, ScanEDBOp(edb.id).asInstanceOf[IROp[Any]]),
        InsertOp(idb.id, DB.Delta, KNOWLEDGE.New, ScanEDBOp(edb.id).asInstanceOf[IROp[Any]]),
      ),
      generalMatch(s"$any$sVar.resetNewDerived\\(${idb.id}, $sVar.edbs.apply\\(${edb.id}\\), $sVar.EDB\\(\\)\\)$anyCapture".r),
      generalMatch(s"$any$sVar.resetNewDelta\\(${idb.id}, $sVar.edbs.apply\\(${edb.id}\\)\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getNewDerivedDB(idb.id), ArrayBuffer(Vector("1")))
    assertEquals(storageManager.getNewDeltaDB(idb.id), ArrayBuffer(Vector("1")))

    storageManager.derivedDB.clear()
    storageManager.deltaDB.clear()
    derived.foreach((k, v) => storageManager.derivedDB(k) = v)
    delta.foreach((k, v) => storageManager.deltaDB(k) = v)
  }

  test("ScanEDBOp") {
    val toRun = compileCheckRel(
      ScanEDBOp(edge.id),
      generalMatch(s"$any$sVar.edbs.apply\\(${edge.id}\\)$anyCapture".r)
    )
    assertEquals(toRun(storageManager), storageManager.edbs(edge.id))
    val toRun2 = compileCheckRel(
      ScanEDBOp(-1),
      generalMatch(s"$any$sVar.EDB\\(\\)$anyCapture".r)
    )
    assertEquals(toRun2(storageManager), storageManager.EDB())
  }

  test("ScanOp") {
    val derived = deepClone(storageManager.derivedDB)
    val delta = deepClone(storageManager.deltaDB)

    storageManager.resetKnownDerived(idb.id, storageManager.EDB(Vector("KnownDerived")))
    storageManager.resetKnownDelta(idb.id,  storageManager.EDB(Vector("KnownDelta")))
    storageManager.resetNewDerived(idb.id, storageManager.EDB(Vector("NewDerived")))
    storageManager.resetNewDelta(idb.id, storageManager.EDB(Vector("NewDelta")))

    var toRun = compileCheckRel(
      ScanOp(idb.id, DB.Derived, KNOWLEDGE.Known),
      generalMatch(s"$any$sVar.getKnownDerivedDB\\(${idb.id}$anyCapture".r)
    )
    assertEquals(toRun(storageManager), ArrayBuffer(Vector("KnownDerived")))
    toRun = compileCheckRel(
      ScanOp(idb.id, DB.Delta, KNOWLEDGE.Known),
      generalMatch(s"$any$sVar.getKnownDeltaDB\\(${idb.id}$anyCapture".r)
    )
    assertEquals(toRun(storageManager), ArrayBuffer(Vector("KnownDelta")))
    toRun = compileCheckRel(
      ScanOp(idb.id, DB.Derived, KNOWLEDGE.New),
      generalMatch(s"$any$sVar.getNewDerivedDB\\(${idb.id}$anyCapture".r)
    )
    assertEquals(toRun(storageManager), ArrayBuffer(Vector("NewDerived")))
    toRun = compileCheckRel(
      ScanOp(idb.id, DB.Delta, KNOWLEDGE.New),
      generalMatch(s"$any$sVar.getNewDeltaDB\\(${idb.id}$anyCapture".r)
    )
    assertEquals(toRun(storageManager), ArrayBuffer(Vector("NewDelta")))

    storageManager.derivedDB.clear()
    storageManager.deltaDB.clear()
    derived.foreach((k, v) => storageManager.derivedDB(k) = v)
    delta.foreach((k, v) => storageManager.deltaDB(k) = v)
  }

  test("JoinProjectSelectOp") {
    val scanEdge = s"$sVar.edbs.apply\\(${edge.id}\\)"
    val toRun = compileCheckRel(
      SelectProjectJoinOp(
        JoinIndexes(
          Seq(Seq(1, 2)), Map[Int, Constant](0 -> "b"), Seq(("v", 0),("v", 1), ("v", 2), ("v", 3)), Seq.empty, Seq.empty
        ),
        ScanEDBOp(edge.id), ScanEDBOp(edge.id),
      ),
      generalMatch(s"$any$sVar.joinProjectHelper\\($scanEdge, $scanEdge, $anyCapture".r)
    )

    assertEquals(toRun(storageManager), ArrayBuffer(Vector("b", "c", "c", "d")))
  }

  test("InsertOp Delta") {
    val derived = deepClone(storageManager.derivedDB)
    val delta = deepClone(storageManager.deltaDB)

    // insert into known, delta from edb
    assertEquals(storageManager.getKnownDeltaDB(edb.id), ArrayBuffer())
    val scanEdb = s"$sVar.edbs.apply\\(${edb.id}\\)"
    var toRun = compileCheck(
      InsertOp(edb.id, DB.Delta, KNOWLEDGE.Known, ScanEDBOp(edb.id).asInstanceOf[IROp[Any]]),
      generalMatch(s"$any$sVar.resetKnownDelta\\(${edb.id}, $scanEdb\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getKnownDeltaDB(edb.id), ArrayBuffer(Vector("1")))

    // insert again to show that it fully resets
    toRun = compileCheck(
      InsertOp(edb.id, DB.Delta, KNOWLEDGE.Known, ScanEDBOp(edb.id).asInstanceOf[IROp[Any]]),
      generalMatch(s"$any$sVar.resetKnownDelta\\(${edb.id}, $scanEdb\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getKnownDeltaDB(edb.id), ArrayBuffer(Vector("1")))

    // insert into new, delta from edb
    assertEquals(storageManager.getNewDeltaDB(edb.id), ArrayBuffer())
    toRun = compileCheck(
      InsertOp(edb.id, DB.Delta, KNOWLEDGE.New, ScanEDBOp(edb.id).asInstanceOf[IROp[Any]]), // insert into new, delta from edb
      generalMatch(s"$any$sVar.resetNewDelta\\(${edb.id}, $scanEdb\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getNewDeltaDB(edb.id), ArrayBuffer(Vector("1")))

    // insert again to show that it fully resets
    toRun = compileCheck(
      InsertOp(edb.id, DB.Delta, KNOWLEDGE.New, ScanEDBOp(edb.id).asInstanceOf[IROp[Any]]), // insert into new, delta from edb
      generalMatch(s"$any$sVar.resetNewDelta\\(${edb.id}, $scanEdb\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getNewDeltaDB(edb.id), ArrayBuffer(Vector("1")))

    storageManager.derivedDB.clear()
    storageManager.deltaDB.clear()
    derived.foreach((k, v) => storageManager.derivedDB(k) = v)
    delta.foreach((k, v) => storageManager.deltaDB(k) = v)
  }

  test("InsertOp Derived") {
    val derived = deepClone(storageManager.derivedDB)
    val delta = deepClone(storageManager.deltaDB)

    assertEquals(storageManager.getKnownDerivedDB(edb.id), ArrayBuffer())
    // insert into known, derived from edb
    val scanEdb = s"$sVar.edbs.apply\\(${edb.id}\\)"
    val edbS = s"$sVar.EDB\\(\\)"
    var toRun = compileCheck(
      InsertOp(edb.id, DB.Derived, KNOWLEDGE.Known, ScanEDBOp(edb.id).asInstanceOf[IROp[Any]]),
      generalMatch(s"$any$sVar.resetKnownDerived\\(${edb.id}, $scanEdb, $edbS\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getKnownDerivedDB(edb.id), ArrayBuffer(Vector("1")))

    // insert again to show that it fully resets
    toRun = compileCheck(
      InsertOp(edb.id, DB.Derived, KNOWLEDGE.Known, ScanEDBOp(edb.id).asInstanceOf[IROp[Any]]),
      generalMatch(s"$any$sVar.resetKnownDerived\\(${edb.id}, $scanEdb, $edbS\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getKnownDerivedDB(edb.id), ArrayBuffer(Vector("1")))

    // insert into new, derived from edb
    assertEquals(storageManager.getNewDerivedDB(edb.id), ArrayBuffer())
    toRun = compileCheck(
      InsertOp(edb.id, DB.Derived, KNOWLEDGE.New, ScanEDBOp(edb.id).asInstanceOf[IROp[Any]]), // insert into new, derived from edb
      generalMatch(s"$any$sVar.resetNewDerived\\(${edb.id}, $scanEdb, $edbS\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getNewDerivedDB(edb.id), ArrayBuffer(Vector("1")))

    // insert again to show that it fully resets
    toRun = compileCheck(
      InsertOp(edb.id, DB.Derived, KNOWLEDGE.New, ScanEDBOp(edb.id).asInstanceOf[IROp[Any]]), // insert into new, derived from edb
      generalMatch(s"$any$sVar.resetNewDerived\\(${edb.id}, $scanEdb, $edbS\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getNewDerivedDB(edb.id), ArrayBuffer(Vector("1")))

    storageManager.derivedDB.clear()
    storageManager.deltaDB.clear()
    derived.foreach((k, v) => storageManager.derivedDB(k) = v)
    delta.foreach((k, v) => storageManager.deltaDB(k) = v)
  }

  test("InsertOp Derived Append") {
    val derived = deepClone(storageManager.derivedDB)
    val delta = deepClone(storageManager.deltaDB)

    assertEquals(storageManager.getKnownDerivedDB(edb.id), ArrayBuffer())

    // insert into known, derived from edb
    val scanEdb = s"$sVar.edbs.apply\\(${edb.id}\\)"
    val emptyEDB = s"$sVar.EDB\\(\\)"
    val scanEdbDerived = s"$sVar.getKnownDerivedDB\\(${edb.id}".r
    var toRun = compileCheck(
      InsertOp(edb.id, DB.Derived, KNOWLEDGE.Known, ScanEDBOp(edb.id).asInstanceOf[IROp[Any]]),
      generalMatch(s"$any$sVar.resetKnownDerived\\(${edb.id}, $scanEdb, $emptyEDB\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getKnownDerivedDB(edb.id), ArrayBuffer(Vector("1")))

    // insert again to show that it appends
    toRun = compileCheck(
      InsertOp(edb.id, DB.Derived, KNOWLEDGE.Known, ScanEDBOp(edb.id).asInstanceOf[IROp[Any]], ScanOp(edb.id, DB.Derived, KNOWLEDGE.Known).asInstanceOf[IROp[Any]]),
      generalMatch(s"$any$sVar.resetKnownDerived\\(${edb.id}, $scanEdb, $scanEdbDerived\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getKnownDerivedDB(edb.id), ArrayBuffer(Vector("1"), Vector("1")))

    // insert with self to double
    toRun = compileCheck(
      InsertOp(edb.id, DB.Derived, KNOWLEDGE.Known, ScanOp(edb.id, DB.Derived, KNOWLEDGE.Known).asInstanceOf[IROp[Any]], ScanOp(edb.id, DB.Derived, KNOWLEDGE.Known).asInstanceOf[IROp[Any]]),
      generalMatch(s"$any$sVar.resetKnownDerived\\(${edb.id}, $scanEdbDerived\\), $scanEdbDerived\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getKnownDerivedDB(edb.id), ArrayBuffer(Vector("1"), Vector("1"), Vector("1"), Vector("1")))

    // insert with empty to show it rewrites
    toRun = compileCheck(
      InsertOp(edb.id, DB.Derived, KNOWLEDGE.Known, ScanEDBOp(idb.id).asInstanceOf[IROp[Any]]),
      generalMatch(s"$any$sVar.resetKnownDerived\\(${edb.id}, $emptyEDB, $emptyEDB\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getKnownDerivedDB(edb.id), ArrayBuffer())

    // NEW
    val scanEdbDerivedNew = s"$sVar.getNewDerivedDB\\(${edb.id}$anyCapture".r
    toRun = compileCheck(
      InsertOp(edb.id, DB.Derived, KNOWLEDGE.New, ScanEDBOp(edb.id).asInstanceOf[IROp[Any]]),
      generalMatch(s"$any$sVar.resetNewDerived\\(${edb.id}, $scanEdb, $emptyEDB\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getNewDerivedDB(edb.id), ArrayBuffer(Vector("1")))

    // insert again to show that it appends
    toRun = compileCheck(
      InsertOp(edb.id, DB.Derived, KNOWLEDGE.New, ScanEDBOp(edb.id).asInstanceOf[IROp[Any]], ScanOp(edb.id, DB.Derived, KNOWLEDGE.New).asInstanceOf[IROp[Any]]),
      generalMatch(s"$any$sVar.resetNewDerived\\(${edb.id}, $scanEdb, $scanEdbDerivedNew$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getNewDerivedDB(edb.id), ArrayBuffer(Vector("1"), Vector("1")))

    // insert with self to double
    toRun = compileCheck(
      InsertOp(edb.id, DB.Derived, KNOWLEDGE.New, ScanOp(edb.id, DB.Derived, KNOWLEDGE.New).asInstanceOf[IROp[Any]], ScanOp(edb.id, DB.Derived, KNOWLEDGE.New).asInstanceOf[IROp[Any]]),
      generalMatch(s"$any$sVar.resetNewDerived\\(${edb.id}, $scanEdbDerivedNew\\), $scanEdbDerivedNew\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getNewDerivedDB(edb.id), ArrayBuffer(Vector("1"), Vector("1"), Vector("1"), Vector("1")))

    // insert with empty to show it rewrites
    toRun = compileCheck(
      InsertOp(edb.id, DB.Derived, KNOWLEDGE.New, ScanEDBOp(idb.id).asInstanceOf[IROp[Any]]),
      generalMatch(s"$any$sVar.resetNewDerived\\(${edb.id}, $emptyEDB, $emptyEDB\\)$anyCapture".r)
    )
    toRun(storageManager)
    assertEquals(storageManager.getNewDerivedDB(edb.id), ArrayBuffer())


    storageManager.derivedDB.clear()
    storageManager.deltaDB.clear()
    derived.foreach((k, v) => storageManager.derivedDB(k) = v)
    delta.foreach((k, v) => storageManager.deltaDB(k) = v)
  }

  test("UnionOp") {
    val scanEdge = s"$sVar.edbs.apply\\(${edge.id}\\)"
    val scanEdb = s"$sVar.edbs.apply\\(${edb.id}\\)"
    val toRun = compileCheckRel(
      UnionOp(OpCode.UNION,
        ScanEDBOp(edge.id),
        ScanEDBOp(edb.id),
        ScanEDBOp(edb.id)
      ),
      generalMatch(s"$any$sVar.union\\($scanEdge, $scanEdb$anyCapture".r)
    )
    assertEquals(toRun(storageManager), ArrayBuffer(Vector("a", "a"), Vector("a", "b"), Vector("b", "c"), Vector("c", "d"), Vector("a", "1"), Vector("1")))
  }

  test("DiffOp") {
    val scanEdge = s"$sVar.edbs.apply\\(${edge.id}\\)"
    val scanEdb = s"$sVar.edbs.apply\\(${edb.id}\\)"
    val toRun = compileCheckRel(
      DiffOp(
        ScanEDBOp(edge.id),
        ScanEDBOp(edb.id)
      ),
      generalMatch(s"$any$sVar.diff\\($scanEdge, $scanEdb\\)$anyCapture".r)
    )
    assertEquals(toRun(storageManager), ArrayBuffer(Vector("a", "a"), Vector("a", "b"), Vector("b", "c"), Vector("c", "d"), Vector("a", "1")))
  }
}
