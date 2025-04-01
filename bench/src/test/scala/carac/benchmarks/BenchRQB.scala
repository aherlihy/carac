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

  @Benchmark def zsouffle_preprofiled_compile(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-compile", blackhole)

  @Benchmark def zsouffle_preprofiled_interp(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-interp", blackhole)
}


@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize= 1)
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
      loadSchema(program, duckDBStorageManager)
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

//  @Benchmark def warm_lambda_ddbidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
//    val storageManager = new DuckDBStorageManager(indexed = true)
//    val engine = new StagedExecutionEngine(storageManager, jo)
//    val mode = "lambda_ddbidx"
//    run_warm_carac(blackhole, mode, engine, storageManager)
//  }

  @Benchmark def warm_lambda_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbn"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }
//  @Benchmark def warm_interp_ddbn(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val storageManager = new DuckDBStorageManager(indexed = false)
//    val engine = new StagedExecutionEngine(storageManager, jo)
//    val mode = "interp_ddbn"
//    run_warm_carac(blackhole, mode, engine, storageManager)
//  }
//
//  @Benchmark def warm_interp_ddbidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val storageManager = new DuckDBStorageManager(indexed = true)
//    val engine = new StagedExecutionEngine(storageManager, jo)
//    val mode = "interp_ddbidx"
//    run_warm_carac(blackhole, mode, engine, storageManager)
//  }

  @Benchmark def warm_lambda_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
    val mode = "lambda_collidx"
    run_warm_carac(blackhole, mode, engine, null)
  }

