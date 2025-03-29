package carac.storage

import carac.dsl.{StorageAtom, Term}
import carac.execution.AllIndexes

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
  def edbContains(rId: RelationId): Boolean
  def getEDB(rId: RelationId): EDB
  def getAllEDBS(): mutable.Map[RelationId, Any] // if you ever just want to read the EDBs as a map, used for testing
  def registerIndexCandidates(cands: mutable.Map[RelationId, mutable.BitSet]): Unit
  def registerRelationSchema(rId: RelationId, terms: Seq[Term], hash: Option[String]): Unit
  // If you already know the schema and do not need to infer it
  def declareTable(rId: RelationId, s: Seq[(String, DatabaseType)]): Unit = {}

  def getDerivedDB(rId: RelationId): EDB
  def getDeltaDB(rId: RelationId): EDB
  def getIDBResult(rId: RelationId): Set[Seq[StorageTerm]]
  def getEDBResult(rId: RelationId): Set[Seq[StorageTerm]]

  def insertDeltaIntoDerived(): Unit
  def writeNewDelta(rId: RelationId, rules: EDB): Unit
  def clearPreviousDeltas(): Unit

  def swapReadWriteDeltas(): Unit
  def deltasEmpty(): Boolean

  def verifyEDBs(idbList: mutable.Map[RelationId, mutable.ArrayBuffer[String]]): Unit

  // Helper method to merge the select-project-join operations.
  def selectProjectJoinHelper(inputs: Seq[EDB], rId: Int, hash: String, onlineSort: Boolean): EDB
  def union(edbs: Seq[EDB]): EDB

  // Helpers for negation
  def addConstantsToDomain(constants: Seq[StorageTerm]): Unit
  def getComplement(rId: RelationId, arity: Int): EDB
  def diff(lhs: EDB, rhs: EDB): EDB
}
