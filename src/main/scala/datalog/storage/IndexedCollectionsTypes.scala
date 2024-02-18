package datalog.storage

import datalog.dsl.{Constant, Variable}

import scala.collection.mutable
import IndexedCollectionsCasts.*
import datalog.execution.JoinIndexes
import datalog.storage.IndexedCollectionsEDB.allIndexesToString

import scala.collection.mutable.ArrayBuffer


/**
 * This file defines the datatypes that the IndexedCollectionsStorageManager operatoes over. These are the simplest example and just wrap Scala IndexedCollections.
 */

/* Necessary evil to avoid path dependent types. All casts go first-thing in the IndexedCollectionsStorageManager (or child)'s public methods that take an EDB as argument */
object IndexedCollectionsCasts {
  def asIndexedCollectionsEDB(to: Relation[StorageTerm]): IndexedCollectionsEDB = to.asInstanceOf[IndexedCollectionsEDB]
  def asIndexedCollectionsSeqEDB(to: Seq[Relation[StorageTerm]]): Seq[IndexedCollectionsEDB] = to.asInstanceOf[Seq[IndexedCollectionsEDB]]
  def asIndexedCollectionsRow(to: Row[StorageTerm]): IndexedCollectionsRow = to.asInstanceOf[IndexedCollectionsRow]
}

given Ordering[StorageTerm] with
  def compare(x: StorageTerm, y: StorageTerm): Int = x match
    case x: Int =>
      y match
        case y: Int =>
          Ordering[Int].compare(x, y)
        case y: String =>
          -1
    case x: String =>
      y match
        case y: String =>
          Ordering[String].compare(x, y)
        case y: Int =>
          1

/**
 * Precise type for the EDB type in IndexedCollectionsStorageManager.
 * Represents one EDB relation, i.e. the set of rows of tuples in a particular EDB relation.
 * AKA mutable.SortedMap[Term, ArrayBuffer[Seq[StorageTerm]]] for each index key, for now ignore multi-key indexes
 */
