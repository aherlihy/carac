package datalog.execution

import datalog.dsl.Atom
import datalog.storage.NS
import datalog.tools.Debug.debug

import scala.collection.mutable

private class Node(r: Int)(using ns: NS) {
  val rId: Int = r
  var idx: Int = -1
  var lowLink: Int = -1
  var edges: mutable.Set[Node] = mutable.Set[Node]()
  var onStack: Boolean = false

  def recursive: Boolean = edges.contains(this)

  override def toString(): String =
    "{" + ns(rId) + ": " + "recursive=" + recursive
      //      " idx=" + idx + " lowLink=" + lowLink + " onstack=" + onStack +
      //      " edges=" + edges.map(e => ns(e.rId)).mkString("[", ", ", "]")
      + "}"
}

class PrecedenceGraph(using ns: NS /* for debugging */) {
  private val adjacencyList = mutable.Map[Int, mutable.Set[Int]]()
  private val aliases = mutable.Map[Int, Int]()

  private def nodes = {
    // Compute a new graph from the adjacency list, respecting alias definitions
    val nodes = mutable.Map[Int, Node]()
    for (from, list) <- adjacencyList do
      for to <- list do
        val fAlias = aliases.getOrElse(from, from)
        val tAlias = aliases.getOrElse(to, to)

        val f = nodes.getOrElseUpdate(fAlias, Node(fAlias))
        val t = nodes.getOrElseUpdate(tAlias, Node(tAlias))
        f.edges.addOne(t)
    nodes.toMap
  }

  // private val nodes: mutable.Map[Int, Node] = mutable.Map[Int, Node]()
  val idbs: mutable.Set[Int] = mutable.Set[Int]()

  override def toString: String = nodes.map((r, n) => ns(r) + " -> " + n.edges.map(e => ns(e.rId)).mkString("[", ", ", "]")).mkString("{", ", ", "}")

  def sortedString(): String =
    scc()
      .map(n => n.map(ns.apply))
      .map(_.mkString("(", ", ", ")"))
      .mkString("{", ", ", "}")

  def addNode(rule: Seq[Atom]): Unit = { // TODO: sort incrementally?
    addNode(rule.head.rId, rule.tail.map(_.rId))
  }

  // TODO : Store the aliases in another data structure, and use them to compute
  //        the graph nodes from the adjacency list
  def updateNodeAlias(rId: Int, aliases: mutable.Map[Int, Int]): Unit = {
    this.aliases.addAll(aliases)
  }

  def addNode(rId: Int, deps: Seq[Int]): Unit = {
    adjacencyList.getOrElseUpdate(rId, mutable.Set[Int]()).addAll(deps)
  }

  private def tarjan(target: Option[Int]): Seq[Set[Int]] = {
    var index = 0
    val stack = mutable.Stack[Node]()
    val sorted = mutable.Queue[mutable.Set[Int]]()

    // TODO: need to indicate recursive anywhere?
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

    // give tarjan a hint
    val graph = nodes
    val order = target.map(t => {
      graph.values.filter(_.rId == t) ++ graph.values.filter(_.rId != t)
    }).getOrElse(graph.values)

    order.foreach(node => {
      if (node.idx == -1) {
        strongConnect(node)
      }
    })

    sorted.map(_.toSet).toSeq
  }

  def topSort(target: Int): Seq[Int] = {
    val sorted = tarjan(target = Some(target))
    sorted
      .dropRight(sorted.size - 1 - sorted.indexWhere(g => g.contains(target)))
      .flatMap(s => s.toSeq).filter(r => idbs.contains(r)) // sort and remove edbs
  }

  def scc(): Seq[Set[Int]] = {
    debug("precedencegraph:", () => toString())
    tarjan(target = None).map(_.toSet)
  }

  def scc(target: Int): Seq[Set[Int]] = {
    val sorted = tarjan(target = Some(target))
    sorted
      .dropRight(sorted.size - 1 - sorted.indexWhere(g => g.contains(target)))
      .map(_.toSet)
      .map(_.filter(r => idbs.contains(r))) // sort and remove edbs
      .filter(_.nonEmpty)
  }

  def removeAliases(aliases: mutable.Map[Int, Int]): Unit = {

  }
}
