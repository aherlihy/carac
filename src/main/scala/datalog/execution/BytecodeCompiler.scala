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
class BytecodeCompiler(val storageManager: StorageManager)(using val jitOptions: JITOptions) extends StagedCompiler(storageManager) {
  given staging.Compiler = jitOptions.dotty
  class IRBytecodeGenerator(methType: MethodType) extends BytecodeGenerator[IROp[?]](
    clsName = "datalog.execution.Generated$$Hidden", methType
  ) {
    import BytecodeGenerator.*

    // protected override val debug = true
    protected def enterTraverse(xb: CodeBuilder, irTree: IROp[?]): Unit =
      traverse(xb, irTree)

    protected def traverse(xb: CodeBuilder, irTree: IROp[?]): Unit =
      irTree match {
      case ProgramOp(c) =>
        traverse(xb, c)

      case DoWhileOp(toCmp, children:_*) =>
        val compMeth = toCmp match
           case DB.Derived => "compareDerivedDBs"
           case DB.Delta => "compareNewDeltaDBs"
        xb.block: xxb =>
          // do
          discardResult(xxb, traverse(xxb, children.head)) // why is this a list if we only ever use the head?
          // while
          xb.aload(0)
          emitCall(xxb, classOf[StorageManager], compMeth)
          toCmp match
            case DB.Derived => xxb.ifeq(xxb.startLabel)
            case DB.Delta   => xxb.ifne(xxb.startLabel)

      case UpdateDiscoveredOp() =>
        xb.aload(0)
        emitSMCall(xb, "updateDiscovered")

      case SwapAndClearOp() =>
        xb.aload(0)
        emitSMCall(xb, "swapKnowledge")
        xb.aload(0)
        emitSMCall(xb, "clearNewDerived")

      case SequenceOp(label, children:_*) =>
        // TODO: take into account heuristics.max_relations? We could create a
        // CodeBuilder for one or more new methods we would immediately call.
        children.foreach(c => discardResult(xb, traverse(xb, c)))

      case InsertOp(rId, db, knowledge, children:_*) =>
        xb.aload(0)
          .constantInstruction(rId)
        traverse(xb, children.head)
        db match
          case DB.Derived =>
            if children.length > 1 then
              traverse(xb, children(1))
            else
              xb.aload(0)
              emitSMCall(xb, "getEmptyEDB")
            val methName = knowledge match
              case KNOWLEDGE.New => "resetNewDerived"
              case KNOWLEDGE.Known => "resetKnownDerived"
            emitSMCall(xb, methName, classOf[Int], classOf[EDB], classOf[EDB])
          case DB.Delta =>
            val methName = knowledge match
              case KNOWLEDGE.New => "resetNewDelta"
              case KNOWLEDGE.Known => "resetKnownDelta"
            emitSMCall(xb, methName, classOf[Int], classOf[EDB])

      case ScanOp(rId, db, knowledge) =>
        val meth = db match {
          case DB.Derived =>
            knowledge match {
              case KNOWLEDGE.New =>
                "getNewDerivedDB"
              case KNOWLEDGE.Known =>
                "getKnownDerivedDB"
            }
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

      case ComplementOp(arity) =>
        xb.aload(0)
          .constantInstruction(arity)
        emitSMCall(xb, "getComplement", classOf[Int])

      case ScanEDBOp(rId) =>
        xb.aload(0)
        if (storageManager.edbContains(rId))
          xb.constantInstruction(rId)
          emitSMCall(xb, "getEDB", classOf[Int])
        else
          emitSMCall(xb, "getEmptyEDB")

      case ProjectJoinFilterOp(rId, k, children: _*) =>
        xb.aload(0)
        emitSeq(xb, children.map(c => xxb => traverse(xxb, c)))
        xb.constantInstruction(rId)
        emitString(xb, k.hash)
        emitBool(xb, jitOptions.onlineSort)
        emitSMCall(xb, "joinProjectHelper_withHash",
          classOf[Seq[?]], classOf[Int], classOf[String], classOf[Boolean])

      case UnionSPJOp(rId, k, children: _*) =>
        val (sortedChildren, _) =
          if (jitOptions.sortOrder != SortOrder.Unordered)
            JoinIndexes.getPresort(
              children.toArray,
              jitOptions.getSortFn(storageManager),
              rId,
              k,
              storageManager
            )
          else
            (children.toArray, k)
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

    /**
     *  Call `methName` on a `StorageManager`.
     *
     *  @pre The stack has the shape [... storageManagerObj methArgs*]
     */
    private def emitSMCall(xb: CodeBuilder, methName: String, methParameterTypes: Class[?]*): Unit =
      emitCall(xb, classOf[StorageManager], methName, methParameterTypes*)
  }

  class IndexedIRBytecodeGenerator(methType: MethodType) extends IRBytecodeGenerator(methType) {
    import BytecodeGenerator.*
    override protected def enterTraverse(xb: CodeBuilder, irTree: IROp[?]): Unit =
      irTree match {
      case UnionSPJOp(rId, k, children: _*) =>
        val (sortedChildren, _) =
          if (jitOptions.sortOrder != SortOrder.Unordered)
            JoinIndexes.getPresort(
              children.toArray,
              jitOptions.getSortFn(storageManager),
              rId,
              k,
              storageManager
            )
          else
            (children.toArray, k)
        // Duplicate code with UnionSPJOp
        xb.aload(0)
        emitSeq(xb, sortedChildren.map(c => xxb => traverse(xxb, c)))
//        emitSMCall(xb, "union", classOf[Seq[?]])

      case UnionOp(label, children: _*) =>
        xb.aload(0)
        emitSeq(xb, children.map(c => xxb => traverse(xxb, c)))
//        emitSMCall(xb, "union", classOf[Seq[?]])

      case _ => throw new Exception(s"Indexed compilation: Unhandled IROp ${irTree.code}")
    }
  }

  override def compile[T](irTree: IROp[T]): CompiledFn[T] = {
    val methType = MethodType.methodType(irTree.classTag.runtimeClass, classOf[StorageManager])
    val generator = IRBytecodeGenerator(methType)
    val entryPoint = generator.generateAndLoad(irTree)
    val compiledFn = (sm: StorageManager) => entryPoint.invoke(sm): T
    compiledFn
  }

  override def compileIndexed[T](irTree: IROp[T]): CompiledFnIndexed[T] = {
    val methType = MethodType.methodType(irTree.classTag.runtimeClass, classOf[StorageManager], classOf[Int])
    val generator = IndexedIRBytecodeGenerator(methType)
    val entryPoint = generator.generateAndLoad(irTree)
    val compiledFn = (sm: StorageManager, i: Int) => entryPoint.invoke(sm, i): T
    compiledFn
  }
}
