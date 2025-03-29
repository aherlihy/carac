package carac.execution.ast.transform

import carac.execution.{ExecutionEngine, PrecedenceGraph}
import carac.storage.{StorageManager, RelationId}

import scala.collection.mutable

class ASTTransformerContext(using val precedenceGraph: PrecedenceGraph)(using val sm: StorageManager) {
  val aliases: mutable.Map[RelationId, RelationId] = mutable.Map[RelationId, RelationId]()
}

