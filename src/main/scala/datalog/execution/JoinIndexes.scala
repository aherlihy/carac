package datalog.execution

import datalog.dsl.{Atom, Constant, Variable}
import datalog.execution.ir.ProjectJoinFilterOp
import datalog.storage.{NS, RelationId, StorageManager}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.quoted.*
import scala.reflect.ClassTag

type AllIndexes = mutable.Map[String, JoinIndexes]

enum PredicateType:
  case POSITIVE, NEGATED

/**
 * Wrapper object for join keys for IDB rules
 *
 * @param varIndexes - indexes of repeated variables within the body
 * @param constIndexes - indexes of constants within the body
 * @param projIndexes - for each term in the head, either ("c", the constant value) or ("v", the first index of the variable within the body)
 * @param deps - set of relations directly depended upon by this rule and the type of operation. Current either ("+", relationId) for positive edges or ("-", relationId) for negative edges, TODO: expand for aggregations
 * @param edb - for rules that have EDBs defined on the same predicate, just read
 * @param atoms - the original atoms from the DSL
 * @param cxns - convenience data structure tracking how many variables in common each atom has
 */
case class JoinIndexes(varIndexes: Seq[Seq[Int]],
                       constIndexes: Map[Int, Constant],
                       projIndexes: Seq[(String, Constant)],
                       deps: Seq[(PredicateType, RelationId)],
                       atoms: Array[Atom],
                       cxns: Map[RelationId, Map[Int, Seq[RelationId]]],
                       edb: Boolean = false
                      ) {
  override def toString(): String = toStringWithNS(null)

  def toStringWithNS(ns: NS): String = "{ vars:" + varToString() +
      ", consts:" + constToString() +
      ", project:" + projToString() +
      ", deps:" + depsToString(ns) +
      ", edb:" + edb +
      ", cxn: " + cxnsToString(ns) +
      " }"

  def varToString(): String = varIndexes.map(v => v.mkString("$", "==$", "")).mkString("[", ",", "]")
  def constToString(): String = constIndexes.map((k, v) => k + "==" + v).mkString("{", "&&", "}")
  def projToString(): String = projIndexes.map((typ, v) => s"$typ$v").mkString("[", " ", "]")
  def depsToString(ns: NS): String = deps.map((typ, rId) => s"$typ${ns(rId)}").mkString("[", ", ", "]")
  def cxnsToString(ns: NS): String = cxns.map((rId, inCommon) => s"{ ${ns(rId)} => ${inCommon.map((count, rIds) => count + ":" + rIds.map(r => ns(r)).mkString("", "/", "")).mkString("(", ", ", ")")}").mkString("[", ", ", "]")
  val hash: String = atoms.map(a => a.hash).mkString("", "", "")
}

