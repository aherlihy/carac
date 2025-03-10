package datalog.execution

import datalog.dsl.{Atom, Constant, Variable}
import datalog.execution.ir.{IROp, ProjectJoinFilterOp, ScanOp}
import datalog.storage.{DB, EDB, NS, RelationId, StorageManager}

import scala.collection.mutable
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
 * @param cxns - convenience data structure tracking how many variables in common each atom has with every other atom.
 */
case class JoinIndexes(varIndexes: Seq[Seq[Int]],
                       constIndexes: mutable.Map[Int, Constant],
                       projIndexes: Seq[(String, Constant)],
                       deps: Seq[(PredicateType, RelationId)],
                       atoms: Seq[Atom],
                       cxns: mutable.Map[String, mutable.Map[Int, Seq[String]]],
                       edb: Boolean = false
                      ) {
  override def toString(): String = ""//toStringWithNS(null)

  def toStringWithNS(ns: NS): String = "{ vars:" + varToString() +
      ", consts:" + constToString() +
      ", project:" + projToString() +
      ", deps:" + depsToString(ns) +
      ", edb:" + edb +
//      ", cxn: " + cxnsToString(ns) +
      " }"

  def varToString(): String = varIndexes.map(v => v.mkString("$", "==$", "")).mkString("[", ",", "]")
  def constToString(): String = constIndexes.map((k, v) => s"$k==$v").mkString("{", "&&", "}")
  def projToString(): String = projIndexes.map((typ, v) => s"$typ$v").mkString("[", " ", "]")
  def depsToString(ns: NS): String = deps.map((typ, rId) => s"${if (typ == PredicateType.POSITIVE) "+" else "-"}${ns(rId)}").mkString("[", ", ", "]")
  def cxnsToString(ns: NS): String =
    cxns.map((h, inCommon) =>
      s"{ ${ns.hashToAtom(h)} => ${
        inCommon.map((count, hashs) =>
          count.toString + ": " + hashs.map(h => ns.hashToAtom(h)).mkString("", "|", "")
        ).mkString("", ", ", "")} }").mkString("[", ",\n", "]")
  val hash: String = atoms.map(a => a.hash).mkString("", "", "")
}

object JoinIndexes {
  def apply(rule: Seq[Atom], precalculatedCxns: Option[mutable.Map[String, mutable.Map[Int, Seq[String]]]]) = {
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
    val cxns = precalculatedCxns.getOrElse(
      body.zipWithIndex.map((atom, idx) => (
        atom.hash,
        body.zipWithIndex
          .map((atom2, idx2) =>
            (idx2, atom2.hash, atom.terms.filter(t => t.isInstanceOf[Variable]).intersect(atom2.terms).size))
          .filter((idx2, rId, count) => idx != idx2 && count != 0)
          .map(t => (t._2, t._3))
          .groupBy(_._2)
          .map((count, hashs) => (count, hashs.map((hash, count2) => hash).toSeq))
          .to(mutable.Map)
      )).to(mutable.Map)
    )

    new JoinIndexes(bodyVars, constants.to(mutable.Map), projects, deps, rule, cxns)
  }

  // used to approximate poor user-defined order
  def presortSelectWorst(sortBy: (Atom, Boolean) => (Boolean, Int), originalK: JoinIndexes, sm: StorageManager, deltaIdx: Int): (Seq[(Atom, Int)], String) = {
    val sortedBody = originalK.atoms.drop(1).zipWithIndex.sortBy((a, idx) => sortBy(a, idx == deltaIdx)).reverse

    val rStack = sortedBody.to(mutable.ListBuffer)
    var newBody = Seq[(Atom, Int)]()
    while (rStack.nonEmpty)
      var nextOpt = rStack.headOption
      while (nextOpt.nonEmpty)
        val next = nextOpt.get
        newBody = newBody :+ next
        rStack.remove(rStack.indexOf(next))

        val cxns = originalK.cxns(next._1.hash)

        if (cxns.nonEmpty)
          val availableNonoverlapping = rStack.filterNot((atom, _) => cxns.values.flatten.toSeq.contains(atom.hash))
          if (availableNonoverlapping.nonEmpty) // pick largest non-overlapping relation
            nextOpt = availableNonoverlapping.headOption
          else // pick the largest relation with the least overlap
            nextOpt = cxns.toSeq.sortBy(_._1).view.map((count, worstCxn) =>
              val availableCxn = rStack.filter((atom, _) => worstCxn.contains(atom.hash)) // use filter not intersect to retain order
              availableCxn.headOption
            ).collectFirst { case Some(x) => x }
        else
          nextOpt = None

    val newAtoms = originalK.atoms.head +: newBody.map(_._1)
    val newHash = JoinIndexes.getRuleHash(newAtoms)

//    println(s"\tOrder: ${newBody.map((a, idx) => s"${sm.ns(a.rId)}:|${sortBy(a, idx == deltaIdx)}|").mkString("", ", ", "")}")
//    if (originalK.atoms.length > 2)
//      print(s"Rule: ${sm.printer.ruleToString(originalK.atoms)} => ")
//      println(s"${sm.printer.ruleToString(originalK.atoms.head +: newBody.map(_._1))}")

    (newBody, newHash)
  }

