package datalog.execution.ast

import datalog.dsl.{Relation, Term}

abstract class AtomNode() extends ASTNode {}

case class NegAtom(expr: ASTNode) extends AtomNode {}

case class LogicAtom(relation: Int, terms: Seq[ASTNode]) extends AtomNode {}

// case class aggregator / constraint / arithmetic op