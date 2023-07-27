package datalog.execution.ast

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.JoinIndexes
import datalog.storage.RelationId

import scala.collection.immutable
import scala.collection.mutable.{ArrayBuffer, Map}

abstract class ASTNode {}

case class ProgramNode(rules: Map[RelationId, ASTNode] = Map.empty) extends ASTNode {}

case class AllRulesNode(rules: ArrayBuffer[ASTNode], rId: RelationId, var edb: Boolean = false) extends ASTNode {}

abstract class AtomNode() extends ASTNode {}

case class LogicAtom(relation: RelationId, terms: Seq[ASTNode], negated: Boolean) extends AtomNode {}

case class RuleNode(head: ASTNode, body: Seq[ASTNode], dslAtoms: Array[Atom], k: JoinIndexes) extends ASTNode {}

abstract class TermNode(value: Term) extends ASTNode {}

case class VarTerm(value: Variable) extends TermNode(value) {}

case class ConstTerm(value: Constant) extends TermNode(value) {}