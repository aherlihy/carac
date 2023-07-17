package test.examples.orbits
import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator
class orbits_test extends ExampleTestGenerator("orbits") with orbits
trait orbits {
  val toSolve: String = "_"
  def pretest(program: Program): Unit = {
    val X, Y, Z = program.variable()
    val intermediate = program.relation[Constant]("intermediate")

    val orbits = program.relation[Constant]("orbits")

    val planet = program.relation[Constant]("planet")

    val satellite = program.relation[Constant]("satellite")

    val star = program.relation[Constant]("star")

    
    star("sun") :- ()
    orbits("earth", "sun") :- ()
    orbits("moon", "earth") :- ()
    
    orbits(X,Y) :- (
      orbits(X,Z),
      orbits(Z,Y) )
    
    satellite(X,Y) :- (
      orbits(X,Y),
      !intermediate(X,Y),
      !star(Y) )
    
    planet(X) :- (
      orbits(X,Y),
      star(Y),
      !intermediate(X,Y) )
    
    intermediate(X,Y) :- (
      orbits(X,Z),
      orbits(Z,Y) )
  }
}
