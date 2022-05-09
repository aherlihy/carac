package datalog

import datalog.execution.{ExecutionEngine, SimpleExecutionEngine}
import datalog.dsl.Program


@main def main = {
  given engine: ExecutionEngine = new SimpleExecutionEngine

  val program = Program()
  val e = program.relation[String]("e")
  val p = program.relation[String]("p")
  val a = program.relation[String]("a")
//  val b = program.relation[String]("b")
//  val c = program.relation[String]("c")
//  val d = program.relation[String]("d")
  val other = program.relation[String]("other")

  val x, y, z = program.variable()

  // TODO: use context param to avoid needing :- ()?
  e("a", "b") :- ()
  e("b", "c") :- ()
  e("c", "d") :- ()
  p(x, y) :- e(x, y)
  p(x, z) :- ( e(x, y), p(y, z) )
  other(x) :- e("a", x)
//  a(x) :- b(x)
//  b(y) :- c(y)
//  c(x) :- a(x)
//  a(x) :- other(x)

  println(other.solve())
}
