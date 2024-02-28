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
class LambdaCompiler(val storageManager: StorageManager)(using JITOptions) extends StagedCompiler(storageManager) {
  given staging.Compiler = jitOptions.dotty
  /** Convert a Seq of lambdas into a lambda returning a Seq. */
  def seqToLambda[T](seq: Seq[StorageManager => T]): StorageManager => Seq[T] =
    seq match
      case seq: immutable.ArraySeq.ofRef[_] =>
        val arr = unsafeArrayToLambda(seq.unsafeArray)
        sm => new immutable.ArraySeq.ofRef(arr(sm).asInstanceOf[Array[AnyRef & T]])
      case _ =>
        sm =>
          seq.map(lambda => lambda(sm))

  /** Convert an Array of lambdas into a lambda returning an Array. */
  def arrayToLambda[T](arr: Array[StorageManager => T]): StorageManager => Array[T] =
    unsafeArrayToLambda(arr).asInstanceOf[StorageManager => Array[T]]

  def unsafeArrayToLambda(arr: Array[? <: AnyRef]): StorageManager => Array[AnyRef] = {
    // TODO: Instead of unrolling by hand, we could use a macro.
    arr.length match
      case 1 =>
        sm =>
          val out = (new Array[AnyRef](1))
          out(0) = arr(0).asInstanceOf[StorageManager => AnyRef](sm)
          out
      case 2 =>
        sm =>
          val out = (new Array[AnyRef](2))
          out(0) = arr(0).asInstanceOf[StorageManager => AnyRef](sm)
          out(1) = arr(1).asInstanceOf[StorageManager => AnyRef](sm)
          out
      case 3 =>
        sm =>
          val out = (new Array[AnyRef](3))
          out(0) = arr(0).asInstanceOf[StorageManager => AnyRef](sm)
          out(1) = arr(1).asInstanceOf[StorageManager => AnyRef](sm)
          out(2) = arr(2).asInstanceOf[StorageManager => AnyRef](sm)
          out
      case _ =>
        sm =>
          val out = (new Array[AnyRef](arr.length))
          var i = 0
          while (i < arr.length)
            out(i) = arr(i).asInstanceOf[StorageManager => AnyRef](sm)
            i += 1
          out
  }

  /** "Compile" an IRTree into nested lambda calls. */
  def compile[T](irTree: IROp[T]): CompiledFn[T] = irTree match
    case ProgramOp(children:_*) =>
      compile(children.head)

    case DoWhileOp(toCmp, children:_*) =>
      val cond: CompiledFn[Boolean] = toCmp match {
        case DB.Derived =>
          !_.compareDerivedDBs()
        case DB.Delta =>
          _.compareNewDeltaDBs()
      }
      val body = compile(children.head)
      sm =>
        while {
          body(sm)
          cond(sm)
        } do ()

    case UpdateDiscoveredOp() =>
      _.updateDiscovered()

    case SwapAndClearOp() =>
      sm =>
        sm.swapKnowledge()
        sm.clearNewDerived()

    case SequenceOp(label, children:_*) =>
      val cOps: Array[CompiledFn[Any]] = children.map(compile).toArray
      cOps.length match
        case 1 =>
          cOps(0)
        case 2 =>
          sm => { cOps(0)(sm); cOps(1)(sm) }
        case 3 =>
          sm => { cOps(0)(sm); cOps(1)(sm); cOps(2)(sm) }
        case _ =>
          sm => {
            var i = 0
            while (i < cOps.length) {
              cOps(i)(sm)
              i += 1
            }
          }

    case InsertOp(rId, db, knowledge, children:_*) =>
      val res = compile(children.head.asInstanceOf[IROp[EDB]])
      db match {
        case DB.Derived =>
          val res2: CompiledFn[EDB] =
            if (children.length > 1)
              compile(children(1).asInstanceOf[IROp[EDB]])
            else
              _.getEmptyEDB(rId)
          knowledge match {
            case KNOWLEDGE.New =>
              sm => sm.resetNewDerived(rId, res(sm), res2(sm))
            case KNOWLEDGE.Known =>
              sm => sm.resetKnownDerived(rId, res(sm), res2(sm))
          }
        case DB.Delta =>
          knowledge match {
            case KNOWLEDGE.New =>
              sm => sm.setNewDelta(rId, res(sm))
            case KNOWLEDGE.Known =>
              sm => sm.setKnownDelta(rId, res(sm))
          }
      }

    case ScanOp(rId, db, knowledge) =>
      db match {
        case DB.Derived =>
          knowledge match {
            case KNOWLEDGE.New =>
              _.getNewDerivedDB(rId)
            case KNOWLEDGE.Known =>
              _.getKnownDerivedDB(rId)
          }
        case DB.Delta =>
          knowledge match {
            case KNOWLEDGE.New =>
              _.getNewDeltaDB(rId)
            case KNOWLEDGE.Known =>
              _.getKnownDeltaDB(rId)
          }
      }

    case ComplementOp(r, arity) =>
      _.getComplement(r, arity)

    case ScanEDBOp(rId) =>
      if (storageManager.edbContains(rId))
        _.getEDB(rId)
      else
        _.getEmptyEDB(rId)

    case ProjectJoinFilterOp(rId, k, children: _*) =>
      val (sortedChildren, newK) =
        if (jitOptions.sortOrder != SortOrder.Unordered && jitOptions.sortOrder != SortOrder.Badluck && jitOptions.granularity.flag == irTree.code)
          JoinIndexes.getOnlineSort(
            children,
            jitOptions.getSortFn(storageManager),
            rId,
            k,
            storageManager
          )
        else
          (children, k)
      val compiledOps = seqToLambda(sortedChildren.map(compile))
      sm => sm.joinProjectHelper_withHash(
        compiledOps(sm),
        rId,
        newK.hash,
        jitOptions.onlineSort
      )

    case UnionSPJOp(rId, k, children: _*) =>
      val (sortedChildren, _) =
        if (jitOptions.sortOrder != SortOrder.Unordered && jitOptions.sortOrder != SortOrder.Badluck)
          JoinIndexes.getPresort(
            children,
            jitOptions.getSortFn(storageManager),
            rId,
            k,
            storageManager
          )
        else
          (children, k)

      val compiledOps = seqToLambda(sortedChildren.map(compile))
      sm => sm.union(compiledOps(sm))

    case UnionOp(label, children: _*) =>
      val compiledOps = seqToLambda(children.map(compile))
      sm => sm.union(compiledOps(sm))

    case DiffOp(children: _*) =>
      val clhs = compile(children.head)
      val crhs = compile(children(1))
      sm => sm.diff(clhs(sm), crhs(sm))
}
