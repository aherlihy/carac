package datalog.storage

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.{AllIndexes, JoinIndexes, PredicateType, GroupingJoinIndexes}

import scala.collection.mutable
import scala.collection.immutable
trait StorageManager(val ns: NS) {
  var iteration = 0
  var knownDbId: KnowledgeId
  var newDbId: KnowledgeId

  val allRulesAllIndexes: mutable.Map[RelationId, AllIndexes]

  val printer: Printer[this.type]

  def initRelation(rId: RelationId, name: String): Unit
  def initEvaluation(): Unit

  def insertEDB(rule: Atom): Unit
  def getEmptyEDB(): EDB
  def edbContains(rId: RelationId): Boolean
  def getEDB(rId: RelationId): EDB
  def getAllEDBS(): mutable.Map[RelationId, Any] // if you ever just want to read the EDBs as a map, used for testing

  // Helpers for negation
  def addConstantsToDomain(constants: Seq[StorageTerm]): Unit
  def getComplement(arity: Int): CollectionsEDB

  def getKnownDerivedDB(rId: RelationId): EDB
  def getNewDerivedDB(rId: RelationId): EDB
  def getKnownDeltaDB(rId: RelationId): EDB
  def getNewDeltaDB(rId: RelationId): EDB
  def getKnownIDBResult(rId: RelationId): Set[Seq[Term]]
  def getNewIDBResult(rId: RelationId): Set[Seq[Term]]
  def getEDBResult(rId: RelationId): Set[Seq[Term]]

  def resetKnownDerived(rId: RelationId, rules: EDB, prev: EDB): Unit
  def resetNewDerived(rId: RelationId, rules: EDB, prev: EDB): Unit
  def resetNewDelta(rId: RelationId, rules: EDB): Unit
  def resetKnownDelta(rId: RelationId, rules: EDB): Unit
  def clearNewDerived(): Unit
  def clearKnownDelta(): Unit

  def swapKnowledge(): Unit
  def compareNewDeltaDBs(): Boolean
  def compareDerivedDBs(): Boolean
  def updateDiscovered(): Unit

  def verifyEDBs(idbList: mutable.Set[RelationId]): Unit

  def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB
  def projectHelper(input: EDB, k: JoinIndexes): EDB
  def joinProjectHelper(inputs: Seq[EDB], k: JoinIndexes, onlineSort: Boolean): EDB
  def joinProjectHelper_withHash(inputs: Seq[EDB], rId: Int, hash: String, onlineSort: Boolean): EDB
  def diff(lhs: EDB, rhs: EDB): EDB
  def union(edbs: Seq[EDB]): EDB

  // Helpers for grouping
  def groupingHelper(base: EDB, gji: GroupingJoinIndexes): EDB

  def SPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB
  def naiveSPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB
}
