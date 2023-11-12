package datalog.execution

import datalog.dsl.{Atom, Constant, Variable, Term, GroupingAtom, AggOp, Comparison, Expression, Constraint}
import datalog.execution.ir.{IROp, ProjectJoinFilterOp, ScanOp}
import datalog.storage.{DB, EDB, NS, RelationId, StorageManager, StorageAggOp, StorageComparison, StorageExpression, getType, comparisons}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.quoted.*
import scala.reflect.ClassTag

type AllIndexes = mutable.Map[String, JoinIndexes]

enum PredicateType:
  case POSITIVE, NEGATED, GROUPING


enum AggOpIndex:
  case LV(i: Int)
  case GV(i: Int)
  case C(c: Constant)

case class GroupingJoinIndexes(varIndexes: Seq[Seq[Int]],
                               constIndexes: mutable.Map[Int, Constant],
                               groupingIndexes: Seq[Int],
                               aggOpInfos: Seq[(StorageAggOp, AggOpIndex)]
                              )

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
 * @param negationInfo - information needed to build the complement relation of negated atoms: for each term, either a constant or a list of pairs (relationid, column) of the ocurrences of the variable in the rule (empty for anonynous variable)
 */
case class JoinIndexes(varIndexes: Seq[Seq[Int]],
                       constIndexes: mutable.Map[Int, Constant],
                       projIndexes: Seq[(String, Constant)],
                       deps: Seq[(PredicateType, RelationId)],
                       atoms: Seq[Atom],
                       cxns: mutable.Map[String, mutable.Map[Int, Seq[String]]],
                       cons: Seq[(Option[Boolean], StorageComparison, StorageExpression, StorageExpression, Int)],
                       constraints: Seq[Constraint],
                       negationInfo: Map[String, Seq[Either[Constant, Seq[(RelationId, Int)]]]],
                       edb: Boolean = false,
                       groupingIndexes: Map[String, GroupingJoinIndexes] = Map.empty
                      ) {
  override def toString(): String = ""//toStringWithNS(null)

  def toStringWithNS(ns: NS): String = "{ vars:" + varToString() +
      ", consts:" + constToString() +
      ", project:" + projToString() +
      ", deps:" + depsToString(ns) +
      ", edb:" + edb +
      ", cxn: " + cxnsToString(ns) +
      ", cons: " + consToString() +
      ", negation: " + negationToString(ns) +
      " }"

  def varToString(): String = varIndexes.map(v => v.mkString("$", "==$", "")).mkString("[", ",", "]")
  def constToString(): String = constIndexes.map((k, v) => s"$k==$v").mkString("{", "&&", "}")
  def projToString(): String = projIndexes.map((typ, v) => s"$typ$v").mkString("[", " ", "]")
  def depsToString(ns: NS): String = deps.map((typ, rId) => s"$typ${ns(rId)}").mkString("[", ", ", "]")
  def cxnsToString(ns: NS): String =
    cxns.map((h, inCommon) =>
      s"{ ${ns.hashToAtom(h)} => ${
        inCommon.map((count, hashs) =>
          count.toString + ": " + hashs.map(h => ns.hashToAtom(h)).mkString("", "|", "")
        ).mkString("", ", ", "")} }").mkString("[", ",\n", "]")
  def consToString(): String = cons.map((o, sc, a, b, _) => s"$o#$sc($a,$b)").mkString("{", ", ", "}")
  def negationToString(ns: NS): String =
    negationInfo.map((h, infos) =>
      s"{ ${ns.hashToAtom(h)} => ${
        infos.map{
          case Left(value) => value
          case Right(value) => s"[ ${value.map((r, c) => s"(${ns(r)}, $c)")} ]"
        }} }").mkString("[", ",\n", "]")
  val hash: String = atoms.map(a => a.hash).mkString("", "", "") + constraints.map(a => a.hash).mkString("", "", "")

  val pos2Term: Int => Term = atoms.tail.flatMap(_.terms).apply
}

