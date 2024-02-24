package test

import datalog.dsl.{Constant, Program, Relation, Term}
import datalog.execution.{Backend, CompileSync, Granularity, JITOptions, Mode, NaiveExecutionEngine, NaiveStagedExecutionEngine, SemiNaiveExecutionEngine, SortOrder, StagedExecutionEngine, ir}
import datalog.storage.{DefaultStorageManager, IndexedStorageManager, StorageTerm, VolcanoStorageManager}

import java.nio.file.{Files, Path, Paths}
import scala.collection.mutable
import scala.io.Source
import scala.jdk.StreamConverters.*
import scala.quoted.staging
import scala.util.{Properties, Using}
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
  val dotty = staging.Compiler.make(getClass.getClassLoader)
  def pretest(program: Program): Unit
  val mTags = tags.map(t => new munit.Tag(t))
  val toSolve: String

  val description: String = directory.getFileName.toString
  val inputFacts: mutable.Map[String, Seq[Seq[StorageTerm]]] = mutable.Map()
  val expectedFacts: mutable.Map[String, Set[Seq[StorageTerm]]] = mutable.Map()

  def generateExpectedFiles(value: Set[Seq[Term]], filename: String): Unit = {
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
        program = context.test.name match {
          case "SemiNaiveVolcano" => Program(SemiNaiveExecutionEngine(VolcanoStorageManager()))
          case "NaiveVolcano" => Program(NaiveExecutionEngine(VolcanoStorageManager()))
          case "SemiNaiveDefault" => Program(SemiNaiveExecutionEngine(DefaultStorageManager()))
          case "NaiveDefault" => Program(NaiveExecutionEngine(DefaultStorageManager()))
          case "NaiveCompiledStagedDefault" =>
            Program(NaiveStagedExecutionEngine(DefaultStorageManager(), JITOptions(mode = Mode.Compiled)))
//          case "CompiledStagedDefault" =>
//            Program(StagedExecutionEngine(DefaultStorageManager(), JITOptions(mode = Mode.Compiled)))
          case "InterpretedStagedDefault" =>
            Program(StagedExecutionEngine(DefaultStorageManager(), JITOptions()))
          case "InterpretedStaged_selDefault" =>
            Program(StagedExecutionEngine(DefaultStorageManager(), JITOptions(sortOrder = SortOrder.Sel)))
          case "InterpretedStagedIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions()))
          case "InterpretedStaged_selIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(sortOrder = SortOrder.VariableR)))
//          case "InterpretedStaged_badluckDefault" =>
//            Program(StagedExecutionEngine(DefaultStorageManager(), JITOptions(sortOrder = SortOrder.Badluck)))

//           blocking
          case "JITStaged_Sel_RULE_Block_QuotesIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.RULE, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Quotes)))
          case "JITStaged_Sel_ALL_Block_QuotesIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.ALL, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Quotes)))
          case "JITStaged_Sel_DELTA_Block_QuotesIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Quotes)))
          case "JITStaged_Sel_RULE_Block_BCIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.RULE, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Bytecode)))
          case "JITStaged_Sel_ALL_Block_BCIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.ALL, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Bytecode)))
          case "JITStaged_Sel_DELTA_Block_BCIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Bytecode)))
          case "JITStaged_Sel_RULE_Block_LambdaIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.RULE, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)))
          case "JITStaged_Sel_ALL_Block_LambdaIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.ALL, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)))
          case "JITStaged_Sel_DELTA_Block_LambdaIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)))


          // async
          case "JITStaged_Sel_RULE_Async_QuotesIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.RULE, dotty = dotty, compileSync = CompileSync.Async, sortOrder = SortOrder.Sel, backend = Backend.Quotes)))
          case "JITStaged_Sel_ALL_Async_QuotesIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.ALL, dotty = dotty, compileSync = CompileSync.Async, sortOrder = SortOrder.Sel, backend = Backend.Quotes)))
          case "JITStaged_Sel_RULE_Async_BCIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.RULE, dotty = dotty, compileSync = CompileSync.Async, sortOrder = SortOrder.Sel, backend = Backend.Bytecode)))
          case "JITStaged_Sel_ALL_Async_BCIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.ALL, dotty = dotty, compileSync = CompileSync.Async, sortOrder = SortOrder.Sel, backend = Backend.Bytecode)))
          case "JITStaged_Sel_RULE_Async_LambdaIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.RULE, dotty = dotty, compileSync = CompileSync.Async, sortOrder = SortOrder.Sel, backend = Backend.Lambda)))
          case "JITStaged_Sel_ALL_Async_LambdaIndexed" =>
            Program(StagedExecutionEngine(IndexedStorageManager(), JITOptions(mode = Mode.JIT, granularity = Granularity.ALL, dotty = dotty, compileSync = CompileSync.Async, sortOrder = SortOrder.Sel, backend = Backend.Lambda)))

          case _ => // WARNING: MUnit just returns null pointers everywhere if an error or assert is triggered in beforeEach
            throw new Exception(s"Unknown engine construction ${context.test.name}") // TODO: this is reported as passing
        }
        inputFacts.foreach((edbName, factInput) =>
          val fact = program.relation[Constant](edbName)
          factInput.foreach(f => fact(f: _*) :- ())
          if (factInput.isEmpty) {
            val edbs = program.ee.storageManager.getAllEDBS()
          }
        )
        pretest(program)
      }
    }

    override def munitFixtures = List(program)

    Seq(
//      "Naive",
//      "SemiNaive",
//      "CompiledStaged", // TODO: for longer tests, can throw MethodTooLarge
//      "InterpretedStaged",
      "InterpretedStaged_sel",
//      "JITStaged_Sel_DELTA_Block_Lambda",
//      "JITStaged_Sel_DELTA_Block_BC",
//      "JITStaged_Sel_DELTA_Block_Quotes",
//      "JITStaged_Sel_ALL_Block_BC",
//      "JITStaged_Sel_RULE_Block_BC",
//      "JITStaged_Sel_RULE_Block_Quotes",
//      "JITStaged_Sel_ALL_Block_Quotes",
//      "JITStaged_Sel_RULE_Async_Quotes",
//      "JITStaged_Sel_ALL_Async_Quotes",
//      "JITStaged_Sel_ALL_Block_Lambda",
//      "JITStaged_Sel_RULE_Block_Lambda",
//      "JITStaged_Sel_ALL_Async_Lambda",
//      "JITStaged_Sel_RULE_Async_Lambda",
//      "JITStaged_Sel_RULE_Async_BC",
//      "JITStaged_Sel_ALL_Async_BC",
    ).foreach(execution => {
      Seq(/*"Volcano", "Default",*/ "Indexed").foreach(storage => {
        if ((execution.contains("Staged") || execution.contains("BytecodeGenerated") || execution.contains("Lambda")) && storage == "Volcano") {} // skip and don't report as skipped
        else if (
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
