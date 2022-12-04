package datalog

import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.dsl.{Program, Constant}
import datalog.storage.{CollectionsStorageManager, IndexedCollStorageManager, RelationalStorageManager}

import scala.util.Random
import scala.collection.mutable
import scala.quoted.*
import scala.quoted.staging.*

def souff() = {
  given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new IndexedCollStorageManager())
  val program = Program(engine)
  val succ = program.relation[Constant]("succ")
  val greaterThanZ = program.relation[Constant]("greaterThanZ")
  val ack = program.relation[Constant]("ack")
  val N, M, X, Y, Ans, Ans2 = program.variable()

  ack("0", N, Ans) :- succ(N, Ans)

  ack(M, "0", Ans) :- ( greaterThanZ(M), succ(X, M), ack(X, "1", Ans) )

  ack(M, N, Ans) :- (
    greaterThanZ(M),
    greaterThanZ(N),
    succ(X, M),
    succ(Y, N),
    ack(M, Y, Ans2),
    ack(X, Ans2, Ans))

  // EDB
  succ("0", "1") :- ()
  succ("1", "2") :- ()
  succ("2", "3") :- ()
  succ("3", "4") :- ()
  succ("4", "5") :- ()
  succ("5", "6") :- ()
  succ("6", "7") :- ()
  succ("7", "8") :- ()
  succ("8", "9") :- ()
  succ("9", "10") :- ()
  succ("10", "11") :- ()
  succ("11", "12") :- ()
  succ("12", "13") :- ()
  succ("13", "14") :- ()
  succ("14", "15") :- ()
  succ("15", "16") :- ()
  succ("16", "17") :- ()
  succ("17", "18") :- ()
  succ("18", "19") :- ()
  succ("19", "20") :- ()
  succ("20", "21") :- ()

  greaterThanZ("1") :- ()
  greaterThanZ("2") :- ()
  greaterThanZ("3") :- ()
  greaterThanZ("4") :- ()
  greaterThanZ("5") :- ()
  greaterThanZ("6") :- ()
  greaterThanZ("7") :- ()
  greaterThanZ("8") :- ()
  greaterThanZ("9") :- ()
  greaterThanZ("10") :- ()
  greaterThanZ("11") :- ()
  greaterThanZ("12") :- ()
  greaterThanZ("13") :- ()
  greaterThanZ("14") :- ()
  greaterThanZ("15") :- ()
  greaterThanZ("16") :- ()
  greaterThanZ("17") :- ()
  greaterThanZ("18") :- ()
  greaterThanZ("19") :- ()
  greaterThanZ("20") :- ()

  println(ack.solve())
}

def msp() = {

//  given Compiler = Compiler.make(getClass.getClassLoader)
//  val str = "println(100)"
//  def expr(using Quotes) = str
//  println(run(expr))
}

def run(): Unit = {
  given engine: ExecutionEngine = new NaiveExecutionEngine(new RelationalStorageManager())
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
