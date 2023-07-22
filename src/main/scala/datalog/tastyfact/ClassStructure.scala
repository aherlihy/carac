/* Author: Benoit Maillard, adapted from https://github.com/benoitmaillard/tasty-carac */
package datalog.tastyfact

import tastyquery.Contexts.Context
import tastyquery.Symbols.ClassSymbol
import tastyquery.Trees.ValOrDefDef
import tastyquery.Symbols.TermSymbol
import tastyquery.Symbols.TermOrTypeSymbol

import Facts.*
import Symbols.Table

/* When we have a ClassSymbol class, we want to be able to know all the members
- generate a MemberDef(class, member) if there is a definition (overriden or not)
- generate a NotMemberDef(class, member) if it is inherited

Alternatively, we could also directly generate LookUp facts
- However, if negation works, it is much easier to only generate MemberDef
- Probably best to implement the closest thing to negative facts */

class ClassStructure(table: Table)(using Context) {
  // all (non-private) members of a class
  def inherited(cs: ClassSymbol): List[TermOrTypeSymbol] =
    val overriden = cs.declarations.flatMap(_.allOverriddenSymbols).toSet
    cs.parentClasses.flatMap(parent => {
      inherited(parent) ++ parent.declarations
    }).filter(!overriden.contains(_))

  // generates all Extends/Defines/NotDefines for cs
  def definitionFacts(cs: ClassSymbol): List[Fact] =
    val defined = cs.declarations.flatMap(s =>
      s.allOverriddenSymbols.toList match {
        case Nil => DefinesWith(
            table.getSymbolId(cs),
            table.getSymbolId(s),
            table.getSymbolId(s)
          ) :: Nil
        case l => l.map(r => 
          DefinesWith(
            table.getSymbolId(cs),
            table.getSymbolId(r),
            table.getSymbolId(s)
          )
        )
      })
    
    // if we have negations we don't need to generate these facts
    val inheritedDefintions = inherited(cs).map(s => NotDefines(table.getSymbolId(cs), table.getSymbolId(s)))

    cs.parentClasses.map(c =>
      Extends(table.getSymbolId(cs), table.getSymbolId(c))
    ) ++ defined ++ inheritedDefintions
}
