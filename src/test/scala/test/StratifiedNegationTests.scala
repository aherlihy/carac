//package test
//
//import carac.dsl.{Constant, Program, __, not}
//import carac.execution.*
//import carac.storage.DefaultStorageManager
//
//class StratifiedNegationTests extends munit.FunSuite {
//
//  test("free variable in rule throws") {
//    val p = Program(new NaiveShallowExecutionEngine(new DefaultStorageManager()))
//    val e = p.relation[Constant]("e")
//    val t = p.relation[Constant]("t")
//    val tc = p.relation[Constant]("tc")
//    val x, y, z = p.variable()
//
//    e(1, 2) :- ()
//    e(2, 3) :- ()
//    t(x, y) :- e(x, y)
//    t(x, z) :- (t(x, y), e(y, z))
//
//    interceptMessage[java.lang.Exception]("Variable with varId 0 appears only in negated rules") {
//      tc(x, y) :- not(t(x, y)) // x and y are not limited.
//    }
//  }
//
//  test("non-limited variable in rule throws") {
//    val p = Program(new NaiveShallowExecutionEngine(new DefaultStorageManager()))
//    val e = p.relation[Constant]("e")
//    val t = p.relation[Constant]("t")
//    val tc = p.relation[Constant]("tc")
//    val x, y, z = p.variable()
//
//    e(1, 2) :- ()
//    e(2, 3) :- ()
//    t(x, y) :- e(x, y)
//    t(x, z) :- (t(x, y), e(y, z))
//
//    interceptMessage[java.lang.Exception]("Variable with varId 2 appears only in negated rules") {
//      tc(x, y) :- (e(x, y), !e(x, z), !e(x, z))
//    }
//  }
//
//  test("non-stratifiable program throws") {
//    val p = Program(new StagedExecutionEngine(new DefaultStorageManager()))
//    val e = p.relation[Constant]("e")
//    val x, y = p.variable()
//
//    e(1, 2) :- ()
//    e(x, y) :- (e(x, y), not(e(y, x)))
//
//    interceptMessage[java.lang.Exception]("Negative cycle detected in input program") {
//      p.solve(e.id)
//    }
//  }
//}
