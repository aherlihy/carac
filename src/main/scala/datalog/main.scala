package datalog

import datalog.dsl.MODE.Interpret
import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, NaiveStagedExecutionEngine, SemiNaiveExecutionEngine, SemiNaiveJITStagedExecutionEngine, SemiNaiveStagedExecutionEngine}
import datalog.dsl.{Constant, Program, __}
import datalog.execution.ast.transform.CopyEliminationPass
import datalog.execution.ir.InterpreterContext
import datalog.storage.{CollectionsStorageManager, NS, RelationalStorageManager}

import scala.util.Random
import scala.collection.mutable

def tc(program: Program): Unit = {
  val edge = program.relation[Constant]("edge")
  val path = program.relation[Constant]("path")
  val oneHop = program.relation[Constant]("oneHop")
  val twoHops = program.relation[Constant]("twoHops")
  val threeHops = program.relation[Constant]("threeHops")
  val fourHops = program.relation[Constant]("fourHops")
//  val query = program.relation[Constant]("query")

  val x, y, z, w, q = program.variable()

  path(x, y) :- edge(x, y)
  path(x, z) :- (edge(x, y), path(y, z))

  oneHop(x, y) :- edge(x, y, "red")
  twoHops(x, z) :- ( oneHop(x, y), oneHop(y, z) )
  threeHops(x, w) :- ( oneHop(x, y), oneHop(y, z), oneHop(z, w))
  fourHops(x, q) :- ( oneHop(x, y), oneHop(y, z), oneHop(z, w), oneHop(w, q))


  edge("a", "a", "red") :- ()
  edge("a", "b", "blue") :- ()
  edge("b", "c", "red") :- ()
  edge("c", "d", "blue") :- ()

  println("RES=" + twoHops.solve(mode = Interpret))
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
  println("RES=" + query.solve(mode = Interpret))
}

def func(program: Program) = {
  val eq = program.relation[Constant]("eq")
  val succ = program.relation[Constant]("succ")
  val f = program.relation[Constant]("f")
  val arg = program.relation[Constant]("arg")
  val args = program.relation[Constant]("args")
  val a, b, v, w, i, p, k, any1, any2 = program.variable()


  succ("1", "2") :- ()
  succ("2", "3") :- ()
  succ("3", "4") :- ()

  f("x", "g") :- ()
  f("y", "f") :- ()

  arg("x", "1", "A") :- ()
  arg("x", "2", "B") :- ()
  arg("x", "3", "Z") :- ()

  arg("y", "1", "C") :- ()
  arg("y", "2", "D") :- ()
  arg("y", "3", "W") :- ()

  eq(a, b) :- ( f(v, a), f(w, b), args(v, w, "3") )

  args(v, w, i) :- ( succ(p, i), arg(v, i, k), arg(w, i, k), args(v, w, p) )
  args(v, w, "1") :- ( arg(v, "1", any1), arg(w, "1", any2) )
  val res = eq.solve()

  println(s"RES LEN=${res.size}; res=$res")
}

def isEqual(program: Program): Unit = {
  val equal = program.relation[Constant]("equal")

  val isEqual = program.relation[Constant]("isEqual")

  val succ = program.relation[Constant]("succ")

  val m, n, r, pn, pm = program.variable()


  equal("0", "0", "1") :- ()
  equal(m, n, r) :- ( succ(pm, m) , succ(pn, n), equal(pm, pn, r) )

  isEqual(r) :- equal("5", "7", r)

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
  val res = isEqual.solve()

  println(s"RES LEN=${res.size}; res=$res")
}

def multiJoin(program: Program): Unit = {
  val edge = program.relation[Constant]("edge")
  val path = program.relation[Constant]("path")
  val hops1 = program.relation[Constant]("hops1")
  val hops2_join = program.relation[Constant]("hops2_join")
  val hops3_join = program.relation[Constant]("hops3_join")
  val hops4_join = program.relation[Constant]("hops4_join")
  val hops5_join = program.relation[Constant]("hops5_join")
  val hops6_join = program.relation[Constant]("hops6_join")
  val hops7_join = program.relation[Constant]("hops7_join")
  val hops8_join = program.relation[Constant]("hops8_join")
//  val hops9_join = program.relation[Constant]("hops9_join")
//  val hops10_join = program.relation[Constant]("hops10_join")

  val x, y, z, w, q = program.variable()
  val a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11 = program.variable()

  path(x, y) :- edge(x, y)
  path(x, z) :- (edge(x, y), path(y, z))

  hops1(x, y) :- edge(x, y)
  hops2_join(a1, a3) :-   (hops1(a1, a2), hops1(a2, a3))
  hops3_join(a1, a4) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4))
  hops4_join(a1, a5) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5))
  hops5_join(a1, a6) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6))
  hops6_join(a1, a7) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7))
  hops7_join(a1, a8) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8))
  hops8_join(a1, a9) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8), hops1(a8, a9))
//  hops9_join(a1, a10) :-  (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8), hops1(a8, a9), hops1(a9, a10))
//  hops10_join(a1, a11) :- (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8), hops1(a8, a9), hops1(a9, a10), hops1(a10, a11))


  for i <- 0 until 200 do
    edge(
      Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString,
      Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString
    ) :- ()
  println("RES=" + hops3_join.solve().size)
}

def cliquer(program: Program): Unit = {
  val edge = program.relation[Constant]("edge")

  val leg = program.relation[Constant]("leg")

  val reachable = program.relation[Constant]("reachable")

  val same_clique = program.relation[Constant]("same_clique")

  val X, Y, Z = program.variable()

  leg(X, Z) :- (edge(X, Y), edge(Y, Z))

  reachable(X, Y) :- edge(X, Y)
  reachable(X, Y) :- (edge(X, Z), reachable(Z, Y))
  same_clique(X, Y) :- (reachable(X, Y), reachable(Y, X))

  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()
  edge("d", "a") :- ()

  reachable("e", "f") :- ()

  println("reachable=" + reachable.solve(mode = Interpret))
}

@main def main = {
  //  val engine = new SemiNaiveStagedExecutionEngine(new CollectionsStorageManager())
//  val program = Program(engine)
//  println("staged")
//  run(program)
//  reversible(program, engine)
//  val run = multiJoin

//  println("OLD SN")
//  given engine1: ExecutionEngine = new SemiNaiveExecutionEngine(new CollectionsStorageManager())
//  val program1 = Program(engine1)
//  multiJoin(program1)
//  println("\n\n_______________________\n\n")

  println("STAGED")
  given engine2: ExecutionEngine = new SemiNaiveStagedExecutionEngine(new CollectionsStorageManager())
  val program2 = Program(engine2)
  tc(program2)
  println("\n\n_______________________\n\n")

  println("HIT STAGED")

  given engine3: ExecutionEngine = new SemiNaiveJITStagedExecutionEngine(new CollectionsStorageManager())
  val program3 = Program(engine3)
  tc(program3)
  println("\n\n_______________________\n\n")
}
