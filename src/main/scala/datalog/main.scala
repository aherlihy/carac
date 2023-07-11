package datalog

import datalog.execution.{ExecutionEngine, JITOptions, SemiNaiveExecutionEngine, StagedExecutionEngine, StagedSnippetExecutionEngine, ir, NaiveExecutionEngine}
import datalog.dsl.{Constant, Program, __}
import datalog.execution.ast.transform.CopyEliminationPass
import datalog.execution.ir.InterpreterContext
import datalog.storage.{DefaultStorageManager, NS, VolcanoStorageManager}

import scala.util.Random
import scala.collection.mutable
import scala.quoted.*

def ackermann(program: Program) = {
  val succ = program.relation[Constant]("succ")
  val greaterThanZ = program.relation[Constant]("greaterThanZ")
  val ack = program.relation[Constant]("ack")
  val N, M, X, Y, Ans, Ans2 = program.variable()

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

  ack("0", N, Ans) :- succ(N, Ans)

  ack(M, "0", Ans) :- (greaterThanZ(M), succ(X, M), ack(X, "1", Ans))

  ack(M, N, Ans) :- (
    greaterThanZ(M),
    greaterThanZ(N),
    succ(X, M),
    succ(Y, N),
    ack(M, Y, Ans2),
    ack(X, Ans2, Ans))

  println("RES=" + ack.solve().size)
}

def tc(program: Program): Unit = {
  val edge = program.relation[Constant]("edge")
  val path = program.relation[Constant]("path")
  val path2a = program.relation[Constant]("path2a")
  val x, y, z = program.variable()

  path(x, y) :- edge(x, y)
  path(x, z) :- (edge(x, y), path(y, z))
  path2a(x, y) :- (path(x, y), edge(y, "a"))

  edge("a", "a", "red") :- ()
  edge("a", "b", "blue") :- ()
  edge("b", "c", "red") :- ()
  edge("c", "d", "blue") :- ()

  println("RES=" + path.solve())
}

def acyclic(program: Program) = {
  val edge = program.relation[Constant]("e")
  val oneHop = program.relation[Constant]("oh")
  val twoHops = program.relation[Constant]("th")
  val queryA = program.relation[Constant]("queryA")
  val queryB = program.relation[Constant]("queryB")

  val x, y, z = program.variable()

  edge("a", "a") :- ()
  edge("a", "b") :- ()
  edge("b", "c") :- ()
  edge("c", "d") :- ()
  oneHop(x, y) :- edge(x, y)
  twoHops(x, z) :- (edge(x, y), oneHop(y, z))
  queryA(x) :- oneHop("a", x)
  queryA(x) :- twoHops("a", x)
  queryB(x) :- (oneHop("a", x), oneHop(x, "c"))

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
  equal(m, n, r) :- (succ(pm, m), succ(pn, n), equal(pm, pn, r))

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
  val hops9_join = program.relation[Constant]("hops9_join")
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
  hops9_join(a1, a10) :-  (hops1(a1, a2), hops8_join(a2, a3), hops7_join(a3, a4), hops1(a4, a5), hops5_join(a5, a6), hops1(a6, a7), hops8_join(a7, a8), hops1(a8, a9), hops1(a9, a10))
//  hops10_join(a1, a11) :- (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8), hops1(a8, a9), hops1(a9, a10), hops1(a10, a11))


  for i <- 0 until 200 do
    edge(
      Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString,
      Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString
    ) :- ()
  println("RES=" + hops5_join.solve().size)
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

  println("reachable=" + same_clique.solve().size)
}

def clique(program: Program): Unit = {
  val edge = program.relation("edge")
  val reachable = program.relation[Constant]("reachable")
  val same_clique = program.relation[Constant]("same_clique")
  val x, y, z = program.variable()

  edge(0,	1) :- ()
  edge(1,	2) :- ()
  edge(2,	3) :- ()
  edge(3,	4) :- ()
  edge(4,	5) :- ()
  edge(5,	0) :- ()
  edge(5,	6) :- ()
  edge(6,	7) :- ()
  edge(7,	8) :- ()
  edge(8,	9) :- ()
  edge(9,	10) :- ()
  edge(10,	7) :- ()
  reachable(x, y) :- edge(x, y)
  reachable(x, y) :- (edge(x, z), reachable(z, y))
  same_clique(x, y) :- (reachable(x, y), reachable(y, x))

  println("result=" + same_clique.solve().size)
}

