package datalog.execution.ast.transform

import datalog.dsl.Atom
import datalog.execution.JoinIndexes
import datalog.execution.ast.*
import datalog.storage.StorageManager

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, Map}

// POC for static AST rewrites, probably want to refactor into miniphases to save traversals

/**
 * Rewrite negated rules
 */
class NegationPass()(using ASTTransformerContext) extends Transformer {
  override def transform(node: ASTNode)(using sm: StorageManager): ASTNode = {
    node match {
      case ProgramNode(idbs) => // just recur to get to AllRules
        ProgramNode(idbs.map((rId, allRules) => (rId, transform(allRules))))
      case AllRulesNode(rules, rId, edb) =>
        if (edb)
          node
        else
          AllRulesNode(
            rules.flatMap(subNode =>
              subNode match
                case RuleNode(head, body, dslAtoms, currentRuleHash) =>
                  val joinIdx = sm.allRulesAllIndexes(rId)(currentRuleHash)
                  if (joinIdx.deps.exists(_._1 == "-")) // negation in rule body, may need to rewrite
                    Set(subNode)
                  else
                    Set(subNode)
                case _ => throw new Exception("Internal error, malformed AST")
          ), rId, edb)
      case _ => node
    }
  }
}
