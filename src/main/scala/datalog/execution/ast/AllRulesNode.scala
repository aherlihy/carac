package datalog.execution.ast

import scala.collection.mutable.ArrayBuffer

case class AllRulesNode(rules: ArrayBuffer[ASTNode]) extends ASTNode {}
