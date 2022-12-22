package examples.clique
import datalog.dsl.{Constant, Program}
import tools.GraphGenerator

import java.nio.file.Paths

class clique extends GraphGenerator(
  Paths.get("src", "test", "scala", "examples", "clique")// TODO: use pwd
) {
  override val toSolve = "same_clique"
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