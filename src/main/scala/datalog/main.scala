package datalog

import buildinfo.BuildInfo
import datalog.dsl.*
import datalog.execution.{Backend, CompileSync, ExecutionEngine, Granularity, JITOptions, SortOrder, StagedExecutionEngine, ir, Mode as CaracMode}
import datalog.storage.{CollectionsStorageManager, DuckDBStorageManager, IndexedStorageManager}

import scala.util.Using
import java.nio.file.{FileSystems, Files, Path, Paths}
import scala.quoted.staging

def ackermann(program: Program): String = {
  val succ = program.namedRelation("succ")
  val greaterThanZ = program.namedRelation("greaterThanZ")
  val ack = program.relation[Constant]("ack")
  val N, M, X, Y, Ans, Ans2 = program.variable()

  ack("0", N, Ans) :- succ(N, Ans)

  ack(M, "0", Ans) :- (ack(X, "1", Ans), greaterThanZ(M), succ(X, M))

  ack(M, N, Ans) :- (
    ack(X, Ans2, Ans),
    succ(Y, N),
    succ(X, M),
    greaterThanZ(N),
    ack(M, Y, Ans2),
    greaterThanZ(M)
  )
  ack.name
}
def cba(program: Program): String = {
  val kind = program.relation[Constant]("kind")
  val term = program.relation[Constant]("term")
  val app = program.relation[Constant]("app")
  val lits = program.relation[Constant]("lits")
  val vars = program.relation[Constant]("vars")
  val abs = program.relation[Constant]("abs")
  val ctrl_term = program.relation[Constant]("ctrl_term")
  val ctrl_var = program.relation[Constant]("ctrl_var")
  val data_term = program.relation[Constant]("data_term")
  val data_var = program.relation[Constant]("data_var")

  val i, v, l, x, t1, f, b, a = program.variable()
  val any1, any2 = program.variable()

  kind("Lit") :- ()
  kind("Var") :- ()
  kind("Abs") :- ()
  kind("App") :- ()

  lits(0, "3") :- ()
  lits(1, "2") :- ()

  term(0, "Lit", 0) :- ()
  term(1, "Lit", 1) :- ()

  vars(0, "x") :- ()
  vars(1, "y") :- ()
  vars(2, "z") :- ()

  term(2, "Var", 0) :- ()
  term(3, "Var", 1) :- ()
  term(4, "Var", 2) :- ()

  abs(0, 0, 8) :- ()
  abs(1, 1, 7) :- ()
  abs(2, 2, 4) :- ()

  term(5, "Abs", 0) :- ()
  term(6, "Abs", 1) :- ()
  term(7, "Abs", 2) :- ()

  app(0, 9, 1) :- ()
  app(1, 2, 0) :- ()
  app(2, 5, 6) :- ()

  term(8, "App", 0) :- ()
  term(9, "App", 1) :- ()
  term(10, "App", 2) :- ()

  data_term(i, v) :- (term(i, "Lit", l), lits(l, v))

  data_term(i, v) :- (term(i, "Var", x), data_var(x, v))

  /*x*/ data_term(i, v) :- (term(i, "App", x), data_term(b, v), ctrl_term(t1, f), abs(f, any2, b), app(x, t1, any1))

  /*x*/ data_var(i, v) :- (ctrl_term(a, f), data_term(b, v), abs(f, i, any2), app(any1, a, b))

  ctrl_term(i, v) :- (term(i, "Var", x), ctrl_var(x, v))

  /*x*/ ctrl_term(i, v) :- (term(i, "App", x), ctrl_term(b, v), ctrl_term(t1, f), abs(f, any2, b), app(x, t1, any1))

  ctrl_term(i, v) :- term(i, "Abs", v)

  /*x*/ ctrl_var(i, v) :- (ctrl_term(a, f), ctrl_term(b, v), abs(f, i, any2), app(any1, a, b))
  data_term.name
}
def equal(program: Program): String = {
  val equal = program.relation[Constant]("equal")

  val isEqual = program.relation[Constant]("isEqual")

  val succ = program.relation[Constant]("succ")

  val m, n, r, pn, pm = program.variable()


  equal("0", "0", "1") :- ()
  equal(m, n, r) :- (succ(pn, n), succ(pm, m), equal(pm, pn, r))

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

  isEqual.name
}
def fib(program: Program): String = {
  val f = program.relation[Constant]("f")
  val succ = program.namedRelation("succ")
  val plus_mod = program.namedRelation("plus_mod")

  val i, r, prev, pprev, x, y = program.variable()

  f("0", "0") :- ()
  f("1", "1") :- ()
  f(i, r) :- (succ(pprev, prev), plus_mod(x, y, r), succ(prev, i), f(pprev, y), f(prev, x))
  f.name
}

