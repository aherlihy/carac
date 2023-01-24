package datalog

import datalog.execution.{ExecutionEngine, SemiNaiveExecutionEngine, NaiveExecutionEngine, NaiveStagedExecutionEngine, SemiNaiveStagedExecutionEngine}
import datalog.dsl.{Constant, Program, __}
import datalog.execution.ast.transform.CopyEliminationPass
import datalog.storage.{CollectionsStorageManager, NS, RelationalStorageManager}

import scala.util.Random
import scala.collection.mutable

def run(program: Program): Unit = {
  val edge = program.relation[Constant]("edge")
  val path = program.relation[Constant]("path")
  val x, y, z = program.variable()

  path(x, y) :- edge(x, y)
  path(x, z) :- (edge(x, y), path(y, z))

  edge("a", "a") :- ()
  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()

//  queryA(x) :- oneHop("a", x)
//  queryA(x) :- oneHop("a", x)

//  queryB(x) :- (oneHop("a", x), oneHop(x, "c"))
//

//  threeHops(x, w) :- ( edge(x, y), oneHop(y, z), oneHop(z, w))
//  fourHops(x, q) :- ( edge(x, y), oneHop(y, z), oneHop(z, w), oneHop(w, q))

//  println(s"graph to string ${program.ee.precedenceGraph.toString()}")
//  program.ee.precedenceGraph.topSort()
//  println(program.ee.precedenceGraph.sortedString())
  println("RES=" + path.solve())
}

@main def main = {
  given engine: ExecutionEngine = new NaiveStagedExecutionEngine(new CollectionsStorageManager())
  val program = Program(engine)
  println("staged")
  run(program)

//  given engine2: ExecutionEngine = new NaiveExecutionEngine(new CollectionsStorageManager())
//  val program2 = Program(engine2)
//  println("seminaive")
//  run(program2)
}
