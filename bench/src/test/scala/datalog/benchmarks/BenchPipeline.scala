package datalog.benchmarks

import java.nio.file.{FileSystems, Files, Path, Paths}
import java.util.concurrent.TimeUnit
import java.nio.ByteOrder
import scala.collection.immutable.Map
import scala.collection.mutable.ArrayBuffer
import scala.sys.process.Process
import scala.util.Using
import datalog.storage.{VolcanoStorageManager, CollectionsEDB, CollectionsRow, VolcanoOperators}
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

/**
 * Benchmarks that are run on all modes
 */
@Fork(1) // # of jvms that it will use
@Warmup(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize = 1)
@Measurement(iterations = 10, time = 10, timeUnit = TimeUnit.SECONDS, batchSize= 1)
@State(Scope.Thread)
//@TearDown(Level.Invocation)
//@BenchmarkMode(Array(Mode.SingleShotTime))
@BenchmarkMode(Array(Mode.AverageTime))
class BenchPipeline {
//  @Setup(Level.Iteration)
//  def s(): Unit = {
//  }

  val datasize = 1000000
  val path = "/Users/anna/dias/pipeline-runner-master/utils/graal"

  @Benchmark def pipeline_optimized(blackhole: Blackhole): Unit = {
    val baseline = "add"
    val projectPath = s"$path/$baseline"
    val volcano = new VolcanoStorageManager()
    val inputData = CollectionsEDB(ArrayBuffer.range(0, datasize).map(i => CollectionsRow(Seq(i))))

    val operators = VolcanoOperators(volcano)
    val src = operators.Scan(inputData, 0)
    val producer = operators.UDFProjectOperator(projectPath, src, outputMD = operators.Metadata.Binary(4, ByteOrder.BIG_ENDIAN))
    val intermediate = operators.UDFProjectOperator(projectPath, producer, outputMD = operators.Metadata.Binary(4, ByteOrder.BIG_ENDIAN), inputMD = operators.Metadata.Binary(4, ByteOrder.BIG_ENDIAN))
    val consumer = operators.UDFProjectOperator(projectPath, intermediate, inputMD = operators.Metadata.Binary(4, ByteOrder.BIG_ENDIAN))
    blackhole.consume(
      consumer.toList()
    )
  }

  @Benchmark def pipeline_baseline(blackhole: Blackhole): Unit = {
    val baseline = "add"
    val projectPath = s"$path/$baseline"
    val volcano = new VolcanoStorageManager()
    val inputData = CollectionsEDB(ArrayBuffer.range(0, datasize).map(i => CollectionsRow(Seq(i))))

    val operators = VolcanoOperators(volcano)
    val src = operators.Scan(inputData, 0)
    val producer = operators.UDFProjectOperator(projectPath, src)
    val intermediate = operators.UDFProjectOperator(projectPath, producer)
    val consumer = operators.UDFProjectOperator(projectPath, intermediate)
    blackhole.consume(
      consumer.toList()
    )
  }

  @Benchmark def pipeline_optimized_operatorfused(blackhole: Blackhole): Unit = {
    val baseline = "add"
    val projectPath = s"$path/$baseline"
    val volcano = new VolcanoStorageManager()
    val inputData = CollectionsEDB(ArrayBuffer.range(0, datasize).map(i => CollectionsRow(Seq(i))))

    val operators = VolcanoOperators(volcano)
    val src = operators.Scan(inputData, 0)

    val fused = operators.Fused3xUDFProjectOperator(
      projectPath,
      src,
    )
    blackhole.consume(
      fused.toList()
    )
  }

  @Benchmark def pipeline_optimized_processfused(blackhole: Blackhole): Unit = {
    val baseline = "add-fused"
    val projectPath = s"$path/$baseline"
    val volcano = new VolcanoStorageManager()
    val inputData = CollectionsEDB(ArrayBuffer.range(0, datasize).map(i => CollectionsRow(Seq(i))))

    val operators = VolcanoOperators(volcano)
    val src = operators.Scan(inputData, 0)

    val fused = operators.UDFProjectOperator(
      projectPath,
      src,
    )
    blackhole.consume(
      fused.toList()
    )
  }

  @Benchmark def pipeline_optimized_unixfused(blackhole: Blackhole): Unit = {
    val baseline = "add"
    val projectPath = s"$path/$baseline"
    val volcano = new VolcanoStorageManager()
    val inputData = CollectionsEDB(ArrayBuffer.range(0, datasize).map(i => CollectionsRow(Seq(i))))

    val operators = VolcanoOperators(volcano)
    val src = operators.Scan(inputData, 0)

    val fused = operators.FusedUnixUDFProjectOperator(
      projectPath,
      src,
    )
//    Thread.sleep(5000)

    blackhole.consume(
      fused.toList()
    )
  }
}
