package graphs

import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.{ExecutionEngine, SimpleExecutionEngine}

import scala.collection.mutable

trait Acyclic extends TestGraph {
  val description = "Acyclic"
  val queries: mutable.Map[String, Query] = mutable.Map[String, Query]()
//  val solutions: mutable.Map[Int, Set[Seq[Term]]] = mutable.Map[Int, Set[Seq[Term]]]()

  def initGraph(program: Program) = {
    given engine: ExecutionEngine = new SimpleExecutionEngine

    val program = Program()
    val edge = program.relation[Constant](" e")
    val oneHop = program.relation[Constant]("oh")
    val twoHops = program.relation[Constant]("th")
    val queryA = program.relation[Constant]("queryA")
    val queryB = program.relation[Constant]("queryB")

    val x, y, z = program.variable()

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
}
