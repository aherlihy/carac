package datalog.execution.ir

import datalog.execution.{JITOptions, JoinIndexes, SortOrder, ir}
import datalog.storage.{DB, EDB, RelationId, StorageManager}
import datalog.tools.Debug
import datalog.tools.Debug.debug

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

enum OpCode:
  case PROGRAM, SWAP_CLEAR, SEQ,
  SCAN, SCANEDB, SCAN_DISCOVERED,
  COMPLEMENT,
  SPJ, UNION, DIFF,
  INSERT_INTO, RESET_DELTA,
  DEBUG, DEBUGP, DOWHILE, UPDATE_DISCOVERED,
  EVAL_STRATUM, EVAL_RULE_NAIVE, EVAL_RULE_SN, EVAL_RULE_BODY, EVAL_NAIVE, EVAL_SN, LOOP_BODY, OTHER // convenience labels for generating functions
object OpCode {
  def relational(opCode: OpCode): Boolean =
    Seq(SCAN, OpCode.SCANEDB, OpCode.SPJ, OpCode.UNION, OpCode.DIFF, OpCode.EVAL_RULE_BODY, OpCode.EVAL_RULE_NAIVE, OpCode.EVAL_RULE_SN).contains(opCode)
}

type CompiledFn[T] = StorageManager => T
type CompiledFnIndexed[T] = (StorageManager, Int) => T
//type CompiledSnippetContinuationFn = (StorageManager, Seq[CompiledFn]) => Any
/**
 * Intermediate representation based on Souffle's RAM
 */
abstract class IROp[T](val children: IROp[T]*)(using val jitOptions: JITOptions, val classTag: ClassTag[T]) {
  val code: OpCode
  var compiledFn: Future[CompiledFn[T]] = null
  var blockingCompiledFn: CompiledFn[T] = null // for when we're blocking and not ahead-of-time, so might as well skip the future
  var compiledSnippetContinuationFn: (StorageManager, Seq[StorageManager => T]) => T = null

  /** Should the children of this op be run in parallel? */
  val runInParallel: Boolean = false

  /**
   * Add continuation to revert control flow to the interpret method, which checks for optimizations/deoptimizations
   */
  def run_continuation(storageManager: StorageManager, opFns: Seq[StorageManager => T]): T =
    throw new Exception(s"Error: calling run on likely rel op with continuation: $code")

  /**
   * Keep control flow entirely within nodes, useful to minimize the size of the dotty-generated AST?
   */
  def run(storageManager: StorageManager): T =
    throw new Exception(s"Error: calling run on likely rel op: $code")

}
object IROp {
  given ExecutionContext = ExecutionContext.global
  def runFns[T: ClassTag](storageManager: StorageManager, seq: Seq[StorageManager => T], inParallel: Boolean = false): Seq[T] =
//    if seq.length == 1 then
//      return immutable.ArraySeq.unsafeWrapArray(Array(seq.head(storageManager)))
//    if !inParallel then
      return seq.map(_(storageManager))
//    val futures = immutable.ArraySeq.newBuilder[Future[T]]
//    futures.sizeHint(seq.length)
//     Spawn threads for the N - 1 first children
//    seq.view.init.foreach: op =>
//      futures += Future(op(storageManager))
//     Run the last child on the current thread.
//    val last = seq.last(storageManager)
//    futures += Future(last)
//    Await.result(Future.sequence(futures.result()), Duration.Inf)
}
import IROp.*

/**
 * @param children: SequenceOp[SequenceOp.NaiveEval, DoWhileOp]
 */
case class ProgramOp(override val children:IROp[Any]*)(using JITOptions) extends IROp[Any](children*) {
  val code: OpCode = OpCode.PROGRAM
  override def run_continuation(storageManager: StorageManager, opFns: Seq[StorageManager => Any]): Any =
    opFns.head(storageManager)
  override def run(storageManager: StorageManager): Any =
    children.head.run(storageManager)

  // convenience methods to get certain subtrees for benchmarking/testing
  def getSubTree(code: OpCode): IROp[Any] =
    code match
      case OpCode.PROGRAM =>
        this
      case OpCode.EVAL_NAIVE =>
        children.head.children(0)
      case OpCode.DOWHILE =>
        children.head.children(1)
      case OpCode.LOOP_BODY =>
        getSubTree(OpCode.DOWHILE).children.head
      case OpCode.EVAL_SN =>
        getSubTree(OpCode.LOOP_BODY).children(1)
      case OpCode.EVAL_RULE_SN => // gets a bit weird here bc there are multiple of these nodes, just gets the first one. Including insert+diff
        getSubTree(OpCode.EVAL_SN).children.head.children.head.children.head.children.head
      case OpCode.EVAL_RULE_BODY =>
        getSubTree(OpCode.EVAL_RULE_SN).children.head
      case OpCode.SPJ =>
        getSubTree(OpCode.EVAL_RULE_BODY).children.head
      case OpCode.SCAN =>
        getSubTree(OpCode.SPJ).children.head.children.head
      case _ =>
        throw new Exception(s"getSubTree not supported for $code, could prob just add it")
}

