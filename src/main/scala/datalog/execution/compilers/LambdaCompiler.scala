package datalog.execution

import datalog.execution.ir.*
import datalog.storage.*

import scala.collection.{immutable}
import scala.quoted.*
import scala.reflect.{classTag, ClassTag}

/**
 * Separate out compile logic from StagedExecutionEngine
 */
class LambdaCompiler(val storageManager: StorageManager)(using JITOptions) extends StagedCompiler(storageManager) {
  given staging.Compiler = jitOptions.dotty
  /** Convert a Seq of lambdas into a lambda returning a Seq. */
  def seqToLambda[T](seq: Seq[StorageManager => T], inParallel: Boolean = false): StorageManager => Seq[T] =
    if inParallel then
      return sm => IROp.runFns(sm, seq, inParallel)(using classTag[AnyRef].asInstanceOf[ClassTag[T]])
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
    case ProgramOp(children*) =>
      compile(children.head)

    case DoWhileOp(toCmp, children*) =>
      val cond: CompiledFn[Boolean] = _.deltasEmpty()
      val body = compile(children.head)
      sm =>
        while {
          body(sm)
          cond(sm)
        } do ()

    case SwapAndClearOp() =>
      sm =>
        sm.swapReadWriteDeltas()
        sm.clearPreviousDeltas()

    case SequenceOp(label, children*) =>
      val cOps: Array[CompiledFn[Any]] = children.map(compile).toArray
//      assert(false, "This is never triggered")
      if irTree.runInParallel then
        // TODO: optimize by directly using the underlying Java stuff.
        sm => IROp.runFns(sm, immutable.ArraySeq.unsafeWrapArray(cOps), inParallel = true)
      else cOps.length match
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
    case InsertDeltaNewIntoDerived() =>
      sm => sm.insertDeltaIntoDerived()

    case ResetDeltaOp(rId, children*) =>
      val res = compile(children.head.asInstanceOf[IROp[EDB]])
      sm => sm.writeNewDelta(rId, res(sm))

    case ScanOp(rId, db) =>
      db match {
        case DB.Derived =>
          _.getDerivedDB(rId)
        case DB.Delta =>
          _.getDeltaDB(rId)
      }

    case ComplementOp(r, arity) =>
      _.getComplement(r, arity)

    case ScanEDBOp(rId) =>
      _.getEDB(rId)

    case ProjectJoinFilterOp(rId, k, children*) =>
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
      sm => sm.selectProjectJoinHelper(
        compiledOps(sm),
        rId,
        newK.hash,
        jitOptions.onlineSort
      )

    case UnionSPJOp(rId, k, children*) =>
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

      val compiledOps = seqToLambda(sortedChildren.map(compile), inParallel = irTree.runInParallel)
      sm => sm.union(compiledOps(sm))

    case UnionOp(label, children*) =>
      val compiledOps = seqToLambda(children.map(compile), inParallel = irTree.runInParallel)
      sm => sm.union(compiledOps(sm))

    case DiffOp(children*) =>
      val clhs = compile(children.head)
      val crhs = compile(children(1))
      sm => sm.diff(clhs(sm), crhs(sm))
}