def prime(program: Program): String = {
  val count_all = program.relation[Constant]("count_all")

  val count_second = program.relation[Constant]("count_second")

  val count_third = program.relation[Constant]("count_third")

  val succ = program.relation[Constant]("succ")

  val n, ppn, pn, pppn, x = program.variable()

  count_second("3") :- ()
  count_second(n) :- (succ(ppn, pn), succ(pn, n), count_second(ppn))

  count_third("3") :- ()
  count_third(n) :- (succ(pppn, ppn), succ(pn, n), count_third(pppn), succ(ppn, pn))

  count_all(x) :- (count_second(x), count_third(x))

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

  count_all.name
}

def tastyslistlib(program: Program): String = {
  val ActualArg = program.namedRelation[String]("ActualArg")
  val ActualReturn = program.namedRelation[String]("ActualReturn")
  val Alloc = program.namedRelation[String]("Alloc")
  val DefinesWith = program.namedRelation[String]("DefinesWith")
  val Extends = program.namedRelation[String]("Extends")
  val FormalArg = program.namedRelation[String]("FormalArg")
  val FormalReturn = program.namedRelation[String]("FormalReturn")
  val HeapType = program.namedRelation[String]("HeapType")
  val NotDefines = program.namedRelation[String]("NotDefines")
  val Reachable = program.namedRelation[String]("Reachable")
  val ThisVar = program.namedRelation[String]("ThisVar")
  val VCall = program.namedRelation[String]("VCall")

  val LookUp = program.relation[String]("LookUp")
  val Move = program.namedRelation[String]("Move")
  val Store = program.relation[String]("Store")
  val Load = program.namedRelation[String]("Load")

  val StaticCall = program.namedRelation[String]("StaticCall")
  val StaticLookUp = program.relation[String]("StaticLookUp")

  val VarPointsTo = program.relation[String]("VarPointsTo")
  val CallGraph = program.relation[String]("CallGraph")
  val FldPointsTo = program.relation[String]("FldPointsTo")
  val InterProcAssign = program.relation[String]("InterProcAssign")

  val Delegate = program.relation[String]("Delegate")
  val SuperCall = program.relation[String]("SuperCall")
  val FieldValDef = program.namedRelation[String]("FieldValDef")

  val Refers = program.relation[String]("Refers")
  val Overrides = program.relation[String]("Overrides")
  val TopLevel = program.relation[String]("TopLevel")

  val varr, heap, meth, to, from, base, baseH, fld, ref = program.variable()
  val toMeth, thiss, thisFrom, invo, sig, inMeth, heapT, m, n, actualFld = program.variable()
  val classA, classB, classC, sigA, sigB, sigC = program.variable()

  VarPointsTo(varr, heap) :- (Reachable(meth), Alloc(varr, heap, meth))
  VarPointsTo(to, heap) :- (Move(to, from), VarPointsTo(from, heap))
  /*x*/ FldPointsTo(baseH, fld, heap) :- (
    VarPointsTo(from, heap),
    VarPointsTo(base, baseH),
    Store(base, fld, from)
  )
  /*x*/ VarPointsTo(to, heap) :- (
    VarPointsTo(base, baseH),
    Load(to, base, fld, inMeth),
    FldPointsTo(baseH, fld, heap)
  )

  /*x*/ Reachable(toMeth) :- (
    LookUp(heapT, sig, toMeth),
    VarPointsTo(base, heap),
    ThisVar(toMeth, thiss),
    VCall(base, sig, invo, inMeth),
    HeapType(heap, heapT),
    Reachable(inMeth),
  )

  /*x*/ VarPointsTo(thiss, heap) :- (
    LookUp(heapT, sig, toMeth),
    VarPointsTo(base, heap),
    ThisVar(toMeth, thiss),
    VCall(base, sig, invo, inMeth),
    HeapType(heap, heapT),
    Reachable(inMeth),
  )

  /*x*/ CallGraph(invo, toMeth) :- (
    LookUp(heapT, sig, toMeth),
    VarPointsTo(base, heap),
    ThisVar(toMeth, thiss),
    VCall(base, sig, invo, inMeth),
    HeapType(heap, heapT),
    Reachable(inMeth)
  )
  CallGraph(invo, toMeth) :- (Reachable(inMeth), StaticCall(toMeth, invo, inMeth))

  // rules for dynamic val
  /*x*/ Reachable(toMeth) :- (
    LookUp(heapT, sig, toMeth),
    VarPointsTo(base, heap),
    ThisVar(toMeth, thiss),
    HeapType(heap, heapT),
    FormalReturn(toMeth, from),
    Load(to, base, sig, inMeth),
    Reachable(inMeth),
  )

  /*x*/ VarPointsTo(thiss, heap) :- (
    LookUp(heapT, sig, toMeth),
    VarPointsTo(base, heap),
    ThisVar(toMeth, thiss),
    HeapType(heap, heapT),
    FormalReturn(toMeth, from),
    Load(to, base, sig, inMeth),
    Reachable(inMeth),
  )

  /*x*/ InterProcAssign(to, from) :- (
    LookUp(heapT, sig, toMeth),
    VarPointsTo(base, heap),
    ThisVar(toMeth, thiss),
    HeapType(heap, heapT),
    FormalReturn(toMeth, from),
    Load(to, base, sig, inMeth),
    Reachable(inMeth),
  )

  /*x*/ InterProcAssign(to, from) :- (
    ActualArg(invo, m, n, from),
    CallGraph(invo, meth),
    FormalArg(meth, m, n, to),
  )

  /*x*/ InterProcAssign(to, from) :- (
    ActualReturn(invo, to),
    FormalReturn(meth, from),
    CallGraph(invo, meth),
  )

  VarPointsTo(to, heap) :- (InterProcAssign(to, from), VarPointsTo(from, heap))

  Reachable(toMeth) :- (StaticCall(toMeth, invo, inMeth), Reachable(inMeth))


  // without negation support, we generate NotDefines facts
  LookUp(classC, sig, meth) :- DefinesWith(classC, sig, meth)

  /*x*/ LookUp(classC, sigA, sigB) :- (
    NotDefines(classC, sigB),
    LookUp(classB, sigA, sigB),
    Extends(classC, classB)
  )
  DefinesWith(classC, sigA, sigC) :- (DefinesWith(classC, sigB, sigC), DefinesWith(classB, sigA, sigB))
  DefinesWith(classC, sigC, sigC) :- DefinesWith(classC, sigB, sigC)

  // with negations we would have something like:
  // LookUp(classC, sig, meth) :- DefinesWith(classC, sig, meth)
  // LookUp(classC, sigA, sigB) :- (LookUp(classB, sigA, sigB), Not(Defines(classC, sigB)), Extends(classC, classB))
  // DefinesWith(classC, sigA, sigC) :- (DefinesWith(classC, sigB, sigC), DefinesWith(classB, sigA, sigB))
  // DefinesWith(classC, sigC, sigC) :- DefinesWith(classC, sigB, sigC)
  // Defines(classC, sigA) :- DefinesWith(classC, sigA, sigC)

  // super calls
  /*x*/ Reachable(toMeth) :- (
    ThisVar(toMeth, thiss),
    ThisVar(inMeth, thisFrom),
    VarPointsTo(thisFrom, heap),
    Reachable(inMeth),
    SuperCall(toMeth, invo, inMeth),
  )

  /*x*/ VarPointsTo(thiss, heap) :- (
    ThisVar(inMeth, thisFrom),
    ThisVar(toMeth, thiss),
    VarPointsTo(thisFrom, heap),
    Reachable(inMeth),
    SuperCall(toMeth, invo, inMeth),
  )

  /*x*/ CallGraph(invo, toMeth) :- (
    ThisVar(inMeth, thisFrom),
    ThisVar(toMeth, thiss),
    VarPointsTo(thisFrom, heap),
    Reachable(inMeth),
    SuperCall(toMeth, invo, inMeth)
  )

  /*x*/ VarPointsTo(to, heap) :- (
    LookUp(heapT, fld, actualFld),
    VarPointsTo(from, heap),
    VarPointsTo(base, baseH),
    HeapType(baseH, heapT),
    Load(to, base, fld, inMeth),
    FieldValDef(actualFld, from),
  )
  VarPointsTo.name
}

