package test

import datalog.dsl.{Constant, Program}
import datalog.execution.{Backend, CompileSync, Granularity, JITOptions, Mode, NaiveShallowExecutionEngine, NaiveStagedExecutionEngine, ShallowExecutionEngine, SortOrder, StagedExecutionEngine, ir}
import datalog.storage.{CollectionsStorageManager, IndexedStorageManager, StorageTerm, DuckDBStorageManager}

import java.nio.file.{Files, Path, Paths}
import scala.collection.mutable
import scala.jdk.StreamConverters.*
import scala.quoted.staging
import scala.util.{Properties, Using}

abstract class ExampleTestGenerator(testname: String,
                                    skip: Set[String] = Set(),
                                    tags: Set[String] = Set()) extends TestGenerator(
  Paths.get("src", "test", "scala", "test", "examples", testname), skip, tags
)

abstract class TestGenerator(directory: Path,
                             skip: Set[String] = Set(),
                             val tags: Set[String] = Set()) extends munit.FunSuite {
  val dotty = staging.Compiler.make(getClass.getClassLoader)
  def pretest(program: Program): Unit
  val mTags = tags.map(t => new munit.Tag(t))
  val toSolve: String

  val description: String = directory.getFileName.toString
  val inputFacts: mutable.Map[String, Seq[Seq[StorageTerm]]] = mutable.Map()
  val expectedFacts: mutable.Map[String, Set[Seq[StorageTerm]]] = mutable.Map()

  def generateExpectedFiles(value: Set[Seq[StorageTerm]], filename: String): Unit = {
    val path = Paths.get(directory.toString, "expected", filename + ".csv")
    println(s"writing ${value.size} facts to $path")
    Using(Files.newBufferedWriter(path)) { writer =>
      writer.write(value.head.map(v => "String").mkString("", "\t", "\n"))
      value.foreach(f => writer.write(f.mkString("", "\t", "\n")))
    }
  }

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
                  case _ => throw new Exception(s"Unknown type ${headers(i)}")
                }).asInstanceOf[StorageTerm]
              ).toSeq
            if (factInput.length != headers.size)
              throw new Exception(s"Input data for fact of length ${factInput.size} but should be ${headers.mkString("[", ", ", "]")}. Line='$l'")
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
            case _ => throw new Exception(s"Unknown type ${headers(i)} in file ${f.getFileName}")
          }).asInstanceOf[StorageTerm]
        ).toSeq)
        .toScala(Set)
      expectedFacts(rule) = expected
      reader.close()
    })

    val program: Fixture[Program] = new Fixture[Program]("Programs") {
      var program: Program = null
      def apply(): Program = program

      override def beforeEach(context: BeforeEach): Unit = {
        val storageManager = if context.test.name.contains("Indexed") then
          IndexedStorageManager()
        else if context.test.name.contains("Collections") then
          CollectionsStorageManager()
        else if context.test.name.contains("DuckDB") then
          DuckDBStorageManager()
        else throw new Exception(s"Unknown storage manager for ${context.test.name}")

        val executionEngine = if context.test.name.contains("Shallow") then
          if context.test.name.contains("Naive") then
            NaiveShallowExecutionEngine(storageManager)
          else
            ShallowExecutionEngine(storageManager)
        else if context.test.name.contains("Staged") then
          val mode = if context.test.name.contains("Compiled") then
            Mode.Compiled
          else if context.test.name.contains("Interpreted") then
            Mode.Interpreted
          else if context.test.name.contains("JIT") then
            Mode.JIT
          else throw new Exception(s"Unknown mode for ${context.test.name}")

          val sortOrder = if context.test.name.contains("sel") then SortOrder.Sel else SortOrder.Unordered
          val compileSync = if context.test.name.contains("Async") then CompileSync.Async else CompileSync.Blocking
          val backend = if context.test.name.contains("Quotes") || context.test.name.contains("Interpreted") then
            Backend.Quotes
          else if context.test.name.contains("Lambda") then
            Backend.Lambda
          else if context.test.name.contains("BC") then
            Backend.Bytecode
          else
            throw new Exception(s"Unknown backend for ${context.test.name}")

          val granularity = if context.test.name.contains("RULE") then
            Granularity.RULE
          else if context.test.name.contains("ALL") then
            Granularity.ALL
          else if context.test.name.contains("DELTA") then
            Granularity.DELTA
          else if context.test.name.contains("Interpreted") || context.test.name.contains("Compiled") then
            Granularity.NEVER
          else throw new Exception(s"Unknown granularity for ${context.test.name}")

          val jitOptions = JITOptions(mode = mode, granularity = granularity, dotty = dotty, compileSync = compileSync, sortOrder = sortOrder, backend = backend)

          if context.test.name.contains("Naive") then
            NaiveStagedExecutionEngine(storageManager, jitOptions)
          else
            StagedExecutionEngine(storageManager, jitOptions)
        else throw new Exception(s"Unknown execution engine for ${context.test.name}")

        program = Program(executionEngine)

        inputFacts.foreach((edbName, factInput) =>
          val fact = program.relation[Constant](edbName)
          factInput.foreach(f => fact(f*) :- ())
          if (factInput.isEmpty) {
            val edbs = program.ee.storageManager.getAllEDBS()
          }
        )
        pretest(program)
      }
    }

    override def munitFixtures = List(program)

    Seq(
      "NaiveShallow",
      "SemiNaiveShallow",
      "CompiledStaged_Lambda",
      "CompiledStaged_BC",
      "CompiledStaged_Quotes",
      "InterpretedStaged",
      "NaiveInterpretedStaged",
      "InterpretedStaged_sel",
      "JITStaged_Sel_DELTA_Block_Lambda",
      "JITStaged_Sel_DELTA_Block_BC",
      "JITStaged_Sel_DELTA_Block_Quotes",
      "JITStaged_Sel_ALL_Block_BC",
      "JITStaged_Sel_RULE_Block_BC",
      "JITStaged_Sel_RULE_Block_Quotes",
      "JITStaged_Sel_ALL_Block_Quotes",
      "JITStaged_Sel_RULE_Async_Quotes",
      "JITStaged_Sel_ALL_Async_Quotes",
      "JITStaged_Sel_ALL_Block_Lambda",
      "JITStaged_Sel_RULE_Block_Lambda",
      "JITStaged_Sel_ALL_Async_Lambda",
      "JITStaged_Sel_RULE_Async_Lambda",
      "JITStaged_Sel_RULE_Async_BC",
      "JITStaged_Sel_ALL_Async_BC",
    ).foreach(execution => {
      Seq("Indexed", /*"Collections",*/ "DuckDB").foreach(storage => {
        if (
            skip.contains(execution) || skip.contains(storage) ||
              (tags ++ Set(execution, storage)).flatMap(t => Properties.envOrNone(t.toUpperCase())).nonEmpty// manually implement --exclude for intellij
        ) {
            test(s"$execution$storage".ignore) {}
          } else {
            val tags = mTags ++ Set(new munit.Tag(execution), new munit.Tag(storage))
            test(s"$execution$storage".withTags(tags)) {
              val p = program()
              if (toSolve != "_") { // solve for one relation, check all expected
                // =========> uncomment to generate expected files
//                p.namedRelation(toSolve).solve()
//                (p.ee.storageManager.getAllEDBS().keys ++ p.ee.precedenceGraph.idbs).foreach(rId =>
//                  val name = p.ee.storageManager.ns(rId)
//                  generateExpectedFiles(p.namedRelation(name).get(), name)
//                )
                // <=========
                assertEquals(
                  p.namedRelation(toSolve).solve(),
                  expectedFacts(toSolve),
                  s"$toSolve did not match expected results"
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
