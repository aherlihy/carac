package datalog.execution

import datalog.dsl.Constant
import datalog.storage.NS

import scala.quoted.*

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
                       edb: Boolean = false) {
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
}



//given ToExpr[JoinIndexes] with {
//  def apply(joinIndexes: JoinIndexes)(using Quotes) =

//}