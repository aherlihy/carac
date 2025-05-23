package carac.benchmarks

import carac.dsl.*
import carac.execution.{Backend, CompileSync, ExecutionEngine, Granularity, JITOptions, SortOrder, StagedExecutionEngine, TyQLExecutionEngine, Mode as CaracMode}
import carac.storage.{DatabaseType, DuckDBStorageManager, IndexedStorageManager}
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.rqb_andersen.rqb_andersen
import test.examples.rqb_cba.rqb_cba
import test.examples.rqb_cspa.rqb_cspa
import test.examples.rqb_ancestry.rqb_ancestry
import test.examples.rqb_sssp.rqb_sssp
import test.examples.rqb_bom.rqb_bom
import tyql.{DatabaseAST, QueryIRNode}

import java.nio.file.{Files, Paths}
import java.util.concurrent.TimeUnit
import scala.sys.process.Process
import scala.util.Using

val SOUFFLE_BIN="/scratch/herlihy/souffle/build/src/souffle"
val COLL_HEURISTIC = 0.01

object RQB_Bench {
  def cleanup(benchmark: String, linear: Boolean): Unit = {
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
    Process(s"mkdir -p  carac-scala-out/$benchmark/lambda_ddbn_tyql").!
    Process(s"mkdir -p  carac-scala-out/$benchmark/lambda_divert_tyql").!
    if linear then
      Process(s"mkdir -p  carac-scala-out/$benchmark/lambda_ddbn_sqlstr").!
    else
      Process(s"mkdir -p  carac-scala-out/$benchmark/lambda_collidx_tyql").!
      Process(s"mkdir -p  carac-scala-out/$benchmark/lambda_collidx_carac").!
      Process(s"mkdir -p  carac-scala-out/$benchmark/lambda_ddbn_carac").!
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 0, batchSize = 1)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_andersen_souffle extends rqb_andersen {
  val factDirectory = s"$directory/facts"
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)

