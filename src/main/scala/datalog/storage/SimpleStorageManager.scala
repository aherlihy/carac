package datalog.storage

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.tools.Debug.debug

import scala.collection.{immutable, mutable}

abstract class SimpleStorageManager(ns: NS) extends StorageManager(ns) {
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

  // dbID => database, because we swap between read (known) and write (new)
  var dbId = 0
  val derivedDB: Database[Int, FactDatabase] = mutable.Map[Int, FactDatabase]()
  val deltaDB: Database[Int, FactDatabase] = mutable.Map[Int, FactDatabase]()

  def idb(rId: Int): IDB = idbs(rId)
  def edb(rId: Int): EDB = edbs(rId)
  val printer: Printer[this.type] = Printer[this.type](this)

  val relOps: RelationalOperators[this.type] = RelationalOperators(this)

  def getDiff(lhs: EDB, rhs: EDB): EDB =
    lhs diff rhs
  // store all relations
  def initRelation(rId: Int, name: String): Unit = {
//    edbs.addOne(rId, EDB())
//    idbs.addOne(rId, IDB())
    ns(rId) = name
  }
  // TODO: For now store IDB and EDB separately
  def insertIDB(rId: Int, rule: Seq[Atom]): Unit = {
    idbs.getOrElseUpdate(rId, IDB()).addOne(Row(rule.map(a => StorageAtom(a.rId, a.terms))*))
  }
  def insertEDB(rule: Atom): Unit = {
    edbs.getOrElseUpdate(rule.rId, EDB()).addOne(rule.terms)
  }
//  def bulkInsertEDB(rId: Int, rules: Relation[StorageTerm]): Unit = {
//    edbs(rId).appendAll(rules)
//  }
//  def bulkInsertEDB(rId: Int, rules: Relation[StorageTerm], dbId: Int): Unit = {
//    derivedDB(dbId).getOrElseUpdate(rId, EDB()).appendAll(rules)
//  }
  /**
   * Initialize derivedDB to clone EDBs, initialize deltaDB to empty
   *
   * @return
   */
  def initEvaluation(): Int = {
    derivedDB.addOne(dbId, FactDatabase())
    deltaDB.addOne(dbId, FactDatabase())

    idbs.foreach((k, relation) => {
      derivedDB(dbId)(k) = EDB()
      deltaDB(dbId)(k) = EDB()
    })
    edbs.foreach((k, relation) => {
      deltaDB(dbId)(k) = EDB()
    }) // Delta-EDB is just empty sets
    dbId += 1
    dbId - 1
  }

  def clearDB(derived: Boolean, dbId: Int): Unit =
    if (derived)
      derivedDB(dbId).foreach((i, e) => e.clear())
    else
      deltaDB(dbId).foreach((i, e) => e.clear())

//  def cloneDerivedDB(from: Int, to: Int): Unit = {
//    derivedDB(to).foreach()
//  }

  def getIDBResult(rId: Int, dbId: Int): Set[Seq[Term]] = getDerivedDB(rId, dbId).map(s => s.toSeq).toSet
  def getEDBResult(rId: Int): Set[Seq[Term]] = edbs(rId).map(s => s.toSeq).toSet

  def getDerivedDB(rId: Int, dbId: Int): EDB = derivedDB(dbId)(rId)
  def getDeltaDB(rId: Int, dbId: Int): EDB = deltaDB(dbId)(rId)

  def resetDerived(rId: Int, dbId: Int, rules: Relation[StorageTerm], prev: Relation[StorageTerm] = Relation[StorageTerm]()): Unit =
    derivedDB(dbId)(rId) = rules ++ prev
  def resetDelta(rId: Int, dbId: Int, rules: Relation[StorageTerm]): Unit =
    deltaDB(dbId)(rId) = rules

  def swapDeltaDBs(dbId1: Int, dbId2: Int): Unit = {
    val t1 = deltaDB(dbId1)
    deltaDB(dbId1) = deltaDB(dbId2)
    deltaDB(dbId2) = t1
  }
  def swapDerivedDBs(dbId1: Int, dbId2: Int): Unit = {
    val t1 = derivedDB(dbId1)
    derivedDB(dbId1) = derivedDB(dbId2)
    derivedDB(dbId2) = t1
  }

  def compareDeltaDBs(dbId1: Int, dbId2: Int): Boolean =
    deltaDB(dbId1) == deltaDB(dbId2)
  def compareDerivedDBs(dbId1: Int, dbId2: Int): Boolean =
    derivedDB(dbId1) == derivedDB(dbId2)

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
  inline def getOperatorKey(rule: Row[StorageAtom]): JoinIndexes = {
    val constants = mutable.Map[Int, StorageConstant]() // position => constant
    val variables = mutable.Map[StorageVariable, Int]() // v.oid => position

    val body = rule.drop(1)

    val deps = body.map(a => a.rId) // TODO: should this be a set?

    val bodyVars = body
      .flatMap(a => a.terms)
      .zipWithIndex // terms, position
      .groupBy(z => z._1)
      .filter((term, matches) => // matches = Seq[(var, pos1), (var, pos2), ...]
        term match {
          case v: StorageVariable =>
            variables(v) = matches.head._2 // first idx for a variable
            !v.anon && matches.length >= 2
          case c: StorageConstant =>
            matches.foreach((_, idx) => constants(idx) = c)
            false
        }
      )
      .map((term, matches) => // get rid of groupBy elem in result tuple
        matches.map(_._2)
      )
      .toIndexedSeq

    // variable ids in the head atom
    val projects = rule(0).terms.map {
      case v: Variable =>
        if (!variables.contains(v)) throw new Exception(f"Free variable in rule head with varId $v.oid")
        if (v.anon) throw new Exception("Anonymous variable ('__') not allowed in head of rule")
        ("v", variables(v))
      case c: Constant => ("c", c)
    }
    JoinIndexes(bodyVars, constants.toMap, projects, deps)
  }

  def getOperatorKeys(rId: Int): Table[JoinIndexes] = {
    if (!idbs.contains(rId)) throw new Exception("EDB '" + ns(rId) + "' has no facts")
    val res = idbs(rId).filter(r => r.nonEmpty).map(rule => getOperatorKey(rule))
    if (edbs.contains(rId)) { // need to add EDBs to final result
      res.addOne(JoinIndexes(IndexedSeq(), Map(), IndexedSeq(), Seq(rId), true))
    }
    res
  }
}