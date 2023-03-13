package datalog.execution.ast.transform

import datalog.execution.{ExecutionEngine, PrecedenceGraph}
import datalog.storage.{CollectionsStorageManager, StorageManager}

import scala.collection.mutable

class ASTTransformerContext(using val precedenceGraph: PrecedenceGraph)(using val sm: CollectionsStorageManager) {
  val aliases: mutable.Map[Int, Int] = mutable.Map[Int, Int]()
}