//  @Benchmark def warm_interp_collidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
//    val mode = "interp_collidx"
//    run_warm_carac(blackhole, mode, engine, null)
//  }

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
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_andersen_embedded() extends rqb_andersen {
  val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
  val ddb_storageManager = new DuckDBStorageManager(indexed = false)
  val ddb_engine = new StagedExecutionEngine(ddb_storageManager, jo)
  val ddb_program = Program(ddb_engine)
  val coll_storageManager = new IndexedStorageManager()
  val coll_engine = new StagedExecutionEngine(coll_storageManager, jo)
  val coll_program = Program(coll_engine)

  loadSchema(ddb_program, ddb_storageManager)
  ddb_storageManager.loadFacts(factDirectory)
  coll_program.loadFromFactDir(factDirectory)

  pretest(coll_program)
  pretest(ddb_program)

  @Benchmark def embedded_lambda_ddbn(blackhole: Blackhole): Unit = {
    blackhole.consume(
      ddb_program.namedRelation(toSolve).solve()
    )
  }
  @Benchmark def embedded_lambda_collidx(blackhole: Blackhole): Unit = {
    blackhole.consume(
      coll_program.namedRelation(toSolve).solve()
    )
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

  @Benchmark def zsouffle_preprofiled_compile(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-compile", blackhole)

  @Benchmark def zsouffle_preprofiled_interp(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-interp", blackhole)
}


@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
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
      loadSchema(program, duckDBStorageManager)
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

//  @Benchmark def warm_lambda_ddbidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
//    val storageManager = new DuckDBStorageManager(indexed = true)
//    val engine = new StagedExecutionEngine(storageManager, jo)
//    val mode = "lambda_ddbidx"
//    run_warm_carac(blackhole, mode, engine, storageManager)
//  }

  @Benchmark def warm_lambda_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbn"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }
//  @Benchmark def warm_interp_ddbn(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val storageManager = new DuckDBStorageManager(indexed = false)
//    val engine = new StagedExecutionEngine(storageManager, jo)
//    val mode = "interp_ddbn"
//    run_warm_carac(blackhole, mode, engine, storageManager)
//  }
//
//  @Benchmark def warm_interp_ddbidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val storageManager = new DuckDBStorageManager(indexed = true)
//    val engine = new StagedExecutionEngine(storageManager, jo)
//    val mode = "interp_ddbidx"
//    run_warm_carac(blackhole, mode, engine, storageManager)
//  }

  @Benchmark def warm_lambda_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
    val mode = "lambda_collidx"
    run_warm_carac(blackhole, mode, engine, null)
  }

//  @Benchmark def warm_interp_collidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
//    val mode = "interp_collidx"
//    run_warm_carac(blackhole, mode, engine, null)
//  }

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
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_cba_embedded extends rqb_cba {
  val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
  val ddb_storageManager = new DuckDBStorageManager(indexed = false)
  val ddb_engine = new StagedExecutionEngine(ddb_storageManager, jo)
  val ddb_program = Program(ddb_engine)
  val coll_storageManager = new IndexedStorageManager()
  val coll_engine = new StagedExecutionEngine(coll_storageManager, jo)
  val coll_program = Program(coll_engine)

  loadSchema(ddb_program, ddb_storageManager)
  ddb_storageManager.loadFacts(factDirectory)
  coll_program.loadFromFactDir(factDirectory)

  pretest(coll_program)
  pretest(ddb_program)

  @Benchmark def embedded_lambda_ddbn(blackhole: Blackhole): Unit = {
    blackhole.consume(
      ddb_program.namedRelation(toSolve).solve()
    )
  }
  @Benchmark def embedded_lambda_collidx(blackhole: Blackhole): Unit = {
    blackhole.consume(
      coll_program.namedRelation(toSolve).solve()
    )
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

  @Benchmark def zsouffle_preprofiled_compile(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-compile", blackhole)

  @Benchmark def zsouffle_preprofiled_interp(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-interp", blackhole)
}


@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
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
      loadSchema(program, duckDBStorageManager)
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

//  @Benchmark def warm_lambda_ddbidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
//    val storageManager = new DuckDBStorageManager(indexed = true)
//    val engine = new StagedExecutionEngine(storageManager, jo)
//    val mode = "lambda_ddbidx"
//    run_warm_carac(blackhole, mode, engine, storageManager)
//  }

  @Benchmark def warm_lambda_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbn"
    run_warm_carac(blackhole, mode, engine, storageManager)
  }
//  @Benchmark def warm_interp_ddbn(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val storageManager = new DuckDBStorageManager(indexed = false)
//    val engine = new StagedExecutionEngine(storageManager, jo)
//    val mode = "interp_ddbn"
//    run_warm_carac(blackhole, mode, engine, storageManager)
//  }
//
//  @Benchmark def warm_interp_ddbidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val storageManager = new DuckDBStorageManager(indexed = true)
//    val engine = new StagedExecutionEngine(storageManager, jo)
//    val mode = "interp_ddbidx"
//    run_warm_carac(blackhole, mode, engine, storageManager)
//  }

  @Benchmark def warm_lambda_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
    val mode = "lambda_collidx"
    run_warm_carac(blackhole, mode, engine, null)
  }

//  @Benchmark def warm_interp_collidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val engine = new StagedExecutionEngine(new IndexedStorageManager(), jo)
//    val mode = "interp_collidx"
//    run_warm_carac(blackhole, mode, engine, null)
//  }
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
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_cspa_embedded extends rqb_cspa {
  val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
  val ddb_storageManager = new DuckDBStorageManager(indexed = false)
  val ddb_engine = new StagedExecutionEngine(ddb_storageManager, jo)
  val ddb_program = Program(ddb_engine)
  val coll_storageManager = new IndexedStorageManager()
  val coll_engine = new StagedExecutionEngine(coll_storageManager, jo)
  val coll_program = Program(coll_engine)

  loadSchema(ddb_program, ddb_storageManager)
  ddb_storageManager.loadFacts(factDirectory)
  coll_program.loadFromFactDir(factDirectory)

  pretest(coll_program)
  pretest(ddb_program)

  @Benchmark def embedded_lambda_ddbn(blackhole: Blackhole): Unit = {
    blackhole.consume(
      ddb_program.namedRelation(toSolve).solve()
    )
  }
  @Benchmark def embedded_lambda_collidx(blackhole: Blackhole): Unit = {
    blackhole.consume(
      coll_program.namedRelation(toSolve).solve()
    )
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
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
    loadSchema(program, storage)
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

//  @Benchmark def warm_lambda_ddbidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
//    val storage = new DuckDBStorageManager(indexed = true)
//    val mode = "lambda_ddbidx"
//    run_warm_carac(blackhole, mode, storage, jo)
//  }

  @Benchmark def warm_lambda_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storage = new DuckDBStorageManager(indexed = false)
    val mode = "lambda_ddbn"
    run_warm_carac(blackhole, mode, storage, jo)
  }

//  @Benchmark def warm_interp_ddbn(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val storage = new DuckDBStorageManager(indexed = false)
//    val mode = "interp_ddbn"
//    run_warm_carac(blackhole, mode, storage, jo)
//  }
//
//  @Benchmark def warm_interp_ddbidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val storage = new DuckDBStorageManager(indexed = true)
//    val mode = "interp_ddbidx"
//    run_warm_carac(blackhole, mode, storage, jo)
//  }
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

  @Benchmark def zsouffle_preprofiled_compile(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-compile", blackhole)

  @Benchmark def zsouffle_preprofiled_interp(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-interp", blackhole)
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_ancestry_embedded extends rqb_ancestry {
  val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
  val ddb_storageManager = new DuckDBStorageManager(indexed = false)
  val ddb_engine = new StagedExecutionEngine(ddb_storageManager, jo)
  val ddb_program = Program(ddb_engine)

  loadSchema(ddb_program, ddb_storageManager)
  ddb_storageManager.loadFacts(factDirectory)

  pretest(ddb_program)

  @Benchmark def embedded_lambda_ddbn(blackhole: Blackhole): Unit = {
    blackhole.consume(
      ddb_storageManager.resultSetToString(ddb_storageManager.runQuery(sqlString))
    )
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
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
    loadSchema(program, storage)
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
//  @Benchmark def warm_lambda_ddbidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
//    val storage = new DuckDBStorageManager(indexed = true)
//    val mode = "lambda_ddbidx"
//    run_warm_carac(blackhole, mode, storage, jo)
//  }

  @Benchmark def warm_lambda_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storage = new DuckDBStorageManager(indexed = false)
    val mode = "lambda_ddbn"
    run_warm_carac(blackhole, mode, storage, jo)
  }

//  @Benchmark def warm_interp_ddbn(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val storage = new DuckDBStorageManager(indexed = false)
//    val mode = "interp_ddbn"
//    run_warm_carac(blackhole, mode, storage, jo)
//  }
//
//  @Benchmark def warm_interp_ddbidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val storage = new DuckDBStorageManager(indexed = true)
//    val mode = "interp_ddbidx"
//    run_warm_carac(blackhole, mode, storage, jo)
//  }
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

  @Benchmark def zsouffle_preprofiled_compile(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-compile", blackhole)

  @Benchmark def zsouffle_preprofiled_interp(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-interp", blackhole)
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_sssp_embedded extends rqb_sssp {
  val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
  val ddb_storageManager = new DuckDBStorageManager(indexed = false)
  val ddb_engine = new StagedExecutionEngine(ddb_storageManager, jo)
  val ddb_program = Program(ddb_engine)

  loadSchema(ddb_program, ddb_storageManager)
  ddb_storageManager.loadFacts(factDirectory)

  pretest(ddb_program)

  @Benchmark def embedded_lambda_ddbn(blackhole: Blackhole): Unit = {
    blackhole.consume(
      ddb_storageManager.resultSetToString(ddb_storageManager.runQuery(sqlString))
    )
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
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
    loadSchema(program, storage)
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
//  @Benchmark def warm_lambda_ddbidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
//    val storage = new DuckDBStorageManager(indexed = true)
//    val mode = "lambda_ddbidx"
//    run_warm_carac(blackhole, mode, storage, jo)
//  }

  @Benchmark def warm_lambda_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storage = new DuckDBStorageManager(indexed = false)
    val mode = "lambda_ddbn"
    run_warm_carac(blackhole, mode, storage, jo)
  }

//  @Benchmark def warm_interp_ddbn(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val storage = new DuckDBStorageManager(indexed = false)
//    val mode = "interp_ddbn"
//    run_warm_carac(blackhole, mode, storage, jo)
//  }
//
//  @Benchmark def warm_interp_ddbidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.Interpreted, sortOrder = SortOrder.Sel)
//    val storage = new DuckDBStorageManager(indexed = true)
//    val mode = "interp_ddbidx"
//    run_warm_carac(blackhole, mode, storage, jo)
//  }
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

  @Benchmark def zsouffle_preprofiled_compile(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-compile", blackhole)

  @Benchmark def zsouffle_preprofiled_interp(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-interp", blackhole)
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_bom_embedded() extends rqb_bom {
  val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
  val ddb_storageManager = new DuckDBStorageManager(indexed = false)
  val ddb_engine = new StagedExecutionEngine(ddb_storageManager, jo)
  val ddb_program = Program(ddb_engine)

  loadSchema(ddb_program, ddb_storageManager)
  ddb_storageManager.loadFacts(factDirectory)

  pretest(ddb_program)

  @Benchmark def embedded_lambda_ddbn(blackhole: Blackhole): Unit = {
    blackhole.consume(
      ddb_storageManager.resultSetToString(ddb_storageManager.runQuery(sqlString))
    )
  }
}