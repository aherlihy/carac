package test.examples.metro

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths
class metro_test extends ExampleTestGenerator("metro") with metro
trait metro {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/metro/facts"
  val toSolve = "platform2_reach"
  def pretest(program: Program): Unit = {
    val platform2_reach = program.relation[Constant]("platform2_reach")

    val St_Reachable = program.relation[Constant]("St_Reachable")
    val Li_Reachable = program.relation[Constant]("Li_Reachable")
    val link = program.relation[Constant]("link")

    val x, y, u, any, any1, z = program.variable()

    St_Reachable(x, y) :- ( link(any,x,y) )
    St_Reachable(x, y) :- ( St_Reachable(x, z), link(any, z, y) )
    Li_Reachable(x, u) :- ( St_Reachable(x, z), link(u, z, any) )
    
    platform2_reach("platform2", y) :- ( St_Reachable("platform2", y), link(any, any1, y) )
    
    link("4","platform1","platform2") :- ()
    link("4","platform2","platform3") :- ()
    link("4","platform3","platform4") :- ()
    link("1","platform4","platform5") :- ()
    link("1","platform5","platform6") :- ()
    link("1","platform7","platform8") :- ()
    link("9","platform8","platform9") :- ()
    link("9","platform9","platform10") :- ()
  }
}
