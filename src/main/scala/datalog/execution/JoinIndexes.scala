package datalog.execution

import datalog.dsl.{Atom, Constant, Variable}
import datalog.storage.NS

import scala.collection.mutable
import scala.quoted.*
import scala.reflect.ClassTag

/**
 * Wrapper object for join keys for IDB rules
 *
 * @param varIndexes - indexes of repeated variables within the body
 * @param constIndexes - indexes of constants within the body
 * @param projIndexes - for each term in the head, either ("c", the constant value) or ("v", the first index of the variable within the body)
 * @param deps - set of relations directly depended upon by this rule
 * @param edb - for rules that have EDBs defined on the same predicate, just read
 */
case class JoinIndexes(varIndexes: Seq[Seq[Int]],
                       constIndexes: Map[Int, Constant],
                       projIndexes: Seq[(String, Constant)],
                       deps: Seq[Int],
                       atoms: Array[Atom],
                       edb: Boolean = false
                      ) {
  override def toString(): String = toStringWithNS(null)

  def toStringWithNS(ns: NS): String = "{ vars:" + varToString() +
      ", consts:" + constToString() +
      ", project:" + projToString() +
      ", deps:" + depsToString(ns) +
      ", edb:" + edb +
      " }"

  def varToString(): String = varIndexes.map(v => v.mkString("$", "==$", "")).mkString("[", ",", "]")
  def constToString(): String = constIndexes.map((k, v) => k + "==" + v).mkString("{", "&&", "}")
  def projToString(): String = projIndexes.map((typ, v) => f"$typ$v").mkString("[", " ", "]")
  def depsToString(ns: NS): String = deps.map(d => if (ns != null) ns(d) else d).mkString("[", ", ", "]")
  val hash: String = atoms.map(a => a.hash).mkString("", "", "")
}

object JoinIndexes {
  def apply(rule: Array[Atom]) = {
    val constants = mutable.Map[Int, Constant]() // position => constant
    val variables = mutable.Map[Variable, Int]() // v.oid => position

    val body = rule.drop(1)

    val deps = body.map(a => a.rId) // TODO: should this be a set?

    val bodyVars = body
      .flatMap(a => a.terms)
      .zipWithIndex // terms, position
      .groupBy(z => z._1)
      .filter((term, matches) => // matches = Seq[(var, pos1), (var, pos2), ...]
        term match {
          case v: Variable =>
            variables(v) = matches.head._2 // first idx for a variable
            !v.anon && matches.size >= 2
          case c: Constant =>
            matches.foreach((_, idx) => constants(idx) = c)
            false
        }
      )
      .map((term, matches) => // get rid of groupBy elem in result tuple
        matches.map(_._2).toIndexedSeq
      )
      .toIndexedSeq

    // variable ids in the head atom
    val projects = rule(0).terms.map {
      case v: Variable =>
        if (!variables.contains(v)) throw new Exception(f"Free variable in rule head with varId $v.oid")
        if (v.anon) throw new Exception("Anonymous variable ('__') not allowed in head of rule")
        ("v", variables(v))
      case c: Constant => ("c", c)
    }
    new JoinIndexes(bodyVars, constants.toMap, projects, deps, rule)
  }

  def getSorted[T: ClassTag](order: Int, input: Array[T], sortBy: T => Int, oldAtoms: Array[Atom]): (Array[T], JoinIndexes) = {
    var tToAtom = input.zipWithIndex.map((t, i) => (t, oldAtoms(i + 1))).sortBy((t, _) => sortBy(t))
    if (order == -1) tToAtom = tToAtom.reverse
    val newAtoms = oldAtoms.head +: tToAtom.map(_._2)
    val sortedK = JoinIndexes(newAtoms)
    val sortedT = tToAtom.map(_._1)
    (sortedT, sortedK)
  }

  def allOrders(rule: Array[Atom]): Map[String, JoinIndexes] = {
    rule.drop(1).permutations.map(r =>
      val toRet = JoinIndexes(rule.head +: r)
      (toRet.hash, toRet)
    ).toMap[String, JoinIndexes]
  }

  def getRuleHash(rule: Array[Atom]): String = rule.map(r => r.hash).mkString("", "", "")
}


//given ToExpr[JoinIndexes] with {
//  def apply(joinIndexes: JoinIndexes)(using Quotes) =

//}