package graphs

import datalog.dsl.{Constant, Program, Relation, Term}

import scala.collection.mutable

case class RecursivePath(program: Program) extends TestGraph {
  val description: String = "RecursivePath"
  val queries: mutable.Map[String, Query] = mutable.Map[String, Query]()

  private val e = program.relation[Constant]("e")
  private val p = program.relation[Constant]("p")
  private val path2a = program.relation[Constant]("path2a")
  private val path2a1 = program.relation[Constant]("path2a1")
  private val edge2a = program.relation[Constant]("edge2a")
  private val reversed = program.relation[Constant]("reversed")

  private val x, y, z = program.variable()

  e("a", "b") :- ()
  e("b", "c") :- ()
  e("c", "d") :- ()
  p(x, y) :- e(x, y)
  p(x, z) :- ( p(x, y), p(y, z) )
  reversed(x, y) :- e(y, x)

  path2a(x) :- p("a", x)
  edge2a(x) :- e("a", x)

  queries(e.name) = Query("edge idb", e, Set(
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
  queries(reversed.name) = Query("order of variables", reversed, Set(
    Vector("b", "a"),
    Vector("c", "b"),
    Vector("d", "c")
  ))
}
