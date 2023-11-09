package datalog.execution.ast.transform

import datalog.execution.ast.*
import datalog.storage.StorageManager
import datalog.dsl.{Atom, GroupingAtom}
import datalog.execution.{JoinIndexes, heuristics}

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, Map}

// POC for static AST rewrites, probably want to refactor into miniphases to save traversals
/**
 * Remove simple redefinitions of rules, ex: oneHop(x, y) :- edge (x,y) ==> oneHop should be removed and edge used everwhere oneHop is used
 */
class CopyEliminationPass()(using ASTTransformerContext) extends Transformer {
  def checkAlias(node: ASTNode): Unit = {
    node match {
      case ProgramNode(allRules) =>
        allRules.map((rId, rules) => {
          checkAlias(rules)
        })
      case AllRulesNode(rules, _, edb) =>
        if (rules.size == 1 && !edb)
          checkAlias(rules.head)
      case RuleNode(head, body, _, _) =>
        if (body.size == 1) // for now just subst simple equality
          (head, body(0)) match {
            case (h: LogicAtom, b: LogicAtom) =>
              if (h.terms == b.terms && h.terms.forall(p => p.isInstanceOf[VarTerm]) && h.negated == b.negated)
                ctx.aliases(h.relation) = ctx.aliases.getOrElse(b.relation, b.relation)
            case _ =>
          }
      case _ =>
    }
  }
  override def transform(node: ASTNode)(using StorageManager): ASTNode = {
    checkAlias(node)
    if (ctx.aliases.nonEmpty)
      node match {
        case ProgramNode(m) =>
          ProgramNode(m.
            filter((rId, allRules) => !ctx.aliases.contains(rId)).
            map((rId, allRules) => (rId, transform(allRules)))
          ) // delete aliased rules
        case AllRulesNode(rules, rId, edb) =>
          AllRulesNode(rules.map(transform), rId, edb)
        case RuleNode(head, body, atoms, k) =>
          var aliased = false
          var newK = k
          val transformedAtoms = atoms.head +: atoms.drop(1).map(a =>
            if (ctx.aliases.contains(a.rId))
              aliased = true
              a match
                case ga: GroupingAtom =>
                  GroupingAtom(Atom(ctx.aliases.getOrElse(a.rId, a.rId), ga.gp.terms, ga.gp.negated), ga.gv, ga.ags)
                case a => 
                  Atom(ctx.aliases.getOrElse(a.rId, a.rId), a.terms, a.negated)
            else
              a
          )
          if (aliased)
            newK = JoinIndexes(transformedAtoms, None, None)
            ctx.sm.allRulesAllIndexes.getOrElseUpdate(transformedAtoms.head.rId, mutable.Map[String, JoinIndexes]()).addOne(newK.hash, newK)
            if (body.size < heuristics.max_length_cache)
              val allK = JoinIndexes.allOrders(transformedAtoms)
              ctx.sm.allRulesAllIndexes(transformedAtoms.head.rId) ++= allK

            ctx.precedenceGraph.addNode(transformedAtoms)
            ctx.precedenceGraph.updateNodeAlias(ctx.aliases)

          RuleNode(transform(head), body.map(transform), transformedAtoms, newK)
        case n: AtomNode => n match {
          case LogicAtom(relation, terms, neg) =>
            LogicAtom(ctx.aliases.getOrElse(relation, relation), terms, neg)
          case LogicGroupingAtom(LogicAtom(relation, terms, neg), gv, ags) =>
            LogicGroupingAtom(LogicAtom(ctx.aliases.getOrElse(relation, relation), terms, neg), gv, ags)
        }
        case n: TermNode => n
      }
    else
      node
  }
}
