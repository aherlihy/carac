package test.examples.rqb_cba

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths
class cba_worst_test extends ExampleTestGenerator("rqb_cba") with rqb_cba
trait rqb_cba {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/rqb_cba/facts"
  val toSolve = "data_term"
  def pretest(program: Program): Unit = {
    val kind = program.namedRelation[Constant]("kind")
    val term = program.namedRelation[Constant]("term")
    val app = program.namedRelation[Constant]("app")
    val lits = program.namedRelation[Constant]("lits")
    val vars = program.namedRelation[Constant]("vars")
    val abs = program.namedRelation[Constant]("abs")
    val ctrl_term = program.relation[Constant]("ctrl_term")
    val ctrl_var = program.relation[Constant]("ctrl_var")
    val data_term = program.relation[Constant]("data_term")
    val data_var = program.relation[Constant]("data_var")

    val i, v, l, x, t1, f, b, a = program.variable()
    val any1, any2 = program.variable()

    data_term( i, v ) :- ( term(i, "Lit", l), lits(l, v) )

    data_term( i, v ) :- ( term(i, "Var", x), data_var(x, v) )

    /*x*/ data_term( i, v ) :- ( term(i, "App", x), data_term(b, v), ctrl_term(t1, f), abs(f, any2, b), app(x, t1, any1) )

    /*x*/ data_var( i, v ) :- ( ctrl_term(a, f), data_term(b, v), abs(f, i, any2), app(any1, a, b))

    ctrl_term( i, v ) :- ( term(i, "Var", x), ctrl_var(x, v) )

    /*x*/ctrl_term( i, v ) :- ( term(i, "App", x), ctrl_term(b, v), ctrl_term(t1, f), abs(f, any2, b), app(x, t1 , any1) )

    ctrl_term( i, v ) :- term(i, "Abs", v)

    /*x*/ ctrl_var( i, v ) :- ( ctrl_term(a, f), ctrl_term(b, v),  abs(f, i, any2), app(any1, a, b) )
  }
}
