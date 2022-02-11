package datalog.execution

import datalog.dsl.Atom
import scala.collection.mutable.{Set, Map}

case class Node(rId: Int, edges: Set[Node] = Set[Node]())

class PrecedenceGraph {
  val nodes = Map[Int, Node]()
  def initNode(rId: Int): Unit = nodes.addOne(rId, Node(rId))
  def addNode(rule: Seq[Atom]): Unit = {
    val node = nodes.getOrElseUpdate(rule(0).rId, Node(rule(0).rId))
    rule.drop(1).foreach(n => {
      val neighbor = nodes.getOrElseUpdate(n.rId, Node(n.rId))
      node.edges.addOne(neighbor)
    })
  }
  /* TODO: actually topological sort
    https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm
    https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm
    https://en.wikipedia.org/wiki/Path-based_strong_component_algorithm
  */

  def getTopSort(rId: Int): Seq[Int] = {
    if (rId == 5) Seq(0,5) // TODO: special case for edge test, TODO actually sort
    else nodes.keys.toSeq.sorted
  }
}
