package test.tastyfact

import datalog.tastyfact.rulesets.{RuleSet, PointsToRuleSet}
import test.tastyfact.utils.Queries.PointsToQuery

class PointsToConstructors
    extends AbstractAnalysisSuite(
      "PointsToConstructors.scala",
      "pointstoconstructors.Main.main",
      PointsToRuleSet
    ) {

  test("all methods are reachable") {
    val reachable = program.namedRelation("Reachable").get().map(_.head)

    assert(
      Set(
        "pointstoconstructors.T.<init>",
        "pointstoconstructors.A.<init>",
        "pointstoconstructors.B.<init>",
        "pointstoconstructors.Main.main"
      ).subsetOf(reachable)
    )
  }

  test("allocation sites are correct") {
    val query = PointsToQuery(program.namedRelation("VarPointsTo"))

    val o1 = query.pointsToSet("pointstoconstructors.Main.main.o1")
    val o2 = query.pointsToSet("pointstoconstructors.Main.main.o2")

    // println("LookUp")
    // for (f <- program.namedRelation("LookUp").get()) println(f)

    // println("FieldValDef")
    // for (f <- program.namedRelation("FieldValDef").get()) println(f)

    // println("PointsTo")
    // for (f <- program.namedRelation("VarPointsTo").get()) println(f)

    // println("VCall")
    // for (f <- program.namedRelation("VCall").get()) println(f)

    // println("ActualArg")
    // for (f <- program.namedRelation("ActualArg").get()) println(f)

    // println("FormalArg")
    // for (f <- program.namedRelation("FormalArg").get()) println(f)

    // println("CallGraph")
    // for (f <- program.namedRelation("CallGraph").get()) println(f)

    // println("InterProcAssign")
    // for (f <- program.namedRelation("InterProcAssign").get()) println(f)

    val b_argB = query.pointsToSet("pointstoconstructors.Main.main.b_argB")
    val b_argA = query.pointsToSet("pointstoconstructors.Main.main.b_argA")
    val b_get = query.pointsToSet("pointstoconstructors.Main.main.b_get")
    // val b_argA0 = query.pointsToSet("pointstoconstructors.Main.main.b_argA0")
    // val b_argA1 = query.pointsToSet("pointstoconstructors.Main.main.b_argA1")
    // println(b_argA1)
    
    assertEquals(b_argA, o1)
    assertEquals(b_argB, o2)
    assertEquals(b_get, o2)
    // assertEquals(b_argA1, o1)
  }
}
