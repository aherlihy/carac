package test.examples.prime

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths
class prime_test_optimized extends ExampleTestGenerator("prime") with prime_optimized
trait prime_optimized {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/prime/facts"

  val toSolve: String = "count_all"
  def pretest(program: Program): Unit = {
    val count_all = program.relation[Constant]("count_all")

    val count_second = program.relation[Constant]("count_second")

    val count_third = program.relation[Constant]("count_third")

    val succ = program.relation[Constant]("succ")

    val n, ppn, pn, pppn, x = program.variable()

    count_second("3") :- ()
    count_second(n) :- ( count_second(ppn), succ(ppn, pn), succ(pn, n) )

    count_third("3") :- ()
    count_third(n) :- ( count_third(pppn), succ(pppn, ppn), succ(ppn, pn), succ(pn, n) )

    count_all(x) :- ( count_second(x), count_third(x) )

    succ("0", "1") :- ()
    succ("1", "2") :- ()
    succ("2", "3") :- ()
    succ("3", "4") :- ()
    succ("4", "5") :- ()
    succ("5", "6") :- ()
    succ("6", "7") :- ()
    succ("7", "8") :- ()
    succ("8", "9") :- ()
    succ("9", "10") :- ()
    succ("10", "11") :- ()
    succ("11", "12") :- ()
    succ("12", "13") :- ()
    succ("13", "14") :- ()
    succ("14", "15") :- ()
    succ("15", "16") :- ()
    succ("16", "17") :- ()
    succ("17", "18") :- ()
    succ("18", "19") :- ()
    succ("19", "20") :- ()
    succ("20", "21") :- ()
  }
}
