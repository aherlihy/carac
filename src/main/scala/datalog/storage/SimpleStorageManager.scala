package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}

import scala.collection.{immutable, mutable}

class SimpleStorageManager(using ns: mutable.Map[Int, String]) extends StorageManager {
  type StorageVariable = Variable
  type StorageConstant = Constant
  case class StorageAtom(rId: Int, terms: IndexedSeq[StorageTerm]) {
    override def toString: String = ns(rId) + terms.mkString("(", ", ", ")")
  }
  type Row[+T] = IndexedSeq[T]
  def Row[T](c: T*) = IndexedSeq[T](c: _*)
  type Table[T] = mutable.ArrayBuffer[T]
  def Table[T](r: T*) = mutable.ArrayBuffer[T](r: _*)
  type Relation[T] = Table[Row[T]]
  def Relation[T](c: Row[T]*) = Table[Row[T]](c: _*)

  type Database[K, V] = mutable.Map[K, V]

  type FactDatabase = Database[Int, EDB]
  def FactDatabase(e: (Int, EDB)*) = mutable.Map[Int, EDB](e: _*)
  type RuleDatabase = Database[Int, IDB]
  def RuleDatabase(e: (Int, IDB)*) = mutable.Map[Int, IDB](e: _*)

  def EDB(c: Row[StorageTerm]*) = Relation[StorageTerm](c: _*)
  def IDB(c: Row[StorageAtom]*) = Relation[StorageAtom](c: _*)

  // "database", i.e. relationID => Relation
  val edbs: FactDatabase = FactDatabase()
  val idbs: RuleDatabase = RuleDatabase()

  // queryID => database
  var increment = 0
  val incrementalDB: Database[Int, FactDatabase] = mutable.Map[Int, FactDatabase]()
  val deltaDB: Database[Int, FactDatabase] = mutable.Map[Int, FactDatabase]()

  def idb(rId: Int): IDB = idbs(rId)
  def edb(rId: Int): EDB = edbs(rId)
  val printer: Printer = Printer(this, ns)

  val relOps: RelationalOperators[SimpleStorageManager] = RelationalOperators(this)

  // store all relations
  def initRelation(rId: Int, name: String): Unit = {
    edbs.addOne(rId, EDB())
    idbs.addOne(rId, IDB())
    ns(rId) = name
  }
  // TODO: For now store IDB and EDB separately
  def insertEDB(rule: StorageAtom): Unit = {
    edbs(rule.rId).addOne(rule.terms)
  }
  def bulkInsertEDB(rId: Int, rules: Relation[StorageTerm]): Unit = {
    edbs(rId).appendAll(rules)
  }
  def bulkInsertEDB(rId: Int, rules: Relation[StorageTerm], queryId: Int): Unit = {
    incrementalDB(queryId).getOrElseUpdate(rId, EDB()).appendAll(rules)
  }
  def resetIncrEDB(rId: Int, rules: Relation[StorageTerm], queryId: Int): Unit = {
    incrementalDB(queryId)(rId) = rules
  }
  def addIncrEDB(rId: Int, rules: Relation[StorageTerm], queryId: Int): Unit = {
    incrementalDB(queryId)(rId) :+ rules
  }
  def resetDeltaEDB(rId: Int, rules: Relation[StorageTerm], queryId: Int): Unit = {
    deltaDB(queryId)(rId) = rules
  }
  def insertIDB(rId: Int, rule: Row[StorageAtom]): Unit = {
    idbs(rId).addOne(rule)
  }

  /**
   * Store results of query for later use
   *
   * @return
   */
  def initEvaluation(): Int = {
    val edbClone = FactDatabase()
    edbs.foreach((k, relation) => {
      edbClone(k) = EDB()
      relation.zipWithIndex.foreach((row, idx) =>
        edbClone(k).addOne(Row[StorageTerm]().appendedAll(row))
      )
    })
    incrementalDB.addOne(increment, edbClone) // TODO: do we need to clone everything in the edb?
    val edbClone2 = FactDatabase()
    edbs.foreach((k, relation) => {
      edbClone2(k) = EDB()
//      relation.zipWithIndex.foreach((row, idx) =>
//        edbClone2(k).addOne(IndexedSeq[StorageTerm]().appendedAll(row))
//      )
    })
    deltaDB.addOne(increment, edbClone2)
    increment += 1
    increment - 1
  }

  def getIncrementDB(rId: Int, queryId: Int): EDB = incrementalDB(queryId)(rId)
  def getDeltaDB(rId: Int, queryId: Int): EDB = deltaDB(queryId)(rId)

  def swapDeltaDBs(qId1: Int, qId2: Int): Unit = {
    val t1 = deltaDB(qId1)
    deltaDB(qId1) = deltaDB(qId2)
    deltaDB(qId2) = t1
  }
  def swapIncrDBs(qId1: Int, qId2: Int): Unit = {
    val t1 = incrementalDB(qId1)
    incrementalDB(qId1) = incrementalDB(qId2)
    incrementalDB(qId2) = t1
  }

  def compareDeltaDBs(qId1: Int, qId2: Int): Boolean = { // TODO: best way to compare nested iterables?
    val db1 = deltaDB(qId1)
    val db2 = deltaDB(qId2)
    db1 == db2
  }

  def compareIncrDBs(qId1: Int, qId2: Int): Boolean = { // TODO: best way to compare nested iterables?
    val db1 = incrementalDB(qId1)
    val db2 = incrementalDB(qId2)
    db1 == db2
  }

  /**
   * Use relational operators to evaluate an IDB rule using Naive algo
   *
   * @param rIds - The ids of the relations
   * @param keys - a JoinIndexes object to join on
   * @return
   */
  private def relationalSPJU(rId: Int, keys: Seq[JoinIndexes], sourceQueryId: Int): EDB = {
    import relOps.*

    val plan = Union(
        keys.map(k =>
          Project(
            Join(
                k.deps.map(r => Scan(incrementalDB(sourceQueryId)(r), r)), k.varIndexes, k.constIndexes
            ),
            k.projIndexes
          )
        )
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
  private def semiNaiveRelationalSPJU(rId: Int, keys: Seq[JoinIndexes], sourceQueryId: Int): EDB = {
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
      )
    )
    plan.toList()
  }

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
  private def semiNaiveIterativeSPJU(rId: Int, keys: Table[JoinIndexes], sourceQueryId: Int): EDB = {
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

  def spju(rId: Int, keys: Seq[JoinIndexes], sourceQueryId: Int): EDB = {
    relationalSPJU(rId, keys, sourceQueryId)
  }

  def spjuSN(rId: Int, keys: Table[JoinIndexes], sourceQueryId: Int): EDB = {
    semiNaiveIterativeSPJU(rId, keys, sourceQueryId)
//    semiNaiveRelationalSPJU(rId, keys, sourceQueryId)
  }
}