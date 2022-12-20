package graphs

import datalog.dsl.{Constant, Program, Relation, Term}

import scala.collection.mutable
import java.nio.file.{Files, Path, Paths}
import scala.io.Source
import scala.jdk.StreamConverters.*
//import scala.quoted.*
//import scala.quoted.staging.*

abstract trait TestIDB {
  def run(program: Program): Unit
  val skip: Seq[String] = Seq()// skip tests, usually for ones that will run out of memory for naive
}

case class EDBFromFile(program: Program, directory: Path) extends TestGraph {
  val description: String = directory.getFileName.toString
  val queries: mutable.Map[String, Query] = mutable.Map[String, Query]()

  // import EDBs
  private val factdir = Paths.get(directory.toString, "facts")
  if (Files.exists(factdir))
    Files.walk(factdir, 1)
      .filter(p => Files.isRegularFile(p))
      .forEach(f => {
        val edbName = f.getFileName.toString.replaceFirst("[.][^.]+$", "")
        val fact = program.relation[Constant](edbName)

        val reader = Files.newBufferedReader(f)
        val headers = reader.readLine().split("\t")
        reader.lines()
          .forEach(l =>
            val factInput = l
              .split("\t")
              .zipWithIndex.map((s, i) =>
                (headers(i) match {
                  case "Int" => s.toInt
                  case "String" => s
                  case _ => throw new Error(s"Unknown type ${headers(i)}")
                }).asInstanceOf[Term]
              )
            if (factInput.size != headers.size)
              throw new Error(s"Input data for fact of length ${factInput.size} but should be ${headers.mkString("[", ", ", "]")}. Line='$l'")
            fact(factInput: _*) :- ())
        reader.close()

        program.ee.storageManager.edbs.getOrElseUpdate(fact.id, program.ee.storageManager.EDB()) // TODO: handle empty collections better
      })

  // define IDBs
  private val classz = this.getClass.getClassLoader.loadClass(s"graphs.$description")
  private val constr = classz.getConstructor()
  private val idbProgram = constr.newInstance().asInstanceOf[TestIDB]

  idbProgram.run(program)
  override val skip: Seq[String] = idbProgram.skip

  // Generate queries
  private val expDir = Paths.get(directory.toString, "expected")
  if (!Files.exists(expDir)) throw new Exception(s"Missing expected directory '$expDir'")
  Files.walk(expDir, 1)
    .filter(p => Files.isRegularFile(p) && p.toString.endsWith(".csv"))
    .forEach(f => {
      val rule = f.getFileName.toString.replaceFirst("[.][^.]+$", "")
      val reader = Files.newBufferedReader(f)
      val headers = reader.readLine().split("\t")
      val res = reader.lines()
        .map(l => l.split("\t").zipWithIndex.map((s, i) =>
          (headers(i) match {
            case "Int" => s.toInt
            case "String" => s
            case _ => throw new Error(s"Unknown type ${headers(i)}")
          }).asInstanceOf[Term]
        ).toSeq)
        .toScala(Set)
      queries(rule) = Query(
        rule,
        program.namedRelation[Constant](rule),
        res
      )
      reader.close()
    })
}
