package datalog.execution

import datalog.dsl.{Atom, Constant, Variable, Term}
import datalog.execution.ir.*
import datalog.storage.{StorageManager, DB, KNOWLEDGE, EDB}
import datalog.tools.Debug.debug

import scala.quoted.*
/**
 * Separate out compile logic from StagedExecutionEngine
 */
class StagedCompiler(val storageManager: StorageManager)(using val jitOptions: JITOptions) {
  given staging.Compiler = jitOptions.dotty
  // TODO: move Exprs to where classes are defined?
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

  def compileIR[T: Type](irTree: IROp[T])(using stagedSM: Expr[StorageManager])(using Quotes): Expr[T] = {
    irTree match {
      case ProgramOp(children:_*) =>
        compileIR(children.head)

      case DoWhileOp(toCmp, children:_*) =>
        val cond = toCmp match {
          case DB.Derived =>
            '{ !$stagedSM.compareDerivedDBs() }
          case DB.Delta =>
            '{ $stagedSM.compareNewDeltaDBs() }
        }
        '{
          while ( {
            ${ compileIR(children.head) };
            $cond;
          }) ()
        }

      case SwapAndClearOp() =>
        '{ $stagedSM.swapKnowledge() ; $stagedSM.clearNewDerived() }

      case SequenceOp(label, children:_*) =>
        val cOps = children.map(compileIR)
        label match
          case OpCode.EVAL_NAIVE if children.length / 2 > heuristics.max_relations =>
            cOps.reduceLeft((acc, next) =>
              '{ $acc ; def eval_naive_lambda() = $next; eval_naive_lambda() }
            )
          case OpCode.EVAL_SN if children.length > heuristics.max_relations =>
            cOps.reduceLeft((acc, next) =>
              '{ $acc ; def eval_sn_lambda() = $next; eval_sn_lambda() }
            )
          case _ =>
            cOps.reduceLeft((acc, next) => // TODO[future]: make a block w reflection instead of reduceLeft for efficiency
              '{ $acc ; $next }
            )

      case InsertOp(rId, db, knowledge, children:_*) =>
        val res = compileIR(children.head.asInstanceOf[IROp[EDB]])
        val res2 = if (children.length > 1) compileIR(children(1).asInstanceOf[IROp[EDB]]) else '{ $stagedSM.getEmptyEDB() }
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

      case ScanEDBOp(rId) =>
        if (storageManager.edbContains(rId))
          '{ $stagedSM.getEDB(${ Expr(rId) }) }
        else
          '{ $stagedSM.getEmptyEDB() }

      case ProjectJoinFilterOp(rId, hash, children: _*) =>
        val FPJOp = irTree.asInstanceOf[ProjectJoinFilterOp]
        val (sortedChildren, newHash) = JoinIndexes.getSortAhead(
          FPJOp.childrenSO,
          c => c.run(storageManager).length,
          rId,
          hash,
          storageManager
        )
        FPJOp.childrenSO = sortedChildren
        //        FPJOp.children = sortedChildren.asInstanceOf[Array[IROp[EDB]]] // save for next run so sorting is faster
        FPJOp.hash = newHash
        val compiledOps = Expr.ofSeq(sortedChildren.map(compileIR))
        '{
          $stagedSM.joinProjectHelper_withHash(
            $compiledOps,
            ${ Expr(rId) },
            ${ Expr(newHash) },
            ${ Expr(jitOptions.sortOrder) }
          )
        }

      case UnionSPJOp(rId, hash, children: _*) =>
        val (sortedChildren, newHash) = JoinIndexes.getPreSortAhead(
          children.toArray,
          a => storageManager.getKnownDerivedDB(a.rId).length,
          rId,
          hash,
          storageManager
        )

        val compiledOps = sortedChildren.map(compileIR)
        '{ $stagedSM.union(${ Expr.ofSeq(compiledOps) }) }

