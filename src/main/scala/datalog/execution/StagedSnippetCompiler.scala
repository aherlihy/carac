package datalog.execution

import datalog.dsl.{Atom, Constant, Variable, Term}
import datalog.execution.ir.*
import datalog.storage.{StorageManager, DB, KNOWLEDGE, EDB}
import datalog.tools.Debug.debug

import scala.quoted.*

/**
 * Instead of compiling entire subtree, compile only contents of the node and call into continue.
 * Unclear if it will ever be useful to only compile a single, mid-tier node, and go back to
 * interpretation (maybe for save points for de-optimizations?) but this is mostly just a POC
 * that it's possible.
 */
class StagedSnippetCompiler(val storageManager: StorageManager)(using val jitOptions: JITOptions) {
  given ToExpr[Constant] with {
    def apply(x: Constant)(using Quotes) = {
      x match {
        case i: Int => Expr(i)
        case s: String => Expr(s)
      }
    }
  }

  given ToExpr[Variable] with {
    def apply(x: Variable)(using Quotes) = {
      '{ Variable(${ Expr(x.oid) }, ${ Expr(x.anon)}) }
    }
  }

  given ToExpr[Term] with {
    def apply(x: Term)(using Quotes) = {
      x match {
        case v: Variable => Expr(v)
        case c: Constant => Expr(c)
      }
    }
  }

  given ToExpr[Atom] with {
    def apply(x: Atom)(using Quotes) = {
      '{ Atom( ${ Expr(x.rId) }, ${ Expr.ofSeq(x.terms.map(y => Expr(y))) }, ${ Expr(x.negated) } ) }
    }
  }

