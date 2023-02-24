package datalog.dsl

import datalog.execution.ExecutionEngine

import scala.collection.mutable
import scala.quoted.{Expr, Quotes}

trait AbstractProgram // TODO: alternate program?

type Constant = Int | String // TODO: other constant types?

val __ = Variable(-1, true)

case class Variable(oid: Int, anon: Boolean = false) {
  override def toString = if (anon) "_" else "v" + oid
  override def equals(that: Any): Boolean =
    that match {
      case v: Variable => !anon && !v.anon && oid == v.oid
      case _ => false // TODO: is this ok?
    }
}

type Term = Constant | Variable

type NegatedAtom = Atom

class Atom(val rId: Int, val terms: Seq[Term]) {
  def :- (body: Atom*): Unit = ???
  def :- (body: Unit): Unit = ???
  val hash: String = s"$rId${terms.mkString("", "", "")}"
}

case class Relation[T <: Constant](id: Int, name: String)(using ee: ExecutionEngine) {
  type RelTerm = T | Variable
  ee.initRelation(id, name)

  case class RelAtom(override val terms: Seq[RelTerm]) extends Atom(id, terms) { // extend Atom so :- can accept atom of any Relation
    // IDB tuple
    override def :-(body: Atom*): Unit = ee.insertIDB(rId, this +: body)
    // EDB tuple
    override def :-(body: Unit): Unit = ee.insertEDB(this)

    override def toString = name + terms.mkString("(", ", ", ")")
  }
  // Create a tuple in this relation
  def apply(ts: RelTerm*): RelAtom = RelAtom(ts.toIndexedSeq)

  def solve(): Set[Seq[Term]] = ee.solve(id).map(s => s.toSeq).toSet
  def get(): Set[Seq[Term]] = ee.get(id)
}
