package datalog.execution

import datalog.dsl.Atom
import datalog.tools.Debug.debug
import datalog.storage.NS

import scala.collection.mutable

class Node(r: Int) (using ns: NS) {
  var recursive = false
  val rId: Int = r
  var idx: Int = -1
  var lowLink: Int = -1
  val edges: mutable.Set[Node] = mutable.Set[Node]()
  var onStack: Boolean = false
  override def toString() =
    "{" + ns(rId) + ": " + "recursive=" + recursive
//      " idx=" + idx + " lowLink=" + lowLink + " onstack=" + onStack +
//      " edges=" + edges.map(e => ns(e.rId)).mkString("[", ", ", "]")
      + "}"
}

class PrecedenceGraph(using ns: NS /* for debugging */) {
  val nodes: mutable.Map[Int, Node] = mutable.Map[Int, Node]()
  val sorted: mutable.Queue[mutable.Set[Int]] = mutable.Queue[mutable.Set[Int]]()
  val idbs: mutable.Set[Int] = mutable.Set[Int]()

  var index = 0
  val stack: mutable.Stack[Node] = mutable.Stack[Node]()

  override def toString: String = nodes.map((r, n) => ns(r) + " -> " + n.edges.map(e => ns(e.rId)).mkString("[", ", ", "]")).mkString("{", ", ", "}")
  def sortedString(): String = sorted.map(cc => cc.map(ns.apply).mkString("(", ", ", ")")).mkString("{", ", ", "}")

  def addNode(rule: Seq[Atom]): Unit = { // TODO: sort incrementally?
    val node = nodes.getOrElseUpdate(rule.head.rId, Node(rule.head.rId))
    rule.drop(1).foreach(n => {
      val neighbor = nodes.getOrElseUpdate(n.rId, Node(n.rId))
      node.edges.addOne(neighbor)
      if (n.rId == node.rId) {
        node.recursive = true
      }
    })
  }

  def addNode(rId: Int, deps: Seq[Int]): Unit = {
    val node = nodes.getOrElseUpdate(rId, Node(rId))
    deps.foreach(n => {
      val neighbor = nodes.getOrElseUpdate(n, Node(n))
      node.edges.addOne(neighbor)
      if (n == rId) {
        node.recursive = true
      }
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
      sorted.addOne(res)
    }
  }

  def topSort(): Seq[Int] = { // TODO: need to indicate recursive anywhere?
    nodes.foreach((rId, node) => {
      if (node.idx == -1) {
        strongConnect(node)
      }
    })
    sorted.toSeq.flatMap(s => s.toSeq).filter(r => idbs.contains(r)) // sort and remove edbs
  }

  def removeAliases(aliases: mutable.Map[Int, Int]): Unit = {

  }
}
