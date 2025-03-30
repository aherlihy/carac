package test.examples.rqb_cspa

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}

class rqb_cspa_test extends ExampleTestGenerator("rqb_cspa") with rqb_cspa
trait rqb_cspa {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/rqb_cspa/facts"
  val toSolve = "ValueFlow"

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
