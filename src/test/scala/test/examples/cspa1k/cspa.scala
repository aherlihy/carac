package test.examples.cspa1k

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}

class cspa1k_test extends ExampleTestGenerator(
  "cspa1k",
  Set(Tags.Naive, Tags.Volcano),
  Set(Tags.Slow, Tags.CI)
) with cspa1k

trait cspa1k {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cspa1k/facts"
  val toSolve = "ValueFlow"

  // Adapted from https://github.com/Hacker0912/RecStep/blob/e4107c814c7b24ae787dddb4af7e3238303f13ab/benchmark_carac_programs/cspa.carac
  def pretest(program: Program): Unit = {
    val Assign = program.namedRelation[Int]("Assign")
    val Dereference = program.namedRelation[Int]("Dereference")

    val ValueFlow = program.relation[Int]("ValueFlow")
    val ValueAlias = program.relation[Int]("ValueAlias")
    val MemoryAlias = program.relation[Int]("MemoryAlias")

    val w, x, y, z = program.variable()

    ValueFlow(y, x) :- Assign(y, x)
    ValueFlow(x, y) :- (Assign(x, z), MemoryAlias(z, y))
    ValueFlow(x, y) :- (ValueFlow(x, z), ValueFlow(z, y))

    MemoryAlias(x, w) :- (Dereference(y, x), ValueAlias(y, z), Dereference(z, w))

    ValueAlias(x, y) :- (ValueFlow(z, x), ValueFlow(z, y))
    ValueAlias(x, y) :- (ValueFlow(z, x), MemoryAlias(z, w), ValueFlow(w, y))

    ValueFlow(x, x) :- Assign(x, y)
    ValueFlow(x, x) :- Assign(y, x)

    MemoryAlias(x, x) :- Assign(y, x)
    MemoryAlias(x, x) :- Assign(x, y)
  }
}
