package datalog.benchmarks

import datalog.dsl.*
import datalog.execution.{Backend, CompileSync, ExecutionEngine, Granularity, JITOptions, NaiveShallowExecutionEngine, ShallowExecutionEngine, SortOrder, StagedExecutionEngine, ir, Mode as CaracMode}
import datalog.storage.{CollectionsStorageManager, DuckDBStorageManager}
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.rqb_andersen.rqb_andersen

import java.nio.file.{FileSystems, Files, Path, Paths}
import java.util.concurrent.TimeUnit
import scala.collection.immutable.Map
import scala.concurrent.duration.Duration
import scala.quoted.staging
import scala.sys.process.Process
import scala.util.Using

val SOUFFLE_BIN="souffle"

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_andersen extends rqb_andersen {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null
//  val dotty = staging.Compiler.make(getClass.getClassLoader)

  //  @Setup(Level.Trial)
  //  def mkdirs(): Unit = {
  //    Process(s"mkdir souffle-out").!
  //    Process(s"mkdir carac-out").!
  //    Process(s"mkdir carac-scala-out").!
  //  }

  @Setup(Level.Iteration)
  def s(): Unit = {
    Process(s"rm -rf souffle-out/compile/$benchmark").!
    Process(s"mkdir souffle-out/compile/$benchmark").!
    Process(s"rm -rf souffle-out/interp/$benchmark").!
    Process(s"mkdir souffle-out/interp/$benchmark").!
    Process(s"rm -rf souffle-out/profile-compile/$benchmark").!
    Process(s"mkdir souffle-out/profile-compile/$benchmark").!
    Process(s"rm -r $benchmark-profile-compile")
    Process(s"rm -rf souffle-out/profile-interp/$benchmark").!
    Process(s"mkdir souffle-out/profile-interp/$benchmark").!
    Process(s"rm -r $benchmark-profile-interp")
    Process(s"rm -rf carac-out/$benchmark").!
    Process(s"mkdir carac-out/$benchmark").!
    Process(s"rm -rf carac-scala-out/$benchmark").!
    Process(s"mkdir carac-scala-out/$benchmark").!
  }

  @Benchmark def carac_warm_bytecode(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Bytecode)
    val engine = new StagedExecutionEngine(new DuckDBStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(factDirectory)
    pretest(program)
    blackhole.consume(
      program.namedRelation(toSolve).solve()
    )
    engine.precedenceGraph.idbs.foreach(i =>
      val idb = engine.storageManager.ns(i)
      Using(Files.newBufferedWriter(Paths.get("carac-scala-out", benchmark, idb + ".csv"))) { writer =>
        engine.get(idb).foreach(f => writer.write(f.mkString("", "\t", "\n")))
      })
  }

  @Benchmark def carac_warm_lambda(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new DuckDBStorageManager(), jo)
    val program = Program(engine)
    program.loadFromFactDir(factDirectory)
    pretest(program)
    blackhole.consume(
      program.namedRelation(toSolve).solve()
    )
    engine.precedenceGraph.idbs.foreach(i =>
      val idb = engine.storageManager.ns(i)
      Using(Files.newBufferedWriter(Paths.get("carac-scala-out", benchmark, idb + ".csv"))) { writer =>
        engine.get(idb).foreach(f => writer.write(f.mkString("", "\t", "\n")))
      })
  }

  @Benchmark def carac_native_lambda(blackhole: Blackhole): Unit = {
    val b = "lambda"
    val pb = Process(Seq(s"../target/native-image/carac", benchmark, b))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def carac_jar_lambda(blackhole: Blackhole): Unit = {
    val b = "lambda"
    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def carac_jar_bytecode(blackhole: Blackhole): Unit = {
    val b = "bytecode"
    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  private def run_souffle(mode: String, blackhole: Blackhole): Unit = {
    println(s"running src/test/scala/datalog/benchmarks/souffle/souffle-driver.sh $benchmark $mode")
    val pb = Process(Seq("src/test/scala/datalog/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, mode))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle $mode failed with code $exitCode")
    blackhole.consume(exitCode)
  }

  @Benchmark def souffle__compile(blackhole: Blackhole): Unit =
    run_souffle("compile", blackhole)

  @Benchmark def souffle__interp(blackhole: Blackhole): Unit =
    run_souffle("interp", blackhole)

  @Benchmark def souffle_profile_compile(blackhole: Blackhole): Unit =
    run_souffle("profile-compile", blackhole)

  @Benchmark def souffle_profile_interp(blackhole: Blackhole): Unit =
    run_souffle("profile-interp", blackhole)
}

