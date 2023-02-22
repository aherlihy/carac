package datalog.execution.ast.transform

import datalog.dsl.{Constant, Variable}
import datalog.execution.ast.{ASTNode, AllRulesNode, AtomNode, ConstTerm, LogicAtom, NegAtom, ProgramNode, RuleNode, TermNode, VarTerm}
import datalog.execution.JoinIndexes
import datalog.storage.StorageManager
import datalog.tools.Debug.debug

import scala.collection.mutable

/**
 * Decorate nodes with join info. TODO: mutate instead of copy?
 */
class JoinIndexPass(using ASTTransformerContext) extends Transformer {
  override def transform(node: ASTNode)(using sm: StorageManager): ASTNode = {
    node match {
      case ProgramNode(idbs) => ProgramNode(idbs.map((rId, allRules) => (rId, transform(allRules))))
      case AllRulesNode(rules, rId, edb) => AllRulesNode(rules.map(transform), rId, edb)
      case RuleNode(h, b, atoms, _) =>
        val constants = mutable.Map[Int, Constant]() // position => constant
        val variables = mutable.Map[Variable, Int]() // v.oid => position
        // TODO: do without cast, handle neg node
        val head = h.asInstanceOf[LogicAtom]
        val body = b.map(n => n.asInstanceOf[LogicAtom]).sortBy(a =>
          if (sm.edbs.contains(a.relation))
            sm.edb(a.relation).size
          else
            0
        )
        val sortedAtoms = atoms.head +: atoms.drop(1).sortBy(a => // TODO: keep atoms around for online join order switching, TODO: don't really need sub-AST and atoms at the same time, get rid of one
          if (sm.edbs.contains(a.rId))
            sm.edb(a.rId).size
          else
            0
        )
        debug("", () => if (atoms != sortedAtoms) s"default atoms=$atoms and sorted=$sortedAtoms" else "")
        // TODO: replace with JoinIndexes
        val deps = body.map(a => a.relation)

        val bodyVars = body
          .flatMap(a => a.terms)
          .zipWithIndex // terms, position
          .groupBy(z => z._1)
          .filter((term, matches) => // matches = Seq[(var, pos1), (var, pos2), ...]
            term match {
              case VarTerm(v) =>
                variables(v) = matches.head._2 // first idx for a variable
                !v.anon && matches.size >= 2
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
        ctx.precedenceGraph.addNode(head.relation, deps)
        RuleNode(head, body, sortedAtoms, Some(JoinIndexes(bodyVars, constants.toMap, projects, deps, atoms)))
      case n:AtomNode => n
      case n:TermNode => n
    }
  }
}
