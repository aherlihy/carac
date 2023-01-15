package datalog.benchmarks

import datalog.dsl.{Constant, Program, Relation}
import datalog.execution.ExecutionEngine

abstract class DLBenchmark {
//  def pretest(program: Program): Unit
  def run(program: Program): Relation[Constant]
  val expected: Any
}