object JoinIndexes {
  def apply(rule: Array[Atom]) = {
    val constants = mutable.Map[Int, Constant]() // position => constant
    val variables = mutable.Map[Variable, Int]() // v.oid => position

    val body = rule.drop(1)

    val deps = body.map(a => (if (a.negated) PredicateType.NEGATED else PredicateType.POSITIVE, a.rId))

    val typeHelper = body.flatMap(a => a.terms.map(* => !a.negated))

    val bodyVars = body
      .flatMap(a => a.terms)      // all terms in one seq
      .zipWithIndex               // term, position
      .groupBy(z => z._1)         // group by term
      .filter((term, matches) =>  // matches = Seq[(var, pos1), (var, pos2), ...]
        term match {
          case v: Variable =>
            matches.map(_._2).find(typeHelper) match
              case Some(pos) =>
                variables(v) = pos
              case None =>
                if (v.oid != -1)
                  throw new Exception(s"Variable with varId ${v.oid} appears only in negated rules")
                else
                  ()
            !v.anon && matches.length >= 2
          case c: Constant =>
            matches.foreach((_, idx) => constants(idx) = c)
            false
        }
      )
      .map((term, matches) =>     // get rid of groupBy elem in result tuple
        matches.map(_._2).toIndexedSeq
      )
      .toIndexedSeq

    // variable ids in the head atom
    val projects = rule.head.terms.map {
      case v: Variable =>
        if (!variables.contains(v))
          throw new Exception(s"Free variable in rule head with varId ${v.oid}")
        if (v.anon)
          throw new Exception("Anonymous variable ('__') not allowed in head of rule")
        ("v", variables(v))
      case c: Constant => ("c", c)
    }

    // produces (atom, { # repeated vars => atom } )
    val cxns = body.zipWithIndex.map((atom, idx) =>
      (atom.rId, body.zipWithIndex.map((atom2, idx2) =>
        (idx2, atom2.rId, atom.terms.filter(t => t.isInstanceOf[Variable]).intersect(atom2.terms).size)
      )
        .filter((idx2, rId, count) => idx != idx2 && count != 0)
        .map(t => (t._2, t._3))
        .groupBy(_._2)
        .map((count, rIds) => (count, rIds.map((rId, count2) => rId).toSeq)))
    ).toMap

    new JoinIndexes(bodyVars, constants.toMap, projects, deps, rule, cxns)
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

  def getPresortWithCxn(input: Array[ProjectJoinFilterOp], sortBy: Atom => Int, rId: Int, oldHash: String, sm: StorageManager)(using jitOptions: JITOptions): (Array[ProjectJoinFilterOp], String) = {
    val originalK = sm.allRulesAllIndexes(rId)(oldHash)

    var sortedBody = originalK.atoms.drop(1).zipWithIndex.sortBy((a, _) => sortBy(a))
    if (jitOptions.sortOrder._1 == -1) sortedBody = sortedBody.reverse
//    if (input.length > 2)
//    println(s"Rule: ${sm.printer.ruleToString(originalK.atoms)}")
//    println(s"Rule cxn: ${originalK.cxnsToString(sm.ns)}")

    val rStack = sortedBody.to(mutable.ListBuffer)
    var newBody = Array[(Atom, Int)]()
//    println("START, stack=" + rStack)
    while(rStack.nonEmpty)
      var next = rStack.head
//      println(s"picking head ${sm.ns(next._1.rId)} off stack")
      while(next != null)
        newBody = newBody :+ next
        rStack.remove(rStack.indexOf(next))
//        println(s"body now: ${newBody.map(_._1).map(a => sm.ns(a.rId)).mkString("[", ", ", "]")}")

        val cxns = originalK.cxns(next._1.rId)
        if (cxns.nonEmpty)
          val bestCxn = cxns.max._2
//          println(s"r with max cxn=${bestCxn.map(r => sm.ns(r)).mkString("[", ", ", "]")}")
          val availableCxn = rStack.filter((atom, _) => bestCxn.contains(atom.rId)) // use filter not intersect to retain order
//          println(s"cxns that are still on the stack = ${availableCxn.map(p => sm.ns(p._1.rId))}")
          if (availableCxn.isEmpty)
            next = null
          else
            next = availableCxn.head
//            println(s"got strongest cxn, ${sm.ns(next._1.rId)}")
        else
          next = null

    val newAtoms = originalK.atoms.head +: newBody.map(_._1)
    val newHash = JoinIndexes.getRuleHash(newAtoms)

//    if (jitOptions.sortOrder._1 == 0)
//      println(s"\tOrder: ${originalK.atoms.drop(1).map(a => s"${sm.ns(a.rId)}:|${sortBy(a)}|").mkString("", ", ", "")}")
//    else
//      println(s"\tOrder: ${newBody.map((a, _) => s"${sm.ns(a.rId)}:|${sortBy(a)}|").mkString("", ", ", "")}")

    (input.map(c => ProjectJoinFilterOp(rId, newHash, newBody.map((_, oldP) => c.childrenSO(oldP)): _*)), newHash)
    //    else
    //      (input, oldHash)
  }

  def getPreSortAhead(input: Array[ProjectJoinFilterOp], sortBy: Atom => Int, rId: Int, oldHash: String, sm: StorageManager)(using jitOptions: JITOptions): (Array[ProjectJoinFilterOp], String) = {
    val originalK = sm.allRulesAllIndexes(rId)(oldHash)

    var newBody = originalK.atoms.drop(1).zipWithIndex.sortBy((a, _) => sortBy(a))
    if (jitOptions.sortOrder._1 == -1) newBody = newBody.reverse
    val newAtoms = originalK.atoms.head +: newBody.map(_._1)
    val newHash = JoinIndexes.getRuleHash(newAtoms)
//    if (input.length > 2)
//      println(s"Rule: ${sm.printer.ruleToString(originalK.atoms)}")
//      println(s"Rule cxn: ${originalK.cxnsToString(sm.ns)}")
//    if (jitOptions.sortOrder._1 == 0)
//      println(s"\tCard: ${originalK.atoms.drop(1).map(a => s"${sm.ns(a.rId)}:|${sortBy(a)}|").mkString("", ", ", "")}")
//    else
//      println(s"\tCard: ${newBody.map((a, _) => s"${sm.ns(a.rId)}:|${sortBy(a)}|").mkString("", ", ", "")}")
    (input.map(c => ProjectJoinFilterOp(rId, newHash, newBody.map((_, oldP) => c.childrenSO(oldP)): _*)), newHash)
//    else
//      (input, oldHash)
  }
  def allOrders(rule: Array[Atom]): AllIndexes = {
    mutable.Map[String, JoinIndexes](rule.drop(1).permutations.map(r =>
      val toRet = JoinIndexes(rule.head +: r)
      toRet.hash -> toRet
    ).toSeq:_*)
  }

  def getRuleHash(rule: Array[Atom]): String = rule.map(r => r.hash).mkString("", "", "")
}