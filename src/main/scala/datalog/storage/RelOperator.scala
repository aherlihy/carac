package datalog.storage

import datalog.dsl.Constant

import scala.collection.mutable

// Indicates the end of the stream
final val NilTuple: Option[Nothing] = None

final val dbg = true//false
class RelationalOperators[S <: StorageManager](val storageManager: S) {
  type edbRow = storageManager.Row[storageManager.StorageTerm]
  type table[T] = storageManager.Table[T]

  trait RelOperator {
    def open(): Unit

    def next(): Option[edbRow]

    def close(): Unit

    def toList(): mutable.ArrayBuffer[edbRow] = { // TODO: fix this to use iterator override
      val list = mutable.ArrayBuffer[edbRow]()
      this.open()
      while (
        this.next() match {
          case Some(r) => {
            list.addOne(r); true
          }
          case _ => false
        }) {}
      this.close()
      list
    }
//      final override def iterator: Iterator[edbRow] =
//        new Iterator[edbRow] with AutoCloseable {
//          private val op =
//            RelOperator.this.clone().asInstanceOf[RelOperator]
//          op.open()
//
//          var n: Option[Option[edbRow]] = Option.empty
//
//          def prepareNext(): Unit = {
//            if (n.nonEmpty) return
//              n = Option(op.next())
//          }
//
//          override def hasNext: Boolean = {
//            prepareNext()
//            n.get.nonEmpty
//          }
//
//          override def next(): edbRow = {
//            prepareNext()
//            val ret = n.get
//            assert(ret.nonEmpty)
//            n = Option.empty
//            ret.get
//          }
//
//          override def close(): Unit = {
//            op.close()
//          }
//        }
  }

  // def scan(rId: Int): RelOperator = Scan(rId)

  case class EmptyScan() extends RelOperator {
    def open(): Unit = {}
    def next(): Option[edbRow] = NilTuple
    def close(): Unit = {}
  }

  case class Scan(relation: storageManager.Relation[storageManager.StorageTerm], rId: Int) extends RelOperator {
    private var currentId: Int = 0
    private var length: Long = relation.size

    def open(): Unit = {
//      debug("SCAN[" + rId + "]")
    }

    def next(): Option[edbRow] = {
      if (currentId >= length) {
        NilTuple
      } else {
        currentId = currentId + 1
        Option(relation(currentId - 1))
      }
    }

    def close(): Unit = {}
  }

  // TODO: most likely will combine Scan+Filter, or split out Join+Filter
  case class Filter(input: RelOperator)
                   (cond: edbRow => Boolean) extends RelOperator {

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
    def open(): Unit = {
//      debug("PROJ[" + ixs + "]")
      input.open()
    }

    override def next(): Option[edbRow] = {
      if (ixs.isEmpty) {
        return input.next()
      }
      input.next() match {
        case Some(t) => Some(t.zipWithIndex.filter((e, i) => ixs.contains(i)).map(_._1))
        case _ => NilTuple
      }
    }

    def close(): Unit = input.close()
  }

  case class Join(inputs: Seq[RelOperator],
                  variables: IndexedSeq[IndexedSeq[Int]],
                  constants: Map[Int, Constant]) extends RelOperator {

    private var outputRelation= mutable.ArrayBuffer[edbRow]()
    private var index = 0

    // TODO [NOW]: >2 key join
    private var left: RelOperator = EmptyScan()
    private var right: RelOperator = EmptyScan()

    def open(): Unit = {
      index = 0
      outputRelation = mutable.ArrayBuffer()

      if(inputs.length == 1) {
        outputRelation = inputs.head.toList().filter(
          joined =>
            (constants.isEmpty || constants.forall((idx, const) => joined(idx) == const)) &&
            (variables.isEmpty || variables.forall(condition => condition.forall(c => joined(c) == joined(condition.head))))
        )
        return
      } else if (inputs.length > 2) {
        throw new Error("TODO: multi-way join")
      }

//      println("inputs=" + inputs)
//
      val inputList: Seq[mutable.ArrayBuffer[edbRow]] = inputs.map(i => i.toList())
//
//      outputRelationNew = inputList.foldLeft(inputList.head)((outer, inner) => {
//        println("acc=" + outer + " op=" + inner)
//        outer.map(outerTuple => {
//          inner.map(innerTuple => {
//            outerTuple ++ innerTuple
//          })
//        }).filter(joined =>
//              (variables.isEmpty || variables.forall(condition =>
//                condition.forall(c => joined(c) == joined(condition.head))))
//                && (constants.isEmpty ||
//                constants.forall((idx, const) => joined(idx) == const)))
//      })

//      left = inputs(0)
//      right = if (inputs.length > 1) inputs(1) else EmptyScan()


      // Nested loop join:
      val outerTable = inputList(0)//.toList()
      val innerTable = inputList(1)//.toList()

      outerTable.foreach(outerTuple => {
        innerTable.foreach(innerTuple => {
          val joined = outerTuple ++ innerTuple
          if ((variables.isEmpty || variables.forall(condition =>
                condition.forall(c => joined(c) == joined(condition.head))))
            && (constants.isEmpty ||
              constants.forall((idx, const) => joined(idx) == const))) {
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
      inputs.foreach(i => i.close())
    }
  }

  /**
   * TODO: remove duplicates
   *
   * @param ops
   */
  case class Union(ops: Seq[RelOperator]) extends RelOperator {
//    private var currentRel: Int = 0
//    private var length: Long = ops.length
    private var outputRelation: IndexedSeq[edbRow] = IndexedSeq()
    private var index = 0
    def open(): Unit = {
      var opResults = ops.map(o => o.toList())
      outputRelation = opResults.flatten.toSet.toIndexedSeq
    }
    def next(): Option[edbRow] = {
      if (index >= outputRelation.size)
        NilTuple
      else
        index += 1
        Option(outputRelation(index - 1))
//      if (currentRel >= length) {
//        NilTuple
//      } else {
//        var nextT = ops(currentRel).next()
//        nextT match {
//          case Some(t) =>
//            nextT
//          case _ =>
//            currentRel = currentRel + 1
//            next()
//        }
//      }
    }
    def close(): Unit = ops.foreach(o => o.close())
  }
  case class Diff(ops: mutable.ArrayBuffer[RelOperator]) extends RelOperator {
    private var outputRelation: IndexedSeq[edbRow] = IndexedSeq()
    private var index = 0
    def open(): Unit = {
      var opResults = ops.map(o => o.toList())
      outputRelation = opResults.toSet.reduce((l, r) => l diff r).toIndexedSeq
    }
    def next(): Option[edbRow] = {
      if (index >= outputRelation.size)
        NilTuple
      else
        index += 1
        Option(outputRelation(index - 1))
    }
    def close(): Unit = ops.foreach(o => o.close())
  }
}