package datalog.storage

import scala.collection.mutable.{ArrayBuffer, Map}
import datalog.dsl.Constant

// Indicates the end of the stream
final val NilTuple: Option[Nothing] = None

trait RelOperator {
  type edbRow = StorageManager#Row[StorageManager#StorageTerm]
  def open(): Unit
  def next(): Option[edbRow]
  def close(): Unit

  def toList(): ArrayBuffer[edbRow] = { // TODO: fix this to use iterator override
    val list = ArrayBuffer[edbRow]()
    this.open()
    while(
      this.next() match {
        case Some(r) => { list.addOne(r); true }
        case _ => false
      }) {}
    this.close()
    list
  }
//  final override def iterator: Iterator[Row] =
//    new Iterator[Row] with AutoCloseable {
//      private val op =
//        RelOperator.this.clone().asInstanceOf[RelOperator]
//      op.open()
//
//      var n: Option[Option[Row]] = Option.empty
//
//      def prepareNext(): Unit = {
//        if (n.nonEmpty) return
//          n = Option(op.next())
//      }
//
//      override def hasNext: Boolean = {
//        prepareNext()
//        n.get.nonEmpty
//      }
//
//      override def next(): Row = {
//        prepareNext()
//        val ret = n.get
//        assert(ret.nonEmpty)
//        n = Option.empty
//        ret.get
//      }
//
//      override def close(): Unit = {
//        op.close()
//      }
//    }
}

// TODO: set op per relation?
case class Scan(rId: Int)(using storageManager: StorageManager) extends RelOperator {
  private var currentId: Int = 0
  private var length: Long = storageManager.edb(rId).size
  def open(): Unit = {}
  def next(): Option[edbRow] = {
    if (currentId >= length) {
      NilTuple
    } else {
      currentId = currentId + 1
      //val x: StorageManager#StorageTerm = ??? : storageManager.StorageTerm
      ???
      //Option[storageManager.Row[StorageManager#StorageTerm]](storageManager.edb(rId)(currentId - 1))
    }
  }
  def close(): Unit = {}
}

// TODO: most likely will combine Scan+Filter, or split out Join+Filter
case class Filter(input: RelOperator)
//                 (using val storageManager: StorageManager)
                 //(cond: storageManager.Row[storageManager.StorageTerm] => Boolean) extends RelOperator {
                 (cond: StorageManager#Row[StorageManager#StorageTerm] => Boolean) extends RelOperator {
  def open(): Unit = input.open()
  override def next(): Option[edbRow] = {
    var nextTuple = input.next()
    while (nextTuple match {
      case Some(n) => !cond(n)
      case _ => false
    }) nextTuple = input.next()
    nextTuple
  }
  def close(): Unit = input.close()
}

case class Project(input: RelOperator, ixs: Seq[Int]) extends RelOperator {
  def open(): Unit = input.open()
  override def next(): Option[edbRow] = {
    input.next() match {
      case Some(t) => Some(t.zipWithIndex.filter{ case (e, i) => ixs.contains(i) }.map(_._1))
      case _ => NilTuple
    }
  }
  def close(): Unit = input.close()
}

case class Join(left: RelOperator,
                right: RelOperator,
                variables: IndexedSeq[IndexedSeq[Int]],
                constants: Map[Int, Constant]
               )extends RelOperator {

  private var outputRelation: ArrayBuffer[edbRow] = ArrayBuffer()
  private var index = 0

  // TODO [NOW]: Figure out >2 key join
  private var varKeysL = variables.map(i => i(0))
  private var varKeysR = variables.map(i => i(1))

  def open(): Unit = {
    index = 0
    outputRelation = ArrayBuffer()

    // Nested loop join:
    var outerTable: ArrayBuffer[edbRow] = left.toList()
    var innerTable = right.toList()

    if (outerTable.isEmpty) {
      innerTable.foreach(innerTuple => {
        // TODO: handle constants, i.e. check here that tuples returned match the constant reqs
        if (variables.isEmpty)
        outputRelation.addOne(innerTuple)
      })
    }

    outerTable.foreach(outerTuple => {
      val outerValues = varKeysL.map(k => outerTuple(k)) // TODO [NOW]: handle constants
      innerTable.foreach(innerTuple => {
        val innerValues = varKeysR.map(k => innerTuple(k))
        if (outerValues.equals(innerValues)) { // TODO: handle free variables
          val joined = outerTuple ++ innerTuple
          outputRelation.addOne(joined)
        }
      })
    })
  }

  def next(): Option[edbRow] = {
    if (index >= outputRelation.length)
      NilTuple
    else {
      index += 1
      Option(outputRelation(index - 1))
    }
  }

  def close(): Unit = {
    left.close()
    right.close()
  }
}