package datalog.execution

import datalog.execution.ir.OpCode

import scala.quoted.staging

case class JITOptions(
                       granularity: OpCode = OpCode.PROGRAM,
                       dotty: staging.Compiler = staging.Compiler.make(getClass.getClassLoader),
                       aot: Boolean = true,
                       block: Boolean = true,
                       thresholdNum: Int = 0,
                       thresholdVal: Float = 2,
                       sortOrder: (Int, Int, Int) = (0, 0, 0),
                       stratified: Boolean = false
                     ) {
  private val unique = Seq(OpCode.DOWHILE, OpCode.EVAL_NAIVE, OpCode.LOOP_BODY)
  if (!aot && !block && unique.contains(granularity))
    throw new Exception(s"Cannot online, async compile singleton IR nodes: $granularity (theres no point)")
}