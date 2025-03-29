package test.examples.traffic

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths
class traffic_test extends ExampleTestGenerator("traffic") with traffic
trait traffic {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/traffic/facts"
  val toSolve: String = "crashes"
  def pretest(program: Program): Unit = {
    val crashable = program.relation[Constant]("crashable")
    val crashes = program.relation[Constant]("crashes")
    val intersect = program.relation[Constant]("intersect")
    val greenSignal = program.relation[Constant]("greenSignal")
    val hasTraffic = program.relation[Constant]("hasTraffic")

    val X, Y = program.variable()
    
    crashable(X, Y) :- ( intersect(X, Y), greenSignal(X), greenSignal(Y) )
    crashes(X) :- ( hasTraffic(X), crashable(X, Y), hasTraffic(Y) )
    crashes(X) :- ( hasTraffic(X), crashable(Y, X), hasTraffic(Y) )
    
    greenSignal("Abercrombie St") :- ()
    greenSignal("Cleveland St") :- ()
    greenSignal("Shepard St") :- ()
    greenSignal("Elizabeth St") :- ()
    greenSignal("Goulburn St") :- ()
    
    hasTraffic("Abercrombie St") :- ()
    hasTraffic("Lawson St") :- ()
    hasTraffic("Elizabeth St") :- ()
    hasTraffic("Goulburn St") :- ()
    
    intersect("Abercrombie St", "Lawson St") :- ()
    intersect("Cleveland St", "Shepard St") :- ()
    intersect("Elizabeth St", "Goulburn St") :- ()
  }
}
