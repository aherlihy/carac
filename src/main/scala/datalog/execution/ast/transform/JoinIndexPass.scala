package datalog.execution.ast.transform

import datalog.dsl.{Variable, Constant}
import datalog.execution.ast.{ASTNode, AllRulesNode, AtomNode, ConstTerm, LogicAtom, NegAtom, ProgramNode, RuleNode, TermNode, VarTerm}
import datalog.execution.JoinIndexes

import scala.collection.mutable

/**
 * Decorate nodes with join info. TODO: mutate instead of copy?
 */
class JoinIndexPass extends Transformer {
  override def transform(node: ASTNode): ASTNode = {
    node match {
      case ProgramNode(idbs) => ProgramNode(idbs.map((rId, allRules) => (rId, transform(allRules))))
      case AllRulesNode(rules, rId) => AllRulesNode(rules.map(transform), rId)
      case RuleNode(h, b, _) =>
        val constants = mutable.Map[Int, Constant]() // position => constant
        val variables = mutable.Map[Variable, Int]() // v.oid => position
        // TODO: do without cast, handle neg node
        val head = h.asInstanceOf[LogicAtom]
        val body = b.map(n => n.asInstanceOf[LogicAtom])

        val deps = body.map(a => a.relation)

        val bodyVars = body
          .flatMap(a => a.terms)
          .zipWithIndex // terms, position
          .groupBy(z => z._1)
          .filter((term, matches) => // matches = Seq[(var, pos1), (var, pos2), ...]
            term match {
              case VarTerm(v) =>
                variables(v) = matches.head._2 // first idx for a variable
                !v.anon && matches.length >= 2
              case ConstTerm(c) =>
                matches.foreach((_, idx) => constants(idx) = c)
                false
            }
          )
          .map((term, matches) => // get rid of groupBy elem in result tuple
            matches.map(_._2).toIndexedSeq
          )
          .toIndexedSeq

        // variable ids in the head atom
        val projects = head.terms.map {
          case VarTerm(v) =>
            if (!variables.contains(v)) throw new Exception(f"Free variable in rule head with varId $v.oid")
            if (v.anon) throw new Exception("Anonymous variable ('__') not allowed in head of rule")
            ("v", variables(v))
          case ConstTerm(c) => ("c", c)
        }.toIndexedSeq
        RuleNode(head, body, Some(JoinIndexes(bodyVars, constants.toMap, projects, deps)))
      case n:AtomNode => n
      case n:TermNode => n
    }
  }
}
