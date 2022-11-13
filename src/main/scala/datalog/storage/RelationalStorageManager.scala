package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}
import datalog.tools.Debug.debug

import scala.collection.{immutable, mutable}

class RelationalStorageManager(ns: mutable.Map[Int, String] = mutable.Map[Int, String]()) extends SimpleStorageManager(ns) {
  def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB = { throw new Error("shouldn't be called") }
  /**
   * Use relational operators to evaluate an IDB rule using Naive algo
   *
   * @param rIds - The ids of the relations
   * @param keys - a JoinIndexes object to join on
   * @return
   */
  def naiveSPJU(rId: Int, keys: Table[JoinIndexes], knownDbId: Int): EDB = {
    debug("naiveSPJU:", () => "r=" + ns(rId) + " keys=" + printer.planToString(keys) + " knownDBId" + knownDbId)
    import relOps.*

    val plan = Union(
        keys.map(k =>
          Project(
            Join(
                k.deps.map(r => Scan(
                  edbs.getOrElse(r, derivedDB(knownDbId)(r)), r)
                ), k.varIndexes, k.constIndexes
            ),
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
  def SPJU(rId: Int, keys: Table[JoinIndexes], knownDbId: Int): EDB = {
    import relOps.*
    debug("SPJU:", () => "r=" + ns(rId) + " keys=" + printer.snPlanToString(keys) + " knownDBId" + knownDbId)
    val plan = Union(
      keys.map(k => // for each idb rule
        Union(
          k.deps.map(d => {
            var found = false
            Project(
              Join(
                k.deps.map(r => {
                  if (r == d && !found) // TODO: this needs to only happen once per, even if r is featured twice
                    found = true
                    Scan(deltaDB(knownDbId)(r), r)
                  else
                    Scan(edbs.getOrElse(r, derivedDB(knownDbId)(r)), r)
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