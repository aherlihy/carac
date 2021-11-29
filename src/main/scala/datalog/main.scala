package datalog

import datalog.execution.{ExecutionEngine, SimpleExecutionEngine}
import datalog.dsl.Program

def transitiveClosure() = {
  given engine: ExecutionEngine = new SimpleExecutionEngine

  val program = Program()
  val e = program.relation[Int]()
  val p = program.relation[Int]()
  val ans = program.relation[Int]()

  val x = program.variable()
  val y = program.variable()
  val z = program.variable()

  e(1, 2) :- () // TODO: use context param to avoid needing :- ()?
  e(2, 3) :- ()
  p(x, y) :- e(x, y)
  p(x, z) :- ( e(x, y), p(y, z) )

  ans(x, 1) :- p(1, x)

  ans.solve()
}

def nonRecursive() = {
  given engine: ExecutionEngine = new SimpleExecutionEngine

  val program = Program()
  val edge = program.relation[String]()
  val oneHop = program.relation[String]()
  val twoHops = program.relation[String]()
  val ans = program.relation[String]()

  val x = program.variable()
  val y = program.variable()
  val z = program.variable()

  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()
  oneHop(x, y) :- edge(x, y)
  twoHops(x, z) :- ( edge(x, y), oneHop(y, z) )
  ans(x) :- oneHop("a", x)
  //  ans(x) :- twoHops("a", x)

  ans.solve()
}

@main def main = {
  System.out.println(nonRecursive())
}
