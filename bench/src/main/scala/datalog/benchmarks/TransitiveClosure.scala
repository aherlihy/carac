package datalog.benchmarks

import datalog.dsl.{Relation, Constant, Program}
import datalog.execution.ExecutionEngine

import scala.util.Random

class TransitiveClosure extends DLBenchmark {
  val expected = Vector(1,2)
  def run(engine: ExecutionEngine): Relation[Constant] = {
    val program = Program(engine)
    val e = program.relation[Constant]("e")
    val p = program.relation[Constant]("p")
    val path2a = program.relation[Constant]("path2a")
    val path2a1 = program.relation[Constant]("path2a1")
    val edge2a = program.relation[Constant]("edge2a")
    val x, y, z = program.variable()
    for i <- 0 until 50 do
      e(
        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString,
        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString
      ) :- ()
    p(x, y) :- e(x, y)
    p(x, z) :- (e(x, y), p(y, z))
    p
  }
}