object JoinIndexes {
  def apply(rule: Seq[Atom], constraints: Seq[Constraint],
    precalculatedCxns: Option[mutable.Map[String, mutable.Map[Int, Seq[String]]]],
    consHint: Option[(Seq[(Option[Boolean], StorageComparison, StorageExpression, StorageExpression, Int)], Int => Term)],
    precalculatedGroupingIndexes: Option[Map[String, GroupingJoinIndexes]]) = {
    val constants = mutable.Map[Int, Constant]() // position => constant
    val variables = mutable.Map[Variable, Int]() // v.oid => position

    val body = rule.drop(1)

    val deps = body.map(a => (
      a match
        case _: GroupingAtom => PredicateType.GROUPING
        case _ => if (a.negated) PredicateType.NEGATED else PredicateType.POSITIVE
      , a.rId))

    val bodyVars = body
      .flatMap(a => a.terms.zipWithIndex.map((t, i) => (t, (a.negated, a.isInstanceOf[GroupingAtom] && i >= a.asInstanceOf[GroupingAtom].gv.length))))  // all terms in one seq
      .zipWithIndex               // term, position
      .groupBy(z => z._1._1)      // group by term
      .filter((term, matches) =>  // matches = Seq[(var, pos1), (var, pos2), ...]
        term match {
          case v: Variable =>
            val wrong = v.oid != -1 && matches.exists(_._1._2._1) && matches.forall(x => x._1._2._1 || x._1._2._2)  // Var occurs negated and all occurrences are either negated or aggregated
            if wrong then
              throw new Exception(s"Variable with varId ${v.oid} appears only in negated atoms (and possibly in aggregated positions of grouping atoms)")
            else
              if (v.oid != -1)
                variables(v) = matches.find(!_._1._2._1).get._2
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


    val variables2 = body.filterNot(_.negated).flatMap(a =>
      a.terms.zipWithIndex.collect{ case (v: Variable, i) if !v.anon => (v, i) }.map((v, i) => (v, (a.rId, i)))
    ).groupBy(_._1).view.mapValues(_.map(_._2))

    val negationInfo = body.filter(_.negated).map(a =>
      a.hash -> a.terms.map{
        case c: Constant => Left(c)
        case v: Variable => Right(if v.anon then Seq() else variables2(v))
      }
    ).toMap

    //groupings
    val groupingIndexes = precalculatedGroupingIndexes.getOrElse(
      body.collect{ case ga: GroupingAtom => ga }.map(ga =>
        val (varsp, ctans) = ga.gp.terms.zipWithIndex.partitionMap{
          case (v: Variable, i) => Left((v, i))
          case (c: Constant, i) => Right((c, i))
        }
        val vars = varsp.filterNot(_._1.anon)
        val gis = ga.gv.map(v => vars.find(_._1 == v).get).map(_._2)
        ga.hash -> GroupingJoinIndexes(
          vars.groupBy(_._1).values.filter(_.size > 1).map(_.map(_._2)).toSeq,
          ctans.map(_.swap).to(mutable.Map),
          gis,
          ga.ags.map(_._1).map(ao =>
            val aoi = ao.t match
              case v: Variable =>
                val i = ga.gv.indexOf(v)
                if i >= 0 then AggOpIndex.GV(gis(i)) else AggOpIndex.LV(vars.find(_._1 == v).get._2)
              case c: Constant => AggOpIndex.C(c)
            ao match
              case AggOp.SUM(t) => (StorageAggOp.SUM, aoi)
              case AggOp.COUNT(t) => (StorageAggOp.COUNT, aoi)
              case AggOp.MIN(t) => (StorageAggOp.MIN, aoi)
              case AggOp.MAX(t) => (StorageAggOp.MAX, aoi)
          )
        )
      ).toMap
    )

    
    val cons = consHint.map((c, m) => c.map((o, c, l, r, _) =>
      val fl = fixExpression(l, variables.apply, m)
      val fr = fixExpression(r, variables.apply, m) 
      (o, c, fl, fr, (maxIndex(fl) ++ maxIndex(fr)).reduceOption(Math.max(_, _)).getOrElse(0)))
    )
    .getOrElse(
      constraints.map(con =>
        checkExpression(con.l, variables.keySet.toSet)
        checkExpression(con.r, variables.keySet.toSet)
        val sl = translateExpression(simplifyExpression(con.l), variables.apply)
        val sr = translateExpression(simplifyExpression(con.r), variables.apply)
        val sc = con.c match
          case Comparison.EQ => StorageComparison.EQ 
          case Comparison.NEQ => StorageComparison.NEQ
          case Comparison.LT => StorageComparison.LT
          case Comparison.LTE => StorageComparison.LTE
          case Comparison.GT => StorageComparison.GT
          case Comparison.GTE => StorageComparison.GTE
        (
          (sl, sr) match
            case (StorageExpression.One(c1: Constant), StorageExpression.One(c2: Constant)) => Some(comparisons(sc)(getType(c1))(c1, c2))
            case _ => None
          ,
          sc,
          sl,
          sr,
          (maxIndex(sl) ++ maxIndex(sr)).reduceOption(Math.max(_, _)).getOrElse(0)
        )
      )
    )

    new JoinIndexes(bodyVars, constants.to(mutable.Map), projects, deps, rule, cxns, cons, constraints, negationInfo, edb = false, groupingIndexes = groupingIndexes)
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
    val newHash = JoinIndexes.getRuleHash(newAtoms, originalK.constraints)

//    println(s"\tOrder: ${newBody.map((a, _) => s"${sm.ns(a.rId)}:|${sortBy(a)}|").mkString("", ", ", "")}")
//    if (originalK.atoms.length > 3)
//      print(s"Rule: ${sm.printer.ruleToString(originalK.atoms)} => ")
//      println(s"${sm.printer.ruleToString(originalK.atoms.head +: newBody.map(_._1))}")

    (newBody, newHash)
  }

  def presortSelect(sortBy: (Atom, Boolean) => (Boolean, Int), originalK: JoinIndexes, sm: StorageManager, deltaIdx: Int): (Seq[(Atom, Int)], String) = {

//    val sortedBody = originalK.atoms.drop(1).zipWithIndex.sortBy((a, _) => (sm.allRulesAllIndexes.contains(a.rId), sortBy(a)))
    val sortedBody = originalK.atoms.drop(1).zipWithIndex.sortBy((a, idx) => sortBy(a, idx == deltaIdx))
//    println(s"\tOrder: ${sortedBody.map((a, _) => s"${sm.ns.hashToAtom(a.hash)}:|${sortBy(a)}|${if (sm.edbContains(a.rId)) "edb" else "idb"}").mkString("", ", ", "")}")
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
    val newHash = JoinIndexes.getRuleHash(newAtoms, originalK.constraints)

//    if (originalK.atoms.length > 3)
//      print(s"Rule: ${sm.printer.ruleToString(originalK.atoms)} => ")
//      println(s"${sm.printer.ruleToString(originalK.atoms.head +: newBody.map(_._1))}")
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
          JoinIndexes(originalK.atoms.head +: newBody.map(_._1), originalK.constraints, Some(originalK.cxns), Some((originalK.cons, originalK.pos2Term)), Some(originalK.groupingIndexes))
        )
        (input.map(c => ProjectJoinFilterOp(rId, newK, newBody.map((_, oldP) => c.childrenSO(oldP)): _*)), newK)
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
          JoinIndexes(originalK.atoms.head +: newBody.map(_._1), originalK.constraints, Some(originalK.cxns), Some((originalK.cons, originalK.pos2Term)), Some(originalK.groupingIndexes))
        )
        (newK.atoms.drop(1).map(a => input(originalK.atoms.drop(1).indexOf(a))), newK)
  }

