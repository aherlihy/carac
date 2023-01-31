package datalog.benchmarks

import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.{ExecutionEngine, SemiNaiveExecutionEngine, SemiNaiveStagedExecutionEngine}
import datalog.storage.CollectionsStorageManager
import org.openjdk.jmh.annotations.{Benchmark, Fork, Measurement, Scope, Setup, State, Warmup, BenchmarkMode, Mode}
import org.openjdk.jmh.infra.Blackhole

import java.util.concurrent.TimeUnit
import scala.collection.mutable
import scala.util.Random

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS, batchSize = 3)
@Measurement(iterations = 20, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 3)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStaged5xJoin {
  // full compiled
  val compiled_e0 = SemiNaiveStagedExecutionEngine(CollectionsStorageManager())
  val compiled_program0 = Program(compiled_e0)
  val compiled_s0 = pretest(compiled_program0)

  // do tree processing ahead-of-time, measure only compile+run generated code
  val compiled_e1 = SemiNaiveStagedExecutionEngine(CollectionsStorageManager())
  val compiled_program1 = Program(compiled_e1)
  val compiled_s1 = pretest(compiled_program1)
  val (compiled_program1_tree, compiled_program1_ctx) = compiled_e1.generateProgramTree(compiled_s1)

  // do tree processing and compilation ahead-of-time, measure only running generated code
  val compiled_e2 = SemiNaiveStagedExecutionEngine(CollectionsStorageManager())
  val compiled_program2 = Program(compiled_e2)
  val compiled_s2 = pretest(compiled_program2)
  val (compiled_program2_tree, compiled_program2_ctx) = compiled_e2.generateProgramTree(compiled_s2)
  val compiled_program2_compiled = compiled_e2.getCompiled(compiled_program2_tree, compiled_program2_ctx)

  // og
  val original_e3 = SemiNaiveExecutionEngine(CollectionsStorageManager())
  val original_program3 = Program(original_e3)
  val original_s3 = pretest(original_program3)

  // full interpreted
  val interpreted_e0 = SemiNaiveStagedExecutionEngine(CollectionsStorageManager())
  val interpreted_program0 = Program(interpreted_e0)
  val interpreted_s0 = pretest(interpreted_program0)

  // do tree processing ahead-of-time, measure only running interpreted
  val interpreted_e1 = SemiNaiveStagedExecutionEngine(CollectionsStorageManager())
  val interpreted_program1 = Program(interpreted_e1)
  val interpreted_s1 = pretest(interpreted_program1)
  val (interpreted_program1_tree, interpreted_program1_ctx) = interpreted_e1.generateProgramTree(interpreted_s1)

//  println("____init finished______")

  def pretest(program: Program): Int = {
    val edge = program.relation[Constant]("edge")
    val path = program.relation[Constant]("path")
    val hops1 = program.relation[Constant]("hops1")
    val hops2_join = program.relation[Constant]("hops2_join")
    val hops3_join = program.relation[Constant]("hops3_join")
    val hops4_join = program.relation[Constant]("hops4_join")
    val hops5_join = program.relation[Constant]("hops5_join")
//    val hops6_join = program.relation[Constant]("hops6_join")
//    val hops7_join = program.relation[Constant]("hops7_join")
//    val hops8_join = program.relation[Constant]("hops8_join")
//    val hops9_join = program.relation[Constant]("hops9_join")
//    val hops10_join = program.relation[Constant]("hops10_join")

    val x, y, z, w, q = program.variable()
    val a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11 = program.variable()

    path(x, y) :- edge(x, y)
    path(x, z) :- (edge(x, y), path(y, z))

    hops1(x, y) :- edge(x, y)
    hops2_join(a1, a3) :-   (hops1(a1, a2), hops1(a2, a3))
    hops3_join(a1, a4) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4))
    hops4_join(a1, a5) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5))
    hops5_join(a1, a6) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6))
//    hops6_join(a1, a7) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7))
//    hops7_join(a1, a8) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8))
//    hops8_join(a1, a9) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8), hops1(a8, a9))
//    hops9_join(a1, a10) :-  (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8), hops1(a8, a9), hops1(a9, a10))
//    hops10_join(a1, a11) :- (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8), hops1(a8, a9), hops1(a9, a10), hops1(a10, a11))


    for i <- 0 until 200 do
      edge(
        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString,
        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString
      ) :- ()
    hops5_join.id
  }

  // measure cost of tree gen, compiling, running
  @Benchmark def full_compiled(blackhole: Blackhole): Unit = {
    blackhole.consume(
      compiled_e0.solveCompiled(compiled_s0)
    )
  }
  // measure cost of compiling, running
  @Benchmark def compile_and_run_compiled(blackhole: Blackhole): Unit = {
    blackhole.consume(
      compiled_e1.compileAndRun(compiled_program1_tree, compiled_program1_ctx)
    )
  }
  // measure cost of running compiled code
  @Benchmark def run_only_compiled(blackhole: Blackhole): Unit = {
    blackhole.consume(
      compiled_e2.solvePreCompiled(compiled_program2_compiled, compiled_program2_ctx)
    )
  }
  // measure original
  @Benchmark def run_original(blackhole: Blackhole): Unit = {
    blackhole.consume(
      original_program3.solve(original_s3)
    )
  }
  // measure cost of tree gen, interpreting
  @Benchmark def full_interpreted(blackhole: Blackhole): Unit = {
    blackhole.consume(
      interpreted_e0.solveInterpreted(interpreted_s0)
    )
  }
  // measure cost of running interpreted code
  @Benchmark def run_only_interpreted(blackhole: Blackhole): Unit = {
    blackhole.consume(
      interpreted_e1.solvePreInterpreted(interpreted_program1_tree, interpreted_program1_ctx)
    )
  }
}
