package datalog.execution.ast.transform

import scala.collection.mutable

class ASTTransformerContext() {
  val aliases: mutable.Map[Int, Int] = mutable.Map[Int, Int]()
}

