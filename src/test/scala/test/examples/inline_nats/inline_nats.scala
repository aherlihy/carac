package test.examples.inline_nats

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program, __, *}
import test.{ExampleTestGenerator, Tags}
class inline_nats_test extends ExampleTestGenerator("inline_nats") with inline_nats
trait inline_nats {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/inline_nats/facts"
  val toSolve: String = "query"
  def pretest(program: Program): Unit = {
    val nat = program.relation[Constant]("nat")
    val query = program.relation[Constant]("query")

    val x, y = program.variable()

    (0 until 10000).foreach(x =>
      nat(x) :- ()
    )

    query(x) :- (nat(x), x |<| 2)

    println({query.solve(); ()})
  }
}
