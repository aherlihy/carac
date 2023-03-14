package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}
import datalog.execution.{JoinIndexes, AllIndexes}

import scala.collection.{immutable, mutable}
import datalog.tools.Debug.debug

class CollectionsStorageManager(ns: NS = new NS()) extends SimpleStorageManager(ns) {
  val allRulesAllIndexes: mutable.Map[RelationId, AllIndexes] = mutable.Map.empty

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
              outerTuple.applyOrElse(i, j => innerTuple(j - outerTuple.length))
            }
            if(scanFilter(k, innerTuple.length + outerTuple.length)(get))
              Some(outerTuple ++ innerTuple)
            else
              None
          })
        })
      })
      .filter(r => scanFilter(k, r.length)(r))
  }

  def projectHelper(input: EDB, k: JoinIndexes): EDB = {
    input.map(t =>
      k.projIndexes.flatMap((typ, idx) =>
        typ match {
          case "v" => t.lift(idx.asInstanceOf[Int])
          case "c" => Some(idx)
          case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
        }).toIndexedSeq
    )
  }

  inline def prefilter(consts: Map[Int, Constant], skip: Int, row: Row[StorageTerm]): Boolean = {
    consts.isEmpty || consts.forall((idx, const) => // for each filter // TODO: make sure out of range fails
      row(idx - skip) == const
    )
  }

  inline def toJoin(k: JoinIndexes, innerTuple: Row[StorageTerm], outerTuple: Row[StorageTerm]): Boolean = {
    k.varIndexes.isEmpty || k.varIndexes.forall(condition =>
      if (condition.head >= innerTuple.length + outerTuple.length)
        true
      else
        val toCompare = innerTuple.applyOrElse(condition.head, j => outerTuple(j - innerTuple.length))
        condition.drop(1).forall(idx =>
          idx >= innerTuple.length + outerTuple.length ||
            innerTuple.applyOrElse(idx, j => outerTuple(j - innerTuple.length)) == toCompare
        )
    )
  }

  def joinProjectHelper_withHash(inputs: Seq[EDB], rId: Int, hash: String, sortOrder: (Int, Int, Int)): EDB = {
    val originalK = allRulesAllIndexes(rId)(hash)
    if (inputs.length == 1) // just filter
      inputs.head
        .filter(e =>
          val filteredC = originalK.constIndexes.filter((ind, _) => ind < e.length)
          prefilter(filteredC, 0, e) && filteredC.length == originalK.constIndexes.length)
        .map(t =>
          originalK.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }).toIndexedSeq)
    else
