package graphs

import datalog.dsl.{Constant, Program, Relation, Term}

import scala.collection.mutable

trait MultiIsolatedCycle extends TestGraph {
  val description = "MultipleIsolatedCycle"
  val queries: mutable.Map[String, Query] = mutable.Map[String, Query]()

  def initGraph(program: Program) = {
    val e = program.relation[Constant]("e")
    val p = program.relation[Constant]("p")
    val path2a = program.relation[Constant]("path2a")
    val path2a1 = program.relation[Constant]("path2a1")
//    val empty1 = program.relation[Constant]()
//    val empty2 = program.relation[Constant]()
    val edge2a = program.relation[Constant]("edge2a")

    val x, y, z = program.variable()

    e("a", "b") :- ()
    e("b", "c") :- ()
    e("c", "d") :- ()
    e("a1", "b1") :- ()
    e("b1", "c1") :- ()
    e("c1", "d1") :- ()
    p(x, y) :- e(x, y)
    p(x, z) :- ( e(x, y), p(y, z) )
    path2a(x) :- p("a", x)
    edge2a(x) :- e("a", x)
    path2a1(x) :- p("a1", x)

    queries(e.name) = Query(
      "edge edb",
      e,
      Set(
        Vector("a", "b"),
        Vector("b", "c"),
        Vector("c", "d"),
        Vector("a1", "b1"),
        Vector("b1", "c1"),
        Vector("c1", "d1")
      ))

    queries(p.name) = Query(
      "path edb",
      p,
      Set(
        Vector("a", "d"),
        Vector("b", "d"),
        Vector("b", "c"),
        Vector("a", "b"),
        Vector("a", "c"),
        Vector("c", "d"),
        Vector("a1", "d1"),
        Vector("b1", "d1"),
        Vector("b1", "c1"),
        Vector("a1", "b1"),
        Vector("a1", "c1"),
        Vector("c1", "d1")
    ))

    queries(path2a.name) = Query(
      "path query to a",
      path2a,
      Set(
        Vector("d"),
        Vector("b"),
        Vector("c")
    ))

    queries(path2a1.name) = Query(
      "path query to a1",
      path2a1,
      Set(
        Vector("d1"),
        Vector("b1"),
        Vector("c1")
      ))
  }
}
