package test.graphs

import carac.dsl.{Constant, Program, Relation, Term}
import carac.execution.{ExecutionEngine, NaiveShallowExecutionEngine}

import scala.collection.mutable

case class TopSort(program: Program) extends TestGraph {
  val description = "TopSort"
  val queries: mutable.Map[String, Query] = mutable.Map[String, Query]()

  private val edge = program.relation[Constant]("edge")
  private val isBefore = program.relation[Constant]("isBefore")
  private val isAfter = program.relation[Constant]("isAfter")

  private val x, y, z = program.variable()

  edge("A", "B") :- ()
  edge("A", "D") :- ()
  edge("A", "E") :- ()
  edge("B", "C") :- ()
  edge("C", "D") :- ()
  edge("C", "E") :- ()
  edge("D", "E") :- ()
  edge("E", "F") :- ()
  edge("F", "G") :- ()
  edge("F", "H") :- ()
  edge("F", "I") :- ()
  edge("G", "J") :- ()
  edge("H", "K") :- ()
  edge("I", "L") :- ()
  edge("J", "M") :- ()
  edge("K", "M") :- ()
  edge("L", "M") :- ()

  isBefore(x, y) :- edge(x, y)
  isBefore(x, y) :- (isBefore(x, z), isBefore(z, y))

  isAfter(x, y) :- edge(y, x)
  isAfter(x, y) :- (isAfter(z, x), isAfter(y, z))

  queries(isBefore.name) = Query("isBefore", isBefore, Set(
    Vector("A", "B"), Vector("A", "C"), Vector("A", "D"), Vector("A", "E"), Vector("A", "F"), Vector("A", "G"), Vector("A", "H"),
    Vector("A", "I"), Vector("A", "J"), Vector("A", "K"), Vector("A", "L"), Vector("A", "M"), Vector("B", "C"), Vector("B", "D"),
    Vector("B", "E"), Vector("B", "F"), Vector("B", "G"), Vector("B", "H"), Vector("B", "I"), Vector("B", "J"), Vector("B", "K"),
    Vector("B", "L"), Vector("B", "M"), Vector("C", "D"), Vector("C", "E"), Vector("C", "F"), Vector("C", "G"), Vector("C", "H"),
    Vector("C", "I"), Vector("C", "J"), Vector("C", "K"), Vector("C", "L"), Vector("C", "M"), Vector("D", "E"), Vector("D", "F"),
    Vector("D", "G"), Vector("D", "H"), Vector("D", "I"), Vector("D", "J"), Vector("D", "K"), Vector("D", "L"), Vector("D", "M"),
    Vector("E", "F"), Vector("E", "G"), Vector("E", "H"), Vector("E", "I"), Vector("E", "J"), Vector("E", "K"), Vector("E", "L"),
    Vector("E", "M"), Vector("F", "G"), Vector("F", "H"), Vector("F", "I"), Vector("F", "J"), Vector("F", "K"), Vector("F", "L"),
    Vector("F", "M"), Vector("G", "J"), Vector("G", "M"), Vector("H", "K"), Vector("H", "M"), Vector("I", "L"), Vector("I", "M"),
    Vector("J", "M"), Vector("K", "M"), Vector("L", "M")

  ))
  queries(isAfter.name) = Query("isAfter", isAfter, Set(
    Vector("B", "A"), Vector("D", "A"), Vector("E", "A"), Vector("C", "B"), Vector("D", "C"), Vector("E", "C"), Vector("E", "D"),
    Vector("F", "E"), Vector("G", "F"), Vector("H", "F"), Vector("I", "F"), Vector("J", "G"), Vector("K", "H"), Vector("L", "I"),
    Vector("M", "J"), Vector("M", "K"), Vector("M", "L"), Vector("A", "C"), Vector("A", "E"), Vector("A", "F"), Vector("B", "D"),
    Vector("B", "E"), Vector("C", "E"), Vector("C", "F"), Vector("D", "F"), Vector("E", "G"), Vector("E", "H"), Vector("E", "I"),
    Vector("F", "J"), Vector("F", "K"), Vector("F", "L"), Vector("G", "M"), Vector("H", "M"), Vector("I", "M"), Vector("C", "D"),
    Vector("E", "B"), Vector("E", "E"), Vector("F", "B"), Vector("F", "D"), Vector("J", "H"), Vector("J", "I"), Vector("K", "G"),
    Vector("K", "I"), Vector("L", "G"), Vector("L", "H"), Vector("A", "B"), Vector("A", "A"), Vector("C", "A"), Vector("C", "C"),
    Vector("D", "B"), Vector("F", "A"), Vector("G", "A"), Vector("G", "B"), Vector("G", "C"), Vector("H", "A"), Vector("H", "B"),
    Vector("H", "C"), Vector("I", "A"), Vector("I", "B"), Vector("I", "C"), Vector("J", "A"), Vector("J", "D"), Vector("J", "C"),
    Vector("K", "A"), Vector("K", "D"), Vector("K", "C"), Vector("L", "A"), Vector("L", "D"), Vector("L", "C"), Vector("M", "E"),
    Vector("D", "D"), Vector("D", "E"), Vector("B", "B"), Vector("B", "C"), Vector("B", "F"), Vector("E", "F"), Vector("B", "G"),
    Vector("B", "H"), Vector("B", "I"), Vector("D", "G"), Vector("D", "H"), Vector("D", "I"), Vector("A", "D"), Vector("A", "G"),
    Vector("A", "H"), Vector("A", "I"), Vector("A", "J"), Vector("B", "J"), Vector("C", "J"), Vector("A", "K"), Vector("B", "K"),
    Vector("C", "K"), Vector("A", "L"), Vector("B", "L"), Vector("C", "L"), Vector("A", "M"), Vector("D", "M"), Vector("C", "M"),
    Vector("C", "G"), Vector("C", "H"), Vector("C", "I"), Vector("E", "J"), Vector("E", "K"), Vector("E", "L"), Vector("F", "C"),
    Vector("F", "F"), Vector("F", "G"), Vector("F", "H"), Vector("F", "I"), Vector("G", "E"), Vector("H", "E"), Vector("I", "E"),
    Vector("D", "J"), Vector("D", "K"), Vector("D", "L"), Vector("B", "M"), Vector("E", "M"), Vector("F", "M"), Vector("G", "D"),
    Vector("G", "G"), Vector("G", "H"), Vector("G", "I"), Vector("H", "D"), Vector("H", "G"), Vector("H", "H"), Vector("H", "I"),
    Vector("I", "D"), Vector("I", "G"), Vector("I", "H"), Vector("I", "I"), Vector("G", "J"), Vector("G", "K"), Vector("G", "L"),
    Vector("H", "J"), Vector("H", "K"), Vector("H", "L"), Vector("I", "J"), Vector("I", "K"), Vector("I", "L"), Vector("J", "B"),
    Vector("J", "E"), Vector("J", "F"), Vector("J", "J"), Vector("J", "K"), Vector("J", "L"), Vector("K", "B"), Vector("K", "E"),
    Vector("K", "F"), Vector("K", "J"), Vector("K", "K"), Vector("K", "L"), Vector("L", "B"), Vector("L", "E"), Vector("L", "F"),
    Vector("L", "J"), Vector("L", "K"), Vector("L", "L"), Vector("M", "A"), Vector("M", "B"), Vector("M", "D"), Vector("M", "C"),
    Vector("M", "F"), Vector("M", "G"), Vector("M", "H"), Vector("M", "I"), Vector("J", "M"), Vector("K", "M"), Vector("L", "M"),
    Vector("M", "M")
  ))
}
