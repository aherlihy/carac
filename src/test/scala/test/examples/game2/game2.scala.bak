package test.examples.game2

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program, __}
import test.{ExampleTestGenerator, Tags}
class game2_test extends ExampleTestGenerator("game2", tags = Set(Tags.Negated)) with game2
trait game2 {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/game2/facts"
  val toSolve: String = "winning"
  def pretest(program: Program): Unit = {
    val X, Y, Z1, Z2 = program.variable()
    val canMove = program.relation[Constant]("canMove")

    val move = program.relation[Constant]("move")

    val odd_move = program.relation[Constant]("odd_move")

    val possible_winning = program.relation[Constant]("possible_winning")

    val winning = program.relation[Constant]("winning")

    
    move("1","2") :- ()
    move("2","3") :- ()
    move("3","4") :- ()
    move("1","3") :- ()
    move("1","5") :- ()
    
    canMove(X) :- ( move(X,__) )
    
    possible_winning(X):- ( odd_move(X,Y), !canMove(Y) )
    
    winning(X):- ( move(X,Y), !possible_winning(Y) )
    
    odd_move(X,Y) :- ( move(X,Y) )
    
    odd_move(X,Y) :- ( move(X,Z1), move(Z1,Z2), odd_move(Z2,Y) )
  }
}