  RQB_Bench.cleanup(benchmark, linear)
  val exit = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, "generate-preprofile")).!
  if (exit != 0) throw new Exception(s"Souffle preprofiling failed with code $exit")

  private def run_souffle(mode: String, blackhole: Blackhole): Unit = {
    val pb = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, mode))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle $mode failed with code $exitCode")
    blackhole.consume(exitCode)
  }
  @Benchmark def souffle_compile(blackhole: Blackhole): Unit =
    run_souffle("compile", blackhole)

  @Benchmark def souffle_interp(blackhole: Blackhole): Unit =
    run_souffle("interp", blackhole)

  @Benchmark def souffle_profilecompile(blackhole: Blackhole): Unit =
    run_souffle("profile-compile", blackhole)

  @Benchmark def souffle_profileinterp(blackhole: Blackhole): Unit =
    run_souffle("profile-interp", blackhole)

  @Benchmark def souffle_preprofiledcompile(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-compile", blackhole)

  @Benchmark def souffle_preprofiledinterp(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-interp", blackhole)
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_andersen_caql_warm extends rqb_andersen {
  val factDirectory = s"$directory/facts"
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)

  RQB_Bench.cleanup(benchmark, linear)

  private def run_warm_tyql(blackhole: Blackhole, mode: String, engine: TyQLExecutionEngine, queryAST: Option[DatabaseAST[?]] = None, queryIR: Option[QueryIRNode] = None): Unit = {
    val program = Program(engine)
    loadDataFromFile(program, directory)
    val query = queryAST.getOrElse(generateTyQL())
    val result = engine.solveTyQL(query, tyqlIRTree = queryIR)
    blackhole.consume(
      result
    )
    val path = Paths.get("carac-scala-out", benchmark, mode, toSolve + ".csv")
    Using(Files.newBufferedWriter(path)) { writer =>
      result.foreach(f => writer.write(f.mkString("", "\t", "\n")))
    }
    engine.storageManager.cleanup()
  }

  private def run_warm_carac(blackhole: Blackhole, mode: String, engine: StagedExecutionEngine): Unit = {
    val program = Program(engine)
    loadDataFromFile(program, directory)
    val query = generateCarac(program)
    blackhole.consume(
      query.solve()
    )
    val idb = engine.storageManager.ns(query.id)
    val path = Paths.get("carac-scala-out", benchmark, mode, idb + ".csv")
    Using(Files.newBufferedWriter(path)) { writer =>
      engine.get(idb).foreach(f => writer.write(f.mkString("", "\t", "\n")))
    }
    engine.storageManager.cleanup()
  }

  @Benchmark def tyql_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new TyQLExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbn_tyql"
    run_warm_tyql(blackhole, mode, engine)
  }

  @Benchmark def tyql_divert(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val query = generateTyQL()
    val queryIR = query.toQueryIR
    val storageManager = if skipDBIntegration(COLL_HEURISTIC, queryIR) then
      new IndexedStorageManager() else
      new DuckDBStorageManager(indexed = false)
    val engine = new TyQLExecutionEngine(storageManager, jo)
    val mode = "lambda_divert_tyql"
    run_warm_tyql(blackhole, mode, engine, Some(query), Some(queryIR))
  }

  @Benchmark def carac_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbn_carac"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def tyql_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new IndexedStorageManager()
    val engine = new TyQLExecutionEngine(storageManager, jo)
    val mode = "lambda_collidx_tyql"
    run_warm_tyql(blackhole, mode, engine)
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_andersen_caql_embedded() extends rqb_andersen {
  val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)

  val ddb_storageManager_tyql = new DuckDBStorageManager(indexed = false)
  val ddb_engine_tyql = new TyQLExecutionEngine(ddb_storageManager_tyql, jo)
  val ddb_program_tyql = Program(ddb_engine_tyql)

  val ddb_storageManager_tyql2 = new DuckDBStorageManager(indexed = false)
  val ddb_engine_tyql2 = new TyQLExecutionEngine(ddb_storageManager_tyql2, jo)
  val ddb_program_tyql2 = Program(ddb_engine_tyql2)

  val ddb_storageManager_carac = new DuckDBStorageManager(indexed = false)
  val ddb_engine_carac = new StagedExecutionEngine(ddb_storageManager_carac, jo)
  val ddb_program_carac = Program(ddb_engine_carac)

  val coll_storageManager = new IndexedStorageManager()
  val coll_engine = new TyQLExecutionEngine(coll_storageManager, jo)
  val coll_program_tyql = Program(coll_engine)

  loadDataFromFile(ddb_program_tyql, directory)
  loadDataFromFile(ddb_program_carac, directory)
  loadDataFromFile(coll_program_tyql, directory)
  loadDataFromFile(ddb_program_tyql2, directory)

  val tyqlQuery = generateTyQL()
  val caracQueryColl = generateCarac(coll_program_tyql)
  val caracQueryDDb = generateCarac(ddb_program_carac)

  @Benchmark def tyql_ddbn(blackhole: Blackhole): Unit = {
    val query = generateTyQL()
    blackhole.consume(
      ddb_engine_tyql2.solveTyQL(query)
    )
  }

//  @Benchmark def tyqlNQ_ddbn(blackhole: Blackhole): Unit = {
//    blackhole.consume(
//      ddb_engine_tyql.solveTyQL(tyqlQuery)
//    )
//  }

  @Benchmark def tyql_collidx(blackhole: Blackhole): Unit = {
    val query = generateTyQL()
    blackhole.consume(
      coll_engine.solveTyQL(query)
    )
  }

  @Benchmark def carac_ddbn(blackhole: Blackhole): Unit = {
    blackhole.consume(
      caracQueryDDb.solve()
    )
  }
  @Benchmark def tyql_divert(blackhole: Blackhole): Unit = {
    val query = generateTyQL()
    val ir = query.toQueryIR
    if skipDBIntegration(COLL_HEURISTIC, ir) then
      coll_engine.solveTyQL(query, tyqlIRTree = Some(ir)) else
      ddb_engine_tyql2.solveTyQL(query, tyqlIRTree = Some(ir))
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 0, batchSize = 1)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_cba_souffle extends rqb_cba {
  val factDirectory = s"$directory/facts"
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)

  RQB_Bench.cleanup(benchmark, linear)
  val exit = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, "generate-preprofile")).!
  if (exit != 0) throw new Exception(s"Souffle preprofiling failed with code $exit")

  private def run_souffle(mode: String, blackhole: Blackhole): Unit = {
    val pb = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, mode))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle $mode failed with code $exitCode")
    blackhole.consume(exitCode)
  }

  @Benchmark def souffle_compile(blackhole: Blackhole): Unit =
    run_souffle("compile", blackhole)

  @Benchmark def souffle_interp(blackhole: Blackhole): Unit =
    run_souffle("interp", blackhole)

  @Benchmark def souffle_profilecompile(blackhole: Blackhole): Unit =
    run_souffle("profile-compile", blackhole)

  @Benchmark def souffle_profileinterp(blackhole: Blackhole): Unit =
    run_souffle("profile-interp", blackhole)

  @Benchmark def souffle_preprofiledcompile(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-compile", blackhole)

  @Benchmark def souffle_preprofiledinterp(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-interp", blackhole)
}


@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_cba_caql_warm extends rqb_cba {
  val factDirectory = s"$directory/facts"
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)

  RQB_Bench.cleanup(benchmark, linear)

  private def run_warm_tyql(blackhole: Blackhole, mode: String, engine: TyQLExecutionEngine, queryAST: Option[DatabaseAST[?]] = None, queryIR: Option[QueryIRNode] = None): Unit = {
    val program = Program(engine)
    loadDataFromFile(program, directory)
    val query = queryAST.getOrElse(generateTyQL())
    val result = engine.solveTyQL(query, tyqlIRTree = queryIR)
    blackhole.consume(
      result
    )
    val path = Paths.get("carac-scala-out", benchmark, mode, toSolve + ".csv")
    Using(Files.newBufferedWriter(path)) { writer =>
      result.foreach(f => writer.write(f.mkString("", "\t", "\n")))
    }
    engine.storageManager.cleanup()
  }

  private def run_warm_carac(blackhole: Blackhole, mode: String, engine: StagedExecutionEngine): Unit = {
    val program = Program(engine)
    loadDataFromFile(program, directory)
    val query = generateCarac(program)
    blackhole.consume(
      query.solve()
    )
    val idb = engine.storageManager.ns(query.id)
    val path = Paths.get("carac-scala-out", benchmark, mode, idb + ".csv")
    Using(Files.newBufferedWriter(path)) { writer =>
      engine.get(idb).foreach(f => writer.write(f.mkString("", "\t", "\n")))
    }
    engine.storageManager.cleanup()
  }

  @Benchmark def tyql_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new TyQLExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbn_tyql"
    run_warm_tyql(blackhole, mode, engine)
  }

  @Benchmark def carac_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbn_carac"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def carac_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new IndexedStorageManager()
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "lambda_collidx_carac"
    run_warm_carac(blackhole, mode, engine)
  }

  @Benchmark def tyql_collidx(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new IndexedStorageManager()
    val engine = new TyQLExecutionEngine(storageManager, jo)
    val mode = "lambda_collidx_tyql"
    run_warm_tyql(blackhole, mode, engine)
  }

  @Benchmark def tyql_divert(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val query = generateTyQL()
    val queryIR = query.toQueryIR
    val storageManager = if skipDBIntegration(COLL_HEURISTIC, queryIR) then
      new IndexedStorageManager() else
      new DuckDBStorageManager(indexed = false)
    val engine = new TyQLExecutionEngine(storageManager, jo)
    val mode = "lambda_divert_tyql"
    run_warm_tyql(blackhole, mode, engine, Some(query), Some(queryIR))
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_cba_caql_embedded() extends rqb_cba {
  val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)

  val ddb_storageManager_tyql = new DuckDBStorageManager(indexed = false)
  val ddb_engine_tyql = new TyQLExecutionEngine(ddb_storageManager_tyql, jo)
  val ddb_program_tyql = Program(ddb_engine_tyql)

  val ddb_storageManager_tyql2 = new DuckDBStorageManager(indexed = false)
  val ddb_engine_tyql2 = new TyQLExecutionEngine(ddb_storageManager_tyql2, jo)
  val ddb_program_tyql2 = Program(ddb_engine_tyql2)


  val ddb_storageManager_carac = new DuckDBStorageManager(indexed = false)
  val ddb_engine_carac = new StagedExecutionEngine(ddb_storageManager_carac, jo)
  val ddb_program_carac = Program(ddb_engine_carac)

  val coll_storageManager_carac = new IndexedStorageManager()
  val coll_engine_carac = new StagedExecutionEngine(coll_storageManager_carac, jo)
  val coll_program_carac = Program(coll_engine_carac)

  val coll_storageManager_tyql = new IndexedStorageManager()
  val coll_engine_tyql = new TyQLExecutionEngine(coll_storageManager_tyql, jo)
  val coll_program_tyql = Program(coll_engine_tyql)

  loadDataFromFile(ddb_program_tyql, directory)
  loadDataFromFile(ddb_program_carac, directory)
  loadDataFromFile(coll_program_carac, directory)
  loadDataFromFile(coll_program_tyql, directory)
  loadDataFromFile(ddb_program_tyql2, directory)

  val tyqlQueryDDB = generateTyQL()
  val tyqlQueryColl = generateTyQL()
  val caracQueryColl = generateCarac(coll_program_carac)
  val caracQueryDDb = generateCarac(ddb_program_carac)

  @Benchmark def tyql_ddbn(blackhole: Blackhole): Unit = {
    val query = generateTyQL()
    blackhole.consume(
      ddb_engine_tyql2.solveTyQL(query)
    )
  }

//  @Benchmark def tyqlNQ_ddbn(blackhole: Blackhole): Unit = {
//    blackhole.consume(
//      ddb_engine_tyql.solveTyQL(tyqlQueryDDB)
//    )
//  }

  @Benchmark def tyql_collidx(blackhole: Blackhole): Unit = {
    blackhole.consume(
      coll_engine_tyql.solveTyQL(tyqlQueryColl)
    )
  }

  @Benchmark def carac_collidx(blackhole: Blackhole): Unit = {
    blackhole.consume(
      caracQueryColl.solve()
    )
  }

  @Benchmark def carac_ddbn(blackhole: Blackhole): Unit = {
    blackhole.consume(
      caracQueryDDb.solve()
    )
  }
  @Benchmark def tyql_divert(blackhole: Blackhole): Unit = {
    val query = generateTyQL()
    val ir = query.toQueryIR
    if skipDBIntegration(COLL_HEURISTIC, ir) then
      coll_engine_tyql.solveTyQL(query, tyqlIRTree = Some(ir)) else
      ddb_engine_tyql2.solveTyQL(query, tyqlIRTree = Some(ir))
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 0, batchSize = 1)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_ancestry_souffle extends rqb_ancestry {
  val factDirectory = s"$directory/facts"
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)

  RQB_Bench.cleanup(benchmark, linear)
  val exit = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, "generate-preprofile")).!
  if (exit != 0) throw new Exception(s"Souffle preprofiling failed with code $exit")

  private def run_souffle(mode: String, blackhole: Blackhole): Unit = {
    val pb = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, mode))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle $mode failed with code $exitCode")
    blackhole.consume(exitCode)
  }

  @Benchmark def souffle_compile(blackhole: Blackhole): Unit =
    run_souffle("compile", blackhole)

  @Benchmark def souffle_interp(blackhole: Blackhole): Unit =
    run_souffle("interp", blackhole)

  @Benchmark def souffle_profilecompile(blackhole: Blackhole): Unit =
    run_souffle("profile-compile", blackhole)

  @Benchmark def souffle_profileinterp(blackhole: Blackhole): Unit =
    run_souffle("profile-interp", blackhole)

  @Benchmark def souffle_preprofiledcompile(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-compile", blackhole)

  @Benchmark def souffle_preprofiledinterp(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-interp", blackhole)
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_ancestry_caql_warm extends rqb_ancestry {
  val factDirectory = s"$directory/facts"
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)

  RQB_Bench.cleanup(benchmark, linear)

  private def run_warm_tyql(blackhole: Blackhole, mode: String, engine: TyQLExecutionEngine, queryAST: Option[DatabaseAST[?]] = None, queryIR: Option[QueryIRNode] = None): Unit = {
    val program = Program(engine)
    loadDataFromFile(program, directory)
    val query = queryAST.getOrElse(generateTyQL())
    val result = engine.solveTyQL(query, tyqlIRTree = queryIR)
    blackhole.consume(
      result
    )
    val path = Paths.get("carac-scala-out", benchmark, mode, toSolve + ".csv")
    Using(Files.newBufferedWriter(path)) { writer =>
      result.foreach(f => writer.write(f.mkString("", "\t", "\n")))
    }
    engine.storageManager.cleanup()
  }

//  private def run_warm_carac(blackhole: Blackhole, mode: String, engine: StagedExecutionEngine, duckDBStorageManager: DuckDBStorageManager): Unit = {
//val program = Program(engine)
//  loadDataFromFile(program, directory)
//  val query = generateCarac(program)
//  blackhole.consume(
//    query.solve()
//  )
//  val idb = engine.storageManager.ns(query.id)
//  val path = Paths.get("carac-scala-out", benchmark, mode, idb + ".csv")
//  Using(Files.newBufferedWriter(path)) { writer =>
//    engine.get(idb).foreach(f => writer.write(f.mkString("", "\t", "\n")))
//  }
//  engine.storageManager.cleanup()
//}

  private def run_warm_sqlstr(blackhole: Blackhole, mode: String, storage: DuckDBStorageManager, options: JITOptions): Unit = {
    val engine = StagedExecutionEngine(storage, options)
    val program = Program(engine)
    loadDataFromFile(program, directory)
    val result = storage.resultSetToString(storage.runQuery(sqlString))
    blackhole.consume(result)
    val path = Paths.get("carac-scala-out", benchmark, mode, toSolve + ".csv")
    Using(Files.newBufferedWriter(path)) { writer =>
      writer.write(result)
    }
    engine.storageManager.cleanup()
  }

  @Benchmark def tyql_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new TyQLExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbn_tyql"
    run_warm_tyql(blackhole, mode, engine)
  }

//  @Benchmark def carac_ddbn(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
//    val storageManager = new DuckDBStorageManager(indexed = false)
//    val engine = new StagedExecutionEngine(storageManager, jo)
//    val mode = "lambda_ddbn_carac"
//    run_warm_carac(blackhole, mode, engine, storageManager)
//  }
//
//  @Benchmark def carac_collidx(blackhole: Blackhole): Unit = {
//    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
//    val storageManager = new IndexedStorageManager()
//    val engine = new StagedExecutionEngine(storageManager, jo)
//    val mode = "lambda_collidx_carac"
//    run_warm_carac(blackhole, mode, engine, storageManager)
//  }

  @Benchmark def sqlstr_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbn_sqlstr"
    run_warm_sqlstr(blackhole, mode, storageManager, jo)
  }

  @Benchmark def tyql_divert(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val query = generateTyQL()
    val queryIR = query.toQueryIR
    val storageManager = if skipDBIntegration(COLL_HEURISTIC, queryIR) then
      ??? else
      new DuckDBStorageManager(indexed = false)
    val engine = new TyQLExecutionEngine(storageManager, jo)
    val mode = "lambda_divert_tyql"
    run_warm_tyql(blackhole, mode, engine, Some(query), Some(queryIR))
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_ancestry_caql_embedded() extends rqb_ancestry {
  val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)

  val ddb_storageManager_tyql = new DuckDBStorageManager(indexed = false)
  val ddb_engine_tyql = new TyQLExecutionEngine(ddb_storageManager_tyql, jo)
  val ddb_program_tyql = Program(ddb_engine_tyql)

  val ddb_storageManager_tyql2 = new DuckDBStorageManager(indexed = false)
  val ddb_engine_tyql2 = new TyQLExecutionEngine(ddb_storageManager_tyql2, jo)
  val ddb_program_tyql2 = Program(ddb_engine_tyql2)

  val ddb_storageManager_tyql3 = new DuckDBStorageManager(indexed = false)
  val ddb_engine_tyql3 = new TyQLExecutionEngine(ddb_storageManager_tyql3, jo)
  val ddb_program_tyql3 = Program(ddb_engine_tyql3)

  //  val ddb_storageManager_carac = new DuckDBStorageManager(indexed = false)
//  val ddb_engine_carac = new StagedExecutionEngine(ddb_storageManager_carac, jo)
//  val ddb_program_carac = Program(ddb_engine_carac)
//
//  val coll_storageManager = new IndexedStorageManager()
//  val coll_engine = new StagedExecutionEngine(coll_storageManager, jo)
//  val coll_program_carac = Program(coll_engine)

  loadDataFromFile(ddb_program_tyql, directory)
  loadDataFromFile(ddb_program_tyql2, directory)
  loadDataFromFile(ddb_program_tyql3, directory)
//  loadDataFromFile(ddb_program_carac, directory)
//  loadDataFromFile(coll_program_carac, directory)

  val tyqlQuery = generateTyQL()
//  val caracQueryColl = generateCarac(coll_program_carac)
//  val caracQueryDDb = generateCarac(ddb_program_carac)

//  @Benchmark def tyqlNQ_ddbn(blackhole: Blackhole): Unit = {
//    blackhole.consume(
//      ddb_engine_tyql.solveTyQL(tyqlQuery)
//    )
//  }

  @Benchmark def tyql_ddbn(blackhole: Blackhole): Unit = {
    val query = generateTyQL()
    blackhole.consume(
      ddb_engine_tyql2.solveTyQL(query)
    )
  }

  @Benchmark def sqlstr_ddbn(blackhole: Blackhole): Unit = {
    blackhole.consume(
      ddb_storageManager_tyql3.resultSetToString(ddb_storageManager_tyql3.runQuery(sqlString))
    )
  }
  @Benchmark def tyql_divert(blackhole: Blackhole): Unit = {
    val query = generateTyQL()
    val ir = query.toQueryIR
    if skipDBIntegration(COLL_HEURISTIC, ir) then
      ??? else
      ddb_engine_tyql2.solveTyQL(query, tyqlIRTree = Some(ir))
  }

//  @Benchmark def carac_collidx(blackhole: Blackhole): Unit = {
//    blackhole.consume(
//      caracQueryColl.solve()
//    )
//  }
//
//  @Benchmark def carac_ddbn(blackhole: Blackhole): Unit = {
//    blackhole.consume(
//      caracQueryDDb.solve()
//    )
//  }
}


