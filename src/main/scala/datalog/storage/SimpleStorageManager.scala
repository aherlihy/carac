package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}

import scala.collection.immutable.ListMap
import scala.collection.mutable.{ArrayBuffer, Map}

class SimpleStorageManager extends StorageManager {
  type StorageVariable = Variable
  type StorageConstant = Constant
  type StorageTerm = StorageVariable | StorageConstant
  case class StorageAtom(rId: Int, terms: IndexedSeq[StorageTerm]) {
    override def toString: String = names(rId) + terms.mkString("(", ", ", ")")
  }
  type Row[+T] = IndexedSeq[T]
  type Table[T] = ArrayBuffer[T]
  type Relation[T] = Table[Row[T]]

  val edbs = Map[Int, Relation[StorageTerm]]()
  val idbs = Map[Int, Relation[StorageAtom]]()
  val names = Map[Int, String]()

  var increment = 0
  val incrementalDB = Map[Int, Map[Int, Relation[StorageTerm]]]() // TODO: map from query
  val deltaDB = Map[Int, Map[Int, Relation[StorageTerm]]]()

  def idb(rId: Int): Relation[StorageAtom] = idbs(rId)
  def edb(rId: Int): Relation[StorageTerm] = edbs(rId)

  val relOps = RelationalOperators(this)

  // store all relations
  def initRelation(rId: Int, name: String): Unit = {
    edbs.addOne(rId, ArrayBuffer[Row[StorageTerm]]())
    idbs.addOne(rId, ArrayBuffer[Row[StorageAtom]]())
    names(rId) = name
  }
  // TODO: For now store IDB and EDB separately
  def insertEDB(rule: StorageAtom): Unit = {
    edbs(rule.rId).addOne(rule.terms)
  }
  def bulkInsertEDB(rId: Int, rules: Relation[StorageTerm]): Unit = {
    edbs(rId).appendAll(rules)
  }
  def bulkInsertEDB(rId: Int, rules: Relation[StorageTerm], queryId: Int): Unit = {
    incrementalDB(queryId).getOrElseUpdate(rId, ArrayBuffer[Row[StorageTerm]]()).appendAll(rules)
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
    val edbClone = Map[Int, Relation[StorageTerm]]()
    edbs.foreach((k, relation) => {
      edbClone(k) = ArrayBuffer[Row[StorageTerm]]()
      relation.zipWithIndex.foreach((row, idx) =>
        edbClone(k).addOne(IndexedSeq[StorageTerm]().appendedAll(row))
      )
    })
    incrementalDB.addOne(increment, edbClone) // TODO: do we need to clone everything in the edb?
    val edbClone2 = Map[Int, Relation[StorageTerm]]()
    edbs.foreach((k, relation) => {
      edbClone2(k) = ArrayBuffer[Row[StorageTerm]]()
//      relation.zipWithIndex.foreach((row, idx) =>
//        edbClone2(k).addOne(IndexedSeq[StorageTerm]().appendedAll(row))
//      )
    })
    deltaDB.addOne(increment, edbClone2)
    increment += 1
    increment - 1
  }

  def getIncrementDB(rId: Int, queryId: Int): Relation[StorageTerm] = incrementalDB(queryId)(rId)
  def getDeltaDB(rId: Int, queryId: Int): Relation[StorageTerm] = deltaDB(queryId)(rId)

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
  private def relationalSPJU(rId: Int, keys: Table[JoinIndexes], sourceQueryId: Int): Relation[StorageTerm] = {
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
  private def semiNaiveRelationalSPJU(rId: Int, keys: Table[JoinIndexes], sourceQueryId: Int): Relation[StorageTerm] = {
    import relOps.*

    val plan = Union(
      keys.map(k => // for each idb rule
        Union(
          ArrayBuffer() ++ k.deps.map(d =>
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

  private def joinHelper(inputs: Seq[Relation[StorageTerm]], k: JoinIndexes): Relation[StorageTerm] = {
    val outputRelation = ArrayBuffer[Row[StorageTerm]]()

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
  private def semiNaiveIterativeSPJU(rId: Int, keys: Table[JoinIndexes], sourceQueryId: Int): Relation[StorageTerm] = {
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
    ArrayBuffer.from(plan)
  }

  def spju(rId: Int, keys: Table[JoinIndexes], sourceQueryId: Int): Relation[StorageTerm] = {
    relationalSPJU(rId, keys, sourceQueryId)
  }

  def spjuSN(rId: Int, keys: Table[JoinIndexes], sourceQueryId: Int): Relation[StorageTerm] = {
    semiNaiveIterativeSPJU(rId, keys, sourceQueryId)
//    semiNaiveRelationalSPJU(rId, keys, sourceQueryId)
  }

  def printIncrementDB(i: Int) = {
    println("INCREMENT:" + dbToString(incrementalDB(i)))
  }
  def printIncrementDB() = {
    println("INCREMENT:" +
      incrementalDB.map((i, db) => ("queryId: " + i, dbToString(db))).mkString("[\n", ",\n", "]"))
  }
  def printDeltaDB(i: Int) = {
    println("DELTA:" + dbToString(deltaDB(i)))
  }
  def printDeltaDB() = {
    println("DELTA:" +
      deltaDB.map((i, db) => ("queryId: " + i, dbToString(db))).mkString("[\n", ",\n", "]"))
  }
  def tableToString[T](r: Relation[T]): String = {
    r.map(s => s.mkString("Rule{", ", ", "}")).mkString("[", ", ", "]")
  }
  def dbToString[T](db: Map[Int, Relation[T]]): String = {
    ListMap(db.toSeq.sortBy(_._1):_*)
      .map((k, v) => (names(k), tableToString(v)))
      .mkString("[\n  ", ",\n  ", "]")
  }
  def planToString(keys: Table[JoinIndexes]): String = {
    "Union( " +
      keys.map(k =>
        "Project" + k.projIndexes.mkString("[", " ", "]") + "( " +
          "JOIN" +
          k.varIndexes.map(v => v.mkString("$", "==$", "")).mkString("[", ",", "]") +
          k.constIndexes.map((k, v) => k + "==" + v).mkString("{", "&&", "}") +
          k.deps.map(names).mkString("(", "*", ")") +
          " )"
      ).mkString("[ ", ", ", " ]") +
      " )"
  }

  def snPlanToString(keys: Table[JoinIndexes]): String = {
    "UNION( " +
      keys.map(k =>
        "UNION(" +
          k.deps.map(d =>
            "PROJECT" + k.projIndexes.mkString("[", " ", "]") + "( " +
              "JOIN" +
              k.varIndexes.map(v => v.mkString("$", "==$", "")).mkString("[", ",", "]") +
              k.constIndexes.map((k, v) => k + "==" + v).mkString("{", "&&", "}") +
              k.deps.map(n =>
                if (n == d)
                  "delta-" + names(n)
                else
                  names(n)
              ).mkString("(", "*", ")") +
              " )"
          ).mkString("[ ", ", ", " ]") + " )"
      ).mkString("[ ", ", ", " ]") +
      " )"
  }

  override def toString = {
    "EDB:" + dbToString(edbs) +
    "\nIDB:" + dbToString(idbs) +
    "\nINCREMENT:" + incrementalDB.map((i, db) => (i, dbToString(db))).mkString("[", ", ", "]") +
    "\nDELTA:" + deltaDB.map((i, db) => (i, dbToString(db))).mkString("[", ", ", "]")
  }
}