package datalog

import datalog.execution.{ExecutionEngine, SimpleExecutionEngine}
import datalog.dsl.Program


@main def main = {
  given engine: ExecutionEngine = new SimpleExecutionEngine

  val program = Program()
  val e = program.relation[String]("e")
  val p = program.relation[String]("p")
//  val ans1 = program.relation[String]()
//  val ans2 = program.relation[String]()
//  val ans3 = program.relation[String]()

  val x, y, z = program.variable()

  // TODO: use context param to avoid needing :- ()?
  e("a", "b") :- ()
  e("b", "c") :- ()
  e("c", "d") :- ()
  p(x, y) :- e(x, y)
  p(x, z) :- ( e(x, y), p(y, z) )

  println(p.solve())
}
