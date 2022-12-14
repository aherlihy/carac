// Example adapted Souffle which adapted from (hacked) from Olson, Gunter, Madhusudan CCS'08, Data is random
package graphs

import datalog.dsl.{Program, Constant}

class access_policy extends TestIDB {
  def run(program: Program): Unit = {
    val Person, SSN, Salary, Email, Dept, Position, Bday = program.variable()
    val Bank1Priv, Bank2Priv, Data1, Data2, User = program.variable()
    val any, any1, any2, any3, any4, any5, any6, any7 = program.variable()
    val view_employee = program.relation[Constant]("view_employee")
    val view_cwPriv = program.relation[Constant]("view_cwPriv")
    val view_bank = program.relation[Constant]("view_bank")
    val view_bank1 = program.relation[Constant]("view_bank1")
    val view_bank2 = program.relation[Constant]("view_bank2")

    val insurance = program.namedRelation[Constant]("insurance")
    val inslogtable = program.namedRelation[Constant]("inslogtable")
    val declwPriv = program.namedRelation[Constant]("declwPriv")
    val insecwPriv = program.namedRelation[Constant]("insecwPriv")
    val cwPriv = program.namedRelation[Constant]("cwPriv")
    val bank1 = program.namedRelation[Constant]("bank1")
    val bank2 = program.namedRelation[Constant]("bank2")
    val employee = program.namedRelation[Constant]("employee")


    view_employee("Alice", Person, SSN, Salary, Email, Dept, Position, Bday) :-
      employee(Person, SSN, Salary, Email, Dept, Position, Bday)

// TODO: add var constraint, does repeated vars in head add constraint
//    view_employee(User, Person, SSN, Salary, Email, Dept, Position, Bday) :-
//      User=Person, view_employee("alice", Person, SSN, Salary, Email, Dept, Position, Bday).

    view_employee(User, Person, SSN, Salary, Email, Dept, Position, Bday) :- (
      view_employee("Alice", User, any, any1, any2, Dept,"Manager", any3),
      view_employee("Alice", Person, SSN, Salary, Email, Dept, Position, Bday)
    )

    view_employee(User, Person, "0", 0, Email, Dept, Position, Bday) :- (
      view_employee("Alice", User, any, any1, any2, any3, any4, any5),
      view_employee("Alice", Person, any6, any7, Email, Dept, Position, Bday)
    )

    view_employee(User, Person, "0", 0, " ", " ", " ", Bday) :- (
      insurance(User),
      view_employee("Alice", Person, any, any1, any2, any3, any4, Bday),
      inslogtable(User, Person, 92311)
    )

    view_cwPriv("Bob", Person, Bank1Priv, Bank2Priv) :- cwPriv(Person, Bank1Priv, Bank2Priv)

    view_bank1("Bob", Data1, Data2) :- bank1(Data1, Data2)

    view_bank1(User, Data1, Data2) :- (
      view_cwPriv("Bob", User, 1, any),
      declwPriv(User, 1, any1), insecwPriv(User, 1, 0),
      view_bank1("Bob", Data1, Data2)
    )

    view_bank2("Bob", Data1, Data2) :- bank2(Data1, Data2)

    view_bank2(User, Data1, Data2) :- (
      view_cwPriv("Bob", User, any, 1),
      declwPriv(User, any1, 1),
      insecwPriv(User, 0, 1),
      view_bank2("Bob", Data1, Data2)
    )
  }
  override val skip = Seq("Naive")
}
