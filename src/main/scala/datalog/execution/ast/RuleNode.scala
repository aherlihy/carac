package datalog.execution.ast
import datalog.execution.JoinIndexes

case class RuleNode(head: ASTNode, body: Seq[ASTNode], joinIdx: Option[JoinIndexes] = None) extends ASTNode {}
