package datalog.execution.ir

import datalog.execution.{PrecedenceGraph, StagedCompiler, ir, JoinIndexes}
import datalog.execution.ast.*
import datalog.storage.{CollectionsStorageManager, DB, KNOWLEDGE, RelationId, StorageManager}
import datalog.tools.Debug
import datalog.tools.Debug.debug

import java.util.concurrent.atomic.AtomicReference
import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.quoted.*
import scala.util.{Failure, Success}

enum OpCode:
  case PROGRAM, SWAP_CLEAR, SEQ, SCAN, SCANEDB, SPJ, INSERT, UNION, DIFF, DEBUG, DOWHILE,
  EVAL_RULE_NAIVE, EVAL_RULE_SN, EVAL_RULE_BODY, EVAL_NAIVE, EVAL_SN, LOOP_BODY, OTHER // convenience labels for generating functions

// TODO: make general SM not collections
type CompiledFn = CollectionsStorageManager => Any
type CompiledRelFn = CollectionsStorageManager => CollectionsStorageManager#EDB
type CompiledSnippetContinuationFn = (CollectionsStorageManager, Seq[CompiledFn]) => Any
type CompiledRelSnippetContinuationFn = (CollectionsStorageManager, Seq[CompiledRelFn]) => CollectionsStorageManager#EDB
/**
 * Intermediate representation based on Souffle's RAM
 */
abstract class IROp[T](val children: IROp[T]*) {
  val code: OpCode
  var compiledFn: Future[CollectionsStorageManager => T] = null
  var blockingCompiledFn: CollectionsStorageManager => T = null
  var compiledSnippetContinuationFn: (CollectionsStorageManager, Seq[CollectionsStorageManager => T]) => T = null

  /**
   * Add continuation to revert control flow to the interpret method, which checks for optimizations/deoptimizations
   */
  def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CollectionsStorageManager => T]): T =
    throw new Exception(s"Error: calling run on likely rel op with continuation: $code")

  /**
   * Keep control flow entirely within nodes, useful to minimize the size of the dotty-generated AST?
   */
  def run(storageManager: CollectionsStorageManager): T =
    throw new Exception(s"Error: calling run on likely rel op: $code")
}

//abstract class IROp[CollectionsStorageManager#EDB](override val children: IROp[CollectionsStorageManager#EDB]*) extends IROp(children:_*) {
//  var compiledRelFn: Future[CompiledRelFn] = null
//  var compiledRelSnippetContinuationFn: CompiledRelSnippetContinuationFn = null
//}

/**
 * @param children: SequenceOp[SequenceOp.NaiveEval, DoWhileOp]
 */
case class ProgramOp(override val children: IROp[Any]*) extends IROp[Any](children:_*) {
  val code: OpCode = OpCode.PROGRAM
  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CollectionsStorageManager => Any]): Any =
    opFns.head(storageManager)
  override def run(storageManager: CollectionsStorageManager): Any =
    children.head.run(storageManager)

  // convenience methods to get certain subtrees
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
case class DoWhileOp(toCmp: DB, override val children: IROp[Any]*) extends IROp[Any](children:_*) {
  val code: OpCode = OpCode.DOWHILE
  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn]): Any =
    while ( {
      opFns.head(storageManager)
//      ctx.count += 1 // TODO: do we need this outside debugging?
      toCmp match {
        case DB.Derived =>
          !storageManager.compareDerivedDBs()
        case DB.Delta =>
          storageManager.compareNewDeltaDBs()
      }
    }) ()
  override def run(storageManager: CollectionsStorageManager): Any =
    while ( {
      children.head.run(storageManager)
      toCmp match {
        case DB.Derived =>
          !storageManager.compareDerivedDBs()
        case DB.Delta =>
          storageManager.compareNewDeltaDBs()
      }
    }) ()
}

/**
 * @param code
 * @param children: [Any*]
 */
case class SequenceOp(override val code: OpCode, override val children: IROp[Any]*) extends IROp[Any](children:_*) {
  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn]): Any =
    opFns.map(o => o(storageManager))
  override def run(storageManager: CollectionsStorageManager): Any =
    children.map(o => o.run(storageManager))
}

