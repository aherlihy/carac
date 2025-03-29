package carac.execution.ast.transform

import carac.execution.ast.ASTNode
import carac.storage.StorageManager

import scala.collection.mutable

abstract class Transformer(using val ctx: ASTTransformerContext) {
  def transform(node: ASTNode)(using sm: StorageManager): ASTNode
}
