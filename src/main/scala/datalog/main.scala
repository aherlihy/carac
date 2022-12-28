package datalog

import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.dsl.{Program, Constant, __}
import datalog.storage.{CollectionsStorageManager, IndexedCollStorageManager, RelationalStorageManager}

import scala.util.Random
import scala.collection.mutable
import scala.quoted.*
import scala.quoted.staging.*

def souff() = {
  given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new CollectionsStorageManager())
  val program = Program(engine)

}

def msp() = {

//  given Compiler = Compiler.make(getClass.getClassLoader)
//  val str = "println(100)"
//  def expr(using Quotes) = str
//  println(run(expr))
}

def run(): Unit = {
  given engine: ExecutionEngine = new NaiveExecutionEngine(new IndexedCollStorageManager())
  val program = Program(engine)
  val edge = program.relation[Constant]("edge")
  val edge2 = program.relation[Constant]("edge2")
  val oneHop = program.relation[Constant]("oneHop")
  val twoHops = program.relation[Constant]("twoHops")
  val threeHops = program.relation[Constant]("threeHops")
  val fourHops = program.relation[Constant]("fourHops")
  val x, y, z, w, q = program.variable()

  edge("a", "a") :- ()
  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()
  edge("d", "e") :- ()
  edge(x, y) :- edge2(x, y)
  oneHop(x, y) :- edge(x, y)
  twoHops(x, z) :- ( edge(x, y), oneHop(y, z) )
  threeHops(x, w) :- ( edge(x, y), oneHop(y, z), oneHop(z, w))
  fourHops(x, q) :- ( edge(x, y), oneHop(y, z), oneHop(z, w), oneHop(w, q))

  oneHop.solve()

}

@main def main = {
  run()
}
