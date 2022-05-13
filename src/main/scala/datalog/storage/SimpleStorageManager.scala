package datalog.storage

import datalog.dsl.{Atom, Constant, Variable, Term}

import scala.collection.{immutable, mutable}

abstract class SimpleStorageManager(ns: mutable.Map[Int, String]) extends StorageManager(ns) {
  type StorageTerm = Term
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
  val printer: Printer = Printer(this)

  val relOps: RelationalOperators[SimpleStorageManager] = RelationalOperators(this)

  def getDiff(lhs: EDB, rhs: EDB): EDB =
    lhs diff rhs
  // store all relations
  def initRelation(rId: Int, name: String): Unit = {
    edbs.addOne(rId, EDB())
    idbs.addOne(rId, IDB())
    ns(rId) = name
  }
  // TODO: For now store IDB and EDB separately
  def insertIDB(rId: Int, rule: Seq[Atom]): Unit = {
    idbs(rId).addOne(Row(rule.map(a => StorageAtom(a.rId, a.terms))*))
  }
  def insertEDB(rule: Atom): Unit = {
    edbs(rule.rId).addOne(rule.terms)
  }
  def bulkInsertEDB(rId: Int, rules: Relation[StorageTerm]): Unit = {
    edbs(rId).appendAll(rules)
  }
  def bulkInsertEDB(rId: Int, rules: Relation[StorageTerm], queryId: Int): Unit = {
    incrementalDB(queryId).getOrElseUpdate(rId, EDB()).appendAll(rules)
  }
  def resetIncrEDB(rId: Int, queryId: Int, rules: Relation[StorageTerm], prev: Relation[StorageTerm] = Relation[StorageTerm]()): Unit = {
    incrementalDB(queryId)(rId) = rules ++ prev
  }
  def addIncrEDB(rId: Int, rules: Relation[StorageTerm], queryId: Int): Unit = {
    incrementalDB(queryId)(rId) :+ rules
  }
  def resetDeltaEDB(rId: Int, rules: Relation[StorageTerm], queryId: Int): Unit = {
    deltaDB(queryId)(rId) = rules
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
  def getResult(rId: Int, queryId: Int): Set[Seq[Term]] = getIncrementDB(rId, queryId).map(s => s.toSeq).toSet
  def getEDBResult(rId: Int): Set[Seq[Term]] = edbs(rId).map(s => s.toSeq).toSet
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

  // TODO: maybe move this into exec engine
  /**
   * For a single rule, get (1) the indexes of repeated variables within the body,
   * (2) the indexes of constants, (3) the indexes of variables in the body present
   * with the head atom, (4) relations that this rule is dependent on.
   * #1, #4 goes to join, #2 goes to select (or also join depending on implementation),
   * #3 goes to project
   *
   * @param rule - Includes the head at idx 0
   */
  def getOperatorKeys(rule: Row[StorageAtom]): JoinIndexes = {
    val constants = mutable.Map[Int, StorageConstant]()
    var projects = IndexedSeq[Int]()

    // variable ids in the head atom
    val headVars = mutable.HashSet() ++ rule(0).terms.flatMap(t => t match {
      case v: Variable => Seq(v.oid)
      case _ => Seq()
    })

    val body = rule.drop(1)

    val deps = body.map(a => a.rId)

    val vars = body
      .flatMap(a => a.terms)
      .zipWithIndex
      .groupBy(z => z._1)
      .filter((term, matches) =>
        term match {
          case v: StorageVariable =>
            if (headVars.contains(v.oid)) projects = projects ++ matches.map(_._2)
            matches.length >= 2
          case c: StorageConstant =>
            matches.foreach((_, idx) => constants(idx) = c)
            false
        }
      )
      .map((term, matches) =>
        matches.map(_._2)
      )
      .toIndexedSeq
    JoinIndexes(vars, constants.toMap, projects, deps)
  }
}