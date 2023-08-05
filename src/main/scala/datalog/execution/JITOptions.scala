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

// TODO: make JITOptions into an enum itself
case class JITOptions(
                       granularity: OpCode = OpCode.OTHER, // default is unoptimized interpret
                       compileSync: CompileSync = CompileSync.Blocking,
                       sortOrder: SortOrder = SortOrder.Unordered,
                       onlineSort: Boolean = false,
                       backend: Backend = Backend.Quotes,
                       fuzzy: Int = 0,
                       dotty: staging.Compiler = staging.Compiler.make(getClass.getClassLoader),
                     ) {
  private val unique = Seq(OpCode.DOWHILE, OpCode.EVAL_NAIVE, OpCode.LOOP_BODY)
  if (unique.contains(granularity))
    throw new Exception(s"Cannot compile singleton IR nodes: $granularity (theres no point)")

  override def toString: String = s"{ Gran: $granularity, blocking: $compileSync, sortOrder: $sortOrder, onlineSort: $onlineSort, backend: $backend }"
  def toBenchmark: String =
    val modeStr = if (granularity == OpCode.OTHER) "interpreted" else if (granularity == OpCode.PROGRAM) "compiled" else "jit"
    val granStr = granularity match
      case OpCode.PROGRAM | OpCode.OTHER => ""
      case OpCode.EVAL_RULE_SN => "ALL"
      case OpCode.EVAL_RULE_BODY => "1RULE"
      case _ => ???
    val onlineSortStr = if (onlineSort) "Online" else ""
    val blockingStr = if (granularity == OpCode.OTHER || granularity == OpCode.PROGRAM) "" else compileSync
    val programStr = s"${modeStr}_default_${sortOrder}_${onlineSortStr}_${fuzzy}_${blockingStr}".toLowerCase()
    val backendStr = if (granularity == OpCode.OTHER) "" else backend.toString.toLowerCase()
    s"${programStr}_${granStr}_$backendStr"

  def getSortFn(storageManager: StorageManager): Atom => (Boolean, Int) =
    (a: Atom) =>
      sortOrder match
        case SortOrder.IntMax =>
          if (storageManager.edbContains(a.rId))
            (true, storageManager.getEDBResult(a.rId).size)
          else
            (true, Int.MaxValue)
        case SortOrder.Sel =>
          (true, storageManager.getKnownDerivedDB(a.rId).length)
        case SortOrder.Mixed =>
          (storageManager.allRulesAllIndexes.contains(a.rId), storageManager.getKnownDerivedDB(a.rId).length)
        case _ => throw new Exception(s"Unknown sort order ${sortOrder}")
}