  def allOrders(rule: Seq[Atom], constraints: Seq[Constraint]): AllIndexes = {
    val idx = JoinIndexes(rule, constraints, None, None, None)
    mutable.Map[String, JoinIndexes](idx.atoms.drop(1).permutations.map(r =>
      val toRet = JoinIndexes(rule.head +: r, idx.constraints, Some(idx.cxns), Some(idx.cons, idx.pos2Term), Some(idx.groupingIndexes))
      toRet.hash -> toRet
    ).toSeq:_*)
  }

  def getRuleHash(rule: Seq[Atom], constraints: Seq[Constraint]): String = rule.map(r => r.hash).mkString("", "", "") + constraints.map(a => a.hash).mkString("", "", "")
}

// ---

private def simplifyExpression(e: Expression): Expression =
  enum ReduceOP:
    case ADD, SUB, MUL, DIV, MOD
  def reduceConstants(c1: Constant, c2: Constant, rop: ReduceOP): Constant = (c1, c2) match
    case (i1: Int, i2: Int) => rop match
      case ReduceOP.ADD => i1 + i2
      case ReduceOP.SUB => i1 - i2
      case ReduceOP.MUL => i1 * i2
      case ReduceOP.DIV => i1 / i2
      case ReduceOP.MOD => i1 % i2
    case (i1: String, i2: String) => rop match
      case ReduceOP.ADD => i1 + i2
      case _ => ???
    case _ => ???      
  import Expression.*
  e match
    case One(t) => e
    case Add(l, r) => simplifyExpression(l) match
      case ne @ One(t) => (t, r) match
        case (c1: Constant, c2: Constant) => One(reduceConstants(c1, c2, ReduceOP.ADD))
        case (c1: Constant, c2: Variable) => Add(One(c2), c1)
        case _ => Add(ne, r)
      case ne @ Add(l2, r2) => (r2, r) match
        case (c1: Constant, c2: Constant) => Add(l2, reduceConstants(c1, c2, ReduceOP.ADD))
        case (c1: Constant, c2: Variable) => Add(Add(l2, c2), c1)
        case _ => Add(ne, r)
      case ne @ Sub(l2, r2) => (r2, r) match
        case (c1: Constant, c2: Constant) => Add(l2, reduceConstants(c2, c1, ReduceOP.SUB))
        case (c1: Constant, c2: Variable) => Sub(Add(l2, c2), c1)
        case _ => Add(ne, r)
      case ne => Add(ne, r)
    case Sub(l, r) => simplifyExpression(l) match
      case ne @ One(t) => (t, r) match
        case (c1: Constant, c2: Constant) => One(reduceConstants(c1, c2, ReduceOP.SUB))
        case _ => Sub(ne, r) // Case (constant, variable) could be simplified
      case ne @ Add(l2, r2) => (r2, r) match
        case (c1: Constant, c2: Constant) => Add(l2, reduceConstants(c1, c2, ReduceOP.SUB))
        case (c1: Constant, c2: Variable) => Add(Sub(l2, c2), c1)
        case _ => Sub(ne, r)
      case ne @ Sub(l2, r2) => (r2, r) match
        case (c1: Constant, c2: Constant) => Sub(l2, reduceConstants(c1, c2, ReduceOP.ADD))
        case (c1: Constant, c2: Variable) => Sub(Sub(l2, c2), c1)
        case _ => Sub(ne, r)
      case ne => Sub(ne, r)
    case Mul(l, r) => simplifyExpression(l) match
      case ne @ One(t) => (t, r) match
        case (c1: Constant, c2: Constant) => One(reduceConstants(c1, c2, ReduceOP.MUL))
        case (c1: Constant, c2: Variable) => Mul(One(c2), c1)
        case _ => Mul(ne, r)
      case ne @ Mul(l2, r2) => (r2, r) match
        case (c1: Constant, c2: Constant) => Mul(l2, reduceConstants(c1, c2, ReduceOP.MUL))
        case (c1: Constant, c2: Variable) => Mul(Mul(l2, c2), c1)
        case _ => Mul(ne, r)
      case ne @ Div(l2, r2) => (r2, r) match
        case (c1: Constant, c2: Variable) => Div(Mul(l2, c2), c1)
        case _ => Mul(ne, r)
      case ne => Mul(ne, r)
    case Div(l, r) => simplifyExpression(l) match
      case ne @ One(t) => (t, r) match
        case (c1: Constant, c2: Constant) => One(reduceConstants(c1, c2, ReduceOP.DIV))
        case _ => Div(ne, r) // Case (constant, variable) could be simplified
      case ne @ Mul(l2, r2) => (r2, r) match
        case (c1: Constant, c2: Variable) => Mul(Div(l2, c2), c1)
        case _ => Div(ne, r)
      case ne @ Div(l2, r2) => (r2, r) match
        case (c1: Constant, c2: Constant) => Div(l2, reduceConstants(c1, c2, ReduceOP.MUL))
        case (c1: Constant, c2: Variable) => Div(Div(l2, c2), c1)
        case _ => Div(ne, r)
      case ne => Div(ne, r)
    case Mod(l, r) => simplifyExpression(l) match
      case ne @ One(t) => (t, r) match
        case (c1: Constant, c2: Constant) => One(reduceConstants(c1, c2, ReduceOP.MOD))
        case _ => Mod(ne, r)
      case ne => Mod(ne, r)

