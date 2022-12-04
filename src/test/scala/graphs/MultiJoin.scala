package graphs

import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.{ExecutionEngine, NaiveExecutionEngine}

import scala.collection.mutable

case class MultiJoin(program: Program) extends TestGraph {
  val description = "MultiJoin"
  val queries: mutable.Map[String, Query] = mutable.Map[String, Query]()

  private val edge = program.relation[Constant]("e")
  private val oneHop = program.relation[Constant]("oh")
  private val twoHops = program.relation[Constant]("th")
  private val threeHops = program.relation[Constant]("threeH")
  private val fourHops = program.relation[Constant]("fourH")

  val x, y, z, w, q = program.variable()

  edge("a", "a") :- ()
  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()
  edge("d", "e") :- ()
  oneHop(x, y) :- edge(x, y)
  twoHops(x, z) :- ( edge(x, y), oneHop(y, z) )
  threeHops(x, w) :- ( edge(x, y), oneHop(y, z), oneHop(z, w))
  fourHops(x, q) :- ( edge(x, y), oneHop(y, z), oneHop(z, w), oneHop(w, q))

  queries(oneHop.name) = Query("oneHop.solve", oneHop, Set(
    Vector("a", "a"),
    Vector("a", "b"),
    Vector("b", "c"),
    Vector("c", "d"),
    Vector("d", "e")
  ))
  queries(twoHops.name) = Query("twoHops.solve", twoHops, Set(
   Vector("a", "a"),
    Vector("a", "b"),
    Vector("a", "c"),
    Vector("b", "d"),
    Vector("c", "e")
  ))
  queries(threeHops.name) = Query("threeHops.solve", threeHops, Set(
    Vector("a", "a"),
    Vector("a", "b"),
    Vector("a", "c"),
    Vector("a", "d"),
    Vector("b", "e")
  ))
  queries(fourHops.name) = Query("fourHops.solve", fourHops, Set(
    Vector("a", "a"),
    Vector("a", "b"),
    Vector("a", "c"),
    Vector("a", "d"),
    Vector("a", "e")
  ))
}
