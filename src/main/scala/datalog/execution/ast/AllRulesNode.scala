package datalog.execution.ast

import scala.collection.mutable.ArrayBuffer

case class AllRulesNode(rules: ArrayBuffer[ASTNode], rId: Int, var edb: Boolean = false) extends ASTNode {}
