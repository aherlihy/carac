package datalog

import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.dsl.{Program, Constant}
import datalog.storage.{CollectionsStorageManager, IndexedCollStorageManager, RelationalStorageManager}

import scala.util.Random
import scala.collection.mutable
import scala.quoted.*
import scala.quoted.staging.*

def tc() = {
  println("naive+coll, before+after")
  given engine: ExecutionEngine = new NaiveExecutionEngine(new CollectionsStorageManager())
  val program = Program(engine)
  val edge = program.relation[String]("edge")
//  val isBefore = program.relation[String]("isBefore")
  val isAfter = program.relation[String]("isAfter")

  val x, y, z = program.variable()

  //  for i <- 0 until 10000 do
  //    e(
  //      Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString,
  //      Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString
  //    )  :- ()

  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()
//  edge("d", "e") :- ()

//  isBefore(x, y) :- edge(x, y)
//  isBefore(x, y) :- ( isBefore(x, z), isBefore(z, y) )

  isAfter(x, y) :- edge(y, x)
  isAfter(x, y) :- ( isAfter(z, x), isAfter(y, z) )


  println(
    isAfter.solve()
      .map(f => f.head.toString + "\t" + f.last.toString)
      .toList
      .sorted
      .mkString("", "\n", "")
  )
  // TODO: start here, step into isAfter
}

def souff() = {
  println("SEMINAIVE")
  given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new RelationalStorageManager())
  val program = Program(engine)
  val edge = program.relation[String]("edge")
  val reachable = program.relation[Constant]("reachable")
  val same_clique = program.relation[Constant]("same_clique")
  val x, y, z = program.variable()

  reachable(x, y) :- edge(x, y)
  reachable(x, y) :- ( edge(x, z), reachable(z, y) )
  same_clique(x, y) :- ( reachable(x, y), reachable(y, x) )

    edge("0", "1") :- ()
    edge("1", "2") :- ()
    edge("2", "0") :- ()
//    edge("3", "0") :- ()
//  edge("1", "2") :- ()
//  edge("2", "3") :- ()
//  edge("3", "4") :- ()
//  edge("4", "5") :- ()
//  edge("5", "0") :- ()
//  edge("5", "6") :- ()
//  edge("6", "7") :- ()
//  edge("7", "8") :- ()
//  edge("8", "9") :- ()
//  edge("9", "10") :- ()
//  edge("10", "7") :- ()

  println("solve=" + same_clique.solve().size)//.map(f => f.head.toString + "\t" + f.last.toString).mkString("", "\n", ""))

}

def msp() = {

//  given Compiler = Compiler.make(getClass.getClassLoader)
//  val str = "println(100)"
//  def expr(using Quotes) = str
//  println(run(expr))
}

def run(): Unit = {
  given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new CollectionsStorageManager())
  val program = Program(engine)
  val edge = program.relation[Constant]("edge")
  val isBefore = program.relation[Constant]("isBefore")
//  val isAfter = program.relation[Constant]("isAfter")
  val x, y, z = program.variable()

  edge("a", "b") :- ()
  isBefore(x, y) :- edge(x, y)
  isBefore(x, y) :- (isBefore(x, z), isBefore(z, y))

  println(isBefore.solve())

//  isAfter(x, y) :- edge(y, x)
//  isAfter(x, y) :- (isAfter(z, x), isAfter(y, z))
}

@main def main = {
  souff()
}
