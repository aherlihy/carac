/* Author: Benoit Maillard, adapted from https://github.com/benoitmaillard/tasty-carac */
package datalog.tastyfact

import tastyquery.Trees.Tree
import tastyquery.Trees.*
import tastyquery.Names.TermName
import tastyquery.Symbols.TermSymbol

object Traversal {
  def subtrees(tree: Tree): List[Tree] = tree match {
    case PackageDef(pid, stats)                   => stats
    case ImportSelector(imported, renamed, bound) => imported :: renamed.toList ::: bound.toList
    case Import(expr, selectors)                  => expr :: selectors
    case Export(expr, selectors)                  => expr :: selectors
    case ClassDef(name, rhs, symbol)              => rhs :: Nil
    case TypeMember(_, rhs, _)                    => rhs :: Nil
    case Template(constr, parents, self, body)    => constr :: parents ::: self.toList ::: body
    case ValDef(name, tpt, rhs, symbol)           => tpt :: rhs.toList
    case DefDef(name, params, tpt, rhs, symbol)   => params.flatMap(_.merge) ::: tpt :: rhs.toList
    case Select(qualifier, name)                  => qualifier :: Nil
    case Super(qual, mix)                         => qual :: Nil
    case Apply(fun, args)                         => fun :: args
    case TypeApply(fun, args)                     => fun :: args
    case New(tpt)                                 => tpt :: Nil
    case Typed(expr, tpt)                         => expr :: tpt :: Nil
    case Assign(lhs, rhs)                         => lhs :: rhs :: Nil
    case NamedArg(name, arg)                      => arg :: Nil
    case Block(stats, expr)                       => stats :+ expr
    case If(cond, thenPart, elsePart)             => cond :: thenPart :: elsePart :: Nil
    case InlineIf(cond, thenPart, elsePart)       => cond :: thenPart :: elsePart :: Nil
    case Lambda(meth, tpt)                        => meth :: tpt.toList
    case Match(selector, cases)                   => selector :: cases
    case InlineMatch(selector, cases)             => selector.toList ::: cases
    case CaseDef(pattern, guard, body)            => pattern :: guard.toList ::: body :: Nil
    case TypeTest(body, tpt)                      => body :: tpt :: Nil
    case Bind(name, body, symbol)                 => body :: Nil
    case Alternative(trees)                       => trees
    case Unapply(fun, implicits, patterns)        => fun :: implicits ++ patterns
    case ExprPattern(expr)                        => expr :: Nil
    case SeqLiteral(elems, elemtpt)               => elems ::: elemtpt :: Nil
    case While(cond, body)                        => cond :: body :: Nil
    case Throw(expr)                              => expr :: Nil
    case Try(expr, cases, finalizer)              => (expr :: cases) ::: finalizer.toList
    case Return(expr, from)                       => expr.toList
    case Inlined(expr, caller, bindings)          => expr :: bindings

    case SingletonTypeTree(term)                        => term :: Nil
    case RefinedTypeTree(parent, refinements, classSym) => parent :: refinements
    case ByNameTypeTree(result)                         => result :: Nil
    case AppliedTypeTree(tycon, args)                   => tycon :: args
    case TypeWrapper(tp)                                => Nil
    case SelectTypeTree(qualifier, name)                => qualifier :: Nil
    case TermRefTypeTree(qualifier, name)               => qualifier :: Nil
    case AnnotatedTypeTree(tpt, annotation)             => tpt :: annotation :: Nil
    case MatchTypeTree(bound, selector, cases)          => bound.toList ::: selector :: cases
    case TypeCaseDef(pattern, body)                     => pattern :: body :: Nil
    case TypeTreeBind(name, body, symbol)               => body :: Nil
    case WildcardTypeBoundsTree(bounds)                 => bounds :: Nil
    case TypeLambdaTree(tparams, body)                  => tparams ::: body :: Nil

    case InferredTypeBoundsTree(bounds)               => Nil
    case ExplicitTypeBoundsTree(low, high)            => low :: high :: Nil
    case TypeAliasDefinitionTree(alias)               => alias :: Nil
    case OpaqueTypeAliasDefinitionTree(bounds, alias) => bounds :: alias :: Nil
    case PolyTypeDefinitionTree(tparams, body)        => tparams ::: body :: Nil
    case NamedTypeBoundsTree(name, bounds)            => Nil
    case _: ImportIdent | _: TypeMember | _: TypeParam | _: Ident | _: This | _: New | _: Literal | _: SelfDef |
        _: WildcardPattern | _: TypeIdent =>
      Nil
  }

  def walkTreeWithContext[R, C](tree: Tree)(con: (Tree, C) => C, initC: C)(op: (Tree, C) => R)(reduce: (R, R) => R, default: => R): R = {
    def rec(c: C)(t: Tree): R = reduce(op(t, c), subtrees(t).map(rec(con(t, c))).foldLeft(default)(reduce))
    rec(initC)(tree)
  }

  def walkTreeWithMethod[R](tree: Tree)(op: (Tree, Seq[TermSymbol]) => R)(reduce: (R, R) => R, default: => R): R = {
    def accumulate(tree: Tree, acc: Seq[TermSymbol]): Seq[TermSymbol] = tree match {
      // TODO add missing cases (probably good enough for now)
      case DefDef(name, params, tpt, rhs, symbol) => acc :+ symbol
      case _ => acc
    }
    walkTreeWithContext(tree)(accumulate, Seq.empty)(op)(reduce, default)
  }
}
