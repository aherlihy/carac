package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.ir.*
import datalog.storage.{DB, EDB, KNOWLEDGE, RelationId, StorageManager}
import datalog.tools.Debug.debug

import java.lang.invoke.MethodType
import java.util.concurrent.atomic.AtomicInteger
import scala.quoted.*
import org.glavo.classfile.CodeBuilder

import scala.collection.{immutable, mutable}

/**
 * Separate out compile logic from StagedExecutionEngine
 */
class StagedCompiler(val storageManager: StorageManager)(using val jitOptions: JITOptions) {
  given staging.Compiler = jitOptions.dotty
  // TODO: move Exprs to where classes are defined?
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


  /** Convert a Seq of lambdas into a lambda returning a Seq. */
  def seqToLambda[T](seq: Seq[StorageManager => T]): StorageManager => Seq[T] =
    // This could be optimized for specific Seq subclasses such as `ArraySeq.ofRef`.
    sm =>
      seq.map(lambda => lambda(sm))

  /** Convert an Array of lambdas into a lambda returning an Array. */
  def arrayToLambda[T](arr: Array[StorageManager => T]): StorageManager => Array[T] =
    // TODO: unroll based on arr.length?
    sm =>
      // This cast will break if T is a primitive, but right now the T in IROp[T]
      // is always a reference type, so we can be more efficient
      // TODO: Change "class IROp[T]" to "class IROp[T <: AnyRef]" so we can construct
      // an Array[T] efficiently without casts, or support primitives too.
      val out = (new Array[AnyRef](arr.length)).asInstanceOf[Array[T]]
      var i = 0
      while (i < arr.length)
        out(i) = arr(i)(sm)
        i += 1
      out

  /** "Compile" an IRTree into nested lambda calls. */
  def compileToLambda[T](irTree: IROp[T]): CompiledFn[T] = irTree match
    case ProgramOp(children:_*) =>
      compileToLambda(children.head)

    case DoWhileOp(toCmp, children:_*) =>
      val cond: CompiledFn[Boolean] = toCmp match {
        case DB.Derived =>
          !_.compareDerivedDBs()
        case DB.Delta =>
          _.compareNewDeltaDBs()
      }
      val body = compileToLambda(children.head)
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
      val cOps: Array[CompiledFn[Any]] = children.map(compileToLambda).toArray
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
      val res = compileToLambda(children.head.asInstanceOf[IROp[EDB]])
      db match {
        case DB.Derived =>
          val res2: CompiledFn[EDB] =
            if (children.length > 1)
              compileToLambda(children(1).asInstanceOf[IROp[EDB]])
            else
              _.getEmptyEDB()
          knowledge match {
            case KNOWLEDGE.New =>
              sm => sm.resetNewDerived(rId, res(sm), res2(sm))
            case KNOWLEDGE.Known =>
              sm => sm.resetKnownDerived(rId, res(sm), res2(sm))
          }
        case DB.Delta =>
          knowledge match {
            case KNOWLEDGE.New =>
              sm => sm.resetNewDelta(rId, res(sm))
            case KNOWLEDGE.Known =>
              sm => sm.resetKnownDelta(rId, res(sm))
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

    case ComplementOp(arity) =>
      _.getComplement(arity)

    case ScanEDBOp(rId) =>
      if (storageManager.edbContains(rId))
        _.getEDB(rId)
      else
        _.getEmptyEDB()

    case ProjectJoinFilterOp(rId, k, children: _*) =>
      val compiledOps = seqToLambda(children.map(compileToLambda))
      sm => sm.joinProjectHelper_withHash(
        compiledOps(sm),
        rId,
        k.hash,
        jitOptions.onlineSort
      )

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

      val compiledOps = arrayToLambda(sortedChildren.map(compileToLambda))
      sm => sm.union(compiledOps(sm))

    case UnionOp(label, children: _*) =>
      val compiledOps = seqToLambda(children.map(compileToLambda))
      sm => sm.union(compiledOps(sm))

    case DiffOp(children: _*) =>
      val clhs = compileToLambda(children.head)
      val crhs = compileToLambda(children(1))
      sm => sm.diff(clhs(sm), crhs(sm))

  class IRBytecodeGenerator(methType: MethodType) extends BytecodeGenerator[IROp[?]](
    clsName = "datalog.execution.Generated$$Hidden", methType
  ) {
    import BytecodeGenerator.*

    // protected override val debug = true

    protected def traverse(xb: CodeBuilder, irTree: IROp[?]): Unit = irTree match {
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

  def getBytecodeGenerated[T](irTree: IROp[T]): CompiledFn[T] = {
    val methType = MethodType.methodType(irTree.classTag.runtimeClass, classOf[StorageManager])
    val generator = IRBytecodeGenerator(methType)
    val entryPoint = generator.generateAndLoad(irTree)
    val compiledFn = (sm: StorageManager) => entryPoint.invoke(sm): T
    compiledFn
  }

  def getCompiled[T](irTree: IROp[T]): CompiledFn[T] = {
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
  def getCompiledIndexed[T](irTree: IROp[T]): CompiledFnIndexed[T] = {
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