/**
 * @param toCmp: DB
 * @param children: [SequenceOp.LoopBody]
 */
case class DoWhileOp(toCmp: DB, override val children:IROp[Any]*)(using JITOptions) extends IROp[Any](children*) {
  val code: OpCode = OpCode.DOWHILE
  override def run_continuation(storageManager: StorageManager, opFns: Seq[CompiledFn[Any]]): Any =
    while ({
      opFns.head(storageManager)
//      ctx.count += 1 // TODO: do we need this outside debugging?
      storageManager.deltasEmpty()
    }) ()
  override def run(storageManager: StorageManager): Any =
    var i = 0
    while ( {
      children.head.children.head.run(storageManager) // swap
//      println(s"DBs start of semi-naive iteration $i: ${storageManager.toString}")
      children.head.children(1).run(storageManager)
//      children.head.run(storageManager)
      i += 1
//      if i > 1 then System.exit(0)
      children.head.run(storageManager)
      storageManager.deltasEmpty()
    }) ()
}

/**
 * @param code
 * @param children: [Any*]
 */
case class SequenceOp(override val code: OpCode, override val children:IROp[Any]*)(using JITOptions) extends IROp[Any](children*) {
  override val runInParallel: Boolean = code == OpCode.EVAL_SN

  override def run_continuation(storageManager: StorageManager, opFns: Seq[CompiledFn[Any]]): Any =
    runFns(storageManager, opFns, inParallel = runInParallel)
  override def run(storageManager: StorageManager): Any =
    runFns(storageManager, children.map(_.run), inParallel = runInParallel)
}

case class SwapAndClearOp()(using JITOptions) extends IROp[Any] {
  val code: OpCode = OpCode.SWAP_CLEAR
  override def run(storageManager: StorageManager): Any =
    storageManager.swapReadWriteDeltas()
    storageManager.clearPreviousDeltas()

  override def run_continuation(storageManager: StorageManager, opFns: Seq[CompiledFn[Any]]): Any =
    run(storageManager)
}

case class InsertDeltaNewIntoDerived()(using JITOptions) extends IROp[Any]() {
  val code: OpCode = OpCode.INSERT_INTO
  override def run_continuation(storageManager:  StorageManager, opFns: Seq[CompiledFn[Any]]): Any =
    run(storageManager)

  override def run(storageManager: StorageManager): Any =
    storageManager.insertDeltaIntoDerived()
}
case class ResetDeltaOp(rId: RelationId, override val children:IROp[Any]*)(using JITOptions) extends IROp[Any](children*) {
  val code: OpCode = OpCode.RESET_DELTA
  override def run_continuation(storageManager:  StorageManager, opFns: Seq[CompiledFn[Any]]): Any =
    val res = opFns.head.asInstanceOf[CompiledFn[EDB]](storageManager)
    storageManager.writeNewDelta(rId, res)
  override def run(storageManager: StorageManager): Any =
    val res = children.head.run(storageManager).asInstanceOf[EDB]
    storageManager.writeNewDelta(rId, res)
}

case class ComplementOp(rId: RelationId, arity: Int)(using JITOptions) extends IROp[EDB] {
  val code: OpCode = OpCode.COMPLEMENT

  override def run(storageManager: StorageManager): EDB =
    storageManager.getComplement(rId, arity)

  override def run_continuation(storageManager: StorageManager, opFns: Seq[CompiledFn[EDB]]): EDB =
    run(storageManager) // bc leaf node, no difference for continuation or run
}

case class ScanOp(rId: RelationId, db: DB)(using JITOptions) extends IROp[EDB] {
  val code: OpCode = OpCode.SCAN

  override def run(storageManager: StorageManager): EDB =
    db match {
      case DB.Derived =>
        storageManager.getDerivedDB(rId)
      case DB.Delta =>
        storageManager.getDeltaDB(rId)
    }
  override def run_continuation(storageManager: StorageManager, opFns: Seq[CompiledFn[EDB]]): EDB =
    run(storageManager) // bc leaf node, no difference for continuation or run
}

case class ScanEDBOp(rId: RelationId)(using JITOptions) extends IROp[EDB] {
  val code: OpCode = OpCode.SCANEDB
  override def run(storageManager: StorageManager): EDB =
    storageManager.getEDB(rId)

  override def run_continuation(storageManager: StorageManager, opFns: Seq[CompiledFn[EDB]]): EDB =
    run(storageManager)
}
/**
 * @param joinIdx
 * @param children: [Scan*deps]
 */
