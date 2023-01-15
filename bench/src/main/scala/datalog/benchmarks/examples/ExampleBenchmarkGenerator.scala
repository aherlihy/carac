package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.{NaiveExecutionEngine, NaiveStagedExecutionEngine, SemiNaiveExecutionEngine, SemiNaiveStagedExecutionEngine}
import datalog.storage.{CollectionsStorageManager, RelationalStorageManager}

import java.nio.file.{Files, Path, Paths}
import scala.collection.mutable
import scala.io.Source
import scala.jdk.StreamConverters.*
import scala.util.Properties

abstract class ExampleBenchmarkGenerator(testname: String,
                                         val skip: Set[String] = Set(),
                                         override val tags: Set[String] = Set()) extends BenchmarkGenerator(
  Paths.get("..", "src", "test", "scala", "test", "examples", testname),
  skip, tags
)

abstract class BenchmarkGenerator(directory: Path,
                             skip: Set[String] = Set(),
                             val tags: Set[String] = Set()) {
  def pretest(program: Program): Unit
  def toSolve: String = "_"
  val description: String = directory.getFileName.toString
  val inputFacts: mutable.Map[String, Seq[Seq[Term]]] = mutable.Map()
  val expectedFacts: mutable.Map[String, Set[Seq[Term]]] = mutable.Map()

  // import EDBs and IDBs
  private val factdir = Paths.get(directory.toString, "facts")
  if (Files.exists(factdir))
    Files.walk(factdir, 1)
      .filter(p => Files.isRegularFile(p))
      .forEach(f => {
        val edbName = f.getFileName.toString.replaceFirst("[.][^.]+$", "")
        val reader = Files.newBufferedReader(f)
        val headers = reader.readLine().split("\t")
        val edbs = reader.lines()
          .map(l => {
            val factInput = l
              .split("\t")
              .zipWithIndex.map((s, i) =>
                (headers(i) match {
                  case "Int" => s.toInt
                  case "String" => s
                  case _ => throw new Error(s"Unknown type ${headers(i)}")
                }).asInstanceOf[Term]
              ).toSeq
            if (factInput.length != headers.size)
              throw new Error(s"Input data for fact of length ${factInput.length} but should be ${headers.mkString("[", ", ", "]")}. Line='$l'")
            factInput
          }).toScala(Seq)
        reader.close()
        inputFacts(edbName) = edbs
      })
  // Generate expected
  private val expDir = Paths.get(directory.toString, "expected")
  if (!Files.exists(expDir)) throw new Exception(s"Missing expected directory '$expDir'")
  Files.walk(expDir, 1)
    .filter(p => Files.isRegularFile(p) && p.toString.endsWith(".csv"))
    .forEach(f => {
      val rule = f.getFileName.toString.replaceFirst("[.][^.]+$", "")
      val reader = Files.newBufferedReader(f)
      val headers = reader.readLine().split("\t")
      val expected = reader.lines()
        .map(l => l.split("\t").zipWithIndex.map((s, i) =>
          (headers(i) match {
            case "Int" => s.toInt
            case "String" => s
            case _ => throw new Error(s"Unknown type ${headers(i)}")
          }).asInstanceOf[Term]
        ).toSeq)
        .toScala(Set)
      expectedFacts(rule) = expected
      reader.close()
    })

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

  def run(program: Program, result: mutable.Map[String, Set[Seq[Term]]]): Unit = {
    if (toSolve != "_") { // solve for one relation, check all expected
      result(toSolve) = program.namedRelation(toSolve).solve()
    } else { // solve all relations for their expected
      expectedFacts.foreach((fact, expected) => {
        result(fact) = program.namedRelation(fact).solve()
      })
    }
  }

  var result: mutable.Map[String, Set[Seq[Term]]] = mutable.Map()
  var programs: mutable.Map[String, Program] = mutable.Map()

  Seq("SemiNaive", "Naive", "NaiveStaged", "SemiNaiveStaged").foreach(execution =>
    Seq("Relational", "Collections").foreach(storage =>
      if (
        (execution.contains("Staged") && storage == "Relational") ||
          skip.contains(execution) || skip.contains(storage) ||
          (tags ++ Set(execution, storage)).flatMap(t => Properties.envOrNone(t.toUpperCase())).nonEmpty
      ) {}
      else {
        programs(s"$execution$storage") = initialize(s"$execution$storage")
      }))

  def setup(): Unit = result.clear()
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
