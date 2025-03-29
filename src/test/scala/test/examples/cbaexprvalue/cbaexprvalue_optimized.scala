package test.examples.cbaexprvalue

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths
class cbaexprvalue_optimized_test extends ExampleTestGenerator("cbaexprvalue") with cbaexprvalue_optimized
trait cbaexprvalue_optimized {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cbaexprvalue/facts"
  val toSolve = "data_term"
  def pretest(program: Program): Unit = {
    val kind = program.relation[Constant]("kind")
    val term = program.relation[Constant]("term")
    val app = program.relation[Constant]("app")
    val lits = program.relation[Constant]("lits")
    val vars = program.relation[Constant]("vars")
    val abs = program.relation[Constant]("abs")
    val ctrl_term = program.relation[Constant]("ctrl_term")
    val ctrl_var = program.relation[Constant]("ctrl_var")
    val data_term = program.relation[Constant]("data_term")
    val data_var = program.relation[Constant]("data_var")

    val i, v, l, x, t1, f, b, a = program.variable()
    val any1, any2 = program.variable()

    kind( "Lit" ) :- ()
    kind( "Var" ) :- ()
    kind( "Abs" ) :- ()
    kind( "App" ) :- ()

    lits( 0, "3") :- ()
    lits( 1, "2" ) :- ()

    term( 0, "Lit", 0 ) :- ()
    term( 1, "Lit", 1 ) :- ()

    vars( 0, "x" ) :- ()
    vars( 1, "y" ) :- ()
    vars( 2, "z" ) :- ()

    term( 2, "Var", 0 ) :- ()
    term( 3, "Var", 1 ) :- ()
    term( 4, "Var", 2 ) :- ()

    abs( 0, 0, 8 ) :- ()
    abs( 1, 1, 7 ) :- ()
    abs( 2, 2, 4 ) :- ()

    term( 5, "Abs", 0 ) :- ()
    term( 6, "Abs", 1 ) :- ()
    term( 7, "Abs", 2 ) :- ()

    app( 0, 9, 1 ) :- ()
    app( 1, 2, 0 ) :- ()
    app( 2, 5, 6 ) :- ()

    term( 8, "App", 0 ) :- ()
    term( 9, "App", 1 ) :- ()
    term( 10, "App", 2 ) :- ()

    data_term( i, v ) :- ( term(i, "Lit", l), lits(l, v) )

    data_term( i, v ) :- ( term(i, "Var", x), data_var(x, v) )

    data_term( i, v ) :- ( app(x, t1, any1), ctrl_term(t1, f), abs(f, any2, b), data_term(b, v),  term(i, "App", x) )

    data_var( i, v ) :- ( data_term(b, v), app(any1, a, b), ctrl_term(a, f), abs(f, i, any2) )

    ctrl_term( i, v ) :- ( term(i, "Var", x), ctrl_var(x, v) )

    ctrl_term( i, v ) :- (  app(x, t1 , any1), ctrl_term(t1, f), abs(f, any2, b), ctrl_term(b, v), term(i, "App", x) )

    ctrl_term( i, v ) :- term(i, "Abs", v)

    ctrl_var( i, v ) :- ( app(any1, a, b), ctrl_term(a, f), abs(f, i, any2), ctrl_term(b, v) )
  }
}
