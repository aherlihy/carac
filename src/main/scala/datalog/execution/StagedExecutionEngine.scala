package datalog.execution

import datalog.dsl.{Atom, Constant, Term, Variable}
import datalog.execution.ast.*
import datalog.execution.ast.transform.{ASTTransformerContext, CopyEliminationPass, JoinIndexPass, Transformer}
import datalog.execution.ir.*
import datalog.storage.{CollectionsStorageManager, DB, KNOWLEDGE, StorageManager}
import datalog.tools.Debug.debug

import scala.collection.mutable
import scala.collection.immutable
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, blocking}
import scala.util.{Failure, Success}
import scala.quoted.*

case class JITOptions(granularity: OpCode = OpCode.PROGRAM, dotty: staging.Compiler = staging.Compiler.make(getClass.getClassLoader), aot: Boolean = true, block: Boolean = true, thresholdNum: Int = 0, thresholdVal: Float = 2) {
//  if ((granularity == OpCode.OTHER || granularity == OpCode.PROGRAM) && (!aot || !block))
//    throw new Exception(s"Invalid JIT options: with $granularity, aot and block must be true: $aot, $block")
  private val unique = Seq(OpCode.DOWHILE, OpCode.EVAL_NAIVE, OpCode.LOOP_BODY)
  if (!aot && !block && unique.contains(granularity))
    throw new Exception(s"Cannot online, async compile singleton IR nodes: $granularity (theres no point)")
}

class StagedExecutionEngine(val storageManager: CollectionsStorageManager, defaultJITOptions: JITOptions = JITOptions()) extends ExecutionEngine {
  import storageManager.EDB
  val precedenceGraph = new PrecedenceGraph(using storageManager.ns)
  val prebuiltOpKeys: mutable.Map[Int, mutable.ArrayBuffer[JoinIndexes]] = mutable.Map[Int, mutable.ArrayBuffer[JoinIndexes]]() // TODO: currently unused, mb remove from EE
  val ast: ProgramNode = ProgramNode()
  private var knownDbId = -1
  private val tCtx = ASTTransformerContext(using precedenceGraph)
  val transforms: Seq[Transformer] = Seq(/*CopyEliminationPass(using tCtx), JoinIndexPass(using tCtx)*/)
  val compiler: StagedCompiler = StagedCompiler(storageManager)
  val dedicatedDotty: staging.Compiler = defaultJITOptions.dotty
  var stragglers: mutable.WeakHashMap[Int, Future[CompiledFn]] = mutable.WeakHashMap.empty // should be ok since we are only removing by ref and then iterating on values only?

  def createIR(ast: ASTNode)(using InterpreterContext): IROp[Any] = IRTreeGenerator().generateSemiNaive(ast)

  def initRelation(rId: Int, name: String): Unit = {
    storageManager.ns(rId) = name
    storageManager.initRelation(rId, name)
  }

  def get(rId: Int): Set[Seq[Term]] = {
    if (storageManager.knownDbId == -1)
      throw new Exception("Solve() has not yet been called")
    if (precedenceGraph.idbs.contains(rId))
      storageManager.getKnownIDBResult(rId)
    else
      storageManager.getEDBResult(rId)
  }

  def get(name: String): Set[Seq[Term]] = {
    get(storageManager.ns(name))
  }

  def insertIDB(rId: Int, ruleSeq: Seq[Atom]): Unit = {
    val rule = ruleSeq.toArray
    precedenceGraph.idbs.addOne(rId)
    val allRules = ast.rules.getOrElseUpdate(rId, AllRulesNode(mutable.ArrayBuffer.empty, rId)).asInstanceOf[AllRulesNode]
    // TODO: sort here in case EDBs/etc are already defined?
    val allK = JoinIndexes.allOrders(rule)
    storageManager.allRulesAllIndexes.getOrElseUpdate(rId, mutable.Map[String, JoinIndexes]()) ++= allK
    val hash = JoinIndexes.getRuleHash(rule)
    precedenceGraph.addNode(rId, allK(hash).deps)

    allRules.rules.append(
      RuleNode(
        LogicAtom(
          rule.head.rId,
          rule.head.terms.map {
            case x: Variable => VarTerm(x)
            case x: Constant => ConstTerm(x)
          }),
        rule.drop(1).map(b =>
          LogicAtom(b.rId, b.terms.map {
            case x: Variable => VarTerm(x)
            case x: Constant => ConstTerm(x)
          })),
        rule,
        hash
      ))
  }

