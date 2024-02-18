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
      "default" -> (() => new DefaultStorageManager()),
      "indexed" -> (() => new IndexedStorageManager())
    )
    val shallowAlgo = immutable.Map[String, StorageManager => ExecutionEngine](
      "seminaive" -> (sm => new SemiNaiveExecutionEngine(sm)),
      "naive" -> (sm => new NaiveExecutionEngine(sm))
    )
    // ---> uncomment to bench shallow embedding
    shallowAlgo.keys.foreach(ee =>
      storageEngines.keys.foreach(sm =>
        programs(s"shallow_${sm}_${ee}_____") = Program(shallowAlgo(ee)(storageEngines(sm)()))
      )
    )
    val backends = Seq(Backend.Quotes, Backend.Bytecode, Backend.Lambda)
    // --> uncomment to bench compile
    backends.foreach(bc =>
      val jo = JITOptions(
        mode = Mode.Compiled,
        backend = bc,
        dotty = dotty
      )
      programs(jo.toBenchmark) = Program(StagedExecutionEngine(DefaultStorageManager(), jo))
    )

    val jitSortOpts = Seq(SortOrder.Unordered, SortOrder.Sel)
    val interpSortOpts = Seq(SortOrder.Unordered, SortOrder.Sel, SortOrder.Badluck)
    val fuzzySortOpts = Seq(0)
    val onlineSortOpts = Seq(false) // add 1 to bench online sort
    val blocking = Seq(CompileSync.Blocking, CompileSync.Async)

  // --> uncomment for interpreted
    interpSortOpts.foreach(sort =>
        onlineSortOpts.foreach(onlineSort =>
          storageEngines.filter((k, v) => k != "volcano").foreach((k, storage) =>
            val jo = JITOptions(
              mode = Mode.Interpreted,
  //            granularity = if (sort == SortOrder.Sel) Granularity.RULE else Granularity.NEVER, // TODO: eventually sort at other grans
              sortOrder = sort,
              onlineSort = onlineSort,
              dotty = dotty,
              storage = k
            )
            programs(jo.toBenchmark) = Program(StagedExecutionEngine(storage(), jo))
          )))

    // JIT options
    val jitGranularities = Seq(
      Granularity.ALL,
      Granularity.RULE,
      Granularity.DELTA
    )

  // --> uncomment for JIT
    jitGranularities.foreach(gran =>
      jitSortOpts.foreach(sort =>
        onlineSortOpts.foreach(onlineSort =>
          fuzzySortOpts.foreach(fuzzy =>
            blocking.foreach(block =>
              backends.foreach(bc =>
                val jo = JITOptions(
                  mode = Mode.JIT,
                  granularity = gran,
                  compileSync = block,
                  sortOrder = sort,
                  onlineSort = onlineSort,
                  backend = bc,
                  fuzzy = fuzzy,
                  dotty = dotty,
                )
                programs(jo.toBenchmark) = Program(
                  StagedExecutionEngine(DefaultStorageManager(), jo)
                )
              )
            )
          )
        )
      )
    )
//    println(s"programs: ${programs.keys}")
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
