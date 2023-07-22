package test.tastyfact.utils

import datalog.dsl.Relation

object Queries {
  class PointsToQuery(relation: Relation[String]) {
    private val table = relation.get().map { case Seq(from: String, to: String) => (from, to) }
      .groupBy(_._1).mapValues(v => v.map(_._2))

    def pointsToSet(v: String) = table(v)
    def pointToSame(v1: String, v2: String) = table.get(v1) == table.get(v2)

  }
}
