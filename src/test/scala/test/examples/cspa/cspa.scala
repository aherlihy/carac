package test.examples.cspa

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

class cspa_test extends ExampleTestGenerator("cspa") with cspa

trait cspa {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cspa/facts"
  val toSolve = "ValueFlow"

  // Adapted from https://github.com/Hacker0912/RecStep/blob/e4107c814c7b24ae787dddb4af7e3238303f13ab/benchmark_datalog_programs/cspa.datalog
  def pretest(program: Program): Unit = {
    val Assign = program.namedRelation[String]("Assign")
    val Dereference = program.namedRelation[String]("Dereference")

    val ValueFlow = program.relation[String]("ValueFlow")
    val ValueAlias = program.relation[String]("ValueAlias")
    val MemoryAlias = program.relation[String]("MemoryAlias")

    val w, x, y, z = program.variable()

    ValueFlow(y, x) :- Assign(y, x)
    ValueFlow(x, y) :- (Assign(x, z), MemoryAlias(z, y))
  }
}
