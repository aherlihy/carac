package graphs

import datalog.dsl.{Program, Constant}
class po1 extends TestIDB {

  def run(program: Program): Unit = {
    val Check = program.namedRelation[Constant]("Check")

    val In = program.namedRelation[Constant]("In")

    val A = program.relation[Constant]("A")

    val a, b, c, d, e, f, i = program.variable()
    val any1, any2, any3, any4, any5, any6, any7, any8 = program.variable()

    A(1,i) :- ( Check(any1, b, c, d, e, f), In(any2, b, c, d, e, f, i) )
    A(2,i) :- ( Check(a, any1, c, d, e, f), In(a, any2, c, d, e, f, i) )
    A(3,i) :- ( Check(a, b, any1, d, e, f), In(a, b, any2, d, e, f, i) )
    A(4,i) :- ( Check(a, b, c, any1, e, f), In(a, b, c, any2, e, f, i) )
    A(5,i) :- ( Check(a, b, c, d, any1, f), In(a, b, c, d, any2, f, i) )
    A(6,i) :- ( Check(a, b, c, d, e, any1), In(a, b, c, d, e, any2, i) )

    A(7, i) :- ( Check(any1, any2, c, d, e, f), In(any3, any4, c, d, e, f, i) )
    A(8, i) :- ( Check(a, any1, any2, d, e, f), In(a, any3, any4, d, e, f, i) )
    A(9, i) :- ( Check(a, b, any1, any2, e, f), In(a, b, any3, any4, e, f, i) )
    A(10, i) :- ( Check(a, b, c, any1, any2, f), In(a, b, c, any3, any4, f, i) )
    A(11, i) :- ( Check(a, b, c, d, any1, any2), In(a, b, c, d, any3, any4, i) )

    A(12, i) :- ( Check(any1, any2, any3, d, e, f), In(any4, any5, any6, d, e, f, i) )
    A(13, i) :- ( Check(a, any1, any2, any3, e, f), In(a, any4, any5, any6, e, f, i) )
    A(14, i) :- ( Check(a, b, any1, any2, any3, f), In(a, b, any4, any5, any6, f, i) )
    A(15, i) :- ( Check(a, b, c, any1, any2, any3), In(a, b, c, any4, any5, any6, i) )

    A(16, i) :- ( Check(any1, any2, any3, any4, e, f), In(any5, any6, any7, any8, e, f, i) )
    A(17, i) :- ( Check(a, any1, any2, any3, any4, f), In(a, any5, any6, any7, any8, f, i) )
    A(18, i) :- ( Check(a, b, any1, any2, any3, any4), In(a, b, any5, any6, any7, any8, i) )

    A(19, i) :- ( Check(a, b, c, d, e, f), In(a, b, c, d, e, f, i) )
  }
}
