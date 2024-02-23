package datalog.storage

import datalog.dsl.{StorageAtom, Constant, Term, Variable}
import datalog.execution.{AllIndexes, JoinIndexes, PredicateType}

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

  def updateAliases(aliases: mutable.Map[RelationId, RelationId]): Unit

  def insertEDB(rule: StorageAtom): Unit
  def getEmptyEDB(rId: RelationId): EDB
  def edbContains(rId: RelationId): Boolean
  def getEDB(rId: RelationId): EDB
  def getAllEDBS(): collection.Map[RelationId, Any] // if you ever just want to read the EDBs as a map, used for testing
  def registerIndexCandidates(cands: mutable.Map[RelationId, mutable.BitSet]): Unit
  def registerRelationArity(rId: RelationId, arity: Int): Unit

  // Helpers for negation
  def addConstantsToDomain(constants: Seq[StorageTerm]): Unit
  def getComplement(rId: RelationId, arity: Int): EDB

  def getKnownDerivedDB(rId: RelationId): EDB
  def getNewDerivedDB(rId: RelationId): EDB
  def getKnownDeltaDB(rId: RelationId): EDB
  def getNewDeltaDB(rId: RelationId): EDB
  def getKnownIDBResult(rId: RelationId): Set[Seq[StorageTerm]]
  def getNewIDBResult(rId: RelationId): Set[Seq[StorageTerm]]
  def getEDBResult(rId: RelationId): Set[Seq[StorageTerm]]

  def resetKnownDerived(rId: RelationId, rules: EDB, prev: EDB): Unit
  def resetNewDerived(rId: RelationId, rules: EDB, prev: EDB): Unit

  def setKnownDerived(rId: RelationId, rules: EDB): Unit
  def setNewDerived(rId: RelationId, rules: EDB): Unit

  def setKnownDelta(rId: RelationId, rules: EDB): Unit
  def clearNewDerived(): Unit

  def insertDeltaIntoDerived(): Unit
  def setNewDelta(rId: RelationId, rules: EDB): Unit
  def clearNewDeltas(): Unit

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

  def SPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB
  def naiveSPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB
}
