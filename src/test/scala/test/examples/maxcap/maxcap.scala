package test.examples.maxcap

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program, __, groupBy, AggOp}
import test.ExampleTestGenerator


class maxcap_test extends ExampleTestGenerator("maxcap") with maxcap

trait maxcap {
  val toSolve = "maxcap"
  def pretest(program: Program): Unit = {
    val edge = program.relation[Constant]("edge")
    val oneh = program.relation[Constant]("oneh")
    val twoh = program.relation[Constant]("twoh")
    val cap = program.relation[Constant]("cap")
    val maxcap = program.relation[Constant]("maxcap")
    
    val x, y, z, a, b = program.variable()

    edge("1", "2") :- ()
    edge("1", "8") :- ()
    edge("1", "5") :- ()
    edge("2", "8") :- ()
    edge("2", "3") :- ()
    edge("2", "6") :- ()
    edge("8", "5") :- ()
    edge("8", "4") :- ()
    edge("8", "3") :- ()
    edge("5", "4") :- ()
    edge("6", "3") :- ()
    edge("6", "7") :- ()
    edge("3", "7") :- ()
    edge("3", "4") :- ()
    edge("4", "7") :- ()
    edge(x, y) :- edge(y, x)

    oneh(x, y, z) :- (edge(x, y), edge(y, z))
    twoh(x, a, b, z) :- (oneh(x, a, b), oneh(a, b, z))

    cap(a, b, z) :- groupBy(twoh(x, a, b, y), Seq(a, b), AggOp.COUNT(1) -> z)

    maxcap(b) :- groupBy(cap(x, y, a), Seq(), AggOp.MAX(a) -> b)
  }
}
