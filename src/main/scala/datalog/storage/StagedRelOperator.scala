package datalog.storage

import scala.collection.mutable.{ArrayBuffer, Map}
import datalog.dsl.{Constant, Relation}

import scala.collection.mutable
import scala.quoted.*

class StagedRelOperators[S <: StorageManager](val storageManager: S) {
  type edbRow = storageManager.Row[storageManager.StorageTerm]
//  type table[T] = storageManager.Table[T]
  trait RelNode {
    val parent: RelNode
    val children: List[RelNode]
//    def generateCode()(using Quotes): Expr[storageManager.Relation[storageManager.StorageTerm]]
  }
  case class Scan(parent: RelNode, children: List[RelNode],
                  relation: storageManager.Relation[storageManager.StorageTerm],
                  rId: Int) extends RelNode {
    def generateCode()(using Quotes): Any = { //Expr[storageManager.Relation[storageManager.StorageTerm]] = {
      // TODO: lift seq
//      val r = Array.from(relation.map(r => r.toSeq))
//      Expr(r)
    }
  }

  case class Filter(parent: RelNode, children: List[RelNode],
                    cond: edbRow => Boolean) extends RelNode {
  }
  case class Project(parent: RelNode, children: List[RelNode], ixs: Seq[Int]) extends RelNode {

  }
  case class Join(parent: RelNode, children: List[RelNode],
                  variables: IndexedSeq[IndexedSeq[Int]],
                  constants: Map[Int, Constant]) extends RelNode {

  }
  case class Union(parent: RelNode, children: List[RelNode]) extends RelNode {

  }
  def generatePlan(root: RelNode)(using Quotes) = {
//    root match {
//      case scan: Scan => scan.generateCode()
//    }
  }
}