package datalog.benchmarks

import datalog.dsl.{Relation, Constant, Program}
import datalog.execution.ExecutionEngine

import scala.util.Random

class MultiJoin extends DLBenchmark {
  override val expected: Any = Vector(1,2)
  def run(engine: ExecutionEngine): Relation[Constant] = {
    val program = Program(engine)
    val edge = program.relation[Constant]("edge")

    val oneHop = program.relation[Constant]("oh")
    val twoHops = program.relation[Constant]("th")
    val threeHops = program.relation[Constant]("threeH")
    val fourHops = program.relation[Constant]("fourH")

    val x, y, z, w, q = program.variable()

    oneHop(x, y) :- edge(x, y)
    twoHops(x, z) :- (edge(x, y), oneHop(y, z))
    threeHops(x, w) :- (edge(x, y), oneHop(y, z), oneHop(z, w))
    fourHops(x, q) :- (edge(x, y), oneHop(y, z), oneHop(z, w), oneHop(w, q))

    for i <- 0 until 50 do
      edge(
        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString,
        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString
      ) :- ()
    fourHops
  }
}
