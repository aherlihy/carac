package datalog.execution

import datalog.dsl.Atom
import datalog.tools.Debug.debug
import datalog.storage.{NS, RelationId}

import scala.collection.mutable

class Node(r: RelationId)(using ns: NS) {
  val rId: RelationId = r
  var idx: Int = -1
  var lowLink: Int = -1
  var edges: mutable.Set[Node] = mutable.Set[Node]() // "weak", i.e. positive edges
  var negEdges: mutable.Set[Node] = mutable.Set[Node]() // "strong", i.e. negative edges
  var onStack: Boolean = false

  // self-recursion, i.e. the head predicate appears in the body at least once. Does not indicate if there is any multi-hop/mutual recursion.
  def recursive: Boolean = edges.contains(this)

  override def toString(): String =
    "{" + ns(rId) + ": " + "recursive=" + recursive
      //      " idx=" + idx + " lowLink=" + lowLink + " onstack=" + onStack +
      //      " edges=" + edges.map(e => ns(e.rId)).mkString("[", ", ", "]")
      + "}"
}

class PrecedenceGraph(using ns: NS /* ns used for pretty printing */) {
  private val adjacencyList = mutable.Map[RelationId, Seq[RelationId]]()
  private val negAdjacencyList = mutable.Map[RelationId, Seq[RelationId]]()
  private val aliases = mutable.Map[RelationId, RelationId]()
  val idbs: mutable.Set[RelationId] = mutable.Set[RelationId]()

  override def toString: String = buildGraph().map((r, n) => ns(r) + " -> " + (n.edges.map(e => s"+${ns(e.rId)}") ++ n.negEdges.map(e => s"-${ns(e.rId)}")).mkString("[", ", ", "]")).mkString("{", ", ", "}")

  def addNode(rule: Seq[Atom]): Unit = {
    idbs.addOne(rule.head.rId)
    adjacencyList.update(rule.head.rId, adjacencyList.getOrElse(rule.head.rId, Seq.empty) ++ rule.drop(1).filter(!_.negated).map(_.rId))
    negAdjacencyList.update(rule.head.rId, negAdjacencyList.getOrElse(rule.head.rId, Seq.empty) ++ rule.drop(1).filter(_.negated).map(_.rId))
  }

  def updateNodeAlias(aliases: mutable.Map[RelationId, RelationId]): Unit = {
    this.aliases.addAll(aliases)
  }

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
  private def buildGraph(): mutable.Map[RelationId, Node] = {
    val nodes = mutable.Map[RelationId, Node]()
    for (from, list) <- adjacencyList do
      for to <- list do
        val fAlias = getAliasedId(from)
        val tAlias = getAliasedId(to)

        val f = nodes.getOrElseUpdate(fAlias, Node(fAlias))
        val t = nodes.getOrElseUpdate(tAlias, Node(tAlias))
        f.edges.addOne(t)

    for (from, list) <- negAdjacencyList do
      for to <- list do
        val fAlias = getAliasedId(from)
        val tAlias = getAliasedId(to)

        val f = nodes.getOrElseUpdate(fAlias, Node(fAlias))
        val t = nodes.getOrElseUpdate(tAlias, Node(tAlias))
        f.negEdges.addOne(t)
    nodes
  }

  // TODO: Any vertex that is not on a directed cycle forms a strongly connected component all by itself, so potentially collapse single-node strata into preceding strata
  def tarjan(target: Option[Int] = None): Seq[Set[Int]] = {
    var index = 0
    val stack = mutable.Stack[Node]()
    val sorted = mutable.Queue[mutable.Set[Int]]()

    def strongConnect(v: Node): Unit = {
      v.idx = index
      v.lowLink = index
      index = index + 1
      stack.push(v)
      v.onStack = true

      (v.edges ++ v.negEdges).foreach(w => {  // for now use both + / - edges, TODO: only use negative edges?
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
        do {}
        sorted.addOne(res)
      }
    }

    // give tarjan a hint
    val graph = buildGraph()
    val order = target.map(t => {
      graph.values.filter(_.rId == t) ++ graph.values.filter(_.rId != t)
    }).getOrElse(graph.values)

    order.foreach(node => {
      if (node.idx == -1) {
        strongConnect(node)
      }
    })

    val result = sorted.map(_.toSet).toSeq

    // check for negative cycle
    result.foreach(strata =>
      strata.foreach(p =>
        if (graph(p).negEdges.map(n => n.rId).intersect(strata).nonEmpty)
          throw new Exception("Negative cycle detected in input program")
      )
    )

    result
  }

  def ullman(target: Option[Int] = None): Seq[Set[RelationId]] = {
    // give ullman the same hint
    val graph = buildGraph()
    val order = target.map(t => {
      graph.values.filter(_.rId == t) ++ graph.values.filter(_.rId != t)
    }).getOrElse(graph.values)

    val stratum = graph.map((k, *) => (k, 0))
    var prevStratum = graph.map((k, *) => (k, 0))
    var setDiff = true
    while (setDiff) {
      order.foreach(node =>
        val p = node.rId
        node.negEdges.foreach(q =>
          stratum(p) = stratum(p).max(1 + stratum(q.rId))
        )
        node.edges.foreach(q =>
          stratum(p) = stratum(p).max(stratum(q.rId))
        )
      )
      if (stratum.nonEmpty && stratum.values.max > stratum.keys.size)
        throw new Exception("Negative cycle detected in input program")
      setDiff = prevStratum != stratum
      prevStratum = stratum.clone
    }
    stratum.toSeq.groupBy(_._2).toSeq.sortBy(_._1).map(_._2).map(v => v.map(_._1).toSet)
  }

  def dropIrrelevant(sorted: Seq[Set[Int]], target: Option[Int] = None): Seq[Set[Int]] = {
    val drop = target.map(t => sorted.size - 1 - sorted.indexWhere(g => g.contains(t)))
    sorted
      .dropRight(drop.get)
      .map(_.toSet)
      .map(_.intersect(idbs)) // sort and remove edbs
      .filter(_.nonEmpty)
  }

  def scc(target: Int): Seq[Set[Int]] = {
    val sorted2 = ullman(Some(target))
    dropIrrelevant(sorted2, Some(target))
  }
}
