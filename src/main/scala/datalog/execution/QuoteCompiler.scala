package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.ir.*
import datalog.storage.*
import datalog.tools.Debug.debug
import org.glavo.classfile.CodeBuilder

import java.lang.invoke.MethodType
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.{immutable, mutable}
import scala.quoted.*

/**
 * Separate out compile logic from StagedExecutionEngine
 */
class QuoteCompiler(val storageManager: StorageManager)(using val jitOptions: JITOptions) extends StagedCompiler(storageManager) {
  given staging.Compiler = jitOptions.dotty
  clearDottyThread()

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
      x match
        case PredicateType.POSITIVE => '{ PredicateType.POSITIVE }
        case PredicateType.NEGATED => '{ PredicateType.NEGATED }
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
          ${ Expr(x.edb) }
        )
      }
    }
  }

  /**
   * Compiles a relational operator into a quote that returns an EDB. Future TODO: merge with compileIR when dotty supports.
   */
  def compileIRRelOp(irTree: IROp[EDB])(using stagedSM: Expr[StorageManager])(using Quotes): Expr[EDB] = {
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

      case ProjectJoinFilterOp(rId, k, children: _*) =>
        val compiledOps = Expr.ofSeq(children.map(compileIRRelOp))
        '{
          $stagedSM.joinProjectHelper_withHash(
            $compiledOps,
            ${ Expr(rId) },
            ${ Expr(k.hash) },
            ${ Expr(jitOptions.onlineSort) }
          )
        }

      case UnionSPJOp(rId, k, children: _*) =>
        val (sortedChildren, _) =
          if (jitOptions.sortOrder != SortOrder.Unordered && jitOptions.sortOrder != SortOrder.Badluck)
            JoinIndexes.getPresort(
              children.toArray,
              jitOptions.getSortFn(storageManager),
              rId,
              k,
              storageManager
            )
          else
            (children.toArray, k)

        val compiledOps = sortedChildren.map(compileIRRelOp)
        '{ $stagedSM.union(${ Expr.ofSeq(compiledOps) }) }

      case UnionOp(label, children: _*) =>
        val compiledOps = children.map(compileIRRelOp)
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
        val clhs = compileIRRelOp(children.head)
        val crhs = compileIRRelOp(children(1))
        '{ $stagedSM.diff($clhs, $crhs) }

      case DebugPeek(prefix, msg, children: _*) =>
        val res = compileIRRelOp(children.head)
        '{ debug(${ Expr(prefix) }, () => s"${${ Expr(msg()) }}") ; $res }

      case _ => throw new Exception(s"Error: compileOpRelOp called with unknown operator ${irTree.code}")
    }
  }

  /**
   * Compiles a unit operator into a quote that returns Any, or really nothing.
   * NOTE: due to a compiler limitation, compileIR can't be parameterized, so have 2 versions of compileIR.
   * To avoid having also 2 versions of getCompiled, compileIR will call into compileIRRel if needed.
   */
  def compileIR(irTree: IROp[Any])(using stagedSM: Expr[StorageManager])(using Quotes): Expr[Any] = {
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

      case UpdateDiscoveredOp() =>
        '{ $stagedSM.updateDiscovered() }

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
            cOps.reduceLeft((acc, next) =>
              '{ $acc ; $next }
            )

      case InsertOp(rId, db, knowledge, children:_*) =>
        val res = compileIRRelOp(children.head.asInstanceOf[IROp[EDB]])
        val res2 = if (children.length > 1) compileIRRelOp(children(1).asInstanceOf[IROp[EDB]]) else '{ $stagedSM.getEmptyEDB() }
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

      case _ => compileIRRelOp(irTree.asInstanceOf[IROp[EDB]]) // unfortunate but necessary to avoid 2x methods
    }
  }

  def compile[T](irTree: IROp[T]): CompiledFn[T] = {
    val casted = irTree.asInstanceOf[IROp[Any]] // this will go away when compileIRIndexed exists or compileIR can take a type param
    val result = staging.run {
      val res: Expr[CompiledFn[Any]] =
        '{ (stagedSm: StorageManager) => ${ compileIR(casted)(using 'stagedSm) } }
      debug("generated code: ", () => res.show)
      res
    }
    clearDottyThread()
    result.asInstanceOf[CompiledFn[T]]
  }

  /**
   * The following compile methods are for compiling with entry points for longer-running operations, so they return an
   * indexed compile fn so execution can begin from the correct index. Currently only for union ops.
   */
  def compileIndexed[T](irTree: IROp[T]): CompiledFnIndexed[T] = {
    val casted = irTree.asInstanceOf[IROp[EDB]] // this will go away when compileIRIndexed exists or compileIR can take a type param
    val result =
      staging.run {
        val res: Expr[(StorageManager, Int) => EDB] =
          '{ (stagedSm: StorageManager, i: Int) => ${ compileIRRelOpIndexed(casted)(using 'stagedSm)(using 'i) } }
        debug("generated code: ", () => res.show)
        res
      }
    clearDottyThread()
    result.asInstanceOf[CompiledFnIndexed[T]] // This cast will go away when compileIR can take a type param
  }

  def compileIRRelOpIndexed(irTree: IROp[EDB])(using stagedSM: Expr[StorageManager])(using i: Expr[Int])(using Quotes): Expr[EDB] = {
    irTree match
      case uOp: UnionOp =>
        '{ ${Expr.ofSeq(uOp.children.toSeq.map(compileIRRelOp))}($i) }
      case uSPJOp: UnionSPJOp =>
        val (sortedChildren, _) =
          if (jitOptions.sortOrder != SortOrder.Unordered && jitOptions.sortOrder != SortOrder.Badluck)
            JoinIndexes.getPresort(
              uSPJOp.children.toArray,
              jitOptions.getSortFn(storageManager),
              uSPJOp.rId,
              uSPJOp.k,
              storageManager
            )
          else
            (uSPJOp.children.toArray, uSPJOp.k)
        '{ ${ Expr.ofSeq(sortedChildren.toSeq.map(compileIRRelOp)) } ($i) }
      case _ => throw new Exception(s"Indexed compilation: Unhandled IROp ${irTree.code}")
  }

  /* Hack to avoid triggering assert for multi-threaded use */
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
