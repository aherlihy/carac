package test.graphs

import carac.dsl.{Constant, Program, Relation, Term}
import carac.storage.StorageTerm

import scala.collection.mutable

trait TestGraph {
  val queries: mutable.Map[String, Query]
  val description: String
  val skip: Seq[String] = Seq()
}

case class Query(description: String, relation: Relation[Constant], solution: Set[Seq[StorageTerm]], skip: Seq[String] = Seq())
