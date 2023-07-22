/* Author: Benoit Maillard, adapted from https://github.com/benoitmaillard/tasty-carac */
package datalog.tastyfact.rulesets

import datalog.dsl.Program
import datalog.tastyfact.rulesets.RuleSet

object PointsToRuleSet extends RuleSet {
  def defineRules(program: Program) = {
    val ActualArg = program.relation[String]("ActualArg")
    val ActualReturn = program.relation[String]("ActualReturn")
    val Alloc = program.relation[String]("Alloc")
    val Move = program.relation[String]("Move")
    val FormalArg = program.relation[String]("FormalArg")
    val FormalReturn = program.relation[String]("FormalReturn")
    val HeapType = program.relation[String]("HeapType")
    val LookUp = program.relation[String]("LookUp")
    val ThisVar = program.relation[String]("ThisVar")
    val VCall = program.relation[String]("VCall")
    val Store = program.relation[String]("Store")
    val Load = program.relation[String]("Load")

    val StaticCall = program.relation[String]("StaticCall")
    val StaticLookUp = program.relation[String]("StaticLookUp")

    val VarPointsTo = program.relation[String]("VarPointsTo")
    val CallGraph = program.relation[String]()
    val FldPointsTo = program.relation[String]()
    val InterProcAssign = program.relation[String]()
    val Reachable = program.relation[String]("Reachable")

    val Delegate = program.relation[String]("Delegate")
    val DefinesWith = program.relation[String]("DefinesWith")
    val NotDefines = program.relation[String]("NotDefines")
    val Extends = program.relation[String]("Extends")
    val SuperCall = program.relation[String]("SuperCall")
    val FieldValDef = program.relation[String]("FieldValDef")

    val Refers = program.relation[String]("Refers")
    val Overrides = program.relation[String]("Overrides")
    val TopLevel = program.relation[String]("TopLevel")

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
    Reachable(toMeth ) :-
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

    VarPointsTo
  }
}
