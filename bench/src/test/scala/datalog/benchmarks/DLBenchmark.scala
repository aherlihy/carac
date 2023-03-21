package datalog.benchmarks

import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.*
import datalog.storage.*
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.quoted.staging

/**
 * Used for both hand-written benchmarks (ex: TransitiveClosure) to be run on all configs, and also auto-generated (ex: Examples)
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

  def initialize(context: String): Program = {
    val program = context match {
      case "SemiNaiveVolcano" =>               Program(SemiNaiveExecutionEngine(   VolcanoStorageManager()))
      case "NaiveVolcano" =>                   Program(NaiveExecutionEngine(       VolcanoStorageManager()))
      case "SemiNaiveDefault" =>              Program(SemiNaiveExecutionEngine(   DefaultStorageManager()))
      case "NaiveDefault" =>                  Program(NaiveExecutionEngine(       DefaultStorageManager()))
      case "NaiveCompiledStagedDefault" =>    Program(NaiveStagedExecutionEngine( DefaultStorageManager() ))
      case "NaiveInterpretedStagedDefault" => Program(NaiveStagedExecutionEngine( DefaultStorageManager(),  JITOptions(ir.OpCode.OTHER, dotty)))
      case "CompiledStagedDefault" =>         Program(StagedExecutionEngine(      DefaultStorageManager(), JITOptions(dotty = dotty)))
      case _ if context.contains("Interpreted") =>
        val preSA = if (context.contains("S1B")) 1 else if (context.contains("S1W")) -1 else 0
        val sA = if (context.contains("S2B")) 1 else if (context.contains("S2W")) -1 else 0
        val sO = if (context.contains("S3B")) 1 else if (context.contains("S3W")) -1 else 0
        Program(StagedExecutionEngine(DefaultStorageManager(), JITOptions(ir.OpCode.OTHER, dotty, false, sortOrder = (preSA, sA, sO))))
      case _ if context.contains("JIT") =>
        val preSA = if (context.contains("S1B")) 1 else if (context.contains("S1W")) -1 else 0
        val sA = if (context.contains("S2B")) 1 else if (context.contains("S2W")) -1 else 0
        val sO = if (context.contains("S3B")) 1 else if (context.contains("S3W")) -1 else 0
        val aot = context.contains("AOT")
        val nonblocking = context.contains("Async")
        val label =
          if (context.contains("NaiveEval")) ir.OpCode.EVAL_NAIVE
          else if (context.contains("SemiNaiveEval")) ir.OpCode.EVAL_SN
          else if (context.contains("LoopBody")) ir.OpCode.LOOP_BODY
          else if (context.contains("Loop")) ir.OpCode.DOWHILE
          else if (context.contains("Program")) ir.OpCode.PROGRAM
          else if (context.contains("FPJ")) ir.OpCode.SPJ
          else if (context.contains("EvalRule")) ir.OpCode.EVAL_RULE_SN
          else if (context.contains("UnionSPJ")) ir.OpCode.EVAL_RULE_BODY
          else throw new Exception(s"Unknown type of JIT staged $context")
        val threshVR = "TV([0-9]*)".r
        val threshNR = "TN([0-9]*)".r
        val thresholdN = context match {
          case threshNR(t) => t.toInt
          case _ => 1
        }
        val thresholdV = context match {
          case threshVR(t) => t.toInt // TODO: handle floats
          case _ => 0
        }
        if (context.contains("Snippet"))
          Program(StagedSnippetExecutionEngine(
            DefaultStorageManager(),
            JITOptions(label, dotty, aot, !nonblocking, thresholdN, thresholdV, sortOrder = (preSA, sA, sO))))
        else
          Program(StagedExecutionEngine(
            DefaultStorageManager(),
            JITOptions(label, dotty, aot, !nonblocking, thresholdN, thresholdV, sortOrder = (preSA, sA, sO))))
      case _ => // WARNING: MUnit just returns null pointers everywhere if an error or assert is triggered in beforeEach
        throw new Exception(s"Unknown engine construction ${context}") // TODO: this is reported as passing
    }
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
