package datalog.execution.ast

abstract class Transformer {
  def transform(node: ASTNode): ASTNode
}
