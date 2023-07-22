package test.tastyfact

import datalog.tastyfact.rulesets.{RuleSet, PointsToRuleSet}
import test.tastyfact.utils.Queries.PointsToQuery
class PointsToNested
    extends AbstractAnalysisSuite(
      "PointsToNested.scala",
      "pointstonested.Main.main",
      PointsToRuleSet
    ) {

  test("all methods are reachable") {
    val reachable = program.namedRelation("Reachable").get().map(_.head)

    assert(
      Set(
        "pointstonested.Main.main",
        "pointstonested.Main.main.fun",
        "pointstonested.Main.main.fun.fun",
        "pointstonested.Main.main.b.fun",
      ).subsetOf(reachable)
    )
  }

  test("allocation sites are correct") {
    val query = PointsToQuery(program.namedRelation("VarPointsTo"))

    val a = query.pointsToSet("pointstonested.Main.main.a")
    val b = query.pointsToSet("pointstonested.Main.main.b")
    val c = query.pointsToSet("pointstonested.Main.main.c")

    assertEquals(a, c)
    assertNotEquals(a, b)
  }
}
