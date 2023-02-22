package datalog.benchmarks

import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.*
import datalog.storage.*
import datalog.tools.Debug.debug

import scala.collection.mutable

/**
 * Used for both hand-written benchmarks (ex: TransitiveClosure) to be run on all configs, and also auto-generated (ex: Examples)
 */
abstract class DLBenchmark {
  def pretest(program: Program): Unit
  val toSolve: String
  val description: String
  val inputFacts: mutable.Map[String, Seq[Seq[Term]]] = mutable.Map()
  val expectedFacts: mutable.Map[String, Set[Seq[Term]]] = mutable.Map()
  var result: mutable.Map[String, Set[Seq[Term]]] = mutable.Map()
  var programs: mutable.Map[String, Program] = mutable.Map()

  def initialize(context: String): Program = {
    val program = context match {
      case "SemiNaiveRelational" =>               Program(SemiNaiveExecutionEngine(   RelationalStorageManager()))
      case "NaiveRelational" =>                   Program(NaiveExecutionEngine(       RelationalStorageManager()))
      case "SemiNaiveCollections" =>              Program(SemiNaiveExecutionEngine(   CollectionsStorageManager()))
      case "SemiNaiveCollectionsBest" =>          Program(SemiNaiveExecutionEngine(   CollectionsStorageManager(sort = 1)))
      case "SemiNaiveCollectionsWorst" =>         Program(SemiNaiveExecutionEngine(   CollectionsStorageManager(sort = -1)))
      case "NaiveCollections" =>                  Program(NaiveExecutionEngine(       CollectionsStorageManager()))
      case "NaiveCompiledStagedCollections" =>    Program(NaiveStagedExecutionEngine( CollectionsStorageManager()))
      case "NaiveInterpretedStagedCollections" => Program(NaiveStagedExecutionEngine( CollectionsStorageManager(),  JITOptions(granularity = ir.OpCode.OTHER)))
      case "InterpretedStagedCollections" =>      Program(StagedExecutionEngine(      CollectionsStorageManager(),  JITOptions(granularity = ir.OpCode.OTHER)))
      case "CompiledStagedCollections" =>         Program(StagedExecutionEngine(      CollectionsStorageManager()))
      case _ if context.contains("JIT") =>
        val aot = context.contains("AOT")
        val nonblocking = context.contains("NonBlocking")
        val label =
          if (context.contains("NaiveEval")) ir.OpCode.EVAL_NAIVE
          else if (context.contains("SemiNaiveEval")) ir.OpCode.EVAL_SN
          else if (context.contains("LoopBody")) ir.OpCode.LOOP_BODY
          else if (context.contains("Loop")) ir.OpCode.DOWHILE
          else if (context.contains("Program")) ir.OpCode.PROGRAM
          else if (context.contains("Join")) ir.OpCode.SPJ
          else throw new Exception(s"Unknown type of JIT staged $context")
        if (context.contains("Snippet"))
          Program(StagedSnippetExecutionEngine(CollectionsStorageManager(), JITOptions(label, aot, !nonblocking)))
        else
          Program(StagedExecutionEngine(CollectionsStorageManager(), JITOptions(label, aot, !nonblocking)))
      case _ => // WARNING: MUnit just returns null pointers everywhere if an error or assert is triggered in beforeEach
        throw new Exception(s"Unknown engine construction ${context}") // TODO: this is reported as passing
    }
    inputFacts.foreach((edbName, factInput) =>
      val fact = program.relation[Constant](edbName)
        factInput.foreach(f => fact(f: _*) :- ())
      if (factInput.size == 0) {
        val edbs = program.ee.storageManager.edbs.asInstanceOf[mutable.Map[Int, Any]]
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
