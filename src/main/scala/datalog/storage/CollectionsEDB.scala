package datalog.storage

import datalog.dsl.{Constant, Variable}

import scala.collection.mutable
import CollectionsCasts.*
import datalog.execution.JoinIndexes

import scala.collection.immutable.Seq
import scala.collection.immutable.ArraySeq
import scala.collection.mutable.ArrayBuffer
import scala.math
import scala.math.Ordering.Double

/**
 * EDB for non-indexed collections-based storage.
 */

/* Necessary evil to avoid path dependent types. All casts go first-thing in the CollectionsStorageManager (or child)'s public methods that take an EDB as argument */
object CollectionsCasts {
  def asCollectionsEDB(to: Relation[StorageTerm]): CollectionsEDB = to.asInstanceOf[CollectionsEDB]
  def asCollectionsSeqEDB(to: Seq[Relation[StorageTerm]]): Seq[CollectionsEDB] = to.asInstanceOf[Seq[CollectionsEDB]]
  def asCollectionsRow(to: Row[StorageTerm]): CollectionsRow = to.asInstanceOf[CollectionsRow]
}

/**
 * Precise type for the EDB type in CollectionsStorageManager.
 * Represents one EDB relation, i.e. the set of rows of tuples in a particular EDB relation.
 * AKA mutable.ArrayBuffer[Seq[StorageTerm]]
 */
