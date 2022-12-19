package graphs

import datalog.dsl.{Program, Constant}
class anon_var extends TestIDB {

  def run(program: Program): Unit = {
    val Check = program.namedRelation[Constant]("Check")

    val In = program.namedRelation[Constant]("In")

    val A1 = program.relation[Constant]("A1")

    val a, b, c, d, e, f, i = program.variable()
    val any1, any2, any3, any4, any5, any6, any7, any8, any9, any10 = program.variable()
    val anyany = program.variable()
    // po1

    A1(1,i) :- ( Check(any1, b, c, d, e, f), In(any2, b, c, d, e, f, i) )
    A1(2,i) :- ( Check(a, any1, c, d, e, f), In(a, any2, c, d, e, f, i) )
    A1(3,i) :- ( Check(a, b, any1, d, e, f), In(a, b, any2, d, e, f, i) )
    A1(4,i) :- ( Check(a, b, c, any1, e, f), In(a, b, c, any2, e, f, i) )
    A1(5,i) :- ( Check(a, b, c, d, any1, f), In(a, b, c, d, any2, f, i) )
    A1(6,i) :- ( Check(a, b, c, d, e, any1), In(a, b, c, d, e, any2, i) )

    A1(7, i) :- ( Check(any1, any2, c, d, e, f), In(any3, any4, c, d, e, f, i) )
    A1(8, i) :- ( Check(a, any1, any2, d, e, f), In(a, any3, any4, d, e, f, i) )
    A1(9, i) :- ( Check(a, b, any1, any2, e, f), In(a, b, any3, any4, e, f, i) )
    A1(10, i) :- ( Check(a, b, c, any1, any2, f), In(a, b, c, any3, any4, f, i) )
    A1(11, i) :- ( Check(a, b, c, d, any1, any2), In(a, b, c, d, any3, any4, i) )

    A1(12, i) :- ( Check(any1, any2, any3, d, e, f), In(any4, any5, any6, d, e, f, i) )
    A1(13, i) :- ( Check(a, any1, any2, any3, e, f), In(a, any4, any5, any6, e, f, i) )
    A1(14, i) :- ( Check(a, b, any1, any2, any3, f), In(a, b, any4, any5, any6, f, i) )
    A1(15, i) :- ( Check(a, b, c, any1, any2, any3), In(a, b, c, any4, any5, any6, i) )

    A1(16, i) :- ( Check(any1, any2, any3, any4, e, f), In(any5, any6, any7, any8, e, f, i) )
    A1(17, i) :- ( Check(a, any1, any2, any3, any4, f), In(a, any5, any6, any7, any8, f, i) )
    A1(18, i) :- ( Check(a, b, any1, any2, any3, any4), In(a, b, any5, any6, any7, any8, i) )

    A1(19, i) :- ( Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i) )