  given ToExpr[JoinIndexes] with {
    def apply(x: JoinIndexes)(using Quotes) = {
      '{
        JoinIndexes(
          ${ Expr(x.varIndexes) },
          ${ Expr(x.constIndexes) },
          ${ Expr(x.projIndexes) },
          ${ Expr(x.deps) },
          ${ Expr(x.atoms) },
          ${ Expr(x.edb) },
        ) }
    }
  }

  def compileIRRelOp(irTree: IROp[EDB])(using stagedSM: Expr[StorageManager])(using stagedFns: Expr[Seq[CompiledFn[EDB]]])(using Quotes): Expr[EDB] = {
    irTree match {
      case ScanOp(rId, db, knowledge) =>
        db match {
          case DB.Derived =>
            knowledge match {
              case KNOWLEDGE.New =>
                '{ $stagedSM.getNewDerivedDB(${ Expr(rId) }) }
              case KNOWLEDGE.Known =>
                '{ $stagedSM.getKnownDerivedDB(${ Expr(rId) }) }
            }
          case DB.Delta =>
            knowledge match {
              case KNOWLEDGE.New =>
                '{ $stagedSM.getNewDeltaDB(${ Expr(rId) }) }
              case KNOWLEDGE.Known =>
                '{ $stagedSM.getKnownDeltaDB(${ Expr(rId) }) }
            }
        }

      case ComplementOp(arity) =>
        '{ $stagedSM.getComplement(${ Expr(arity) }) }

      case ScanEDBOp(rId) =>
        if (storageManager.edbContains(rId))
          '{ $stagedSM.getEDB(${ Expr(rId) }) }
        else
          '{ $stagedSM.getEmptyEDB() }

      case ProjectJoinFilterOp(rId, hash, children:_*) =>
        val compiledOps = '{ $stagedFns.map(s => s($stagedSM)) }
        '{
          $stagedSM.joinProjectHelper(
            $compiledOps,
            ${ Expr(storageManager.allRulesAllIndexes(rId)(hash)) },
            ${ Expr(jitOptions.sortOrder) }
          )
        }

      case UnionOp(label, children:_*) =>
        val compiledOps = '{ $stagedFns.map(s => s($stagedSM)) }
        '{ $stagedSM.union($compiledOps) }

      case UnionSPJOp(rId, hash, children: _*) =>
        val compiledOps = '{ $stagedFns.map(s => s($stagedSM)) }
        '{ $stagedSM.union($compiledOps) }

      case DiffOp(children:_*) =>
        '{ $stagedSM.diff($stagedFns(0)($stagedSM), $stagedFns(1)($stagedSM)) }

      case DebugPeek(prefix, msg, children:_*) =>
        val res = compileIRRelOp(children.head)
        '{ debug(${ Expr(prefix) }, () => s"${${ Expr(msg()) }}") ; $res }

      case _ => throw new Exception("Error: compileRelOp called with unit operation")
    }
  }

  def compileIR(irTree: IROp[Any])(using stagedSM: Expr[StorageManager])(using stagedFns: Expr[Seq[CompiledFn[Any]]])(using Quotes): Expr[Any] = {
    irTree match {
      case ProgramOp(children) =>
        '{ $stagedFns(0)($stagedSM) }

      case DoWhileOp(toCmp, children:_*) =>
        val cond = toCmp match {
          case DB.Derived =>
            '{ !$stagedSM.compareDerivedDBs() }
          case DB.Delta =>
            '{ $stagedSM.compareNewDeltaDBs() }
        }
        '{
          while ( {
            $stagedFns(0)($stagedSM);
            $cond;
          }) ()
        }

      case UpdateDiscoveredOp() =>
        '{ $stagedSM.updateDiscovered() }

      case SwapAndClearOp() =>
        '{ $stagedSM.swapKnowledge() ; $stagedSM.clearNewDerived() }

      case SequenceOp(label, children:_*) =>
        '{ $stagedFns.foreach(s => s($stagedSM)) } // no need to generate lambdas bc already there!

      case InsertOp(rId, db, knowledge, children:_*) =>
        val res = '{ $stagedFns(0)($stagedSM).asInstanceOf[EDB] }
        val res2 = if (children.length > 1) '{ $stagedFns(1)($stagedSM).asInstanceOf[EDB] } else '{ $stagedSM.getEmptyEDB() }
        db match {
          case DB.Derived =>
            knowledge match {
              case KNOWLEDGE.New =>
                '{ $stagedSM.resetNewDerived(${ Expr(rId) }, $res, $res2) }
              case KNOWLEDGE.Known =>
                '{ $stagedSM.resetKnownDerived(${ Expr(rId) }, $res, $res2) }
            }
          case DB.Delta =>
            knowledge match {
              case KNOWLEDGE.New =>
                '{ $stagedSM.resetNewDelta(${ Expr(rId) }, $res) }
              case KNOWLEDGE.Known =>
                '{ $stagedSM.resetKnownDelta(${ Expr(rId) }, $res) }
            }
        }

      case DebugNode(prefix, msg) =>
        '{ debug(${ Expr(prefix) }, () => $stagedSM.printer.toString()) }

      case _ =>
        val irStagedFns = stagedFns.asInstanceOf[Expr[Seq[CompiledFn[EDB]]]]
        compileIRRelOp(irTree.asInstanceOf[IROp[EDB]])(using stagedSM)(using irStagedFns) // unfortunate but necessary to avoid 2x methods
    }
  }

  def getCompiledSnippet[T](irTree: IROp[T])(using staging.Compiler): ((StorageManager, Seq[CompiledFn[T]]) => T) = {
    val casted = irTree.asInstanceOf[IROp[Any]] // this will go away when compileIRIndexed exists or compileIR can take a type param
    staging.run {
      val res: Expr[(StorageManager, Seq[CompiledFn[Any]]) => Any] =
        '{ (stagedSm: StorageManager, stagedFns: Seq[CompiledFn[Any]]) => ${ compileIR(casted)(using 'stagedSm)(using 'stagedFns) } }
      debug("generated code: ", () => res.show)
      res
    }.asInstanceOf[(StorageManager, Seq[CompiledFn[T]]) => T]
  }
}