  def presortSelect(sortBy: (Atom, Boolean) => (Boolean, Int), originalK: JoinIndexes, sm: StorageManager, deltaIdx: Int): (Seq[(Atom, Int)], String) = {

//    val sortedBody = originalK.atoms.drop(1).zipWithIndex.sortBy((a, _) => (sm.allRulesAllIndexes.contains(a.rId), sortBy(a)))
    val sortedBody = originalK.atoms.drop(1).zipWithIndex.sortBy((a, idx) => sortBy(a, idx == deltaIdx))
//    println(s"\tOrder: ${sortedBody.map((a, idx) => s"${sm.ns.hashToAtom(a.hash)}:|${sortBy(a, idx == deltaIdx)}|${if (sm.edbContains(a.rId)) "edb" else "idb"}").mkString("", ", ", "")}")
    //    if (input.length > 2)
//    println(s"Rule: ${sm.printer.ruleToString(originalK.atoms)}\n")
//    println(s"Rule cxn: ${originalK.cxnsToString(sm.ns)}\n")

    val rStack = sortedBody.to(mutable.ListBuffer)
    var newBody = Seq[(Atom, Int)]()
//    println("START, stack=" + rStack.map(_._1).mkString("[", ", ", "]"))
    while (rStack.nonEmpty)
      var nextOpt = rStack.headOption
//      println(s"\tpicking head ${sm.ns.hashToAtom(nextOpt.get._1.hash)} off stack")
      while (nextOpt.nonEmpty)
        val next = nextOpt.get
        newBody = newBody :+ next
        rStack.remove(rStack.indexOf(next))
//        println(s"\t\tbody now: ${newBody.map(_._1).map(a => sm.ns.hashToAtom(a.hash)).mkString("[", ", ", "]")}")

        val cxns = originalK.cxns(next._1.hash)
        if (cxns.nonEmpty)
//          println(s"\t\tcxns, in order: ${cxns.toSeq.sortBy(_._1).reverse.map((_, hashs) => hashs.map(r => sm.ns.hashToAtom(r)).mkString("(", ", ", ")"))}")
          nextOpt = cxns.toSeq.sortBy(_._1).reverse.view.map((count, bestCxn) =>
//            println(s"\t\t\ttesting best cxn of $count = ${bestCxn.map(r => sm.ns.hashToAtom(r)).mkString("[", ", ", "]")}")
            val availableCxn = rStack.filter((atom, _) => bestCxn.contains(atom.hash)) // use filter not intersect to retain order so we always take largest relation out of the strongest-connected relations
//            println(s"\t\t\tcxns that are still on the stack = ${availableCxn.map(p => sm.ns.hashToAtom(p._1.hash))}")
            availableCxn.headOption
          ).collectFirst { case Some(x) => x }
        else
          nextOpt = None
//        println(s"\t\t\t==>next cxn to add: ${nextOpt.map(next => sm.ns.hashToAtom(next._1.hash)).getOrElse("None")}")

    val newAtoms = originalK.atoms.head +: newBody.map(_._1)
    val newHash = JoinIndexes.getRuleHash(newAtoms)

//    if (originalK.atoms.length > 3)
//    print(s"Rule: ${sm.printer.bodyToString(originalK.atoms.drop(1))} => ")
//    println(s"${sm.printer.bodyToString(newBody.map(_._1))}")
//    println(s"test, compare ${newBody.map(_._1.rId)} to ${originalK.atoms.drop(1).toList.map(_.rId)}")
//    if (newBody.map(_._1.rId) == originalK.atoms.drop(1).toList.map(_.rId))
////      println("=> unchanged")
//      ()
//    else
//      println(s"\n\t${newBody.map((a, idx) => s"${sm.ns.hashToAtom(a.hash)}:|${sortBy(a, idx == deltaIdx)}|${if (sm.edbContains(a.rId)) "edb" else "idb"}").mkString("", ", ", "")}")
//    if (originalK.atoms.length > 2)
//      print(s"Rule: ${sm.printer.ruleToString(originalK.atoms)} => ")
//      println(s"${sm.printer.ruleToString(originalK.atoms.head +: newBody.map(_._1))}")
//    if newBody.map(_._1.rId) == originalK.atoms.drop(1).toList.map(_.rId) then println("=> unchanged") else println(s"swap: ${sm.printer.bodyToString(originalK.atoms.drop(1))} => ${sm.printer.bodyToString(newBody.map(_._1))}")
    (newBody, newHash)
  }

