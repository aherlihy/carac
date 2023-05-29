package datalog.storage

import datalog.dsl.{Atom, Term, Variable, Constant}
import datalog.execution.{JoinIndexes, AllIndexes}

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

  /**
   * Returns all the possible facts that could appear in the EDB for a given
   * relation.
   *
   * - The resulting EDB will contain all possible facts using constants
   * from the original EDB and IDB.
   * - The arity of the resulting EDB will respected.
   *
   * @param arity the arity of the resulting EDB.
   * @return the resulting EDB.
   */
  def getAllPossibleEDBs(arity: Int): EDB

  /**
   * Returns the discovered EDBs from a previous stratum for a particular
   * relation. The returned EDBs will always be monotonically increasing for any
   * given relation.
   *
   * @param rId the relation to get the discovered EDBs for.
   * @return the resulting EDBs.
   */
  def getDiscoveredEDBs(rId: RelationId): EDB
  
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

  def swapKnowledge(): Unit
  def compareNewDeltaDBs(): Boolean
  def compareDerivedDBs(): Boolean
  def updateDiscovered(): Unit

  def verifyEDBs(idbList: mutable.Set[RelationId]): Unit

  def joinHelper(inputs: Seq[EDB], k: JoinIndexes): EDB
  def projectHelper(input: EDB, k: JoinIndexes): EDB
  def joinProjectHelper(inputs: Seq[EDB], k: JoinIndexes, sortOrder: (Int, Int, Int)): EDB
  def joinProjectHelper_withHash(inputs: Seq[EDB], rId: Int, hash: String, sortOrder: (Int, Int, Int)): EDB
  def diff(lhs: EDB, rhs: EDB): EDB
  def union(edbs: Seq[EDB]): EDB

  def SPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB
  def naiveSPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB
}
