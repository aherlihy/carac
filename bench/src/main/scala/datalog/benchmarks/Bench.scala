package datalog.benchmarks

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import datalog.dsl.*
//import datalog.execution.old_manual_opt.{ManuallyCollapseParent, ManuallyInlinedExternal, ManuallyInlinedInternal}
import datalog.execution.{ExecutionEngine, /*ManuallyInlinedEE, ManuallyInlinedUnrolledEE,*/ NaiveExecutionEngine, SemiNaiveExecutionEngine}
import datalog.storage.{CollectionsStorageManager, IndexedCollStorageManager, RelationalStorageManager}

import scala.collection.mutable

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
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

    e("a", "b") :- ()
    e("b", "c") :- ()
    e("c", "d") :- ()
    p(x, y) :- e(x, y)
    p(x, z) :- ( e(x, y), p(y, z) )
    path2a(x) :- p("a", x)
    edge2a(x) :- e("a", x)

    def solve[T <: Constant](rel: Relation[T]): Unit =
      blackhole.consume(
        assert(rel.solve() == Set(Vector("a", "d"), Vector("b", "d"), Vector("b", "c"), Vector("a", "b"), Vector("a", "c"), Vector("c", "d")))
      )

    // FIXME: we redirect Console.out.println to a dummy stream here because
    // running the solver prints out a lot of debug information that obscures
    // the benchmarking output.
    Console.withOut(dummyStream) {
      solve(p)
//      solve(ans1)
//      solve(ans2)
    }
  }

//  @Benchmark def transitiveClosure_naive_relational(blackhole: Blackhole): Unit =
//    given engine: ExecutionEngine = new NaiveExecutionEngine(new RelationalStorageManager())
//    transitiveClosure(engine, blackhole)
//
//  @Benchmark def transitiveClosure_semiNaive_relational(blackhole: Blackhole): Unit =
//    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new RelationalStorageManager())
//    transitiveClosure(engine, blackhole)

//  @Benchmark def transitiveClosure_manually_inlined(blackhole: Blackhole): Unit =
//    given engine: ExecutionEngine = new ManuallyInlinedEE(new CollectionsStorageManager())
//    transitiveClosure(engine, blackhole)
//
//  @Benchmark def transitiveClosure_manually_inlined_unrolled(blackhole: Blackhole): Unit =
//    given engine: ExecutionEngine = new ManuallyInlinedUnrolledEE(new CollectionsStorageManager())
//    transitiveClosure(engine, blackhole)
//
//  @Benchmark def transitiveClosure_manually_inline_unrolled_indexed(blackhole: Blackhole): Unit =
//    given engine: ExecutionEngine = new ManuallyInlinedUnrolledEE(new IndexedCollStorageManager())
//    transitiveClosure(engine, blackhole)
//  @Benchmark def transitiveClosure_manually_inlined_internal(blackhole: Blackhole): Unit =
//    given engine: ExecutionEngine = new ManuallyInlinedInternal(new CollectionsStorageManager())
//    transitiveClosure(engine, blackhole)
//
//  @Benchmark def transitiveClosure_manually_inlined_external(blackhole: Blackhole): Unit =
//    given engine: ExecutionEngine = new ManuallyInlinedExternal(new CollectionsStorageManager())
//    transitiveClosure(engine, blackhole)
//
//  @Benchmark def transitiveClosure_manually_collapse_parent(blackhole: Blackhole): Unit =
//    given engine: ExecutionEngine = new ManuallyCollapseParent(new CollectionsStorageManager())
//    transitiveClosure(engine, blackhole)

  @Benchmark def transitiveClosure_baseline(blackhole: Blackhole): Unit =
    given engine: ExecutionEngine = new SemiNaiveExecutionEngine(new CollectionsStorageManager())
    transitiveClosure(engine, blackhole)


  //  @Benchmark def transitiveClosure_manualOptMinimized(blackhole: Blackhole): Unit =
//    given engine: ExecutionEngine = new ManuallyOptimizedExecutionEngine3(new CollectionsStorageManager())
//    transitiveClosure(engine, blackhole)
//  @Benchmark def transitiveClosure_Naive_collections(blackhole: Blackhole): Unit =
//    given engine: ExecutionEngine = new NaiveExecutionEngine(new CollectionsStorageManager())
//    transitiveClosure(engine, blackhole)
}
