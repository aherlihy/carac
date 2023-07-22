/* Author: Benoit Maillard, adapted from https://github.com/benoitmaillard/tasty-carac */
package datalog.tastyfact

import coursier.{Dependency, Fetch}
import coursier.core.{Module, ModuleName, Organization}
import datalog.dsl.Program
import datalog.execution.SemiNaiveExecutionEngine
import datalog.storage.DefaultStorageManager
import datalog.tastyfact.PointsTo
import datalog.tastyfact.rulesets.PointsToRuleSet

import java.nio.file.{FileSystems, Path}
import scala.annotation.meta.param
import scala.util.{Failure, Success}

import tastyquery.Classpaths.Classpath
import tastyquery.jdk.ClasspathLoaders
import tastyquery.Contexts
import tastyquery.Contexts.Context
import tastyquery.Contexts.ctx
import tastyquery.Names.*
import tastyquery.Symbols.ClassSymbol
import tastyquery.Symbols.ClassTypeParamSymbol
import tastyquery.Symbols.TermSymbol
import tastyquery.Trees.*

import coursier.Fetch
import coursier.Dependency
import coursier.core.Organization
import coursier.core.ModuleName
import coursier.core.Module

abstract class Test
abstract class T2 extends Test
case class T1() extends T2

object Main {
  case class Config(
      classPath: Option[Path] = None,
      output: Option[Path] = None,
      mainMethod: String = "Main.main",
      help: Boolean = false,
      print: Boolean = false,
      factsOnly: Boolean = false,
  )

  val usage = "Usage: tastycarac [-h] [-p] [-f] [-m main] [-o output] classpath"

  def main(args: Array[String]): Unit = {
    val argsList = args.toList
    val config = parseArgs(argsList)

    if config.help then println(usage)
    else {
      val facts = inspect(config.classPath.get, config.mainMethod)

      println(f"Generated ${facts.size} facts...")

      if config.print then for (f <- facts)
        println(f" - ${f.productPrefix}${f.productIterator.mkString("(", ", ", ")")}")

      if config.output.isDefined then
        Facts.exportFacts(facts, config.output.get) match {
          case Failure(e) =>
            println(f"Something went wrong while saving the facts: ${e}")
          case Success(v) =>
            println(
              f"Facts saved successfully in ${config.output.get.toAbsolutePath().toString()}"
            )
        
        }

      if !config.factsOnly then {
        val engine = SemiNaiveExecutionEngine(DefaultStorageManager())
        val program = Program(engine)
        val toSolve = PointsToRuleSet.defineRules(program)

        for (f <- facts) {
          program.namedRelation(f.productPrefix).apply(f.productIterator.map(_.toString).toSeq:_*) :- ()
        }

        toSolve.solve()
        
        val pointstoSets: Map[String, Set[String]] = program.namedRelation("VarPointsTo").get().map {
          case Seq(from: String, to: String) => (from, to)
        }.groupBy(_._1).mapValues(_.map(_._2)).toMap

        println(program.namedRelation("VarPointsTo").get().mkString("\n", "\n", "\n"))

        println("========================================================")
        println(f"Computed points-to sets for ${pointstoSets.size} variables")
        println("Inferred points-to sets:")

        for ((variable, set) <- pointstoSets)
          println(f"- ${variable} -> {${set.mkString(", ")}}")
      }
    }
  }

  def parseArgs(argsList: List[String], acc: Config = Config()): Config =
    argsList match {
      case ("-o" | "--output") :: value :: tail =>
        parseArgs(tail, acc.copy(output = Some(Path.of(value))))
      case ("-m" | "--main") :: value :: tail =>
        parseArgs(tail, acc.copy(mainMethod = value))
      case ("-h" | "--help") :: tail => Config(help = true)
      case ("-p" | "--print") :: tail =>
        parseArgs(tail, acc.copy(print = true))
      case ("-f" | "--factsonly") :: tail =>
        parseArgs(tail, acc.copy(factsOnly = true))
      case classPath :: tail =>
        parseArgs(tail, acc.copy(classPath = Some(Path.of(classPath))))
      case Nil => acc
    }

  def inspect(input: Path, mainMethod: String) = {
    val module = Dependency(Module(Organization("org.scala-lang"), ModuleName("scala3-library_3"), Map.empty), "3.3.1-RC4") // TODO: auto-set version

    val scalaStdLib = Fetch()
      .addDependencies(module)
      .run().map(_.toPath()).toList
    
    val javaStdLib = FileSystems.getFileSystem(java.net.URI.create("jrt:/")).getPath("modules", "java.base")

    val classpath = ClasspathLoaders.read(input :: javaStdLib :: scalaStdLib)
    given Context = Contexts.init(classpath)
    val myLibSyms = ctx.findSymbolsByClasspathEntry(classpath.entries.head)
    val trees = myLibSyms.collect { case cs: ClassSymbol => cs }
    PointsTo(trees).generateFacts(mainMethod)
  }
}
