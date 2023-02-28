package datalog.benchmarks.examples

import datalog.dsl.{Constant, Program}
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import java.nio.file.Paths
import java.util.concurrent.TimeUnit
@Fork(1) // # of jvms that it will use
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
class ship_benchmark() extends ExampleBenchmarkGenerator("ship") with ship {
 override def toSolve: String = super.toSolve
 @Setup
 def s(): Unit = setup() // can't add annotations to super, so just call

 @TearDown
 def f(): Unit = finish()

 // relational, naive
 @Benchmark def naive_relational(blackhole: Blackhole): Unit = {
  blackhole.consume(
   run(programs("NaiveRelational"), result)
  )
 }
 // relational, seminaive
 @Benchmark def seminaive_relational(blackhole: Blackhole): Unit = {
  blackhole.consume(
   run(programs("SemiNaiveRelational"), result)
  )
 }

 // collections, naive
 @Benchmark def naive_collections(blackhole: Blackhole): Unit = {
  blackhole.consume(
   run(programs("NaiveCollections"), result)
  )
 }
 // relational, seminaive
 @Benchmark def seminaive_collections(blackhole: Blackhole): Unit = {
  blackhole.consume(
   run(programs("SemiNaiveCollections"), result)
  )
 }

 // staged, naive
 @Benchmark def naive_staged(blackhole: Blackhole): Unit = {
  blackhole.consume(
   run(programs("NaiveStagedCollections"), result)
  )
 }

 // staged, seminaive
 @Benchmark def seminaive_staged(blackhole: Blackhole): Unit = {
  blackhole.consume(
   run(programs("SemiNaiveStagedCollections"), result)
  )
 }
}
trait ship {
  def toSolve: String = "ship_to"
  def pretest(program: Program): Unit = {
    val customer_city = program.relation[Constant]("customer_city")

    val has_ordered = program.relation[Constant]("has_ordered")

    val product_name = program.relation[Constant]("product_name")

    val ship_to = program.relation[Constant]("ship_to")

    val ProdName, City, CustNo, ProdNo = program.variable()
    
    ship_to(ProdName, City) :- ( has_ordered(CustNo, ProdNo), customer_city(CustNo, City), product_name(ProdNo, ProdName) )
    
    customer_city("1", "london") :- ()
    customer_city("2", "paris") :- ()
    customer_city("3", "San Francisco") :- ()
    customer_city("4", "munich") :- ()
    customer_city("5", "seoul") :- ()
    
    has_ordered("1", "1") :- ()
    has_ordered("2", "2") :- ()
    has_ordered("3", "3") :- ()
    has_ordered("4", "4") :- ()
    has_ordered("5", "5") :- ()
    
    product_name("1", "tea") :- ()
    product_name("2", "bread") :- ()
    product_name("3", "flowers") :- ()
    product_name("4", "sausage") :- ()
    product_name("5", "horse") :- ()
  }
}