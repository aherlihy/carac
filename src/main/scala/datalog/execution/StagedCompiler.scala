package datalog.execution

import datalog.dsl.Constant
import datalog.execution.ir.*
import datalog.storage.{CollectionsStorageManager, StorageManager, DB, KNOWLEDGE}
import datalog.tools.Debug.debug

import scala.quoted.*
/**
 * Separate out compile logic from StagedExecutionEngine
 */
class StagedCompiler(val storageManager: StorageManager) {
  given ToExpr[Constant] with {
    def apply(x: Constant)(using Quotes) = {
      x match {
        case i: Int => Expr(i)
        case s: String => Expr(s)
      }
    }
  }

  given ToExpr[JoinIndexes] with {
    def apply(x: JoinIndexes)(using Quotes) = {
      '{ JoinIndexes(${ Expr(x.varIndexes) }, ${ Expr(x.constIndexes) }, ${ Expr(x.projIndexes) }, ${ Expr(x.deps) }, ${ Expr(x.edb) }) }
    }
  }

//  def compileIRRelOp[T](irTree: IRRelOp)(using stagedSM: Expr[StorageManager {type EDB = T}], t: Type[T])(using Quotes): Expr[T] = { // TODO: Instead of parameterizing, use staged path dependent type: i.e. stagedSM.EDB
  def compileIRRelOp(irTree: IROp[CollectionsStorageManager#EDB])(using stagedSM: Expr[CollectionsStorageManager])(using Quotes): Expr[CollectionsStorageManager#EDB] = {
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

      case ScanEDBOp(rId) =>
        if (storageManager.edbs.contains(rId))
          '{ $stagedSM.edbs(${ Expr(rId) }) }
        else
          '{ $stagedSM.EDB() }

      case JoinOp(keys, children:_*) =>
        val compiledOps = Expr.ofSeq(children.map(compileIRRelOp))
        // TODO[future]: inspect keys and optimize join algo
        '{
          $stagedSM.joinHelper(
            $compiledOps,
            ${ Expr(keys) }
          )
        }

      case ProjectOp(keys, children:_*) =>
        if (children.head.code == OpCode.JOIN) // merge join+project
          val compiledOps = Expr.ofSeq(children.head.asInstanceOf[JoinOp].children.map(compileIRRelOp))
          '{ $stagedSM.joinProjectHelper($compiledOps, ${ Expr(keys) }) }
        else
          '{ $stagedSM.projectHelper(${ compileIRRelOp(children.head) }, ${ Expr(keys) }) }

      case UnionOp(label, children:_*) =>
        val compiledOps = children.map(compileIRRelOp)
        label match
          case OpCode.EVAL_RULE_NAIVE if children.size > heuristics.max_deps =>
            val lambdaOps = compiledOps.map(e => '{ def eval_rule_lambda() = $e; eval_rule_lambda() })
            '{ $stagedSM.union(${Expr.ofSeq(lambdaOps)}) }
          case OpCode.EVAL_RULE_SN if children.size > heuristics.max_deps =>
            val lambdaOps = compiledOps.map(e => '{ def eval_rule_sn_lambda() = $e; eval_rule_sn_lambda() })
            '{ $stagedSM.union(${ Expr.ofSeq(lambdaOps) }) }
          case _ =>
            '{ $stagedSM.union(${Expr.ofSeq(compiledOps)}) }

      case DiffOp(children:_*) =>
        val clhs = compileIRRelOp(children.head)
        val crhs = compileIRRelOp(children(1))
        '{ $stagedSM.diff($clhs, $crhs) }

      case DebugPeek(prefix, msg, children:_*) =>
        val res = compileIRRelOp(children.head)
        '{ debug(${ Expr(prefix) }, () => s"${${ Expr(msg()) }}") ; $res }

      case _ => throw new Exception("Error: compileRelOp called with unit operation")
    }
  }

//  def compileIR[T](irTree: IROp)(using stagedSM: Expr[StorageManager {type EDB = T}], t: Type[T])(using Quotes): Expr[Any] = { // TODO: Instead of parameterizing, use staged path dependent type: i.e. stagedSM.EDB
  def compileIR(irTree: IROp[Any])(using stagedSM: Expr[CollectionsStorageManager])(using Quotes): Expr[Any] = {
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
          case OpCode.EVAL_NAIVE if children.size / 2 > heuristics.max_relations =>
            cOps.reduceLeft((acc, next) =>
              '{ $acc ; def eval_naive_lambda() = $next; eval_naive_lambda() }
            )
          case OpCode.EVAL_SN if children.size > heuristics.max_relations =>
            cOps.reduceLeft((acc, next) =>
              '{ $acc ; def eval_sn_lambda() = $next; eval_sn_lambda() }
            )
          case _ =>
            cOps.reduceLeft((acc, next) => // TODO[future]: make a block w reflection instead of reduceLeft for efficiency
              '{ $acc ; $next }
            )

      case InsertOp(rId, db, knowledge, children:_*) =>
        val res = compileIRRelOp(children.head.asInstanceOf[IROp[CollectionsStorageManager#EDB]])
        val res2 = if (children.size > 1) compileIRRelOp(children(1).asInstanceOf[IROp[CollectionsStorageManager#EDB]]) else '{ $stagedSM.EDB() }
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
        compileIRRelOp(irTree.asInstanceOf[IROp[CollectionsStorageManager#EDB]])(using stagedSM)
    }
  }

  def clearDottyThread(compiler: staging.Compiler) =
    val driverField = compiler.getClass.getDeclaredField("driver")
    driverField.setAccessible(true)
    val driver = driverField.get(compiler)
    val contextBaseField = driver.getClass.getDeclaredField("contextBase")
    contextBaseField.setAccessible(true)
    val contextBase = contextBaseField.get(driver)
    val threadField = contextBase.getClass.getSuperclass.getDeclaredField("thread")
    threadField.setAccessible(true)
    threadField.set(contextBase, null)

  def getCompiled(irTree: IROp[Any])(using compiler: staging.Compiler): CompiledFn = {
    val result = staging.run {
      val res: Expr[CompiledFn] =
        '{ (stagedSm: CollectionsStorageManager) => ${ compileIR(irTree)(using 'stagedSm) } }
      debug("generated code: ", () => res.show)
      res
    }
    clearDottyThread(compiler)
    result
  }

  def getCompiledRel(irTree: IROp[CollectionsStorageManager#EDB])(using compiler: staging.Compiler): CompiledRelFn = {
    val result = staging.run {
      val res: Expr[CompiledRelFn] =
        '{ (stagedSm: CollectionsStorageManager) => ${ compileIRRelOp(irTree)(using 'stagedSm) } }
      debug("generated code: ", () => res.show)
      res
    }
    clearDottyThread(compiler)
    result
  }
}
