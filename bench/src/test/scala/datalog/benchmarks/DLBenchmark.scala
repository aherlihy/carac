package datalog.benchmarks

import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.*
import datalog.storage.*

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
      case "NaiveStagedCollections" => Program(NaiveStagedExecutionEngine(CollectionsStorageManager()))
      case "SemiNaiveStagedCollections" => Program(SemiNaiveStagedExecutionEngine(CollectionsStorageManager()))
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
