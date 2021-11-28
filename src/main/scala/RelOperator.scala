package datalog

import scala.collection.mutable.{ArrayBuffer, Map}

// Indicates the end of the stream
final val NilTuple: Option[Nothing] = None

type Row2 = IndexedSeq[Any] // storageManager.EDBRow] TODO: get rid of Any

trait RelOperator {
  def open(): Unit
  def next(): Option[Row2]
  def close(): Unit

  def toList(): ArrayBuffer[Row2] = { // TODO: fix this to use iterator override
    val list = ArrayBuffer[Row2]()
    while(
      this.next() match {
        case Some(r) => { list.addOne(r); true }
        case _ => false
      }) {}
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
  private var length: Long = storageManager.edb(rId).length
  def open(): Unit = {}
  def next(): Option[Row2] = {
    if (currentId >= length) {
      NilTuple
    } else {
      currentId = currentId + 1
      Option(storageManager.edb(rId)(currentId - 1))
    }
  }
  def close(): Unit = {}
}

// TODO: most likely will combine Scan+Filter
case class Filter(input: RelOperator, cond: Row2 => Boolean) extends RelOperator {
  def open(): Unit = input.open()
  override def next(): Option[Row2] = {
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
  override def next(): Option[Row2] = {
    input.next() match {
      case Some(t) => Some(t.zipWithIndex.filter{ case (e, i) => ixs.contains(i) })
      case _ => NilTuple
    }
  }
  def close(): Unit = input.close()
}

case class Join(left: RelOperator,
                right: RelOperator,
                variables: IndexedSeq[IndexedSeq[Int]],
                constants: Map[Int, Constant]
               ) extends RelOperator {

  private var outputRelation: List[Row2] = List()
  private var index = 0

  // TODO [NOW]: Figure out >2 key join
  private var varKeysL = variables.map(i => i(0))
  private var varKeysR = variables.map(i => i(1))

  def open(): Unit = {
    index = 0
    outputRelation = List()

    // Nested loop join:
    var outerTable: ArrayBuffer[Row2] = left.toList()
    var innerTable = right.toList()

    outerTable.foreach(outerTuple => {
      val outerValues = varKeysL.map(k => outerTuple(k)) // TODO [NOW]: handle constants
      innerTable.foreach(innerTuple => {
        val innerValues = varKeysR.map(k => innerTuple(k))
        if (outerValues.equals(innerValues)) { // TODO: handle free variables
          val joined = outerTuple ++ innerTuple
          outputRelation :+ joined
        }
      })
    })
  }

  def next(): Option[Row2] = {
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