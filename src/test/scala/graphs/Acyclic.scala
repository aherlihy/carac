package graphs

import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.{ExecutionEngine, SimpleExecutionEngine}

import scala.collection.mutable

case class Acyclic(program: Program) extends TestGraph {
  val description = "Acyclic"
  val queries: mutable.Map[String, Query] = mutable.Map[String, Query]()

  private val edge = program.relation[Constant](" e")
  private val oneHop = program.relation[Constant]("oh")
  private val twoHops = program.relation[Constant]("th")
  private val queryA = program.relation[Constant]("queryA")
  private val queryB = program.relation[Constant]("queryB")

  private val x, y, z = program.variable()

  edge("a", "a") :- ()
  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()
  oneHop(x, y) :- edge(x, y)
  twoHops(x, z) :- ( edge(x, y), oneHop(y, z) )
  queryA(x) :- oneHop("a", x)
  queryA(x) :- twoHops("a", x)
  queryB(x) :- (oneHop("a", x), oneHop(x, "c"))

  queries(queryA.name) = Query("oneHop from a", queryA, Set(
    Vector("a"),
    Vector("b"),
    Vector("c")
  ))
  queries(queryB.name) = Query("oneHop between a and c", queryB, Set(
    Vector("b", "b") // TODO: should this be (b, b) or (b)?
  ))
}