case class ProjectJoinFilterOp(rId: RelationId, var k: JoinIndexes, override val children:IROp[EDB]*)(using jitOptions: JITOptions) extends IROp[EDB](children*) {
  val code: OpCode = OpCode.SPJ
  var childrenSO: Array[IROp[EDB]] = children.toArray

  override def run_continuation(storageManager: StorageManager, opFns: Seq[CompiledFn[EDB]]): EDB =
    val inputs = opFns.map(s => s(storageManager))
    storageManager.selectProjectJoinHelper(
      inputs,
      rId,
      k.hash,
      jitOptions.onlineSort
    )
  override def run(storageManager: StorageManager): EDB =
    val inputs = children.map(s => s.run(storageManager))
//    println(s"inputs in SPJU=${inputs.map(_.factToString)}")
    val res = storageManager.selectProjectJoinHelper(
        inputs,
        rId,
        k.hash,
        jitOptions.onlineSort
      )
//    println(s"=> result of SPJU on ${storageManager.printer.ruleToString(k.atoms)}: ${storageManager.ns(rId)}=${res.factToString}")
    res
}

/**
 * @param code
 * @param children: [Scan|UnionSPJ*rules]
 */
case class UnionOp(override val code: OpCode, override val children:IROp[EDB]*)(using JITOptions) extends IROp[EDB](children*) {
//  var compiledFnIndexed: java.util.concurrent.Future[CompiledFnIndexed[EDB]] = null
  var compiledFnIndexed: Future[CompiledFnIndexed[EDB]] = null
  var blockingCompiledFnIndexed: CompiledFnIndexed[EDB] = null

  override val runInParallel = true

  override def run_continuation(storageManager: StorageManager, opFns: Seq[CompiledFn[EDB]]): EDB =
    storageManager.union(runFns(storageManager, opFns, inParallel = runInParallel))
  override def run(storageManager: StorageManager): EDB =
    storageManager.union(runFns(storageManager, children.map(_.run), inParallel = runInParallel))
}

/**
 * Special case union for single rule body; for convenience
 * @param code
 * @param children: [Scan*atoms]
 */
case class UnionSPJOp(rId: RelationId, var k: JoinIndexes, override val children:ProjectJoinFilterOp*)(using JITOptions) extends IROp[EDB](children*) {
  val code: OpCode = OpCode.EVAL_RULE_BODY
  var compiledFnIndexed: Future[CompiledFnIndexed[EDB]] = null
//  var compiledFnIndexed: java.util.concurrent.Future[CompiledFnIndexed[EDB]] = null
  // for now not filled out bc not planning on compiling higher than this

  override val runInParallel = true

  override def run_continuation(storageManager: StorageManager, opFns: Seq[CompiledFn[EDB]]): EDB =
    storageManager.union(runFns(storageManager, opFns, inParallel = runInParallel))

  override def run(storageManager: StorageManager): EDB =
    if (jitOptions.sortOrder == SortOrder.Unordered || jitOptions.sortOrder == SortOrder.Badluck || children.length < 3 || jitOptions.granularity.flag != OpCode.OTHER) // If not only interpreting, then don't optimize since we are waiting for the optimized version to compile
      storageManager.union(runFns(storageManager, children.map(_.run), inParallel = runInParallel))
    else
      val (sortedChildren, newK) = JoinIndexes.getPresort(
        children,
        jitOptions.getSortFn(storageManager),
        rId,
        k,
        storageManager
      )
      storageManager.union(runFns(storageManager, sortedChildren.map(_.run), inParallel = runInParallel))
}
/**
 * @param children: [Union|Scan, Scan]
 */
case class DiffOp(override val children:IROp[EDB]*)(using JITOptions) extends IROp[EDB](children*) {
  val code: OpCode = OpCode.DIFF
  private val queryResult = children.head
  private val derivedKnownRead = children(1)
  override def run_continuation(storageManager: StorageManager, opFns: Seq[CompiledFn[EDB]]): EDB =
    storageManager.diff(opFns.head(storageManager), opFns(1)(storageManager)) // TODO: diffInPlace
  override def run(storageManager: StorageManager): EDB =
    storageManager.diff(queryResult.run(storageManager), derivedKnownRead.run(storageManager))
}

case class DebugNode(prefix: String, dbg: () => String)(using JITOptions) extends IROp[Any] {
  val code: OpCode = OpCode.DEBUG
  override def run(storageManager: StorageManager): Any =
    debug(prefix, dbg)
}

/**
 * @param prefix - text to write
 * @param dbg - more to write, potentially a toString method on children.head
 * @param children - [IROp[EDB]] to return
 */
case class DebugPeek(prefix: String, dbg: () => String, override val children:IROp[EDB]*)(using JITOptions) extends IROp[EDB](children*) {
  val code: OpCode = OpCode.DEBUGP
  override def run_continuation(storageManager: StorageManager, opFns: Seq[CompiledFn[EDB]]): EDB =
    val res = opFns.head(storageManager)
    debug(prefix, () => s"${dbg()} ${storageManager.printer.factToString(res)}")
    res
  override def run(storageManager: StorageManager): EDB =
    val res = children.head.run(storageManager)
    debug(prefix, () => s"${dbg()} ${storageManager.printer.factToString(res)}")
    res
}
