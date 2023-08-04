package datalog.execution

import datalog.execution.ir.OpCode

import scala.quoted.staging

enum CompileSync:
  case Async, Blocking
enum SortOrder:
  case Sel, IntMax, Mixed, Badluck, Unordered, Worst
enum Backend:
  case Quotes, Bytecode

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
    val programStr = s"${modeStr}_default_${sortOrder}_${onlineSortStr}_${fuzzy}_${compileSync}".toLowerCase()
    s"${programStr}_${granStr}_${backend.toString.toLowerCase()}"

}