private def checkExpression(e: Expression, vars: Set[Variable]): Unit =
  def checkTerm(t: Term): Unit = t match
    case v: Variable =>
      if (!vars.contains(v))
        throw new Exception(s"Variable with varId ${v.oid} appears only in comparison atoms")
    case _ => ()
  import Expression.*
  e match
    case One(t) => checkTerm(t)
    case Add(l, r) => checkExpression(l, vars); checkTerm(r)
    case Sub(l, r) => checkExpression(l, vars); checkTerm(r)
    case Mul(l, r) => checkExpression(l, vars); checkTerm(r)
    case Div(l, r) => checkExpression(l, vars); checkTerm(r)
    case Mod(l, r) => checkExpression(l, vars); checkTerm(r)


private def translateExpression(e: Expression, m: Variable => Int): StorageExpression =
  import Expression as E
  import StorageExpression as SE
  def translateTerm(t: Term): Either[Constant, Int] = t match
    case c: Constant => Left(c)
    case v: Variable => Right(m(v))  
  e match
    case E.One(t) => SE.One(translateTerm(t))
    case E.Add(l, r) => SE.Add(translateExpression(l, m), translateTerm(r))
    case E.Sub(l, r) => SE.Sub(translateExpression(l, m), translateTerm(r))
    case E.Mul(l, r) => SE.Mul(translateExpression(l, m), translateTerm(r))
    case E.Div(l, r) => SE.Div(translateExpression(l, m), translateTerm(r))
    case E.Mod(l, r) => SE.Mod(translateExpression(l, m), translateTerm(r))