def input_output(program: Program): Unit = {
  // input, i.e. defined in facts+here, i.e. named
  val InputOutputNumberSymbol = program.relation[Constant]("InputOutputNumberSymbol")
  val InputOutputReceiveNumberSymbol = program.relation[Constant]("InputOutputReceiveNumberSymbol")
  val InputOutputReceiveSendNumberSymbol = program.relation[Constant]("InputOutputReceiveSendNumberSymbol")
  val InputOutputReceiveSendSymbolNumber = program.relation[Constant]("InputOutputReceiveSendSymbolNumber")
  val InputOutputReceiveSymbolNumber = program.relation[Constant]("InputOutputReceiveSymbolNumber")
  val InputOutputSendNumberSymbol = program.relation[Constant]("InputOutputSendNumberSymbol")
  val InputOutputSendSymbolNumber = program.relation[Constant]("InputOutputSendSymbolNumber")
  val InputOutputSymbolNumber = program.relation[Constant]("InputOutputSymbolNumber")

  // input, i.e. defined in facts
  val InputNumberSymbol = program.relation[Constant]("InputNumberSymbol")
  val InputReceiveNumberSymbol = program.relation[Constant]("InputReceiveNumberSymbol")
  val InputReceiveSendNumberSymbol = program.relation[Constant]("InputReceiveSendNumberSymbol")
  val InputReceiveSendSymbolNumber = program.relation[Constant]("InputReceiveSendSymbolNumber")
  val InputReceiveSymbolNumber = program.relation[Constant]("InputReceiveSymbolNumber")
  val InputSendNumberSymbol = program.relation[Constant]("InputSendNumberSymbol")
  val InputSendSymbolNumber = program.relation[Constant]("InputSendSymbolNumber")
  val InputSymbolNumber = program.relation[Constant]("InputSymbolNumber")

  // output, i.e. new relations
  val OutputNumberSymbol = program.relation[Constant]("OutputNumberSymbol")
  val OutputReceiveNumberSymbol = program.relation[Constant]("OutputReceiveNumberSymbol")
  val OutputReceiveSendNumberSymbol = program.relation[Constant]("OutputReceiveSendNumberSymbol")
  val OutputReceiveSendSymbolNumber = program.relation[Constant]("OutputReceiveSendSymbolNumber")
  val OutputReceiveSymbolNumber = program.relation[Constant]("OutputReceiveSymbolNumber")
  val OutputSendNumberSymbol = program.relation[Constant]("OutputSendNumberSymbol")
  val OutputSendSymbolNumber = program.relation[Constant]("OutputSendSymbolNumber")
  val OutputSymbolNumber = program.relation[Constant]("OutputSymbolNumber")

  val ReceiveNumberSymbol = program.relation[Constant]("ReceiveNumberSymbol")
  val ReceiveSendNumberSymbol = program.relation[Constant]("ReceiveSendNumberSymbol")
  val ReceiveSendSymbolNumber = program.relation[Constant]("ReceiveSendSymbolNumber")
  val ReceiveSymbolNumber = program.relation[Constant]("ReceiveSymbolNumber")
  val ReceiverNumberSymbol = program.relation[Constant]("ReceiverNumberSymbol")
  val ReceiverSymbolNumber = program.relation[Constant]("ReceiverSymbolNumber")
  val SendNumberSymbol = program.relation[Constant]("SendNumberSymbol")
  val SendSymbolNumber = program.relation[Constant]("SendSymbolNumber")

  val SenderNumberSymbol = program.relation[Constant]("SenderNumberSymbol")
  val SenderSymbolNumber = program.relation[Constant]("SenderSymbolNumber")

  val x, y = program.variable()

  InputNumberSymbol(1, "b") :- ()
  InputOutputNumberSymbol(1, "b") :- ()
  InputOutputReceiveNumberSymbol(1, "b") :- ()
  InputOutputReceiveNumberSymbol(x, y) :- (SenderNumberSymbol(x, y))
  InputOutputReceiveSendNumberSymbol(1, "b") :- ()
  InputOutputReceiveSendNumberSymbol(x, y) :- (SenderNumberSymbol(x, y))
  InputOutputReceiveSendSymbolNumber("b", 1) :- ()
  InputOutputReceiveSendSymbolNumber(x, y) :- (SenderSymbolNumber(x, y))
  InputOutputReceiveSymbolNumber("b", 1) :- ()
  InputOutputReceiveSymbolNumber(x, y) :- (SenderSymbolNumber(x, y))
  InputOutputSendNumberSymbol(1, "b") :- ()
  InputOutputSendSymbolNumber("b", 1) :- ()
  InputOutputSymbolNumber("b", 1) :- ()
  InputReceiveNumberSymbol(1, "b") :- ()
  InputReceiveNumberSymbol(x, y) :- (SenderNumberSymbol(x, y))
  InputReceiveSendNumberSymbol(1, "b") :- ()
  InputReceiveSendNumberSymbol(x, y) :- (SenderNumberSymbol(x, y))
  InputReceiveSendSymbolNumber("b", 1) :- ()
  InputReceiveSendSymbolNumber(x, y) :- (SenderSymbolNumber(x, y))
  InputReceiveSymbolNumber("b", 1) :- ()
  InputReceiveSymbolNumber(x, y) :- (SenderSymbolNumber(x, y))
  InputSendNumberSymbol(1, "b") :- ()
  InputSendSymbolNumber("b", 1) :- ()
  InputSymbolNumber("b", 1) :- ()
  OutputNumberSymbol(1, "b") :- ()
  OutputReceiveNumberSymbol(1, "b") :- ()
  OutputReceiveNumberSymbol(x, y) :- (SenderNumberSymbol(x, y))
  OutputReceiveSendNumberSymbol(1, "b") :- ()
  OutputReceiveSendNumberSymbol(x, y) :- (SenderNumberSymbol(x, y))
  OutputReceiveSendSymbolNumber("b", 1) :- ()
  OutputReceiveSendSymbolNumber(x, y) :- (SenderSymbolNumber(x, y))
  OutputReceiveSymbolNumber("b", 1) :- ()
  OutputReceiveSymbolNumber(x, y) :- (SenderSymbolNumber(x, y))
  OutputSendNumberSymbol(1, "b") :- ()
  OutputSendSymbolNumber("b", 1) :- ()
  OutputSymbolNumber("b", 1) :- ()
  ReceiveNumberSymbol(1, "b") :- ()
  ReceiveNumberSymbol(x, y) :- SenderNumberSymbol(x, y)
  ReceiveSendNumberSymbol(1, "b") :- ()
  ReceiveSendNumberSymbol(x, y) :- (SenderNumberSymbol(x, y))
  ReceiveSendSymbolNumber("b", 1) :- ()
  ReceiveSendSymbolNumber(x, y) :- (SenderSymbolNumber(x, y))
  ReceiveSymbolNumber("b", 1) :- ()
  ReceiveSymbolNumber(x, y) :- (SenderSymbolNumber(x, y))
  ReceiverNumberSymbol(x, y) :- (InputOutputReceiveSendNumberSymbol(x, y))
  ReceiverNumberSymbol(x, y) :- (InputOutputSendNumberSymbol(x, y))
  ReceiverNumberSymbol(x, y) :- (InputReceiveSendNumberSymbol(x, y))
  ReceiverNumberSymbol(x, y) :- (InputSendNumberSymbol(x, y))
  ReceiverNumberSymbol(x, y) :- (OutputReceiveSendNumberSymbol(x, y))
  ReceiverNumberSymbol(x, y) :- (OutputSendNumberSymbol(x, y))
  ReceiverNumberSymbol(x, y) :- (SendNumberSymbol(x, y))
  ReceiverSymbolNumber(x, y) :- (InputOutputReceiveSendSymbolNumber(x, y))
  ReceiverSymbolNumber(x, y) :- (InputOutputSendSymbolNumber(x, y))
  ReceiverSymbolNumber(x, y) :- (InputReceiveSendSymbolNumber(x, y))
  ReceiverSymbolNumber(x, y) :- (InputSendSymbolNumber(x, y))
  ReceiverSymbolNumber(x, y) :- (OutputReceiveSendSymbolNumber(x, y))
  ReceiverSymbolNumber(x, y) :- (OutputSendSymbolNumber(x, y))
  ReceiverSymbolNumber(x, y) :- (SendSymbolNumber(x, y))
  SendNumberSymbol(1, "b") :- ()
  SendSymbolNumber("b", 1) :- ()

  val res = InputOutputSymbolNumber.solve()

  println(s"RES=${res.size}")
}

