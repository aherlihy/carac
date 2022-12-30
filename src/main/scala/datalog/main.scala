package datalog

import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine, StagedExecutionEngine}
import datalog.dsl.{Constant, Program, __}
import datalog.storage.{CollectionsStorageManager, NS, RelationalStorageManager}
import datalog.execution.ast.CopyEliminationPass

import scala.util.Random
import scala.collection.mutable

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
  given engine: ExecutionEngine = new StagedExecutionEngine(new CollectionsStorageManager())
  val program = Program(engine)
  val edge = program.relation[Constant]("edge")
  val oneHop = program.relation[Constant]("oneHop")
  val twoHops = program.relation[Constant]("twoHops")
  val threeHops = program.relation[Constant]("threeHops")
//  val fourHops = program.relation[Constant]("fourHops")
  val x, y, z, w, q = program.variable()

  edge("a", "a") :- ()
  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()
  edge("d", "e") :- ()

  oneHop("a", y) :- edge(x, y)
  twoHops(x, y) :- oneHop(x, y)
  threeHops(x, y) :- (twoHops(x, z), edge(z, y))


//  threeHops(x, w) :- ( edge(x, y), oneHop(y, z), oneHop(z, w))
//  fourHops(x, q) :- ( edge(x, y), oneHop(y, z), oneHop(z, w), oneHop(w, q))

//  println(s"graph to string ${program.ee.precedenceGraph.toString()}")
//  program.ee.precedenceGraph.topSort()
//  println(program.ee.precedenceGraph.sortedString())
//  oneHop.solve()
//  val tree = program.ee.asInstanceOf[StagedExecutionEngine].tree
//  val cpp = CopyEliminationPass().transform(tree)
//  println(program.ee.storageManager.printer.printAST(tree))
//  println("transformed:")
//  println(program.ee.storageManager.printer.printAST(cpp))
}

@main def main = {
  run()
}
