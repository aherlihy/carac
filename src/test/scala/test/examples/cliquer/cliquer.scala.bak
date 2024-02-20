package test.examples.cliquer

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths
class cliquer_test extends ExampleTestGenerator("cliquer") with cliquer
trait cliquer {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cliquer/facts"
  val toSolve = "same_clique"
  def pretest(program: Program): Unit = {
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
