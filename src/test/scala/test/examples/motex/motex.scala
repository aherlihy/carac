package test.examples.motex

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths
class motex_test extends ExampleTestGenerator("motex") with motex
trait motex {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/motex/facts"
  val toSolve = "query"
  def pretest(program: Program): Unit = {
    val assign = program.relation[Constant]("assign")
    val assignOp = program.namedRelation[Constant]("assignOp")
    val inverses = program.namedRelation[Constant]("inverses")
    val equiv = program.relation[Constant]("equiv")
    val query = program.relation[Constant]("query")

    val a, b, x, y, cst, f2, f1 = program.variable()

//    assignOp("v1", "+", "v0", 1) :- ()
//    assignOp("v2", "-", "v1", 1) :- ()
//    inverses("+", "-") :- ()

    equiv(a, b) :- assign(a, b)
    equiv(a, b) :- (equiv(a, x), equiv(x, b))
    //  equiv(a, b) :- ( assignOp(x, f1, a, cst), assignOp(b, f2, y, cst), equiv(x, y), inverses(f1, f2))
    equiv(a, b) :- (assignOp(x, f1, a, cst), assignOp(b, f2, x, cst), inverses(f1, f2))

    query(x) :- equiv(x, "v2") // should be "v0"
  }
}
