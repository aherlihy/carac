package test.tastyfact

import datalog.tastyfact.rulesets.{RuleSet, PointsToRuleSet}
import test.tastyfact.utils.Queries.PointsToQuery
class PointsToInheritance
    extends AbstractAnalysisSuite(
      "PointsToInheritance.scala",
      "pointstoinheritance.Main.main",
      PointsToRuleSet
    ) {

  test("all methods are reachable") {
    val reachable = program.namedRelation("Reachable").get().map(_.head)

    assert(
      Set(
        "pointstoinheritance.Main.main",
        "pointstoinheritance.A.test",
        "pointstoinheritance.A.privateTest",
        "pointstoinheritance.A.withPrivateCall",
        "pointstoinheritance.A.withNonPrivateCall",
        "pointstoinheritance.B.test",        
      ).subsetOf(reachable)
    )

    assert(!reachable.contains("pointstoinheritance.B.privateTest"))
  }

  test("allocation sites are correct") {
    val query = PointsToQuery(program.namedRelation("VarPointsTo"))

    val a_instance = query.pointsToSet("pointstoinheritance.Main.main.a_instance")
    val b_instance = query.pointsToSet("pointstoinheritance.Main.main.b_instance")

    val a_x = query.pointsToSet("pointstoinheritance.Main.main.a_x")
    val a_xx = query.pointsToSet("pointstoinheritance.Main.main.a_xx")
    val a_t = query.pointsToSet("pointstoinheritance.Main.main.a_t")
    val a_t2 = query.pointsToSet("pointstoinheritance.Main.main.a_t2")
    val a_pc = query.pointsToSet("pointstoinheritance.Main.main.a_pc")
    val a_npc = query.pointsToSet("pointstoinheritance.Main.main.a_npc")

    assertEquals(a_x, a_instance)
    assertEquals(a_xx, a_instance)
    assertEquals(a_t, a_instance)
    assertEquals(a_t2, a_instance)
    assertEquals(a_pc, a_instance)
    assertEquals(a_npc, a_instance)

    val b_x = query.pointsToSet("pointstoinheritance.Main.main.b_x")
    val b_xx = query.pointsToSet("pointstoinheritance.Main.main.b_xx")
    val b_t = query.pointsToSet("pointstoinheritance.Main.main.b_t")
    val b_t2 = query.pointsToSet("pointstoinheritance.Main.main.b_t2")
    val b_pc = query.pointsToSet("pointstoinheritance.Main.main.b_pc")
    val b_npc = query.pointsToSet("pointstoinheritance.Main.main.b_npc")
    val b_sup = query.pointsToSet("pointstoinheritance.Main.main.b_sup")
    val b_traitVal = query.pointsToSet("pointstoinheritance.Main.main.b_traitVal")

    assertEquals(b_x, b_instance)
    assertEquals(b_xx, a_instance)
    assertEquals(b_t, b_instance)
    assertEquals(b_t2, b_instance)
    assertEquals(b_pc, a_instance)
    assertEquals(b_npc, b_instance)
    assertEquals(b_sup, a_instance)
    
    val c_x = query.pointsToSet("pointstoinheritance.Main.main.c_x")
    val c_xx = query.pointsToSet("pointstoinheritance.Main.main.c_xx")
    val c_t = query.pointsToSet("pointstoinheritance.Main.main.c_t")
    val c_pc = query.pointsToSet("pointstoinheritance.Main.main.c_pc")
    val c_npc = query.pointsToSet("pointstoinheritance.Main.main.c_npc")
    
    assertEquals(c_x, a_instance)
    assertEquals(c_xx, a_instance)
    assertEquals(c_t, a_instance)
    assertEquals(c_pc, a_instance)
    assertEquals(c_npc, a_instance)

    val d_x = query.pointsToSet("pointstoinheritance.Main.main.d_x")
    val d_xx = query.pointsToSet("pointstoinheritance.Main.main.d_xx")
    val d_t = query.pointsToSet("pointstoinheritance.Main.main.d_t")
    val d_pc = query.pointsToSet("pointstoinheritance.Main.main.d_pc")
    val d_npc = query.pointsToSet("pointstoinheritance.Main.main.d_npc")
    val d_sup = query.pointsToSet("pointstoinheritance.Main.main.d_sup")
    val d_traitVal = query.pointsToSet("pointstoinheritance.Main.main.d_traitVal")

    assertEquals(d_x, b_instance)
    assertEquals(d_xx, a_instance)
    assertEquals(d_t, b_instance)
    assertEquals(d_pc, a_instance)
    assertEquals(d_npc, b_instance)
    assertEquals(d_sup, a_instance)
    assertEquals(d_traitVal, b_instance)
  }
}
