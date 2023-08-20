package test.examples.tastyslistlibinverse

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}

class tastyslistlibinverse_autoworstsel_test extends ExampleTestGenerator(
  "tastyslistlibinverse",
  Set(Tags.Naive, Tags.Volcano),
  Set(Tags.Slow, Tags.CI)
) with tastyslistlibinverse_autoworstsel

trait tastyslistlibinverse_autoworstsel {
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

    val Equiv = program.relation[String]("Equiv")
    val InverseFns = program.relation[String]("InverseFns")
    val VarEquiv = program.relation[String]("VarEquiv")
    val EquivToOutput = program.relation[String]("EquivToOutput")

    InverseFns("slistlib.Main.main.deserialize", "slistlib.Main.main.serialize") :- ()
    InverseFns("slistlib.Main.main.serialize", "slistlib.Main.main.deserialize") :- ()

    val v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10,
    v11, v12, v13, v14, v15, v16, v17, v18, v19, v20,
    v21, v22, v23, v24, v25, v26, v27, v28, v29, v30,
    v31, v32, v33, v34, v35, v36, v37, v41 = program.variable()

    CallGraph(v12, v9) :- (Reachable(v14), StaticCall(v9, v12, v14))
    CallGraph(v12, v9) :- (VarPointsTo(v11, v1), ThisVar(v9, v10), ThisVar(v14, v11), Reachable(v14), SuperCall(v9, v12, v14))
    CallGraph(v12, v9) :- (VarPointsTo(v5, v1), LookUp(v15, v13, v9), Reachable(v14), ThisVar(v9, v10), VCall(v5, v13, v12, v14), HeapType(v1, v15))
    DefinesWith(v21, v22, v24) :- (DefinesWith(v21, v23, v24), DefinesWith(v20, v22, v23))
    DefinesWith(v21, v24, v24) :- (DefinesWith(v21, v23, v24))
    Equiv(v30, v29) :- (VarEquiv(v41, v37), ActualArg(v34, v27, v28, v29), ActualArg(v32, v25, v26, v41), ActualReturn(v34, v37), ActualReturn(v32, v30), StaticCall(v31, v32, v35), Reachable(v35),  StaticCall(v33, v34, v35), InverseFns(v31, v33))
    FldPointsTo(v6, v7, v1) :- (VarPointsTo(v5, v6), VarPointsTo(v4, v1), Store(v5, v7, v4))
    InterProcAssign(v3, v4) :- (ActualArg(v12, v16, v17, v4), CallGraph(v12, v2), FormalArg(v2, v16, v17, v3))
    InterProcAssign(v3, v4) :- (ActualReturn(v12, v3), FormalReturn(v2, v4), CallGraph(v12, v2))
    InterProcAssign(v3, v4) :- (VarPointsTo(v5, v1), LookUp(v15, v13, v9), Reachable(v14), ThisVar(v9, v10), Load(v3, v5, v13, v14), FormalReturn(v9, v4), HeapType(v1, v15))
    LookUp(v21, v13, v2) :- (DefinesWith(v21, v13, v2))
    LookUp(v21, v22, v23) :- (NotDefines(v21, v23), LookUp(v20, v22, v23), Extends(v21, v20))
    Reachable(v9) :- (Reachable(v14), StaticCall(v9, v12, v14))
    Reachable(v9) :- (VarPointsTo(v11, v1), ThisVar(v9, v10), ThisVar(v14, v11), Reachable(v14), SuperCall(v9, v12, v14))
    Reachable(v9) :- (VarPointsTo(v5, v1), LookUp(v15, v13, v9), Reachable(v14), ThisVar(v9, v10), Load(v3, v5, v13, v14), FormalReturn(v9, v4), HeapType(v1, v15))
    Reachable(v9) :- (VarPointsTo(v5, v1), LookUp(v15, v13, v9), Reachable(v14), ThisVar(v9, v10), VCall(v5, v13, v12, v14), HeapType(v1, v15))
    VarEquiv(v36, v37) :- (VarPointsTo(v37, v1), VarPointsTo(v36, v1))
    VarPointsTo(v0, v1) :- (Reachable(v2), Alloc(v0, v1, v2))
    VarPointsTo(v10, v1) :- (VarPointsTo(v11, v1), ThisVar(v9, v10), ThisVar(v14, v11), Reachable(v14), SuperCall(v9, v12, v14))
    VarPointsTo(v10, v1) :- (VarPointsTo(v5, v1), LookUp(v15, v13, v9), Reachable(v14), ThisVar(v9, v10), Load(v3, v5, v13, v14), FormalReturn(v9, v4), HeapType(v1, v15))
    VarPointsTo(v10, v1) :- (VarPointsTo(v5, v1), LookUp(v15, v13, v9), Reachable(v14), ThisVar(v9, v10), VCall(v5, v13, v12, v14), HeapType(v1, v15))
    VarPointsTo(v3, v1) :- (VarPointsTo(v4, v1), Move(v3, v4))
    VarPointsTo(v3, v1) :- (VarPointsTo(v4, v1), InterProcAssign(v3, v4))
    VarPointsTo(v3, v1) :- (VarPointsTo(v5, v6), VarPointsTo(v4, v1), LookUp(v15, v7, v18), Load(v3, v5, v7, v14), HeapType(v6, v15), FieldValDef(v18, v4))
    VarPointsTo(v3, v1) :- (VarPointsTo(v5, v6), Load(v3, v5, v7, v14), FldPointsTo(v6, v7, v1))
    EquivToOutput(v0) :- Equiv("slistlib.Main.main.OUTPUT_VAR", v0)
  }
}
