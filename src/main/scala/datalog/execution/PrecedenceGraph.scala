package datalog.execution

import datalog.dsl.Atom

import scala.collection.mutable

class Node(r: Int) {
  val rId: Int = r
  var idx: Int = -1
  var lowLink: Int = -1
  val edges: mutable.Set[Node] = mutable.Set[Node]()
  var onStack: Boolean = false
  override def toString() =
    "{" + rId + ": "
      + "idx=" + idx + " lowLink=" + lowLink + " onstack=" + onStack + " edges="
      + edges.map(e => e.rId).mkString("[", ", ", "]")
      + "}"
}

class PrecedenceGraph {
  val nodes: mutable.Map[Int, Node] = mutable.Map[Int, Node]()
  val result: mutable.Queue[mutable.Set[Int]] = mutable.Queue[mutable.Set[Int]]()

  var index = 0 // TODO: for now onetime sort
  val stack: mutable.Stack[Node] = mutable.Stack[Node]()

  def addNode(rule: Seq[Atom]): Unit = {
    val node = nodes.getOrElseUpdate(rule.head.rId, Node(rule.head.rId))
    rule.drop(1).foreach(n => {
      val neighbor = nodes.getOrElseUpdate(n.rId, Node(n.rId))
      node.edges.addOne(neighbor)
    })
  }

  def strongConnect(v: Node): Unit = {
    v.idx = index
    v.lowLink = index
    index = index + 1
    stack.push(v)
    v.onStack = true

    v.edges.foreach(w => {
      if (w.idx == -1) { // recur
        strongConnect(w)
        v.lowLink = v.lowLink.min(w.lowLink)
      } else if (w.onStack) {
        // w is in current scc
        v.lowLink = v.lowLink.min(w.idx)
      }
    })

    if (v.lowLink == v.idx) {
      val res = mutable.Set[Int]()
      res.addOne(v.rId)
      while
        // add to component
        val w = stack.pop()
        w.onStack = false
        res.addOne(w.rId)
        w.rId != v.rId
      do {} // TODO: weird?
      result.addOne(res)
    }
  }

  def getTopSort(fromRid: Int): Seq[Int] = {
    printNodes()
    nodes.foreach((rId, node) => {
      if (node.idx == -1) {
        strongConnect(node)
      }
    })

    result.toSeq.flatten
  }

  def printNodes() = {
    println("PRECEDEDENCE: " + nodes.map((r, n) => r + " -> " + n.edges.map(e => e.rId)).mkString("{", ", ", "}"))
  }
}
