package datalog.benchmarks

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import datalog.dsl.*
import datalog.execution.{ExecutionEngine, NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.storage.{CollectionsStorageManager, IndexedCollStorageManager, JoinOptIdxCollStorageManager, RelationalStorageManager, NS, JoinOptCollStorageManager}

import scala.collection.mutable
import scala.util.Random

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class Bench {
  val dummyStream = new java.io.PrintStream(_ => ())


  def transitiveClosure(engine: ExecutionEngine, blackhole: Blackhole): Unit = {

    val program = Program(engine)
    val e = program.relation[String]("e")
    val p = program.relation[String]("p")
    val path2a = program.relation[String]("path2a")
    val path2a1 = program.relation[String]("path2a1")
    val edge2a = program.relation[String]("edge2a")

    val x, y, z = program.variable()

    for i <- 0 until 50 do
      e(
        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString,
        Random.alphanumeric.dropWhile(_.isDigit).dropWhile(_.isUpper).head.toString
      )  :- ()

    p(x, y) :- e(x, y)
    p(x, z) :- ( e(x, y), p(y, z) )


    def solve[T <: Constant](rel: Relation[T]): Unit =
      blackhole.consume(
          rel.solve()
      )

    // FIXME: we redirect Console.out.println to a dummy stream here because
    // running the solver prints out a lot of debug information that obscures
    // the benchmarking output.
    Console.withOut(dummyStream) {
      solve(p)
    }
  }

  // relational, naive
  @Benchmark def transitiveClosure_naive_relational(blackhole: Blackhole): Unit =
    given engine: ExecutionEngine = new NaiveExecutionEngine(new RelationalStorageManager())
    transitiveClosure(engine, blackhole)

  @Benchmark def transitiveClosure_naive_relationalOpt(blackhole: Blackhole): Unit =
    given engine: ExecutionEngine = new NaiveExecutionEngine(new RelationalStorageManager(NS(), true))
    transitiveClosure(engine, blackhole)

  // relational, seminaive
  @Benchmark def transitiveClosure_semiNaive_relational(blackhole: Blackhole): Unit =
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new RelationalStorageManager())
    transitiveClosure(engine, blackhole)

  @Benchmark def transitiveClosure_semiNaive_relationalOpt(blackhole: Blackhole): Unit =
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new RelationalStorageManager(NS(), true))
    transitiveClosure(engine, blackhole)

  // seminaive, indexedcoll
  @Benchmark def transitiveClosure_semiNaive_IdxcollectionsOpt(blackhole: Blackhole): Unit =
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new JoinOptIdxCollStorageManager())
    transitiveClosure(engine, blackhole)

  @Benchmark def transitiveClosure_semiNaive_Idxcollections(blackhole: Blackhole): Unit =
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new IndexedCollStorageManager())
    transitiveClosure(engine, blackhole)

  // naive, indexedcoll
  @Benchmark def transitiveClosure_naive_IdxcollectionsOpt(blackhole: Blackhole): Unit =
    given engine: ExecutionEngine = new NaiveExecutionEngine(new JoinOptIdxCollStorageManager())
    transitiveClosure(engine, blackhole)

  @Benchmark def transitiveClosure_naive_Idxcollections(blackhole: Blackhole): Unit =
    given engine: ExecutionEngine = new NaiveExecutionEngine(new IndexedCollStorageManager())
    transitiveClosure(engine, blackhole)

  // seminaive, coll
  @Benchmark def transitiveClosure_semiNaive_collectionsOpt(blackhole: Blackhole): Unit =
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new JoinOptCollStorageManager())
    transitiveClosure(engine, blackhole)

  @Benchmark def transitiveClosure_semiNaive_collections(blackhole: Blackhole): Unit =
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new CollectionsStorageManager())
    transitiveClosure(engine, blackhole)

  // naive, coll
  @Benchmark def transitiveClosure_naive_collectionsOpt(blackhole: Blackhole): Unit =
    given engine: ExecutionEngine = new NaiveExecutionEngine(new JoinOptCollStorageManager())
    transitiveClosure(engine, blackhole)

  @Benchmark def transitiveClosure_naive_collections(blackhole: Blackhole): Unit =
    given engine: ExecutionEngine = new NaiveExecutionEngine(new CollectionsStorageManager())
    transitiveClosure(engine, blackhole)
}
