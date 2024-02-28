package test.examples.tastyslistlibinverse

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}

class tastyslistlibinverse_worst_test extends ExampleTestGenerator(
  "tastyslistlibinverse",
  Set(Tags.Naive, Tags.Volcano),
  Set(Tags.Slow, Tags.CI)
) with tastyslistlibinverse_worst

trait tastyslistlibinverse_worst {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/tastyslistlibinverse/facts"
  val toSolve = "EquivToOutput"

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
  }
}
