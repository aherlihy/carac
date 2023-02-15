package datalog.benchmarks

import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.*
import datalog.storage.*
import datalog.tools.Debug.debug

import scala.collection.mutable

abstract class DLBenchmark {
  def pretest(program: Program): Unit
  def toSolve: String = "_"
  val description: String
  val inputFacts: mutable.Map[String, Seq[Seq[Term]]] = mutable.Map()
  val expectedFacts: mutable.Map[String, Set[Seq[Term]]] = mutable.Map()
  var result: mutable.Map[String, Set[Seq[Term]]] = mutable.Map()
  var programs: mutable.Map[String, Program] = mutable.Map()

  def initialize(context: String): Program = {
    val program = context match {
      case "SemiNaiveRelational" => Program(SemiNaiveExecutionEngine(RelationalStorageManager()))
      case "NaiveRelational" => Program(NaiveExecutionEngine(RelationalStorageManager()))
      case "SemiNaiveCollections" => Program(SemiNaiveExecutionEngine(CollectionsStorageManager()))
      case "NaiveCollections" => Program(NaiveExecutionEngine(CollectionsStorageManager()))
      case "NaiveCompiledStagedCollections" => Program(NaiveCompiledStagedExecutionEngine(CollectionsStorageManager()))
      case "InterpretedStagedCollections" => Program(InterpretedStagedExecutionEngine(CollectionsStorageManager()))
      case "CompiledStagedCollections" => Program(CompiledStagedExecutionEngine(CollectionsStorageManager()))
      case _ if context.contains("JITStaged") =>
        val aot = context.contains("AOT")
        val nonblocking = context.contains("NonBlocking")
        val label =
          if (context.contains("NaiveEval")) ir.OpCode.EVAL_NAIVE
          else if (context.contains("SemiNaiveEval")) ir.OpCode.EVAL_SN
          else if (context.contains("LoopBody")) ir.OpCode.LOOP_BODY
          else if (context.contains("Loop")) ir.OpCode.LOOP
          else if (context.contains("Program")) ir.OpCode.PROGRAM
          else throw new Exception(s"Unknown type of JIT staged $context")
        Program(JITStagedExecutionEngine(CollectionsStorageManager(), label, aot, !nonblocking))
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
    if (toSolve != "_") { // solve for one relation, check all expected
      result(toSolve) = program.namedRelation(toSolve).solve()
    } else { // solve all relations for their expected
      expectedFacts.keys.foreach(relation => {
        result(relation) = program.namedRelation(relation).solve()
      })
    }
  }

  def finish(): Unit = {
    debug("in finish: ", () => programs.keys.mkString("[", ", ", "]"))
    programs.keys.filter(k => k.contains("JITStaged")).foreach(k =>
      programs(k).ee.asInstanceOf[JITStagedExecutionEngine].waitForAll()
    )
    assert(result.nonEmpty)
    if (toSolve != "_") { // solve for one relation, check all expected
      assert(result(toSolve) == expectedFacts(toSolve))
    } else { // solve all relations for their expected
      expectedFacts.foreach((fact, expected) => {
        assert(result(fact) == expectedFacts(fact))
      })
    }
  }
}