      case UnionOp(label, children: _*) =>
        val compiledOps = children.map(compileIR)
        label match
          case OpCode.EVAL_RULE_NAIVE if children.length > heuristics.max_deps =>
            val lambdaOps = compiledOps.map(e => '{ def eval_rule_lambda() = $e ; eval_rule_lambda() })
            '{ $stagedSM.union(${ Expr.ofSeq(lambdaOps) }) }
          case OpCode.EVAL_RULE_SN if children.length > heuristics.max_deps =>
            val lambdaOps = compiledOps.map(e => '{ def eval_rule_sn_lambda() = $e ; eval_rule_sn_lambda() })
            '{ $stagedSM.union(${ Expr.ofSeq(lambdaOps) }) }
          case _ =>
            '{ $stagedSM.union(${ Expr.ofSeq(compiledOps) }) }

      case DiffOp(children: _*) =>
        val clhs = compileIR(children.head)
        val crhs = compileIR(children(1))
        '{ $stagedSM.diff($clhs, $crhs) }

      case DebugPeek(prefix, msg, children: _*) =>
        val res = compileIR(children.head)
        '{ debug(${ Expr(prefix) }, () => s"${${ Expr(msg()) }}") ; $res }

      case _ => throw new Exception(s"Error: compileOp called with unknown operator ${irTree.code}")
    }
  }

  def getCompiled[T: Type](irTree: IROp[T]): CompiledFn[T] = {
    val result = staging.run {
      val res: Expr[CompiledFn[T]] =
        '{ (stagedSm: StorageManager) => ${ compileIR(irTree)(using 'stagedSm) } }
      debug("generated code: ", () => res.show)
      res
    }
    clearDottyThread()
    result
  }

  /* These compile methods are for compiling with entry points for longer-running operations, so they return (sm, i) instead of i */

  def getCompiledUnionSPJ(irTree: UnionSPJOp): (StorageManager, Int) => EDB = {
    val result = staging.run {
      val res: Expr[(StorageManager, Int) => EDB] =
        '{ (stagedSm: StorageManager, i: Int) => ${ compileIRUnionSPJ(irTree)(using 'stagedSm)(using 'i) } }
      debug("generated code: ", () => res.show)
      res
    }
    clearDottyThread()
    result
  }

  def getCompiledEvalRule(irTree: UnionOp): (StorageManager, Int) => EDB = {
    val result = staging.run {
      val res: Expr[(StorageManager, Int) => EDB] =
        '{ (stagedSm: StorageManager, i: Int) => ${ compileIREvalRule(irTree)(using 'stagedSm)(using 'i) } }
      debug("generated code: ", () => res.show)
      res
    }
    clearDottyThread()
    result
  }

  def compileIREvalRule(uOp: UnionOp)(using stagedSM: Expr[StorageManager])(using i: Expr[Int])(using Quotes): Expr[EDB] = {
    '{ ${Expr.ofSeq(uOp.children.toSeq.map(compileIR))}($i) }
  }

  def compileIRUnionSPJ(uOp: UnionSPJOp)(using stagedSM: Expr[StorageManager])(using i: Expr[Int])(using Quotes): Expr[EDB] = {
    val (sortedChildren, newHash) = JoinIndexes.getPreSortAhead(
      uOp.children.toArray,
      a => storageManager.getKnownDerivedDB(a.rId).length,
      uOp.rId,
      uOp.hash,
      storageManager
    )
    '{ ${ Expr.ofSeq(sortedChildren.toSeq.map(compileIR)) } ($i) }
  }

  def clearDottyThread() = {
    val driverField = jitOptions.dotty.getClass.getDeclaredField("driver")
    driverField.setAccessible(true)
    val driver = driverField.get(jitOptions.dotty)
    val contextBaseField = driver.getClass.getDeclaredField("contextBase")
    contextBaseField.setAccessible(true)
    val contextBase = contextBaseField.get(driver)
    val threadField = contextBase.getClass.getSuperclass.getDeclaredField("thread")
    threadField.setAccessible(true)
    threadField.set(contextBase, null)
  }
}
