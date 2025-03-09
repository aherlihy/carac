package datalog.execution

import datalog.dsl.{Atom, Constant, StorageAtom, Term, Variable}
import datalog.execution
import datalog.execution.ast.*
import datalog.execution.ast.transform.{ASTTransformerContext, CopyEliminationPass, Transformer}
import datalog.execution.ir.*
import datalog.storage.{DB, EDB, KNOWLEDGE, StorageManager, StorageTerm, RelationId}
import datalog.tools.Debug.debug

import java.util.concurrent.{Executors, ForkJoinPool}
import scala.collection.mutable
import scala.collection.immutable
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future, blocking}
import scala.util.{Failure, Success}
import scala.quoted.*

class StagedExecutionEngine(val storageManager: StorageManager, val defaultJITOptions: JITOptions = JITOptions(mode = Mode.Interpreted)) extends ExecutionEngine {
  val precedenceGraph = new PrecedenceGraph(using storageManager.ns)
  val ast: ProgramNode = ProgramNode()
  private val tCtx = ASTTransformerContext(using precedenceGraph)(using storageManager)
  given JITOptions = defaultJITOptions
  val transforms: Seq[Transformer] = Seq(CopyEliminationPass(using tCtx))
  var threadpool: ForkJoinPool = null
//  var threadpool: ExecutorService = null

  val compiler: StagedCompiler = defaultJITOptions.backend match
    case Backend.Quotes => QuoteCompiler(storageManager)
    case Backend.Bytecode => BytecodeCompiler(storageManager)
    case Backend.Lambda => LambdaCompiler(storageManager)

  var stragglers: mutable.WeakHashMap[Int, Future[CompiledFn[?]]] = mutable.WeakHashMap.empty // should be ok since we are only removing by ref and then iterating on values only?

  def createIR(ast: ASTNode)(using InterpreterContext): IROp[Any] = IRTreeGenerator().generateTopLevelProgram(ast, naive=false)

  def initRelation(rId: Int, name: String): Unit = {
    storageManager.ns(rId) = name
    storageManager.initRelation(rId, name)
  }

  def get(rId: Int): Set[Seq[StorageTerm]] = {
    if (!storageManager.initialized)
      throw new Exception("Solve() has not yet been called")
    if (precedenceGraph.idbs.contains(rId))
      storageManager.getKnownIDBResult(rId)
    else
      storageManager.getEDBResult(rId)
  }

  def get(name: String): Set[Seq[StorageTerm]] = {
    get(storageManager.ns(name))
  }

  def insertIDB(rId: Int, ruleSeq: Seq[Atom]): Unit = {
    precedenceGraph.addNode(ruleSeq)
//    println(s"adding IDB ${storageManager.printer.ruleToString(ruleSeq)}")

    var rule = ruleSeq
    var k = JoinIndexes(rule, None)
    storageManager.allRulesAllIndexes.getOrElseUpdate(rId, mutable.Map[String, JoinIndexes]()).addOne(k.hash, k)
    storageManager.addConstantsToDomain(k.constIndexes.values.toSeq)

    val allLocs = k.varIndexes.flatten ++ k.constIndexes.keys
    if (allLocs.nonEmpty) {
      val relationCands = mutable.Map[RelationId, mutable.BitSet]()
      var start = 0
      for atom <- ruleSeq.drop(1) do {
        for i <- atom.terms.indices do {
          if (allLocs.contains(start + i))
            relationCands.getOrElseUpdate(atom.rId, mutable.BitSet()).addOne(i)
        }
        start += atom.terms.size
      }
      if relationCands.nonEmpty then storageManager.registerIndexCandidates(relationCands) // add at once to deduplicate ahead of time and avoid repeated calls
    }
    ruleSeq.foreach(r => storageManager.registerRelationSchema(r.rId, r.terms))

    if (rule.length <= heuristics.max_length_cache)
      val allK = JoinIndexes.allOrders(rule)
      storageManager.allRulesAllIndexes(rId) ++= allK

    if (defaultJITOptions.sortOrder == SortOrder.Sel) // sort before inserting, just in case EDBs are defined
      val (sortedBody, newHash) = JoinIndexes.presortSelect( // use preSort bc no child nodes to rearrange
        (a, _) =>
          if (storageManager.edbContains(a.rId))
            (true, storageManager.getEDBResult(a.rId).size)
          else
            (true, Int.MaxValue),
        k,
        storageManager,
        -1
      )
      rule = rule.head +: sortedBody.map(_._1)
      k = JoinIndexes(rule, Some(k.cxns))
      storageManager.allRulesAllIndexes(rId).addOne(k.hash, k)
    else if (defaultJITOptions.sortOrder == SortOrder.Badluck) // mimic "bad luck" program definition, so ingest rules in a bad order and then don't update them.
      val (sortedBody, newHash) = JoinIndexes.presortSelectWorst(
        (a, _) =>
          if (storageManager.edbContains(a.rId))
            (true, storageManager.getEDBResult(a.rId).size)
          else
            (true, Int.MaxValue),
        k,
        storageManager,
        -1
      )
      rule = rule.head +: sortedBody.map(_._1)
      k = JoinIndexes(rule, Some(k.cxns))
      storageManager.allRulesAllIndexes(rId).addOne(k.hash, k)

    //    println(s"${storageManager.printer.ruleToString(rule)}")

    val allRules = ast.rules.getOrElseUpdate(rId, AllRulesNode(mutable.ArrayBuffer.empty, rId)).asInstanceOf[AllRulesNode]
    allRules.rules.append(
      RuleNode(
        LogicAtom(
          rule.head.rId,
          rule.head.terms.map {
            case x: Variable => VarTerm(x)
            case x: Constant => ConstTerm(x)
          },
          rule.head.negated
        ),
        rule.drop(1).map(b =>
          LogicAtom(b.rId, b.terms.map {
            case x: Variable => VarTerm(x)
            case x: Constant => ConstTerm(x)
          }, b.negated)
        ),
        rule,
        k
      ))
  }

