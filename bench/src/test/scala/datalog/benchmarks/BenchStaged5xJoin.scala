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
@Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchStaged5xJoin {
  // full compiled
  val compiled_e0 = SemiNaiveStagedExecutionEngine(CollectionsStorageManager())
  val compiled_program0 = Program(compiled_e0)
  val compiled_s0 = pretest(compiled_program0)

  // only compile+run compiled
  val compiled_e1 = SemiNaiveStagedExecutionEngine(CollectionsStorageManager())
  val compiled_program1 = Program(compiled_e1)
  val compiled_s1 = pretest(compiled_program1)
  val (compiled_program1_tree, compiled_program1_ctx) = compiled_e1.generateProgramTree(compiled_s1)

  // only run compiled
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

  // only run interpreted
  val interpreted_e1 = SemiNaiveStagedExecutionEngine(CollectionsStorageManager())
  val interpreted_program1 = Program(interpreted_e1)
  val interpreted_s1 = pretest(interpreted_program1)
  val (interpreted_program1_tree, interpreted_program1_ctx) = interpreted_e1.generateProgramTree(interpreted_s1)

  def pretest(program: Program): Int = {
    val edge = program.relation[Constant]("edge")
    val path = program.relation[Constant]("path")
    val hops1 = program.relation[Constant]("hops1")
//    val hops2 = program.relation[Constant]("hops2")
//    val hops3 = program.relation[Constant]("hops3")
//    val hops4 = program.relation[Constant]("hops4")
//    val hops5 = program.relation[Constant]("hops5")
//    val hops6 = program.relation[Constant]("hops6")
//    val hops7 = program.relation[Constant]("hops7")
//    val hops8 = program.relation[Constant]("hops8")
//    val hops9 = program.relation[Constant]("hops9")
//    val hops10 = program.relation[Constant]("hops10")
    val hops2_join = program.relation[Constant]("hops2_join")
    val hops3_join = program.relation[Constant]("hops3_join")
    val hops4_join = program.relation[Constant]("hops4_join")
    val hops5_join = program.relation[Constant]("hops5_join")
//    val hops6_join = program.relation[Constant]("hops6_join")
//    val hops7_join = program.relation[Constant]("hops7_join")
//    val hops8_join = program.relation[Constant]("hops8_join")
//    val hops9_join = program.relation[Constant]("hops9_join")
//    val hops10_join = program.relation[Constant]("hops10_join")
//    val hops11 = program.relation[Constant]("hops11")
//    val hops12 = program.relation[Constant]("hops12")
//    val hops13 = program.relation[Constant]("hops13")
//    val hops14 = program.relation[Constant]("hops14")
//    val hops15 = program.relation[Constant]("hops15")
//    val hops16 = program.relation[Constant]("hops16")
//    val hops17 = program.relation[Constant]("hops17")
//    val hops18 = program.relation[Constant]("hops18")
//    val hops19 = program.relation[Constant]("hops19")
//    val hops20 = program.relation[Constant]("hops20")

    val x, y, z, w, q = program.variable()
    val a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11 = program.variable()

    path(x, y) :- edge(x, y)
    path(x, z) :- (edge(x, y), path(y, z))

    hops1(x, y) :- edge(x, y)
//    hops2(x, y) :- (hops1(x, z), hops1(z, y))
//    hops3(x, y) :- (hops1(x, z), hops2(z, y))
//    hops4(x, y) :- (hops1(x, z), hops3(z, y))
//    hops5(x, y) :- (hops1(x, z), hops4(z, y))
//    hops6(x, y) :- (hops1(x, z), hops5(z, y))
//    hops7(x, y) :- (hops1(x, z), hops6(z, y))
//    hops8(x, y) :- (hops1(x, z), hops7(z, y))
//    hops9(x, y) :- (hops1(x, z), hops8(z, y))
//    hops10(x, y) :- (hops1(x, z), hops9(z, y))
//    hops11(x, y) :- (hops1(x, z), hops10(z, y))
//    hops12(x, y) :- (hops1(x, z), hops11(z, y))
//    hops13(x, y) :- (hops1(x, z), hops12(z, y))
//    hops14(x, y) :- (hops1(x, z), hops13(z, y))
//    hops15(x, y) :- (hops1(x, z), hops14(z, y))
//    hops16(x, y) :- (hops1(x, z), hops15(z, y))
//    hops17(x, y) :- (hops1(x, z), hops16(z, y))
//    hops18(x, y) :- (hops1(x, z), hops17(z, y))
//    hops19(x, y) :- (hops1(x, z), hops18(z, y))
//    hops20(x, y) :- (hops1(x, z), hops19(z, y))

    hops2_join(a1, a3) :-   (hops1(a1, a2), hops1(a2, a3))
    hops3_join(a1, a4) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4))
    hops4_join(a1, a5) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5))
    hops5_join(a1, a6) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6))
//    hops6_join(a1, a7) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7))
//    hops7_join(a1, a8) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8))
//    hops8_join(a1, a9) :-   (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8), hops1(a8, a9))
//    hops9_join(a1, a10) :-  (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8), hops1(a8, a9), hops1(a9, a10))
//    hops10_join(a1, a11) :- (hops1(a1, a2), hops1(a2, a3), hops1(a3, a4), hops1(a4, a5), hops1(a5, a6), hops1(a6, a7), hops1(a7, a8), hops1(a8, a9), hops1(a9, a10), hops1(a10, a11))


    for i <- 0 until 150 do
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
      compiled_e1.solvePreCompiled(compiled_e1.getCompiled(compiled_program1_tree, compiled_program1_ctx), compiled_program1_ctx)
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
