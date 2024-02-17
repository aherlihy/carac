package datalog.storage

import datalog.dsl.{Constant, Variable}

import scala.collection.mutable
import IndexedCollectionsCasts.*
import datalog.execution.JoinIndexes

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
                                 rName: String,
                                 arity: Int) extends EDB with IterableOnce[IndexedCollectionsRow] {
  // position => index, where index is term => tuple
  // TODO: distinguish between primary (one sortedMap of tuples), and secondary (one map of indexes)
  // TODO: for now keep all indexes updated, but add flag to not bother rebuilding

  var name: String = rName // TODO: clean up, for now used for debugging
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
    // TODO: potentially build indexes async, or turn of index update
    wrapped.addAll(edbs)
    edbs.foreach(edb =>
      indexes.foreach((idx, index) => // TODO: what to do with duplicates?
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
    wrapped.addOne(edb)
    indexes.foreach((idx, index) =>
      // TODO: what to do with duplicates?
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
   * Combine EDBs, maintaining the indexes of each. TODO: Indexes should not ever be different?
   * @param suffix
   * @return
   */
  def concat(suffix: IndexedCollectionsEDB): IndexedCollectionsEDB =
    val copy = IndexedCollectionsEDB.copyWithIndexes(this, suffix.indexes.keys) // TODO: do we really need a new copy here? do we need both indexes?
    copy.addAll(suffix.wrapped)
    copy.name = name
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
  def filterProjectWithIndex(constIndexes: mutable.Map[Int, Constant],
                             projIndexes: Seq[(String, Constant)],
                             skip: Int): IndexedCollectionsEDB =
    val constFilter = constIndexes.filter((ind, _) => ind < arity)
    if (constFilter.isEmpty)
      if (projIndexes.isEmpty)
        this // TODO: no need to make a copy if no filter or project needed, so retain indexes?
      else
        IndexedCollectionsEDB(wrapped.map(_.project(projIndexes)), indexes.keys, name, projIndexes.length) // TODO: only project so just copy, no indexes
    else
      // TODO: If multiple pick the most selective (smallest), but for now just pick first until i can verify not too expensive to check
      // mostSelectiveIdx = constFilter.map((idx, const) => (idx, indexes(idx)(const).size)).minBy(_._2)._1
      // TODO: is it better to use index on each condition and get intersection?
      val index = constFilter.head
      val rest = constFilter.drop(1)

      val result = indexes(index._1)(index._2).collect{// copy matching EBDs
        // TODO: verify lowerBound == skip
        case edb if IndexedCollectionsEDB.filtertest(rest, skip, edb) => edb.project(projIndexes)
      }
//      TODO: this will make a copy with the indexes
      val newArity = if projIndexes.isEmpty then arity else projIndexes.length
      IndexedCollectionsEDB(result, indexes.keys, name, newArity)
//      TODO: this will make a copy with NO indexes
//      IndexedCollectionsEDB(result, Seq[Int](), name)

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

    val outerConstantFilter = joinIndexes.constIndexes.filter((ind, _) => ind < outer.arity)
    val innerConstantFilter = joinIndexes.constIndexes.filter((ind, _) => ind >= outer.arity && ind < outer.arity + inner.arity)
    // if any of the indexes are past the end of the current tuple, ignore and wait til later
    // TODO: potentially do join with only the in-range indexes?
    val joinKeys = joinIndexes.varIndexes.filter(shared => shared.forall(_ < outer.arity + inner.arity))
    println(s"\t2-way join($name*${toJoin.name}), relevant keys=${joinKeys.mkString("[", ", ", "]")}, outerConstants=${outerConstantFilter.mkString("{", ", ", "}")}, innerConstants=${innerConstantFilter.mkString("{", ", ", "}")}")

//    println(s"$name*${toJoin.name}: on ${joinKeys}, outerArity=${outer.arity}, innerArity=${inner.arity}")

    // TODO: filter first to cut down size, but have to rebuild indexes, below. Alternatively could join and filter as we go
    val filteredOuter =
      if (outerConstantFilter.isEmpty)
        this
      else
        val filtered = outer.indexes(outerConstantFilter.head._1)(outerConstantFilter.head._2).
          filter(e => IndexedCollectionsEDB.filtertest(outerConstantFilter.drop(1), 0, e))
        IndexedCollectionsEDB(filtered, outer.indexes.keys, outer.name, outer.arity) // TODO: Do not actually need indexes on outer relation
    val filteredInner =
      if (innerConstantFilter.isEmpty)
        toJoin
      else
        val filtered = inner.indexes(innerConstantFilter.head._1)(innerConstantFilter.head._2).
          filter(e => IndexedCollectionsEDB.filtertest(innerConstantFilter.drop(1), outer.arity, e))
        IndexedCollectionsEDB(filtered, inner.indexes.keys, inner.name, inner.arity)

    val result = if (joinKeys.isEmpty) // join without keys, so just loop over everything without index
      println("\t\tjoin without condition, -> product")
      filteredOuter.wrapped.flatMap(outerTuple => filteredInner.wrapped.map( innerTuple => outerTuple.concat(innerTuple)))
    else if (joinKeys.length == 1 && joinKeys.head.length == 2) // most of the time
      val outerKey = joinKeys.head.head
      val innerKey = joinKeys.head(1) - outer.arity
      println(s"\t\tsingle condition: joinKeys relative position 1:$outerKey, 2:$innerKey")

      filteredOuter.wrapped.flatMap(outerTuple =>
        // TODO: handle when join indexes are both past the end of the first tuple
        val indexVal = outerTuple(outerKey)
        val matchingInners = filteredInner.indexes(innerKey).getOrElse(indexVal, ArrayBuffer.empty)
        println(s"outerTuple[$outerKey]=$indexVal, matching innerTuple[$innerKey]=${matchingInners.mkString("[", ", ", "]")}")
        matchingInners.map(innerTuple =>
          outerTuple.concat(innerTuple)
        )
      )
    else
//      println("==============> bad join")
      throw new Exception("here")
      // multi-key join, so TODO: pick most selective index?
      // TODO: for now revert to `toJoin`
      filteredOuter.wrapped.flatMap(outerTuple =>
        // TODO: write more efficiently, using indexes
        filteredInner.wrapped.filter(innerTuple =>
          IndexedCollectionsEDB.tojoin(joinKeys, innerTuple, outerTuple)
        ).map(
          innerTuple => outerTuple.concat(innerTuple)
        )
      )
    // TODO: only re-add indexes after this point? Or all?
    IndexedCollectionsEDB(result, indexes.keys, s"${outer.name}x${inner.name}", arity + toJoin.arity)

  def map(f: IndexedCollectionsRow => IndexedCollectionsRow): IndexedCollectionsEDB = ???
//    IndexedCollectionsEDB(wrapped.map(e => f(e)))

  def filter(f: IndexedCollectionsRow => Boolean): IndexedCollectionsEDB = ???
//    IndexedCollectionsEDB(wrapped.filter(f))

  def flatMap(f: IndexedCollectionsRow => IterableOnce[IndexedCollectionsRow]): IndexedCollectionsEDB = ???
//    IndexedCollectionsEDB(wrapped.flatMap(e => f(e)))

  def factToString: String =
    val inner = wrapped.map(s => s.mkString("(", ", ", ")")).mkString("[", ", ", "]")
    val indexStr = indexes.map((pos, tMap) => s"i$pos|${tMap.keys.size}|").mkString("{", ", ", "}")
    s"$indexStr:$inner"

  // TODO: potentially remove IterableOnce, or restructure to use indexes with iterable ops "automatically"
  def length = ???
  def clear = ???
  def toSet = ???
  def apply = ???
  def mkString = ???
  def iterator = ???
  def nonEmpty(): Boolean =
    println(s"checking empty on $name, wrapped=${wrapped.length}")
    wrapped.nonEmpty
}

object IndexedCollectionsEDB {
  extension (edbs: Seq[EDB])
    // TODO: do something more efficient than rebuilding indexes for all the unioned EDBs
    def unionEDB: EDB =
      if (edbs.isEmpty)
        throw new Exception("Internal error, union on zero relations")
      else
        val output = IndexedCollectionsEDB.copyWithIndexes(asIndexedCollectionsEDB(edbs.head))
        output.addAll(edbs.drop(1).flatten(using e => asIndexedCollectionsEDB(e).wrapped).distinct.to(mutable.ArrayBuffer))

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

  inline def filtertest(consts: mutable.Map[Int, Constant], skip: Int, row: IndexedCollectionsRow): Boolean = {
    consts.isEmpty || consts.forall((idx, const) => // for each filter
      row(idx - skip) == const
    )
  }

  inline def tojoin(varIndexes: Seq[Seq[Int]], innerTuple: IndexedCollectionsRow, outerTuple: IndexedCollectionsRow): Boolean = {
    varIndexes.forall(condition =>
      val toCompare = innerTuple.applyOrElse(condition.head, j => outerTuple(j - innerTuple.length))
      condition.drop(1).forall(idx =>
//          idx >= innerTuple.length + outerTuple.length ||
          innerTuple.applyOrElse(idx, j => outerTuple(j - innerTuple.length)) == toCompare
      )
    )
  }

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
  def length: Int = wrapped.size
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


