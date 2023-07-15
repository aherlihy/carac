package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}
import datalog.tools.Debug.debug
import datalog.execution.{AllIndexes, JoinIndexes}

import scala.collection.{immutable, mutable}

/**
 * This is a classic pull-based/volcano model for query operators that operate over the CollectionsTypes,
 * which are essentially just wrapped Scala collections.
 * @param ns
 */
class VolcanoStorageManager(ns: NS = NS()) extends CollectionsStorageManager(ns) {
  def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB = ???
  def projectHelper(input: EDB, k: JoinIndexes): EDB = ???
  def joinProjectHelper(inputs: Seq[EDB], k: JoinIndexes, sortOrder: (Int, Int, Int)): EDB = ???
  def joinProjectHelper_withHash(inputs: Seq[EDB], rId: Int, hash: String, sortOrder: (Int, Int, Int)): EDB = ???


  /**
   * Returns the [[relOps.VolOperator]] after applying negation, if the rule is
   * actually negated.
   *
   * @param negated true iff the complement of the rule should be taken.
   * @param arity the arity of the relation.
   * @param rId the [[datalog.dsl.RelationId]] of the relation.
   * @param edb the [[relOps.VolOperator]] representing the EDB.
   * @return the [[relOps.VolOperator]] after applying negation.
   */
  private def withNegation(negated: Boolean)
                          (rId: RelationId, arity: Int, edb: relOps.VolOperator): relOps.VolOperator =
    import relOps.*
    if !negated then edb
    else
      val complement = Scan(getAllPossibleEDBs(arity), rId)
      Diff(mutable.ArrayBuffer(complement, edb))

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
            Scan(edbs.getOrElse(rId, CollectionsEDB()), rId)
          else
            Project(
              Join(k.deps.zipWithIndex.map((tr, i) =>
                val r = tr._2
                // TODO: warn if EDB is empty? Right now can't tell the difference between undeclared and empty EDB
                Union(Seq(
                  Scan(getKnownDerivedDB(r), r),
                  Scan(getDiscoveredEDBs(r), r),
                ))
//                withNegation(k.negated(i))(r, k.sizes(i), scan)
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
          Scan(edbs.getOrElse(rId, CollectionsEDB()), rId)
        else
          var idx = -1 // if dep is featured more than once, only us delta once, but at a different pos each time
          Union(
            k.deps.map((typ, d) => {
              var found = false
              Project(
                Join(
                  k.deps.zipWithIndex.map((tr, i) => {
                    val r = tr._2
                    if (r == d && !found && i > idx)
                      found = true
                      idx = i
//                      withNegation(k.negated(i))(r, k.sizes(i),
                        Union(Seq(
                          Scan(getKnownDeltaDB(r), r),
                          Scan(getDiscoveredEDBs(r), r),
                        ))
//                      )
                    else
//                      withNegation(k.negated(i))(r, k.sizes(i),
                        Union(Seq(
                          Scan(getKnownDerivedDB(r), r),
                          Scan(getDiscoveredEDBs(r), r),
                        ))
//                      )
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