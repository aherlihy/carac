package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}
import datalog.tools.Debug.debug

import scala.collection.{immutable, mutable}

class RelationalStorageManager(ns: NS = NS()) extends SimpleStorageManager(ns) {
  def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB = { throw new Error("shouldn't be called") }
  /**
   * Use relational operators to evaluate an IDB rule using Naive algo
   *
   * @param rIds - The ids of the relations
   * @param keys - a JoinIndexes object to join on
   * @return
   */
  def naiveSPJU(rId: Int, keys: Table[JoinIndexes], knownDbId: Int): EDB = {
    debug("naiveSPJU:", () => "r=" + ns(rId) + " keys=" + printer.naivePlanToString(keys) + " knownDBId" + knownDbId)
    import relOps.*

    val plan = Union(
        keys.map(k =>
          if (k.edb)
            Scan(edbs(rId), rId)
          else
            Project(
              Join(
                  k.deps.map(r => Scan(
                    derivedDB(knownDbId).getOrElse(r, edbs(r)), r)
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
        if (k.edb)
          Scan(edbs(rId), rId)
        else
          var idx = -1 // if dep is featured more than once, only us delta once, but at a different pos each time
          Union(
            k.deps.map(d => {
              var found = false
              Project(
                Join(
                  k.deps.zipWithIndex.map((r, i) => {
                    if (r == d && !found && i > idx)
                      found = true
                      idx = i
                      Scan(deltaDB(knownDbId)(r), r)
                    else
                      Scan(derivedDB(knownDbId).getOrElse(r, edbs(r)), r)
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