def tastyslistlibinverse(program: Program): String = {
  val ActualArg = program.namedRelation[String]("ActualArg")
  val ActualReturn = program.namedRelation[String]("ActualReturn")
  val Alloc = program.namedRelation[String]("Alloc")
  val DefinesWith = program.namedRelation[String]("DefinesWith")
  val Extends = program.namedRelation[String]("Extends")
  val FormalArg = program.namedRelation[String]("FormalArg")
  val FormalReturn = program.namedRelation[String]("FormalReturn")
  val HeapType = program.namedRelation[String]("HeapType")
  val NotDefines = program.namedRelation[String]("NotDefines")
  val Reachable = program.namedRelation[String]("Reachable")
  val ThisVar = program.namedRelation[String]("ThisVar")
  val VCall = program.namedRelation[String]("VCall")

  val LookUp = program.relation[String]("LookUp")
  val Move = program.namedRelation[String]("Move")
  val Store = program.relation[String]("Store")
  val Load = program.namedRelation[String]("Load")

  val StaticCall = program.namedRelation[String]("StaticCall")
  val StaticLookUp = program.relation[String]("StaticLookUp")

  val VarPointsTo = program.relation[String]("VarPointsTo")
  val CallGraph = program.relation[String]("CallGraph")
  val FldPointsTo = program.relation[String]("FldPointsTo")
  val InterProcAssign = program.relation[String]("InterProcAssign")

  val Delegate = program.relation[String]("Delegate")
  val SuperCall = program.relation[String]("SuperCall")
  val FieldValDef = program.namedRelation[String]("FieldValDef")

  val Refers = program.relation[String]("Refers")
  val Overrides = program.relation[String]("Overrides")
  val TopLevel = program.relation[String]("TopLevel")

  val varr, heap, meth, to, from, base, baseH, fld, ref = program.variable()
  val toMeth, thiss, thisFrom, invo, sig, inMeth, heapT, m, n, actualFld = program.variable()
  val classA, classB, classC, sigA, sigB, sigC = program.variable()

  def unoptimizedPointsTo(): Unit = {
    VarPointsTo(varr, heap) :- (
      Reachable(meth),
      Alloc(varr, heap, meth)
    )
    VarPointsTo(to, heap) :- (
      Move(to, from),
      VarPointsTo(from, heap)
    )

    /*x*/ FldPointsTo(baseH, fld, heap) :- (
      VarPointsTo(from, heap),
      VarPointsTo(base, baseH),
      Store(base, fld, from)
    )
    /*x*/ VarPointsTo(to, heap) :- (
      VarPointsTo(base, baseH),
      Load(to, base, fld, inMeth),
      FldPointsTo(baseH, fld, heap)
    )

    /*x*/ Reachable(toMeth) :- (
      VarPointsTo(base, heap),
      LookUp(heapT, sig, toMeth),
      Reachable(inMeth),
      ThisVar(toMeth, thiss),
      VCall(base, sig, invo, inMeth),
      HeapType(heap, heapT),
    )

    /*x*/ VarPointsTo(thiss, heap) :- (
      VarPointsTo(base, heap),
      LookUp(heapT, sig, toMeth),
      Reachable(inMeth),
      ThisVar(toMeth, thiss),
      VCall(base, sig, invo, inMeth),
      HeapType(heap, heapT),
    )

    /*x*/ CallGraph(invo, toMeth) :- (
      VarPointsTo(base, heap),
      LookUp(heapT, sig, toMeth),
      Reachable(inMeth),
      ThisVar(toMeth, thiss),
      VCall(base, sig, invo, inMeth),
      HeapType(heap, heapT),
    )

    CallGraph(invo, toMeth) :- (
      Reachable(inMeth),
      StaticCall(toMeth, invo, inMeth)
    )

    // rules for dynamic val
    /*x*/ Reachable(toMeth) :- (
      VarPointsTo(base, heap),
      LookUp(heapT, sig, toMeth),
      Reachable(inMeth),
      ThisVar(toMeth, thiss),
      HeapType(heap, heapT),
      FormalReturn(toMeth, from),
      Load(to, base, sig, inMeth),
    )

    /*x*/ VarPointsTo(thiss, heap) :- (
      VarPointsTo(base, heap),
      LookUp(heapT, sig, toMeth),
      Reachable(inMeth),
      ThisVar(toMeth, thiss),
      HeapType(heap, heapT),
      FormalReturn(toMeth, from),
      Load(to, base, sig, inMeth),
    )

    /*x*/ InterProcAssign(to, from) :- (
      VarPointsTo(base, heap),
      LookUp(heapT, sig, toMeth),
      Reachable(inMeth),
      ThisVar(toMeth, thiss),
      HeapType(heap, heapT),
      FormalReturn(toMeth, from),
      Load(to, base, sig, inMeth),
    )

    /*x*/ InterProcAssign(to, from) :- (
      ActualArg(invo, m, n, from),
      CallGraph(invo, meth),
      FormalArg(meth, m, n, to),
    )

    /*x*/ InterProcAssign(to, from) :- (
      ActualReturn(invo, to),
      FormalReturn(meth, from),
      CallGraph(invo, meth),
    )

    VarPointsTo(to, heap) :- (
      VarPointsTo(from, heap),
      InterProcAssign(to, from),
    )

    Reachable(toMeth) :- (
      Reachable(inMeth),
      StaticCall(toMeth, invo, inMeth),
    )


    // without negation support, we generate NotDefines facts
    LookUp(classC, sig, meth) :- DefinesWith(classC, sig, meth)

    /*x*/ LookUp(classC, sigA, sigB) :- (
      NotDefines(classC, sigB),
      LookUp(classB, sigA, sigB),
      Extends(classC, classB)
    )
    DefinesWith(classC, sigA, sigC) :- (
      DefinesWith(classB, sigA, sigB),
      DefinesWith(classC, sigB, sigC)
    )
    DefinesWith(classC, sigC, sigC) :- DefinesWith(classC, sigB, sigC)

    // with negations we would have something like:
    // LookUp(classC, sig, meth) :- DefinesWith(classC, sig, meth)
    // LookUp(classC, sigA, sigB) :- (LookUp(classB, sigA, sigB), Not(Defines(classC, sigB)), Extends(classC, classB))
    // DefinesWith(classC, sigA, sigC) :- (DefinesWith(classC, sigB, sigC), DefinesWith(classB, sigA, sigB))
    // DefinesWith(classC, sigC, sigC) :- DefinesWith(classC, sigB, sigC)
    // Defines(classC, sigA) :- DefinesWith(classC, sigA, sigC)

    // super calls
    /*x*/ Reachable(toMeth) :- (
      VarPointsTo(thisFrom, heap),
      Reachable(inMeth),
      ThisVar(toMeth, thiss),
      ThisVar(inMeth, thisFrom),
      SuperCall(toMeth, invo, inMeth),
    )

    /*x*/ VarPointsTo(thiss, heap) :- (
      VarPointsTo(thisFrom, heap),
      Reachable(inMeth),
      ThisVar(inMeth, thisFrom),
      ThisVar(toMeth, thiss),
      SuperCall(toMeth, invo, inMeth),
    )

    /*x*/ CallGraph(invo, toMeth) :- (
      VarPointsTo(thisFrom, heap),
      Reachable(inMeth),
      ThisVar(inMeth, thisFrom),
      ThisVar(toMeth, thiss),
      SuperCall(toMeth, invo, inMeth)
    )

    /*x*/ VarPointsTo(to, heap) :- (
      VarPointsTo(from, heap),
      VarPointsTo(base, baseH),
      LookUp(heapT, fld, actualFld),
      HeapType(baseH, heapT),
      Load(to, base, fld, inMeth),
      FieldValDef(actualFld, from),
    )
    // END SLOWEST -------------------------------------
  }

  unoptimizedPointsTo()

  // Add inverse extension to points-to, for convenience store in a new relation
  val a0, a1, a2, a3 = program.variable()
  val input, output, F, instr, invF, invInstr, ctx, v0, v1, v2, heap2, heap1, arg = program.variable()

  val Equiv = program.relation[String]("Equiv")
  val InverseFns = program.relation[String]("InverseFns")
  val VarEquiv = program.relation[String]("VarEquiv")
  val EquivToOutput = program.relation[String]("EquivToOutput")

  InverseFns("slistlib.Main.main.deserialize", "slistlib.Main.main.serialize") :- ()
  InverseFns("slistlib.Main.main.serialize", "slistlib.Main.main.deserialize") :- ()
  VarEquiv(v0, v1) :- (VarPointsTo(v0, heap), VarPointsTo(v1, heap))

  // "unoptimized" with pt on either side
  //    Equiv(output, input) :- (
  //      VarEquiv(output, v2),
  //      VarEquiv(arg, v1),
  //      VarEquiv(input, v0),
  //      ActualReturn(instr, v2),
  //      ActualReturn(invInstr, v1),
  //      Reachable(ctx),
  //      ActualArg(instr, a0, a1, arg),
  //      ActualArg(invInstr, a2, a3, v0),
  //      InverseFns(F, invF),
  //      StaticCall(F, instr, ctx),
  //      StaticCall(invF, invInstr, ctx),
  //    )

  // "optimized" with pt on either side
  //    Equiv(output, input) :- (
  //      VarEquiv(output, v2),
  //      ActualReturn(instr, v2),
  //      StaticCall(F, instr, ctx),
  //      Reachable(ctx),
  //      ActualArg(instr, a0, a1, arg),
  //      VarEquiv(arg, v1),
  //      ActualReturn(invInstr, v1),
  //      StaticCall(invF, invInstr, ctx),
  //      InverseFns(F, invF),
  //      ActualArg(invInstr, a2, a3, v0),
  //      VarEquiv(input, v0)
  //    )

  // "optimized
  //    Equiv(output, input) :- (
  //      ActualReturn(instr, output),
  //      StaticCall(F, instr, ctx),
  //      Reachable(ctx),
  //      ActualArg(instr, a0, a1, arg),
  //      VarEquiv(arg, v1),
  //      ActualReturn(invInstr, v1),
  //      StaticCall(invF, invInstr, ctx),
  //      InverseFns(F, invF),
  //      ActualArg(invInstr, a2, a3, input),
  //    )

  // "unoptimized"
  Equiv(output, input) :- (
    VarEquiv(arg, v1),
    ActualReturn(instr, output),
    ActualReturn(invInstr, v1),
    Reachable(ctx),
    ActualArg(instr, a0, a1, arg),
    ActualArg(invInstr, a2, a3, input),
    InverseFns(F, invF),
    StaticCall(F, instr, ctx),
    StaticCall(invF, invInstr, ctx),
  )
  EquivToOutput(v0) :- Equiv("slistlib.Main.main.OUTPUT_VAR", v0)
  EquivToOutput.name
}

