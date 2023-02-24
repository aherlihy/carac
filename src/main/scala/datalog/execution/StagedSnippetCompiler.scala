package datalog.execution

import datalog.dsl.{Atom, Constant, Variable, Term}
import datalog.execution.ir.*
import datalog.storage.{CollectionsStorageManager, DB, KNOWLEDGE, StorageManager}
import datalog.tools.Debug.debug

import scala.quoted.*

/**
 * Instead of compiling entire subtree, compile only contents of the node and call into continue.
 * Unclear if it will ever be useful to only compile a single, mid-tier node, and go back to
 * interpretation (maybe for save points for de-optimizations?) but this is mostly just a POC
 * that it's possible.
 */
class StagedSnippetCompiler(val storageManager: StorageManager) {
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
      '{ Atom( ${ Expr(x.rId) }, ${ Expr.ofSeq(x.terms.map(y => Expr(y))) } ) }
    }
  }

  given ToExpr[JoinIndexes] with {
    def apply(x: JoinIndexes)(using Quotes) = {
      '{ JoinIndexes(${ Expr(x.varIndexes) }, ${ Expr(x.constIndexes) }, ${ Expr(x.projIndexes) }, ${ Expr(x.deps) }, ${Expr (x.atoms) }, ${ Expr(x.edb) }) }
    }
  }

  def compileIRRelOp(irTree: IROp[CollectionsStorageManager#EDB])(using stagedSM: Expr[CollectionsStorageManager])(using stagedFns: Expr[Seq[CompiledRelFn]])(using Quotes): Expr[CollectionsStorageManager#EDB] = { // TODO: Instead of parameterizing, use staged path dependent type: i.e. stagedSM.EDB
    irTree match {
      case ScanOp(rId, db, knowledge) =>
        db match { // TODO[future]: Since edb is accessed upon first iteration, potentially optimize away getOrElse
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

      case ScanEDBOp(rId) =>
        if (storageManager.edbs.contains(rId))
          '{ $stagedSM.edbs(${ Expr(rId) }) }
        else
          '{ $stagedSM.EDB() }

      case ProjectJoinFilterOp(keys, children:_*) =>
        val compiledOps = '{ $stagedFns.map(s => s($stagedSM)) }
        // TODO[future]: inspect keys and optimize join algo
        '{
          $stagedSM.joinProjectHelper(
            $compiledOps,
            ${ Expr(keys) }
          )
        }

      case UnionOp(label, children:_*) =>
        val compiledOps = '{ $stagedFns.map(s => s($stagedSM)) }
        '{ $stagedSM.union($compiledOps) }

      case UnionSPJOp(k, children: _*) =>
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

  def compileIR(irTree: IROp[Any])(using stagedSM: Expr[CollectionsStorageManager])(using stagedFns: Expr[Seq[CompiledFn]])(using Quotes): Expr[Any] = { // TODO: Instead of parameterizing, use staged path dependent type: i.e. stagedSM.EDB
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

      case SwapAndClearOp() =>
        '{ $stagedSM.swapKnowledge() ; $stagedSM.clearNewDerived() }

      case SequenceOp(label, children:_*) =>
        '{ $stagedFns.foreach(s => s($stagedSM)) } // no need to generate lambdas bc already there!

      case InsertOp(rId, db, knowledge, children:_*) =>
        val res = '{ $stagedFns(0)($stagedSM).asInstanceOf[CollectionsStorageManager#EDB] }
        val res2 = if (children.size > 1) '{ $stagedFns(1)($stagedSM).asInstanceOf[CollectionsStorageManager#EDB] } else '{ $stagedSM.EDB() }
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
        throw new Exception(s"Error: unhandled node type $irTree")
    }
  }

  def getCompiledSnippet(irTree: IROp[Any])(using staging.Compiler): ((CollectionsStorageManager, Seq[CompiledFn]) => Any) = {
    staging.run {
      val res: Expr[(CollectionsStorageManager, Seq[CompiledFn]) => Any] =
        '{ (stagedSm: CollectionsStorageManager, stagedFns: Seq[CompiledFn]) => ${ compileIR(irTree)(using 'stagedSm)(using 'stagedFns) } }
      debug("generated code: ", () => res.show)
      res
    }
  }

  def getCompiledSnippetRel(irTree: IROp[CollectionsStorageManager#EDB])(using staging.Compiler): ((CollectionsStorageManager, Seq[CompiledRelFn]) => CollectionsStorageManager#EDB) = {
    staging.run {
      val res: Expr[(CollectionsStorageManager, Seq[CompiledRelFn]) => CollectionsStorageManager#EDB] =
        '{ (stagedSm: CollectionsStorageManager, stagedFns: Seq[CompiledRelFn]) => ${ compileIRRelOp(irTree)(using 'stagedSm)(using 'stagedFns) } }
      debug("generated code: ", () => res.show)
      res
    }
  }
}