private def fixExpression(se: StorageExpression, m: Variable => Int, rm: Int => Term): StorageExpression =
  import StorageExpression.*
  se match
    case One(t) => One(t.map(x => m(rm(x).asInstanceOf[Variable])))
    case Add(l, r) => Add(fixExpression(l, m, rm), r.map(x => m(rm(x).asInstanceOf[Variable])))
    case Sub(l, r) => Sub(fixExpression(l, m, rm), r.map(x => m(rm(x).asInstanceOf[Variable])))
    case Mul(l, r) => Mul(fixExpression(l, m, rm), r.map(x => m(rm(x).asInstanceOf[Variable])))
    case Div(l, r) => Div(fixExpression(l, m, rm), r.map(x => m(rm(x).asInstanceOf[Variable])))
    case Mod(l, r) => Mod(fixExpression(l, m, rm), r.map(x => m(rm(x).asInstanceOf[Variable])))

private def maxIndex(se: StorageExpression): Option[Int] =
  import StorageExpression.*
  se match
    case One(t) => t.toOption
    case Add(l, r) => (maxIndex(l) ++ r.toOption).reduceOption(Math.max(_, _))
    case Sub(l, r) => (maxIndex(l) ++ r.toOption).reduceOption(Math.max(_, _))
    case Mul(l, r) => (maxIndex(l) ++ r.toOption).reduceOption(Math.max(_, _))
    case Div(l, r) => (maxIndex(l) ++ r.toOption).reduceOption(Math.max(_, _))
    case Mod(l, r) => (maxIndex(l) ++ r.toOption).reduceOption(Math.max(_, _))
  