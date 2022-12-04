package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}

import scala.collection.{immutable, mutable}

import datalog.tools.Debug.debug

class CollectionsStorageManager(ns: NS = new NS()) extends SimpleStorageManager(ns) {
  inline def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB = {
    inputs
      .reduceLeft((outer, inner) => {
        outer.flatMap(outerTuple => {
          inner.map(innerTuple => {
            outerTuple ++ innerTuple
          })
        })
      })
      .filter(joined =>
        (k.varIndexes.isEmpty || k.varIndexes.forall(condition =>
          condition.forall(c => joined(c) == joined(condition.head))
        )) &&
          (k.constIndexes.isEmpty || k.constIndexes.forall((idx, const) =>
            joined(idx) == const
          ))
      )
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
              k.projIndexes.flatMap((typ, idx) =>
                typ match {
                  case "v" => t.lift(idx.asInstanceOf[Int])
                  case "c" => Some(idx)
                  case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
                })
            )
        }).toSet
        )
  }

  def naiveSPJU(rId: Int, keys: Table[JoinIndexes], knownDbId: Int): EDB = {
    keys.flatMap(k => { // for each idb rule
      joinHelper(
        k.deps.map(r => edbs.getOrElse(r, derivedDB(knownDbId)(r))), k
      ).map(t =>
        k.projIndexes.flatMap((typ, idx) =>
          typ match {
            case "v" => t.lift(idx.asInstanceOf[Int])
            case "c" => Some(idx)
            case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
          }
        )
      ).toSet
    })
  }
}