def manyRelations(program: Program): Int = {
  val edge = program.relation[Constant]("edge")
  val path = program.relation[Constant]("path")
  val hops1 = program.relation[Constant]("hops1")
  val hops2 = program.relation[Constant]("hops2")
  val hops3 = program.relation[Constant]("hops3")
  val hops4 = program.relation[Constant]("hops4")
  val hops5 = program.relation[Constant]("hops5")
  val hops6 = program.relation[Constant]("hops6")
  val hops7 = program.relation[Constant]("hops7")
  val hops8 = program.relation[Constant]("hops8")
  val hops9 = program.relation[Constant]("hops9")
  val hops10 = program.relation[Constant]("hops10")
  val hops11 = program.relation[Constant]("hops11")
  val hops12 = program.relation[Constant]("hops12")
  val hops13 = program.relation[Constant]("hops13")
  val hops14 = program.relation[Constant]("hops14")
  val hops15 = program.relation[Constant]("hops15")
  val hops16 = program.relation[Constant]("hops16")
  val hops17 = program.relation[Constant]("hops17")
  val hops18 = program.relation[Constant]("hops18")
  val hops19 = program.relation[Constant]("hops19")
  val hops20 = program.relation[Constant]("hops20")

  val x, y, z, w, q = program.variable()

  path(x, y) :- edge(x, y)
  path(x, z) :- (edge(x, y), path(y, z))

  hops1(x, y) :- edge(x, y)
  hops2(x, y) :- (hops1(x, z), hops1(z, y))
  hops3(x, y) :- (hops1(x, z), hops2(z, y))
  hops4(x, y) :- (hops1(x, z), hops3(z, y))
  hops5(x, y) :- (hops1(x, z), hops4(z, y))
  hops6(x, y) :- (hops1(x, z), hops5(z, y))
  hops7(x, y) :- (hops1(x, z), hops6(z, y))
  hops8(x, y) :- (hops1(x, z), hops7(z, y))
  hops9(x, y) :- (hops1(x, z), hops8(z, y))
  hops10(x, y) :- (hops1(x, z), hops9(z, y))
  hops11(x, y) :- (hops1(x, z), hops10(z, y))
  hops12(x, y) :- (hops1(x, z), hops11(z, y))
  hops13(x, y) :- (hops1(x, z), hops12(z, y))
  hops14(x, y) :- (hops1(x, z), hops13(z, y))
  hops15(x, y) :- (hops1(x, z), hops14(z, y))
  hops16(x, y) :- (hops1(x, z), hops15(z, y))
  hops17(x, y) :- (hops1(x, z), hops16(z, y))
  hops18(x, y) :- (hops1(x, z), hops17(z, y))
  hops19(x, y) :- (hops1(x, z), hops18(z, y))
  hops20(x, y) :- (hops1(x, z), hops19(z, y))

  for i <- 0 until 20 do
    edge(
      Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString,
      Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString
    ) :- ()
  val res = hops20.solve()
  hops20.solve()
  hops20.solve()
  hops20.solve()
  hops20.solve()
  println(s"RES=${res.size}")
  hops20.id
}

