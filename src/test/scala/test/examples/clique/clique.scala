package test.examples.clique

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths

class clique_test extends ExampleTestGenerator("clique") with clique
trait clique {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/clique/facts"
  val toSolve = "same_clique"
  def pretest(program: Program): Unit = {
    val edge = program.namedRelation("edge")
    val reachable = program.relation[Constant]("reachable")
    val same_clique = program.relation[Constant]("same_clique")
    val x, y, z = program.variable()

    reachable(x, y) :- edge(x, y)
    reachable(x, y) :- ( edge(x, z), reachable(z, y) )
    same_clique(x, y) :- ( reachable(x, y), reachable(y, x) )
  }
 }