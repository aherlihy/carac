package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.ir.*
import datalog.storage.*
import datalog.tools.Debug.debug
import org.glavo.classfile.CodeBuilder

import java.lang.constant.MethodTypeDesc
import java.lang.invoke.MethodType
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.{immutable, mutable}
import scala.quoted.*

/**
 * Separate out compile logic from StagedExecutionEngine
 */
class BytecodeCompiler(val storageManager: StorageManager)(using JITOptions) extends StagedCompiler(storageManager) {
  given staging.Compiler = jitOptions.dotty
  class IRBytecodeGenerator(methType: MethodType) extends BytecodeGenerator[IROp[?]](
    clsName = "datalog.execution.Generated$$Hidden", methType
  ) {
    import BytecodeGenerator.*

    // protected override val debug = true
    protected def enterTraverse(xb: CodeBuilder, irTree: IROp[?]): Unit =
      traverse(xb, irTree)

    protected def traverse(xb: CodeBuilder, irTree: IROp[?]): Unit = {
      irTree match {
        case ProgramOp(c) =>
          traverse(xb, c)

        case DoWhileOp(toCmp, children:_*) =>
          val compMeth = "compareNewDeltaDBs"
          xb.block: xxb =>
            // do
            discardResult(xxb, traverse(xxb, children.head)) // why is this a list if we only ever use the head?
            // while
            xb.aload(0)
            emitCall(xxb, classOf[StorageManager], compMeth)
            xxb.ifne(xxb.startLabel)

        case SwapAndClearOp() =>
          xb.aload(0)
          emitSMCall(xb, "swapKnowledge")
          xb.aload(0)
          emitSMCall(xb, "clearNewDeltas")

        case SequenceOp(label, children:_*) =>
          // TODO: take into account heuristics.max_relations? We could create a
          // CodeBuilder for one or more new methods we would immediately call.
          children.foreach(c => discardResult(xb, traverse(xb, c)))

        case InsertDeltaNewIntoDerived() =>
          xb.aload(0)
          emitSMCall(xb, "insertDeltaIntoDerived")

        case ResetDeltaOp(rId, children:_*) =>
          xb.aload(0)
            .constantInstruction(rId)
          traverse(xb, children.head)
          emitSMCall(xb, "setNewDelta", classOf[Int], classOf[EDB])

        case ScanOp(rId, db, knowledge) =>
          val meth = db match {
            case DB.Derived =>
              "getKnownDerivedDB"
            case DB.Delta =>
              knowledge match {
                case KNOWLEDGE.New =>
                  "getNewDeltaDB"
                case KNOWLEDGE.Known =>
                  "getKnownDeltaDB"
              }
          }
          xb.aload(0)
            .constantInstruction(rId)
          emitSMCall(xb, meth, classOf[Int])

        case ComplementOp(rId, arity) =>
          xb.aload(0)
            .constantInstruction(rId)
            .constantInstruction(arity)
          emitSMCall(xb, "getComplement", classOf[Int], classOf[Int])

        case ScanEDBOp(rId) =>
          xb.aload(0)
          if (storageManager.edbContains(rId))
            xb.constantInstruction(rId)
            emitSMCall(xb, "getEDB", classOf[Int])
          else
            emitSMCall(xb, "getEmptyEDB")

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

          xb.aload(0)
          emitSeq(xb, sortedChildren.map(c => xxb => traverse(xxb, c)))
          xb.constantInstruction(rId)
          emitString(xb, newK.hash)
          emitBool(xb, jitOptions.onlineSort)
          emitSMCall(xb, "selectProjectJoinHelper",
            classOf[Seq[?]], classOf[Int], classOf[String], classOf[Boolean])

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
          // Duplicate code with UnionSPJOp
          xb.aload(0)
          emitSeq(xb, sortedChildren.map(c => xxb => traverse(xxb, c)))
          emitSMCall(xb, "union", classOf[Seq[?]])

        case UnionOp(label, children: _*) =>
          xb.aload(0)
          emitSeq(xb, children.map(c => xxb => traverse(xxb, c)))
          emitSMCall(xb, "union", classOf[Seq[?]])

        case DiffOp(children:_*) =>
          xb.aload(0)
          traverse(xb, children(0))
          traverse(xb, children(1))
          emitSMCall(xb, "diff", classOf[EDB], classOf[EDB])

        case DebugPeek(prefix, msg, children: _*) =>
          assert(false, s"Unimplemented node: $irTree")

        case DebugNode(prefix, msg) =>
          assert(false, s"Unimplemented node: $irTree")
      }
    }

    /**
     *  Call `methName` on a `StorageManager`.
     *
     *  @pre The stack has the shape [... storageManagerObj methArgs*]
     */
    private def emitSMCall(xb: CodeBuilder, methName: String, methParameterTypes: Class[?]*): Unit =
      emitCall(xb, classOf[StorageManager], methName, methParameterTypes*)
  }

  override def compile[T](irTree: IROp[T]): CompiledFn[T] = {
    val methType = MethodType.methodType(irTree.classTag.runtimeClass, classOf[StorageManager])
    val generator = IRBytecodeGenerator(methType)
    val entryPoint = generator.generateAndLoad(irTree)
    val compiledFn = (sm: StorageManager) => entryPoint.invoke(sm): T
    compiledFn
  }
}