  def insertEDB(rule: Atom): Unit = {
    storageManager.insertEDB(rule)
    val allRules = ast.rules.getOrElseUpdate(rule.rId, AllRulesNode(mutable.ArrayBuffer.empty, rule.rId)).asInstanceOf[AllRulesNode]
    allRules.edb = true
  }

  // NOTE: this method is just for testing to see how much overhead tree processing has, not used irl.
  def generateProgramTree(rId: Int): (IROp[Any], InterpreterContext) = {
    // verify setup
    storageManager.verifyEDBs(precedenceGraph.idbs)
    if (storageManager.edbs.contains(rId) && !precedenceGraph.idbs.contains(rId)) { // if just an edb predicate then return
      debug("Returning EDB without any IDB rule: ", () => storageManager.ns(rId))
      throw new Exception("NOTE: using generateProgramTree which is only for benchmarking")
    }
    if (!precedenceGraph.idbs.contains(rId)) {
      throw new Exception("Solving for rule without body")
    }
    val transformedAST = transforms.foldLeft(ast: ASTNode)((t, pass) => pass.transform(t)(using storageManager))

    var toSolve = rId
    if (tCtx.aliases.contains(rId))
      toSolve = tCtx.aliases.getOrElse(rId, rId)
      if (storageManager.edbs.contains(toSolve) && !precedenceGraph.idbs.contains(toSolve)) { // if just an edb predicate then return
        throw new Exception("NOTE: using generateProgramTree which is only for benchmarking")
      }
    given irCtx: InterpreterContext = InterpreterContext(storageManager, precedenceGraph, toSolve)
    val irTree = createIR(transformedAST)
    (irTree, irCtx)
  }

  // Separate these out for easier benchmarking

  def preCompile(irTree: IROp[Any]): CompiledFn = {
    given staging.Compiler = dedicatedDotty
    compiler.getCompiled(irTree)
  }
  def solvePreCompiled(compiled: CompiledFn, ctx: InterpreterContext): Set[Seq[Term]] = {
    compiled(storageManager)
    storageManager.getNewIDBResult(ctx.toSolve)
  }

  def solveCompiled(irTree: IROp[Any], ctx: InterpreterContext): Set[Seq[Term]] = {
    debug("", () => "compile-only mode")
    given staging.Compiler = dedicatedDotty
    val compiled = compiler.getCompiled(irTree)
    compiled(storageManager)
    storageManager.getNewIDBResult(ctx.toSolve)
  }

  def solveInterpreted[T](irTree: IROp[Any], ctx: InterpreterContext):  Set[Seq[Term]] = {
    debug("", () => "interpret-only mode")
    irTree.run(storageManager)
    storageManager.getNewIDBResult(ctx.toSolve)
  }

  def solveJIT(irTree: IROp[Any], ctx: InterpreterContext)(using jitOptions: JITOptions): Set[Seq[Term]] = {
    debug("", () => s"JIT with options $jitOptions")
    jit(irTree)
    storageManager.getNewIDBResult(ctx.toSolve)
  }

