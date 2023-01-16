package datalog.execution.ast

import scala.collection.mutable.{ArrayBuffer, Map}

case class ProgramNode(rules: Map[Int, ASTNode] = Map.empty) extends ASTNode {

}
