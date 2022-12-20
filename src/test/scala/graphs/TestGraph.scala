package graphs

import datalog.dsl.{Constant, Program, Relation, Term}

import scala.collection.mutable

trait TestGraph {
  val queries: mutable.Map[String, Query]
  val description: String
  val skip: Seq[String] = Seq()
}

case class Query(description: String, relation: Relation[Constant], solution: Set[Seq[Term]], skip: Seq[String] = Seq())