def anon_var(program: Program) = {
  val Check = program.relation[Constant]("Check")
  val In = program.relation[Constant]("In")
  val A1 = program.relation[Constant]("A1")


  In(7, 10, 9, 8, 7, 8, 4) :- ()
  In(3, 5, 2, 4, 6, 9, 8) :- ()
  In(1, 10, 8, 8, 9, 1, 9) :- ()
  In(8, 7, 6, 5, 3, 6, 3) :- ()
  In(7, 6, 9, 4, 9, 9, 4) :- ()
  In(2, 3, 1, 10, 5, 8, 7) :- ()
  In(8, 8, 10, 10, 7, 6, 4) :- ()
  In(2, 3, 5, 5, 6, 10, 4) :- ()
  In(3, 6, 6, 8, 9, 3, 3) :- ()
  In(6, 9, 7, 7, 5, 3, 3) :- ()
  In(2, 1, 4, 6, 8, 2, 6) :- ()
  In(4, 5, 2, 5, 2, 7, 6) :- ()


  Check(4, 8, 10, 6, 10, 10) :- ()
  Check(8, 5, 8, 2, 1, 3) :- ()
  Check(1, 10, 10, 10, 10, 10) :- ()
  Check(5, 2, 4, 8, 3, 9) :- ()
  Check(9, 5, 6, 6, 3, 8) :- ()
  Check(6, 3, 7, 9, 9, 9) :- ()
  Check(1, 9, 8, 1, 5, 10) :- ()
  Check(9, 4, 5, 7, 5, 8) :- ()
  Check(9, 7, 5, 9, 4, 3) :- ()
  Check(6, 8, 7, 9, 6, 6) :- ()

  val a, b, c, d, e, f, i = program.variable()

  A1(1, i) :- (Check(__, b, c, d, e, f), In(__, b, c, d, e, f, i))
  A1(2, i) :- (Check(a, __, c, d, e, f), In(a, __, c, d, e, f, i))
  A1(3, i) :- (Check(a, b, __, d, e, f), In(a, b, __, d, e, f, i))
  A1(4, i) :- (Check(a, b, c, __, e, f), In(a, b, c, __, e, f, i))
  A1(5, i) :- (Check(a, b, c, d, __, f), In(a, b, c, d, __, f, i))
  A1(6, i) :- (Check(a, b, c, d, e, __), In(a, b, c, d, e, __, i))

  A1(7, i) :- (Check(__, __, c, d, e, f), In(__, __, c, d, e, f, i))
  A1(8, i) :- (Check(a, __, __, d, e, f), In(a, __, __, d, e, f, i))
  A1(9, i) :- (Check(a, b, __, __, e, f), In(a, b, __, __, e, f, i))
  A1(10, i) :- (Check(a, b, c, __, __, f), In(a, b, c, __, __, f, i))
  A1(11, i) :- (Check(a, b, c, d, __, __), In(a, b, c, d, __, __, i))

  A1(12, i) :- (Check(__, __, __, d, e, f), In(__, __, __, d, e, f, i))
  A1(13, i) :- (Check(a, __, __, __, e, f), In(a, __, __, __, e, f, i))
  A1(14, i) :- (Check(a, b, __, __, __, f), In(a, b, __, __, __, f, i))
  A1(15, i) :- (Check(a, b, c, __, __, __), In(a, b, c, __, __, __, i))

  A1(16, i) :- (Check(__, __, __, __, e, f), In(__, __, __, __, e, f, i))
  A1(17, i) :- (Check(a, __, __, __, __, f), In(a, __, __, __, __, f, i))
  A1(18, i) :- (Check(a, b, __, __, __, __), In(a, b, __, __, __, __, i))

  A1(19, i) :- (Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i))

  // po2
  val A2 = program.relation[Constant]("A2")
  A2(1, i) :- (Check(__, b, c, d, e, __), In(__, b, c, d, e, __, i))
  A2(2, i) :- (Check(a, __, c, d, e, __), In(a, __, c, d, e, __, i))
  A2(3, i) :- (Check(a, b, __, __, e, f), In(a, b, __, __, e, f, i))
  A2(4, i) :- (Check(__, __, c, d, __, __), In(__, __, c, d, __, __, i))
  A2(5, i) :- (Check(a, __, __, __, __, f), In(a, __, __, __, __, f, i))
  A2(6, i) :- (Check(__, b, c, d, __, f), In(__, b, c, d, __, f, i))
  A2(7, i) :- (Check(__, b, c, __, e, f), In(__, b, c, __, e, f, i))
  A2(8, i) :- (Check(__, b, __, d, e, f), In(__, b, __, d, e, f, i))
  A2(9, i) :- (Check(__, __, c, d, e, f), In(__, __, c, d, e, f, i))
  A2(10, i) :- (Check(__, b, c, d, __, __), In(__, b, c, d, __, __, i))
  A2(11, i) :- (Check(__, b, c, __, __, f), In(__, b, c, __, __, f, i))
  A2(12, i) :- (Check(__, b, __, __, e, f), In(__, b, __, __, e, f, i))
  A2(13, i) :- (Check(__, __, __, d, e, f), In(__, __, __, d, e, f, i))
  A2(14, i) :- (Check(__, b, c, __, __, __), In(__, b, c, __, __, __, i))
  A2(15, i) :- (Check(__, b, __, __, __, f), In(__, b, __, __, __, f, i))
  A2(16, i) :- (Check(__, __, __, __, e, f), In(__, __, __, __, e, f, i))
  A2(17, i) :- (Check(__, b, c, d, e, f), In(__, b, c, d, e, f, i))
  A2(18, i) :- (Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i))

  // po3
  val A3 = program.relation[Constant]("A3")
  A3(1, i) :- (Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i))
  A3(2, i) :- (Check(a, __, c, d, e, f), In(a, __, c, d, e, f, i))
  A3(3, i) :- (Check(a, __, __, d, e, f), In(a, __, __, d, e, f, i))
  A3(4, i) :- (Check(a, __, __, __, e, f), In(a, __, __, __, e, f, i))
  A3(5, i) :- (Check(a, __, __, __, __, f), In(a, __, __, __, __, f, i))
  A3(6, i) :- (Check(a, __, __, __, __, __), In(a, __, __, __, __, __, i))
  A3(7, i) :- (Check(a, b, __, d, e, f), In(a, b, __, d, e, f, i))
  A3(8, i) :- (Check(a, b, __, __, e, f), In(a, b, __, __, e, f, i))
  A3(9, i) :- (Check(a, b, __, __, __, f), In(a, b, __, __, __, f, i))
  A3(10, i) :- (Check(a, b, __, __, __, __), In(a, b, __, __, __, __, i))
  A3(11, i) :- (Check(a, b, c, __, e, f), In(a, b, c, __, e, f, i))
  A3(12, i) :- (Check(a, b, c, __, __, f), In(a, b, c, __, __, f, i))
  A3(13, i) :- (Check(a, b, c, __, __, __), In(a, b, c, __, __, __, i))
  A3(14, i) :- (Check(a, b, c, d, __, f), In(a, b, c, d, __, f, i))
  A3(15, i) :- (Check(a, b, c, d, __, __), In(a, b, c, d, __, __, i))

  // po4
  val A4 = program.relation[Constant]("A4")
  A4(1, i) :- (Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i))
  A4(2, i) :- (Check(a, b, c, d, __, __), In(a, b, c, d, __, __, i))
  A4(3, i) :- (Check(a, __, c, d, __, __), In(a, __, c, d, __, __, i))
  A4(4, i) :- (Check(a, b, __, d, __, __), In(a, b, __, d, __, __, i))
  A4(5, i) :- (Check(a, b, c, __, __, __), In(a, b, c, __, __, __, i))
  A4(6, i) :- (Check(__, __, c, d, __, __), In(__, __, c, d, __, __, i))
  A4(7, i) :- (Check(a, b, __, __, __, __), In(a, b, __, __, __, __, i))
  A4(8, i) :- (Check(a, __, __, __, __, __), In(a, __, __, __, __, __, i))
  A4(9, i) :- (Check(__, b, __, __, __, __), In(__, b, __, __, __, __, i))

  // po5
  val A5 = program.relation[Constant]("A5")
  A5(1, i) :- (Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i))
  A5(2, i) :- (Check(a, __, __, d, e, f), In(a, __, __, d, e, f, i))
  A5(3, i) :- (Check(a, b, __, d, e, f), In(a, b, __, d, e, f, i))
  A5(4, i) :- (Check(a, __, __, __, e, f), In(a, __, __, __, e, f, i))
  A5(5, i) :- (Check(a, b, __, __, __, f), In(a, b, __, __, __, f, i))
  A5(6, i) :- (Check(a, __, __, __, __, __), In(a, __, __, __, __, __, i))
  A5(7, i) :- (Check(a, b, __, d, e, f), In(a, b, __, d, e, f, i))
  A5(8, i) :- (Check(a, b, __, __, e, f), In(a, b, __, __, e, f, i))
  A5(9, i) :- (Check(a, b, __, __, __, f), In(a, b, __, __, __, f, i))
  A5(10, i) :- (Check(a, b, __, __, __, __), In(a, b, __, __, __, __, i))
  A5(11, i) :- (Check(a, b, c, __, e, f), In(a, b, c, __, e, f, i))
  A5(12, i) :- (Check(a, __, c, __, __, f), In(a, __, c, __, __, f, i))
  A5(13, i) :- (Check(a, b, c, __, __, __), In(a, b, c, __, __, __, i))
  A5(14, i) :- (Check(a, b, c, d, __, f), In(a, b, c, d, __, f, i))
  A5(15, i) :- (Check(a, __, c, d, __, __), In(a, __, c, d, __, __, i))

  val res = A1.solve()
  println(s"RES=${res.size}")
}

def scratch(program: Program) =
  val a1 = program.relation[Constant]("a1")
  val a2 = program.relation[Constant]("a2")
  val b = program.relation[Constant]("b")
  val c = program.relation[Constant]("c")
  val e = program.relation[Constant]("e")

  val x, y, z = program.variable()

  e(1, 2, 3) :- ()
  e(11, 22, 33) :- ()
  e(111, 222, 333) :- ()

  b(x) :- e(1, x, 3)

  c(x, y) :- (e(x, y, 3), b(x))

  a1(x, y, z, 1) :- (e(y, z, 3), b(x, y), c(y, z))
  a2(x, y, z, 1) :- (e(y, z, 3), b(x, y), c(y, z))

  println(a2.solve())

