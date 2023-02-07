package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}
import datalog.execution.JoinIndexes
import datalog.tools.Debug.debug

import scala.collection.{View, immutable, mutable}

class CollectionsStorageManager3(ns: NS = new NS()) extends SimpleStorageManager(ns) {
  override def projectHelper(input: CollectionsStorageManager3.this.EDB, k: JoinIndexes): CollectionsStorageManager3.this.EDB = input
  // to cut down on size, see if you can filter out elements from the outer loop before entering inner loop
  inline def prefilter(consts: Map[Int, Constant], skip: Int, row: Row[StorageTerm]): Boolean = {
    consts.isEmpty || consts.forall((idx, const) => // for each filter // TODO: make sure out of range fails
        row(idx - skip) == const
    )
  }
  inline def toJoin(k: JoinIndexes, innerTuple: Row[StorageTerm], outerTuple: Row[StorageTerm]): Boolean = {
    k.varIndexes.isEmpty || k.varIndexes.forall(condition =>
      if (condition.head >= innerTuple.size + outerTuple.size)
        true
      else
        val toCompare = innerTuple.applyOrElse(condition.head, j => outerTuple(j - innerTuple.size))
        condition.drop(1).forall(idx =>
          idx >= innerTuple.size + outerTuple.size ||
            innerTuple.applyOrElse(idx, j => outerTuple(j - innerTuple.size)) == toCompare
        )
    )
  }

  def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB = {
    if (inputs.length == 1) // just filter
      inputs.view.head
        .filter(e =>
          val filteredC = k.constIndexes.filter((ind, _) => ind < e.size)
            prefilter(filteredC, 0, e) && filteredC.size == k.constIndexes.size)
        .map(t =>
          k.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }).toIndexedSeq)
        .to(mutable.ArrayBuffer)
    else
      inputs.view
        .map(i => i.view)
        .reduceLeft((outer: View[Row[StorageTerm]], inner: View[Row[StorageTerm]]) =>
          outer
            .filter(o =>
              prefilter(k.constIndexes.filter((i, _) => i < o.size), 0, o)
            ) // filter outer tuple
            .flatMap(outerTuple =>
              inner
                .filter(i =>
                  prefilter(k.constIndexes.filter((ind, _) => ind >= outerTuple.size && ind < (outerTuple.size + i.size)), outerTuple.size, i) && toJoin(k, outerTuple, i)
                )
                .map(innerTuple => outerTuple ++ innerTuple))
        )
        .filter(edb => k.constIndexes.filter((i, _) => i >= edb.size).isEmpty)
        .map(t =>
          k.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }).toIndexedSeq
        )
        .to(mutable.ArrayBuffer)
  }

  def SPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB = {
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
      }).toSet
    )
  }

  def naiveSPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB = {
    debug("NaiveSPJU:", () => s"r=${ns(rId)} keys=${printer.naivePlanToString(keys)} knownDBId $knownDbId")
    keys.flatMap(k => { // for each idb rule
      if (k.edb)
        edbs.getOrElse(rId, EDB())
      else
        joinHelper(
          k.deps.map(r => derivedDB(knownDbId).getOrElse(r, edbs.getOrElse(r, EDB()))), k  // TODO: warn if EDB is empty? Right now can't tell the difference between undeclared and empty EDB)
        ).toSet
    })
  }
}