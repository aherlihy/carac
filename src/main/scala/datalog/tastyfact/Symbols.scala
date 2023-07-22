/* Author: Benoit Maillard, adapted from https://github.com/benoitmaillard/tasty-carac */
package datalog.tastyfact

import scala.collection.mutable
import tastyquery.Trees.*
import tastyquery.Symbols.Symbol
import tastyquery.Names.Name

object Symbols {
  abstract class SymbolId

  case class UniqueSymbolId(path: List[Name], count: Int) extends SymbolId {
    override def toString(): String =
      if count > 0 then f"${path.mkString(".")}#$count"
      else path.mkString(".")
  }

  case class ThisSymbolId(meth: SymbolId) extends SymbolId {
    override def toString(): String = f"${meth.toString}.this" 
  }

  case object GlobalContext extends SymbolId {
    override def toString(): String = "$GLOBAL"
  }

  class Table {
    val duplicates = mutable.Map.empty[List[Name], Int]
    val symbolIds = mutable.Map.empty[Symbol, SymbolId]

    def getSymbolId(s: Symbol): SymbolId =
      symbolIds.get(s) match {
        case Some(value) => value
        case None =>
          val path = fullPath(s)
          val id = getSymbolIdFromPath(path)
          symbolIds.put(s, id)
          id
      }

    def getSymbolIdFromPath(path: List[Name]): SymbolId =
      val count = duplicates.updateWith(path)(o => o.map(_ + 1).orElse(Some(0))).get
      UniqueSymbolId(path, count)
  }

  def fullPath(s: Symbol): List[Name] = s.owner match {
    case owner: Symbol if !owner.isRoot => fullPath(owner) :+ s.name
    case _ => s.name :: Nil
  }
}