def isAfter(program: Program) =
  val edge = program.relation[Constant]("edge")
  val isBefore = program.relation[Constant]("isBefore")
  val isAfter = program.relation[Constant]("isAfter")

  val x, y, z = program.variable()

  edge("A", "B") :- ()
  edge("A", "D") :- ()
  edge("A", "E") :- ()
  edge("B", "C") :- ()
  edge("C", "D") :- ()
  edge("C", "E") :- ()
  edge("D", "E") :- ()
  edge("E", "F") :- ()
  edge("F", "G") :- ()
  edge("F", "H") :- ()
  edge("F", "I") :- ()
  edge("G", "J") :- ()
  edge("H", "K") :- ()
  edge("I", "L") :- ()
  edge("J", "M") :- ()
  edge("K", "M") :- ()
  edge("L", "M") :- ()

  isBefore(x, y) :- edge(x, y)
  isBefore(x, y) :- (isBefore(x, z), isBefore(z, y))

  isAfter(x, y) :- edge(y, x)
  isAfter(x, y) :- (isAfter(z, x), isAfter(y, z))

  println(isAfter.solve().size)

def pointstofun(program: Program) = {
  val ActualArg = program.relation[String]("ActualArg")
  val ActualReturn = program.relation[String]("ActualReturn")
  val Alloc = program.relation[String]("Alloc")
  val DefinesWith = program.relation[String]("DefinesWith")
  val Extends = program.relation[String]("Extends")
  val FormalArg = program.relation[String]("FormalArg")
  val FormalReturn = program.relation[String]("FormalReturn")
  val HeapType = program.relation[String]("HeapType")
  val NotDefines = program.relation[String]("NotDefines")
  val Reachable = program.relation[String]("Reachable")
  val ThisVar = program.relation[String]("ThisVar")
  val VCall = program.relation[String]("VCall")

  val LookUp = program.relation[String]("LookUp")
  val Store = program.relation[String]("Store")
  val Load = program.relation[String]("Load")

  val Move = program.relation[String]("Move")
  val StaticCall = program.relation[String]("StaticCall")
  val StaticLookUp = program.relation[String]("StaticLookUp")

  val VarPointsTo = program.relation[String]("VarPointsTo")
  val CallGraph = program.relation[String]()
  val FldPointsTo = program.relation[String]()
  val InterProcAssign = program.relation[String]()

  val Delegate = program.relation[String]("Delegate")
  val SuperCall = program.relation[String]("SuperCall")
  val FieldValDef = program.relation[String]("FieldValDef")

  val Refers = program.relation[String]("Refers")
  val Overrides = program.relation[String]("Overrides")
  val TopLevel = program.relation[String]("TopLevel")


  ActualArg("instr#1", "list0", "arg0", "pointstofun.Main.writeReplace.temp#1") :- ()
  ActualArg("instr#8", "list0", "arg0", "pointstofun.PointsToFun.fun1.a1") :- ()
  ActualArg("instr#10", "list0", "arg0", "pointstofun.PointsToFun.fun2.a2") :- ()

  ActualReturn("instr#4", "pointstofun.Main.main.temp") :- ()
  ActualReturn("instr#8", "pointstofun.PointsToFun.fun1.b1") :- ()
  ActualReturn("instr#10", "pointstofun.PointsToFun.fun2.b2") :- ()

  Alloc("pointstofun.Main.writeReplace.temp", "new[scala.runtime.ModuleSerializationProxy]#0", "pointstofun.Main.writeReplace") :- ()
  Alloc("pointstofun.Main.main.p", "new[pointstofun.PointsToFun]#1", "pointstofun.Main.main") :- ()
  Alloc("pointstofun.PointsToFun.fun1.a1", "new[pointstofun.A1]#2", "pointstofun.PointsToFun.fun1") :- ()
  Alloc("pointstofun.PointsToFun.fun2.a2", "new[pointstofun.A2]#3", "pointstofun.PointsToFun.fun2") :- ()

  DefinesWith("pointstofun.Main", "pointstofun.Main.main", "pointstofun.Main.main") :- ()
  DefinesWith("pointstofun.Main", "pointstofun.Main.writeReplace", "pointstofun.Main.writeReplace") :- ()
  DefinesWith("pointstofun.Main", "pointstofun.Main.<init>", "pointstofun.Main.<init>") :- ()
  DefinesWith("pointstofun.A", "pointstofun.A.<init>", "pointstofun.A.<init>") :- ()
  DefinesWith("pointstofun.PointsToFun", "pointstofun.PointsToFun.fun1", "pointstofun.PointsToFun.fun1") :- ()
  DefinesWith("pointstofun.PointsToFun", "pointstofun.PointsToFun.fun2", "pointstofun.PointsToFun.fun2") :- ()
  DefinesWith("pointstofun.PointsToFun", "pointstofun.PointsToFun.<init>", "pointstofun.PointsToFun.<init>") :- ()
  DefinesWith("pointstofun.PointsToFun", "pointstofun.PointsToFun.id", "pointstofun.PointsToFun.id") :- ()
  DefinesWith("pointstofun.A1", "pointstofun.A1.<init>", "pointstofun.A1.<init>") :- ()
  DefinesWith("pointstofun.A2", "pointstofun.A2.<init>", "pointstofun.A2.<init>") :- ()

  Extends("pointstofun.Main", "java.lang.Object") :- ()
  Extends("pointstofun.A", "java.lang.Object") :- ()
  Extends("pointstofun.PointsToFun", "java.lang.Object") :- ()
  Extends("pointstofun.A1", "pointstofun.A") :- ()
  Extends("pointstofun.A2", "pointstofun.A") :- ()

  FormalArg("pointstofun.PointsToFun.id", "list0", "arg0", "pointstofun.PointsToFun.id.a") :- ()

  FormalReturn("pointstofun.Main.writeReplace", "pointstofun.Main.writeReplace.temp") :- ()
  FormalReturn("pointstofun.Main.main", "pointstofun.Main.main.temp") :- ()
  FormalReturn("pointstofun.PointsToFun.fun1", "pointstofun.PointsToFun.fun1.temp") :- ()
  FormalReturn("pointstofun.PointsToFun.fun2", "pointstofun.PointsToFun.fun2.temp") :- ()
  FormalReturn("pointstofun.PointsToFun.id", "pointstofun.PointsToFun.id.a") :- ()

  HeapType("new[scala.runtime.ModuleSerializationProxy]#0", "scala.runtime.ModuleSerializationProxy") :- ()
  HeapType("new[pointstofun.PointsToFun]#1", "pointstofun.PointsToFun") :- ()
  HeapType("new[pointstofun.A1]#2", "pointstofun.A1") :- ()
  HeapType("new[pointstofun.A2]#3", "pointstofun.A2") :- ()

  NotDefines("pointstofun.Main", "scala.Any.asInstanceOf") :- ()
  NotDefines("pointstofun.Main", "scala.Any.equals") :- ()
  NotDefines("pointstofun.Main", "scala.Any.isInstanceOf") :- ()
  NotDefines("pointstofun.Main", "scala.Any.##") :- ()
  NotDefines("pointstofun.Main", "scala.Any.$asInstanceOf$") :- ()
  NotDefines("pointstofun.Main", "scala.Any.getClass") :- ()
  NotDefines("pointstofun.Main", "scala.Any.!=") :- ()
  NotDefines("pointstofun.Main", "scala.Any.==") :- ()
  NotDefines("pointstofun.Main", "scala.Any.$isInstanceOf$") :- ()
  NotDefines("pointstofun.Main", "scala.Any.asInstanceOf") :- ()
  NotDefines("pointstofun.Main", "scala.Any.equals") :- ()
  NotDefines("pointstofun.Main", "scala.Any.isInstanceOf") :- ()
  NotDefines("pointstofun.Main", "scala.Any.##") :- ()
  NotDefines("pointstofun.Main", "scala.Any.$asInstanceOf$") :- ()
  NotDefines("pointstofun.Main", "scala.Any.getClass") :- ()
  NotDefines("pointstofun.Main", "scala.Any.!=") :- ()
  NotDefines("pointstofun.Main", "scala.Any.==") :- ()
  NotDefines("pointstofun.Main", "scala.Any.$isInstanceOf$") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.finalize") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.notifyAll") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.equals") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.ne") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.getClass") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.notify") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.hashCode") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.<init>") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.toString") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.clone") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.wait") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.wait#1") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.wait#2") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.synchronized") :- ()
  NotDefines("pointstofun.Main", "java.lang.Object.eq") :- ()
  NotDefines("pointstofun.A", "scala.Any.asInstanceOf") :- ()
  NotDefines("pointstofun.A", "scala.Any.equals") :- ()
  NotDefines("pointstofun.A", "scala.Any.isInstanceOf") :- ()
  NotDefines("pointstofun.A", "scala.Any.##") :- ()
  NotDefines("pointstofun.A", "scala.Any.$asInstanceOf$") :- ()
  NotDefines("pointstofun.A", "scala.Any.getClass") :- ()
  NotDefines("pointstofun.A", "scala.Any.!=") :- ()
  NotDefines("pointstofun.A", "scala.Any.==") :- ()
  NotDefines("pointstofun.A", "scala.Any.$isInstanceOf$") :- ()
  NotDefines("pointstofun.A", "scala.Any.asInstanceOf") :- ()
  NotDefines("pointstofun.A", "scala.Any.equals") :- ()
  NotDefines("pointstofun.A", "scala.Any.isInstanceOf") :- ()
  NotDefines("pointstofun.A", "scala.Any.##") :- ()
  NotDefines("pointstofun.A", "scala.Any.$asInstanceOf$") :- ()
  NotDefines("pointstofun.A", "scala.Any.getClass") :- ()
  NotDefines("pointstofun.A", "scala.Any.!=") :- ()
  NotDefines("pointstofun.A", "scala.Any.==") :- ()
  NotDefines("pointstofun.A", "scala.Any.$isInstanceOf$") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.finalize") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.notifyAll") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.equals") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.ne") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.getClass") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.notify") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.hashCode") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.<init>") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.toString") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.clone") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.wait") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.wait#1") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.wait#2") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.synchronized") :- ()
  NotDefines("pointstofun.A", "java.lang.Object.eq") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.asInstanceOf") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.equals") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.isInstanceOf") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.##") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.$asInstanceOf$") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.getClass") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.!=") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.==") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.$isInstanceOf$") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.asInstanceOf") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.equals") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.isInstanceOf") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.##") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.$asInstanceOf$") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.getClass") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.!=") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.==") :- ()
  NotDefines("pointstofun.PointsToFun", "scala.Any.$isInstanceOf$") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.finalize") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.notifyAll") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.equals") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.ne") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.getClass") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.notify") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.hashCode") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.<init>") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.toString") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.clone") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.wait") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.wait#1") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.wait#2") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.synchronized") :- ()
  NotDefines("pointstofun.PointsToFun", "java.lang.Object.eq") :- ()
  NotDefines("pointstofun.A1", "scala.Any.asInstanceOf") :- ()
  NotDefines("pointstofun.A1", "scala.Any.equals") :- ()
  NotDefines("pointstofun.A1", "scala.Any.isInstanceOf") :- ()
  NotDefines("pointstofun.A1", "scala.Any.##") :- ()
  NotDefines("pointstofun.A1", "scala.Any.$asInstanceOf$") :- ()
  NotDefines("pointstofun.A1", "scala.Any.getClass") :- ()
  NotDefines("pointstofun.A1", "scala.Any.!=") :- ()
  NotDefines("pointstofun.A1", "scala.Any.==") :- ()
  NotDefines("pointstofun.A1", "scala.Any.$isInstanceOf$") :- ()
  NotDefines("pointstofun.A1", "scala.Any.asInstanceOf") :- ()
  NotDefines("pointstofun.A1", "scala.Any.equals") :- ()
  NotDefines("pointstofun.A1", "scala.Any.isInstanceOf") :- ()
  NotDefines("pointstofun.A1", "scala.Any.##") :- ()
  NotDefines("pointstofun.A1", "scala.Any.$asInstanceOf$") :- ()
  NotDefines("pointstofun.A1", "scala.Any.getClass") :- ()
  NotDefines("pointstofun.A1", "scala.Any.!=") :- ()
  NotDefines("pointstofun.A1", "scala.Any.==") :- ()
  NotDefines("pointstofun.A1", "scala.Any.$isInstanceOf$") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.finalize") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.notifyAll") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.equals") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.ne") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.getClass") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.notify") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.hashCode") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.<init>") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.toString") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.clone") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.wait") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.wait#1") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.wait#2") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.synchronized") :- ()
  NotDefines("pointstofun.A1", "java.lang.Object.eq") :- ()
  NotDefines("pointstofun.A1", "pointstofun.A.<init>") :- ()
  NotDefines("pointstofun.A2", "scala.Any.asInstanceOf") :- ()
  NotDefines("pointstofun.A2", "scala.Any.equals") :- ()
  NotDefines("pointstofun.A2", "scala.Any.isInstanceOf") :- ()
  NotDefines("pointstofun.A2", "scala.Any.##") :- ()
  NotDefines("pointstofun.A2", "scala.Any.$asInstanceOf$") :- ()
  NotDefines("pointstofun.A2", "scala.Any.getClass") :- ()
  NotDefines("pointstofun.A2", "scala.Any.!=") :- ()
  NotDefines("pointstofun.A2", "scala.Any.==") :- ()
  NotDefines("pointstofun.A2", "scala.Any.$isInstanceOf$") :- ()
  NotDefines("pointstofun.A2", "scala.Any.asInstanceOf") :- ()
  NotDefines("pointstofun.A2", "scala.Any.equals") :- ()
  NotDefines("pointstofun.A2", "scala.Any.isInstanceOf") :- ()
  NotDefines("pointstofun.A2", "scala.Any.##") :- ()
  NotDefines("pointstofun.A2", "scala.Any.$asInstanceOf$") :- ()
  NotDefines("pointstofun.A2", "scala.Any.getClass") :- ()
  NotDefines("pointstofun.A2", "scala.Any.!=") :- ()
  NotDefines("pointstofun.A2", "scala.Any.==") :- ()
  NotDefines("pointstofun.A2", "scala.Any.$isInstanceOf$") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.finalize") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.notifyAll") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.equals") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.ne") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.getClass") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.notify") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.hashCode") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.<init>") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.toString") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.clone") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.wait") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.wait#1") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.wait#2") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.synchronized") :- ()
  NotDefines("pointstofun.A2", "java.lang.Object.eq") :- ()
  NotDefines("pointstofun.A2", "pointstofun.A.<init>") :- ()

  Reachable("pointstofun.Main.main") :- ()

  ThisVar("pointstofun.Main.<init>", "pointstofun.Main.<init>.this") :- ()
  ThisVar("pointstofun.Main.writeReplace", "pointstofun.Main.writeReplace.this") :- ()
  ThisVar("pointstofun.Main.main", "pointstofun.Main.main.this") :- ()
  ThisVar("pointstofun.A.<init>", "pointstofun.A.<init>.this") :- ()
  ThisVar("pointstofun.PointsToFun.<init>", "pointstofun.PointsToFun.<init>.this") :- ()
  ThisVar("pointstofun.PointsToFun.fun1", "pointstofun.PointsToFun.fun1.this") :- ()
  ThisVar("pointstofun.PointsToFun.fun2", "pointstofun.PointsToFun.fun2.this") :- ()
  ThisVar("pointstofun.PointsToFun.id", "pointstofun.PointsToFun.id.this") :- ()
  ThisVar("pointstofun.A1.<init>", "pointstofun.A1.<init>.this") :- ()
  ThisVar("pointstofun.A2.<init>", "pointstofun.A2.<init>.this") :- ()

  VCall("pointstofun.Main.<init>.this", "java.lang.Object.<init>", "instr#0", "pointstofun.Main.<init>") :- ()
  VCall("pointstofun.Main.writeReplace.temp", "scala.runtime.ModuleSerializationProxy.<init>", "instr#1", "pointstofun.Main.writeReplace") :- ()
  VCall("pointstofun.Main.main.p", "pointstofun.PointsToFun.<init>", "instr#2", "pointstofun.Main.main") :- ()
  VCall("pointstofun.Main.main.p", "pointstofun.PointsToFun.fun1", "instr#3", "pointstofun.Main.main") :- ()
  VCall("pointstofun.Main.main.p", "pointstofun.PointsToFun.fun2", "instr#4", "pointstofun.Main.main") :- ()
  VCall("pointstofun.A.<init>.this", "java.lang.Object.<init>", "instr#5", "pointstofun.A.<init>") :- ()
  VCall("pointstofun.PointsToFun.<init>.this", "java.lang.Object.<init>", "instr#6", "pointstofun.PointsToFun.<init>") :- ()
  VCall("pointstofun.PointsToFun.fun1.a1", "pointstofun.A1.<init>", "instr#7", "pointstofun.PointsToFun.fun1") :- ()
  VCall("pointstofun.PointsToFun.fun1.this", "pointstofun.PointsToFun.id", "instr#8", "pointstofun.PointsToFun.fun1") :- ()
  VCall("pointstofun.PointsToFun.fun2.a2", "pointstofun.A2.<init>", "instr#9", "pointstofun.PointsToFun.fun2") :- ()
  VCall("pointstofun.PointsToFun.fun2.this", "pointstofun.PointsToFun.id", "instr#10", "pointstofun.PointsToFun.fun2") :- ()
  VCall("pointstofun.A1.<init>.this", "pointstofun.A.<init>", "instr#11", "pointstofun.A1.<init>") :- ()
  VCall("pointstofun.A2.<init>.this", "pointstofun.A.<init>", "instr#12", "pointstofun.A2.<init>") :- ()

  val varr, heap, meth, to, from, base, baseH, fld, ref = program.variable()
  val toMeth, thiss, thisFrom, invo, sig, inMeth, heapT, m, n, actualFld = program.variable()
  val classA, classB, classC, sigA, sigB, sigC = program.variable()

  VarPointsTo(varr, heap) :- (Reachable(meth), Alloc(varr, heap, meth))
  VarPointsTo(to, heap) :- (Move(to, from), VarPointsTo(from, heap))
  FldPointsTo(baseH, fld, heap) :- (Store(base, fld, from), VarPointsTo(from, heap), VarPointsTo(base, baseH))
  VarPointsTo(to, heap) :- (Load(to, base, fld, inMeth), VarPointsTo(base, baseH), FldPointsTo(baseH, fld, heap))

  Reachable(toMeth) :-
    (VCall(base, sig, invo, inMeth), Reachable(inMeth),
      VarPointsTo(base, heap),
      HeapType(heap, heapT), LookUp(heapT, sig, toMeth),
      ThisVar(toMeth, thiss))

  VarPointsTo(thiss, heap) :-
    (VCall(base, sig, invo, inMeth), Reachable(inMeth),
      VarPointsTo(base, heap),
      HeapType(heap, heapT), LookUp(heapT, sig, toMeth),
      ThisVar(toMeth, thiss))

  CallGraph(invo, toMeth) :-
    (VCall(base, sig, invo, inMeth), Reachable(inMeth),
      VarPointsTo(base, heap),
      HeapType(heap, heapT), LookUp(heapT, sig, toMeth),
      ThisVar(toMeth, thiss))

  // rules for dynamic val
  Reachable(toMeth) :-
    (Load(to, base, sig, inMeth), Reachable(inMeth),
      VarPointsTo(base, heap),
      HeapType(heap, heapT), LookUp(heapT, sig, toMeth),
      ThisVar(toMeth, thiss),
      FormalReturn(toMeth, from))

  VarPointsTo(thiss, heap) :-
    (Load(to, base, sig, inMeth), Reachable(inMeth),
      VarPointsTo(base, heap),
      HeapType(heap, heapT), LookUp(heapT, sig, toMeth),
      ThisVar(toMeth, thiss),
      FormalReturn(toMeth, from))

  InterProcAssign(to, from) :-
    (Load(to, base, sig, inMeth), Reachable(inMeth),
      VarPointsTo(base, heap),
      HeapType(heap, heapT), LookUp(heapT, sig, toMeth),
      ThisVar(toMeth, thiss),
      FormalReturn(toMeth, from))

  InterProcAssign(to, from) :- (CallGraph(invo, meth), FormalArg(meth, m, n, to), ActualArg(invo, m, n, from))

  InterProcAssign(to, from) :- (CallGraph(invo, meth), FormalReturn(meth, from), ActualReturn(invo, to))

  VarPointsTo(to, heap) :- (InterProcAssign(to, from), VarPointsTo(from, heap))

  Reachable(toMeth) :- (StaticCall(toMeth, invo, inMeth), Reachable(inMeth))

  CallGraph(invo, toMeth) :- (StaticCall(toMeth, invo, inMeth), Reachable(inMeth))

  // without negation support, we generate NotDefines facts
  LookUp(classC, sig, meth) :- DefinesWith(classC, sig, meth)
  LookUp(classC, sigA, sigB) :- (LookUp(classB, sigA, sigB), NotDefines(classC, sigB), Extends(classC, classB))
  DefinesWith(classC, sigA, sigC) :- (DefinesWith(classC, sigB, sigC), DefinesWith(classB, sigA, sigB))
  DefinesWith(classC, sigC, sigC) :- DefinesWith(classC, sigB, sigC)

  // with negations we would have something like:
  // LookUp(classC, sig, meth) :- DefinesWith(classC, sig, meth)
  // LookUp(classC, sigA, sigB) :- (LookUp(classB, sigA, sigB), Not(Defines(classC, sigB)), Extends(classC, classB))
  // DefinesWith(classC, sigA, sigC) :- (DefinesWith(classC, sigB, sigC), DefinesWith(classB, sigA, sigB))
  // DefinesWith(classC, sigC, sigC) :- DefinesWith(classC, sigB, sigC)
  // Defines(classC, sigA) :- DefinesWith(classC, sigA, sigC)

  // super calls
  Reachable(toMeth) :-
    (SuperCall(toMeth, invo, inMeth), Reachable(inMeth),
      ThisVar(inMeth, thisFrom), VarPointsTo(thisFrom, heap),
      ThisVar(toMeth, thiss))

  VarPointsTo(thiss, heap) :-
    (SuperCall(toMeth, invo, inMeth), Reachable(inMeth),
      ThisVar(inMeth, thisFrom), VarPointsTo(thisFrom, heap),
      ThisVar(toMeth, thiss))

  CallGraph(invo, toMeth) :-
    (SuperCall(toMeth, invo, inMeth), Reachable(inMeth),
      ThisVar(inMeth, thisFrom), VarPointsTo(thisFrom, heap),
      ThisVar(toMeth, thiss))

  VarPointsTo(to, heap) :-
    (Load(to, base, fld, inMeth), VarPointsTo(base, baseH),
      HeapType(baseH, heapT), LookUp(heapT, fld, actualFld),
      FieldValDef(actualFld, from),
      VarPointsTo(from, heap))

  println(program.ee.storageManager.toString())

  println(s"RES=${VarPointsTo.solve().size}")
}

