package datalog.execution

import datalog.dsl.Atom
import datalog.execution.ir.OpCode
import datalog.storage.StorageManager

import scala.quoted.staging

enum CompileSync:
  case Async, Blocking
enum SortOrder:
  case Sel, IntMax, Mixed, Badluck, Unordered, Worst
enum Backend:
  case MacroQuotes, Quotes, Bytecode, Lambda
enum Granularity(val flag: OpCode):
  case ALL extends Granularity(OpCode.EVAL_RULE_SN)
  case RULE extends Granularity(OpCode.EVAL_RULE_BODY)
  case DELTA extends Granularity(OpCode.SPJ)
  case NEVER extends Granularity(OpCode.OTHER)

enum Mode:
  case Interpreted
  case Compiled
  case JIT

val DEFAULT_FUZZY = 0

// TODO: make JITOptions into an enum itself
case class JITOptions(
                       mode: Mode = Mode.Interpreted,
                       granularity: Granularity = Granularity.NEVER,
                       compileSync: CompileSync = CompileSync.Blocking,
                       sortOrder: SortOrder = SortOrder.Unordered,
//                       onlineSort: Boolean = false, TODO: remove, not really used
                       backend: Backend = Backend.Quotes,
                       fuzzy: Int = DEFAULT_FUZZY,
                       dotty: staging.Compiler = staging.Compiler.make(getClass.getClassLoader),
                       useGlobalContext: Boolean = true,
                       runtimeSort: SortOrder = SortOrder.Unordered, // only used with macros to separate compile-time sort (usual sortOrder), and then an additional online sort
                       debug: Boolean = false
                     ) {
  if ((mode == Mode.Compiled || mode == Mode.Interpreted) &&
    (compileSync != CompileSync.Blocking || granularity != Granularity.NEVER || fuzzy != 0))
    throw new Exception(s"Do you really want to set JIT options with $mode?")
  if (
    (mode == Mode.Interpreted && backend != Backend.Quotes &&  backend != Backend.MacroQuotes) ||
      // (mode == Mode.Compiled && sortOrder != SortOrder.Unordered) ||
      (fuzzy != DEFAULT_FUZZY && compileSync == CompileSync.Blocking) ||
      (compileSync != CompileSync.Async && !useGlobalContext))
    throw new Exception(s"Weird options for mode $mode ($backend, $sortOrder, or $compileSync), are you sure?")

  override def toString: String = s"{ Mode $mode Gran: $granularity, blocking: $compileSync, sortOrder: $sortOrder, runtimeSort: $runtimeSort, backend: $backend }"
  def toBenchmark: String =
    val granStr = if (granularity == Granularity.NEVER) "" else granularity.toString
    val blockingStr = if (mode == Mode.JIT) compileSync else ""
    val programStr = s"${mode}_default_${sortOrder}_${runtimeSort}_${fuzzy}_${blockingStr}".toLowerCase()
    val backendStr = if (mode == Mode.Interpreted) "" else backend.toString.toLowerCase()
    s"${programStr}_${granStr}_$backendStr"

  def getSortFn(storageManager: StorageManager): (Atom, Boolean) => (Boolean, Int) =
    JITOptions.getSortFn(sortOrder, storageManager)
  def getRuntimeSortFn(storageManager: StorageManager): (Atom, Boolean) => (Boolean, Int) =
    JITOptions.getSortFn(runtimeSort, storageManager)
}

object JITOptions {
  /**
   * Determine the sort order of facts + rules. Return (IDB?, weight).
   * @param sortOrder
   * @param storageManager
   * @return a sorting function to pass to collection.sortBy. The sortFn eturns (tiebreaker, weight) where tiebreaker
   *         is if it's an IDB relation, then cardinality, so that IDBs can be sorted separately from EDBs
   */
  def getSortFn(sortOrder: SortOrder, storageManager: StorageManager): (Atom, Boolean) => (Boolean, Int) =
      sortOrder match
        case SortOrder.IntMax => // return EDB cardinalities and treat IDBs as max weight, so go first. Useful for sorting before start of execution.
          (a: Atom, isDelta: Boolean) =>
            if (storageManager.edbContains(a.rId))
              (true, storageManager.getEDBResult(a.rId).size)
            else
              (true, Int.MaxValue)
        case SortOrder.Sel => // return cardinalities of both EDB and IDB since computation will have started, return exact delta cardinalities.
          (a: Atom, isDelta: Boolean) =>
            if (isDelta)
              (true, storageManager.getKnownDeltaDB(a.rId).length)
            else
              (true, storageManager.getKnownDerivedDB(a.rId).length)
        case SortOrder.Mixed => //
          (a: Atom, isDelta: Boolean) =>
            if (isDelta)
              (storageManager.allRulesAllIndexes.contains(a.rId), storageManager.getKnownDeltaDB(a.rId).length)
            else
              (storageManager.allRulesAllIndexes.contains(a.rId), storageManager.getKnownDerivedDB(a.rId).length)
        case _ => throw new Exception(s"Unknown sort order ${sortOrder}")
}
