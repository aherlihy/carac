package test.examples.prime

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths
class prime_test_worst extends ExampleTestGenerator("prime") with prime_worst
trait prime_worst {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"

  val toSolve: String = "count_all"
  def pretest(program: Program): Unit = {
    val count_all = program.relation[Constant]("count_all")

    val count_second = program.relation[Constant]("count_second")

    val count_third = program.relation[Constant]("count_third")

    val succ = program.namedRelation("succ")

    val n, ppn, pn, pppn, x = program.variable()

    count_second("3") :- ()
    count_second(n) :- ( succ(ppn, pn), succ(pn, n), count_second(ppn) )

    count_third("3") :- ()
    count_third(n) :- ( succ(pppn, ppn), succ(pn, n), count_third(pppn), succ(ppn, pn))

    count_all(x) :- ( count_second(x), count_third(x) )
  }
}
