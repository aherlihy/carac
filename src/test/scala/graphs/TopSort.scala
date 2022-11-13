package graphs

import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.{ExecutionEngine, NaiveExecutionEngine}

import scala.collection.mutable

case class TopSort(program: Program) extends TestGraph {
  val description = "TopSort" // from Souffle tests
  val queries: mutable.Map[String, Query] = mutable.Map[String, Query]()

  private val edge = program.relation[Constant](" e")
  private val isBefore = program.relation[Constant]("before")
  private val isAfter = program.relation[Constant]("after")

  private val x, y, z = program.variable()

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
  isBefore(x, y) :- ( isBefore(x, z), isBefore(z, y) )

  // Vertex x is after vertex y if the graph has an edge from y to x.
  isAfter(x, y) :- edge(y, x)

  // Vertex x is after vertex y if some vertex z is after x and y is after z.
  isAfter(x, y) :- ( isAfter(z, x), isAfter(y, z) )

}