    // po2
    val A2 = program.relation[Constant]("A2")
    A2(1,i) :- ( Check(any1, b, c, d, e, any2), In(any3, b, c, d, e, any4, i) )
    A2(2,i) :- ( Check(a, any1, c, d, e, any2), In(a, any3, c, d, e, any4, i) )
    A2(3,i) :- ( Check(a, b, any1, any2, e, f), In(a, b, any3, any4, e, f, i) )
    A2(4,i) :- ( Check(any1, any2, c, d, any3, any4), In(any5, any6, c, d, any7, any8, i) )
    A2(5,i) :- ( Check(a, any1, any2, any3, any4, f), In(a, any5, any6, any7, any8, f, i) )
    A2(6,i) :- ( Check(any1, b, c, d, any2, f), In(any3, b, c, d, any4, f, i) )
    A2(7, i) :- ( Check(any1, b, c, any2, e, f), In(any3, b, c, any4, e, f, i) )
    A2(8, i) :- ( Check(any1, b, any2, d, e, f), In(any3, b, any4, d, e, f, i) )
    A2(9, i) :- ( Check(any1, any2, c, d, e, f), In(any3, any4, c, d, e, f, i) )
    A2(10, i) :- ( Check(any1, b, c, d, any2, any3), In(any4, b, c, d, any5, any6, i) )
    A2(11, i) :- ( Check(any1, b, c, any2, any3, f), In(any4, b, c, any5, any6, f, i) )
    A2(12, i) :- ( Check(any1, b, any2, any3, e, f), In(any4, b, any5, any6, e, f, i) )
    A2(13, i) :- ( Check(any1, any2, any3, d, e, f), In(any4, any5, any6, d, e, f, i) )
    A2(14, i) :- ( Check(any1, b, c, any2, any3, any4), In(any5, b, c, any6, any7, any8, i) )
    A2(15, i) :- ( Check(any1, b, any2, any3, any4, f), In(any5, b, any6, any7, any8, f, i) )
    A2(16, i) :- ( Check(any1, any2, any3, any4, e, f), In(any5, any6, any7, any8, e, f, i) )
    A2(17, i) :- ( Check(any1, b, c, d, e, f), In(any2, b, c, d, e, f, i) )
    A2(18, i) :- ( Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i) )

    // po3
    val A3 = program.relation[Constant]("A3")
    A3(1,i) :- ( Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i) )
    A3(2,i) :- ( Check(a, any1, c, d, e, f), In(a, any2, c, d, e, f, i) )
    A3(3,i) :- ( Check(a, any1, any2, d, e, f), In(a, any3, any4, d, e, f, i) )
    A3(4,i) :- ( Check(a, any1, any2, any3, e, f), In(a, any4, any5, any6, e, f, i) )
    A3(5,i) :- ( Check(a, any1, any2, any3, any4, f), In(a, any5, any6, any7, any8, f, i) )
    A3(6,i) :- ( Check(a, any1, any2, any3, any4, any5), In(a, any6, any7, any8, any9, any10, i) )
    A3(7, i) :- ( Check(a, b, any1, d, e, f), In(a, b, any2, d, e, f, i) )
    A3(8, i) :- ( Check(a, b, any1, any2, e, f), In(a, b, any3, any4, e, f, i) )
    A3(9, i) :- ( Check(a, b, any1, any2, any3, f), In(a, b, any4, any5, any6, f, i) )
    A3(10, i) :- ( Check(a, b, any1, any2, any3, any4), In(a, b, any5, any6, any7, any8, i) )
    A3(11, i) :- ( Check(a, b, c, any1, e, f), In(a, b, c, any2, e, f, i) )
    A3(12, i) :- ( Check(a, b, c, any1, any2, f), In(a, b, c, any3, any4, f, i) )
    A3(13, i) :- ( Check(a, b, c, any1, any2, any3), In(a, b, c, any4, any5, any6, i) )
    A3(14, i) :- ( Check(a, b, c, d, any1, f), In(a, b, c, d, any2, f, i) )
    A3(15, i) :- ( Check(a, b, c, d, any1, any2), In(a, b, c, d, any3, any4, i) )

    // po4
    val A4 = program.relation[Constant]("A4")
    A4(1,i) :- ( Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i) )
    A4(2,i) :- ( Check(a, b, c, d, any1, any2), In(a, b, c, d, any3, any4, i) )
    A4(3,i) :- ( Check(a, any1, c, d, any2, any3), In(a, any6, c, d, any4, any5, i) )
    A4(4,i) :- ( Check(a, b, any1, d, any2, any3), In(a, b, any4, d, any5, any6, i) )
    A4(5,i) :- ( Check(a, b, c, any1, any2, any3), In(a, b, c, any4, any5, any6, i) )
    A4(6,i) :- ( Check(any1, any2, c, d, any3, any4), In(any5, any8, c, d, any6, any7, i) )
    A4(7, i) :- ( Check(a, b, any1, any2, any3, any4), In(a, b, any5, any6, any7, any8, i) )
    A4(8, i) :- ( Check(a, any1, any2, any3, any4, any5), In(a, any6, any7, any8, any9, any10, i) )
    A4(9, i) :- ( Check(any1, b, any2, any3, any4, any5), In(any6, b, any7, any8, any9, any10, i) )

    // po5
    val A5 = program.relation[Constant]("A5")
    A5(1,i) :- ( Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i) )
    A5(2,i) :- ( Check(a, any1, any2, d, e, f), In(a, any3, any4, d, e, f, i) )
    A5(3,i) :- ( Check(a, b, any1, d, e, f), In(a, b, any2, d, e, f, i) )
    A5(4,i) :- ( Check(a, any1, any2, any3, e, f), In(a, any4, any5, any6, e, f, i) )
    A5(5,i) :- ( Check(a, b, any1, any2, any3, f), In(a, b, any4, any5, any6, f, i) )
    A5(6,i) :- ( Check(a, any1, any2, any3, any4, any5), In(a, any6, any7, any8, any9, any10, i) )
    A5(7, i) :- ( Check(a, b, any1, d, e, f), In(a, b, any2, d, e, f, i) )
    A5(8, i) :- ( Check(a, b, any1, any2, e, f), In(a, b, any3, any4, e, f, i) )
    A5(9, i) :- ( Check(a, b, any1, any2, any3, f), In(a, b, any4, any5, any6, f, i) )
    A5(10, i) :- ( Check(a, b, any1, any2, any3, any4), In(a, b, any5, any6, any7, any8, i) )
    A5(11, i) :- ( Check(a, b, c, any1, e, f), In(a, b, c, any2, e, f, i) )
    A5(12, i) :- ( Check(a, any1, c, any2, any3, f), In(a, any4, c, any5, any6, f, i) )
    A5(13, i) :- ( Check(a, b, c, any1, any2, any3), In(a, b, c, any4, any5, any6, i) )
    A5(14, i) :- ( Check(a, b, c, d, any1, f), In(a, b, c, d, any2, f, i) )
    A5(15, i) :- ( Check(a, any1, c, d, any2, any3), In(a, any4, c, d, any5, any6, i) )
  }
}
