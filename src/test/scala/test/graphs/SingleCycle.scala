package test.graphs

import carac.dsl.{Constant, Program, Relation, Term}

import scala.collection.mutable

case class SingleCycle(program: Program) extends TestGraph {
  val description: String = "SingleCycle"
  val queries: mutable.Map[String, Query] = mutable.Map[String, Query]()

  private val e = program.relation[Constant]("e")
  private val p = program.relation[Constant]("p")
  private val path2a = program.relation[Constant]("path2a")
  private val edge2a = program.relation[Constant]("edge2a")

  private val x, y, z = program.variable()

  e("a", "b") :- ()
  e("b", "c") :- ()
  e("c", "d") :- ()
  p(x, y) :- e(x, y)
  p(x, z) :- ( e(x, y), p(y, z) )

  path2a(x) :- p("a", x)
  edge2a(x) :- e("a", x)

  queries(e.name) = Query("edge edb", e, Set(
    Vector("a", "b"),
    Vector("b", "c"),
    Vector("c", "d")))

  queries(edge2a.name) = Query("edge query to a", edge2a, Set(
    Vector("b")
  ))
  queries(path2a.name) = Query("path query to a", path2a, Set(
    Vector("d"),
    Vector("b"),
    Vector("c"),
  ))
}
