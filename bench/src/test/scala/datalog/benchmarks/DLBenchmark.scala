package datalog.benchmarks

import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.*
import datalog.execution.ir.OpCode.PROGRAM
import datalog.storage.*
import datalog.tools.Debug.debug

import scala.collection.{immutable, mutable}
import scala.quoted.staging

/**
 * Used for both hand-written benchmarks (ex: TransitiveClosure) to be run on all configs, and also auto-generated (ex: Examples).
 */
abstract class DLBenchmark {
  val dotty = staging.Compiler.make(getClass.getClassLoader)
  def pretest(program: Program): Unit
  val toSolve: String
  val description: String
  val inputFacts: mutable.Map[String, Seq[Seq[Term]]] = mutable.Map()
  val expectedFacts: mutable.Map[String, Set[Seq[Term]]] = mutable.Map()
  var result: mutable.Map[String, Set[Seq[Term]]] = mutable.Map()
  var programs: mutable.Map[String, Program] = mutable.Map()

  /**
   * Load all dimensions of the benchmark matrix into `programs` once so can reuse between calls
   */
  def initAllEngines(): Unit = {
    // Non-Staged combinations
    val storageEngines = immutable.Map[String, () => StorageManager](
      "volcano" -> (() => new VolcanoStorageManager()),
      "default" -> (() => new DefaultStorageManager())
    )
    val executionEngines = immutable.Map[String, StorageManager => ExecutionEngine](
      "seminaive" -> (sm => new SemiNaiveExecutionEngine(sm)),
      "naive" -> (sm => new NaiveExecutionEngine(sm))
    )
    // ---> uncomment to bench shallow embedding
//    executionEngines.keys.foreach(ee =>
//      storageEngines.keys.foreach(sm =>
//        programs(s"${ee}_$sm") = Program(executionEngines(ee)(storageEngines(sm)()))
//      )
//    )
    // ---> uncomment to bench compiled unordered
//    programs("compiled_default_unordered") = Program(StagedExecutionEngine(DefaultStorageManager(), JITOptions(ir.OpCode.PROGRAM, dotty)))
    // Optimization modes
    def toS(s: Int*): String = s"${
      if (s.forall(_ == 0)) "unordered" else "" //if (s.forall(_ >= 0)) "best" else "worst"
    }${
      s(0).abs match
        case 0 => ""
        case 2 => "crd"
        case 1 => "sel"
        case 3 => "intmax"
        case 4 => "mixed"
        case _ => throw new Exception(s"Unknown sort order $s")
    }${
      s(2).abs match
        case 0 => ""
        case 1 => "Online"
        case _ => throw new Exception(s"Unknown sort order $s")
    }${
      s(1).abs match
        case 0 => ""
        case 1 => "_fuzzytwo"
        case 2 => "_fuzzyall"
        case _ => throw new Exception(s"Unknown sort order $s")
    }"

    val mainSortOpts = Seq(0) // add 1-4 to include sorted opts
    val fuzzySortOpts = Seq(0) // add 1, 2 to include fuzzy benchmarks
    val onlineSortOpts = Seq(0) // add 1 to bench online sort
    val sortCombos = mainSortOpts.flatMap(i1 => fuzzySortOpts.flatMap(i2 => onlineSortOpts.map(i3 => (i1, i2, i3))))

    println(s"sortCombos=$sortCombos")

  // --> uncomment for interpreted
    sortCombos.foreach(s =>
      programs(s"interpreted_default_${toS(s._1, s._2, s._3)}") = Program(StagedExecutionEngine(
        DefaultStorageManager(),
        JITOptions(ir.OpCode.OTHER, dotty, false, sortOrder = s)
      ))
      programs(s"interpreted_default_${toS(-s._1, -s._2, -s._3)}") = Program(StagedExecutionEngine(
        DefaultStorageManager(),
        JITOptions(ir.OpCode.OTHER, dotty, false, sortOrder = (-s._1, -s._2, -s._3))
      ))
    )

    // JIT options
    val jitGranularities = Seq(
      ir.OpCode.EVAL_RULE_BODY,
      ir.OpCode.EVAL_RULE_SN,
    )
    val blocking = Seq(true, false)

  // --> uncomment for JIT
//    jitGranularities.foreach(gran =>
//      sortCombos.foreach(s =>
//        blocking.foreach(block =>
//          val jo = JITOptions(granularity = gran, dotty = dotty, aot = !block, block = block, sortOrder = s)
//          programs(s"jit_default_${toS(s._1, s._2, s._3)}_${if (!block) "async" else "blocking"}_${gran.toString.replace("_", "")}") = Program(
//            StagedExecutionEngine(DefaultStorageManager(), jo)
//          )
//        )
//      )
//    )
    println(s"programs: ${programs.keys}")
  }


  /**
   * Separate out loading data in case benchmarks don't want everything in the programs matrix and prefer
   * to manually construct.
   * @param program
   * @return
   */
  def loadData(program: Program): Program = {
    inputFacts.foreach((edbName, factInput) =>
      val fact = program.relation[Constant](edbName)
        factInput.foreach(f => fact(f: _*) :- ())
      if (factInput.isEmpty) {
        val edbs = program.ee.storageManager.getAllEDBS()
      }
    )
    pretest(program)
    program
  }

  def setup(): Unit = {
    result.clear()
  }

  def run(program: Program, result: mutable.Map[String, Set[Seq[Term]]]): Unit = {
    expectedFacts.keys.foreach(relation => {
      result(relation) = program.namedRelation(relation).solve()
    })
  }

  def finish(): Unit = {
    debug("in finish: ", () => programs.keys.mkString("[", ", ", "]"))
    programs.keys.filter(k => k.contains("JITStaged")).foreach(k =>
      programs(k).ee.asInstanceOf[StagedExecutionEngine].waitForStragglers()
    )
    assert(result.nonEmpty)
    if (toSolve != "_") { // solve for one relation, check all expected
//      assert(result(toSolve) == expectedFacts(toSolve)) TODO: this is just input_output I think?
    } else { // solve all relations for their expected
      expectedFacts.foreach((fact, expected) => {
        assert(result(fact) == expectedFacts(fact))
      })
    }
  }
}
