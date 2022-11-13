package datalog

import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.dsl.Program
import datalog.storage.{CollectionsStorageManager, IndexedCollStorageManager, RelationalStorageManager}

import scala.collection.mutable

def tc() = {
  given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new CollectionsStorageManager())
  val program = Program(engine)
  val e = program.relation[String]("e")
  val p = program.relation[String]("tc")
  val path2a = program.relation[String]("path2a")
//  val edge2a = program.relation[String]("edge2a")

  val x, y, z = program.variable()

  // TODO: use context param to avoid needing :- ()?
  e("a", "b") :- ()
  e("b", "c") :- ()
  e("c", "d") :- ()
  p(x, y) :- e(x, y)
  p(x, z) :- ( p(x, y), p(y, z) )
//  path2a(x) :- p("a", x)
//  edge2a(x) :- e("a", x)
  //  a(x) :- b(x)
  //  b(y) :- c(y)
  //  c(x) :- a(x)
  //  a(x) :- other(x)

  println(p.solve())
}

def soufex() = {
  given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new IndexedCollStorageManager())
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

def topsortSouff() = {
  given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new IndexedCollStorageManager())
  val program = Program(engine)
  val edge = program.relation[String](" e")
  val isBefore = program.relation[String]("isBefore")
  val isAfter = program.relation[String]("isAfter")
  val allBeforeA = program.relation[String]("allBA")

  val x, y, z = program.variable()

  edge("A",	"B") :- ()
  edge("A",	"D") :- ()
  edge("A",	"E") :- ()
  edge("B",	"C") :- ()
  edge("C",	"D") :- ()
  edge("C",	"E") :- ()
  edge("D",	"E") :- ()
  edge("E",	"F") :- ()
  edge("F",	"G") :- ()
  edge("F",	"H") :- ()
  edge("F",	"I") :- ()
  edge("G",	"J") :- ()
  edge("H",	"K") :- ()
  edge("I",	"L") :- ()
  edge("J",	"M") :- ()
  edge("K",	"M") :- ()
  edge("L",	"M") :- ()

  // Vertex x is before vertex y if the graph has an edge from x to y.
  isBefore(x, y) :- edge(x, y)

  // Vertex x is before vertex y if some vertex z is before x and z is before y.
  isBefore(x, y) :- ( isBefore(x, z), edge(z, y) )

  // Vertex x is after vertex y if the graph has an edge from y to x.
  isAfter(x, y) :- edge(y, x)

  // Vertex x is after vertex y if some vertex z is after x and y is after z.
  isAfter(x, y) :- ( isAfter(z, x), isAfter(y, z) )

  allBeforeA(x) :- isBefore(x, "M")

  println("isBeforeM=" + allBeforeA.solve())

}

@main def main = {
  tc()
}