def stratified(program: Program) = {
  val b = program.relation[Constant]("e")
  val p1 = program.relation[Constant]("p1")
  val p2 = program.relation[Constant]("p2")
  val p3 = program.relation[Constant]("p3")
  val q = program.relation[Constant]("q")
  val r = program.relation[Constant]("r")
  val x, y, z = program.variable()

  // p1, p2 and p3 are in the same stratum
  p1(x, y, z) :- b(x, y, z)
  p1(x, y, z) :- p2(y, z, x)
  p2(x, y, z) :- b(x, y, z)
  p2(x, y, z) :- p3(y, z, x)
  p3(x, y, z) :- b(x, y, z)
  p3(x, y, z) :- p1(y, z, x)

  q(x, y, z) :- (p1(x, y, z), p2(x, y, z), p3(x, y, z))

  r(x, y, z) :- (q(x, y, z), p1(x, y, z), p2(x, y, z), p3(x, y, z))


  b("a", "b", "c") :- ()
  b("x", "y", "z") :- ()

  println(r.solve())
}
@main def main = {
//  val stratifiedA = false
//  println("NAIVE")
//  given engine0: ExecutionEngine = new NaiveExecutionEngine(new DefaultStorageManager(), stratified = stratifiedA)
//  val program0 = Program(engine0)
//  stratified(program0)
//  println("\n\n_______________________\n\n")

  val dotty = staging.Compiler.make(getClass.getClassLoader)
  println("SEMINAIVE:")
  given engine1: ExecutionEngine = new SemiNaiveExecutionEngine(new DefaultStorageManager())
  val program1 = Program(engine1)
  clique(program1)
  println("\n\n_______________________\n\n")

//    val jo2 = JITOptions(ir.OpCode.OTHER, dotty, aot = false, block = true)
//    println("INTERP")
//    given engine3a: ExecutionEngine = new StagedExecutionEngine(new DefaultStorageManager(), jo2)

//    val program3a = Program(engine3a)
//    stratified(program3a)
//    println("\n\n_______________________\n\n")
//
//  val jo3 = JITOptions(ir.OpCode.EVAL_RULE_SN, dotty, aot = false, block = true, sortOrder = (0, 0, 0), stratified = stratifiedA)
//  println("JIT")
//  given engine3: ExecutionEngine = new StagedExecutionEngine(new DefaultStorageManager(), jo3)
//  val program3 = Program(engine3)
//  stratified(program3)
//  println("\n\n_______________________\n\n")
//
//  println("JIT Snippet")
//  val engine4: ExecutionEngine = new StagedSnippetExecutionEngine(new DefaultStorageManager(), jo3)
//  val program4 = Program(engine4)
//  stratified(program4)
//  println("\n\n_______________________\n\n")

//  println("JIT STAGED: aot EvalSN")
//  val engine5: ExecutionEngine = new JITStagedExecutionEngine(new DefaultStorageManager(), ir.OpCode.EVAL_SN, true, true)
//  val program5 = Program(engine5)
//  manyRelations(program5)
//  println("\n\n_______________________\n\n")

//  val jo6 = JITOptions(ir.OpCode.PROGRAM, dotty, aot = false, block = true, sortOrder = (0, 0, 0))
//  println("COMPILE")
//  given engine6: ExecutionEngine = new StagedExecutionEngine(new DefaultStorageManager(), jo6)
//  val program6 = Program(engine6)
//  stratified(program6)
//  println("\n\n_______________________\n\n")
}
