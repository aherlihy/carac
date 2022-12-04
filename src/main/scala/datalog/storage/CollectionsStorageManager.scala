package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}

import scala.collection.{immutable, mutable}

import datalog.tools.Debug.debug

class CollectionsStorageManager(ns: NS = new NS()) extends SimpleStorageManager(ns) {
  inline def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB = {

    if(inputs.length == 1) { // just a scan, so only filter don't join
      inputs.head.filter(
        joined =>
          (k.constIndexes.isEmpty || k.constIndexes.forall((idx, const) => joined(idx) == const)) &&
            (k.varIndexes.isEmpty || k.varIndexes.forall(condition => condition.forall(c => joined(c) == joined(condition.head))))
      )
    } else if (inputs.isEmpty || inputs.length > 2) {
      throw new Error("TODO: multi-way join")
    } else {
      val outerTable = inputs.head
      val innerTable = inputs(1)

      outerTable.flatMap(outerTuple => {
        innerTable.flatMap(innerTuple => {
          val joined = outerTuple ++ innerTuple
          if ((k.varIndexes.isEmpty || k.varIndexes.forall(condition =>
            condition.forall(c => joined(c) == joined(condition.head))))
            && (k.constIndexes.isEmpty ||
            k.constIndexes.forall((idx, const) => joined(idx) == const))) {
            Some(joined)
          } else {
            None
          }
        })
      })
    }
  }

  /**
   * Use iterative collection operators to evaluate an IDB rule using Semi-Naive algo
   *
   * @param rId - The id of the relations
   * @param keys - a JoinIndexes object to join on
   * @return
   */
  def SPJU(rId: Int, keys: Table[JoinIndexes], knownDbId: Int): EDB = {
    debug("SPJU:", () => "r=" + ns(rId) + " keys=" + printer.snPlanToString(keys) + " knownDBId" + knownDbId)
      keys.flatMap(k => // for each idb rule
        var idx = -1 // if dep is featured more than once, only us delta once, but at a different pos each time
        k.deps.flatMap(d => {
          var found = false // TODO: perhaps need entry in derived/delta for each atom instead of each relation?
          joinHelper(
            k.deps.zipWithIndex.map((r, i) =>
              if (r == d && !found && i > idx) {
                found = true
                idx = i
                deltaDB(knownDbId)(r)
              }
              else {
                derivedDB(knownDbId).getOrElse(r, edbs(r))
              }
            ), k)
            .map(t =>
              k.projIndexes.flatMap(idx => t.lift(idx))
            )
        }).toSet
        )
  }

  def naiveSPJU(rId: Int, keys: Table[JoinIndexes], knownDbId: Int): EDB = {
    keys.flatMap(k => { // for each idb rule
      joinHelper(
        k.deps.map(r => edbs.getOrElse(r, derivedDB(knownDbId)(r))), k
      ).map(t =>
        k.projIndexes.flatMap(idx => t.lift(idx))
      ).toSet
    })
  }
}