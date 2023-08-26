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
  case Quotes, Bytecode, Lambda
enum Granularity(val flag: OpCode):
  case ALL extends Granularity(OpCode.EVAL_RULE_SN)
  case RULE extends Granularity(OpCode.EVAL_RULE_BODY)
  case DELTA extends Granularity(OpCode.SPJ)
  case NEVER extends Granularity(OpCode.OTHER)

enum Mode:
  case Interpreted
  case Compiled
  case JIT

val DEFAULT_FUZZY = 4

// TODO: make JITOptions into an enum itself
case class JITOptions(
                       mode: Mode = Mode.Interpreted,
                       granularity: Granularity = Granularity.NEVER,
                       compileSync: CompileSync = CompileSync.Blocking,
                       sortOrder: SortOrder = SortOrder.Unordered,
                       onlineSort: Boolean = false,
                       backend: Backend = Backend.Quotes,
                       fuzzy: Int = DEFAULT_FUZZY,
                       dotty: staging.Compiler = staging.Compiler.make(getClass.getClassLoader),
                       useGlobalContext: Boolean = true
                     ) {
  if ((mode == Mode.Compiled || mode == Mode.Interpreted) &&
    (compileSync != CompileSync.Blocking || granularity != Granularity.NEVER || fuzzy != DEFAULT_FUZZY))
    throw new Exception(s"Do you really want to set JIT options with $mode?")
  if (
    (mode == Mode.Interpreted && backend != Backend.Quotes) ||
      (mode == Mode.Compiled && sortOrder != SortOrder.Unordered) ||
//      (fuzzy != DEFAULT_FUZZY && compileSync == CompileSync.Blocking) ||
      (compileSync != CompileSync.Async && !useGlobalContext))
    throw new Exception(s"Weird options for mode $mode ($backend, $sortOrder, or $compileSync), are you sure?")

  override def toString: String = s"{ Mode $mode Gran: $granularity, blocking: $compileSync, sortOrder: $sortOrder, onlineSort: $onlineSort, backend: $backend }"
  def toBenchmark: String =
    val granStr = if (granularity == Granularity.NEVER) "" else granularity.toString
    val onlineSortStr = if (onlineSort) "Online" else ""
    val blockingStr = if (mode == Mode.JIT) compileSync else ""
    val programStr = s"${mode}_default_${sortOrder}_${onlineSortStr}_${fuzzy}_${blockingStr}".toLowerCase()
    val backendStr = if (mode == Mode.Interpreted) "" else backend.toString.toLowerCase()
    s"${programStr}_${granStr}_$backendStr"

  def getSortFn(storageManager: StorageManager): (Atom, Boolean) => (Boolean, Int) =
      sortOrder match
        case SortOrder.IntMax =>
          (a: Atom, isDelta: Boolean) => if (storageManager.edbContains(a.rId))
            (true, storageManager.getEDBResult(a.rId).size)
          else
            (true, Int.MaxValue)
        case SortOrder.Sel =>
          (a: Atom, isDelta: Boolean) =>
            if (isDelta)
              (true, storageManager.getKnownDeltaDB(a.rId).length)
            else
              (true, storageManager.getKnownDerivedDB(a.rId).length)
        case SortOrder.Mixed =>
          (a: Atom, isDelta: Boolean) =>
            if (isDelta)
              (storageManager.allRulesAllIndexes.contains(a.rId), storageManager.getKnownDeltaDB(a.rId).length)
            else
              (storageManager.allRulesAllIndexes.contains(a.rId), storageManager.getKnownDerivedDB(a.rId).length)
        case _ => throw new Exception(s"Unknown sort order ${sortOrder}")
}
