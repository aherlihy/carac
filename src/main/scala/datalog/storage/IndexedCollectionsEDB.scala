package datalog.storage

import datalog.dsl.{Constant, Variable}

import scala.collection.mutable
import IndexedCollectionsCasts.*
import datalog.execution.JoinIndexes
import datalog.storage.IndexedCollectionsEDB.{allIndexesToString, indexToString}

import scala.collection.immutable.ArraySeq
import scala.collection.mutable.ArrayBuffer
import java.util.{Arrays, HashMap, TreeMap}


/**
 * EDB for indexed collections-based storage.
 */

/* Necessary evil to avoid path dependent types. All casts go first-thing in the IndexedCollectionsStorageManager (or child)'s public methods that take an EDB as argument */
object IndexedCollectionsCasts {
  def asIndexedCollectionsEDB(to: Relation[StorageTerm]): IndexedCollectionsEDB = to.asInstanceOf[IndexedCollectionsEDB]
  def asIndexedCollectionsSeqEDB(to: Seq[Relation[StorageTerm]]): Seq[IndexedCollectionsEDB] = to.asInstanceOf[Seq[IndexedCollectionsEDB]]
  def asCollectionsRow(to: Row[StorageTerm]): CollectionsRow = to.asInstanceOf[CollectionsRow]
}

/**
 * Precise type for the EDB type in IndexedCollectionsStorageManager.
 * Represents one EDB relation, i.e. the set of rows of tuples in a particular EDB relation.
 * AKA mutable.SortedMap[Term, ArrayBuffer[Seq[StorageTerm]]] for each index key, for now ignore multi-key indexes
 */
