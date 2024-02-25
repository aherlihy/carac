package datalog.benchmarks

import datalog.benchmarks.DLBenchmark
import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.{NaiveExecutionEngine, NaiveStagedExecutionEngine, SemiNaiveExecutionEngine, StagedExecutionEngine, ExecutionEngine, ir}
import datalog.storage.{DefaultStorageManager, VolcanoStorageManager, StorageManager, StorageTerm}

import java.nio.file.{Files, Path, Paths}
import scala.collection.{immutable, mutable}
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
          val edbs = reader.lines()
            .map(l => {
              val factInput = l
                .split("\t")
                .zipWithIndex.map((s, i) =>
                (headers(i) match {
                  case "Int" => s.toInt
                  case "String" => s
                  case _ => throw new Exception(s"Unknown type ${headers(i)}")
                }).asInstanceOf[StorageTerm]
              ).toSeq
              if (factInput.size != headers.size)
                throw new Exception(s"Input data for fact of length ${factInput.size} but should be ${headers.mkString("[", ", ", "]")}. Line='$l'")
              factInput
            }).toScala(Seq)
          reader.close()
          inputFacts(edbName) = edbs
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
        val expected = reader.lines()
          .map(l => l.split("\t").zipWithIndex.map((s, i) =>
            (headers(i) match {
              case "Int" => s.toInt
              case "String" => s
              case _ => throw new Exception(s"Unknown type ${headers(i)}")
            }).asInstanceOf[StorageTerm]
          ).toSeq)
          .toScala(Set)
        expectedFacts(rule) = expected
        reader.close()
      })
  }
  // do this once, in the constructor
  loadEDBFiles()
  initAllEngines()
  programs.values.foreach(p => loadData(p))
//  programs.keys.foreach(println)
}
