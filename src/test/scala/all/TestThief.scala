package all
import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.storage.{CollectionsStorageManager, IndexedCollStorageManager, RelationalStorageManager}
import graphs.{EDBFromFile, TestGraph}

import java.nio.file.*
//import java.io.File

class TestThief extends munit.FunSuite {
  val partialDir = Paths.get("src", "test", "scala", "graphs", "fromFile", "partial")
  val graph = new Fixture[TestGraph]("Current tests") {
    var graph: TestGraph = null
    var program: Program = null
    def apply() = graph

    override def beforeEach(context: BeforeEach): Unit = {
      println("in beforeEach, arg=" + context.test.name)
      program = new Program(new SemiNaiveExecutionEngine(new CollectionsStorageManager()))
      graph = EDBFromFile(program, Paths.get(partialDir.toString, context.test.name))
    }
    override def afterEach(context: AfterEach): Unit = {
      // Always gets called, even if test failed.
      println("in afterEach, arg=" + context.test.name)
    }
  }
  override def munitFixtures = List(graph)

//  test("exists") {
//     `file` is the temporary file that was created for this test case.
//    assert(Files.exists(file()))
//  }

  partialDir.toFile
    .listFiles
    .filter(_.isDirectory)
    .map(_.getName)
    .map(testdir => {
      //      test(testdir) {
      //        println("in test, graph()=" + graph())
      //        assert(true)
      //      }

      test(testdir) {
        val g = graph()
        println("in test, graph=" + g)
        g.queries.map((hint, query) => {
          println("RUNNING QUERY " + g.description + "." + query.description)
          assertEquals(
            query.relation.solve(),
            query.solution,
            hint
          )
        })
      }
    })
}
