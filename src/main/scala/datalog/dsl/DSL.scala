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

def not(atom: Atom): Atom = !atom

class Atom(val rId: Int, val terms: Seq[Term], val negated: Boolean) {
  def unary_! : Atom = ???
  def :- (body: (Atom | Constraint)*): Unit = ???
  def :- (body: Unit): Unit = ???
  val hash: String = s"${if (negated) "!" else ""}$rId.${terms.mkString("", "", "")}"
}

case class Relation[T <: Constant](id: Int, name: String)(using ee: ExecutionEngine) {
  type RelTerm = T | Variable
  ee.initRelation(id, name)

  case class RelAtom(override val terms: Seq[RelTerm],
                     override val negated: Boolean = false,
                    ) extends Atom(id, terms, negated) { // extend Atom so :- can accept atom of any Relation
    override def unary_! : Atom = copy(negated = !negated)
    // IDB tuple
    override def :-(body: (Atom | Constraint)*): Unit =
      if (negated)
        throw new Exception("Cannot have negated predicates in the head of a rule")
      ee.insertIDB(rId, this +: body)
    // EDB tuple
    override def :-(body: Unit): Unit =
      if (negated)
        throw new Exception("Cannot have negated EDB, define a new EDB")
      ee.insertEDB(this)

    override def toString = name + terms.mkString("(", ", ", ")")
  }
  // Create a tuple in this relation
  def apply(ts: RelTerm*): RelAtom = RelAtom(ts.toIndexedSeq)

  def solve(): Set[Seq[Term]] = ee.solve(id).map(s => s.toSeq).toSet
  def get(): Set[Seq[Term]] = ee.get(id)
}


enum AggOp(val t: Term):
  case SUM(override val t: Term) extends AggOp(t)
  case COUNT(override val t: Term) extends AggOp(t)
  case MIN(override val t: Term) extends AggOp(t)
  case MAX(override val t: Term) extends AggOp(t)

case class GroupingAtom(gp: Atom, gv: Seq[Variable], ags: Seq[(AggOp, Variable)])
  extends Atom(gp.rId, gv ++ ags.map(_._2), false):
    // We set the relation id of the grouping predicate because the 'virtual' relation will be computed from it and also because we need it to be so for certain logic: dep in JoinIndexes, node id in DependencyGraph, etc.
    override val hash: String = s"G#${gp.hash}-${gv.mkString("", "", "")}-${ags.mkString("", "", "")}"

object groupBy:
  def apply(gp: Atom, gv: Seq[Variable], ags: (AggOp, Variable)*): GroupingAtom =
    if (gp.negated)
      throw new Exception("The grouping predicate cannot be negated")
    if (gv.size != gv.distinct.size)
      throw new Exception("The grouping variables cannot be repeated")
    if (ags.map(_._2).size != ags.map(_._2).distinct.size)
      throw new Exception("The aggregation variables cannot be repeated")
    val gpVars = gp.terms.collect{ case v: Variable => v }.toSet
    val gVars = gv.toSet
    val aggVars = ags.map(_._2).toSet
    val aggdVars = ags.map(_._1.t).collect{ case v: Variable => v }.toSet
    if (gVars.contains(__) || aggVars.contains(__) || aggdVars.contains(__))
      throw new Exception("Anonymous variable ('__') not allowed as a grouping variable, aggregation variable or aggregated variable")
    if (aggVars.intersect(gpVars).nonEmpty)
      throw new Exception("No aggregation variable must not occur in the grouping predicate")
    if (!(aggdVars ++ gVars).subsetOf(gpVars))
      throw new Exception("The aggregated variables and the grouping variables must occurr in the grouping predicate")
    GroupingAtom(gp, gv, ags)


enum Comparison:
  case EQ, NEQ, LT, LTE, GT, GTE

enum Expression:
  case One(t: Term)
  case Add(l: Expression, r: Term)
  case Sub(l: Expression, r: Term)
  case Mul(l: Expression, r: Term)
  case Div(l: Expression, r: Term)
  case Mod(l: Expression, r: Term)


case class Constraint(c: Comparison, l: Expression, r: Expression):
  val hash: String = s"C|$l$c$r}"

private def checkExpression(e: Expression): Unit =
  inline def isAnonVariable(t: Term): Boolean = t.isInstanceOf[Variable] && t.asInstanceOf[Variable].anon
  def aux(e: Expression): Boolean = e match
    case Expression.One(t) => isAnonVariable(t)
    case Expression.Add(l, r) => aux(l) || isAnonVariable(r)
    case Expression.Sub(l, r) => aux(l) || isAnonVariable(r)
    case Expression.Mul(l, r) => aux(l) || isAnonVariable(r)
    case Expression.Div(l, r) => aux(l) || isAnonVariable(r)
    case Expression.Mod(l, r) => aux(l) || isAnonVariable(r)
  if (aux(e))
      throw new Exception("Anonymous variable ('__') not allowed in comparison atoms")

implicit def term2ExpressionOne(x: Term): Expression.One = Expression.One(x)

extension (e: Expression)
  def +(t: Term): Expression.Add =
    Expression.Add(e, t)
  def -(t: Term): Expression.Sub =
    Expression.Sub(e, t)
  def *(t: Term): Expression.Mul =
    Expression.Mul(e, t)
  def /(t: Term): Expression.Div =
    Expression.Div(e, t)
  def %(t: Term): Expression.Mod =
    Expression.Mod(e, t)

  def |=|(o: Expression): Constraint =
    checkExpression(e)
    checkExpression(o)
    Constraint(Comparison.EQ, e, o)
  def |!=|(o: Expression): Constraint =
    checkExpression(e)
    checkExpression(o)
    Constraint(Comparison.NEQ, e, o)
  def |<|(o: Expression): Constraint =
    checkExpression(e)
    checkExpression(o)
    Constraint(Comparison.LT, e, o)
  def |<=|(o: Expression): Constraint =
    checkExpression(e)
    checkExpression(o)
    Constraint(Comparison.LTE, e, o)
  def |>|(o: Expression): Constraint =
    checkExpression(e)
    checkExpression(o)
    Constraint(Comparison.GT, e, o)
  def |>=|(o: Expression): Constraint =
    checkExpression(e)
    checkExpression(o)
    Constraint(Comparison.GTE, e, o)
