package carac.benchmarks

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import carac.dsl.*
import carac.execution.{Backend, CompileSync, ExecutionEngine, Granularity, JITOptions, NaiveShallowExecutionEngine, ShallowExecutionEngine, SortOrder, StagedExecutionEngine, ir, Mode as CaracMode}
import carac.storage.CollectionsStorageManager
import test.examples.tastyslistlibinverse.tastyslistlibinverse_worst
import test.examples.tastyslistlib.tastyslistlib_worst
import test.examples.ackermann.ackermann_worst
import test.examples.equal.equal_worst
import test.examples.fib.fib_worst
import test.examples.cbaexprvalue.cbaexprvalue_worst
import test.examples.prime.prime_worst

import java.nio.file.{FileSystems, Files, Path, Paths}
import scala.util.Using
import scala.collection.immutable.Map
import scala.concurrent.duration.Duration
import scala.quoted.staging

import scala.sys.process.Process

/**
 * Benchmarks that are run on all modes
 */
@Fork(1) // # of jvms that it will use
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchCLI_tastyslistlibinverse extends tastyslistlibinverse_worst {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null
  val dotty = staging.Compiler.make(getClass.getClassLoader)

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

  @Benchmark def carac_warm_quotes(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Quotes)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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

  @Benchmark def carac_warm_bytecode(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Bytecode)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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


  @Benchmark def souffle__compile(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-compile.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle__interp(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-interp.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle_profile_compile(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-profile-compile.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle_profile_interp(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-profile-interp.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
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

  @Benchmark def carac_jar_quotes(blackhole: Blackhole): Unit = {
    val b = "quotes"
    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchCLI_tastyslistlib extends tastyslistlib_worst {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null
  val dotty = staging.Compiler.make(getClass.getClassLoader)

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

  @Benchmark def carac_warm_quotes(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Quotes)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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

  @Benchmark def carac_warm_bytecode(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Bytecode)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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


  @Benchmark def souffle__compile(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-compile.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle__interp(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-interp.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle_profile_compile(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-profile-compile.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle_profile_interp(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-profile-interp.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
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

  @Benchmark def carac_jar_quotes(blackhole: Blackhole): Unit = {
    val b = "quotes"
    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchCLI_ackermann extends ackermann_worst {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null
  val dotty = staging.Compiler.make(getClass.getClassLoader)

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

  @Benchmark def carac_warm_quotes(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Quotes)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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

  @Benchmark def carac_warm_bytecode(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Bytecode)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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


  @Benchmark def souffle__compile(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-compile.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle__interp(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-interp.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle_profile_compile(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-profile-compile.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle_profile_interp(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-profile-interp.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
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

  @Benchmark def carac_jar_quotes(blackhole: Blackhole): Unit = {
    val b = "quotes"
    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchCLI_cbaexprvalue extends cbaexprvalue_worst {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null
  val dotty = staging.Compiler.make(getClass.getClassLoader)

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

  @Benchmark def carac_warm_quotes(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Quotes)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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

  @Benchmark def carac_warm_bytecode(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Bytecode)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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


  @Benchmark def souffle__compile(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-compile.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle__interp(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-interp.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle_profile_compile(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-profile-compile.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle_profile_interp(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-profile-interp.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
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

  @Benchmark def carac_jar_quotes(blackhole: Blackhole): Unit = {
    val b = "quotes"
    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchCLI_equal extends equal_worst {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null
  val dotty = staging.Compiler.make(getClass.getClassLoader)

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

  @Benchmark def carac_warm_quotes(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Quotes)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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

  @Benchmark def carac_warm_bytecode(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Bytecode)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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


  @Benchmark def souffle__compile(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-compile.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle__interp(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-interp.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle_profile_compile(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-profile-compile.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle_profile_interp(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-profile-interp.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
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

  @Benchmark def carac_jar_quotes(blackhole: Blackhole): Unit = {
    val b = "quotes"
    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchCLI_prime extends prime_worst {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null
  val dotty = staging.Compiler.make(getClass.getClassLoader)

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

  @Benchmark def carac_warm_quotes(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Quotes)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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

  @Benchmark def carac_warm_bytecode(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Bytecode)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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


  @Benchmark def souffle__compile(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-compile.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle__interp(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-interp.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle_profile_compile(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-profile-compile.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle_profile_interp(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-profile-interp.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
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

  @Benchmark def carac_jar_quotes(blackhole: Blackhole): Unit = {
    val b = "quotes"
    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }
}

@Fork(1) // # of jvms that it will use
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
@BenchmarkMode(Array(Mode.AverageTime))
class BenchCLI_fib extends fib_worst {
  val pattern = """.*examples/(.*?)/facts.*""".r
  val benchmark = pattern.findFirstMatchIn(factDirectory).get.group(1)
  var directory = null
  val dotty = staging.Compiler.make(getClass.getClassLoader)

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

  @Benchmark def carac_warm_quotes(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Quotes)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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

  @Benchmark def carac_warm_bytecode(blackhole: Blackhole): Unit = {
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Bytecode)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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
    val jo = JITOptions(mode = CaracMode.JIT, granularity = Granularity.DELTA, dotty = dotty, compileSync = CompileSync.Blocking, sortOrder = SortOrder.Sel, backend = Backend.Lambda)
    val engine = new StagedExecutionEngine(new CollectionsStorageManager(), jo)
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


  @Benchmark def souffle__compile(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-compile.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle__interp(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-interp.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle_profile_compile(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-profile-compile.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }

  @Benchmark def souffle_profile_interp(blackhole: Blackhole): Unit = {
    val pb = Process(Seq(s"src/test/scala/carac/benchmarks/run-cli/souffle-profile-interp.sh", benchmark))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Souffle exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
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

  @Benchmark def carac_jar_quotes(blackhole: Blackhole): Unit = {
    val b = "quotes"
    val pb = Process(Seq(s"../target/pack/bin/main", benchmark, b))
    val exitCode = pb.!
    if (exitCode != 0) throw new Exception(s"Carac $benchmark and $b exited with code $exitCode")
    blackhole.consume(exitCode) // prob unnecessary
  }
}
