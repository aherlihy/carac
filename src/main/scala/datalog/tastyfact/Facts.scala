/* Author: Benoit Maillard, adapted from https://github.com/benoitmaillard/tasty-carac */
package datalog.tastyfact

import tastyquery.Symbols.TermSymbol
import datalog.tastyfact.Symbols.SymbolId

import java.io.{BufferedWriter, FileWriter}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths}
import scala.util.{Failure, Success, Try, Using}


object Facts {
  type Variable = SymbolId
  type Heap = String // allocation site
  type Method = SymbolId
  type Signature = SymbolId
  type Field = String
  type Instruction = String
  type Type = SymbolId
  type Index = String

  abstract class Fact extends Product

  // val v = new ... (heap is the allocation site, inMeth is the method)
  case class Alloc(varr: Variable, heap: String, inMeth: Method) extends Fact

  // val to = from (from is a variable)
  case class Move(to: Variable, from: Variable) extends Fact

  // to := base.fld
  case class Load(to: Variable, base: Variable, fld: Variable, inMeth: Method) extends Fact

  // base.fld = from
  case class Store(base: Variable, fld: Variable, from: Variable) extends Fact

  // base.sig(...) at #invo inside inMeth
  case class VCall(
      base: Variable,
      sig: Signature,
      invo: Instruction,
      inMeth: Method
  ) extends Fact

  // def meth(..., arg, ...) where arg is the n-th argument
  case class FormalArg(meth: Method, arglist: Index, n: Index, arg: Variable) extends Fact

  // meth(..., arg, ...) #invo where arg is the n-th argument
  case class ActualArg(invo: Instruction, arglist: Index, n: Index, arg: Variable) extends Fact

  // meth returns variable arg at the end of its body
  case class FormalReturn(meth: Method, arg: Variable) extends Fact

  // val varr = somemethod(...) at #invo (there must be a matching vcall)
  case class ActualReturn(invo: Instruction, varr: Variable) extends Fact

  // indicates the full path of this in meth
  case class ThisVar(meth: Method, thiss: Variable) extends Fact

  // allocation site heap has type typee
  case class HeapType(heap: Heap, typee: Type) extends Fact

  // link between a method signature and an actual method definition
  case class LookUp(typee: Type, sig: Signature, meth: Method) extends Fact

  // final result
  case class VarPointsTo(varr: Variable, heap: Heap) extends Fact

  // instruction #invo calls meth
  case class CallGraph(invo: Instruction, meth: Method) extends Fact

  // baseH.fld points to heap
  case class FieldPointsTo(baseH: Heap, fld: Field, heap: Heap) extends Fact

  // to gets assigned from (variable from another method via args)
  case class InterProcAssign(to: Variable, from: Variable) extends Fact

  // meth is reachable (we should always have Reachable(@main))
  case class Reachable(meth: Method) extends Fact

  case class StaticCall(meth: Method, invo: Instruction, inMeth: Method) extends Fact

  case class SuperCall(meth: Method, invo: Instruction, inMeth: Method) extends Fact

  case class FieldValDef(fld: Variable, from: Variable) extends Fact

  case class DefinesWith(typee: Type, parent: Variable, meth: Variable) extends Fact
  case class NotDefines(typee: Type, parent: Variable) extends Fact
  case class Extends(typeA: Type, typeB: Type) extends Fact

  def exportFacts(facts: Seq[Fact], output: Path): Try[Unit] =
    facts
      .groupBy(_.productPrefix)
      .foldLeft[Try[Unit]](Success(()))((res, cur) =>
        res.flatMap(_ =>
          exportRelation(output.resolve(f"${cur._1}.csv"), cur._2)
        )
      )

  private def exportRelation(path: Path, facts: Seq[Fact]): Try[Unit] =
    Using(Files.newBufferedWriter(path)) { writer =>
      writer.write(facts.head.productIterator.map(_ => "String").mkString("\t"))
      writer.write("\n")

      for (f <- facts) {
        writer.write(f.productIterator.mkString("\t"))
        writer.write("\n")
      }
    }
}
