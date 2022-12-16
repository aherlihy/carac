package all
import datalog.dsl.{Program, Relation}
import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.storage.{CollectionsStorageManager, IndexedCollStorageManager, RelationalStorageManager}
import graphs.{EDBFromFile, TestGraph}

import java.nio.file.*

abstract class TestThief(p: () => Program, t: String, engine: String = "SemiNaive", storage: String = "Collections") extends munit.FunSuite {
  private val srcDir = Paths.get("src", "test", "scala", "graphs", "fromFile", t)

  val graph: Fixture[TestGraph] = new Fixture[TestGraph]("Graph") {
    var graph: TestGraph = null
    var program: Program = null
    def apply(): TestGraph = graph

    override def beforeEach(context: BeforeEach): Unit = {
      program = p()
      graph = EDBFromFile(program, Paths.get(srcDir.toString, context.test.name))
    }
  }
  override def munitFixtures = List(graph)

    srcDir.toFile
    .listFiles
    .filter(_.isDirectory)
    .map(_.getName)
    .foreach(testdir => {
      test(testdir) {
        val g = graph()
        g.queries.map((hint, query) => {
//          assume(!query.skip.contains(engine), s"skipping engine $engine")
//          assume(!query.skip.contains(storage), s"skipping storage $storage")
          if (!query.skip.contains(engine) && !query.skip.contains(storage)) {
            val expected = query.solution
            val result = query.relation.solve()
            assertEquals(expected, result, s"relation '$hint' did not match'")
//            println(s"passed: $testdir.$hint.solve()") // get around munit lack of nesting
          } else {
            println(s"skipped: $testdir.$hint for config $engine$storage")
          }
        })
      }
    })
}

// better way to instantiate type w reflection?
class TT_PARTIAL_SemiNaive_Relational extends TestThief(() => new Program(
  new SemiNaiveExecutionEngine(
    new RelationalStorageManager())), "partial", "SemiNaive", "Relational")
class TT_PARTIAL_Naive_Relational extends TestThief(() => new Program(
  new NaiveExecutionEngine(
    new RelationalStorageManager())), "partial", "Naive", "Relational")
class TT_PARTIAL_SemiNaive_IdxCollections extends TestThief(() => new Program(
  new SemiNaiveExecutionEngine(
    new IndexedCollStorageManager())), "partial", "SemiNaive", "IndexedColl")
class TT_PARTIAL_Naive_IdxCollections extends TestThief(() => new Program(
  new NaiveExecutionEngine(
    new IndexedCollStorageManager())), "partial", "Naive", "IndexedColl")

class TT_COMPLETE_SemiNaive_Relational extends TestThief(() => new Program(
  new SemiNaiveExecutionEngine(
    new RelationalStorageManager())), "complete", "SemiNaive", "Relational")
class TT_COMPLETE_Naive_Relational extends TestThief(() => new Program(
  new NaiveExecutionEngine(
    new RelationalStorageManager())), "complete", "Naive", "Relational")
class TT_COMPLETE_SemiNaive_IdxCollections extends TestThief(() => new Program(
  new SemiNaiveExecutionEngine(
    new IndexedCollStorageManager())), "complete", "SemiNaive", "IndexedColl")
class TT_COMPLETE_Naive_IdxCollections extends TestThief(() => new Program(
  new NaiveExecutionEngine(
    new IndexedCollStorageManager())), "complete", "Naive", "IndexedColl")
