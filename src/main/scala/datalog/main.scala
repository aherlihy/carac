package datalog

import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.dsl.Program
import datalog.storage.{CollectionsStorageManager, IndexedCollStorageManager, RelationalStorageManager}
import scala.util.Random

import scala.collection.mutable

def tc() = {
  println("naive+coll, before+after")
  given engine: ExecutionEngine = new NaiveExecutionEngine(new CollectionsStorageManager())
  val program = Program(engine)
  val edge = program.relation[String]("edge")
//  val isBefore = program.relation[String]("isBefore")
  val isAfter = program.relation[String]("isAfter")

  val x, y, z = program.variable()

  //  for i <- 0 until 10000 do
  //    e(
  //      Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString,
  //      Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString
  //    )  :- ()

  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()
//  edge("d", "e") :- ()

//  isBefore(x, y) :- edge(x, y)
//  isBefore(x, y) :- ( isBefore(x, z), isBefore(z, y) )

  isAfter(x, y) :- edge(y, x)
  isAfter(x, y) :- ( isAfter(z, x), isAfter(y, z) )


  println(
    isAfter.solve()
      .map(f => f.head.toString + "\t" + f.last.toString)
      .toList
      .sorted
      .mkString("", "\n", "")
  )
  // TODO: start here, step into isAfter
}

def soufex() = {
  given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new CollectionsStorageManager())
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
  given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new RelationalStorageManager())
  val program = Program(engine)
  val edge = program.relation[String](" edge")
  val isBefore = program.relation[String]("isBefore")
  val isAfter = program.relation[String]("isAfter")

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
//  isBefore(x, y) :- edge(x, y)

  // Vertex x is before vertex y if some vertex z is before x and z is before y.
//  isBefore(x, y) :- ( isBefore(x, z), edge(z, y) )

  // Vertex x is after vertex y if the graph has an edge from y to x.
  isAfter(x, y) :- edge(y, x)

  // Vertex x is after vertex y if some vertex z is after x and y is after z.
  isAfter(x, y) :- ( isAfter(z, x), isAfter(y, z) )

  println("solve=" + isAfter.solve().map(f => f.head.toString + "\t" + f.last.toString).mkString("", "\n", ""))

}

@main def main = {
  tc()
}
