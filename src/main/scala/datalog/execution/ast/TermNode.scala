package datalog.execution.ast

import datalog.dsl.{Term, Variable, Constant}

abstract class TermNode(value: Term) extends ASTNode {}

case class VarTerm(value: Variable) extends TermNode(value) {}

case class ConstTerm(value: Constant) extends TermNode(value) {}