package datalog.storage

import datalog.dsl.{Constant, Variable}

import scala.collection.mutable
import IndexedCollectionsCasts.*
import datalog.execution.JoinIndexes
import datalog.storage.IndexedCollectionsEDB.allIndexesToString

import scala.collection.mutable.ArrayBuffer
import java.util.TreeMap


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
case class IndexedCollectionsEDB(var wrapped: mutable.ArrayBuffer[IndexedCollectionsRow],
                                 indexKeys: mutable.BitSet,
                                 name: String,
                                 arity: Int,
                                 var skipIndexes: mutable.Set[Int] // don't build indexes for these keys
                                ) extends EDB with IterableOnce[IndexedCollectionsRow] {
  import IndexedCollectionsEDB.*

  if indexKeys.exists(_ >= arity) then throw new Exception(s"Error: creating edb $name with indexes ${indexKeys.mkString("[", ", ", "]")} but arity $arity")
  if skipIndexes.exists(_ >= arity) then throw new Exception(s"Error: creating edb $name but skiping indexes ${skipIndexes.mkString("[", ", ", "]")} but arity $arity")
  // TODO: distinguish between primary (one sortedMap of tuples), and secondary (one map of indexes)
  private val indexes: mutable.Map[Int, IndexMap] = mutable.Map[Int, IndexMap]()
  bulkRegisterIndex(indexKeys)

  inline def getIndex(idx: Int): TreeMap[StorageTerm, ArrayBuffer[IndexedCollectionsRow]] =
    if (skipIndexes.contains(idx)) throw new Exception(s"Error: trying to access data in $name with skipped index $idx")
    indexes(idx)

  def bulkRegisterIndex(idxs: mutable.Set[Int]): this.type =
    idxs.foreach(registerIndex)
    this
  /**
   * Add new index key. If data already exists, build the index immediately
   * @param idx
   * @return
   */
  def registerIndex(idx: Int): this.type = {
    // TODO: potentially build indexes async
    indexKeys.addOne(idx)
    if (!indexes.contains(idx))
      val newIndex: IndexMap = TreeMap()
      if (!skipIndexes.contains(idx))
        wrapped.foreach(edb => newIndex.lookupOrCreate(edb(idx)).addOne(edb))
      indexes(idx) = newIndex
    this
  }

  def skipIndex(idx: Int): this.type = {
    skipIndexes.addOne(idx)
    this
  }

  def bulkRebuildIndex(): this.type = {
    skipIndexes = mutable.Set()
    // rebuild indexes
    indexes.keys.foreach(idx =>
      val newIndex: IndexMap = TreeMap()
      wrapped.foreach(edb => newIndex.lookupOrCreate(edb(idx)).addOne(edb))
      indexes(idx) = newIndex
    )
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
    val keysToRebuild = indexKeys.diff(skipIndexes)
    edbs.foreach(edb =>
      keysToRebuild.foreach(idx =>
        indexes(idx).lookupOrCreate(edb(idx)).addOne(edb)
      )
    )
    this
  }

  /**
   * Bulk add data and update indexes if they exist
   *
   * @param edbs
   * @return
   */
  def addAndDeduplicate(edbs: mutable.ArrayBuffer[IndexedCollectionsRow]): this.type = {
    val keysToRebuild = indexKeys.diff(skipIndexes)
    edbs.foreach(edb =>
      if (!contains(edb)) {
        wrapped.addOne(edb)
        keysToRebuild.foreach(idx =>
          indexes(idx).lookupOrCreate(edb(idx)).addOne(edb)
        )
      }
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
    val keysToRebuild = indexKeys.diff(skipIndexes)
    keysToRebuild.foreach(idx =>
      indexes(idx).lookupOrCreate(edb(idx)).addOne(edb)
    )
    this

  def contains(edb: IndexedCollectionsRow): Boolean =
    if (indexes.isEmpty)
      wrapped.contains(edb)
    else
      val i = indexes.head._1 // for now take first index and use it to filter, TODO: use smallest
      val values = indexes(i).get(edb(i))
      values != null && values.contains(edb)

  /**
   * Removes that from this. TODO: in-place ok bc only at end of iteration?
   */
  def diffInPlace(that: IndexedCollectionsEDB): IndexedCollectionsEDB =
    this.wrapped = wrapped.diff(that.wrapped)
    indexes.foreach((idx, index) =>
      index.foreach((term, tuples) =>
        tuples.filterInPlace(tuple =>
          val include = !that.contains(tuple)
          include
        )
      )
    )
    this
  def diff(that: IndexedCollectionsEDB): IndexedCollectionsEDB =
    val res = wrapped.diff(that.wrapped)
    IndexedCollectionsEDB(res, indexKeys, name, arity, indexKeys) // should be no indexes bc will become delta.new, be checked for loop, and cleared

  /**
   * Combine EDBs, maintaining the indexes of each.
   * @param suffix
   * @return
   */
  // TODO: maybe use insert not copy, except problem is that rules is delta.new, and prev is derived.known. Delta cannot mutate because needed for the end-of-iteration check, and derived cannot mutate (?) because needed to be read by other rules potentially
  def copyAndAdd(suffix: IndexedCollectionsEDB): IndexedCollectionsEDB =
    val copy = IndexedCollectionsEDB.copyWithIndexes(this, suffix.indexKeys)
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
   * @return
   */
  def projectFilterWithIndex(constIndexes: mutable.Map[Int, Constant],
                             projIndexes: Seq[(String, Constant)],
                             newName: String,
                             newIndexes: mutable.BitSet): IndexedCollectionsEDB =
    val constFilter = constIndexes.filter((ind, _) => ind < arity)
    if (constFilter.isEmpty)
      if (projIndexes.isEmpty)
        this
      else
        IndexedCollectionsEDB(wrapped.map(_.project(projIndexes)), newIndexes, newName, projIndexes.length, newIndexes) // should build no indexes bc result of project
    else
      // TODO: is it better to use index on each condition and get intersection?
      // TODO: If multiple pick the most selective (smallest), but for now just pick first until i can verify not too expensive to check
      val (position, constant) = constFilter.head // constFilter.map((idx, const) => (idx, indexes(idx)(const).size)).minBy(_._2)._1
      val rest = constFilter.drop(1)

      val result = indexes(position).lookupOrCreate(constant).collect{// copy matching EBDs
        case edb if edb.filterConstant(rest) => edb.project(projIndexes)
      }
      val newArity = if projIndexes.isEmpty then arity else projIndexes.length
      IndexedCollectionsEDB(result, newIndexes, newName, newArity, newIndexes) // do not rebuild indexes after project

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
    val keysInRange = joinIndexes.varIndexes.map(shared => shared.filter(_ < (outer.arity + inner.arity))).filter(_.nonEmpty)

//     println(s"\t2-way join($name*${toJoin.name}), allKeys=${joinIndexes.varIndexes},  relKeys=${keysInRange.mkString("[", ", ", "]")}, outerConstants=${outerConstantFilters.mkString("{", ", ", "}")}, innerConstants=${innerConstantFilters.mkString("{", ", ", "}")}")
//     println(s"innerIndexes=${IndexedCollectionsEDB.allIndexesToString(toJoin)}")

    // store relative positions: [ (innerPos, outerPos), ...] including self-constraints
    val relativeKeys = keysInRange.map(k =>
      (k.filter(_ < outer.arity), k.collect { case i if i >= outer.arity && i < outer.arity + inner.arity => i - outer.arity })
    ).filter((outerPos, innerPos) => outerPos.size + innerPos.size > 1)
    // relative positions of only keys shared between relations, drop all but 1 key (TODO: use best index, for now use head)
    val relativeJoinKeys = relativeKeys.collect {
      case (outerPos, innerPos) if outerPos.nonEmpty && innerPos.nonEmpty => (outerPos.head, innerPos.head)
    }
    val keyToJoin = relativeJoinKeys.headOption // TODO: get "best" index"

//    println(s"\t\trelative join positions = ${relativeKeys.map((o, i) => s"(outers: ${o.mkString("", ".", "")}, inners: ${i.mkString("", ".", "")})").mkString("[", ", ", "]")}")

    // Filter first to cut down size, but have to rebuild indexes, but only in 1st round
    val filteredOuter = if (skip > 1) outer else // only filter outer if first 2-way join
      if (outerConstantFilters.isEmpty)
        if (relativeKeys.forall((outerKeys, _) => outerKeys.length <= 1)) // no constant filters, no self constraints:
          outer
        else // need to self-constrain only:
          val filtered = outer.wrapped.filter(outerTuple =>
            relativeKeys.forall((outerKeys, _) => outerTuple.filterConstraint(outerKeys))
          )
          IndexedCollectionsEDB(filtered, outer.indexKeys, outer.name, outer.arity, outer.indexKeys) // don't need index on outer
      else // need to both filter + maybe self constrain
        val filtered = outer.getIndex(outerConstantFilters.head._1).lookupOrCreate(outerConstantFilters.head._2).
          filter(outerTuple =>
            outerTuple.filterConstant(outerConstantFilters.drop(1)) && relativeKeys.forall((outerKeys, _) => outerTuple.filterConstraint(outerKeys))
          )
        IndexedCollectionsEDB(filtered, outer.indexKeys, outer.name, outer.arity, outer.indexKeys) // don't need index on outer

    val filteredInner =
      if (innerConstantFilters.isEmpty)
        if (relativeKeys.forall((_, innerKeys) => innerKeys.length <= 1)) // no constant filters, no self constraints:
          inner
        else // need to self-constrain:
          val filtered = inner.wrapped.filter(innerTuple =>
            relativeKeys.forall((_, innerKeys) => innerTuple.filterConstraint(innerKeys))
          )
          val withInnerKey = if keyToJoin.isEmpty then mutable.Set() else inner.indexKeys.filter(_ != keyToJoin.get._2)
          IndexedCollectionsEDB(filtered, inner.indexKeys, inner.name, inner.arity, withInnerKey) // do not rebuild indexes on anything but join key
      else // need to both filter + maybe self constrain
        val filtered = inner.getIndex(innerConstantFilters.head._1).lookupOrCreate(innerConstantFilters.head._2).
          filter(innerTuple =>
            innerTuple.filterConstant(innerConstantFilters.drop(1)) && relativeKeys.forall((_, innerKeys) => innerTuple.filterConstraint(innerKeys))
          )
        val withInnerKey = if keyToJoin.isEmpty then mutable.Set() else inner.indexKeys.filter(_ != keyToJoin.get._2)
        IndexedCollectionsEDB(filtered, inner.indexKeys, inner.name, inner.arity, withInnerKey)

    val result = if (keyToJoin.isEmpty) // join without keys, so just loop over everything without index
      filteredOuter.wrapped.flatMap(outerTuple => filteredInner.wrapped.map( innerTuple => outerTuple.concat(innerTuple)))
    else
      val outerPosToUse = keyToJoin.get._1
      val innerPosToUse = keyToJoin.get._2
      if (relativeJoinKeys.isEmpty) { // no shared join keys, so just filter by internal constraint
        // TODO: if no shared keys don't bother building indexes on intermediate result
        filteredOuter.wrapped.flatMap(outerTuple => filteredInner.wrapped.map( innerTuple => outerTuple.concat(innerTuple)))
      } else {
        // TODO: picking first constraint, should pick best constraint
        // remove keys that do not have at least one condition in each tuple, and keep only the first condition (since self-constraints already filtered out)
        val secondaryKeys = relativeJoinKeys.drop(1) // TODO: remove indexToUse if not first
//        println(s"\t\tprimary join outer[$outerPosToUse], inner[$innerPosToUse], secondary join positions: $secondaryKeys")

        filteredOuter.wrapped.flatMap(outerTuple =>
          val indexVal = outerTuple(outerPosToUse)
          if !filteredInner.indexKeys.contains(innerPosToUse) then throw new Exception(s"Missing index on inner ${filteredInner.name} at position $innerPosToUse, expected initial indexes=${filteredInner.indexKeys.mkString("[", ", ", "]")}, skippedIndexes=${filteredInner.skipIndexes.mkString("[", ", ", "]")}")
          val matchingInners = filteredInner.getIndex(keyToJoin.get._2).lookupOrCreate(indexVal)
          matchingInners.collect {
            // check shared keys
            case innerTuple if (secondaryKeys.forall((outerPos, innerPos) => outerTuple(outerPos) == innerTuple(innerPos))) => {
              outerTuple.concat(innerTuple)
            }
          }
        )
      }

//    println(s"\tintermediateR=${result.map(_.mkString("(", ", ", ")")).mkString("[", ", ", "]")}")
    val combinedIndexes = outer.indexKeys ++ inner.indexKeys.map(_ + arity)
    IndexedCollectionsEDB(result, combinedIndexes, s"${outer.name}x${inner.name}", arity + toJoin.arity, combinedIndexes)


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
  type IndexMap = TreeMap[StorageTerm, ArrayBuffer[IndexedCollectionsRow]]
  extension (index: IndexMap)
    def lookupOrCreate(key: StorageTerm): ArrayBuffer[IndexedCollectionsRow] =
      // TODO: tune initialSize?
      index.computeIfAbsent(key, _ => new mutable.ArrayBuffer(initialSize = 16))
    inline def contains(key: StorageTerm): Boolean =
      index.containsKey(key)
    inline def foreach(f: java.util.function.BiConsumer[StorageTerm, ArrayBuffer[IndexedCollectionsRow]]): Unit =
      index.forEach(f)

  extension (edbs: Seq[EDB])
    def unionEDB: EDB =
      if (edbs.isEmpty)
        throw new Exception("Internal error, union on zero relations")
      else
        val head = asIndexedCollectionsEDB(edbs.head)
        IndexedCollectionsEDB(
          edbs.flatten(using e => asIndexedCollectionsEDB(e).wrapped).distinct.to(mutable.ArrayBuffer),
          head.indexKeys,
          head.name,
          head.arity,
          head.indexKeys // don't skip any of used by diff?
        )

    // TODO: merge individual indexes instead of rebuilding?
  extension (edbs: Seq[EDB])
    def unionInPlace: EDB =
      if (edbs.isEmpty)
        throw new Exception("Internal error, union on zero relations")
      else
        val head = asIndexedCollectionsEDB(edbs.head)
        edbs.foreach(e =>
          head.addAndDeduplicate(asIndexedCollectionsEDB(e).wrapped)
        )
        head


  /**
   * Create empty EDB, optionally preregister index keys if known ahead of time
   * @param preIndexes
   * @return
   */
  def empty(arity: Int, preIndexes: mutable.BitSet = mutable.BitSet(), rName: String = "ANON", skipIndexes: mutable.BitSet): IndexedCollectionsEDB =
    IndexedCollectionsEDB(mutable.ArrayBuffer[IndexedCollectionsRow](), preIndexes, rName, arity, skipIndexes)

  /**
   * Copy the EDB, including the indexes, and any additional indexes to be maintained in `extras`
   * @param toCopy
   * @param extras
   * @return
   */
  def copyWithIndexes(toCopy: IndexedCollectionsEDB, extras: Iterable[Int] = Seq.empty): IndexedCollectionsEDB =
    val copy = IndexedCollectionsEDB(toCopy.wrapped.clone(), toCopy.indexKeys ++ extras, toCopy.name, toCopy.arity, toCopy.skipIndexes)
    copy

  // Print methods
  def indexSizeToString(name: String, indexedCollectionsEDB: IndexedCollectionsEDB): String =
    s"$name: ${indexedCollectionsEDB.indexKeys.map(pos => s"i$pos|${indexedCollectionsEDB.getIndex(pos).size}|").mkString("[", ", ", "]")}"

  def indexToString(index: IndexMap): String =
    import scala.jdk.CollectionConverters.*
    index.asScala.toSeq.sortBy(_._1).map((term, matchingRows) =>
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


