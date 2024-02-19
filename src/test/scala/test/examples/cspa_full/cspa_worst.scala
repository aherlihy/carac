//package test.examples.cspa_full
//
//import buildinfo.BuildInfo
//import datalog.dsl.{Constant, Program}
//import test.{ExampleTestGenerator, Tags}
//
//class cspa_worst_test extends ExampleTestGenerator(
//  "cspa",
//  Set(Tags.Naive, Tags.Volcano),
//  Set(Tags.Slow, Tags.CI)
//) with cspa
//
//trait cspa_worst {
//  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/cspa/facts"
//  val toSolve = "ValueFlow"
//
//  // Adapted from https://github.com/Hacker0912/RecStep/blob/e4107c814c7b24ae787dddb4af7e3238303f13ab/benchmark_datalog_programs/cspa.datalog
//  def pretest(program: Program): Unit = {
//    val Assign = program.namedRelation[String]("Assign")
//    val Dereference = program.namedRelation[String]("Dereference")
//
//    val ValueFlow = program.relation[String]("ValueFlow")
//    val ValueAlias = program.relation[String]("ValueAlias")
//    val MemoryAlias = program.relation[String]("MemoryAlias")
//
//    val v0, v1, v2, v3 = program.variable()
//
//    ValueFlow(v2, v1) :- Assign(v2, v1)
//    ValueFlow(v1, v1) :- Assign(v1, v2)
//    ValueFlow(v1, v1) :- Assign(v2, v1)
//
//    MemoryAlias(v1, v1) :- Assign(v2, v1)
//    MemoryAlias(v1, v1) :- Assign(v1, v2)
//
////    ValueFlow(v1, v2) :- (Assign(v1, v3), MemoryAlias(v3, v2))
//    ValueFlow(v1, v2) :- (MemoryAlias(v3, v2), Assign(v1, v3)) // badluck
////    ValueFlow(v1, v2) :- (ValueFlow(v1, v3), ValueFlow(v3, v2))
//    ValueFlow(v1, v2) :- (ValueFlow(v3, v2), ValueFlow(v1, v3)) // badluck
//
////    MemoryAlias(v1, v0) :- (Dereference(v2, v1), ValueAlias(v2, v3), Dereference(v3, v0))
//    MemoryAlias(v1, v0) :- (ValueAlias(v2, v3), Dereference(v3, v0), Dereference(v2, v1)) // badluck
//
////    ValueAlias(v1, v2) :- (ValueFlow(v3, v1), ValueFlow(v3, v2))
//    ValueAlias(v1, v2) :- (ValueFlow(v3, v2), ValueFlow(v3, v1)) // badluck
////    ValueAlias(v1, v2) :- (ValueFlow(v3, v1), MemoryAlias(v3, v0), ValueFlow(v0, v2))
//    ValueAlias(v1, v2) :- (ValueFlow(v0, v2), ValueFlow(v3, v1), MemoryAlias(v3, v0)) // badluck
//  }
//}
