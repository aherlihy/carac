package datalog.execution.ast

import datalog.dsl.{Atom, Constant, Term, Variable}

import scala.collection.immutable
import scala.collection.mutable.{ArrayBuffer, Map}

abstract class ASTNode {}

case class AllRulesNode(rules: ArrayBuffer[ASTNode], rId: Int, var edb: Boolean = false) extends ASTNode {}

abstract class AtomNode() extends ASTNode {}

case class NegAtom(expr: ASTNode) extends AtomNode {}

case class LogicAtom(relation: Int, terms: Seq[ASTNode]) extends AtomNode {}

// case class aggregator / constraint / arithmetic op
case class ProgramNode(rules: Map[Int, ASTNode] = Map.empty) extends ASTNode {}

case class RuleNode(head: ASTNode, body: Seq[ASTNode], dslAtoms: Array[Atom], currentRuleHash: String) extends ASTNode {
  // TODO: assert head is instanceOf LogicAtom
}

abstract class TermNode(value: Term) extends ASTNode {}

case class VarTerm(value: Variable) extends TermNode(value) {}

case class ConstTerm(value: Constant) extends TermNode(value) {}