package datalog.storage

import datalog.dsl.Constant
import datalog.storage.CollectionsCasts.asCollectionsEDB
import datalog.tools.Debug.debug
import datalog.execution.{AggOpIndex, GroupingJoinIndexes}
import datalog.storage.StorageAggOp

import java.io.{BufferedInputStream, BufferedOutputStream, BufferedReader, BufferedWriter, DataInputStream, DataOutputStream, EOFException, InputStreamReader, OutputStreamWriter}
import java.nio.{ByteBuffer, ByteOrder}
import scala.collection.mutable
import scala.sys.process.*

// Indicates the end of the stream
final val NilTuple: Option[Nothing] = None

/**
 * These are relational operators for the pull-based Volcano engine.
 *
 * @param storageManager: Right now this is always going to be a CollectionsStorageManager. If needed can be
 * made more general to operate over EDBs instead of CollectionsEDBs and so on.
 */
class VolcanoOperators[S <: StorageManager](val storageManager: S) {
  trait VolOperator {
    def open(): Unit

    def next(): Option[CollectionsRow]

    def close(): Unit

    def toList(): CollectionsEDB = { // TODO: fix this to use iterator override
      val list = CollectionsEDB()
      this.open()
      while (
        this.next() match {
          case Some(r) =>
            list.addOne(r)
            true
          case _ => false
        }) {}
      this.close()
      list
    }
//      final override def iterator: Iterator[CollectionsRow] =
//        new Iterator[CollectionsRow] with AutoCloseable {
//          private val op =
//            RelOperator.this.clone().asInstanceOf[RelOperator]
//          op.open()
//
//          var n: Option[Option[CollectionsRow]] = Option.empty
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
//          override def next(): CollectionsRow = {
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

  case class UDFScanOperator(path: String) extends VolOperator {
    val outputRelation = CollectionsEDB()
    var index = 0
    def open(): Unit = {
      index = 0
      val pb = Process(path) // Or, Seq(<process>, <args>)

      val processLogger = ProcessLogger(
        (output: String) => {
          outputRelation.addOne(CollectionsRow(Seq(output)))
        }, // Handle standard output
        (error: String) => System.err.println(error) // Handle error output
      )

      val exitCode = pb.run(processLogger).exitValue()

      if (exitCode != 0) throw new Exception(s"User-supplied utility exited with code $exitCode")
    }

    def next(): Option[CollectionsRow] = {
      if (index >= outputRelation.length)
        NilTuple
      else
        index += 1
        Option(outputRelation(index - 1))
    }

    def close(): Unit = {}
  }

  enum Metadata:
    case CSV
    case Binary(length: Int, byteOrder: ByteOrder)

  case class UDFProjectOperator(path: String, input: VolOperator, inputMD: Metadata = Metadata.CSV, outputMD: Metadata = Metadata.CSV) extends VolOperator {
    var process: Process = _
    var processOutput: BufferedInputStream = _
    var processInput: BufferedOutputStream = _
    var processOutputReader: BufferedReader = _
    var processInputWriter: BufferedWriter = _
    var counter = 0
    val bb = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN) // TODO set to metadata

    def open(): Unit = {
      counter = 0
      input.open()

      // Start the subprocess
      val io = new ProcessIO(
        stdin => {
          processInput = new BufferedOutputStream(stdin)
        },
        stdout => {
          processOutput = new BufferedInputStream(stdout)
        },
        stderr => {
          scala.io.Source.fromInputStream(stderr).getLines().foreach(l => System.out.println(s"Error from subprocess: $l"))
        }
      )

      val p = s"$path${if (outputMD == Metadata.CSV) "" else "-producer"}${if (inputMD == Metadata.CSV) "" else "-consumer"}"
      process = p.run(io)

//      processInputWriter = new BufferedWriter(new OutputStreamWriter(processInput))
//      processOutputReader = new BufferedReader(new InputStreamReader(processOutput))
    }

