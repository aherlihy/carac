package test.tastyfact

import datalog.tastyfact.rulesets.{RuleSet, PointsToRuleSet}
import test.tastyfact.utils.Queries.PointsToQuery
class PointsToFields
    extends AbstractAnalysisSuite(
      "PointsToFields.scala",
      "pointstofields.Main.main",
      PointsToRuleSet
    ) {

  test("all methods are reachable") {
    val reachable = program.namedRelation("Reachable").get().map(_.head)

    assert(
      Set(
        "pointstofields.Main.main",
        "pointstofields.MyObject.<init>",
        "pointstofields.MyObject.fun1",
        "pointstofields.MyObject.fun2",
        "pointstofields.A.<init>",
      ).subsetOf(reachable)
    )
  }

  test("allocation sites are correct") {
    val query = PointsToQuery(program.namedRelation("VarPointsTo"))

    val initX = query.pointsToSet("pointstofields.Main.main.initX")
    
    val a1 = query.pointsToSet("pointstofields.Main.main.a1")
    val a2 = query.pointsToSet("pointstofields.Main.main.a2")
    
    val x1 = query.pointsToSet("pointstofields.Main.main.x1")
    val x2 = query.pointsToSet("pointstofields.Main.main.x2")

    assert(initX.subsetOf(x1))
    assert(initX.subsetOf(x2))
    assert(a1.subsetOf(x1))
    assert(a2.subsetOf(x2))
    assertEquals(x1.intersect(x2).size, 1)
  }
}
