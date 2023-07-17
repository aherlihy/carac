package test.examples.disconnected
import datalog.dsl.{Constant, Program, __}
import test.ExampleTestGenerator
class disconnected_test extends ExampleTestGenerator("disconnected") with disconnected
trait disconnected {
  val toSolve: String = "DisConnected"
  def pretest(program: Program): Unit = {
    val Edge = program.namedRelation[Constant]("Edge")

    val n, m, z = program.variable()
    val DisConnected = program.relation[Constant]("DisConnected")
    val Connected = program.relation[Constant]("Connected")
    
    Connected(n, n) :- ( Edge(n, __, __) )
    Connected(n, n) :- ( Edge(__, n, __) )
    Connected(n, m) :- ( Edge(n, m, __) )
    Connected(n, m) :- ( Edge(n, z, __), Connected(z, m) )
    
    DisConnected(n, m) :- ( Edge(n, __, __), Edge(__, m, __), !Connected(n, m) )
    DisConnected(n, m) :- ( Edge(n, __, __), Edge(m, __, __), !Connected(n, m) )
  }
}
