package test.tastyfact

import datalog.tastyfact.rulesets.{RuleSet, PointsToRuleSet}
import test.tastyfact.utils.Queries.PointsToQuery
class PointsToFunSuite
    extends AbstractAnalysisSuite(
      "PointsToFun.scala",
      "pointstofun.Main.main",
      PointsToRuleSet
    ) {

  test("all methods are reachable") {
    val reachable = program.namedRelation("Reachable").get().map(_.head)

    assert(
      Set(
        "pointstofun.Main.main",
        "pointstofun.PointsToFun.fun1",
        "pointstofun.PointsToFun.fun2",
        "pointstofun.PointsToFun.id"
      ).subsetOf(reachable)
    )
  }

  test("allocation sites are correct") {
    val query = PointsToQuery(program.namedRelation("VarPointsTo"))

    val a1 = query.pointsToSet("pointstofun.PointsToFun.fun1.a1")
    val a2 = query.pointsToSet("pointstofun.PointsToFun.fun2.a2")
    val b1 = query.pointsToSet("pointstofun.PointsToFun.fun1.b1")
    val b2 = query.pointsToSet("pointstofun.PointsToFun.fun2.b2")

    assertEquals(a1.size, 1)
    assertEquals(a2.size, 1)
    assertEquals(a1.intersect(a2), Set.empty)
    assert(a1.intersect(b1).nonEmpty)
    assert(a2.intersect(b2).nonEmpty)
  }
}