def tc(program: Program): String = {
  val base = program.namedRelation[Constant]("base")
  val tc = program.relation[Constant]("tc")
  val tcl = program.relation[Constant]("tcl")
  val tcr = program.relation[Constant]("tcr")


  val X, Y, Z, W = program.variable()

  tcl(X, Y) :- (base(X, Y))
  tcl(X, Y) :- (tcl(X, Z), base(Z, Y))

  tcr(X, Y) :- (base(X, Y))
  tcr(X, Y) :- (base(X, Z), tcr(Z, Y))

  tc(X, Y) :- (base(X, Y))
  tc(X, Y) :- (tc(X, Z), tc(Z, Y))

  base("a", "b") :- ()
  base("b", "c") :- ()
  base("c", "d") :- ()
  base("z", "z") :- ()
  tc.name
}

def rqb_andersen(program: Program): String = {
  val addressOf = program.namedRelation("addressOf")
  val assign = program.namedRelation("assign")
  val load = program.namedRelation("load")
  val store = program.namedRelation("store")
  val pointsTo = program.relation[Constant]("pointsTo")

  val x, y, z, w = program.variable()

  pointsTo(y, x) :- addressOf(y, x)

  pointsTo(y, x) :- (assign(y, z), pointsTo(z, x))

  pointsTo(y, w) :- (
    load(y, x),
    pointsTo(x, z),
    pointsTo(z, w)
  )

  pointsTo(z, w) :- (
    store(y, x),
    pointsTo(y, z),
    pointsTo(x, w)
  )
  pointsTo.name
}

