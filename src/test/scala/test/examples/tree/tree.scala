package test.examples.tree

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program, __}
import test.ExampleTestGenerator

import java.nio.file.Paths
class tree_test extends ExampleTestGenerator("tree") with tree
trait tree {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tree/facts"
  val toSolve: String = "S"
  def pretest(program: Program): Unit = {
    val S = program.relation[Constant]("S")

    val T = program.relation[Constant]("T")

    val R = program.relation[Constant]("R")

    val x1, x2, x3, x4, a = program.variable()

    S (x1, x3) :- ( T(x1, x2), R(x2, __, x3) )
    T (x1, x4) :- ( R(x1, __, x2), R(x2, __, x3), T(x3, x4) )
    T (x1, x3) :- ( R(x1, a, x2), R(x2, a, x3) )
    
    R("1", "a", "2") :- ()
    R("2", "b", "3") :- ()
    R("3", "a", "4") :- ()
    R("4", "a", "5") :- ()
    R("5", "a", "6") :- ()
  }
}
