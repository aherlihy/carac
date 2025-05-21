package test.examples.rqb_cspa

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}
import carac.storage.{DatabaseType, StorageManager}

class rqb_cspa_test extends ExampleTestGenerator("rqb_cspa") with rqb_cspa
trait rqb_cspa {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/rqb_cspa/facts"
  val toSolve = "ValueFlow"

  def pretest(program: Program): Unit = {
    val assign = program.namedRelation[Int]("assign")
    val dereference = program.namedRelation[Int]("dereference")

    val ValueFlow = program.relation[Int]("ValueFlow")
    val ValueAlias = program.relation[Int]("ValueAlias")
    val MemoryAlias = program.relation[Int]("MemoryAlias")

    val w, x, y, z = program.variable()

    ValueFlow(y, x) :- assign(y, x)
    ValueFlow(x, y) :- (assign(x, z), MemoryAlias(z, y))
    ValueFlow(x, y) :- (ValueFlow(x, z), ValueFlow(z, y))

    MemoryAlias(x, w) :- (dereference(y, x), ValueAlias(y, z), dereference(z, w))

    ValueAlias(x, y) :- (ValueFlow(z, x), ValueFlow(z, y))
    ValueAlias(x, y) :- (ValueFlow(z, x), MemoryAlias(z, w), ValueFlow(w, y))

    ValueFlow(x, x) :- assign(x, y)
    ValueFlow(x, x) :- assign(y, x)

    MemoryAlias(x, x) :- assign(y, x)
    MemoryAlias(x, x) :- assign(x, y)
  }

  def loadSchema(program: Program, storageManager: StorageManager): Unit =
    val assign = program.relation("assign")
    val assignS = Seq(("c0", DatabaseType.INTEGER), ("c1", DatabaseType.INTEGER))
    storageManager.registerRelationSchema(assign.id, assignS)
    val dereference = program.relation("dereference")
    val dereferenceS = Seq(("c0", DatabaseType.INTEGER), ("c1", DatabaseType.INTEGER))
    storageManager.registerRelationSchema(dereference.id, dereferenceS)
}
