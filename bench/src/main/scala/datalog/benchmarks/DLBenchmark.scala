package datalog.benchmarks

import datalog.dsl.{Relation, Constant}
import datalog.execution.ExecutionEngine

abstract class DLBenchmark {
  def run(engine: ExecutionEngine): Relation[Constant]
  val expected: Any
}
