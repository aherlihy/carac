package test.examples.ackermann

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}

trait ackermann_worst {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ackermann/facts"

  val toSolve = "ack"
  def pretest(program: Program): Unit = {
    val succ = program.namedRelation("succ")
    val greaterThanZ = program.namedRelation("greaterThanZ")
    val ack = program.relation[Constant]("ack")
    val N, M, X, Y, Ans, Ans2 = program.variable()

    ack("0", N, Ans) :- succ(N, Ans)

    ack(M, "0", Ans) :- ( ack(X, "1", Ans), greaterThanZ(M), succ(X, M) )

    ack(M, N, Ans) :- (
      ack(X, Ans2, Ans),
      succ(Y, N),
      succ(X, M),
      greaterThanZ(N),
      ack(M, Y, Ans2),
      greaterThanZ(M)
    )
  }
}

class ackermann_worst_test extends ExampleTestGenerator("ackermann"
//  Set(Tags.Naive, Tags.Volcano), // run only SemiNaiveIdxColl
//  Set(Tags.Slow, Tags.CI)
) with ackermann_worst