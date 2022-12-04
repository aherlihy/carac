package graphs

import datalog.dsl.{Program, Constant}

object clique {
  def run(program: Program): Unit = {
    val edge = program.namedRelation("edge")
    val reachable = program.relation[Constant]("reachable")
    val same_clique = program.relation[Constant]("same_clique")
    val x, y, z = program.variable()

    reachable(x, y) :- edge(x, y)
    reachable(x, y) :- ( edge(x, z), reachable(z, y) )
    same_clique(x, y) :- ( reachable(x, y), reachable(y, x) )
  }
 }