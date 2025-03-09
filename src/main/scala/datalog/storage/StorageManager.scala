package datalog.storage

import datalog.dsl.{StorageAtom, Constant, Term, Variable}
import datalog.execution.{AllIndexes, JoinIndexes, PredicateType}

import scala.collection.mutable
import scala.collection.immutable
trait StorageManager(val ns: NS) {
  var iteration = 0
  var initialized: Boolean

  val allRulesAllIndexes: mutable.Map[RelationId, AllIndexes]

  val printer: Printer[this.type]

  def initRelation(rId: RelationId, name: String): Unit
  def initEvaluation(): Unit

  def updateAliases(aliases: mutable.Map[RelationId, RelationId]): Unit

  def insertEDB(rule: StorageAtom): Unit
  def getEmptyEDB(rId: RelationId): EDB
  def edbContains(rId: RelationId): Boolean
  def getEDB(rId: RelationId): EDB
  def getAllEDBS(): mutable.Map[RelationId, Any] // if you ever just want to read the EDBs as a map, used for testing
  def registerIndexCandidates(cands: mutable.Map[RelationId, mutable.BitSet]): Unit
  def registerRelationSchema(rId: RelationId, terms: Seq[Term]): Unit
  // If you already know the schema and do not need to infer it
  def declareTable(rId: RelationId, s: Seq[(String, DatabaseType)]): Unit = {}

  def getKnownDerivedDB(rId: RelationId): EDB
  def getKnownDeltaDB(rId: RelationId): EDB
  def getNewDeltaDB(rId: RelationId): EDB
  def getKnownIDBResult(rId: RelationId): Set[Seq[StorageTerm]]
  def getNewIDBResult(rId: RelationId): Set[Seq[StorageTerm]]
  def getEDBResult(rId: RelationId): Set[Seq[StorageTerm]]

  def insertDeltaIntoDerived(): Unit
  def setNewDelta(rId: RelationId, rules: EDB): Unit
  def clearNewDeltas(): Unit

  def swapKnowledge(): Unit
  def compareNewDeltaDBs(): Boolean

  def verifyEDBs(idbList: mutable.Set[RelationId]): Unit

  // Helper method to merge the select-project-join operations.
  def selectProjectJoinHelper(inputs: Seq[EDB], rId: Int, hash: String, onlineSort: Boolean): EDB
  def union(edbs: Seq[EDB]): EDB

  // Helpers for negation
  def addConstantsToDomain(constants: Seq[StorageTerm]): Unit
  def getComplement(rId: RelationId, arity: Int): EDB
  def diff(lhs: EDB, rhs: EDB): EDB
}
