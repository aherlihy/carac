package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}

import scala.collection.{immutable, mutable}

class CollectionsStorageManager(ns: mutable.Map[Int, String] = mutable.Map[Int, String]()) extends SimpleStorageManager(ns) {
  private def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB = {
    val outputRelation = EDB()

    if(inputs.length == 1) {
      return inputs.head.filter(
        joined =>
          (k.constIndexes.isEmpty || k.constIndexes.forall((idx, const) => joined(idx) == const)) &&
            (k.varIndexes.isEmpty || k.varIndexes.forall(condition => condition.forall(c => joined(c) == joined(condition.head))))
      )
    }
    if (inputs.isEmpty || inputs.length > 2)
      throw new Error("TODO: multi-way join")

    // TODO: multi-way join

    val outerTable = inputs.head
    val innerTable = inputs(1)

    outerTable.foreach(outerTuple => {
      innerTable.foreach(innerTuple => {
        val joined = outerTuple ++ innerTuple
        if ((k.varIndexes.isEmpty || k.varIndexes.forall(condition =>
          condition.forall(c => joined(c) == joined(condition.head))))
          && (k.constIndexes.isEmpty ||
          k.constIndexes.forall((idx, const) => joined(idx) == const))) {
          outputRelation.addOne(joined)
        }
      })
    })
    outputRelation
  }

  /**
   * Use iterative collection operators to evaluate an IDB rule using Semi-Naive algo
   *
   * @param rIds - The ids of the relations
   * @param keys - a JoinIndexes object to join on
   * @return
   */
  def SPJU(rId: Int, keys: Seq[JoinIndexes], sourceQueryId: Int): EDB = {
    val plan =
      keys.flatMap(k => // for each idb rule
        k.deps.flatMap(d =>
              joinHelper(
                k.deps.map(r =>
                  if (r == d)
                    deltaDB(sourceQueryId)(r)
                  else
                    incrementalDB(sourceQueryId)(r)
                ), k)
                .map(t => t.zipWithIndex.filter((e, i) => k.projIndexes.contains(i)).map(_._1))
          ).toSet
        ).toSet
    mutable.ArrayBuffer.from(plan)
  }

  def naiveSPJU(rId: Int, keys: Seq[JoinIndexes], sourceQueryId: Int): EDB = {
    val plan =
      keys.flatMap(k => // for each idb rule
          joinHelper(
            k.deps.map(r => incrementalDB(sourceQueryId)(r)), k
          )
          .map(t => t.zipWithIndex.filter((e, i) => k.projIndexes.contains(i)).map(_._1))
      ).toSet
    mutable.ArrayBuffer.from(plan) // TODO: use Table not ArrayBuffer
  }
}