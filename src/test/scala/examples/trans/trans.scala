package examples.trans

import datalog.dsl.{Constant, Program}
import tools.GraphGenerator

import java.nio.file.Paths
class trans extends GraphGenerator(
  Paths.get("src", "test", "scala", "examples", "trans") // TODO: use pwd
) {

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
