package test.examples.ship

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program}
import test.ExampleTestGenerator

import java.nio.file.Paths
class ship_test extends ExampleTestGenerator("ship") with ship
trait ship {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/ship/facts"
  val toSolve: String = "ship_to"
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
