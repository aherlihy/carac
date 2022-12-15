package datalog

import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.dsl.{Program, Constant}
import datalog.storage.{CollectionsStorageManager, IndexedCollStorageManager, RelationalStorageManager}

import scala.util.Random
import scala.collection.mutable
import scala.quoted.*
import scala.quoted.staging.*

def souff() = {
  given engine: ExecutionEngine = new NaiveExecutionEngine(new RelationalStorageManager())
  val program = Program(engine)
//  val f1 = program.relation[Constant]("f1")
  val succ = program.relation[Constant]("succ")
  val plus_mod = program.relation[Constant]("plus_mod")

  plus_mod("0", "0", "0") :- ()
  plus_mod("0", "1", "1") :- ()
  plus_mod("1", "0", "1") :- ()
  plus_mod("1", "1", "2") :- ()
  plus_mod("0", "2", "2") :- ()
  plus_mod("2", "0", "2") :- ()
  plus_mod("2", "1", "0") :- ()
  plus_mod("1", "2", "0") :- ()
  plus_mod("2", "2", "1") :- ()

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

  val f = program.relation[Constant]("f")
  val i, r, prev, pprev, x, y = program.variable()

//  f("0", "0") :- ()
//  f("1", "1") :- ()
////  f(i, r) :- f(i, r)
//  f(i, r) :- ( succ(prev, i), succ(pprev, prev),f(prev, x), f(pprev, y), plus_mod(x, y, r) )
  f("0", "0") :- ()
  f("1", "1") :- ()
  f(i, r) :- ( succ(prev, i), succ(pprev, prev),f(prev, x), f(pprev, y), plus_mod(x, y, r) )

  assert(f.solve().size == 21)

}

def msp() = {

//  given Compiler = Compiler.make(getClass.getClassLoader)
//  val str = "println(100)"
//  def expr(using Quotes) = str
//  println(run(expr))
}

def run(): Unit = {
//  given engine: ExecutionEngine = new NaiveExecutionEngine(new RelationalStorageManager())
//  val program = Program(engine)
//  val edge = program.relation[Constant]("edge")
//  val isBefore = program.relation[Constant]("isBefore")
////  val isAfter = program.relation[Constant]("isAfter")
//  val x, y, z = program.variable()
//
//  edge("a", "b") :- ()
//  isBefore(x, y) :- edge(x, y)
//  isBefore(x, y) :- (isBefore(x, z), isBefore(z, y))
//
//  println(isBefore.solve())
//
//  isAfter(x, y) :- edge(y, x)
//  isAfter(x, y) :- (isAfter(z, x), isAfter(y, z))
}

@main def main = {
  souff()
}