  def jitRel(irTree: IROp[CollectionsStorageManager#EDB])(using jitOptions: JITOptions): CollectionsStorageManager#EDB = {
//    println(s"IN INTERPRET REL_IR, code=${irTree.code}")
    // If async compiling, then make a new dotty for nodes for which there are multiple. TODO: pool?
//    lazy val newDotty = dedicatedDotty//if (jitOptions.block) dedicatedDotty else staging.Compiler.make(getClass.getClassLoader)
    irTree match {
      case op: UnionSPJOp if jitOptions.granularity == op.code => // check if aot compile is ready
        startCompileThreadRel(op, dedicatedDotty)
        checkResult(op.compiledFn, op, () => op.run_continuation(storageManager, op.children.map(o => (sm: CollectionsStorageManager) => jitRel(o))))

      case op: ProjectJoinFilterOp if jitOptions.granularity == op.code => // check if aot compile is ready
        startCompileThreadRel(op, dedicatedDotty)
        checkResult(op.compiledFn, op, () => op.run_continuation(storageManager, op.children.map(o => (sm: CollectionsStorageManager) => jitRel(o))))
//        if (!jitOptions.block && op.compiledFn == null && !jitOptions.aot)
//          startCompileThreadRel(op, newDotty)
//        else if (jitOptions.block && op.blockingCompiledFn == null && !jitOptions.aot)
//          startCompileThreadRel(op, newDotty)
//        else
//          debug("", () => s"TV: ${jitOptions.thresholdVal}; TN: ${jitOptions.thresholdNum}::${op.children.sliding(2).map {
//              case Seq(x, y, _*) =>
//                val l = x.run(storageManager).size
//                val r = y.run(storageManager).size
//                if (l != 0  && r != 0) l.toFloat / r else 0
//              case _ => 0
//          }.mkString("(", ", ", ")")}")
//          val recompile = op.children.sliding(2).map{
//            case Seq(x, y, _*) =>
//              val l = x.run(storageManager).size
//              val r = y.run(storageManager).size
//              l != 0 && r != 0 && l.toFloat / r > jitOptions.thresholdVal
//            case _ => false
//          }.count(b => b) > jitOptions.thresholdNum
//          if (recompile)
//            startCompileThreadRel(op, newDotty)
//        checkResult(op.compiledFn, op, () => op.run_continuation(storageManager, op.children.map(o => (sm: CollectionsStorageManager) => jitRel(o))))
      case op: UnionOp if jitOptions.granularity == op.code =>
        startCompileThreadRel(op, dedicatedDotty)
        checkResult(op.compiledFn, op, () => op.run_continuation(storageManager, op.children.map(o => (sm: CollectionsStorageManager) => jitRel(o))))

      case op: ScanOp =>
        op.run(storageManager)

      case op: ScanEDBOp =>
        op.run(storageManager)

      case op: ProjectJoinFilterOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: CollectionsStorageManager) => jitRel(o)))

      case op: UnionOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: CollectionsStorageManager) => jitRel(o)))

      case op: UnionSPJOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: CollectionsStorageManager) => jitRel(o)))

      case op: DiffOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: CollectionsStorageManager) => jitRel(o)))

      case op: DebugPeek =>
        op.run_continuation(storageManager, op.children.map(o => (sm: CollectionsStorageManager) => jitRel(o)))
      case _ => throw new Exception("Error: interpretRelOp called with unit operation")
    }
  }

  inline def checkResult[T](value: Future[CollectionsStorageManager => T], op: IROp[T], default: () => T)(using jitOptions: JITOptions): T =
    value.value match {
      case Some(Success(run)) =>
        debug(s"Compilation succeeded: ${op.code}", () => "")
        stragglers.remove(op.compiledFn.hashCode()) // TODO: might not work, but jsut end up waiting for completed future
        run(storageManager)
      case Some(Failure(e)) =>
        stragglers.remove(op.compiledFn.hashCode())
        throw Exception(s"Error compiling ${op.code} with: ${e.getCause}")
      case None =>
        if (jitOptions.block)
          debug(s"${op.code} compilation not ready yet, so blocking", () => "")
          val res = Await.result(op.compiledFn, Duration.Inf)(storageManager)
          stragglers.remove(op.compiledFn.hashCode())
          res
        else
          debug(s"${op.code} compilation not ready yet, so defaulting", () => "")
          default()
    }
  inline def startCompileThread(op: IROp[Any], dotty: staging.Compiler)(using jitOptions: JITOptions): Unit =
    debug(s"starting online compilation for code ${op.code}", () => "")