  def insertEDB(rule: StorageAtom): Unit = {
    storageManager.insertEDB(rule)
    val allRules = ast.rules.getOrElseUpdate(rule.rId, AllRulesNode(mutable.ArrayBuffer.empty, rule.rId)).asInstanceOf[AllRulesNode]
    allRules.edb = true
  }

  // Separate out for easier benchmarking tree stuff vs. compilation
  def solveCompiled(irTree: IROp[Any], ctx: InterpreterContext): Set[Seq[StorageTerm]] = {
    val compiled = compiler.compile(irTree)
    compiled(storageManager)
    storageManager.getNewIDBResult(ctx.toSolve)
  }

  def solveInterpreted[T](irTree: IROp[Any], ctx: InterpreterContext):  Set[Seq[StorageTerm]] = {
    debug("", () => "interpret-only mode")
    irTree.run(storageManager)
    storageManager.getNewIDBResult(ctx.toSolve)
  }

  def solveJIT(irTree: IROp[Any], ctx: InterpreterContext)(using jitOptions: JITOptions): Set[Seq[StorageTerm]] = {
    debug("", () => s"JIT with options $jitOptions")
    val executionContext = if (jitOptions.compileSync == CompileSync.Async && !jitOptions.useGlobalContext)
//      threadpool = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors())
      threadpool = new ForkJoinPool(Runtime.getRuntime.availableProcessors)
      ExecutionContext.fromExecutor(threadpool)
    else
      scala.concurrent.ExecutionContext.global

    given ExecutionContext = executionContext

    jit(irTree)
    storageManager.getNewIDBResult(ctx.toSolve)
  }

  inline def checkResult[T](value: Future[StorageManager => T], op: IROp[T], default: () => T)(using jitOptions: JITOptions): T = {
    value.value match {
      case Some(Success(run)) =>
        debug(s"Compilation succeeded: ${op.code}", () => "")
        stragglers.remove(op.compiledFn.hashCode()) // TODO: might not work, but jsut end up waiting for completed future
        run(storageManager)
      case Some(Failure(e)) =>
        stragglers.remove(op.compiledFn.hashCode())
        throw Exception(s"Error compiling ${op.code} with: $e")
      case None =>
        if (jitOptions.compileSync == CompileSync.Blocking)
          debug(s"${op.code} compilation not ready yet, so blocking", () => "")
          val res = Await.result(op.compiledFn, Duration.Inf)(storageManager)
          stragglers.remove(op.compiledFn.hashCode())
          res
        else
          debug(s"${op.code} compilation not ready yet, so defaulting", () => "")
          default()
    }
  }

