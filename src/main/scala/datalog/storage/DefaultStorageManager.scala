package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}
import datalog.execution.{AllIndexes, JoinIndexes}
import datalog.storage.CollectionsCasts.*

import scala.collection.{immutable, mutable}
import datalog.tools.Debug.debug

/**
 * This is a storage manager that uses push-based operators that operate over the CollectionsTypes,
 * which are essentially just wrapped Scala collections.
 * @param ns
 */
class DefaultStorageManager(ns: NS = new NS()) extends CollectionsStorageManager(ns) {

  private inline def scanFilter(k: JoinIndexes, maxIdx: Int)(get: Int => StorageTerm = x => x) = {
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

  override def joinHelper(inputEDB: Seq[EDB], k: JoinIndexes): CollectionsEDB = {
    val inputs = asCollectionsSeqEDB(inputEDB)
    inputs
      .reduceLeft((outer: CollectionsEDB, inner: CollectionsEDB) => {
        outer.flatMap(outerTuple => {
          inner.flatMap(innerTuple => {
            val get = (i: Int) => {
              outerTuple.applyOrElse(i, j => innerTuple(j - outerTuple.length))
            }
            if(scanFilter(k, innerTuple.length + outerTuple.length)(get))
              Some(outerTuple.concat(innerTuple))
            else
              None
          })
        })
      })
      .filter(r => scanFilter(k, r.length)(r.apply))
  }

  override def projectHelper(input: EDB, k: JoinIndexes): CollectionsEDB = {
    asCollectionsEDB(input).map(t =>
      CollectionsRow(k.projIndexes.flatMap((typ, idx) =>
        typ match {
          case "v" => t.lift(idx.asInstanceOf[Int])
          case "c" => Some(idx)
          case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
        }))
    )
  }

  private inline def prefilter(consts: Map[Int, Constant], skip: Int, row: CollectionsRow): Boolean = {
    consts.isEmpty || consts.forall((idx, const) => // for each filter // TODO: make sure out of range fails
      row(idx - skip) == const
    )
  }

  private inline def toJoin(k: JoinIndexes, innerTuple: CollectionsRow, outerTuple: CollectionsRow): Boolean = {
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

  override def joinProjectHelper_withHash(inputsEDB: Seq[EDB], rId: Int, hash: String, sortOrder: (Int, Int, Int)): CollectionsEDB = {
    val inputs = asCollectionsSeqEDB(inputsEDB)
    val originalK = allRulesAllIndexes(rId)(hash)
    if (inputs.length == 1) // just filter
      inputs.head
        .filter(e =>
          val filteredC = originalK.constIndexes.filter((ind, _) => ind < e.length)
          prefilter(filteredC, 0, e) && filteredC.size == originalK.constIndexes.size)
        .map(t =>
          CollectionsRow(originalK.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            })))
    else
//      val (sorted, newHash) = JoinIndexes.getSorted(inputs.toArray, edb => edb.length, rId, hash, this, sortAhead) // NOTE: already sorted in staged compiler/ProjectJoinFilterOp.run
      val result = inputs
        .foldLeft(
          (CollectionsEDB(), 0, allRulesAllIndexes(rId)(hash))
        )((combo: (CollectionsEDB, Int, JoinIndexes), innerT: CollectionsEDB) =>
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
                  .map(innerTuple => outerTuple.concat(innerTuple)))
            (edbResult, atomI + 1, k)
        )
      result._1
        .filter(edb => result._3.constIndexes.filter((i, _) => i >= edb.length).isEmpty)
        .map(t =>
          CollectionsRow(
            result._3.projIndexes.flatMap((typ, idx) =>
              typ match {
                case "v" => t.lift(idx.asInstanceOf[Int])
                case "c" => Some(idx)
                case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
              })
          )
        )
  }

  override def joinProjectHelper(inputsEDB: Seq[EDB], originalK: JoinIndexes, sortOrder: (Int, Int, Int)): CollectionsEDB = { // OLD, only keep around for benchmarks
    val inputs = asCollectionsSeqEDB(inputsEDB)
    if (inputs.length == 1) // just filter
      inputs.head
        .filter(e =>
          val filteredC = originalK.constIndexes.filter((ind, _) => ind < e.length)
          prefilter(filteredC, 0, e) && filteredC.size == originalK.constIndexes.size)
        .map(t =>
          CollectionsRow(originalK.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            })))

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
          (CollectionsEDB(), 0, preSortedK)
        )((combo: (CollectionsEDB, Int, JoinIndexes), innerT: CollectionsEDB) =>
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
                  .map(innerTuple => outerTuple.concat(innerTuple)))
            (edbResult, atomI + 1, k)
          )
      result._1
        .filter(edb => result._3.constIndexes.filter((i, _) => i >= edb.length).isEmpty)
        .map(t =>
          CollectionsRow(
            result._3.projIndexes.flatMap((typ, idx) =>
              typ match {
                case "v" => t.lift(idx.asInstanceOf[Int])
                case "c" => Some(idx)
                case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
              })
          )
        )
  }

  /**
   * Returns the [[CollectionsEDB]] after applying negation, if the rule is
   * actually negated.
   *
   * @param negated true iff the complement of the rule should be taken.
   * @param arity the arity of the relation.
   * @param edb the [[CollectionsEDB]] to be negated.
   * @return the [[CollectionsEDB]] after applying negation.
   */
  private def withNegation(negated: Boolean)
                          (arity: Int, edb: EDB): EDB =
    if !negated then edb
    else diff(getAllPossibleEDBs(arity), edb)

  /**
   * Use iterative collection operators to evaluate an IDB rule using Semi-Naive algo
   *
   * @param rId - The id of the relations
   * @param keys - a JoinIndexes object to join on
   * @return
   */
  override def SPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): CollectionsEDB = {
    debug("SPJU:", () => s"r=${ns(rId)} keys=${printer.snPlanToString(keys)} knownDBId $knownDbId")
      CollectionsEDB(keys.flatMap(k => // union of each definition of rId
        if (k.edb)
          edbs.getOrElse(rId, CollectionsEDB()).wrapped
        else
          var idx = -1 // if dep is featured more than once, only us delta once, but at a different pos each time
          k.deps.flatMap(d => {
            var found = false // TODO: perhaps need entry in derived/delta for each atom instead of each relation?
            joinProjectHelper(
              k.deps.zipWithIndex.map((r, i) =>
                if (r == d && !found && i > idx) {
                  found = true
                  idx = i
                  withNegation(k.negated(i))(k.sizes(i),
                    union(Seq(getKnownDeltaDB(r), getDiscoveredEDBs(r)))
                  )
                }
                else {
                  withNegation(k.negated(i))(k.sizes(i),
                    union(Seq(getKnownDerivedDB(r), getDiscoveredEDBs(r)))
                  ) // TODO: warn if EDB is empty? Right now can't tell the difference between undeclared and empty EDB
                }
              ), k, (0, 0, 0)).wrapped // don't sort when not staging
          }).distinct
      ))
  }

  override def naiveSPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): CollectionsEDB = {
    debug("NaiveSPJU:", () => s"r=${ns(rId)} keys=${printer.naivePlanToString(keys)} knownDBId $knownDbId")
    CollectionsEDB(
      keys.flatMap(k => { // for each idb rule
        if (k.edb)
          edbs.getOrElse(rId, CollectionsEDB()).wrapped
        else
          projectHelper(
            joinHelper(
              k.deps.zipWithIndex.map((r, i) =>
                withNegation(k.negated(i))(k.sizes(i),
                  union(Seq(getKnownDerivedDB(r), getDiscoveredEDBs(r)))
                )
              ), k // TODO: warn if EDB is empty? Right now can't tell the difference between undeclared and empty EDB)
            ), k
          ).wrapped.distinct
      })
    )
  }
}