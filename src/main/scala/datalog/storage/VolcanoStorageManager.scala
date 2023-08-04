package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}
import datalog.tools.Debug.debug
import datalog.execution.{AllIndexes, JoinIndexes, PredicateType}

import scala.collection.{immutable, mutable}

/**
 * This is a classic pull-based/volcano model for query operators that operate over the CollectionsTypes,
 * which are essentially just wrapped Scala collections.
 * @param ns
 */
class VolcanoStorageManager(ns: NS = NS()) extends CollectionsStorageManager(ns) {
  def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB = ???
  def projectHelper(input: EDB, k: JoinIndexes): EDB = ???
  def joinProjectHelper(inputs: Seq[EDB], k: JoinIndexes, onlineSort: Boolean): EDB = ???
  def joinProjectHelper_withHash(inputs: Seq[EDB], rId: Int, k: JoinIndexes, onlineSort: Boolean): EDB = ???

  /**
   * Use relational operators to evaluate an IDB rule using Naive algo
   *
   * @param rIds - The ids of the relations
   * @param keys - a JoinIndexes object to join on
   * @return
   */
  def naiveSPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB = {
    debug("naiveSPJU:", () => s"r=${ns(rId)}($rId) keys=${printer.naivePlanToString(keys)} knownDBId $knownDbId")
    import relOps.*

    val plan = Union(
        keys.map(k =>
          if (k.edb)
            Scan(discoveredFacts.getOrElse(rId, CollectionsEDB()), rId)
          else
            Project(
              Join(k.deps.zipWithIndex.map((md, i) =>
                val (typ, r) = md
                val q = Scan(getKnownDerivedDB(r), r)
                typ match
                  case PredicateType.NEGATED =>
                    val arity = k.atoms(i + 1).terms.length
                    val compl = getComplement(arity)
                    val res = Diff(Seq(Scan(compl, r), q))
                    debug(s"found negated relation, rule=", () => s"${printer.ruleToString(k.atoms)}\n\tarity=$arity")
                    res
                  case _ => q

              ), k.varIndexes, k.constIndexes),
              k.projIndexes
            )
        ).toSeq
    )
    plan.toList()
  }

  /**
   * Use relational operators to evaluate an IDB rule using Semi-Naive algo
   *
   * @param rIds - The ids of the relations
   * @param keys - a JoinIndexes object to join on
   * @return
   */
  def SPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB = {
    import relOps.*
    debug("SPJU:", () => s"r=${ns(rId)} keys=${printer.snPlanToString(keys)} knownDBId $knownDbId")
    val plan = Union(
      keys.map(k => // for each idb rule
        if (k.edb)
          Scan(discoveredFacts.getOrElse(rId, CollectionsEDB()), rId)
        else
          var idx = -1 // if dep is featured more than once, only us delta once, but at a different pos each time
          Union(
            k.deps.map((typ, d) => {
              var found = false
              Project(
                Join(
                  k.deps.zipWithIndex.map((md, i) => {
                    val (typ, r) = md
                    val q = if (r == d && !found && i > idx)
                      found = true
                      idx = i
                      if (typ != PredicateType.NEGATED) // if negated then we want the complement of all facts not just the delta
                        Scan(getKnownDeltaDB(r), r)
                      else
                        Scan(getKnownDerivedDB(r), r)
                    else
                      Scan(getKnownDerivedDB(r), r)
                    typ match
                      case PredicateType.NEGATED =>
                        val arity = k.atoms(i + 1).terms.length
                        val compl = getComplement(arity)
                        val res = Diff(Seq(Scan(compl, r), q))
                        debug(s"found negated relation, rule=", () => s"${printer.ruleToString(k.atoms)}\n\tarity=$arity")
                        res
                      case _ => q
                  }),
                  k.varIndexes,
                  k.constIndexes
                ),
                k.projIndexes
              )
            })
          )
      ).toSeq
    )
    plan.toList()
  }
}