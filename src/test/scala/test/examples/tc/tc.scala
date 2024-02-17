package test.examples.tc

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths
class tc_test extends ExampleTestGenerator("tc") with tc
trait tc {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tc/facts"
  val toSolve = "many_vars"
  def pretest(program: Program): Unit = {
    val base = program.namedRelation[Constant]("base")

    val tc = program.relation[Constant]("tc")

    val tcl = program.relation[Constant]("tcl")

    val tcr = program.relation[Constant]("tcr")

    val many_vars = program.relation[Constant]("many_vars")

//    val wConst = program.relation[Constant]("wConst")

    val X, Y, Z, W = program.variable()
    
    tcl(X, Y) :- ( base(X,Y) )
    tcl(X,Y) :- ( tcl(X,Z), base(Z,Y) )
    
    tcr(X,Y) :- ( base(X,Y) )
    tcr(X,Y) :- ( base(X, Z),tcr(Z,Y) )
    
    tc(X,Y) :- ( base(X,Y) )
    tc(X,Y) :- ( tc(X,Z),tc(Z,Y) )
    
    base("a","b") :- ()
    base("b","c") :- ()
    base("c","d") :- ()
    base("z", "z") :- ()
//    base("z", "a") :- ()
//    many_vars(X, W) :- (tc(Z, X), tc(Z, W), tc(W, Y))
    many_vars(X) :- (tc(Z, X), tc(Z, X))

//    wConst(X) :- (tc("a", X), base(X, X))
  }
}
