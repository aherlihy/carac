package datalog.execution.ast.transform

import datalog.execution.ast.ASTNode

import scala.collection.mutable

abstract class Transformer(using val ctx: ASTTransformerContext) {
  def transform(node: ASTNode): ASTNode
}