@Fork(1) // # of jvms that it will use
@Warmup(iterations = 0, batchSize = 1)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_sssp_souffle extends rqb_sssp {
  val factDirectory = s"$directory/facts"
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)

  RQB_Bench.cleanup(benchmark, linear)
  val exit = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, "generate-preprofile")).!
  if (exit != 0) throw new Exception(s"Souffle preprofiling failed with code $exit")

  private def run_souffle(mode: String, blackhole: Blackhole): Unit = {
    val pb = Process(Seq("src/test/scala/carac/benchmarks/souffle/souffle-driver.sh", SOUFFLE_BIN, benchmark, mode))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle $mode failed with code $exitCode")
    blackhole.consume(exitCode)
  }

  @Benchmark def souffle_compile(blackhole: Blackhole): Unit =
    run_souffle("compile", blackhole)

  @Benchmark def souffle_interp(blackhole: Blackhole): Unit =
    run_souffle("interp", blackhole)

  @Benchmark def souffle_profilecompile(blackhole: Blackhole): Unit =
    run_souffle("profile-compile", blackhole)

  @Benchmark def souffle_profileinterp(blackhole: Blackhole): Unit =
    run_souffle("profile-interp", blackhole)

  @Benchmark def souffle_preprofiledcompile(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-compile", blackhole)

  @Benchmark def souffle_preprofiledinterp(blackhole: Blackhole): Unit =
    run_souffle("preprofiled-interp", blackhole)
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_sssp_caql_warm extends rqb_sssp {
  val factDirectory = s"$directory/facts"
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)

  RQB_Bench.cleanup(benchmark, linear)

  private def run_warm_tyql(blackhole: Blackhole, mode: String, engine: TyQLExecutionEngine, queryAST: Option[DatabaseAST[?]] = None, queryIR: Option[QueryIRNode] = None): Unit = {
    val program = Program(engine)
    loadDataFromFile(program, directory)
    val query = queryAST.getOrElse(generateTyQL())
    val result = engine.solveTyQL(query, tyqlIRTree = queryIR)
    blackhole.consume(
      result
    )
    val path = Paths.get("carac-scala-out", benchmark, mode, toSolve + ".csv")
    Using(Files.newBufferedWriter(path)) { writer =>
      result.foreach(f => writer.write(f.mkString("", "\t", "\n")))
    }
    engine.storageManager.cleanup()
  }

  //  private def run_warm_carac(blackhole: Blackhole, mode: String, engine: StagedExecutionEngine, duckDBStorageManager: DuckDBStorageManager): Unit = {
//  val program = Program(engine)
//  loadDataFromFile(program, directory)
//  val query = generateCarac(program)
//  blackhole.consume(
//    query.solve()
//  )
//  val idb = engine.storageManager.ns(query.id)
//  val path = Paths.get("carac-scala-out", benchmark, mode, idb + ".csv")
//  Using(Files.newBufferedWriter(path)) { writer =>
//    engine.get(idb).foreach(f => writer.write(f.mkString("", "\t", "\n")))
//  }
//  engine.storageManager.cleanup()
//}

  private def run_warm_sqlstr(blackhole: Blackhole, mode: String, storage: DuckDBStorageManager, options: JITOptions): Unit = {
    val engine = StagedExecutionEngine(storage, options)
    val program = Program(engine)
    loadDataFromFile(program, directory)
    val result = storage.resultSetToString(storage.runQuery(sqlString))
    blackhole.consume(result)
    val path = Paths.get("carac-scala-out", benchmark, mode, toSolve + ".csv")
    Using(Files.newBufferedWriter(path)) { writer =>
      writer.write(result)
    }
    engine.storageManager.cleanup()
  }

  @Benchmark def tyql_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new TyQLExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbn_tyql"
    run_warm_tyql(blackhole, mode, engine)
  }

  //  @Benchmark def carac_ddbn(blackhole: Blackhole): Unit = {
  //    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
  //    val storageManager = new DuckDBStorageManager(indexed = false)
  //    val engine = new StagedExecutionEngine(storageManager, jo)
  //    val mode = "lambda_ddbn_carac"
  //    run_warm_carac(blackhole, mode, engine, storageManager)
  //  }
  //
  //  @Benchmark def carac_collidx(blackhole: Blackhole): Unit = {
  //    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
  //    val storageManager = new IndexedStorageManager()
  //    val engine = new StagedExecutionEngine(storageManager, jo)
  //    val mode = "lambda_collidx_carac"
  //    run_warm_carac(blackhole, mode, engine, storageManager)
  //  }

  @Benchmark def sqlstr_ddbn(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val storageManager = new DuckDBStorageManager(indexed = false)
    val engine = new StagedExecutionEngine(storageManager, jo)
    val mode = "lambda_ddbn_sqlstr"
    run_warm_sqlstr(blackhole, mode, storageManager, jo)
  }

  @Benchmark def tyql_divert(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val query = generateTyQL()
    val queryIR = query.toQueryIR
    val storageManager = if skipDBIntegration(COLL_HEURISTIC, queryIR) then
      ??? else
      new DuckDBStorageManager(indexed = false)
    val engine = new TyQLExecutionEngine(storageManager, jo)
    val mode = "lambda_divert_tyql"
    run_warm_tyql(blackhole, mode, engine, Some(query), Some(queryIR))
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchRQB_sssp_caql_embedded() extends rqb_sssp {
  val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)

  val ddb_storageManager_tyql = new DuckDBStorageManager(indexed = false)
  val ddb_engine_tyql = new TyQLExecutionEngine(ddb_storageManager_tyql, jo)
  val ddb_program_tyql = Program(ddb_engine_tyql)

  val ddb_storageManager_tyql2 = new DuckDBStorageManager(indexed = false)
  val ddb_engine_tyql2 = new TyQLExecutionEngine(ddb_storageManager_tyql2, jo)
  val ddb_program_tyql2 = Program(ddb_engine_tyql2)

  val ddb_storageManager_tyql3 = new DuckDBStorageManager(indexed = false)
  val ddb_engine_tyql3 = new TyQLExecutionEngine(ddb_storageManager_tyql3, jo)
  val ddb_program_tyql3 = Program(ddb_engine_tyql3)

  //  val ddb_storageManager_carac = new DuckDBStorageManager(indexed = false)
  //  val ddb_engine_carac = new StagedExecutionEngine(ddb_storageManager_carac, jo)
  //  val ddb_program_carac = Program(ddb_engine_carac)
  //
  //  val coll_storageManager = new IndexedStorageManager()
  //  val coll_engine = new StagedExecutionEngine(coll_storageManager, jo)
  //  val coll_program_carac = Program(coll_engine)

  loadDataFromFile(ddb_program_tyql, directory)
  loadDataFromFile(ddb_program_tyql2, directory)
  loadDataFromFile(ddb_program_tyql3, directory)
  //  loadDataFromFile(ddb_program_carac, directory)
  //  loadDataFromFile(coll_program_carac, directory)

  val tyqlQuery = generateTyQL()
  //  val caracQueryColl = generateCarac(coll_program_carac)
  //  val caracQueryDDb = generateCarac(ddb_program_carac)

//  @Benchmark def tyqlNQ_ddbn(blackhole: Blackhole): Unit = {
//    blackhole.consume(
//      ddb_engine_tyql.solveTyQL(tyqlQuery)
//    )
//  }

  @Benchmark def tyql_ddbn(blackhole: Blackhole): Unit = {
    val query = generateTyQL()
    blackhole.consume(
      ddb_engine_tyql2.solveTyQL(query)
    )
  }

  @Benchmark def sqlstr_ddbn(blackhole: Blackhole): Unit = {
    blackhole.consume(
      ddb_storageManager_tyql3.resultSetToString(ddb_storageManager_tyql3.runQuery(sqlString))
    )
  }
  @Benchmark def tyql_divert(blackhole: Blackhole): Unit = {
    val query = generateTyQL()
    val ir = query.toQueryIR
    if skipDBIntegration(COLL_HEURISTIC, ir) then
      ??? else
      ddb_engine_tyql2.solveTyQL(query, tyqlIRTree = Some(ir))
  }


  //  @Benchmark def carac_collidx(blackhole: Blackhole): Unit = {
  //    blackhole.consume(
  //      caracQueryColl.solve()
  //    )
  //  }
  //
  //  @Benchmark def carac_ddbn(blackhole: Blackhole): Unit = {
  //    blackhole.consume(
  //      caracQueryDDb.solve()
  //    )
  //  }
}