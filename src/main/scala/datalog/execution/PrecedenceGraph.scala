package datalog.execution

import datalog.dsl.Atom
import datalog.tools.Debug.debug
import datalog.storage.{NS, RelationId}

import scala.collection.mutable

private class Node(r: RelationId)(using ns: NS) {
  val rId: RelationId = r
  var idx: Int = -1
  var lowLink: Int = -1
  var edges: mutable.Set[Node] = mutable.Set[Node]()
  var onStack: Boolean = false

  // self-recursion, i.e. the head predicate appears in the body at least once. Does not indicate if there is any multi-hop/mutual recursion.
  def recursive: Boolean = edges.contains(this)

  override def toString(): String =
    "{" + ns(rId) + ": " + "recursive=" + recursive
      //      " idx=" + idx + " lowLink=" + lowLink + " onstack=" + onStack +
      //      " edges=" + edges.map(e => ns(e.rId)).mkString("[", ", ", "]")
      + "}"
}

class PrecedenceGraph(using ns: NS /* for debugging */) {
  private val adjacencyList = mutable.Map[RelationId, mutable.Set[RelationId]]()
  private val aliases = mutable.Map[RelationId, RelationId]()

  /**
   * Get the rule id that corresponds to the given rule id, following alias
   * definitions.
   * @param rId the rule id to resolve
   * @return the rule id that corresponds to the given rule id
   */
  private def getAliasedId(rId: RelationId): RelationId = {
    var current = rId
    while aliases.contains(current) do
      current = aliases(current)
    current
  }

  /**
   * Compute a new graph from the adjacency list, respecting alias definitions.
   */
  private def buildGraph = {
    val nodes = mutable.Map[RelationId, Node]()
    for (from, list) <- adjacencyList do
      for to <- list do
        val fAlias = getAliasedId(from)
        val tAlias = getAliasedId(to)

        val f = nodes.getOrElseUpdate(fAlias, Node(fAlias))
        val t = nodes.getOrElseUpdate(tAlias, Node(tAlias))
        f.edges.addOne(t)
    nodes.toMap
  }

  val idbs: mutable.Set[RelationId] = mutable.Set[RelationId]()

  override def toString: String = buildGraph.map((r, n) => ns(r) + " -> " + n.edges.map(e => ns(e.rId)).mkString("[", ", ", "]")).mkString("{", ", ", "}")

  def sortedString(): String =
    scc()
      .map(n => n.map(ns.apply))
      .map(_.mkString("(", ", ", ")"))
      .mkString("{", ", ", "}")

  def addNode(rule: Seq[Atom]): Unit = {
    addNode(rule.head.rId, rule.tail.map(_.rId))
  }

  def updateNodeAlias(rId: RelationId, aliases: mutable.Map[RelationId, RelationId]): Unit = {
    this.aliases.addAll(aliases)
  }

  def addNode(rId: RelationId, deps: Seq[RelationId]): Unit = {
    adjacencyList.getOrElseUpdate(rId, mutable.Set[RelationId]()).addAll(deps)
  }

  private def tarjan(target: Option[Int]): Seq[Set[Int]] = {
    var index = 0
    val stack = mutable.Stack[Node]()
    val sorted = mutable.Queue[mutable.Set[Int]]()

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
    val graph = buildGraph
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

  def topSort(target: RelationId): Seq[RelationId] = {
    val sorted = tarjan(target = Some(target))
    sorted
      .dropRight(sorted.size - 1 - sorted.indexWhere(g => g.contains(target)))
      .flatMap(s => s.toSeq).filter(r => idbs.contains(r)) // sort and remove edbs
  }

  def scc(): Seq[Set[RelationId]] = {
    debug("precedencegraph:", () => toString())
    tarjan(target = None)
  }
}
