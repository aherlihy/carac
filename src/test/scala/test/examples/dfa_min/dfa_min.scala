package test.examples.dfa_min

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program, __, *}
import test.{ExampleTestGenerator, Tags}
class dfa_min_test extends ExampleTestGenerator("dfa_min") with dfa_min
trait dfa_min {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/dfa_min/facts"
  val toSolve: String = "MinEquiv"
  def pretest(program: Program): Unit = {
    val Final = program.namedRelation("Final")
    val Tr = program.namedRelation("Tr")
    val Init = program.relation[Constant]("Init")
    val Q = program.relation[Constant]("Q")

    val Dis = program.relation[Constant]("Dis")
    val Equiv = program.relation[Constant]("Equiv")
    val NotMinEquiv = program.relation[Constant]("NotMinEquiv")
    val MinEquiv = program.relation[Constant]("MinEquiv")

    val q, r, a, s, t = program.variable()

    Init(0) :- ()

    Q(q) :- Tr(q, __, __)

    Dis(q, r) :- (Q(q), Q(r), Final(q), !Final(r))
    Dis(q, r) :- (Tr(q, a, s), Tr(r, a, t), Dis(s, t))
    Dis(q, r) :- Dis(r, q)

    Equiv(q, r) :- (Q(q), Q(r), !Dis(q, r))

    NotMinEquiv(q, r) :- (Equiv(q, r), Equiv(q, s), s |<| r)
    MinEquiv(q, r) :- (Equiv(q, r), !NotMinEquiv(q, r))
  }
}