  /**
   * For loop operations can compile asynchronously and then pick up compiled code at the current loop iteration.
   */
  inline def compileAsync(op: (UnionOp | UnionSPJOp), ec: ExecutionContext)(using jitOptions: JITOptions): EDB = {
    // threadpool = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors())
    // threadpool = new ForkJoinPool(Runtime.getRuntime.availableProcessors)
    // val executionContext = ExecutionContext.fromExecutor(threadpool)
    // given scala.concurrent.ExecutionContext = executionContext // scala.concurrent.ExecutionContext.global

    given ExecutionContext = ec

    val future = Future {
      compiler.compileIndexed(op)
    }

//    Thread.sleep(1000)
    storageManager.union(op.children.zipWithIndex.map((c, i) =>
      future.value match {
        case Some(Success(run)) =>
          debug(s"Compilation succeeded: ${op.code}", () => "")
          run(storageManager, i)
        case Some(Failure(e)) =>
          throw Exception(s"Error compiling ${op.code} with: ${e.getCause}")
        case None =>
          debug(s"${op.code} subsection compilation not ready yet, so defaulting", () => "")
          c.run(storageManager)
      }
    ))
//    val future = ec.submit(() => compiler.compileIndexed(op))
//    Thread.sleep(1000)
//    storageManager.union(op.children.zipWithIndex.map((c, i) =>
//      if (future.isDone)
//        try {
//          val run = future.get()
//          //                println(s"Compilation succeeded: ${op.code}")
//          run(storageManager, i)
//        } catch {
//          case e: Throwable => throw new Exception(s"Error compiling ${op.code} with: $e")
//        }
//      else
//      println(s"${op.code} subsection compilation not ready yet, so defaulting to interpreter")
//      if (i >= op.children.length - 1)
//        future.cancel(true)
//        c.run(storageManager)
//    ))
  }

