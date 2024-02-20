package test.examples.trans

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths
class trans_test extends ExampleTestGenerator("trans") with trans
trait trans {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/trans/facts"
  val toSolve = "A"
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