case class SwapAndClearOp() extends IROp[Any] {
  val code: OpCode = OpCode.SWAP_CLEAR
  override def run(storageManager: CollectionsStorageManager): Any =
    storageManager.swapKnowledge()
    storageManager.clearNewDerived()

  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledFn]): Any =
    run(storageManager)
}

/**
 * @param rId
 * @param db
 * @param knowledge
 * @param children: [Scan|Union, Scan?]
 */
case class InsertOp(rId: RelationId, db: DB, knowledge: KNOWLEDGE, override val children: IROp[Any]*) extends IROp[Any](children:_*) {
  val code: OpCode = OpCode.INSERT
  override def run_continuation(storageManager:  CollectionsStorageManager, opFns: Seq[CompiledFn]): Any =
    val res = opFns.head.asInstanceOf[CompiledRelFn](storageManager)
    val res2 = if (opFns.size == 1) storageManager.EDB() else opFns(1).asInstanceOf[CompiledRelFn](storageManager)
    db match {
      case DB.Derived =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.resetKnownDerived(rId, res, res2)
          case KNOWLEDGE.New =>
            storageManager.resetNewDerived(rId, res, res2)
        }
      case DB.Delta =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.resetKnownDelta(rId, res)
          case KNOWLEDGE.New =>
            storageManager.resetNewDelta(rId, res)
        }
    }
  override def run(storageManager: CollectionsStorageManager): Any =
    val res = children.head.run(storageManager).asInstanceOf[CollectionsStorageManager#EDB]
    val res2 = if (children.size > 1) children(1).run(storageManager).asInstanceOf[CollectionsStorageManager#EDB] else storageManager.EDB()
    db match {
      case DB.Derived =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.resetKnownDerived(rId, res, res2)
          case KNOWLEDGE.New =>
            storageManager.resetNewDerived(rId, res, res2)
        }
      case DB.Delta =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.resetKnownDelta(rId, res)
          case KNOWLEDGE.New =>
            storageManager.resetNewDelta(rId, res)
        }
    }
}

case class ScanOp(rId: RelationId, db: DB, knowledge: KNOWLEDGE) extends IROp[CollectionsStorageManager#EDB] {
  val code: OpCode = OpCode.SCAN

  override def run(storageManager: CollectionsStorageManager): CollectionsStorageManager#EDB =
    db match {
      case DB.Derived =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.getKnownDerivedDB(rId)
          case KNOWLEDGE.New =>
            storageManager.getNewDerivedDB(rId)
        }
      case DB.Delta =>
        knowledge match {
          case KNOWLEDGE.Known =>
            storageManager.getKnownDeltaDB(rId)
          case KNOWLEDGE.New =>
            storageManager.getNewDeltaDB(rId)
        }
    }
  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn]): CollectionsStorageManager#EDB =
    run(storageManager)
}

case class ScanEDBOp(rId: RelationId) extends IROp[CollectionsStorageManager#EDB] {
  val code: OpCode = OpCode.SCANEDB
  override def run(storageManager: CollectionsStorageManager): CollectionsStorageManager#EDB =
    storageManager.edbs.getOrElse(rId, storageManager.EDB())

  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn]): CollectionsStorageManager#EDB =
    run(storageManager)
}
/**
 * @param joinIdx
 * @param children: [Scan*deps]
 */
case class ProjectJoinFilterOp(rId: RelationId, hash: String, override val children: ScanOp*) extends IROp[CollectionsStorageManager#EDB](children:_*) {
  val code: OpCode = OpCode.SPJ

  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn]): CollectionsStorageManager#EDB =
    println("in cont")
    val inputs = opFns.map(s => s(storageManager))
    val (sorted, newHash) = JoinIndexes.getSortAhead(
      inputs.toArray,
      edb => edb.size,
      rId,
      hash,
      storageManager
    )
    storageManager.joinProjectHelper_withHash(
      sorted,
      rId,
      newHash
    )
  override def run(storageManager: CollectionsStorageManager): CollectionsStorageManager#EDB =
