package examples.ackermann

import datalog.dsl.{Constant, Program}
import tools.GraphGenerator

import java.nio.file.{Path, Paths}
import scala.util.Properties

class ackermann extends GraphGenerator(
  Paths.get("src", "test", "scala", "examples", "ackermann"), // TODO: use pwd
  Set("Naive", "Relational"), // run only SemiNaiveIdxColl
  Set("Slow", "LocalOnly")
) {
  def pretest(program: Program): Unit = {
    val succ = program.namedRelation("succ")
    val greaterThanZ = program.namedRelation("greaterThanZ")
    val ack = program.relation[Constant]("ack")
    val N, M, X, Y, Ans, Ans2 = program.variable()

    ack("0", N, Ans) :- succ(N, Ans)

    ack(M, "0", Ans) :- ( greaterThanZ(M), succ(X, M), ack(X, "1", Ans) )

    ack(M, N, Ans) :- (
      greaterThanZ(M),
      greaterThanZ(N),
      succ(X, M),
      succ(Y, N),
      ack(M, Y, Ans2),
      ack(X, Ans2, Ans))
  }
}
