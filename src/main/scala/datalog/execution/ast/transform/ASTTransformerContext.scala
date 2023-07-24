package datalog.execution.ast.transform

import datalog.execution.{ExecutionEngine, PrecedenceGraph}
import datalog.storage.{StorageManager}

import scala.collection.mutable

class ASTTransformerContext(using val precedenceGraph: PrecedenceGraph)(using val sm: StorageManager) {
  val aliases: mutable.Map[Int, Int] = mutable.Map[Int, Int]()
}

