package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}
import datalog.execution.{AllIndexes, JoinIndexes, PredicateType}
import datalog.storage.IndexedCollectionsCasts.*
import datalog.tools.Debug.debug

import scala.collection.{immutable, mutable}

/**
 * This is a storage manager that uses push-based operators that operate over the IndexedCollectionsTypes,
 * which are essentially just wrapped Scala collections.
 * @param ns
 */
class IndexedStorageManager(ns: NS = new NS()) extends IndexedCollectionsStorageManager(ns) {

  private /*inline*/ def scanFilter(k: JoinIndexes, maxIdx: Int)(get: Int => StorageTerm = x => x) = ??? /*{
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
  }*/

  override def joinHelper(inputEDB: Seq[EDB], k: JoinIndexes): IndexedCollectionsEDB = ??? /*{
    val inputs = asIndexedCollectionsSeqEDB(inputEDB)
    inputs
      .reduceLeft((outer: IndexedCollectionsEDB, inner: IndexedCollectionsEDB) => {
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
  }*/

  override def projectHelper(input: EDB, k: JoinIndexes): IndexedCollectionsEDB = ??? /*{
    asIndexedCollectionsEDB(input).map(t =>
      IndexedCollectionsRow(k.projIndexes.flatMap((typ, idx) =>
        typ match {
          case "v" => t.lift(idx.asInstanceOf[Int])
          case "c" => Some(idx)
          case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
        }))
    )
  }*/

  private /*inline*/ def prefilter(consts: mutable.Map[Int, Constant], skip: Int, row: IndexedCollectionsRow): Boolean = {
    consts.isEmpty || consts.forall((idx, const) => // for each filter // TODO: make sure out of range fails
      row(idx - skip) == const
    )
  }

