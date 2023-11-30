package datalog.dsl

import datalog.execution.{ExecutionEngine, NaiveExecutionEngine}

import java.nio.file.{Files, Path}
import scala.collection.mutable

// TODO: better to have program as given instance?
class Program(engine: ExecutionEngine) extends AbstractProgram {
  given ee: ExecutionEngine = engine
  var varCounter = 0
  def variable(): Variable = {
    varCounter += 1
    Variable(varCounter - 1)
  }
  var relCounter = 0
  def relation[T <: Constant](userName: String = relCounter.toString): Relation[T] = {
    if (ee.storageManager.ns.contains(userName)) {
      throw new Exception("Named relation '" + userName + "' already exists")
    }
    relCounter += 1
    Relation[T](relCounter - 1, userName)
  }

  def namedRelation[T <: Constant](userName: String): Relation[T] = {
    if (!ee.storageManager.ns.contains(userName)) {
      throw new Exception("Named relation '" + userName + "' does not exist")
    }
    val rId = ee.storageManager.ns(userName)
    Relation[T](rId, userName)
  }

  // TODO: also provide solve for multiple/all predicates, or return table so users can query over the derived DB
  def solve(rId: Int): Set[Seq[Term]] = ee.solve(rId).map(s => s.toSeq).toSet

  def initializeEmptyFactsFromDir(directory: String): Unit = {
    val factdir = Path.of(directory)
    if (Files.exists(factdir)) {
      Files.walk(factdir, 1)
        .filter(p => Files.isRegularFile(p))
        .forEach(f => {
          val edbName = f.getFileName.toString.replaceFirst("[.][^.]+$", "")
          val fact = relation[Constant](edbName)
          ee.insertEmptyEDB(fact.id)
        })
    } else throw new Exception(s"Directory $factdir does not contain any facts")
  }

  def loadFromFactDir(directory: String): Unit = {
    val factdir = Path.of(directory)
    if (Files.exists(factdir)) {
      Files.walk(factdir, 1)
        .filter(p => Files.isRegularFile(p))
        .forEach(f => {
//          println(s"reading file $f")
          val edbName = f.getFileName.toString.replaceFirst("[.][^.]+$", "")
          val reader = Files.newBufferedReader(f)
          val firstLine = reader.readLine()
          if (firstLine != null) { // empty file, empty EDB
            val headers = firstLine.split("\t")
            val fact =
              if (ee.storageManager.ns.contains(edbName)) namedRelation[Constant](edbName)
              else relation[Constant](edbName)
            reader.lines()
              .forEach(l => {
                val factInput = l
                  .split("\t")
                  .zipWithIndex.map((s, i) =>
                  (headers(i) match {
                    case "Int" => s.toInt
                    case "String" => s
                    case _ => s // TODO: for now files without headers are assumed to be all strings. throw new Exception(s"Unknown type ${headers(i)}")
                  }).asInstanceOf[Term]
                ).toSeq
                if (factInput.length != headers.size)
                  throw new Exception(s"Input data for fact of length ${factInput.size} but should be ${headers.mkString("[", ", ", "]")}. Line='$l'")
                fact(factInput: _*) :- ()
              })
          }
          reader.close()
        })
    }/* else {
      println(s"Warning: Unable to find fact directory '$factdir'")
    }*/
  }
}
