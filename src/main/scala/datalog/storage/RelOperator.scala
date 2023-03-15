package datalog.storage

import datalog.dsl.Constant
import datalog.tools.Debug.debug

import scala.collection.mutable

// Indicates the end of the stream
final val NilTuple: Option[Nothing] = None

final val dbg = true//false
class RelationalOperators[S <: StorageManager](val storageManager: S) {
  trait RelOperator {
    def open(): Unit

    def next(): Option[SimpleRow]

    def close(): Unit

    def toList(): SimpleEDB = { // TODO: fix this to use iterator override
      val list = SimpleEDB()
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
//      final override def iterator: Iterator[SimpleRow] =
//        new Iterator[SimpleRow] with AutoCloseable {
//          private val op =
//            RelOperator.this.clone().asInstanceOf[RelOperator]
//          op.open()
//
//          var n: Option[Option[SimpleRow]] = Option.empty
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
//          override def next(): SimpleRow = {
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
    def next(): Option[SimpleRow] = NilTuple
    def close(): Unit = {}
  }

  case class Scan(relation: SimpleEDB, rId: Int) extends RelOperator {
    private var currentId: Int = 0
    private var length: Long = relation.length

    def open(): Unit = {
//      debug(s"SCAN[$rId]")
    }

    def next(): Option[SimpleRow] = {
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
                   (cond: SimpleRow => Boolean) extends RelOperator {

    def open(): Unit = input.open()

    override def next(): Option[SimpleRow] = {
      var nextTuple = input.next()
      while (nextTuple match {
        case Some(n) => !cond(n)
        case _ => false
      }) nextTuple = input.next()
      nextTuple
    }

    def close(): Unit = input.close()
  }

  case class Project(input: RelOperator, ixs: Seq[(String, Constant)]) extends RelOperator {
    def open(): Unit = {
//      debug(s"PROJ[$ixs]")
      input.open()
    }

    override def next(): Option[SimpleRow] = {
      if (ixs.isEmpty) {
        return input.next()
      }
      input.next() match {
        case Some(t) =>
          Some(
            SimpleRow(ixs.flatMap((typ, idx) =>
              typ match {
                case "v" => t.lift(idx.asInstanceOf[Int])
                case "c" => Some(idx)
                case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
              }))
          )
        case _ => NilTuple
      }
    }

    def close(): Unit = input.close()
  }

  case class Join(inputs: Seq[RelOperator],
                     variables: Seq[Seq[Int]],
                     constants: Map[Int, Constant]) extends RelOperator {

    private var outputRelation = SimpleEDB()
    private var index = 0

    def scanFilter(maxIdx: Int)(get: Int => StorageTerm = x => x.asInstanceOf[StorageTerm]) = {
      val vCmp = variables.isEmpty || variables.forall(condition =>
        if (condition.head >= maxIdx)
          true
        else
          val toCompare = get(condition.head)
            condition.drop(1).forall(idx =>
          idx >= maxIdx || get(idx) == toCompare
        )
      )
      val kCmp = constants.isEmpty || constants.forall((idx, const) =>
        idx >= maxIdx || get(idx) == const
      )
      vCmp && kCmp
    }

    override def open(): Unit = {
      index = 0
      val inputList: Seq[SimpleEDB] = inputs.map(i => i.toList())

      outputRelation = inputList
        .reduceLeft((outer, inner) => {
          outer.flatMap(outerTuple => {
            inner.flatMap(innerTuple => {
              val get = (i: Int) => {
                outerTuple.applyOrElse(i, j => innerTuple(j - outerTuple.length))
              }
              if(scanFilter(innerTuple.length + outerTuple.length)(get))
                Some(outerTuple.concat(innerTuple))
              else
                None
            })
          })
        })
        .filter(r => scanFilter(r.length)(r.apply))
    }
    def next(): Option[SimpleRow] = {
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
    private var outputRelation: SimpleEDB = SimpleEDB()
    private var index = 0
    def open(): Unit = {
      val opResults = ops.map(o => o.toList())
//      outputRelation = opResults.flatten.toSet.toIndexedSeq
      import SimpleEDB.unionEDB
      opResults.unionEDB
    }
    def next(): Option[SimpleRow] = {
      if (index >= outputRelation.length)
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
    private var outputRelation: SimpleEDB = SimpleEDB()
    private var index = 0
    def open(): Unit =
      outputRelation = ops.map(o => o.toList()).toSet.reduce((l, r) => l diff r)
    def next(): Option[SimpleRow] = {
      if (index >= outputRelation.length)
        NilTuple
      else
        index += 1
        Option(outputRelation(index - 1))
    }
    def close(): Unit = ops.foreach(o => o.close())
  }
}