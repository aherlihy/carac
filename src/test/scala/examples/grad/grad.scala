package examples.grad

import datalog.dsl.{Constant, Program}
import tools.TestGenerator

import java.nio.file.Paths
class grad extends TestGenerator(
  Paths.get("src", "test", "scala", "examples", "grad") // TODO: use pwd
) {

  override val toSolve: String = "grad"
  def pretest(program: Program): Unit = {
    val course = program.relation[Constant]("course")

    val grad = program.relation[Constant]("grad")

    val pre = program.relation[Constant]("pre")

    val student = program.relation[Constant]("student")

    val take = program.relation[Constant]("take")

    val Pre, Post, X, S = program.variable()

    student("adam") :- ()
    student("bob") :- ()
    student("pete") :- ()
    student("scott") :- ()
    student("tony") :- ()
    
    course("eng") :- ()
    course("his") :- ()
    course("lp") :- ()
    
    take("adam","eng") :- ()
    take("pete","his") :- ()
    take("pete","eng") :- ()
    take("scott","his") :- ()
    take("scott","lp") :- ()
    take("tony","his") :- ()
    
    pre("eng","lp") :- ()
    pre("hist","eng") :- ()
    
    pre(Pre,Post) :- ( pre(Pre,X), pre(X,Post) )
    
    grad(S) :- ( take(S,"his"), take(S,"eng") )
  }
}