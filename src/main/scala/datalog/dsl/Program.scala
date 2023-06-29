package datalog.dsl

import datalog.execution.{ExecutionEngine, NaiveExecutionEngine}

import scala.collection.mutable

// TODO: better to have program as given instance?
class Program(engine: ExecutionEngine) extends AbstractProgram {
  given ee: ExecutionEngine = engine
  var varCounter = 0
  def variable(): Variable = {
    varCounter += 1
    Variable(varCounter - 1)
  }
  var relCounter = 0
  def relation[T <: Constant](userName: String = relCounter.toString, columns: Seq[ColumnType] = Seq.empty): Relation[T] = {
    relCounter += 1
    Relation[T](relCounter - 1, userName, columns)
  }

  def namedRelation[T <: Constant](userName: String, columns: Seq[ColumnType] = Seq.empty): Relation[T] = {
    if (!ee.storageManager.ns.contains(userName)) {
      throw new Exception("Named relation '" + userName + "' does not exist")
    }
    val rId = ee.storageManager.ns(userName)
    Relation[T](rId, userName, columns)
  }

  // TODO: also provide solve for multiple/all predicates, or return table so users can query over the derived DB
  def solve(rId: Int): Set[Seq[Term]] = ee.solve(rId).map(s => s.toSeq).toSet
}
