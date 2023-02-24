package datalog.execution.ast.transform

import datalog.execution.ast.*
import datalog.storage.StorageManager

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
      case RuleNode(head, body, _, _, _) =>
        if (body.size == 1) // for now just subst simple equality
          (head, body(0)) match {
            case (h: LogicAtom, b: LogicAtom) =>
              if (h.terms == b.terms && h.terms.forall(p => p.isInstanceOf[VarTerm]))
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
        case RuleNode(head, body, atoms, keys, h) =>
          RuleNode(transform(head), body.map(transform), atoms, keys, h)
        case n: AtomNode => n match {
          case NegAtom(expr) =>
            NegAtom(transform(expr))
          case LogicAtom(relation, terms) =>
            LogicAtom(ctx.aliases.getOrElse(relation, relation), terms)
        }
        case n: TermNode => n
      }
    else
      node
  }
}
