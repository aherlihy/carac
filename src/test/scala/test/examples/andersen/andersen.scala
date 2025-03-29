package test.examples.andersen

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths

trait andersen {
 val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/andersen/facts"
 val toSolve = "pointsTo"
 def pretest(program: Program): Unit = {
  val addressOf = program.namedRelation("addressOf")
  val assign = program.namedRelation("assign")
  val load = program.namedRelation("load")
  val store = program.namedRelation("store")

  val x, y, z, w = program.variable()
  val pointsTo = program.relation[Constant]("pointsTo")

  pointsTo(y, x) :- addressOf(y, x)

  pointsTo(y, x) :- (assign(y, z), pointsTo(z, x))

  pointsTo(y, w) :- (
    load(y, x),
    pointsTo(x, z),
    pointsTo(z, w))

  pointsTo(z, w) :- (
    store(y, x),
    pointsTo(y, z),
    pointsTo(x, w))
 }
}

class andersen_test() extends ExampleTestGenerator("andersen") with andersen