  private /*inline*/ def toJoin(k: JoinIndexes, innerTuple: IndexedCollectionsRow, outerTuple: IndexedCollectionsRow): Boolean = {
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

  override def joinProjectHelper_withHash(inputsEDB: Seq[EDB], rId: Int, hash: String, onlineSort: Boolean): IndexedCollectionsEDB = {
    val originalK = allRulesAllIndexes(rId)(hash)
//    println(s"JoinIndexes=${originalK.toStringWithNS(ns)}")
    val inputs = asIndexedCollectionsSeqEDB(inputsEDB)
//    println(s"input arity: ${inputs.map(e => s"${e.name}:${e.arity}").mkString("[", ", ", "]")}")
//    var intermediateCardinalities = Seq[Int]()
    val fResult = if (inputs.length == 1) {// just filter + project
      inputs.head.filterProjectWithIndex(
        originalK.constIndexes,
        originalK.projIndexes,
        0
      )
    } else {
      val result = inputs
        .foldLeft(
          (IndexedCollectionsEDB.empty(), 0, originalK) // initialize intermediate indexed-collection
        )((combo: (IndexedCollectionsEDB, Int, JoinIndexes), innerT: IndexedCollectionsEDB) =>
          val outerT = combo._1
          val atomI = combo._2
          var k = combo._3
          if (atomI == 0) // not a monad :(
            (innerT, atomI + 1, k)
          else
            val (inner, outer) = // on the fly swapping of join order
              if (atomI > 1 && onlineSort && outerT.length > innerT.length)
                val body = k.atoms.drop(1)
                val newerHash = JoinIndexes.getRuleHash(Seq(k.atoms.head, body(atomI)) ++ body.dropRight(body.length - atomI) ++ body.drop(atomI + 1))
                k = allRulesAllIndexes(rId).getOrElseUpdate(newerHash, JoinIndexes(originalK.atoms.head +: body, Some(originalK.cxns)))
                (outerT, innerT)
              else
                (innerT, outerT)
            // outer = outer relation, inner = inner relation
            val edbResult = outer.joinFilterWithIndex(k, 0, inner)
            //            intermediateCardinalities = intermediateCardinalities :+ edbResult.length
            (edbResult, atomI + 1, k)
        )
      result._1.filterProjectWithIndex(result._3.constIndexes, result._3.projIndexes, 0)
    }
    fResult.name = ns(rId)
    fResult

//      if (inputs.length > 2) println(s"${intermediateCardinalities.dropRight(1).mkString("", "\n", "")}")
//      result._1
//        .filter(edb => result._3.constIndexes.filter((i, _) => i >= edb.length).isEmpty)
//        .map(t =>
//          IndexedCollectionsRow(
//            result._3.projIndexes.flatMap((typ, idx) =>
//              typ match {
//                case "v" => t.lift(idx.asInstanceOf[Int])
//                case "c" => Some(idx)
//                case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
//              })
//          )
//        )
  }

  override def joinProjectHelper(inputsEDB: Seq[EDB], originalK: JoinIndexes, onlineSort: Boolean): IndexedCollectionsEDB = ??? /*{ // OLD, only keep around for benchmarks
    val inputs = asIndexedCollectionsSeqEDB(inputsEDB)
    if (inputs.length == 1) // just filter
      inputs.head
        .filter(e =>
          val filteredC = originalK.constIndexes.filter((ind, _) => ind < e.length)
          prefilter(filteredC, 0, e) && filteredC.size == originalK.constIndexes.size)
        .map(t =>
          IndexedCollectionsRow(originalK.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            })))

    else
      var preSortedK = originalK // TODO: find better ways to reduce with 2 acc
      var sorted = inputs
      val result = sorted
        .foldLeft(
          (IndexedCollectionsEDB(), 0, preSortedK) // initialize intermediate indexed-collection
        )((combo: (IndexedCollectionsEDB, Int, JoinIndexes), innerT: IndexedCollectionsEDB) =>
          val outerT = combo._1
          val atomI = combo._2
          var k = combo._3
          if (atomI == 0) // not a monad :(
            (innerT, atomI + 1, k)
          else
            val (inner, outer) =
              if (atomI > 1 && onlineSort && outerT.length > innerT.length)
                val body = k.atoms.drop(1)
                k = JoinIndexes(Seq(k.atoms.head, body(atomI)) ++ body.dropRight(body.length - atomI) ++ body.drop(atomI + 1), Some(originalK.cxns))
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
          IndexedCollectionsRow(
            result._3.projIndexes.flatMap((typ, idx) =>
              typ match {
                case "v" => t.lift(idx.asInstanceOf[Int])
                case "c" => Some(idx)
                case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
              })
          )
        )
  }*/

  /**
   * Use iterative collection operators to evaluate an IDB rule using Semi-Naive algo
   *
   * @param rId - The id of the relations
   * @param keys - a JoinIndexes object to join on
   * @return
   */
  override def SPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): IndexedCollectionsEDB = ??? /*{
    println(s"keys= $keys")
    debug("SPJU:", () => s"r=${ns(rId)} keys=${printer.snPlanToString(keys)} knownDBId $knownDbId")
      IndexedCollectionsEDB(keys.flatMap(k => // union of each definition of rId
        if (k.edb)
          discoveredFacts.getOrElse(rId, IndexedCollectionsEDB()).wrapped
        else
          var idx = -1 // if dep is featured more than once, only use delta once, but at a different pos each time
          k.deps.flatMap((*, d) => {
            var found = false // TODO: perhaps need entry in derived/delta for each atom instead of each relation?
            joinProjectHelper(
              k.deps.zipWithIndex.map((tr, i) =>
                val (typ, r) = tr
                val q = if (r == d && !found && i > idx)
                  found = true
                  idx = i
                  if (typ != PredicateType.NEGATED) // if negated then we want the complement of all facts not just the delta
                    getKnownDeltaDB(r)
                  else
                    getKnownDerivedDB(r)
                else
                  getKnownDerivedDB(r)

                typ match
                  case PredicateType.NEGATED =>
                    val arity = k.atoms(i + 1).terms.length
                    val compl = getComplement(arity)
                    val res = diff(compl, q)
                    debug("found negated relation, rule=", () => s"${printer.ruleToString(k.atoms)}\n\tarity=$arity, compl=${printer.factToString(compl)}, Q=${printer.factToString(q)}, final res=${printer.factToString(res)}")
                    res
                  case _ => q
            ), k, false).wrapped // don't sort in shallow embedding
          }).distinct
      ))
  }
  */

  override def naiveSPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): IndexedCollectionsEDB = ??? /*{
    debug("NaiveSPJU:", () => s"r=${ns(rId)} keys=${printer.naivePlanToString(keys)} knownDBId $knownDbId")
    IndexedCollectionsEDB(
      keys.flatMap(k => { // for each idb rule
        if (k.edb)
          discoveredFacts.getOrElse(rId, IndexedCollectionsEDB()).wrapped
        else
          projectHelper(
            joinHelper(
              k.deps.zipWithIndex.map((md, i) =>
                val (typ, r) = md
                val q = getKnownDerivedDB(r)
                typ match
                  case PredicateType.NEGATED =>
                    val arity = k.atoms(i + 1).terms.length
                    val compl = getComplement(arity)
                    val res = diff(compl, q)
                    debug(s"found negated relation, rule=", () => s"${printer.ruleToString(k.atoms)}\n\tarity=$arity, compl=${printer.factToString(compl)}, Q=${printer.factToString(q)}, final res=${printer.factToString(res)}")
                    res
                  case _ => q
              ), k // TODO: warn if EDB is empty? Right now can't tell the difference between undeclared and empty EDB)
            ), k
          ).wrapped.distinct
      })
    )
  }*/
}
