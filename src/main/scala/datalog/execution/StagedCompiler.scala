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

  def compileIRRelOp[T](irTree: IRRelOp)(using stagedSM: Expr[StorageManager {type EDB = T}], t: Type[T])(using ctx: InterpreterContext)(using Quotes): Expr[T] = { // TODO: Instead of parameterizing, use staged path dependent type: i.e. stagedSM.EDB
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

      case JoinOp(subOps, keys) =>
        val compiledOps = Expr.ofSeq(subOps.map(compileIRRelOp))
        // TODO[future]: inspect keys and optimize join algo
        '{
          $stagedSM.joinHelper(
            $compiledOps,
            ${ Expr(keys) }
          )
        }

      case ProjectOp(subOp, keys) =>
        if (subOp.code == OpCode.JOIN) // merge join+project
          val compiledOps = Expr.ofSeq(subOp.asInstanceOf[JoinOp].ops.map(compileIRRelOp))
          '{ $stagedSM.joinProjectHelper($compiledOps, ${ Expr(keys) }) }
        else
          '{ $stagedSM.projectHelper(${ compileIRRelOp(subOp) }, ${ Expr(keys) }) }

      case UnionOp(ops, label) =>
        val compiledOps = ops.map(compileIRRelOp)
        label match
          case OpCode.EVAL_RULE_NAIVE if ops.size > heuristics.max_deps =>
            val lambdaOps = compiledOps.map(e => '{ def eval_rule_lambda() = $e; eval_rule_lambda() })
            '{ $stagedSM.union(${Expr.ofSeq(lambdaOps)}) }
          case OpCode.EVAL_RULE_SN if ops.size > heuristics.max_deps =>
            val lambdaOps = compiledOps.map(e => '{ def eval_rule_sn_lambda() = $e; eval_rule_sn_lambda() })
            '{ $stagedSM.union(${ Expr.ofSeq(lambdaOps) }) }
          case _ =>
            '{ $stagedSM.union(${Expr.ofSeq(compiledOps)}) }

      case DiffOp(lhs, rhs) =>
        val clhs = compileIRRelOp(lhs)
        val crhs = compileIRRelOp(rhs)
        '{ $stagedSM.diff($clhs, $crhs) }

      case DebugPeek(prefix, msg, op) =>
        val res = compileIRRelOp(op)
        '{ debug(${ Expr(prefix) }, () => s"${${ Expr(msg()) }}") ; $res }

      case _ => throw new Exception("Error: compileRelOp called with unit operation")
    }
  }

  def compileIR[T](irTree: IROp)(using stagedSM: Expr[StorageManager {type EDB = T}], t: Type[T])(using ctx: InterpreterContext)(using Quotes): Expr[Any] = { // TODO: Instead of parameterizing, use staged path dependent type: i.e. stagedSM.EDB
    irTree match {
      case ProgramOp(body) =>
        compileIR(body)

      case DoWhileOp(body, toCmp) =>
        val cond = toCmp match {
          case DB.Derived =>
            '{ !$stagedSM.compareDerivedDBs() }
          case DB.Delta =>
            '{ $stagedSM.compareNewDeltaDBs() }
        }
        '{
          while ( {
            ${ compileIR(body) };
            $cond;
          }) ()
        }

      case SwapAndClearOp() =>
        '{ $stagedSM.swapKnowledge() ; $stagedSM.clearNewDB(${ Expr(true) }) }

      case SequenceOp(ops, label) =>
        val cOps = ops.map(compileIR)
        label match
          case OpCode.EVAL_NAIVE if ops.length / 2 > heuristics.max_relations =>
            cOps.reduceLeft((acc, next) =>
              '{ $acc ; def eval_naive_lambda() = $next; eval_naive_lambda() }
            )
          case OpCode.EVAL_SN if ops.length > heuristics.max_relations =>
            cOps.reduceLeft((acc, next) =>
              '{ $acc ; def eval_sn_lambda() = $next; eval_sn_lambda() }
            )
          case _ =>
            cOps.reduceLeft((acc, next) => // TODO[future]: make a block w reflection instead of reduceLeft for efficiency
              '{ $acc ; $next }
            )

      case InsertOp(rId, db, knowledge, subOp, subOp2) =>
        val res = compileIRRelOp(subOp)
        val res2 = if (subOp2.isEmpty) '{ $stagedSM.EDB() } else compileIRRelOp(subOp2.get)
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
        irTree match {
          case op: IRRelOp => compileIRRelOp(op)
          case _ =>
            throw new Exception(s"Error: unhandled node type $irTree")
        }
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

  def getCompiled(irTree: IROp, ctx: InterpreterContext)(using compiler: staging.Compiler): CollectionsStorageManager => CollectionsStorageManager#EDB = {
    given irCtx: InterpreterContext = ctx
    val result = staging.run {
      val res: Expr[CollectionsStorageManager => Any] =
        '{ (stagedSm: CollectionsStorageManager) => ${ compileIR[CollectionsStorageManager#EDB](irTree)(using 'stagedSm) } }
      debug("generated code: ", () => res.show)
      res
    }.asInstanceOf[CollectionsStorageManager => CollectionsStorageManager#EDB]
//    clearDottyThread(compiler)
    result
  }
}
