package graphs

import datalog.dsl.{Program, Constant}
class cliquer extends TestIDB {

  def run(program: Program): Unit = {
    val edge = program.relation[Constant]("edge")

    val leg = program.relation[Constant]("leg")

    val reachable = program.relation[Constant]("reachable")

    val same_clique = program.relation[Constant]("same_clique")

    val X, Y, Z = program.variable()

    leg(X,Z) :- ( edge(X,Y), edge(Y,Z) )

    reachable(X, Y) :- edge(X, Y)
    reachable(X, Y) :- ( edge(X, Z), reachable(Z, Y) )
    same_clique(X, Y) :- ( reachable(X, Y), reachable(Y, X) )

    edge("a", "b") :- ()
    edge("b", "c") :- ()
    edge("c", "d") :- ()
    edge("d", "a") :- ()

    reachable("e","f") :- ()
  }
}