  def jit[T](irTree: IROp[T])(using jitOptions: JITOptions)(using ec: ExecutionContext): T = {
//    debug("", () => s"IN STAGED JIT IR, code=${irTree.code}, gran=${jitOptions.granularity}")
    irTree match {
      case op: ProgramOp =>
        op.run_continuation(storageManager, Seq(sm => jit(op.children.head)))

      case op: DoWhileOp =>
       op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: SequenceOp if irTree.code == OpCode.EVAL_SN =>
        // TODO: inspect delta known and recompile conditionally only if the deltas have changed enough
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: SequenceOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: SwapAndClearOp =>
        op.run(storageManager)

      case op: InsertDeltaNewIntoDerived =>
        op.run(storageManager)

      case op: ResetDeltaOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o.asInstanceOf[IROp[EDB]])))

      case op: DebugNode =>
        op.run(storageManager)

      case op: UnionSPJOp if jitOptions.granularity.flag == op.code && op.children.length > 2=>
        if (jitOptions.sortOrder != SortOrder.Unordered && jitOptions.sortOrder != SortOrder.Badluck && jitOptions.fuzzy != 0) // sort child relations and see if change is above threshold
          val (nb, _) = JoinIndexes.presortSelect(jitOptions.getSortFn(storageManager), op.k, storageManager, -1)
          val oldBody = op.k.atoms.drop(1).map(_.hash)
          val newBodyIdx = nb.map(h => oldBody.indexOf(h._1.hash))

          def levenstein(s1: String, s2: String): Int = { // from wikipedia
            val memorizedCosts = mutable.Map[(Int, Int), Int]()

            def lev: ((Int, Int)) => Int = {
              case (k1, k2) =>
                memorizedCosts.getOrElseUpdate((k1, k2), (k1, k2) match {
                  case (i, 0) => i
                  case (0, j) => j
                  case (i, j) =>
                    Seq(1 + lev((i - 1, j)),
                      1 + lev((i, j - 1)),
                      lev((i - 1, j - 1))
                        + (if (s1(i - 1) != s2(j - 1)) 1 else 0)).min
                })
            }

            lev((s1.length, s2.length))
          }

          val distance = levenstein(newBodyIdx.mkString("", "", ""), Array.range(0, newBodyIdx.length).mkString("", "", ""))
          if (distance < jitOptions.fuzzy)
            return if (op.blockingCompiledFn != null)
              op.blockingCompiledFn(storageManager)
            else
              op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

        if (jitOptions.compileSync == CompileSync.Blocking) {
          op.blockingCompiledFn = compiler.compile(op)
          op.blockingCompiledFn(storageManager)
        } else {
          compileAsync(op, ec)
        }

      case op: UnionOp if jitOptions.granularity.flag == op.code && op.children.length > 2 =>
        if (jitOptions.compileSync == CompileSync.Blocking) {
          op.blockingCompiledFn = compiler.compile(op)
          op.blockingCompiledFn(storageManager)
        } else {
          compileAsync(op, ec)
        }
      case op: ProjectJoinFilterOp if jitOptions.granularity.flag == op.code  && op.children.length > 2 =>
        op.blockingCompiledFn = compiler.compile(op)
        op.blockingCompiledFn(storageManager)

      case op: ScanOp =>
        op.run(storageManager)

      case op: ScanEDBOp =>
        op.run(storageManager)

      case op: ComplementOp =>
        op.run(storageManager)

      case op: ProjectJoinFilterOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: UnionOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: UnionSPJOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: DiffOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case op: DebugPeek =>
        op.run_continuation(storageManager, op.children.map(o => (sm: StorageManager) => jit(o)))

      case _ => throw new Exception(s"Error: JIT-ing unknown operator ${irTree.code}")
    }
  }

  def waitForStragglers(): Unit = {
    debug("", () => s"cleaning up ${stragglers.values.size} stragglers")
    stragglers.values.foreach(t =>
      try {
        Await.result(t, Duration.Inf)
      } catch {
        case e => throw new Exception(s"Exception cleaning up compiler: ${e.getCause}")
      }
    )
    stragglers.clear()
  }

  override def solve(rId: Int): Set[Seq[StorageTerm]] = {
    given JITOptions = defaultJITOptions
//    println(s"jit opts==${defaultJITOptions.toBenchmark}")
    debug("", () => s"solve $rId with options $defaultJITOptions")
    // verify setup
    storageManager.verifyEDBs(precedenceGraph.idbs)
    if (storageManager.edbContains(rId) && !precedenceGraph.idbs.contains(rId)) { // if just an edb predicate then return
      debug("Returning EDB without any IDB rule: ", () => storageManager.ns(rId))
      return storageManager.getEDBResult(rId)
    }
    if (!precedenceGraph.idbs.contains(rId)) {
      throw new Exception("Solving for rule without body")
    }

    // generate and transform tree
    val transformedAST = transforms.foldLeft(ast: ASTNode)((t, pass) => pass.transform(t)(using storageManager))
    storageManager.updateAliases(tCtx.aliases)
    var toSolve = rId
    if (tCtx.aliases.contains(rId)) {
      toSolve = tCtx.aliases.getOrElse(rId, rId)
      debug("aliased:", () => s"${storageManager.ns(rId)} => ${storageManager.ns(toSolve)}")
      if (storageManager.edbContains(toSolve) && !precedenceGraph.idbs.contains(toSolve)) { // if just an edb predicate then return
        debug("Returning EDB as IDB aliased to EDB: ", () => storageManager.ns(toSolve))
        return storageManager.getEDBResult(toSolve)
      }
    }

    given irCtx: InterpreterContext = InterpreterContext(storageManager, precedenceGraph, toSolve)
    debug("AST: ", () => storageManager.printer.printAST(ast))
    debug("TRANSFORMED: ", () => storageManager.printer.printAST(transformedAST))
    debug("PG: ", () => precedenceGraph.toString())

    val irTree = createIR(transformedAST)

    debug("IRTree: ", () => storageManager.printer.printIR(irTree))
//    println(s"INIT STORAGE: ${storageManager.toString}")
    defaultJITOptions.mode match
      case Mode.Interpreted => solveInterpreted(irTree, irCtx)
      case Mode.Compiled => solveCompiled(irTree, irCtx)
      case Mode.JIT => solveJIT(irTree, irCtx)
  }
}
class NaiveStagedExecutionEngine(storageManager: StorageManager, defaultJITOptions: JITOptions = JITOptions(mode = Mode.Interpreted)) extends StagedExecutionEngine(storageManager, defaultJITOptions) {
  override def createIR(ast: ASTNode)(using InterpreterContext): IROp[Any] = IRTreeGenerator().generateTopLevelProgram(ast, naive=true)
}
