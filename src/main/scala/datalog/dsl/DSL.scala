package datalog.dsl

import datalog.execution.ExecutionEngine

trait AbstractProgram

type Constant = Int | String // TODO: other constant types?

case class Variable(oid: Int) {
  override def toString = "v" + oid
  override def equals(that: Any): Boolean =
    that match {
      case v: Variable => oid == v.oid
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
    val rId = id
    def :-(body: Atom*): Unit = {
      ee.insertIDB(rId, this +: body)
    }
    // EDB tuple
    def :-(body: Unit): Unit = ee.insertEDB(this)

    override def toString = rId + terms.mkString("(", ", ", ")")
  }
  // Create a tuple in this relation
  def apply(ts: RelTerm*): RelAtom = {
    new RelAtom(ts.toIndexedSeq)
  }

  def solve(): Any = ee.solve(id)
  def solveNaive(): Any = ee.solveNaive(id)
}
