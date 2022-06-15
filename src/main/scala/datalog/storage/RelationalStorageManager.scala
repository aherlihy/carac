package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}

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
  def naiveSPJU(rId: Int, keys: Table[JoinIndexes], sourceQueryId: Int): EDB = {
    import relOps.*

    val plan = Union(
        keys.map(k =>
          Project(
            Join(
                k.deps.map(r => Scan(incrementalDB(sourceQueryId)(r), r)), k.varIndexes, k.constIndexes
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
  def SPJU(rId: Int, keys: Table[JoinIndexes], sourceQueryId: Int): EDB = {
    import relOps.*

    val plan = Union(
      keys.map(k => // for each idb rule
        Union(
          k.deps.map(d =>
            Project(
              Join(
                k.deps.map(r =>
                  if (r == d)
                    Scan(deltaDB(sourceQueryId)(r), r)
                  else
                    Scan(incrementalDB(sourceQueryId)(r), r)
                ),
                k.varIndexes,
                k.constIndexes
              ),
              k.projIndexes
            )
          )
        )
      ).toSeq
    )
    plan.toList()
  }
}