  def getPresort(input: Seq[ProjectJoinFilterOp], sortBy: (Atom, Boolean) => (Boolean, Int), rId: Int, originalK: JoinIndexes, sm: StorageManager)(using jitOptions: JITOptions): (Seq[ProjectJoinFilterOp], JoinIndexes) = {
    jitOptions.sortOrder match
      case SortOrder.Unordered | SortOrder.Badluck => (input, originalK)
      case SortOrder.Sel | SortOrder.Mixed | SortOrder.IntMax | SortOrder.Worst =>
        val (newBody, newHash) =
          if (jitOptions.sortOrder == SortOrder.Worst)
            presortSelectWorst(sortBy, originalK, sm, -1)
          else
            presortSelect(sortBy, originalK, sm, -1)
        val newK = sm.allRulesAllIndexes(rId).getOrElseUpdate(
          newHash,
          JoinIndexes(originalK.atoms.head +: newBody.map(_._1), Some(originalK.cxns))
        )
        (input.map(c => ProjectJoinFilterOp(rId, newK, newBody.map((_, oldP) => c.childrenSO(oldP))*)), newK)
  }

  def getOnlineSort(input: Seq[IROp[EDB]], sortBy: (Atom, Boolean) => (Boolean, Int), rId: Int, originalK: JoinIndexes, sm: StorageManager)(using jitOptions: JITOptions): (Seq[IROp[EDB]], JoinIndexes) = {
    val deltaIdx = input.indexWhere(op => // will return -1 if delta is negated relation, which is OK just ignore for now
      op match
        case o: ScanOp => o.db == DB.Delta
        case _ => false
    )
    jitOptions.sortOrder match
      case SortOrder.Unordered | SortOrder.Badluck => (input, originalK)
      case SortOrder.Sel | SortOrder.Mixed | SortOrder.IntMax | SortOrder.Worst =>
        val (newBody, newHash) =
          if (jitOptions.sortOrder == SortOrder.Worst)
            presortSelectWorst(sortBy, originalK, sm, deltaIdx)
          else
            presortSelect(sortBy, originalK, sm, deltaIdx)
        val newK = sm.allRulesAllIndexes(rId).getOrElseUpdate(
          newHash,
          JoinIndexes(originalK.atoms.head +: newBody.map(_._1), Some(originalK.cxns))
        )
        (newK.atoms.drop(1).map(a => input(originalK.atoms.drop(1).indexOf(a))), newK)
  }

  def allOrders(rule: Seq[Atom]): AllIndexes = {
    val idx = JoinIndexes(rule, None)
    mutable.Map[String, JoinIndexes](rule.drop(1).permutations.map(r =>
      val toRet = JoinIndexes(rule.head +: r, Some(idx.cxns))
      toRet.hash -> toRet
    ).toSeq*)
  }

  def getRuleHash(rule: Seq[Atom]): String = rule.map(r => r.hash).mkString("", "", "")
}
