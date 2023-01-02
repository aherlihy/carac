package datalog.execution.ast.transform

import datalog.execution.{ExecutionEngine, PrecedenceGraph}

import scala.collection.mutable

class ASTTransformerContext(using val precedenceGraph: PrecedenceGraph) {
  val aliases: mutable.Map[Int, Int] = mutable.Map[Int, Int]()
}