//    if (jitOptions.block)
//      given staging.Compiler = dotty
//      op.blockingCompiledFn = compiler.getCompiled(op)
//    else
    given scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
    op.compiledFn = Future {
      given staging.Compiler = dotty; // dedicatedDotty //staging.Compiler.make(getClass.getClassLoader) // TODO: new dotty per thread, maybe concat
      compiler.getCompiled(op)
    }
    stragglers.addOne(op.compiledFn.hashCode(), op.compiledFn)

  inline def startCompileThreadRel(op: IROp[CollectionsStorageManager#EDB], dotty: staging.Compiler)(using jitOptions: JITOptions): Unit =
//    if (jitOptions.block)
//      given staging.Compiler = dotty
//      op.blockingCompiledFn = compiler.getCompiledRel(op)
//    else
    given scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
    op.compiledFn = Future {
      given staging.Compiler = dotty; // dedicatedDotty //staging.Compiler.make(getClass.getClassLoader) // TODO: new dotty per thread, maybe concat
      compiler.getCompiledRel(op)
    }
    stragglers.addOne(op.compiledFn.hashCode(), op.compiledFn)


  def jit(irTree: IROp[Any])(using jitOptions: JITOptions): Any = {
    debug("", () => s"IN STAGED JIT IR, code=${irTree.code}, gran=${jitOptions.granularity}")
    irTree match {
      case op: ProgramOp =>
        op.run_continuation(storageManager, Seq(sm => jit(op.children.head)))

      case op: DoWhileOp =>
       op.run_continuation(storageManager, op.children.map(o => (sm: CollectionsStorageManager) => jit(o)))

      case op: SequenceOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: CollectionsStorageManager) => jit(o)))

      case op: SwapAndClearOp =>
        op.run(storageManager)

      case op: InsertOp =>
        op.run_continuation(storageManager, op.children.map(o => (sm: CollectionsStorageManager) => jitRel(o.asInstanceOf[IROp[CollectionsStorageManager#EDB]])))

      case op: DebugNode =>
        op.run(storageManager)

      case _ =>
        jitRel(irTree.asInstanceOf[IROp[CollectionsStorageManager#EDB]])
//        throw new Exception(s"Error: unhandled node type $irTree")
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

  override def solve(rId: Int, jitOptions: JITOptions = defaultJITOptions): Set[Seq[Term]] = {
    given JITOptions = jitOptions
//    println(s"sort opts=${storageManager.preSortAhead}, ${storageManager.sortAhead}, ${storageManager.sortOnline} & gran=${jitOptions.granularity}")
    debug("", () => s"solve $rId with options $jitOptions")
    // verify setup
    storageManager.verifyEDBs(precedenceGraph.idbs)
    if (storageManager.edbs.contains(rId) && !precedenceGraph.idbs.contains(rId)) { // if just an edb predicate then return
      debug("Returning EDB without any IDB rule: ", () => storageManager.ns(rId))
      return storageManager.getEDBResult(rId)
    }
    if (!precedenceGraph.idbs.contains(rId)) {
      throw new Exception("Solving for rule without body")
    }

    // generate and transform tree
    val transformedAST = transforms.foldLeft(ast: ASTNode)((t, pass) => pass.transform(t)(using storageManager))
    var toSolve = rId
    if (tCtx.aliases.contains(rId)) {
      toSolve = tCtx.aliases.getOrElse(rId, rId)
      debug("aliased:", () => s"${storageManager.ns(rId)} => ${storageManager.ns(toSolve)}")
      if (storageManager.edbs.contains(toSolve) && !precedenceGraph.idbs.contains(toSolve)) { // if just an edb predicate then return
        debug("Returning EDB as IDB aliased to EDB: ", () => storageManager.ns(toSolve))
        return storageManager.getEDBResult(toSolve)
      }
    }

    given irCtx: InterpreterContext = InterpreterContext(storageManager, precedenceGraph, toSolve)
    debug("AST: ", () => storageManager.printer.printAST(ast))
    debug("TRANSFORMED: ", () => storageManager.printer.printAST(transformedAST))
//    debug("PG: ", () => irCtx.sortedRelations.toString())

    val irTree = createIR(transformedAST)

    debug("IRTree: ", () => storageManager.printer.printIR(irTree))
    if (jitOptions.granularity == OpCode.OTHER) // i.e. never compile
      solveInterpreted(irTree, irCtx)
    else if (jitOptions.granularity == OpCode.PROGRAM && jitOptions.aot && jitOptions.block) // i.e. compile asap
      solveCompiled(irTree, irCtx)
    else
      solveJIT(irTree, irCtx)
  }
}
class NaiveStagedExecutionEngine(storageManager: CollectionsStorageManager, defaultJITOptions: JITOptions = JITOptions()) extends StagedExecutionEngine(storageManager, defaultJITOptions) {
  import storageManager.EDB
  override def createIR(ast: ASTNode)(using InterpreterContext): IROp[Any] = IRTreeGenerator().generateNaive(ast)
}
