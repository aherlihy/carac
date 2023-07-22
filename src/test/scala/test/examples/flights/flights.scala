package test.examples.flights
import datalog.dsl.{Constant, Program}
import test.{ExampleTestGenerator, Tags}
class flights_test extends ExampleTestGenerator("flights", tags = Set(Tags.Negated)) with flights
trait flights {
  val toSolve: String = "QAonly"
  def pretest(program: Program): Unit = {
    val X, Y, Z = program.variable()
    val QAflies = program.relation[Constant]("QAflies")

    val QAonly = program.relation[Constant]("QAonly")

    val VAflies = program.relation[Constant]("VAflies")

    val flight = program.relation("flight")

    
    QAflies(X,Y) :- ( flight("QA",X,Y) )
    QAflies(X,Y) :- ( flight("QA",X,Z), QAflies(Z,Y) )
    
    VAflies(X,Y) :- ( flight("VA",X,Y) )
    VAflies(X,Y) :- ( flight("VA",X,Z), VAflies(Z,Y) )
    
    QAonly(X,Y)  :- ( QAflies(X,Y), ! VAflies(X,Y) )
    
    flight("QA","AU","CHI") :- ()
    flight("QA","AU","JPN") :- ()
    flight("QA","JPN","DEN") :- ()
    flight("QA","JPN","CHI") :- ()
    flight("QA","AU","CHI") :- ()
    flight("VA","AU","CHI") :- ()
    flight("VA","JPN","ZA") :- ()
    flight("VA","JPN","DEN") :- ()
    flight("VA","DEN","ZA") :- ()
  }
}
