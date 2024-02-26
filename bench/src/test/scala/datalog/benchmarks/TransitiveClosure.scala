//package datalog.benchmarks
//
//import datalog.dsl.{Constant, Program, Relation, Term}
//import datalog.execution.ExecutionEngine
//import datalog.storage.StorageTerm
//
//import scala.collection.mutable
//import scala.util.Random
//
//class TransitiveClosure extends DLBenchmark {
//  val toSolve: String = "fourHops"
//  override val description: String = "TransitiveClosure"
//  override val expectedFacts: mutable.Map[String, Set[Seq[StorageTerm]]] = mutable.Map(
//    "fourHops" -> Set.empty,
//  )
//  def pretest(program: Program): Unit = {
//    val edge = program.relation[Constant]("edge")
//    val path = program.relation[Constant]("path")
//    val path2a = program.relation[Constant]("path2a")
//    val oneHop = program.relation[Constant]("oneHop")
//    val twoHops = program.relation[Constant]("twoHops")
//    val threeHops = program.relation[Constant]("threeHops")
//    val fourHopsJoin = program.relation[Constant]("fourHopsJoin")
//    val fourHops = program.relation[Constant]("fourHops")
//
//    val x, y, z, w, q = program.variable()
//
//    path(x, y) :- edge(x, y)
//    path(x, z) :- (edge(x, y), path(y, z))
//    path2a(x) :- path(x, "a")
//
//    oneHop(x, y) :- edge(x, y)
//    twoHops(x, z) :- (edge(x, y), oneHop(y, z))
//    threeHops(x, w) :- (edge(x, y), twoHops(y, w))
//    fourHops(x, q) :- (edge(x, y), threeHops(y, q))
//
//    fourHopsJoin(x, q) :- (edge(x, y), oneHop(y, z), oneHop(z, w), oneHop(w, q))
//
//    for i <- 0 until 50 do
//      edge(
//        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString,
//        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString
//      ) :- ()
//  }
//
//  override def finish(): Unit =
//    if(result.size != 1)
//      throw new Exception(s"Benchmark error in $description: wrong size")
//}