case class IndexedCollectionsEDB(var wrapped: mutable.ArrayBuffer[CollectionsRow],
                                 indexKeys: mutable.BitSet,
                                 name: String,
                                 arity: Int,
                                 var skipIndexes: mutable.BitSet // don't build indexes for these keys
                                ) extends GeneralCollectionsEDB {
  import IndexedCollectionsEDB.{lookupOrCreate,lookupOrEmpty,IndexMap, insertInto}
  def emptyCopy: IndexedCollectionsEDB = IndexedCollectionsEDB.empty(arity, name, indexKeys, mutable.BitSet())

  if indexKeys.exists(_ >= arity) then throw new Exception(s"Error: creating edb $name with indexes ${indexKeys.mkString("[", ", ", "]")} but arity $arity")
  if skipIndexes.exists(_ >= arity) then throw new Exception(s"Error: creating edb $name but skiping indexes ${skipIndexes.mkString("[", ", ", "]")} but arity $arity")
  // TODO: distinguish between primary (one sortedMap of tuples), and secondary (one map of indexes)
  val indexes: Array[IndexMap] = new Array[IndexMap](arity)
  bulkRegisterIndex(indexKeys)

  inline def getIndex(idx: Int): IndexMap =
    if (skipIndexes.contains(idx)) throw new Exception(s"Error: trying to access data in $name with skipped index $idx")
    indexes(idx)

  private def createIndexMap(): IndexMap =
    // Customizing the initialCapacity and loadFactor parameters of the
    // constructor do not seem to improve performance at least on
    // datalog.benchmarks.examples.cspa10k_optimized.jit_indexed_sel__0_blocking_DELTA_lambda_EOL
    new HashMap()

  def bulkRegisterIndex(idxs: mutable.BitSet): this.type =
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
    if (indexes(idx) == null)
      val newIndex: IndexMap = createIndexMap()
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
    skipIndexes = mutable.BitSet()
    // rebuild indexes
    var i = 0
    while i < indexes.length do
      val oldIndex = indexes(i)
      if oldIndex != null then
        val newIndex: IndexMap = createIndexMap()
        wrapped.foreach(edb => newIndex.lookupOrCreate(edb(i)).addOne(edb))
        indexes(i) = newIndex
      i += 1
    this
  }

  /**
   * Bulk add data and update indexes if they exist
   * @param edbs
   * @return
   */
  def addAll(edbs: mutable.ArrayBuffer[CollectionsRow]): this.type = {
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
  def addAndDeduplicate(edbs: mutable.ArrayBuffer[CollectionsRow]): this.type = {
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
  def addOne(edb: CollectionsRow): this.type =
    if (edb.size != arity) throw new Exception(s"Adding row with length ${edb.size} to EDB $name with arity $arity")
    wrapped.addOne(edb)
    val keysToRebuild = indexKeys.diff(skipIndexes)
    keysToRebuild.foreach(idx =>
      indexes(idx).lookupOrCreate(edb(idx)).addOne(edb)
    )
    this

  def contains(edb: CollectionsRow): Boolean =
    var i = 0
    var smallestIdx = (-1, Int.MaxValue)
    while i < indexes.length do
      if (indexes(i) != null)
        val idxTerms = indexes(i).get(edb(i))
        val size = if idxTerms == null then 0 else idxTerms.size
        if (size < smallestIdx._2)
          smallestIdx = (i, size)
      i += 1
    if smallestIdx._1 == indexes.length || smallestIdx._1 == -1 then
      throw new Exception(s"Missing index on $name, no indexes found of $indexKeys")
    else
      val values = indexes(smallestIdx._1).get(edb(smallestIdx._1))
      values != null && containsRow(values, edb)

  // merge and deduplicate the passed indexes into the current EDB
  def mergeEDBs(toCopy: GeneralCollectionsEDB): Unit =
    val indexesToMerge = asIndexedCollectionsEDB(toCopy).indexes
    var i = 0
    var update = true
    while i < indexesToMerge.length do
      if (indexesToMerge(i) != null) // for each defined index
        if (indexes(i) == null)
          indexes(i) = createIndexMap()

        indexesToMerge(i).forEach((term, rowsToAdd) =>
          indexes(i).insertInto(term, rowsToAdd, wrapped, update, false) // only write to wrapped once
        )
        update = false
      i += 1

  private def containsRow(container: mutable.ArrayBuffer[CollectionsRow], row: CollectionsRow): Boolean =
    var i = 0
    val rowLength = row.length
    row.unsafeArray match
      case rowArray: Array[Int] =>
        while i < container.length do
          if Arrays.equals(container(i).unsafeArray.asInstanceOf[Array[Int]], rowArray) then
            return true
          i += 1
        end while
      case rowArray: Array[AnyRef] =>
        while i < container.length do
          if Arrays.equals(container(i).unsafeArray.asInstanceOf[Array[AnyRef]], rowArray) then
            return true
          i += 1
        end while
    false

  /**
   * Used only for getting a final result when we want to make sure to deduplicate results + get a set type
   * @return
   */
  def getSetOfSeq: Set[Seq[StorageTerm]] =
    wrapped.map(s => s.toSeq).toSet

  def projectAndDiff(constIndexes: mutable.Map[Int, Constant],
                     projIndexes: Seq[(String, Constant)],
                     newName: String,
                     newIndexes: mutable.BitSet,
                     derivedDB: CollectionsDatabase,
                     rId: RelationId
                    ): IndexedCollectionsEDB =
    val constFilter = constIndexes.filter((ind, _) => ind < arity)
//    println(s"constIdx=${constIndexes}, constFilter=$constFilter, arity=$arity, wrapped=$wrapped")
    val newWrapped = ArrayBuffer[CollectionsRow]()
    var rest: mutable.Map[Int, Constant] = null
    val toCopy = if (constFilter.isEmpty)
      wrapped
    else
      val (position, constant) = constFilter.head // TODO: pick best index, constFilter.map((idx, const) => (idx, indexes(idx)(const).size)).minBy(_._2)._1
      rest = constFilter.drop(1)
      indexes(position).lookupOrEmpty(constant)

    var i = 0
    while i < toCopy.length do
      val projected = toCopy(i).project(projIndexes)
//      println(s"projected=$projected checking derived(${newName})=${if derivedDB.contains(rId) then derivedDB(rId) else "X"}, constantTest=${rest == null || toCopy(i).filterConstant(rest)}")
//      println(s"projected=$projected, derivedDB.contains($rId)=${derivedDB.contains(rId)}, derivedDB($rId).contains=${derivedDB.contains(rId) && derivedDB(rId).contains(projected)}")
      if
        (rest == null || toCopy(i).filterConstant(rest)) &&                   // filter constants
          (!derivedDB.contains(rId) || !derivedDB(rId).contains(projected))   // diff with derived
      then
        newWrapped.addOne(projected)
      i += 1

    IndexedCollectionsEDB(
      newWrapped.distinct, // no duplicates within single query result, so just need to dep in union
      newIndexes,
      newName,
      projIndexes.length,
      mutable.BitSet()
    )

  /**
   * Assume this is the outer relation, toJoin is inner relation.
   * Merge join + filter but not project because project happens only once at the end
   *
   * @param joinIndexes
   * @param skip
   * @param toJoin
   * @return
   */
  def joinFilter(joinIndexes: JoinIndexes, skip: Int, toJoinG: GeneralCollectionsEDB): GeneralCollectionsEDB =
    val toJoin = asIndexedCollectionsEDB(toJoinG)
    var outer = this
    var inner = toJoin
    // TODO: check and potentially swap inner + outer, if onlineSort

    // get relevant join keys + filters for this subquery
    val outerConstantFilters = joinIndexes.constIndexes.filter((ind, _) => ind < outer.arity)
    val innerConstantFilters = joinIndexes.constIndexes.collect{ case (ind, c) if ind >= outer.arity && ind < outer.arity + inner.arity => (ind - outer.arity, c) }
    val keysInRange = joinIndexes.varIndexes.map(shared => shared.filter(_ < (outer.arity + inner.arity))).filter(_.nonEmpty)

//     println(s"\t2-way join($name*${toJoin.name}), allKeys=${joinIndexes.varIndexes},  relKeys=${keysInRange.mkString("[", ", ", "]")}, outerConstants=${outerConstantFilters.mkString("{", ", ", "}")}, innerConstants=${innerConstantFilters.mkString("{", ", ", "}")}")
//     println(s"\tinput rels=${wrapped.mkString("[",",","]")} * ${toJoin.wrapped.mkString("[",",","]")}")
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
        val filtered = outer.getIndex(outerConstantFilters.head._1).lookupOrEmpty(outerConstantFilters.head._2).
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
          val withInnerKey = if keyToJoin.isEmpty then mutable.BitSet() else inner.indexKeys.filter(_ != keyToJoin.get._2)
          IndexedCollectionsEDB(filtered, inner.indexKeys, inner.name, inner.arity, withInnerKey) // do not rebuild indexes on anything but join key
      else // need to both filter + maybe self constrain
        val filtered = inner.getIndex(innerConstantFilters.head._1).lookupOrEmpty(innerConstantFilters.head._2).
          filter(innerTuple =>
            innerTuple.filterConstant(innerConstantFilters.drop(1)) && relativeKeys.forall((_, innerKeys) => innerTuple.filterConstraint(innerKeys))
          )
        val withInnerKey = if keyToJoin.isEmpty then mutable.BitSet() else inner.indexKeys.filter(_ != keyToJoin.get._2)
        IndexedCollectionsEDB(filtered, inner.indexKeys, inner.name, inner.arity, withInnerKey)
//    println(s"filteredInner=${filteredInner.wrapped.mkString("[", ", ", "]")}")

    val result = if (keyToJoin.isEmpty) // join without keys, so just loop over everything without index
      filteredOuter.wrapped.flatMap(outerTuple => filteredInner.wrapped.map( innerTuple => outerTuple.concat(innerTuple)))
    else
      val outerPosToUse = keyToJoin.get._1
      val innerPosToUse = keyToJoin.get._2
      if (relativeJoinKeys.isEmpty) { // no shared join keys, so just filter by internal constraint
        filteredOuter.wrapped.flatMap(outerTuple => filteredInner.wrapped.map( innerTuple => outerTuple.concat(innerTuple)))
      } else {
        // TODO: picking first constraint, should pick best constraint
        // remove keys that do not have at least one condition in each tuple, and keep only the first condition (since self-constraints already filtered out)
        val secondaryKeys = relativeJoinKeys.drop(1)
//        println(s"\t\tprimary join outer[$outerPosToUse], inner[$innerPosToUse], secondary join positions: $secondaryKeys")

        val res = filteredOuter.wrapped.flatMap(outerTuple =>
          val indexVal = outerTuple(outerPosToUse)
          if !filteredInner.indexKeys.contains(innerPosToUse) then throw new Exception(s"Missing index on inner ${filteredInner.name} at position $innerPosToUse, expected initial indexes=${filteredInner.indexKeys.mkString("[", ", ", "]")}, skippedIndexes=${filteredInner.skipIndexes.mkString("[", ", ", "]")}")
          val matchingInners = filteredInner.getIndex(keyToJoin.get._2).lookupOrEmpty(indexVal)
          matchingInners.collect {
            // check shared keys
            case innerTuple if (secondaryKeys.forall((outerPos, innerPos) => outerTuple(outerPos) == innerTuple(innerPos))) => {
              outerTuple.concat(innerTuple)
            }
          }
        )
//        println(s"res=${res}")
        res
      }

//    println(s"\tintermediateR=${result.map(_.mkString("(", ", ", ")")).mkString("[", ", ", "]")}")
    val combinedIndexes = outer.indexKeys ++ inner.indexKeys.map(_ + arity)
    IndexedCollectionsEDB(
      result,//.distinct, // TODO: benchmark if better to do this on intermediate results or at union level
      combinedIndexes,
      s"${outer.name}x${inner.name}",
      arity + toJoin.arity,
      combinedIndexes // do not build indexes on intermediate results because will always be outer, and other than the first join all constant filters will be on the inner. Indexes will be rebuild on final project
    )

  def diff(toDiff: EDB): GeneralCollectionsEDB =
    val edbToDiff = asIndexedCollectionsEDB(toDiff)
    val newWrapped = wrapped.filter(edb => !edbToDiff.contains(edb))
    IndexedCollectionsEDB(
      newWrapped.distinct,
      indexKeys,
      name,
      arity,
      skipIndexes
    )

  def factToString: String =
    val inner = wrapped.sorted.map(s => s.mkString("(", ", ", ")")).mkString("[", ", ", "]")
    // if you want to print indexes directly, uncomment
    val indexStr = indexes.map(idx => indexToString(idx)).mkString("{", ", ", "}") + ":"
    s"$indexStr$inner"

  def nonEmpty: Boolean = wrapped.nonEmpty
  def length = wrapped.size

  def clear(): Unit =
    wrapped.clear()
    indexKeys.foreach(i => indexes(i).clear())
    skipIndexes.clear()

  // TODO: potentially remove IterableOnce, or restructure to use indexes with iterable ops "automatically"
  def map(f: CollectionsRow => CollectionsRow): IndexedCollectionsEDB = ???
  def filter(f: CollectionsRow => Boolean): IndexedCollectionsEDB = ???
  def flatMap(f: CollectionsRow => IterableOnce[CollectionsRow]): IndexedCollectionsEDB = ???
  def toSet = ???
  def apply = ???
  def mkString = ???
  def iterator = ???
}

object IndexedCollectionsEDB {
  type IndexMap = HashMap[StorageTerm, ArrayBuffer[CollectionsRow]]
  extension (index: IndexMap)
    def lookupOrCreate(key: StorageTerm): ArrayBuffer[CollectionsRow] =
      // TODO: tune initialSize?
      index.computeIfAbsent(key, _ => new mutable.ArrayBuffer(initialSize = 16))
    def lookupOrEmpty(key: StorageTerm): ArrayBuffer[CollectionsRow] =
      val valueOrNull = index.get(key)
      if valueOrNull != null then
        valueOrNull
      else
        // TODO: tune initialSize?
        new mutable.ArrayBuffer(initialSize = 16)

    def insertInto(key: StorageTerm,
                   toAdd: ArrayBuffer[CollectionsRow],
                   wrappedToModify: ArrayBuffer[CollectionsRow],
                   update: Boolean,
                   deduplicate: Boolean) =
      val valueOrNull = index.get(key)
      if valueOrNull != null then // index is defined, so need to ensure no duplicates
        val dedup = if deduplicate then toAdd.filter(row => !valueOrNull.contains(row)) else toAdd
        valueOrNull.addAll(dedup)
        if update then wrappedToModify.addAll(dedup)
      else
        index.put(key, ArrayBuffer.from(toAdd))
        if update then wrappedToModify.addAll(toAdd) // TODO: need to deduplicate here as well?

    inline def contains(key: StorageTerm): Boolean =
      index.containsKey(key)
    inline def foreach(f: java.util.function.BiConsumer[StorageTerm, ArrayBuffer[CollectionsRow]]): Unit =
      index.forEach(f)
    inline def clear(): Unit =
      index.clear()

  extension (edbs: Seq[EDB])
    def unionEDB: EDB =
      if (edbs.isEmpty)
        throw new Exception("Internal error, union on zero relations")
      else if (edbs.size == 1)
        edbs.head
      else
        val head = asIndexedCollectionsEDB(edbs.head)
        IndexedCollectionsEDB(
          edbs.flatten(using e => asIndexedCollectionsEDB(e).wrapped).distinct.to(mutable.ArrayBuffer),
          head.indexKeys,
          head.name,
          head.arity,
          mutable.BitSet()//.indexKeys // don't skip any of used by diff?
        )

  /**
   * Create empty EDB, optionally preregister index keys if known ahead of time
   * @param preIndexes
   * @return
   */
  def empty(arity: Int, rName: String = "ANON", preIndexes: mutable.BitSet = mutable.BitSet(), skipIndexes: mutable.BitSet = mutable.BitSet()): IndexedCollectionsEDB =
    IndexedCollectionsEDB(mutable.ArrayBuffer[CollectionsRow](), preIndexes, rName, arity, skipIndexes)

  // Print methods
  def indexSizeToString(name: String, indexedCollectionsEDB: IndexedCollectionsEDB): String =
    s"  $name: ${indexedCollectionsEDB.indexKeys.map(pos => s"i$pos|${
      if indexedCollectionsEDB.skipIndexes.contains(pos) then "[X]" else indexedCollectionsEDB.getIndex(pos).size
    }|").mkString("[", ", ", "]")}"

  def indexToString(index: IndexMap): String =
    import scala.jdk.CollectionConverters.*
    if index == null then "null" else
      index.asScala.toSeq.sortBy(_._1).map((term, matchingRows) =>
        s"$term => ${matchingRows.map(r => r.mkString("[", ", ", "]")).mkString("[", ", ", "]")}"
      ).mkString("{", ", ", "}")

  def allIndexesToString(e: GeneralCollectionsEDB): String = {
    val edb = asIndexedCollectionsEDB(e)
    s"  ${edb.name}:\n\t${
      edb.indexes.zipWithIndex.map((index, pos) =>
        s"@$pos: ${
          if (edb.skipIndexes.contains(pos))
            "[SKIP]"
          else
            indexToString(index)
        }"
      ).mkString("  ", "\n\t  ", "")
    }"
  }
}


