package datalog.dsl

import datalog.execution.ExecutionEngine

import scala.collection.mutable

trait AbstractProgram // TODO: alternate program?

type Printer = mutable.Map[Int, String] // TODO: will eventually want bimap

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

trait Atom {
  def :- (body: Atom*): Unit
  def :- (body: Unit): Unit
  val rId: Int
  val terms: IndexedSeq[Term]
}

// TODO: not using name for anything now, but in the future if relations are automatically derived from source, then the user needs to be able to refer to them by name
case class Relation[T <: Constant](id: Int, name: String)(using ee: ExecutionEngine) {
  type RelTerm = T | Variable
  ee.initRelation(id, name)

  case class RelAtom(terms: IndexedSeq[RelTerm]) extends Atom { // extend Atom so :- can accept atom of any Relation
    // IDB tuple
    val rId: Int = id
    def :-(body: Atom*): Unit = ee.insertIDB(rId, this +: body)
    // EDB tuple
    def :-(body: Unit): Unit = ee.insertEDB(this)

    override def toString = name + terms.mkString("(", ", ", ")")
  }
  // Create a tuple in this relation
  def apply(ts: RelTerm*): RelAtom = RelAtom(ts.toIndexedSeq)

  def solve(): Set[Seq[Term]] = ee.solve(id).map(s => s.toSeq).toSet
}
