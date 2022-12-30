package datalog.execution.ast

case class RuleNode(head: ASTNode, body: Seq[ASTNode]) extends ASTNode {}
