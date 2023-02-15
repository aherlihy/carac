package test.examples.small

import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths

class small_test extends ExampleTestGenerator("small") with small
trait small {
  val toSolve = "ancestor"
  def pretest(program: Program): Unit = {
    val ancestor = program.relation[Constant]("ancestor")

    val father = program.relation[Constant]("father")

    val mother = program.relation[Constant]("mother")

    val parent = program.relation[Constant]("parent")

    val X, Y, Z = program.variable()
    
    parent(X,Y) :- ( mother(X,Y) )
    parent(X,Y) :- ( father(X,Y) )
    ancestor(X,Y) :- ( parent(X,Y) )
    ancestor(X,Y) :- ( parent(X,Z), ancestor(Z,Y) )
    
    mother("claudette", "ann") :- ()
    mother("jeannette", "bill") :- ()
    mother("mireille", "john") :- ()
    father("john", "ann") :- ()
    father("john", "bill") :- ()
    father("jean-jacques", "alphonse") :- ()
    father("alphonse", "mireille") :- ()
    father("brad", "john") :- ()
  }
}
