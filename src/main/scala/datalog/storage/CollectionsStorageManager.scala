package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}
import datalog.execution.JoinIndexes

import scala.collection.{immutable, mutable}

import datalog.tools.Debug.debug

class CollectionsStorageManager(ns: NS = new NS()) extends SimpleStorageManager(ns) {
  inline def scanFilter(k: JoinIndexes, maxIdx: Int)(get: Int => StorageTerm = x => x) = {
    val vCmp = k.varIndexes.isEmpty || k.varIndexes.forall(condition =>
      if (condition.head >= maxIdx)
        true
      else
        val toCompare = get(condition.head)
          condition.drop(1).forall(idx =>
        idx >= maxIdx || get(idx) == toCompare
      )
    )
    val kCmp = k.constIndexes.isEmpty || k.constIndexes.forall((idx, const) =>
      idx >= maxIdx || get(idx) == const
    )
    vCmp && kCmp
  }

  override def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB = {
    inputs
      .reduceLeft((outer: EDB, inner: EDB) => {
        outer.flatMap(outerTuple => {
          inner.flatMap(innerTuple => {
            val get = (i: Int) => {
              outerTuple.applyOrElse(i, j => innerTuple(j - outerTuple.size))
            }
            if(scanFilter(k, innerTuple.size + outerTuple.size)(get))
              Some(outerTuple ++ innerTuple)
            else
              None
          })
        })
      })
      .filter(r => scanFilter(k, r.size)(r))
  }

  /**
   * Use iterative collection operators to evaluate an IDB rule using Semi-Naive algo
   *
   * @param rId - The id of the relations
   * @param keys - a JoinIndexes object to join on
   * @return
   */
  def SPJU(rId: Int, keys: mutable.ArrayBuffer[JoinIndexes], knownDbId: Int): EDB = {
    debug("SPJU:", () => s"r=${ns(rId)} keys=${printer.snPlanToString(keys)} knownDBId $knownDbId")
      keys.flatMap(k => // for each idb rule
        if (k.edb)
          edbs.getOrElse(rId, EDB())
        else
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
                  derivedDB(knownDbId).getOrElse(r, edbs.getOrElse(r, EDB())) // TODO: warn if EDB is empty? Right now can't tell the difference between undeclared and empty EDB
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

  def naiveSPJU(rId: Int, keys: mutable.ArrayBuffer[JoinIndexes], knownDbId: Int): EDB = {
    debug("NaiveSPJU:", () => s"r=${ns(rId)} keys=${printer.naivePlanToString(keys)} knownDBId $knownDbId")
    keys.flatMap(k => { // for each idb rule
      if (k.edb)
        edbs.getOrElse(rId, EDB())
      else
        joinHelper(
          k.deps.map(r => derivedDB(knownDbId).getOrElse(r, edbs.getOrElse(r, EDB()))), k  // TODO: warn if EDB is empty? Right now can't tell the difference between undeclared and empty EDB)
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