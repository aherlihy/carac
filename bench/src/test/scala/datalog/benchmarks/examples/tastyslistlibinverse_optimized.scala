package datalog.benchmarks.examples

import datalog.benchmarks.ExampleBenchmarkGenerator
import datalog.dsl.{Constant, Program}
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import test.examples.tastyslistlibinverse_optimized.tastyslistlibinverse_optimized as tastyslistlibinverse_optimized_test

import java.nio.file.Paths
import java.util.concurrent.TimeUnit

@Fork(examples_fork) // # of jvms that it will use
@Warmup(iterations = examples_warmup_iterations, time = examples_warmup_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@Measurement(iterations = examples_iterations, time = examples_time, timeUnit = TimeUnit.SECONDS, batchSize = examples_xl_batchsize)
@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
class tastyslistlibinverse_optimized() extends ExampleBenchmarkGenerator("tastyslistlibinverse_optimized") with tastyslistlibinverse_optimized_test {
}