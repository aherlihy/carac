/* Author: Benoit Maillard, adapted from https://github.com/benoitmaillard/tasty-carac */
package datalog.tastyfact.rulesets

import datalog.dsl.Program
import datalog.dsl.Relation

trait RuleSet {
  def defineRules(program: Program): Relation[String]
}
