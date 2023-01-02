package datalog

import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine, NaiveStagedExecutionEngine}
import datalog.dsl.{Constant, Program, __}
import datalog.execution.ast.transform.CopyEliminationPass
import datalog.storage.{CollectionsStorageManager, NS, RelationalStorageManager}

import scala.util.Random
import scala.collection.mutable

def run(program: Program): Unit = {
  val edge = program.relation[Constant]("edge")
  val oneHop = program.relation[Constant]("oneHop")
  val twoHops = program.relation[Constant]("twoHops")
  val threeHops = program.relation[Constant]("threeHops")

  val queryA = program.relation[Constant]("queryA")
  val queryB = program.relation[Constant]("queryB")
  val x, y, z, w, q = program.variable()

  oneHop(x, y) :- edge(x, y)
  twoHops(x, z) :- (oneHop(x, y), oneHop(y, z))
  threeHops(x, y) :- (twoHops(x, z), edge(z, y))

  edge("a", "a") :- ()
  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()

  queryA(x) :- oneHop("a", x)
  queryA(x) :- twoHops("a", x)

  queryB(x) :- (oneHop("a", x), oneHop(x, "c"))


//  threeHops(x, w) :- ( edge(x, y), oneHop(y, z), oneHop(z, w))
//  fourHops(x, q) :- ( edge(x, y), oneHop(y, z), oneHop(z, w), oneHop(w, q))

//  println(s"graph to string ${program.ee.precedenceGraph.toString()}")
//  program.ee.precedenceGraph.topSort()
//  println(program.ee.precedenceGraph.sortedString())
  println(queryA.solve())
}

@main def main = {
  given engine: ExecutionEngine = new NaiveStagedExecutionEngine(new CollectionsStorageManager())
  val program = Program(engine)
  println("staged")
  run(program)

//  given engine2: ExecutionEngine = new NaiveExecutionEngine(new CollectionsStorageManager())
//  val program2 = Program(engine2)
//  println("naive")
//  run(program2)
}