case class IndexedCollectionsEDB(wrapped: mutable.ArrayBuffer[IndexedCollectionsRow],
                                 indexKeys: Iterable[Int],
                                 name: String,
                                 arity: Int) extends EDB with IterableOnce[IndexedCollectionsRow] {
  if indexKeys.exists(_ >= arity) then throw new Exception(s"Error: creating edb $name with indexes ${indexKeys.mkString("[", ", ", "]")} but arity $arity")
  // TODO: distinguish between primary (one sortedMap of tuples), and secondary (one map of indexes)
  // TODO: for now keep all indexes updated, but add flag to not bother rebuilding

  val indexes: mutable.Map[Int, mutable.SortedMap[StorageTerm, ArrayBuffer[IndexedCollectionsRow]]] = mutable.Map[Int, mutable.SortedMap[StorageTerm, mutable.ArrayBuffer[IndexedCollectionsRow]]]()
  bulkRegisterIndex(indexKeys)

  def bulkRegisterIndex(idxs: Iterable[Int]): this.type =
    idxs.map(registerIndex)
    this
  /**
   * Add new index key. If data already exists, build the index immediately
   * @param idx
   * @return
   */
  def registerIndex(idx: Int): this.type = {
    // TODO: potentially build indexes async
    if (!indexes.contains(idx))
      val newIndex = mutable.SortedMap[StorageTerm, mutable.ArrayBuffer[IndexedCollectionsRow]]()
      wrapped.foreach(edb => newIndex.getOrElseUpdate(edb(idx), mutable.ArrayBuffer[IndexedCollectionsRow]()).addOne(edb))
      indexes(idx) = newIndex
    this
  }

  /**
   * Bulk add data and update indexes if they exist
   * @param edbs
   * @return
   */
  def addAll(edbs: mutable.ArrayBuffer[IndexedCollectionsRow]): this.type = {
    // TODO: potentially build indexes async
    wrapped.addAll(edbs)
    edbs.foreach(edb =>
      indexes.foreach((idx, index) =>
        index.
          getOrElseUpdate(edb(idx), mutable.ArrayBuffer[IndexedCollectionsRow]()). // if no term of this value exists yet, add empty buffer
          addOne(edb)
      )
    )
    this
  }

  /**
   * Add a single element. TODO: find uses and probably remove, more efficient to do addAll
   * @param edb
   * @return
   */
  def addOne(edb: IndexedCollectionsRow): this.type =
    if (edb.size != arity) throw new Exception(s"Adding row with length ${edb.size} to EDB $name with arity $arity")
    wrapped.addOne(edb)
    indexes.foreach((idx, index) =>
      index.
        getOrElseUpdate(edb(idx), mutable.ArrayBuffer[IndexedCollectionsRow]()). // if no term of this value exists yet, add empty buffer
        addOne(edb)
    )
    this

  def contains(edb: IndexedCollectionsRow): Boolean =
    if (indexes.isEmpty)
      wrapped.contains(edb)
    else
      val i = indexes.head._1 // for now take first index and use it to filter
      indexes(i).contains(edb(i)) && indexes(i)(edb(i)).contains(edb)

  /**
   * Removes that from this. TODO: in-place ok bc only at end of iteration?
   */
  def diff(that: IndexedCollectionsEDB): IndexedCollectionsEDB =
//    indexes.foreach((idx, index) =>
//      index.map((term, tuples) => // TODO: more efficient to make new + rebuild indexes than remove?
//        tuples.filterInPlace(tuple =>
//          !that.contains(tuple)
//        )
//      )
//    )
//    this
    // TODO: more efficient, use index
    val res = wrapped.diff(that.wrapped)
    IndexedCollectionsEDB(res, indexes.keys, name, arity)

  /**
   * Combine EDBs, maintaining the indexes of each.
   * @param suffix
   * @return
   */
  def concat(suffix: IndexedCollectionsEDB): IndexedCollectionsEDB =
    val copy = IndexedCollectionsEDB.copyWithIndexes(this, suffix.indexes.keys) // TODO: do we really need a new copy here? do we need both indexes?
    copy.addAll(suffix.wrapped)
    copy

  /**
   * Used only for getting a final result when we want to make sure to deduplicate results + get a set type
   * @return
   */
  def getSetOfSeq: Set[Seq[StorageTerm]] =
    wrapped.map(s => s.toSeq).toSet


  /**
   * Called when no join is needed, single-relation body rule, so justs filters and projects
   * Right now only single-key indexes, so could pick the most selective index to use as primary.
   * @param constIndexes
   * @param projIndexes
   * @param skip
   * @return
   */
  def projectFilterWithIndex(constIndexes: mutable.Map[Int, Constant],
                             projIndexes: Seq[(String, Constant)],
                             newName: String,
                             newIndexes: mutable.Set[Int],
                             skip: Int): IndexedCollectionsEDB =
    val constFilter = constIndexes.filter((ind, _) => ind < arity)
    if (constFilter.isEmpty)
      if (projIndexes.isEmpty)
        this
      else
        IndexedCollectionsEDB(wrapped.map(_.project(projIndexes)), newIndexes, newName, projIndexes.length)
    else
      // TODO: is it better to use index on each condition and get intersection?
      // TODO: If multiple pick the most selective (smallest), but for now just pick first until i can verify not too expensive to check
      val (position, constant) = constFilter.head // constFilter.map((idx, const) => (idx, indexes(idx)(const).size)).minBy(_._2)._1
      val rest = constFilter.drop(1)

      val result = indexes(position).getOrElse(constant, ArrayBuffer.empty).collect{// copy matching EBDs
        case edb if edb.filterConstant(rest) => edb.project(projIndexes)
      }
//    TODO: need to rebuild indexes?
      val newArity = if projIndexes.isEmpty then arity else projIndexes.length
      IndexedCollectionsEDB(result, newIndexes, newName, newArity)

  /**
   * Assume this is the outer relation, toJoin is inner relation.
   * Merge join + filter but not project because project happens only once at the end
   *
   * @param joinIndexes
   * @param skip
   * @param toJoin
   * @return
   */
  def joinFilterWithIndex(joinIndexes: JoinIndexes,
                          skip: Int,
                          toJoin: IndexedCollectionsEDB): IndexedCollectionsEDB =
    var outer = this
    var inner = toJoin
    // TODO: check and potentially swap inner + outer, if onlineSort

    // get relevant join keys + filters for this subquery
    val outerConstantFilters = joinIndexes.constIndexes.filter((ind, _) => ind < outer.arity)
    val innerConstantFilters = joinIndexes.constIndexes.collect{ case (ind, c) if ind >= outer.arity && ind < outer.arity + inner.arity => (ind - outer.arity, c) }
    val joinKeys = joinIndexes.varIndexes.filter(shared => shared.forall(_ < outer.arity + inner.arity))

//     println(s"\t2-way join($name*${toJoin.name}), allKeys=${joinIndexes.varIndexes},  relKeys=${joinKeys.mkString("[", ", ", "]")}, outerConstants=${outerConstantFilters.mkString("{", ", ", "}")}, innerConstants=${innerConstantFilters.mkString("{", ", ", "}")}")
//     println(s"outerIndexes=${IndexedCollectionsEDB.allIndexesToString(this)}")
//     println(s"innerIndexes=${IndexedCollectionsEDB.allIndexesToString(toJoin)}")

    // store relative positions: [ (innerPos, outerPos), ...]
    val relativeKeys = joinKeys.map(k =>
      (k.filter(_ < outer.arity), k.collect { case i if i >= outer.arity && i < outer.arity + inner.arity => i - outer.arity })
    ).filter((outerPos, innerPos) => outerPos.size + innerPos.size > 1)

//     println(s"\t\trelative join positions = ${relativeKeys.map((o, i) => s"(outers: ${o.mkString("", ".", "")}, inners: ${i.mkString("", ".", "")})").mkString("[", ", ", "]")}")

    // TODO: filter first to cut down size, but have to rebuild indexes, below. Alternatively could join and filter as we go, but would be tricky if filter key != join key
    val filteredOuter =
      if (outerConstantFilters.isEmpty)
        if (relativeKeys.forall((outerKeys, _) => outerKeys.length <= 1)) // no constant filters, no self constraints:
          this
        else // need to self-constrain:
          val filtered = outer.wrapped.filter(outerTuple => // TODO: filter in place
            relativeKeys.forall((outerKeys, _) => outerTuple.filterConstraint(outerKeys))
          )
          IndexedCollectionsEDB(filtered, outer.indexes.keys, outer.name, outer.arity) // TODO: Do not actually need indexes on outer relation
      else // need to both filter + maybe self constrain
        val filtered = outer.indexes(outerConstantFilters.head._1).getOrElse(outerConstantFilters.head._2, ArrayBuffer.empty).
          filter(outerTuple => // TODO: filter in place
            outerTuple.filterConstant(outerConstantFilters.drop(1)) && relativeKeys.forall((outerKeys, _) => outerTuple.filterConstraint(outerKeys))
          )
        IndexedCollectionsEDB(filtered, outer.indexes.keys, outer.name, outer.arity) // TODO: Do not actually need indexes on outer relation

    val filteredInner =
      if (innerConstantFilters.isEmpty)
        if (relativeKeys.forall((_, innerKeys) => innerKeys.length <= 1)) // no constant filters, no self constraints:
          toJoin
        else // need to self-constrain:
          val filtered = inner.wrapped.filter(innerTuple => // TODO: filter in place
            relativeKeys.forall((_, innerKeys) => innerTuple.filterConstraint(innerKeys))
          )
          IndexedCollectionsEDB(filtered, inner.indexes.keys, inner.name, inner.arity)
      else // need to both filter + maybe self constrain
        val filtered = inner.indexes(innerConstantFilters.head._1).getOrElse(innerConstantFilters.head._2, ArrayBuffer.empty).
          filter(innerTuple => // TODO: filter in place
            innerTuple.filterConstant(innerConstantFilters.drop(1)) && relativeKeys.forall((_, innerKeys) => innerTuple.filterConstraint(innerKeys))
          )
        IndexedCollectionsEDB(filtered, inner.indexes.keys, inner.name, inner.arity)

    val result = if (joinKeys.isEmpty) // join without keys, so just loop over everything without index
      filteredOuter.wrapped.flatMap(outerTuple => filteredInner.wrapped.map( innerTuple => outerTuple.concat(innerTuple)))
    else
      val indexToUse = relativeKeys.find((o, i) => o.nonEmpty && i.nonEmpty) // TODO: pick "best" index
      if (indexToUse.isEmpty) { // no shared join keys, so just filter by internal constraint
        // TODO: if no shared keys don't bother building indexes on intermediate result
        filteredOuter.wrapped.flatMap(outerTuple => filteredInner.wrapped.map( innerTuple => outerTuple.concat(innerTuple)))
      } else {
        val outerPosToUse = indexToUse.get._1.head
        val innerPosToUse = indexToUse.get._2.head

        // remove keys that do not have at least one condition in each tuple, and keep only the first condition (since self-constraints already filtered out)
        val secondaryKeys = relativeKeys.drop(1).collect {
          case (outerPos, innerPos) if outerPos.nonEmpty && innerPos.nonEmpty => (outerPos.head, innerPos.head)
        }
//        println(s"\t\tsecondary join positions: $secondaryKeys")

        filteredOuter.wrapped.flatMap(outerTuple =>
          val indexVal = outerTuple(outerPosToUse)
//          if !filteredInner.indexes.contains(innerPosToUse) then throw new Exception(s"Missing index on inner ${filteredInner.name} at position $innerPosToUse")
          val matchingInners = filteredInner.indexes(innerPosToUse).getOrElse(indexVal, ArrayBuffer.empty)
          matchingInners.collect {
            // check shared keys
            case innerTuple if (secondaryKeys.forall((outerPos, innerPos) => outerTuple(outerPos) == innerTuple(innerPos))) => {
              outerTuple.concat(innerTuple)
            }
          }
        )
      }

//    println(s"\tintermediateR=${result.map(_.mkString("(", ", ", ")")).mkString("[", ", ", "]")}")
    val combinedIndexes = indexes.keys ++ toJoin.indexes.keys.map(_ + arity)
    // TODO: filter out indexes that are no longer needed
    IndexedCollectionsEDB(result, combinedIndexes, s"${outer.name}x${inner.name}", arity + toJoin.arity)


  def factToString: String =
    val inner = wrapped.map(s => s.mkString("(", ", ", ")")).mkString("[", ", ", "]")
    val indexStr = ""//indexes.map((pos, tMap) => s"i$pos|${tMap.keys.size}|").mkString("{", ", ", "}") + ":"
    s"$indexStr$inner"

  def nonEmpty(): Boolean = wrapped.nonEmpty
  def length = wrapped.size

  // TODO: potentially remove IterableOnce, or restructure to use indexes with iterable ops "automatically"
  def map(f: IndexedCollectionsRow => IndexedCollectionsRow): IndexedCollectionsEDB = ???
  def filter(f: IndexedCollectionsRow => Boolean): IndexedCollectionsEDB = ???
  def flatMap(f: IndexedCollectionsRow => IterableOnce[IndexedCollectionsRow]): IndexedCollectionsEDB = ???
  def clear = ???
  def toSet = ???
  def apply = ???
  def mkString = ???
  def iterator = ???
}

