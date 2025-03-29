package carac.benchmarks

import carac.benchmarks.DLBenchmark
import carac.dsl.{Constant, Program, Relation, Term}
import carac.execution.{NaiveShallowExecutionEngine, NaiveStagedExecutionEngine, ShallowExecutionEngine, StagedExecutionEngine, ExecutionEngine, ir}
import carac.storage.{StorageManager, StorageTerm}

import java.nio.file.{Files, Path, Paths}
import scala.collection.{immutable, mutable}
import immutable.ArraySeq
import scala.io.Source
import scala.jdk.StreamConverters.*
import scala.util.Properties
import scala.quoted.*

abstract class ExampleBenchmarkGenerator(testname: String,
                                         val skip: Set[String] = Set(),
                                         override val tags: Set[String] = Set()) extends BenchmarkGenerator(
  Paths.get("..", "src", "test", "scala", "test", "examples", testname),
  skip, tags
)

/**
 * Benchmarks that have a fact directory that needs to be pre-loaded into the DB
 *
 * @param directory
 * @param skip
 * @param tags
 */
abstract class BenchmarkGenerator(val directory: Path,
                             skip: Set[String] = Set(),
                             val tags: Set[String] = Set()) extends DLBenchmark {
  def pretest(program: Program): Unit
  val description: String = directory.getFileName.toString

  // import EDBs and IDBs, do this once to avoid slow test initialization
  def loadEDBFiles(): Unit = {
    val factdir = Paths.get(directory.toString, "facts")
    if (Files.exists(factdir))
      Files.walk(factdir, 1)
        .filter(p => Files.isRegularFile(p))
        .forEach(f => {
          val edbName = f.getFileName.toString.replaceFirst("[.][^.]+$", "")
          val reader = Files.newBufferedReader(f)
          val headers = reader.readLine().split("\t")
          val allInts = headers.forall(_ == "Int")
          val edbsBuilder = Seq.newBuilder[Seq[StorageTerm]]
          reader.lines()
            .forEach(l =>
              val splittedInput = l.split("\t")
              val factInput = {
                if (allInts) {
                  ArraySeq.ofInt(splittedInput.map(_.toInt))
                } else {
                  splittedInput.zipWithIndex.map((s, i) =>
                    (headers(i) match {
                      case "Int" => s.toInt
                      case "String" => s
                      case _ => throw new Exception(s"Unknown type ${headers(i)}")
                    }).asInstanceOf[StorageTerm]
                  ).toSeq
                }
              }
              if (factInput.size != headers.size)
                throw new Exception(s"Input data for fact of length ${factInput.size} but should be ${headers.mkString("[", ", ", "]")}. Line='$l'")
              edbsBuilder += factInput
            )
          reader.close()
          inputFacts(edbName) = edbsBuilder.result()
        })
    // Generate expected
    val expDir = Paths.get(directory.toString, "expected")
    if (!Files.exists(expDir)) throw new Exception(s"Missing expected directory '$expDir'")
    Files.walk(expDir, 1)
      .filter(p => Files.isRegularFile(p) && p.toString.endsWith(".csv"))
      .forEach(f => {
        val rule = f.getFileName.toString.replaceFirst("[.][^.]+$", "")
        val reader = Files.newBufferedReader(f)
        val headers = reader.readLine().split("\t")
        val allInts = headers.forall(_ == "Int")
        val expectedBuilder = Set.newBuilder[Seq[StorageTerm]]
        reader.lines()
          .forEach(l =>
            val splittedInput = l.split("\t")
            expectedBuilder += {
              if (allInts) {
                ArraySeq.ofInt(splittedInput.map(_.toInt))
              } else {
                splittedInput.zipWithIndex.map((s, i) =>
                  (headers(i) match {
                    case "Int" => s.toInt
                    case "String" => s
                    case _ => throw new Exception(s"Unknown type ${headers(i)}")
                  }).asInstanceOf[StorageTerm]
                ).toSeq
              }
            }
          )
        expectedFacts(rule) = expectedBuilder.result()
        reader.close()
      })
  }
  // do this once, in the constructor
  loadEDBFiles()
  initAllEngines()
  programs.values.foreach(p => loadData(p))
//  programs.keys.foreach(println)
}
