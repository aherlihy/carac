package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.ir.*
import datalog.storage.{DB, EDB, KNOWLEDGE, StorageManager, StorageAggOp}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.quoted.*

/**
 * Instead of compiling entire subtree, compile only contents of the node and call into continue.
 * Unclear if it will ever be useful to only compile a single, mid-tier node, and go back to
 * interpretation (maybe for save points for de-optimizations?) but this is mostly just a POC
 * that it's possible.
 */
class StagedSnippetCompiler(val storageManager: StorageManager)(using val jitOptions: JITOptions) {
  given MutableMapToExpr[T: Type : ToExpr, U: Type : ToExpr]: ToExpr[mutable.Map[T, U]] with {
    def apply(map: mutable.Map[T, U])(using Quotes): Expr[mutable.Map[T, U]] =
      '{ mutable.Map(${ Expr(map.toSeq) }: _*) }
  }

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

  given ToExpr[PredicateType] with {
    def apply(x: PredicateType)(using Quotes) = {
      Expr(x)
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
          ${ Expr(x.cxns) },
          ${ Expr(x.edb) },
        ) }
    }
  }


  given ToExpr[StorageAggOp] with {
    def apply(x: StorageAggOp)(using Quotes) = {
      x match
        case StorageAggOp.SUM => '{ StorageAggOp.SUM }
        case StorageAggOp.COUNT => '{ StorageAggOp.COUNT }
        case StorageAggOp.MIN => '{ StorageAggOp.MIN }
        case StorageAggOp.MAX => '{ StorageAggOp.MAX }
    }
  }

  given ToExpr[AggOpIndex] with {
    def apply(x: AggOpIndex)(using Quotes) = {
      x match
        case AggOpIndex.LV(i) => '{ AggOpIndex.LV(${ Expr(i) }) }
        case AggOpIndex.GV(i) => '{ AggOpIndex.GV(${ Expr(i) }) }
        case AggOpIndex.C(c) => '{ AggOpIndex.C(${ Expr(c) }) }
      
    }
  }

  given ToExpr[GroupingJoinIndexes] with {
    def apply(x: GroupingJoinIndexes)(using Quotes) = {
      '{
        GroupingJoinIndexes(
          ${ Expr(x.varIndexes) },
          ${ Expr(x.constIndexes) },
          ${ Expr(x.groupingIndexes) },
          ${ Expr(x.aggOpInfos) }
        )
      }
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

      case ProjectJoinFilterOp(rId, k, children:_*) =>
        val compiledOps = '{ $stagedFns.map(s => s($stagedSM)) }
        '{
          $stagedSM.joinProjectHelper(
            $compiledOps,
            ${ Expr(k) },
            ${ Expr(jitOptions.onlineSort) }
          )
        }

      case UnionOp(label, children:_*) =>
        val compiledOps = '{ $stagedFns.map(s => s($stagedSM)) }
        '{ $stagedSM.union($compiledOps) }

      case UnionSPJOp(rId, k, children: _*) =>
        val compiledOps = '{ $stagedFns.map(s => s($stagedSM)) }
        '{ $stagedSM.union($compiledOps) }

      case DiffOp(children:_*) =>
        '{ $stagedSM.diff($stagedFns(0)($stagedSM), $stagedFns(1)($stagedSM)) }

      case GroupingOp(child, gji) =>
        '{ $stagedSM.groupingHelper($stagedFns.head($stagedSM), ${ Expr(gji) }) }

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
