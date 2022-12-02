package graphs

import datalog.dsl.{Constant, Program, Relation, Term}

import scala.collection.mutable
import java.nio.file.{Files, Path, Paths}
import scala.io.Source
import scala.jdk.StreamConverters._


case class EDBFromFile(program: Program, directory: Path) extends TestGraph {
  val description = directory.getFileName.toString
  val queries: mutable.Map[String, Query] = mutable.Map[String, Query]()

  // import EDBs
  val factdir = Paths.get(directory.toString, "facts")
  println("EDBFromFile class constructor, file=" + directory.getFileName)
  Files.walk(factdir, 1)
    .filter(p => Files.isRegularFile(p))
    .forEach(f => {
      val edbName = f.getFileName.toString.replaceFirst("[.][^.]+$", "")
      val fact = program.relation[Constant](edbName)

      val reader = Files.newBufferedReader(f)
      var line: String = null
      while ({line = reader.readLine(); line != null}) {
        fact(line.split("\t"): _*) :- ()
      }
      reader.close()
    })

//  idbs(program) // generate IDBs, in theory could do this also by parsing
  val edge = program.namedRelation("edge")
  val isBefore = program.relation[Constant]("is_before")
  val isAfter = program.relation[Constant]("is_after")
  val x, y, z = program.variable()

  isBefore(x, y) :- edge(x, y)
  isBefore(x, y) :- (isBefore(x, z), isBefore(z, y))

  isAfter(x, y) :- edge(y, x)
  isAfter(x, y) :- (isAfter(z, x), isAfter(y, z))

  // Generate queries
  Files.walk(directory, 1)
    .filter(p => Files.isRegularFile(p) && p.toString.endsWith(".csv"))
    .forEach(f => {
      val rule = f.getFileName.toString.replaceFirst("[.][^.]+$", "")
      val reader = Files.newBufferedReader(f)
      val res = reader.lines()
        .map(l => l.split("\t").map(s => s.asInstanceOf[Term]).toSeq)
        .toScala(Set)
      queries(rule) = Query(
        rule,
        program.namedRelation[Constant](rule),
        res
      )
      reader.close()
    })
}
