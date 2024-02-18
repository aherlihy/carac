package test.examples.tastyslistlib

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}

class tastyslistlib_worst_test extends ExampleTestGenerator(
  "tastyslistlib",
  Set(Tags.Naive, Tags.Volcano),
  Set(Tags.Slow, Tags.CI)
) with tastyslistlib_worst

trait tastyslistlib_worst {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlib/facts"
  val toSolve = "VarPointsTo"

  def pretest(program: Program): Unit = {
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

//    val Delegate = program.relation[String]("Delegate")
    val SuperCall = program.relation[String]("SuperCall")
    val FieldValDef = program.namedRelation[String]("FieldValDef")

//    val Refers = program.relation[String]("Refers")
//    val Overrides = program.relation[String]("Overrides")
//    val TopLevel = program.relation[String]("TopLevel")

    val varr, heap, meth, to, from, base, baseH, fld, ref = program.variable()
    val toMeth, thiss, thisFrom, invo, sig, inMeth, heapT, m, n, actualFld = program.variable()
    val classA, classB, classC, sigA, sigB, sigC = program.variable()

    VarPointsTo(varr, heap) :- (Reachable(meth), Alloc(varr, heap, meth))
    VarPointsTo(to, heap) :- (Move(to, from), VarPointsTo(from, heap))
    /*x*/FldPointsTo(baseH, fld, heap) :- (
      VarPointsTo(from, heap),
      VarPointsTo(base, baseH),
      Store(base, fld, from)
    )
    /*x*/VarPointsTo(to, heap) :- (
      VarPointsTo(base, baseH),
      Load(to, base, fld, inMeth),
      FldPointsTo(baseH, fld, heap)
    )

    /*x*/Reachable(toMeth) :- (
      LookUp(heapT, sig, toMeth),
      VarPointsTo(base, heap),
      ThisVar(toMeth, thiss),
      VCall(base, sig, invo, inMeth),
      HeapType(heap, heapT),
      Reachable(inMeth),
    )

    /*x*/VarPointsTo(thiss, heap) :- (
      LookUp(heapT, sig, toMeth),
      VarPointsTo(base, heap),
      ThisVar(toMeth, thiss),
      VCall(base, sig, invo, inMeth),
      HeapType(heap, heapT),
      Reachable(inMeth),
    )

    /*x*/CallGraph(invo, toMeth) :- (
      LookUp(heapT, sig, toMeth),
      VarPointsTo(base, heap),
      ThisVar(toMeth, thiss),
      VCall(base, sig, invo, inMeth),
      HeapType(heap, heapT),
      Reachable(inMeth)
    )
    CallGraph(invo, toMeth) :- (Reachable(inMeth), StaticCall(toMeth, invo, inMeth))

    // rules for dynamic val
    /*x*/Reachable(toMeth) :- (
      LookUp(heapT, sig, toMeth),
      VarPointsTo(base, heap),
      ThisVar(toMeth, thiss),
      HeapType(heap, heapT),
      FormalReturn(toMeth, from),
      Load(to, base, sig, inMeth),
      Reachable(inMeth),
    )

    /*x*/VarPointsTo(thiss, heap) :- (
      LookUp(heapT, sig, toMeth),
      VarPointsTo(base, heap),
      ThisVar(toMeth, thiss),
      HeapType(heap, heapT),
      FormalReturn(toMeth, from),
      Load(to, base, sig, inMeth),
      Reachable(inMeth),
    )

    /*x*/InterProcAssign(to, from) :- (
      LookUp(heapT, sig, toMeth),
      VarPointsTo(base, heap),
      ThisVar(toMeth, thiss),
      HeapType(heap, heapT),
      FormalReturn(toMeth, from),
      Load(to, base, sig, inMeth),
      Reachable(inMeth),
    )

    /*x*/InterProcAssign(to, from) :- (
      ActualArg(invo, m, n, from),
      CallGraph(invo, meth),
      FormalArg(meth, m, n, to),
    )

    /*x*/InterProcAssign(to, from) :- (
      ActualReturn(invo, to),
      FormalReturn(meth, from),
      CallGraph(invo, meth),
    )

    VarPointsTo(to, heap) :- (InterProcAssign(to, from), VarPointsTo(from, heap))

    Reachable(toMeth) :- (StaticCall(toMeth, invo, inMeth), Reachable(inMeth))


    // without negation support, we generate NotDefines facts
    LookUp(classC, sig, meth) :- DefinesWith(classC, sig, meth)

    /*x*/LookUp(classC, sigA, sigB) :- (
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
    /*x*/Reachable(toMeth) :- (
      ThisVar(toMeth, thiss),
      ThisVar(inMeth, thisFrom),
      VarPointsTo(thisFrom, heap),
      Reachable(inMeth),
      SuperCall(toMeth, invo, inMeth),
    )

    /*x*/VarPointsTo(thiss, heap) :- (
      ThisVar(inMeth, thisFrom),
      ThisVar(toMeth, thiss),
      VarPointsTo(thisFrom, heap),
      Reachable(inMeth),
      SuperCall(toMeth, invo, inMeth),
    )

    /*x*/CallGraph(invo, toMeth) :- (
      ThisVar(inMeth, thisFrom),
      ThisVar(toMeth, thiss),
      VarPointsTo(thisFrom, heap),
      Reachable(inMeth),
      SuperCall(toMeth, invo, inMeth)
    )

    /*x*/VarPointsTo(to, heap) :- (
      LookUp(heapT, fld, actualFld),
      VarPointsTo(from, heap),
      VarPointsTo(base, baseH),
      HeapType(baseH, heapT),
      Load(to, base, fld, inMeth),
      FieldValDef(actualFld, from),
    )
  }
}
