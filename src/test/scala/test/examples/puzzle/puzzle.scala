package test.examples.puzzle

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths
class puzzle_test extends ExampleTestGenerator("puzzle") with puzzle
trait puzzle {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/puzzle/facts"
  val toSolve = "state"
  def pretest(program: Program): Unit = {
    val opp = program.relation[Constant]("opp")

    val safe = program.relation[Constant]("safe")

    val state = program.relation[Constant]("state")

    val X, U, V, X1, Y = program.variable()

    state("n","n","n","n") :- ()

    state(X,X,U,V) :- (
      safe(X,X,U,V),
      opp(X,X1),
      state(X1,X1,U,V) )
    state(X,Y,X,V) :- (
      safe(X,Y,X,V),
      opp(X,X1),
      state(X1,Y,X1,V) )
    state(X,Y,U,X) :- (
      safe(X,Y,U,X),
      opp(X,X1),
      state(X1,Y,U,X1) )
    state(X,Y,U,V) :- (
      safe(X,Y,U,V),
      opp(X,X1),
      state(X1,Y,U,V) )
    
    opp("n","s") :- ()
    opp("s","n") :- ()

    safe("n","s","n","s") :- ()
    safe("n","n","n","n") :- ()
    safe("n","s","n","n") :- ()
    safe("n","n","n","s") :- ()
    safe("s","s","s","s") :- ()
    safe("s","n","s","n") :- ()
    safe("s","s","s","n") :- ()
    safe("s","n","s","s") :- ()
    
    safe(X,X,X1,X) :- opp(X,X1)
  }
}
