package carac.benchmarks

import carac.dsl.*
import carac.execution.{Backend, CompileSync, ExecutionEngine, Granularity, JITOptions, NaiveShallowExecutionEngine, ShallowExecutionEngine, SortOrder, StagedExecutionEngine, ir, Mode as CaracMode}
import carac.storage.{CollectionsStorageManager, DuckDBStorageManager, IndexedStorageManager}
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.rqb_andersen.rqb_andersen
import test.examples.rqb_cba.rqb_cba_worst

import java.nio.file.{FileSystems, Files, Path, Paths}
import java.util.concurrent.TimeUnit
import scala.collection.immutable.Map
import scala.concurrent.duration.Duration
import scala.quoted.staging
import scala.sys.process.Process
import scala.util.Using

val SOUFFLE_BIN="souffle"///scratch/herlihy/souffle/build/src/souffle"

object RQB_Bench {
  def cleanup(benchmark: String): Unit = {
    Process(s"rm -rf souffle-out/compile/$benchmark").!
    Process(s"mkdir -p souffle-out/compile/$benchmark").!
    Process(s"rm -rf souffle-out/interp/$benchmark").!
    Process(s"mkdir -p souffle-out/interp/$benchmark").!
    Process(s"rm -rf souffle-out/profile-compile/$benchmark").!
    Process(s"mkdir -p souffle-out/profile-compile/$benchmark").!
    Process(s"rm -r $benchmark-profile-compile")
    Process(s"rm -rf souffle-out/profile-interp/$benchmark").!
    Process(s"mkdir -p souffle-out/profile-interp/$benchmark").!
    Process(s"rm -r $benchmark-profile-interp")
    //    Process(s"rm -rf carac-out/$benchmark").!
    //    Process(s"mkdir -p carac-out/$benchmark").!
    //    Process(s"rm -rf carac-scala-out/$benchmark").!
    //    Process(s"mkdir -p  carac-scala-out/$benchmark").!
    Process(s"mkdir -p  carac-scala-out/$benchmark/lambda_ddbidx").!
    Process(s"mkdir -p  carac-scala-out/$benchmark/lambda_collidx").!
    Process(s"mkdir -p  carac-scala-out/$benchmark/lambda_ddbn").!
    Process(s"mkdir -p  carac-scala-out/$benchmark/interp_ddbidx").!
    Process(s"mkdir -p  carac-scala-out/$benchmark/interp_collidx").!
    Process(s"mkdir -p  carac-scala-out/$benchmark/interp_ddbn").!
  }
}

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

  RQB_Bench.cleanup(benchmark)

  private def run_warm_carac(blackhole: Blackhole, mode: String, engine: ExecutionEngine): Unit = {
    val program = Program(engine)
    program.loadFromFactDir(factDirectory)
    pretest(program)
    blackhole.consume(
      program.namedRelation(toSolve).solve()
    )
    val idb = engine.storageManager.ns(toSolve)
    val path = Paths.get("carac-scala-out", benchmark, mode, toSolve + ".csv")
    Using(Files.newBufferedWriter(path)) { writer =>
      engine.get(idb).foreach(f => writer.write(f.mkString("", "\t", "\n")))
    }
  }

  private def run_souffle(mode: String, blackhole: Blackhole): Unit = {
    val pb = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, mode))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle $mode failed with code $exitCode")
    blackhole.consume(exitCode)
  }

  @Benchmark def carac_warm_lambda_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new DuckDBStorageManager(indexed = true), jo)
    val mode = "lambda_ddbidx"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def carac_warm_lambda_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new DuckDBStorageManager(indexed = false), jo)
    val mode = "lambda_ddbn"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def carac_warm_interp_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new DuckDBStorageManager(indexed = false), jo)
    val mode = "interp_ddbn"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def carac_warm_interp_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new DuckDBStorageManager(indexed = true), jo)
    val mode = "interp_ddbidx"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def carac_warm_lambda_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
    val mode = "lambda_collidx"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def carac_warm_interp_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
    val mode = "interp_collidx"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def souffle__compile(blackhole: Blackhole): Unit =
    run_souffle("compile", blackhole)

  @Benchmark def souffle__interp(blackhole: Blackhole): Unit =
    run_souffle("interp", blackhole)

  @Benchmark def souffle_profile_compile(blackhole: Blackhole): Unit =
    run_souffle("profile-compile", blackhole)

  @Benchmark def souffle_profile_interp(blackhole: Blackhole): Unit =
    run_souffle("profile-interp", blackhole)

  //  @Benchmark def carac_native_lambda(blackhole: Blackhole): Unit = {
  //    val b = "lambda"
  //    val pb = Process(Seq(s"../target/native-image/carac", benchmark, b))
  //    val exitCode = pb.!
  //    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
  //    blackhole.consume(exitCode) // prob unnecessary
  //  }

  //  @Benchmark def carac_jar_lambda(blackhole: Blackhole): Unit = {
  //    val b = "lambda"
  //    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
  //    val exitCode = pb.!
  //    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
  //    blackhole.consume(exitCode) // prob unnecessary
  //  }
  //
  //  @Benchmark def carac_jar_bytecode(blackhole: Blackhole): Unit = {
  //    val b = "bytecode"
  //    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
  //    val exitCode = pb.!
  //    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
  //    blackhole.consume(exitCode) // prob unnecessary
  //  }
  //
  //  @Benchmark def carac_jar_interp(blackhole: Blackhole): Unit = {
  //    val b = "Interpreted"
  //    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
  //    val exitCode = pb.!
  //    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
  //    blackhole.consume(exitCode) // prob unnecessary
  //  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_cba extends rqb_cba_worst {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null

  RQB_Bench.cleanup(benchmark)

  private def run_warm_carac(blackhole: Blackhole, mode: String, engine: ExecutionEngine): Unit = {
    val program = Program(engine)
    program.loadFromFactDir(factDirectory)
    pretest(program)
    blackhole.consume(
      program.namedRelation(toSolve).solve()
    )
    val idb = engine.storageManager.ns(toSolve)
    val path = Paths.get("carac-scala-out", benchmark, mode, toSolve + ".csv")
    Using(Files.newBufferedWriter(path)) { writer =>
      engine.get(idb).foreach(f => writer.write(f.mkString("", "\t", "\n")))
    }
  }

  private def run_souffle(mode: String, blackhole: Blackhole): Unit = {
    val pb = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, mode))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle $mode failed with code $exitCode")
    blackhole.consume(exitCode)
  }

  @Benchmark def carac_warm_lambda_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new DuckDBStorageManager(indexed = true), jo)
    val mode = "lambda_ddbidx"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def carac_warm_lambda_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new DuckDBStorageManager(indexed = false), jo)
    val mode = "lambda_ddbn"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def carac_warm_interp_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new DuckDBStorageManager(indexed = false), jo)
    val mode = "interp_ddbn"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def carac_warm_interp_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new DuckDBStorageManager(indexed = true), jo)
    val mode = "interp_ddbidx"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def carac_warm_lambda_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
    val mode = "lambda_collidx"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def carac_warm_interp_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
    val mode = "interp_collidx"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def souffle__compile(blackhole: Blackhole): Unit =
    run_souffle("compile", blackhole)

  @Benchmark def souffle__interp(blackhole: Blackhole): Unit =
    run_souffle("interp", blackhole)

  @Benchmark def souffle_profile_compile(blackhole: Blackhole): Unit =
    run_souffle("profile-compile", blackhole)

  @Benchmark def souffle_profile_interp(blackhole: Blackhole): Unit =
    run_souffle("profile-interp", blackhole)

  //  @Benchmark def carac_native_lambda(blackhole: Blackhole): Unit = {
  //    val b = "lambda"
  //    val pb = Process(Seq(s"../target/native-image/carac", benchmark, b))
  //    val exitCode = pb.!
  //    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
  //    blackhole.consume(exitCode) // prob unnecessary
  //  }

  //  @Benchmark def carac_jar_lambda(blackhole: Blackhole): Unit = {
  //    val b = "lambda"
  //    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
  //    val exitCode = pb.!
  //    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
  //    blackhole.consume(exitCode) // prob unnecessary
  //  }
  //
  //  @Benchmark def carac_jar_bytecode(blackhole: Blackhole): Unit = {
  //    val b = "bytecode"
  //    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
  //    val exitCode = pb.!
  //    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
  //    blackhole.consume(exitCode) // prob unnecessary
  //  }
  //
  //  @Benchmark def carac_jar_interp(blackhole: Blackhole): Unit = {
  //    val b = "Interpreted"
  //    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
  //    val exitCode = pb.!
  //    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
  //    blackhole.consume(exitCode) // prob unnecessary
  //  }
}

