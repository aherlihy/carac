package test.examples.dfa_live_vars
import datalog.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}
class dfa_live_vars_test extends ExampleTestGenerator("dfa_live_vars", tags = Set(Tags.Negated)) with dfa_live_vars
trait dfa_live_vars {
  val toSolve: String = "live_vars_out"
  def pretest(program: Program): Unit = {
    val x, y, v = program.variable()
    val live_vars_in = program.relation[Constant]("live_vars_in")
    val live_vars_out = program.relation[Constant]("live_vars_out")
    val defP = program.relation[Constant]("defP")
    val use = program.relation[Constant]("use")
    val cf_edge = program.relation[Constant]("cf_edge")

    cf_edge("A","B") :- ()
    cf_edge("B","C") :- ()
    cf_edge("C","C") :- ()
    cf_edge("C","D") :- ()
    cf_edge("B","D") :- ()
    cf_edge("D","E") :- ()
    
    defP("A", "x1") :- ()
    
    use("B", "x1") :- ()
    defP("B", "x2") :- ()
    
    use("C", "x2") :- ()
    defP("C", "x2") :- ()
    
    use("D", "x2") :- ()
    defP("D", "x3") :- ()
    
    use("E", "x2") :- ()
    use("E", "x3") :- ()
    
    live_vars_in(x,v) :- ( use(x,v) )
    live_vars_in(x,v) :- ( !defP(x,v), live_vars_out(x,v) )
    
    live_vars_out(x,v) :- ( cf_edge(x,y), live_vars_in(y,v) )
  }
}