//      val (sorted, newHash) = JoinIndexes.getSorted(inputs.toArray, edb => edb.length, rId, hash, this, sortAhead) // NOTE: already sorted in staged compiler/ProjectJoinFilterOp.run
      val result = inputs
        .foldLeft(
          (SimpleEDB(), 0, allRulesAllIndexes(rId)(hash))
        )((combo: (EDB, Int, JoinIndexes), innerT: EDB) =>
          val outerT = combo._1
          val atomI = combo._2
          var k = combo._3
          if (atomI == 0) // not a monad :(
            (innerT, atomI + 1, k)
          else
            val (inner, outer) =
              if (atomI > 1 && ((sortOrder._3 == 1 && outerT.length > innerT.length) || (sortOrder._3 == -1 && innerT.length > outerT.length)))
                val body = k.atoms.drop(1)
                val newerHash = JoinIndexes.getRuleHash(Array(k.atoms.head, body(atomI)) ++ body.dropRight(body.length - atomI) ++ body.drop(atomI + 1))
                k = allRulesAllIndexes(rId)(newerHash)
                (outerT, innerT)
              else
                (innerT, outerT)
            val edbResult = outer
              .filter(o =>
                prefilter(k.constIndexes.filter((i, _) => i < o.length), 0, o)
              ) // filter outer tuple
              .flatMap(outerTuple =>
                inner
                  .filter(i =>
                    prefilter(k.constIndexes.filter((ind, _) => ind >= outerTuple.length && ind < (outerTuple.length + i.length)), outerTuple.length, i) && toJoin(k, outerTuple, i)
                  )
                  .map(innerTuple => outerTuple ++ innerTuple))
            (edbResult, atomI + 1, k)
        )
      result._1
        .filter(edb => result._3.constIndexes.filter((i, _) => i >= edb.length).isEmpty)
        .map(t =>
          result._3.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }).toIndexedSeq
        )
  }

  def joinProjectHelper(inputs: Seq[EDB], originalK: JoinIndexes, sortOrder: (Int, Int, Int)): EDB = { // OLD, only keep around for benchmarks
    if (inputs.length == 1) // just filter
      inputs.head
        .filter(e =>
          val filteredC = originalK.constIndexes.filter((ind, _) => ind < e.length)
          prefilter(filteredC, 0, e) && filteredC.length == originalK.constIndexes.length)
        .map(t =>
          originalK.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }).toIndexedSeq)
        .to(mutable.ArrayBuffer)
    else
      var preSortedK = originalK // TODO: find better ways to reduce with 2 acc
      var sorted = inputs
      if (sortOrder._2 != 0)
        var edbToAtom = inputs.toArray.zipWithIndex.map((edb, i) => (edb, originalK.atoms(i + 1))).sortBy((edb, _) => edb.length)
        if (sortOrder._2 == -1) edbToAtom = edbToAtom.reverse
        val newAtoms = originalK.atoms.head +: edbToAtom.map(_._2)
        preSortedK = JoinIndexes(newAtoms)
        sorted = edbToAtom.map(_._1)

      val result = sorted
        .foldLeft(
          (SimpleEDB(), 0, preSortedK)
        )((combo: (EDB, Int, JoinIndexes), innerT: EDB) =>
          val outerT = combo._1
          val atomI = combo._2
          var k = combo._3
          if (atomI == 0) // not a monad :(
            (innerT, atomI + 1, k)
          else
            val (inner, outer) =
              if (atomI > 1 && ((sortOrder._3 == 1 && outerT.length > innerT.length) || (sortOrder._3 == -1 && innerT.length > outerT.length)))
                val body = k.atoms.drop(1)
                k = JoinIndexes(Array(k.atoms.head, body(atomI)) ++ body.dropRight(body.length - atomI) ++ body.drop(atomI + 1))
                (outerT, innerT)
              else
                (innerT, outerT)
            val edbResult = outer
              .filter(o =>
                prefilter(k.constIndexes.filter((i, _) => i < o.length), 0, o)
              ) // filter outer tuple
              .flatMap(outerTuple =>
                inner
                  .filter(i =>
                    prefilter(k.constIndexes.filter((ind, _) => ind >= outerTuple.length && ind < (outerTuple.length + i.length)), outerTuple.length, i) && toJoin(k, outerTuple, i)
                  )
                  .map(innerTuple => outerTuple ++ innerTuple))
            (edbResult, atomI + 1, k)
          )
      result._1
        .filter(edb => result._3.constIndexes.filter((i, _) => i >= edb.length).isEmpty)
        .map(t =>
          result._3.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }).toIndexedSeq
        )
  }

  /**
   * Use iterative collection operators to evaluate an IDB rule using Semi-Naive algo
   *
   * @param rId - The id of the relations
   * @param keys - a JoinIndexes object to join on
   * @return
   */
  def SPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB = {
    debug("SPJU:", () => s"r=${ns(rId)} keys=${printer.snPlanToString(keys)} knownDBId $knownDbId")
      keys.flatMap(k => // union of each definition of rId
        if (k.edb)
          edbs.getOrElse(rId, SimpleEDB())
        else
          var idx = -1 // if dep is featured more than once, only us delta once, but at a different pos each time
          k.deps.flatMap(d => {
            var found = false // TODO: perhaps need entry in derived/delta for each atom instead of each relation?
            joinProjectHelper(
              k.deps.zipWithIndex.map((r, i) =>
                if (r == d && !found && i > idx) {
                  found = true
                  idx = i
                  deltaDB(knownDbId)(r)
                }
                else {
                  derivedDB(knownDbId).getOrElse(r, edbs.getOrElse(r, SimpleEDB())) // TODO: warn if EDB is empty? Right now can't tell the difference between undeclared and empty EDB
                }
              ), k, (0, 0, 0)) // don't sort when not staging
          }).toSet
      )
  }

  def naiveSPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB = {
    debug("NaiveSPJU:", () => s"r=${ns(rId)} keys=${printer.naivePlanToString(keys)} knownDBId $knownDbId")
    keys.flatMap(k => { // for each idb rule
      if (k.edb)
        edbs.getOrElse(rId, SimpleEDB())
      else
        projectHelper(
          joinHelper(
            k.deps.map(r => derivedDB(knownDbId).getOrElse(r, edbs.getOrElse(r, SimpleEDB()))), k // TODO: warn if EDB is empty? Right now can't tell the difference between undeclared and empty EDB)
          ), k).toSet
    })
  }
}