case class CollectionsEDB(wrapped: mutable.ArrayBuffer[CollectionsRow],
                          name: String,
                          arity: Int
                         ) extends GeneralCollectionsEDB {
  export wrapped.{ length, clear, nonEmpty, toSet, apply, mkString, iterator }
  def bulkRegisterIndex(idxs: mutable.BitSet): this.type = this
  def registerIndex(idx: RelationId): this.type = this

  def emptyCopy: CollectionsEDB = CollectionsEDB.empty(arity, name)
  def mergeEDBs(toCopy: GeneralCollectionsEDB): Unit =
    val edbToCopy = asCollectionsEDB(toCopy)
    wrapped.addAll(edbToCopy.wrapped)

  def addAll(edbs: mutable.ArrayBuffer[CollectionsRow]): this.type =
    wrapped.addAll(edbs)
    this

  def addAndDeduplicate(edbs: mutable.ArrayBuffer[CollectionsRow]): this.type =
    wrapped.addAll(edbs).distinct
    this

  def addOne(elem: CollectionsRow): this.type =
    if (elem.size != arity) throw new Exception(s"Adding row with length ${elem.size} to EDB $name with arity $arity")
    wrapped.addOne(elem)
    this

  def contains(edb: CollectionsRow): Boolean =
    wrapped.contains(edb)

  def projectAndDiff(constIndexes: mutable.Map[Int, Constant],
                     projIndexes: Seq[(String, Constant)],
                     newName: String,
                     newIndexes: mutable.BitSet,
                     derivedDB: CollectionsDatabase,
                     rId: RelationId
                    ): GeneralCollectionsEDB =

    val constFilter = constIndexes.filter((ind, _) => ind < arity)
//    println(s"constIdx=${constIndexes}, constFilter=$constFilter, arity=$arity, wrapped=$wrapped")
    val newWrapped = ArrayBuffer[CollectionsRow]()
    val toCopy = wrapped

    var i = 0
    while i < toCopy.length do
      val projected = toCopy(i).project(projIndexes)
//      println(s"projected=$projected checking derived(${newName})=${if derivedDB.contains(rId) then derivedDB(rId) else "X"}, constantTest=${constFilter.isEmpty || toCopy(i).filterConstant(constFilter)}")
//      println(s"projected=$projected, derivedDB.contains($rId)=${derivedDB.contains(rId)}, derivedDB($rId).contains=${derivedDB.contains(rId) && derivedDB(rId).contains(projected)}")
      if
        (constFilter.isEmpty || toCopy(i).filterConstant(constFilter)) && // filter constants
          (!derivedDB.contains(rId) || !derivedDB(rId).contains(projected)) // diff with derived
      then
        newWrapped.addOne(projected)
      i += 1

    CollectionsEDB(
      newWrapped.distinct, // no duplicates within single query result, so just need to dep in union
      newName,
      projIndexes.length
    )

  def joinFilter(joinIndexes: JoinIndexes, skip: Int, toJoinG: GeneralCollectionsEDB): GeneralCollectionsEDB =
    val toJoin = asCollectionsEDB(toJoinG)
    val outer = this
    val inner = toJoin

    // get relevant join keys + filters for this subquery
    val outerConstantFilters = joinIndexes.constIndexes.filter((ind, _) => ind < outer.arity)
    val innerConstantFilters = joinIndexes.constIndexes.collect { case (ind, c) if ind >= outer.arity && ind < outer.arity + inner.arity => (ind - outer.arity, c) }
    val keysInRange = joinIndexes.varIndexes.map(shared => shared.filter(_ < (outer.arity + inner.arity))).filter(_.nonEmpty)

    //     println(s"\t2-way join($name*${toJoin.name}), allKeys=${joinIndexes.varIndexes},  relKeys=${keysInRange.mkString("[", ", ", "]")}, outerConstants=${outerConstantFilters.mkString("{", ", ", "}")}, innerConstants=${innerConstantFilters.mkString("{", ", ", "}")}")
    //     println(s"\tinput rels=${wrapped.mkString("[",",","]")} * ${toJoin.wrapped.mkString("[",",","]")}")
    //     println(s"innerIndexes=${IndexedCollectionsEDB.allIndexesToString(toJoin)}")

    // store relative positions: [ (innerPos, outerPos), ...] including self-constraints
    val relativeKeys = keysInRange.map(k =>
      (k.filter(_ < outer.arity), k.collect { case i if i >= outer.arity && i < outer.arity + inner.arity => i - outer.arity })
    ).filter((outerPos, innerPos) => outerPos.size + innerPos.size > 1)
    // relative positions of only keys shared between relations
    val relativeJoinKeys = relativeKeys.collect {
      case (outerPos, innerPos) if outerPos.nonEmpty && innerPos.nonEmpty => (outerPos.head, innerPos.head)
    }
    val keyToJoin = relativeJoinKeys.headOption

    //    println(s"\t\trelative join positions = ${relativeKeys.map((o, i) => s"(outers: ${o.mkString("", ".", "")}, inners: ${i.mkString("", ".", "")})").mkString("[", ", ", "]")}")

    // Filter first to cut down size
    val filteredOuter = if (skip > 1) outer else // only filter outer if first 2-way join
      if (outerConstantFilters.isEmpty)
        if (relativeKeys.forall((outerKeys, _) => outerKeys.length <= 1)) // no constant filters, no self constraints:
          outer
        else // need to self-constrain only:
          val filtered = outer.wrapped.filter(outerTuple =>
            relativeKeys.forall((outerKeys, _) => outerTuple.filterConstraint(outerKeys))
          )
          filtered
      else // need to both filter + maybe self constrain
        val filtered = outer.wrapped.
          filter(outerTuple =>
            outerTuple.filterConstant(outerConstantFilters) && relativeKeys.forall((outerKeys, _) => outerTuple.filterConstraint(outerKeys))
          )
        filtered

    val filteredInner =
      if (innerConstantFilters.isEmpty)
        if (relativeKeys.forall((_, innerKeys) => innerKeys.length <= 1)) // no constant filters, no self constraints:
          inner
        else // need to self-constrain:
          val filtered = inner.wrapped.filter(innerTuple =>
            relativeKeys.forall((_, innerKeys) => innerTuple.filterConstraint(innerKeys))
          )
          filtered
      else // need to both filter + maybe self constrain
        val filtered = inner.wrapped.
          filter(innerTuple =>
            innerTuple.filterConstant(innerConstantFilters)
              && relativeKeys.forall((_, innerKeys) => innerTuple.filterConstraint(innerKeys))
          )
        filtered
//    println(s"filteredInner=${filteredInner.mkString("[", ", ", "]")}")

    val result = if (keyToJoin.isEmpty) // join without keys, so just loop over everything without index
      filteredOuter.iterator.flatMap(outerTuple => filteredInner.iterator.map(innerTuple => outerTuple.concat(innerTuple)))
    else
      val outerPosToUse = keyToJoin.get._1
      val innerPosToUse = keyToJoin.get._2
      if (relativeJoinKeys.isEmpty) { // no shared join keys, so just filter by internal constraint
        filteredOuter.iterator.flatMap(outerTuple => filteredInner.iterator.map(innerTuple => outerTuple.concat(innerTuple)))
      } else {
        // TODO: picking first constraint, should pick best constraint
        // remove keys that do not have at least one condition in each tuple, and keep only the first condition (since self-constraints already filtered out)
        val secondaryKeys = relativeJoinKeys
        //        println(s"\t\tprimary join outer[$outerPosToUse], inner[$innerPosToUse], secondary join positions: $secondaryKeys")

        val res = filteredOuter.iterator.flatMap(outerTuple =>
          val indexVal = outerTuple(outerPosToUse)
          val matchingInners = filteredInner.iterator.filter(innerTuple => innerTuple(keyToJoin.get._2) == indexVal)
          matchingInners.collect {
            // check shared keys
            case innerTuple if (secondaryKeys.forall((outerPos, innerPos) => outerTuple(outerPos) == innerTuple(innerPos))) => {
              outerTuple.concat(innerTuple)
            }
          }
        )
//        println(s"res=$res")
        res
      }

    //    println(s"\tintermediateR=${result.map(_.mkString("(", ", ", ")")).mkString("[", ", ", "]")}")
    CollectionsEDB(
      result.to(mutable.ArrayBuffer),
      s"${outer.name}x${inner.name}",
      arity + toJoin.arity,
    )

  def getSetOfSeq: Set[Seq[StorageTerm]] =
    wrapped.map(s => s.toSeq).toSet

  def diff(toDiff: EDB): GeneralCollectionsEDB =
    val toDiffEDB = asCollectionsEDB(toDiff)
    CollectionsEDB(
      wrapped.diff(toDiffEDB.wrapped),
      name,
      arity
    )

  def factToString: String =
    wrapped.map(s => s.mkString("(", ", ", ")")).sorted.mkString("[", ", ", "]")

  def insertInto(key: StorageTerm, toAdd: ArrayBuffer[CollectionsRow], wrappedToModify: ArrayBuffer[CollectionsRow], update: Boolean, deduplicate: Boolean) =
    ???

  // TODO: skip for now, not used.
  def map(f: CollectionsRow => CollectionsRow): CollectionsEDB = ???
  def filter(f: CollectionsRow => Boolean): CollectionsEDB = ???
  def flatMap(f: CollectionsRow => IterableOnce[CollectionsRow]): CollectionsEDB = ???
  def toSet = ???
}

object CollectionsEDB {
  extension (edbs: Seq[EDB])
    def unionEDB: EDB =
      if (edbs.isEmpty)
        throw new Exception("Internal error, union on zero relations")
      else if (edbs.size == 1)
        edbs.head
      else
        val head = asCollectionsEDB(edbs.head)
        CollectionsEDB(
          edbs.flatten(using e => asCollectionsEDB(e).wrapped).distinct.to(mutable.ArrayBuffer),
          head.name,
          head.arity
        )

  def apply(arity: Int, rName: String, elems: CollectionsRow*): CollectionsEDB =
    new CollectionsEDB(ArrayBuffer[CollectionsRow](elems*), rName, arity)
  def empty(arity: Int, rName: String = "ANON", preIndexes: mutable.BitSet = mutable.BitSet(), skipIndexes: mutable.BitSet = mutable.BitSet()): CollectionsEDB =
    CollectionsEDB(ArrayBuffer[CollectionsRow](), rName, arity)
}

