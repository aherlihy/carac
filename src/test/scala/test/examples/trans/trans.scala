package test.examples.trans

import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths
class trans extends ExampleTestGenerator("trans") {

  def pretest(program: Program): Unit = {
    val A = program.relation[Constant]("A")

    val x, y, z = program.variable()
    
    A(x,z) :- ( A(x,y), A(y,z) )
    
    A("a","b") :- ()
    A("b","c") :- ()
    A("c","d") :- ()
    A("d","e") :- ()
  }
}
