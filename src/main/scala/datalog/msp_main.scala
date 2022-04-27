package datalog

import scala.quoted.*
given staging.Compiler = staging.Compiler.make(getClass.getClassLoader)


@main def msp_main = {
  val base = "2".toDouble
  val exp = "3".toInt
  // 1) Staged, runtime generation
  val power3runtime: Double => Double = // (x: Int) => x * x * x * 1
    powerMSP(exp) // Staged, runtime generation
  val stagedResult = power3runtime(base)
  println("stagedR=" + stagedResult)

  // 2) macro, compile-time generation
//  def power3macro(x: Double) = // x * x * x * 1
//    powerMacro(x, 3)
//  val macroResult = power3macro(base)
//  println("macroResult=" + macroResult)
}