object IndexedCollectionsEDB {
  extension (edbs: Seq[EDB])
    // TODO: merge indexes instead of rebuilding?
    def unionEDB: EDB =
      if (edbs.isEmpty)
        throw new Exception("Internal error, union on zero relations")
      else
        val head = asIndexedCollectionsEDB(edbs.head)
        IndexedCollectionsEDB(
          edbs.flatten(using e => asIndexedCollectionsEDB(e).wrapped).distinct.to(mutable.ArrayBuffer),
          head.indexes.keys,
          head.name,
          head.arity
        )

  /**
   * Create empty EDB, optionally preregister index keys if known ahead of time
   * @param preIndexes
   * @return
   */
  def empty(arity: Int, preIndexes: Iterable[Int] = Seq.empty, rName: String = "ANON"): IndexedCollectionsEDB =
    IndexedCollectionsEDB(mutable.ArrayBuffer[IndexedCollectionsRow](), preIndexes, rName, arity)

  /**
   * Copy the EDB, including the indexes, and any additional indexes to be maintained in `extras`
   * @param toCopy
   * @param extras
   * @return
   */
  def copyWithIndexes(toCopy: IndexedCollectionsEDB, extras: Iterable[Int] = Seq.empty): IndexedCollectionsEDB =
    val copy = IndexedCollectionsEDB(toCopy.wrapped.clone(), toCopy.indexes.keys ++ extras, toCopy.name, toCopy.arity)
    copy