//    val k = storageManager.allRulesAllIndexes(rId)(hash)
//    var tToAtom = children.zipWithIndex.map((t, i) => (t, k.atoms(i + 1))).sortBy((t, _) => t.run(storageManager).size)
//    if (storageManager.sortAhead == -1) tToAtom = tToAtom.reverse
//    val newK = JoinIndexes((k.atoms.head +: tToAtom.map(_._2)).toArray)
//    val sorted = tToAtom.map(_._1)
//    val input = sorted.map(s => s.run(storageManager))
    val inputs = children.map(s => s.run(storageManager))
    val (sorted, newHash) = JoinIndexes.getSortAhead(
      inputs.toArray,
      edb => edb.size,
      rId,
      hash,
      storageManager
    )
//  storageManager.joinProjectHelper(
//    input,
//    newK
//  )
//  storageManager.joinProjectHelper_withHash(
    storageManager.joinProjectHelper_withHash(
      sorted,
      rId,
      newHash
    )
}

/**
 * @param code
 * @param children: [Scan|UnionSPJ*rules]
 */
case class UnionOp(override val code: OpCode, override val children: IROp[CollectionsStorageManager#EDB]*) extends IROp[CollectionsStorageManager#EDB](children:_*) {
  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn]): CollectionsStorageManager#EDB =
    storageManager.union(opFns.map(o => o(storageManager)))
  override def run(storageManager: CollectionsStorageManager): CollectionsStorageManager#EDB =
    storageManager.union(children.map(o => o.run(storageManager)))
}

/**
 * Special case union for single rule body; for convenience
 * @param code
 * @param children: [Scan*atoms]
 */
case class UnionSPJOp(rId: RelationId, hash: String, override val children: ProjectJoinFilterOp*) extends IROp[CollectionsStorageManager#EDB](children:_*) {
  val code: OpCode = OpCode.EVAL_RULE_BODY
  // for now not filled out bc not planning on compiling higher than this
  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn]): CollectionsStorageManager#EDB =
    storageManager.union(opFns.map(o => o(storageManager)))
    // this is called if the compiled version isn't ready yet
//     TODO: start compiling for the joins here?
//    ???

  override def run(storageManager: CollectionsStorageManager): CollectionsStorageManager#EDB =
    val sortedChildren = JoinIndexes.getPreSortAhead( // TODO: this isn't saved anywhere, in case this is traversed again
      children.toArray,
      a => storageManager.getKnownDerivedDB(a.rId).size,
      rId,
      hash,
      storageManager
    )
    storageManager.union(sortedChildren.map(o => o.run(storageManager)))
}
/**
 * @param children: [Union|Scan, Scan]
 */
case class DiffOp(override val children: IROp[CollectionsStorageManager#EDB]*) extends IROp[CollectionsStorageManager#EDB](children:_*) {
  val code: OpCode = OpCode.DIFF
  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn]): CollectionsStorageManager#EDB =
    storageManager.diff(opFns(0)(storageManager), opFns(1)(storageManager))
  override def run(storageManager: CollectionsStorageManager): CollectionsStorageManager#EDB =
    storageManager.diff(children.head.run(storageManager), children(1).run(storageManager))
}

case class DebugNode(prefix: String, dbg: () => String) extends IROp[Any] {
  val code: OpCode = OpCode.DEBUG
  override def run(storageManager: CollectionsStorageManager): Any =
    debug(prefix, dbg)
}

/**
 * @param prefix - text to write
 * @param dbg - more to write, potentially a toString method on children.head
 * @param children - [IROp[CollectionsStorageManager#EDB]] to return
 */
case class DebugPeek(prefix: String, dbg: () => String, override val children: IROp[CollectionsStorageManager#EDB]*) extends IROp[CollectionsStorageManager#EDB](children:_*) {
  val code: OpCode = OpCode.DEBUG
  override def run_continuation(storageManager: CollectionsStorageManager, opFns: Seq[CompiledRelFn]): CollectionsStorageManager#EDB =
    val res = opFns.head(storageManager)
    debug(prefix, () => s"${dbg()} ${storageManager.printer.factToString(res)}")
    res
  override def run(storageManager: CollectionsStorageManager): CollectionsStorageManager#EDB =
    val res = children.head.run(storageManager)
    debug(prefix, () => s"${dbg()} ${storageManager.printer.factToString(res)}")
    res
}
