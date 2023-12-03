package datalog
package execution

import datalog.dsl.*
import datalog.execution.JITOptions
import datalog.execution.ir.*
import datalog.storage.{DefaultStorageManager, StorageManager, RelationId}

import scala.quoted.*
import scala.compiletime.uninitialized

/** A program that specifies a relation `toSolve` to be solved. */
abstract class SolvableProgram(engine: ExecutionEngine) extends Program(engine) {
  /** The relation to be solved when running the program. */
  val toSolve: String
}

/**
 * A base class to pre-compile program during the Scala compiler compile-time.
 *
 * This API is a bit awkward to use since the Scala macro system requires us
 * to define the macro in a separate file from where it's used.
 * To pre-compile a program, first define it as a subclass of SolvableProgram:
 *
 *    class MyProgram(engine: ExecutionEngine) extends SolvableProgram(engine) {
 *      val edge = relation[Constant]("edge")
 *      // ...
 *      override val toSolve = edge
 *    }
 *
 * Then define an object to hold the pre-compiled lambda:
 *
 *    object MyMacroCompiler extends MacroCompiler(MyProgram(_)) {
 *       inline def compile(): StorageManager => Any = ${compileImpl()}
 *    }
 *
 * Then in a separate file, call the macro to cache its result:
 *
 *    val compiled = MyMacroCompiler.compile()
 *
 * You can then run the program, with extra facts loaded at runtime if desired:
 *
 *    MyMacroCompiler.runCompiled(compiled)(p => p.edge("b", "c") :- ())
 */
abstract class MacroCompiler[T <: SolvableProgram](val makeProgram: ExecutionEngine => T, jitOptions: JITOptions) {
  /** Generate an engine suitable for use with the output of `compile()`. */
  def makeEngine(): StagedExecutionEngine = {
    val storageManager = DefaultStorageManager()
    StagedExecutionEngine(storageManager, jitOptions)
  }
  private val engine: StagedExecutionEngine = makeEngine()
  private val program: T = makeProgram(engine)

  protected def compileImpl()(using Quotes): Expr[StorageManager => Any] = {
    engine.generateProgramTree(program.namedRelation(program.toSolve).id) match
      case Left(id) =>
        ' { (sm: StorageManager ) => sm.getEDBResult(${Expr(id)}) }
      case Right(irTree, irCtx) =>
        given InterpreterContext = irCtx
        // TODO: more precise type for engine.compiler to avoid the cast.
        val compiler = engine.compiler.asInstanceOf[QuoteCompiler]
        val x = '{ (sm: StorageManager) =>
          ${ compiler.compileIR(irTree)(using 'sm) }
        }
        // println(x.show)
        x
  }

  /**
   * Generate the macro-compiled program solver.
   *
   * Cache the result in a val to avoid running the macro multiple times,
   * then pass it to `runCompiled`.
   *
   * Subclasses should implement this by just calling `${compileImpl()}`.
   */
  inline def compile(): StorageManager => Any

  /**
   * Run a macro-compiled program solver with a fresh Program at runtime.
   *
   * @param compiled  The output of a call to `this.compile()`.
   * @param op        Operations to run on the fresh program, this
   *                  can be used to add extra facts at runtime.
   *                  TODO: Find a nice way to restrict this to only allow
   *                  adding extra facts and nothing else.
   */
  def runCompiled(compiled: StorageManager => Any)(op: T => Any): Set[Seq[Term]] = {
    val runtimeEngine = makeEngine()
    val runtimeProgram = makeProgram(runtimeEngine)

    // Even though we don't use the generated tree at runtime,
    // we still need to generate it to find the de-aliased irCtx.toSolve
    // and to populate runtimeEngine.storageManager.allRulesAllIndexes
    runtimeEngine.generateProgramTree(program.namedRelation(program.toSolve).id) match
      case Left(toSolve) =>
        runtimeEngine.storageManager.getEDBResult(toSolve)
      case Right(irTree, irCtx) =>
        op(runtimeProgram)

        compiled(runtimeEngine.storageManager)

        runtimeEngine.storageManager.getNewIDBResult(irCtx.toSolve)
  }
}
