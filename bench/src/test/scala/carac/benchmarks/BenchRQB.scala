package carac.benchmarks

import carac.dsl.*
import carac.execution.{Backend, CompileSync, ExecutionEngine, Granularity, JITOptions, SortOrder, StagedExecutionEngine, Mode as CaracMode}
import carac.storage.{DatabaseType, DuckDBStorageManager, IndexedStorageManager}
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.rqb_andersen.rqb_andersen
import test.examples.rqb_cba.rqb_cba
import test.examples.rqb_cspa.rqb_cspa
import test.examples.rqb_ancestry.rqb_ancestry
import test.examples.rqb_sssp.rqb_sssp
import test.examples.rqb_bom.rqb_bom

import java.nio.file.{Files, Paths}
import java.util.concurrent.TimeUnit
import scala.sys.process.Process
import scala.util.Using

val SOUFFLE_BIN="/scratch/herlihy/souffle/build/src/souffle"

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
@Warmup(iterations = 0, batchSize = 1)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_andersen_souffle extends rqb_andersen {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null

  RQB_Bench.cleanup(benchmark)

  private def run_souffle(mode: String, blackhole: Blackhole): Unit = {
    val pb = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, mode))
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


@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_andersen_carac extends rqb_andersen {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null

  RQB_Bench.cleanup(benchmark)

  private def run_warm_carac(blackhole: Blackhole, mode: String, engine: ExecutionEngine, duckDBStorageManager: DuckDBStorageManager): Unit = {
    val program = Program(engine)
    if (duckDBStorageManager != null)
      val addressOf = program.relation("addressOf")
      val assign = program.relation("assign")
      val loadT = program.relation("loadT")
      val store = program.relation("store")
      duckDBStorageManager.declareTable(addressOf.id, Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
      duckDBStorageManager.declareTable(assign.id, Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
      duckDBStorageManager.declareTable(loadT.id, Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
      duckDBStorageManager.declareTable(store.id, Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
      duckDBStorageManager.edbs.initializeTable(addressOf.id, "addressOf", Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
      duckDBStorageManager.edbs.initializeTable(assign.id, "assign", Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
      duckDBStorageManager.edbs.initializeTable(loadT.id, "loadT", Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
      duckDBStorageManager.edbs.initializeTable(store.id, "store", Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT)))
      duckDBStorageManager.loadFacts(factDirectory)
    else
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
    engine.storageManager.cleanup()
  }

  @Benchmark def warm_lambda_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = true)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbidx"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }

  @Benchmark def warm_lambda_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbn"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }

  @Benchmark def warm_interp_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "interp_ddbn"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }

  @Benchmark def warm_interp_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val storageManager = new DuckDBStorageManager(indexed = true)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "interp_ddbidx"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }

  @Benchmark def warm_lambda_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
    val mode = "lambda_collidx"
    run_warm_carac(blackhole, mode, engine, null)
  }

  @Benchmark def warm_interp_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
    val mode = "interp_collidx"
    run_warm_carac(blackhole, mode, engine, null)
  }

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
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_andersen_embedded() extends ExampleBenchmarkGenerator(
  "rqb_andersen"
) with rqb_andersen {
  @Benchmark def jit_indexed_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_indexed_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_ddb_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_ddb_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
  @Benchmark def jit_ddbnidx_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_ddbnidx_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 0, batchSize = 1)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_cba_souffle extends rqb_cba {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null

  RQB_Bench.cleanup(benchmark)

  private def run_souffle(mode: String, blackhole: Blackhole): Unit = {
    val pb = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, mode))
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


@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_cba_carac extends rqb_cba {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null

  RQB_Bench.cleanup(benchmark)

  private def run_warm_carac(blackhole: Blackhole, mode: String, engine: ExecutionEngine, duckDBStorageManager: DuckDBStorageManager): Unit = {
    val program = Program(engine)
    if (duckDBStorageManager != null)
      val term = program.relation("term")
      val termS = Seq(("c0", DatabaseType.INTEGER), ("c1", DatabaseType.TEXT), ("c2", DatabaseType.INTEGER))
      duckDBStorageManager.declareTable(term.id, termS)
      duckDBStorageManager.edbs.initializeTable(term.id, "term", termS)
      val vars = program.relation("vars")
      val varsS = Seq(("c0", DatabaseType.INTEGER), ("c1", DatabaseType.TEXT))
      duckDBStorageManager.declareTable(vars.id, varsS)
      duckDBStorageManager.edbs.initializeTable(vars.id, "vars", varsS)
      val app = program.relation("app")
      val appS = Seq(("c0", DatabaseType.INTEGER), ("c1", DatabaseType.INTEGER), ("c2", DatabaseType.INTEGER))
      duckDBStorageManager.declareTable(app.id, appS)
      duckDBStorageManager.edbs.initializeTable(app.id, "app", appS)
      val lits = program.relation("lits")
      val litsS = Seq(("c0", DatabaseType.INTEGER), ("c1", DatabaseType.TEXT))
      duckDBStorageManager.declareTable(lits.id, litsS)
      duckDBStorageManager.edbs.initializeTable(lits.id, "lits", litsS)
      val abs = program.relation("abs")
      val absS = Seq(("c0", DatabaseType.INTEGER), ("c1", DatabaseType.INTEGER), ("c2", DatabaseType.INTEGER))
      duckDBStorageManager.declareTable(abs.id, absS)
      duckDBStorageManager.edbs.initializeTable(abs.id, "abs", absS)
      duckDBStorageManager.loadFacts(factDirectory)
    else
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
    engine.storageManager.cleanup()
  }

  @Benchmark def warm_lambda_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = true)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbidx"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }

  @Benchmark def warm_lambda_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbn"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }

  @Benchmark def warm_interp_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "interp_ddbn"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }

  @Benchmark def warm_interp_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val storageManager = new DuckDBStorageManager(indexed = true)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "interp_ddbidx"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }

  @Benchmark def warm_lambda_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
    val mode = "lambda_collidx"
    run_warm_carac(blackhole, mode, engine, null)
  }

  @Benchmark def warm_interp_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
    val mode = "interp_collidx"
    run_warm_carac(blackhole, mode, engine, null)
  }

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
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_cba_embedded() extends ExampleBenchmarkGenerator(
  "rqb_cba"
) with rqb_cba {
  @Benchmark def jit_indexed_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_indexed_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_ddb_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_ddb_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_ddbnidx_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_ddbnidx_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 0, batchSize = 1)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_cspa_souffle extends rqb_cspa {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null

  RQB_Bench.cleanup(benchmark)

  private def run_souffle(mode: String, blackhole: Blackhole): Unit = {
    val pb = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, mode))
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


