package test.examples.orbits1

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program, __}
import test.{ExampleTestGenerator, Tags}
class orbits1_test extends ExampleTestGenerator("orbits1", tags = Set(Tags.Negated)) with orbits1
trait orbits1 {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/orbits1/facts"
  val toSolve: String = "_"
  def pretest(program: Program): Unit = {
    val X, Y, Z = program.variable()
    val intermediate = program.relation[Constant]("intermediate")

    val orbits = program.relation[Constant]("orbits")

    val planet = program.relation[Constant]("planet")

    val star = program.relation[Constant]("star")

    
    star("sun") :- ()
    orbits("earth", "sun") :- ()
    orbits("moon", "earth") :- ()
    
    orbits(X,Y) :- (
      orbits(X,Z),
      orbits(Z,Y) )
    
    planet(X) :- (
      orbits(X,Y),
      star(Y),
      !intermediate(X,Y) )
    
    intermediate(X,Y) :- (
      orbits(X,Y),
      orbits(__,Y) )
  }
}
