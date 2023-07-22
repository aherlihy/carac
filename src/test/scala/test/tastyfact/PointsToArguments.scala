package test.tastyfact

import datalog.tastyfact.rulesets.PointsToRuleSet
import test.tastyfact.utils.Queries.PointsToQuery

class PointsToArguments
    extends AbstractAnalysisSuite(
      "PointsToArguments.scala",
      "pointstoarguments.Main.main",
      PointsToRuleSet
    ) {

  test("all methods are reachable") {
    val reachable = program.namedRelation("Reachable").get().map(_.head)

    assert(
      Set(
        "pointstoarguments.Main.main",
        "pointstoarguments.C1.<init>",
        "pointstoarguments.C1.test",
        "pointstoarguments.C2.<init>",
        "pointstoarguments.C2.test",
        "pointstoarguments.Main.main.fun",
        "pointstoarguments.Pair.first",
        "pointstoarguments.Pair.second",
        // "pointstoarguments.Pair.swap",
        "pointstoarguments.TPair.meth1",
        "pointstoarguments.TPair.meth2",
      ).subsetOf(reachable)
    )
  }

  test("allocation sites are correct") {
    val query = PointsToQuery(program.namedRelation("VarPointsTo"))

    val c1 = query.pointsToSet("pointstoarguments.Main.main.c1")
    val c2 = query.pointsToSet("pointstoarguments.Main.main.c2")

    val p1 = query.pointsToSet("pointstoarguments.Main.main.p1")
    val p2 = query.pointsToSet("pointstoarguments.Main.main.p2")
    val p3 = query.pointsToSet("pointstoarguments.Main.main.p3")
    // val swap = query.pointsToSet("pointstoarguments.Main.main.swap")

    val p1_v1 = query.pointsToSet("pointstoarguments.Main.main.p1_v1")
    val p1_v2 = query.pointsToSet("pointstoarguments.Main.main.p1_v2")

    val p2_v1 = query.pointsToSet("pointstoarguments.Main.main.p2_v1")
    val p2_v2 = query.pointsToSet("pointstoarguments.Main.main.p2_v2")

    val p3_v1 = query.pointsToSet("pointstoarguments.Main.main.p3_v1")
    val p3_v2 = query.pointsToSet("pointstoarguments.Main.main.p3_v2")
    val p3_t1 = query.pointsToSet("pointstoarguments.Main.main.p3_t1")
    val p3_t2 = query.pointsToSet("pointstoarguments.Main.main.p3_t2")

    // val swap_v1 = query.pointsToSet("pointstoarguments.Main.main.swap_v1")
    // val swap_v2 = query.pointsToSet("pointstoarguments.Main.main.swap_v2")

    assertEquals(p2_v1, c1)
    assertEquals(p2_v2, c2)

    assertEquals(p3_v1, c1)
    assertEquals(p3_v2, c2)

    assertNotEquals(p3_t1, p3_t2)

    // assertEquals(swap_v1, c2)
    // assertEquals(swap_v2, c1)
  }

  test("default arguments") {
    val query = PointsToQuery(program.namedRelation("VarPointsTo"))

    val default = query.pointsToSet("pointstoarguments.Main.main.default")
    val res1 = query.pointsToSet("pointstoarguments.Main.main.res1")
    val res2 = query.pointsToSet("pointstoarguments.Main.main.res2")
    val res3 = query.pointsToSet("pointstoarguments.Main.main.res3")
    val c11 = query.pointsToSet("pointstoarguments.Main.main.c11")

    assertEquals(res1, default)
    assertEquals(res2, default)
    assertEquals(res3, c11) // make sure default is not used
  }
}

