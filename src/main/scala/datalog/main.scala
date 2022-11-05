package datalog

import datalog.execution.{ExecutionEngine, ManuallyInlinedEE, ManuallyInlinedUnrolledEE, NaiveExecutionEngine, SemiNaiveExecutionEngine, StagedExecutionEngine}
import datalog.dsl.Program
import datalog.execution.old_manual_opt.ManuallyInlinedExternal
import datalog.storage.{CollectionsStorageManager, IndexedCollStorageManager, RelationalStorageManager}

import scala.collection.mutable

def tc() = {
  given engine: ExecutionEngine = new StagedExecutionEngine(new IndexedCollStorageManager())
  val program = Program(engine)
  val e = program.relation[String]("e")
  val p = program.relation[String]("p")
  val path2a = program.relation[String]("path2a")
  val path2a1 = program.relation[String]("path2a1")
  val edge2a = program.relation[String]("edge2a")

  val x, y, z = program.variable()

  // TODO: use context param to avoid needing :- ()?
  e("a", "b") :- ()
  e("b", "c") :- ()
  e("c", "d") :- ()
  p(x, y) :- e(x, y)
  p(x, z) :- ( e(x, y), p(y, z) )
  path2a(x) :- p("a", x)
  edge2a(x) :- e("a", x)
  //  a(x) :- b(x)
  //  b(y) :- c(y)
  //  c(x) :- a(x)
  //  a(x) :- other(x)

  println(p.solve())
}

def soufex() = {
  given engine: ExecutionEngine = new StagedExecutionEngine(new IndexedCollStorageManager())
  val program = Program(engine)
  val e = program.relation[Int]("e")
  val a = program.relation[Int]("a")
  val b = program.relation[Int]("b")
  val c = program.relation[Int]("c")
  val d = program.relation[Int]("d")

  val x = program.variable()

  a(x) :- (b(x), c(x))
  b(1) :- ()
  b(x) :- (c(x), d(x))
  c(2) :- ()
  c(x) :- (b(x), d(x))
  d(3) :- ()
  e(4) :- ()
  println("solve=" + e.solve())
}

@main def main = {
  tc()
}