@main def main(benchmark: String, back: String) = {
  val engine = if back.contains("Interpreted") then
    new StagedExecutionEngine(new DuckDBStorageManager(), JITOptions(mode = CaracMode.Interpreted))
  else
    val b = back match
      case "quotes" => Backend.Quotes
      case "bytecode" => Backend.Bytecode
      case "lambda" => Backend.Lambda
      case _ => throw new Exception(s"Unknown backend $back")
    val dotty = staging.Compiler.make(Predef.getClass.getClassLoader)
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = b)
    new StagedExecutionEngine(new DuckDBStorageManager(), jo)

  val program = Program(engine)

  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/$benchmark/facts"
  program.loadFromFactDir(factDirectory)

  val toSolve = benchmark match
    case "ackermann" => ackermann(program)
    case "cbaexprvalue" => cba(program)
    case "equal" => equal(program)
    case "fib" => fib(program)
    case "prime" => prime(program)
    case "tastyslistlib" => tastyslistlib(program)
    case "tastyslistlibinverse" => tastyslistlibinverse(program)
    case "tc" => tc(program)
    case "rqb_andersen" => rqb_andersen(program)
    case _ => throw new Exception(s"Unknown benchmark $benchmark")

  val result = program.namedRelation(toSolve).solve()
//  Using(Files.newBufferedWriter(Paths.get("carac-out", benchmark, engine.storageManager.ns(toSolve) + ".csv"))) { writer =>
//    result.foreach(f => writer.write(f.mkString("", "\t", "\n")))
//  }
  println(s"Result = $result")


  engine.precedenceGraph.idbs.foreach(i =>
    val idb = engine.storageManager.ns(i)
    Using(Files.newBufferedWriter(Paths.get("carac-out", benchmark, idb + ".csv"))) { writer =>
      engine.get(idb).foreach(f => writer.write(f.mkString("", "\t", "\n")))
    })
}
