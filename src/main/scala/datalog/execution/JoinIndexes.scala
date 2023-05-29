package datalog.execution

import datalog.dsl.{Atom, Constant, Variable}
import datalog.execution.ir.ProjectJoinFilterOp
import datalog.storage.{StorageManager, NS}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.quoted.*
import scala.reflect.ClassTag

type AllIndexes = mutable.Map[String, JoinIndexes]

/**
 * Wrapper object for join keys for IDB rules
 *
 * @param varIndexes - indexes of repeated variables within the body
 * @param constIndexes - indexes of constants within the body
 * @param projIndexes - for each term in the head, either ("c", the constant value) or ("v", the first index of the variable within the body)
 * @param deps - set of relations directly depended upon by this rule
 * @param negated - for each atom in the body, whether it is negated
 * @param sizes - for each atom in the body, the number of terms it has
 * @param edb - for rules that have EDBs defined on the same predicate, just read
 */
case class JoinIndexes(varIndexes: Seq[Seq[Int]],
                       constIndexes: Map[Int, Constant],
                       projIndexes: Seq[(String, Constant)],
                       deps: Seq[Int],
                       negated: Array[Boolean],
                       sizes: Array[Int],
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
    val sizes = body.map(a => a.terms.size)
    val negated = body.map(a => a.negated)

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

    // The head atom may not be negated.
    if (rule(0).negated) throw new Exception("Head atom cannot be negated")

    // All variables in the head atom must be limited.
    val variablesInPositiveAtoms =
      rule.drop(1)
        .filter(!_.negated)
        .flatMap(_.terms)
        .flatMap {
          case v: Variable => Some(v)
          case _ => None
        }
    val variablesLimited = rule.flatMap(_.terms)
      .flatMap {
        case v: Variable => Some(v)
        case _ => None
      }
      .forall(v => variablesInPositiveAtoms.contains(v))

    if !variablesLimited then throw new Exception("Some variables in the head atom are not limited")

    new JoinIndexes(bodyVars, constants.toMap, projects, deps, negated, sizes, rule)
  }

  def getSortAhead[T: ClassTag](input: Array[T], sortBy: T => Int, rId: Int, oldHash: String, sm: StorageManager)(using jitOptions: JITOptions): (Array[T], String) = {
    if (jitOptions.sortOrder._2 != 0)
      val oldAtoms = sm.allRulesAllIndexes(rId)(oldHash).atoms
//      debug("", () => s"in getSorted: deps=${oldAtoms.drop(1).map(s => sm.ns(s.rId)).mkString("", ",", "")} current relation sizes: ${input.map(i => s"${sortBy(i)}|").mkString("", ", ", "")}")
      var tToAtom = input.zipWithIndex.map((t, i) => (t, oldAtoms(i + 1))).sortBy((t, _) => sortBy(t))
      if (jitOptions.sortOrder._2 == -1) tToAtom = tToAtom.reverse
      val newHash = JoinIndexes.getRuleHash(oldAtoms.head +: tToAtom.map(_._2))

      val sortedT = tToAtom.map(_._1)
      (sortedT, newHash)
    else
      (input, oldHash)
  }

  def getPreSortAhead(input: Array[ProjectJoinFilterOp], sortBy: Atom => Int, rId: Int, oldHash: String, sm: StorageManager)(using jitOptions: JITOptions): (Array[ProjectJoinFilterOp], String) = {
    val originalK = sm.allRulesAllIndexes(rId)(oldHash)
    if (jitOptions.sortOrder._1 != 0)
//      debug("", () => s"in compiler UNION[spj] deps=${originalK.deps.map(s => sm.ns(s)).mkString("", ",", "")} current relation sizes: ${originalK.atoms.drop(1).map(a => s"${sm.ns(a.rId)}:|${sortBy(a)}|").mkString("", ", ", "")}")
      var newBody = originalK.atoms.drop(1).zipWithIndex.sortBy((a, _) => sortBy(a))
      if (jitOptions.sortOrder._1 == -1) newBody = newBody.reverse
      val newAtoms = originalK.atoms.head +: newBody.map(_._1)
      val newHash = JoinIndexes.getRuleHash(newAtoms)
      (input.map(c => ProjectJoinFilterOp(rId, newHash, newBody.map((_, oldP) => c.childrenSO(oldP)): _*)), newHash)
    else
      (input, oldHash)
  }
  def allOrders(rule: Array[Atom]): AllIndexes = {
    mutable.Map[String, JoinIndexes](rule.drop(1).permutations.map(r =>
      val toRet = JoinIndexes(rule.head +: r)
      toRet.hash -> toRet
    ).toSeq:_*)
  }

  def getRuleHash(rule: Array[Atom]): String = rule.map(r => r.hash).mkString("", "", "")
}