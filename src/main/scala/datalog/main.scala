package datalog

import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, NaiveStagedExecutionEngine, SemiNaiveExecutionEngine, SemiNaiveStagedExecutionEngine}
import datalog.dsl.{Constant, Program, __}
import datalog.execution.ast.transform.CopyEliminationPass
import datalog.execution.ir.InterpreterContext
import datalog.storage.{CollectionsStorageManager, NS, RelationalStorageManager}

import scala.util.Random
import scala.collection.mutable

def run(program: Program): Unit = {
  val edge = program.relation[Constant]("edge")
  val path = program.relation[Constant]("path")
  val oneHop = program.relation[Constant]("oh")
  val twoHops = program.relation[Constant]("th")
  val threeHops = program.relation[Constant]("threeH")
  val fourHops = program.relation[Constant]("fourH")

  val x, y, z, w, q = program.variable()

  path(x, y) :- edge(x, y)
  path(x, z) :- (edge(x, y), path(y, z))

  oneHop(x, y) :- edge(x, y)
  twoHops(x, z) :- ( edge(x, y), oneHop(y, z) )
  threeHops(x, w) :- ( edge(x, y), oneHop(y, z), oneHop(z, w))
  fourHops(x, q) :- ( edge(x, y), oneHop(y, z), oneHop(z, w), oneHop(w, q))

  edge("a", "a") :- ()
  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()

  println("RES=" + twoHops.solve())
}

def reversible(program: Program, engine: ExecutionEngine): Unit = {
  val assign = program.relation[Constant]("assign")
  val assignOp = program.relation[Constant]("assignOp")
  val inverses = program.relation[Constant]("inverses")
  val equiv = program.relation[Constant]("equiv")
  val query = program.relation[Constant]("query")

  val a, b, x, y, cst, f2, f1 = program.variable()

  assignOp("v1", "+", "v0", 1) :- ()
  assignOp("v2", "-", "v1", 1) :- ()
  inverses("+", "-") :- ()

  equiv(a, b) :- assign(a, b)
  equiv(a, b) :- ( equiv(a, x), equiv(x, b) )
//  equiv(a, b) :- ( assignOp(x, f1, a, cst), assignOp(b, f2, y, cst), equiv(x, y), inverses(f1, f2))
  equiv(a, b) :- ( assignOp(x, f1, a, cst), assignOp(b, f2, x, cst), inverses(f1, f2) )

  query(x) :- equiv(x, "v2")

//  println("RES=" + engine.solveCompiled(query.id))
  println("RES=" + query.solve())
}

@main def main = {
  val engine = new SemiNaiveStagedExecutionEngine(new CollectionsStorageManager())
  val program = Program(engine)
  println("staged")
  run(program)
//  reversible(program, engine)

//  given engine2: ExecutionEngine = new NaiveExecutionEngine(new CollectionsStorageManager())
//  val program2 = Program(engine2)
//  println("seminaive")
//  run(program2)
}
