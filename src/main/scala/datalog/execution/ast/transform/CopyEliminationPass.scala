package datalog.execution.ast.transform

import datalog.execution.ast.*

import scala.collection.mutable.{ArrayBuffer, Map}

// POC for static AST rewrites, probably want to refactor into miniphases to save traversals
/**
 * Remove simple redefinitions of rules, ex: oneHop(x, y) :- edge (x,y) ==> oneHop should be removed and edge used everwhere oneHop is used
 */
class CopyEliminationPass() extends Transformer {
  private val aliases = Map[Int, Int]()
  def checkAlias(node: ASTNode): Unit = {
    node match {
      case ProgramNode(allRules) =>
        allRules.map((rId, rules) => {
          checkAlias(rules)
        })
      case AllRulesNode(rules, _) =>
        if (rules.size == 1)
          checkAlias(rules.head)
      case RuleNode(head, body, _) =>
        if (body.size == 1) // for now just subst simple equality
          (head, body(0)) match {
            case (h: LogicAtom, b: LogicAtom) =>
              if (h.terms == b.terms)
                aliases(h.relation) = aliases.getOrElse(b.relation, b.relation)
            case _ =>
          }
      case _ =>
    }
  }
  override def transform(node: ASTNode): ASTNode = {
    checkAlias(node)
    if (aliases.nonEmpty)
      node match {
        case ProgramNode(m) =>
          ProgramNode(m.
            filter((rId, allRules) => !aliases.contains(rId)).
            map((rId, allRules) => (rId, transform(allRules)))
          ) // delete aliases
        case AllRulesNode(rules, rId) =>
          AllRulesNode(rules.map(transform), rId)
        case RuleNode(head, body, _) =>
          RuleNode(transform(head), body.map(transform))
        case n: AtomNode => n match {
          case NegAtom(expr) =>
            NegAtom(transform(expr))
          case LogicAtom(relation, terms) =>
            LogicAtom(aliases.getOrElse(relation, relation), terms)
        }
        case n: TermNode => n
      }
    else
      node
  }
}
