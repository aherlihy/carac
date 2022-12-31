package datalog.execution.ast.transform

import datalog.execution.ast.ASTNode

abstract class Transformer {
  def transform(node: ASTNode): ASTNode
}
