package test.examples.array

import buildinfo.BuildInfo
import datalog.dsl.*
import test.ExampleTestGenerator

import java.nio.file.Paths

class array_test extends ExampleTestGenerator("array") with array
trait array {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/array/facts"
  val toSolve = "_"
  def pretest(program: Program): Unit = {
    val default = program.namedRelation("default")
    val parameters = program.namedRelation("parameters")
    val values = program.namedRelation("values")

    val dom = program.relation[Constant]("dom")
    val indices = program.relation[Constant]("indices")
    val element = program.relation[Constant]("element")

    val left = program.relation[Constant]("left")
    val right = program.relation[Constant]("right")
    val neighbourhood = program.relation[Constant]("neighbourhood")

    val i, j, k, l = program.variable()

    val tmp1 = program.relation[Constant]("tmp1")
    val tmp2 = program.relation[Constant]("tmp2")

    (-1 to 100).foreach(x =>
      dom(x) :- ()
    )

    indices(0) :- ()
    indices(i) :- (indices(j), parameters(k), dom(i), i |=| (j + 1), i |<| k)

    tmp1(i) :- values(i, __)

    element(i, j) :- (indices(i), !tmp1(i), default(j))
    element(i, j) :- (indices(i), values(i, j))

    tmp2(i) :- element(i, __)
    
    left(i, j) :- (!tmp2(k), element(i, __), default(j), k |=| (i - 1), dom(k))
    left(i, j) :- (element(k, j), element(i, __), k |=| (i - 1))
    
    right(i, j) :- (!tmp2(k), element(i, __), default(j), k |=| (i + 1), dom(k))
    right(i, j) :- (element(k, j), element(i, __), k |=| (i + 1))
    
    neighbourhood(i, j, k, l) :- (left(i, j), element(i, k), right(i, l))
  }
 }