    override def next(): Option[CollectionsRow] = {
      input.next() match {
        case Some(tuple) => {
          // Write to the subprocess and flush
//          println(s"received: ${tuple}")
          inputMD match
            case Metadata.CSV =>
//              println("Consumer CSV")
              val inputInt = tuple.wrapped.head.toString // conversion bc CollectionRow type, known string
              val processInputWriter = new BufferedWriter(new OutputStreamWriter(processInput))
              processInputWriter.write(inputInt.toString)
              processInputWriter.newLine()
              processInputWriter.flush()

            case Metadata.Binary(length, byteOrder) =>
//              println("Consumer Binary")
              val inputInt = tuple.wrapped.head.asInstanceOf[Int] // conversion bc CollectionsRow type
              bb.clear()
              bb.putInt(inputInt)
              bb.flip()
              processInput.write(bb.array())
              processInput.flush()

          val response =
            outputMD match
              case Metadata.CSV =>
//                println("Producer CSV")
                val processOutputReader = new BufferedReader(new InputStreamReader(processOutput))
                processOutputReader.readLine()

              case Metadata.Binary(length, byteOrder) =>
//                println("Producer Binary")
                bb.clear()
                processOutput.read(bb.array())
                val read = bb.getInt
//                println(s"Read in $read")
                read // TODO: fix

//          println(s"received response $response")
          Some(CollectionsRow(Seq(response))) // Emit the contents of that line
        }
        case None =>
          NilTuple
      }
    }

    def close(): Unit = {
      input.close()
      // Close the subprocess streams
      processInput.close()
      processOutput.close()
//      if (process.isAlive()) process.destroy() // TODO: needed?
    }
  }

  case class EmptyScan() extends VolOperator {
    def open(): Unit = {}
    def next(): Option[CollectionsRow] = NilTuple
    def close(): Unit = {}
  }

  case class Scan(relation: CollectionsEDB, rId: Int) extends VolOperator {
    private var currentId: Int = 0
    private var length: Long = relation.length

    def open(): Unit = {
//      debug(s"SCAN[$rId]")
    }

    def next(): Option[CollectionsRow] = {
      if (currentId >= length) {
        NilTuple
      } else {
        currentId += 1
        Option(relation(currentId - 1))
      }
    }

    def close(): Unit = {}
  }

  // NOTE: this isn't currently used by SPJU bc merged scan+filter
  case class Filter(input: VolOperator)
                   (cond: CollectionsRow => Boolean) extends VolOperator {

    def open(): Unit = input.open()

    override def next(): Option[CollectionsRow] = {
      var nextTuple = input.next()
      while (nextTuple match {
        case Some(n) => !cond(n)
        case _ => false
      }) nextTuple = input.next()
      nextTuple
    }

    def close(): Unit = input.close()
  }

  case class Project(input: VolOperator, ixs: Seq[(String, Constant)]) extends VolOperator {
    def open(): Unit = {
//      debug(s"PROJ[$ixs]")
      input.open()
    }

    override def next(): Option[CollectionsRow] = {
      if (ixs.isEmpty) {
        return input.next()
      }
      input.next() match {
        case Some(t) =>
          Some(
            CollectionsRow(ixs.flatMap((typ, idx) =>
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

  case class Join(inputs: Seq[VolOperator],
                  variables: Seq[Seq[Int]],
                  constants: mutable.Map[Int, Constant]) extends VolOperator {

    private var outputRelation = CollectionsEDB()
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
      val inputList: Seq[CollectionsEDB] = inputs.map(i => i.toList())

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
    def next(): Option[CollectionsRow] = {
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
  case class Union(ops: Seq[VolOperator]) extends VolOperator {
    private var outputRelation: CollectionsEDB = CollectionsEDB()
    private var index = 0
    def open(): Unit = {
      val opResults = ops.map(o => o.toList())
      import CollectionsEDB.unionEDB
      outputRelation = asCollectionsEDB(opResults.unionEDB)
    }
    def next(): Option[CollectionsRow] = {
      if (index >= outputRelation.length)
        NilTuple
      else
        index += 1
        Option(outputRelation(index - 1))
    }
    def close(): Unit = ops.foreach(o => o.close())
  }
  case class Diff(ops: Seq[VolOperator]) extends VolOperator {
    private var outputRelation: CollectionsEDB = CollectionsEDB()
    private var index = 0
    def open(): Unit =
      outputRelation = ops.map(o => o.toList()).toSet.reduce((l, r) => l diff r)
    def next(): Option[CollectionsRow] = {
      if (index >= outputRelation.length)
        NilTuple
      else
        index += 1
        Option(outputRelation(index - 1))
    }
    def close(): Unit = ops.foreach(o => o.close())
  }


  // Closely coupled to Collections. Alternative: calculate an individual group when calling next()
  case class Grouping(input: VolOperator, gji: GroupingJoinIndexes) extends VolOperator {
    private var outputRelation: CollectionsEDB = CollectionsEDB()
    private var index = 0

    def open(): Unit =
      outputRelation = asCollectionsEDB(storageManager.groupingHelper(input.toList(), gji))

    def next(): Option[CollectionsRow] = {
      if (index >= outputRelation.length)
        NilTuple
      else
        index += 1
        Option(outputRelation(index - 1))
    }
    def close(): Unit = input.close()
  }
}