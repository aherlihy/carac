package datalog.dsl

import datalog.execution.{ExecutionEngine, SimpleExecutionEngine}

// TODO: better to have program as given instance?
class Program(engine: ExecutionEngine = new SimpleExecutionEngine) extends AbstractProgram {
  given ee: ExecutionEngine = engine
  var varCounter = 0
  def variable(): Variable = {
    varCounter += 1
    Variable(varCounter - 1)
  }
  var relCounter = 0
  def relation[T <: Constant](userName: String = relCounter.toString): Relation[T] = {
    relCounter += 1
    Relation[T](relCounter - 1, userName)
  }

  // TODO: also provide solve for multiple/all predicates, or return table so users can query over the derived DB
  def solve(rId: Int): Any = ee.solve(rId) // TODO: get rid of any
  def solveNaive(rId: Int): Any = ee.solveNaive(rId) // TODO: get rid of any
}
