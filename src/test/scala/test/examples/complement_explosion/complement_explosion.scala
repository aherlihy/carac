package test.examples.small

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths

class complement_explosion_test extends ExampleTestGenerator("complement_explosion") with complement_explosion
trait complement_explosion {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/complement_explosion/facts"
  val toSolve = "derived"
  def pretest(program: Program): Unit = {
    val dom = program.relation[Constant]("dom")

    val base = program.relation[Constant]("base")
    val derived = program.relation[Constant]("derived")

    val a, b, c, d, e, f = program.variable()
    
    (1 to 100).foreach(x =>
      dom(x) :- ()  
    )

    base(1, 2, 3, 4, 5, 6) :- ()
    base(11, 12, 13, 14, 15, 16) :- ()

    derived(a, b, c, d, e, f) :- (base(a, b, c, d, e, f), !base(f, e, d, c, b, a))
  }
}
