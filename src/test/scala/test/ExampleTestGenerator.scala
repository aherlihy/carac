package test

import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.{NaiveExecutionEngine, SemiNaiveExecutionEngine, NaiveStagedExecutionEngine, SemiNaiveStagedExecutionEngine}
import datalog.storage.{CollectionsStorageManager, RelationalStorageManager}

import java.nio.file.{Files, Path, Paths}
import scala.collection.mutable
import scala.io.Source
import scala.jdk.StreamConverters.*
import scala.util.Properties
//import scala.quoted.*
//import scala.quoted.staging.*

abstract class ExampleTestGenerator(testname: String,
                                    skip: Set[String] = Set(),
                                    tags: Set[String] = Set()) extends TestGenerator(
  Paths.get("src", "test", "scala", "test", "examples", testname), skip, tags
)

abstract class TestGenerator(directory: Path,
                             skip: Set[String] = Set(),
                             val tags: Set[String] = Set()) extends munit.FunSuite {
  def pretest(program: Program): Unit
  val mTags = tags.map(t => new munit.Tag(t))
  val toSolve: String = "_"

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
//    // define IDBs
//    val classz = this.getClass.getClassLoader.loadClass(s"examples.$description")
//    val constr = classz.getConstructor()
//    val idbProgram = constr.newInstance().asInstanceOf[TestIDB]
//
//    idbProgram.run(program)

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

    val program: Fixture[Program] = new Fixture[Program]("Programs") {
      var program: Program = null
      def apply(): Program = program

      override def beforeEach(context: BeforeEach): Unit = {
        program = context.test.name match {
          case "SemiNaiveRelational" => Program(SemiNaiveExecutionEngine(RelationalStorageManager()))
          case "NaiveRelational" => Program(NaiveExecutionEngine(RelationalStorageManager()))
          case "SemiNaiveCollections" => Program(SemiNaiveExecutionEngine(CollectionsStorageManager()))
          case "NaiveCollections" => Program(NaiveExecutionEngine(CollectionsStorageManager()))
          case "NaiveStagedCollections" => Program(NaiveStagedExecutionEngine(CollectionsStorageManager()))
          case "SemiNaiveStagedCollections" => Program(SemiNaiveStagedExecutionEngine(CollectionsStorageManager()))
          case _ => // WARNING: MUnit just returns null pointers everywhere if an error or assert is triggered in beforeEach
            throw new Exception(s"Unknown engine construction ${context.test.name}") // TODO: this is reported as passing
        }
        inputFacts.foreach((edbName, factInput) =>
          val fact = program.relation[Constant](edbName)
          factInput.foreach(f => fact(f: _*) :- ())
          if (factInput.size == 0) {
            val edbs = program.ee.storageManager.edbs.asInstanceOf[mutable.Map[Int, Any]]
          }
        )
        pretest(program)
      }
    }

    override def munitFixtures = List(program)

    Seq("SemiNaive", "Naive", "NaiveStaged", "SemiNaiveStaged").foreach(execution => {
      Seq("Relational", "Collections").foreach(storage => {
        if (
            skip.contains(execution) || skip.contains(storage) ||
              (tags ++ Set(execution, storage)).flatMap(t => Properties.envOrNone(t.toUpperCase())).nonEmpty ||// manually implement --exclude for intellij
              (execution.contains("Staged") && storage == "Relational")
        ) {
            test(s"$execution$storage".ignore) {}
          } else {
            val tags = mTags ++ Set(new munit.Tag(execution), new munit.Tag(storage))
            test(s"$execution$storage".withTags(tags)) {
              val p = program()
              if (toSolve != "_") { // solve for one relation, check all expected
                assertEquals(
                  p.namedRelation(toSolve).solve(),
                  expectedFacts(toSolve)
                )
                expectedFacts.foreach((fact, expected) => {
                  assertEquals(
                    p.namedRelation(fact).get(),
                    expected,
                    s"$fact did not match expected results"
                  )
                })
              } else { // solve all relations for their expected
                expectedFacts.foreach((fact, expected) => {
                  assertEquals(
                    p.namedRelation(fact).solve(),
                    expected,
                    s"$fact did not match expected results"
                  )
                })
              }
            }
          }
      })
    })
}