  // Print methods
  def indexSizeToString(name: String, indexes: mutable.Map[Int, mutable.SortedMap[StorageTerm, mutable.ArrayBuffer[IndexedCollectionsRow]]]): String =
    s"$name: ${indexes.map((pos, tMap) => s"i$pos|${tMap.size}|").mkString("[", ", ", "]")}"

  def indexToString(index: mutable.SortedMap[StorageTerm, mutable.ArrayBuffer[IndexedCollectionsRow]]): String =
    index.toSeq.sortBy(_._1).map((term, matchingRows) =>
      s"$term => ${matchingRows.map(r => r.mkString("[", ", ", "]")).mkString("[", ", ", "]")}"
    ).mkString("{", ", ", "}")

  def allIndexesToString(edb: IndexedCollectionsEDB): String = {
    s"${edb.name}:\n\t${
      edb.indexes.toSeq.map((pos, index) =>
        s"@$pos: ${indexToString(index)}"
      ).mkString("", "\n\t", "")
    }"
  }
}

/**
 * Precise type for the Row (aka Tuple) type in IndexedCollectionsStorageManager.
 * Represents a single tuple within a relation, either EDB or IDB.
 * AKA a Seq[StorageTerm]
 */
case class IndexedCollectionsRow(wrappedS: Seq[StorageTerm]) extends Row[StorageTerm] { // TODO: update from Seq to ArrayBuffer?
  val wrapped = wrappedS.toIndexedSeq // noop if already IndexedSeq
  def toSeq = wrapped
  override def toString: String = wrapped.mkString("(", ", ", ")")
  def concat(suffix: Row[StorageTerm]): IndexedCollectionsRow =
    IndexedCollectionsRow(wrapped.concat(asIndexedCollectionsRow(suffix).wrapped))
  export wrapped.{ apply, iterator, lift, mkString, size }

  // Inlined and specialized applyOrElse to avoid significant boxing overhead.
  inline def applyOrElse(i: Int, inline default: Int => StorageTerm): StorageTerm =
    if i >= wrapped.size then default(i) else apply(i)

  def project(projIndexes: Seq[(String, Constant)]): IndexedCollectionsRow = // make a copy
    IndexedCollectionsRow(projIndexes.map((typ, idx) =>
      typ match {
        case "v" => wrapped(idx.asInstanceOf[Int])
        case "c" => idx
        case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
      }
    ).toIndexedSeq)

  /* Equality constraint $1 == $2 */
  inline def filterConstraint(keys: Seq[Int]): Boolean =
    keys.size <= 1 ||                    // there is no self-constraint, OR
      keys.drop(1).forall(idxToMatch =>  // all the self constraints hold
        wrapped(keys.head) == wrapped(idxToMatch)
      )

  /* Constant filter $1 = c */
  inline def filterConstant(consts: mutable.Map[Int, Constant]): Boolean =
    consts.isEmpty || consts.forall((idx, const) => // for each filter
      wrapped(idx) == const
    )
}

/**
 * Precise type for the Database type in IndexedCollectionsStorageManager.
 * Represents a DB containing a set of rows, i.e. tuples of terms.
 * AKA a mutable.Map[RelationId, ArrayBuffer[Seq[Term]]].
 */
case class IndexedCollectionsDatabase(wrapped: mutable.Map[RelationId, IndexedCollectionsEDB]) extends Database[IndexedCollectionsEDB] {
  export wrapped.{ apply, getOrElse, foreach, contains, update, exists, toSeq, forall }
}
object IndexedCollectionsDatabase {
  def apply(elems: (RelationId, IndexedCollectionsEDB)*): IndexedCollectionsDatabase = new IndexedCollectionsDatabase(mutable.Map[RelationId, IndexedCollectionsEDB](elems *))
}


