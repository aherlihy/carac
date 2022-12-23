package examples.func

import datalog.dsl.{Constant, Program}
import tools.TestGenerator

import java.nio.file.Paths
class func extends TestGenerator(
  Paths.get("src", "test", "scala", "examples", "func") // TODO: use pwd
) {

  def pretest(program: Program): Unit = {
    val eq = program.relation[Constant]("eq")
    val succ = program.relation[Constant]("succ")
    val f = program.relation[Constant]("f")
    val arg = program.relation[Constant]("arg")
    val args = program.relation[Constant]("args")
    val a, b, v, w, i, p, k, any1, any2 = program.variable()

    
    succ("1", "2") :- ()
    succ("2", "3") :- ()
    succ("3", "4") :- ()
    
    f("x", "g") :- ()
    f("y", "f") :- ()
    
    arg("x", "1", "A") :- ()
    arg("x", "2", "B") :- ()
    arg("x", "3", "Z") :- ()
    
    arg("y", "1", "C") :- ()
    arg("y", "2", "D") :- ()
    arg("y", "3", "W") :- ()
    
    eq(a, b) :- ( f(v, a), f(w, b), args(v, w, "3") )
    
    args(v, w, i) :- ( succ(p, i), arg(v, i, k), arg(w, i, k), args(v, w, p) )
    args(v, w, "1") :- ( arg(v, "1", any1), arg(w, "1", any2) )
  }
}
