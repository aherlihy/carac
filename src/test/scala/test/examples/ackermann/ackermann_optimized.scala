package test.examples.ackermann

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}

trait ackermann_optimized {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"
  val toSolve = "ack"
  def pretest(program: Program): Unit = {
    val succ = program.namedRelation("succ")
    val greaterThanZ = program.namedRelation("greaterThanZ")
    val ack = program.relation[Constant]("ack")
    val N, M, X, Y, Ans, Ans2 = program.variable()

    ack("0", N, Ans) :- succ(N, Ans)

    ack(M, "0", Ans) :- ( greaterThanZ(M), succ(X, M), ack(X, "1", Ans) )

    ack(M, N, Ans) :- (
      greaterThanZ(M),
      succ(X, M),
      ack(M, Y, Ans2),
      succ(Y, N),
      greaterThanZ(N),
      ack(X, Ans2, Ans))
  }
}

class ackermann_optimized_test extends ExampleTestGenerator("ackermann"
//  Set(Tags.Naive, Tags.Volcano), // run only SemiNaiveIdxColl
//  Set(Tags.Slow, Tags.CI)
) with ackermann_optimized