@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_cspa_carac extends rqb_cspa {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null

  RQB_Bench.cleanup(benchmark)

  private def run_warm_carac(blackhole: Blackhole, mode: String, engine: ExecutionEngine, duckDBStorageManager: DuckDBStorageManager): Unit = {
    val program = Program(engine)
    if (duckDBStorageManager != null)
      val assign = program.relation("assign")
      val assignS = Seq(("c0", DatabaseType.INTEGER), ("c1", DatabaseType.INTEGER))
      duckDBStorageManager.declareTable(assign.id, assignS)
      duckDBStorageManager.edbs.initializeTable(assign.id, "assign", assignS)
      val dereference = program.relation("dereference")
      val dereferenceS = Seq(("c0", DatabaseType.INTEGER), ("c1", DatabaseType.INTEGER))
      duckDBStorageManager.declareTable(dereference.id, dereferenceS)
      duckDBStorageManager.edbs.initializeTable(dereference.id, "dereference", dereferenceS)
      duckDBStorageManager.loadFacts(factDirectory)
    else
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
    engine.storageManager.cleanup()
  }

  @Benchmark def warm_lambda_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = true)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbidx"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }

  @Benchmark def warm_lambda_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbn"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }

  @Benchmark def warm_interp_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "interp_ddbn"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }

  @Benchmark def warm_interp_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val storageManager = new DuckDBStorageManager(indexed = true)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "interp_ddbidx"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }

  @Benchmark def warm_lambda_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
    val mode = "lambda_collidx"
    run_warm_carac(blackhole, mode, engine, null)
  }

  @Benchmark def warm_interp_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
    val mode = "interp_collidx"
    run_warm_carac(blackhole, mode, engine, null)
  }
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
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_cspa_embedded() extends ExampleBenchmarkGenerator(
  "rqb_cspa"
) with rqb_cspa {
  @Benchmark def jit_indexed_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_indexed_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_ddb_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_ddb_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def jit_ddbnidx_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }

  @Benchmark def interpreted_ddbnidx_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run(programs(p), result))
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_ancestry_carac extends rqb_ancestry {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null

  RQB_Bench.cleanup(benchmark)

  private def run_warm_carac(blackhole: Blackhole, mode: String, storage: DuckDBStorageManager, options: JITOptions): Unit = {
    val engine = StagedExecutionEngine(storage, options)
    val program = Program(engine)
    val parents = program.relation("parents")
    val parentsS = Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT))
    storage.declareTable(parents.id, parentsS)
    storage.edbs.initializeTable(parents.id, "parents", parentsS)
    storage.loadFacts(factDirectory)
    pretest(program)
    val result = storage.resultSetToString(storage.runQuery(sqlString))
    blackhole.consume(result)
    val path = Paths.get("carac-scala-out", benchmark, mode, toSolve + ".csv")
    Using(Files.newBufferedWriter(path)) { writer =>
      writer.write(result)
    }
    engine.storageManager.cleanup()
  }

  @Benchmark def warm_lambda_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storage = new DuckDBStorageManager(indexed = true)
    val mode = "lambda_ddbidx"
    run_warm_carac(blackhole, mode, storage, jo)
  }

  @Benchmark def warm_lambda_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storage = new DuckDBStorageManager(indexed = false)
    val mode = "lambda_ddbn"
    run_warm_carac(blackhole, mode, storage, jo)
  }

  @Benchmark def warm_interp_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val storage = new DuckDBStorageManager(indexed = false)
    val mode = "interp_ddbn"
    run_warm_carac(blackhole, mode, storage, jo)
  }

  @Benchmark def warm_interp_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val storage = new DuckDBStorageManager(indexed = true)
    val mode = "interp_ddbidx"
    run_warm_carac(blackhole, mode, storage, jo)
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 0, batchSize = 1)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_ancestry_souffle extends rqb_ancestry {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null

  RQB_Bench.cleanup(benchmark)

  private def run_souffle(mode: String, blackhole: Blackhole): Unit = {
    val pb = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, mode))
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

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_ancestry_embedded() extends ExampleBenchmarkGenerator(
  "rqb_ancestry"
) with rqb_ancestry {
  @Benchmark def jit_ddb_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run_ddb(sqlString, programs(p), result))
  }

  @Benchmark def interpreted_ddb_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run_ddb(sqlString, programs(p), result))
  }

  @Benchmark def jit_ddbnidx_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run_ddb(sqlString, programs(p), result))
  }

  @Benchmark def interpreted_ddbnidx_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run_ddb(sqlString, programs(p), result))
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_sssp_carac extends rqb_sssp {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null

  RQB_Bench.cleanup(benchmark)

  private def run_warm_carac(blackhole: Blackhole, mode: String, storage: DuckDBStorageManager, options: JITOptions): Unit = {
    val engine = StagedExecutionEngine(storage, options)
    val program = Program(engine)
    val base = program.relation("base")
    val baseS = Seq(("c0", DatabaseType.INTEGER), ("c1", DatabaseType.INTEGER))
    storage.declareTable(base.id, baseS)
    storage.edbs.initializeTable(base.id, "base", baseS)
    val edge = program.relation("edge")
    val edgeS = Seq(("c0", DatabaseType.INTEGER), ("c1", DatabaseType.INTEGER), ("c2", DatabaseType.INTEGER))
    storage.declareTable(edge.id, edgeS)
    storage.edbs.initializeTable(edge.id, "edge", edgeS)
    storage.loadFacts(factDirectory)
    pretest(program)
    val result = storage.resultSetToString(storage.runQuery(sqlString))
    blackhole.consume(result)
    val path = Paths.get("carac-scala-out", benchmark, mode, toSolve + ".csv")
    Using(Files.newBufferedWriter(path)) { writer =>
      writer.write(result)
    }
    engine.storageManager.cleanup()
  }
  @Benchmark def warm_lambda_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storage = new DuckDBStorageManager(indexed = true)
    val mode = "lambda_ddbidx"
    run_warm_carac(blackhole, mode, storage, jo)
  }

  @Benchmark def warm_lambda_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storage = new DuckDBStorageManager(indexed = false)
    val mode = "lambda_ddbn"
    run_warm_carac(blackhole, mode, storage, jo)
  }

  @Benchmark def warm_interp_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val storage = new DuckDBStorageManager(indexed = false)
    val mode = "interp_ddbn"
    run_warm_carac(blackhole, mode, storage, jo)
  }

  @Benchmark def warm_interp_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val storage = new DuckDBStorageManager(indexed = true)
    val mode = "interp_ddbidx"
    run_warm_carac(blackhole, mode, storage, jo)
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 0, batchSize = 1)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_sssp_souffle extends rqb_sssp {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null

  RQB_Bench.cleanup(benchmark)

  private def run_souffle(mode: String, blackhole: Blackhole): Unit = {
    val pb = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, mode))
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

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_sssp_embedded() extends ExampleBenchmarkGenerator(
  "rqb_sssp"
) with rqb_sssp {
  @Benchmark def jit_ddb_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run_ddb(sqlString, programs(p), result))
  }

  @Benchmark def interpreted_ddb_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run_ddb(sqlString, programs(p), result))
  }

  @Benchmark def jit_ddbnidx_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run_ddb(sqlString, programs(p), result))
  }

  @Benchmark def interpreted_ddbnidx_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run_ddb(sqlString, programs(p), result))
  }
}
@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_bom_carac extends rqb_bom {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null

  RQB_Bench.cleanup(benchmark)

  private def run_warm_carac(blackhole: Blackhole, mode: String, storage: DuckDBStorageManager, options: JITOptions): Unit = {
    val engine = StagedExecutionEngine(storage, options)
    val program = Program(engine)
    val assbl = program.relation("assbl")
    val assblS = Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.TEXT))
    storage.declareTable(assbl.id, assblS)
    storage.edbs.initializeTable(assbl.id, "assbl", assblS)
    val basic = program.relation("basic")
    val basicS = Seq(("c0", DatabaseType.TEXT), ("c1", DatabaseType.INTEGER))
    storage.declareTable(basic.id, basicS)
    storage.edbs.initializeTable(basic.id, "basic", basicS)
    storage.loadFacts(factDirectory)
    pretest(program)
    val result = storage.resultSetToString(storage.runQuery(sqlString))
    blackhole.consume(result)
    val path = Paths.get("carac-scala-out", benchmark, mode, toSolve + ".csv")
    Using(Files.newBufferedWriter(path)) { writer =>
      writer.write(result)
    }
    engine.storageManager.cleanup()
  }
  @Benchmark def warm_lambda_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storage = new DuckDBStorageManager(indexed = true)
    val mode = "lambda_ddbidx"
    run_warm_carac(blackhole, mode, storage, jo)
  }

  @Benchmark def warm_lambda_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storage = new DuckDBStorageManager(indexed = false)
    val mode = "lambda_ddbn"
    run_warm_carac(blackhole, mode, storage, jo)
  }

  @Benchmark def warm_interp_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val storage = new DuckDBStorageManager(indexed = false)
    val mode = "interp_ddbn"
    run_warm_carac(blackhole, mode, storage, jo)
  }

  @Benchmark def warm_interp_ddbidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val storage = new DuckDBStorageManager(indexed = true)
    val mode = "interp_ddbidx"
    run_warm_carac(blackhole, mode, storage, jo)
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 0, batchSize = 1)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_bom_souffle extends rqb_bom {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null

  RQB_Bench.cleanup(benchmark)

  private def run_souffle(mode: String, blackhole: Blackhole): Unit = {
    val pb = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, mode))
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

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_bom_embedded() extends ExampleBenchmarkGenerator(
  "rqb_bom"
) with rqb_bom {
  @Benchmark def jit_ddb_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run_ddb(sqlString, programs(p), result))
  }

  @Benchmark def interpreted_ddb_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run_ddb(sqlString, programs(p), result))
  }

  @Benchmark def jit_ddbnidx_sel__0_blocking_DELTA_lambda_EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run_ddb(sqlString, programs(p), result))
  }

  @Benchmark def interpreted_ddbnidx_sel__0____EOL(blackhole: Blackhole): Unit = {
    val p = s"${Thread.currentThread.getStackTrace()(2).getMethodName.split("_EOL").head}"
    if (!programs.contains(p))
      throw new Exception(f"Error: program for '$p' not found")
    blackhole.consume(run_ddb(sqlString, programs(p), result))
  }
}
