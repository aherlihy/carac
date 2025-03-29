package test.examples.csda10k

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}

class csda10k_test extends ExampleTestGenerator(
  "csda10k",
  Set(Tags.Naive, Tags.Volcano),
  Set(Tags.Slow, Tags.CI)
) with csda10k

trait csda10k {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/csda10k/facts"
  val toSolve = "Null"

  // Adapted from https://drive.google.com/drive/mobile/folders/1M4WxwykUd-jX8jBA50pSNf2R-1IJ49PJ/1DRj-cfISV9v34vU7DH1PKEu13PaMIf71/1V1JWRdDzjjGxBPSKLg6diEAjJ-eRbQvj?usp=drive_link&pli=1&sort=13&direction=a
  def pretest(program: Program): Unit = {
    val NullEdge = program.namedRelation("NullEdge")
    val Arc = program.namedRelation("Arc")

    val Null = program.relation[Int]("Null")

    val x, y, w = program.variable()

    Null(x, y) :- NullEdge(x, y)
    Null(x, y) :- (Null(x, w), Arc(w, y))
  }
}
