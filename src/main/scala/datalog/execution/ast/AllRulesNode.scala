package datalog.execution.ast

import scala.collection.mutable.ArrayBuffer

case class AllRulesNode(rules: ArrayBuffer[ASTNode], rId: Int) extends ASTNode {}
