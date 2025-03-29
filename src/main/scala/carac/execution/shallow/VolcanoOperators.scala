//package carac.execution
//
//import carac.dsl.Constant
//import carac.storage.{GeneralCollectionsEDB, CollectionsRow, StorageManager, StorageTerm}
//import carac.tools.Debug.debug
//
//import scala.collection.mutable
//import scala.collection.immutable.ArraySeq
//
//// Indicates the end of the stream
//final val NilTuple: Option[Nothing] = None
//
///**
// * These are relational operators for the pull-based Volcano engine.
// *
// * @param storageManager: Right now this is always going to be a CollectionsStorageManager. If needed can be
// * made more general to operate over EDBs instead of GeneralCollectionsEDBs and so on.
// */
//class VolcanoOperators[S <: StorageManager](val storageManager: S) {
//  import GeneralCollectionCasts.*
//  trait VolOperator {
//    def open(): Unit
//
//    def next(): Option[CollectionsRow]
//
//    def close(): Unit
//
//    def toList(): GeneralCollectionsEDB = { // TODO: fix this to use iterator override
//      val list = storageManager.getEmptyEDB(0)
//      this.open()
//      while (
//        this.next() match {
//          case Some(r) =>
//            list.addOne(r)
//            true
//          case _ => false
//        }) {}
//      this.close()
//      list
//    }
////      final override def iterator: Iterator[CollectionsRow] =
////        new Iterator[CollectionsRow] with AutoCloseable {
////          private val op =
////            RelOperator.this.clone().asInstanceOf[RelOperator]
////          op.open()
////
////          var n: Option[Option[CollectionsRow]] = Option.empty
////
////          def prepareNext(): Unit = {
////            if (n.nonEmpty) return
////              n = Option(op.next())
////          }
////
////          override def hasNext: Boolean = {
////            prepareNext()
////            n.get.nonEmpty
////          }
////
////          override def next(): CollectionsRow = {
////            prepareNext()
////            val ret = n.get
////            assert(ret.nonEmpty)
////            n = Option.empty
////            ret.get
////          }
////
////          override def close(): Unit = {
////            op.close()
////          }
////        }
//  }
//
//  case class EmptyScan() extends VolOperator {
//    def open(): Unit = {}
//    def next(): Option[CollectionsRow] = NilTuple
//    def close(): Unit = {}
//  }
//
//  case class Scan(relation: GeneralCollectionsEDB, rId: Int) extends VolOperator {
//    private var currentId: Int = 0
//    private var length: Long = relation.length
//
//    def open(): Unit = {
////      debug(s"SCAN[$rId]")
//    }
//
//    def next(): Option[CollectionsRow] = {
//      if (currentId >= length) {
//        NilTuple
//      } else {
//        currentId += 1
//        Option(relation(currentId - 1))
//      }
//    }
//
//    def close(): Unit = {}
//  }
//
//  // NOTE: this isn't currently used by SPJU bc merged scan+filter
//  case class Filter(input: VolOperator)
//                   (cond: CollectionsRow => Boolean) extends VolOperator {
//
//    def open(): Unit = input.open()
//
//    override def next(): Option[CollectionsRow] = {
//      var nextTuple = input.next()
//      while (nextTuple match {
//        case Some(n) => !cond(n)
//        case _ => false
//      }) nextTuple = input.next()
//      nextTuple
//    }
//
//    def close(): Unit = input.close()
//  }
//
//  case class Project(input: VolOperator, ixs: Seq[(String, Constant)]) extends VolOperator {
//    def open(): Unit = {
////      debug(s"PROJ[$ixs]")
//      input.open()
//    }
//
//    override def next(): Option[CollectionsRow] = {
//      if (ixs.isEmpty) {
//        return input.next()
//      }
//      input.next() match {
//        case Some(t) =>
//          Some(
//            ArraySeq.from(ixs.flatMap((typ, idx) =>
//              typ match {
//                case "v" => t.lift(idx.asInstanceOf[Int])
//                case "c" => Some(idx)
//                case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
//              }))
//          )
//        case _ => NilTuple
//      }
//    }
//
//    def close(): Unit = input.close()
//  }
//
//  case class Join(inputs: Seq[VolOperator],
//                  variables: Seq[Seq[Int]],
//                  constants: mutable.Map[Int, Constant]) extends VolOperator {
//
//    private var outputRelation = storageManager.getEmptyEDB(0)
//    private var index = 0
//
//    def scanFilter(maxIdx: Int)(get: Int => StorageTerm = x => x.asInstanceOf[StorageTerm]) = {
//      val vCmp = variables.isEmpty || variables.forall(condition =>
//        if (condition.head >= maxIdx)
//          true
//        else
//          val toCompare = get(condition.head)
//            condition.drop(1).forall(idx =>
//          idx >= maxIdx || get(idx) == toCompare
//        )
//      )
//      val kCmp = constants.isEmpty || constants.forall((idx, const) =>
//        idx >= maxIdx || get(idx) == const
//      )
//      vCmp && kCmp
//    }
//
//    override def open(): Unit = {
//      index = 0
//      val inputList: Seq[GeneralCollectionsEDB] = inputs.map(i => i.toList())
//
//      outputRelation = inputList
//        .reduceLeft((outer, inner) => {
//          outer.flatMap(outerTuple => {
//            inner.flatMap(innerTuple => {
//              val get = (i: Int) => {
//                outerTuple.applyOrElse(i, j => innerTuple(j - outerTuple.length))
//              }
//              if(scanFilter(innerTuple.length + outerTuple.length)(get))
//                Some(outerTuple.concat(innerTuple))
//              else
//                None
//            })
//          })
//        })
//        .filter(r => scanFilter(r.length)(r.apply))
//    }
//    def next(): Option[CollectionsRow] = {
//      if (index >= outputRelation.length)
//        NilTuple
//      else {
//        index += 1
//        Option(outputRelation(index - 1))
//      }
//    }
//
//    def close(): Unit = {
//      inputs.foreach(i => i.close())
//    }
//  }
//
//
//  /**
//   * TODO: remove duplicates
//   *
//   * @param ops
//   */
//  case class Union(ops: Seq[VolOperator]) extends VolOperator {
//    private var outputRelation: GeneralCollectionsEDB = storageManager.getEmptyEDB(0)
//    private var index = 0
//    def open(): Unit = {
//      val opResults = ops.map(o => o.toList())
//      outputRelation = storageManager.union(opResults*)
//    }
//    def next(): Option[CollectionsRow] = {
//      if (index >= outputRelation.length)
//        NilTuple
//      else
//        index += 1
//        Option(outputRelation(index - 1))
//    }
//    def close(): Unit = ops.foreach(o => o.close())
//  }
//  case class Diff(ops: Seq[VolOperator]) extends VolOperator {
//    private var outputRelation: GeneralCollectionsEDB = storageManager.getEmptyEDB(0)
//    private var index = 0
//    def open(): Unit =
//      outputRelation = ops.map(o => o.toList()).toSet.reduce((l, r) => l.diff(r))
//    def next(): Option[CollectionsRow] = {
//      if (index >= outputRelation.length)
//        NilTuple
//      else
//        index += 1
//        Option(outputRelation(index - 1))
//    }
//    def close(): Unit = ops.foreach(o => o.close())
//  }
//}
//class PullBasedSPJU extends SPJU {
//  def naiveSPJU(sm: StorageManager, rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB = ???// {
  //    debug("naiveSPJU:", () => s"r=${ns(rId)}($rId) keys=${printer.naivePlanToString(keys)} knownDBId $knownDbId")
  //    import relOps.*
  //
  //    val plan = Union(
  //        keys.map(k =>
  //          if (k.edb)
  //            Scan(discoveredFacts.getOrElse(rId, CollectionsEDB()), rId)
  //          else
  //            Project(
  //              Join(k.deps.zipWithIndex.map((md, i) =>
  //                val (typ, r) = md
  //                val q = Scan(getKnownDerivedDB(r), r)
  //                typ match
  //                  case PredicateType.NEGATED =>
  //                    val arity = k.atoms(i + 1).terms.length
  //                    val compl = getComplement(r, arity)
  //                    val res = Diff(Seq(Scan(compl, r), q))
  //                    debug(s"found negated relation, rule=", () => s"${printer.ruleToString(k.atoms)}\n\tarity=$arity")
  //                    res
  //                  case _ => q
  //
  //              ), k.varIndexes, k.constIndexes),
  //              k.projIndexes
  //            )
  //        ).toSeq
  //    )
  //    plan.toList()
  //  }
  //
  //  /**
  //   * Use relational operators to evaluate an IDB rule using Semi-Naive algo
  //   *
  //   * @param rIds - The ids of the relations
  //   * @param keys - a JoinIndexes object to join on
  //   * @return
  //   */
  //  def SPJU(sm: StorageManager, rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): EDB = ??? //{
  //    import relOps.*
  //    debug("SPJU:", () => s"r=${ns(rId)} keys=${printer.snPlanToString(keys)} knownDBId $knownDbId")
  //    val plan = Union(
  //      keys.map(k => // for each idb rule
  //        if (k.edb)
  //          Scan(discoveredFacts.getOrElse(rId, CollectionsEDB()), rId)
  //        else
  //          var idx = -1 // if dep is featured more than once, only us delta once, but at a different pos each time
  //          Union(
  //            k.deps.map((typ, d) => {
  //              var found = false
  //              Project(
  //                Join(
  //                  k.deps.zipWithIndex.map((md, i) => {
  //                    val (typ, r) = md
  //                    val q = if (r == d && !found && i > idx)
  //                      found = true
  //                      idx = i
  //                      if (typ != PredicateType.NEGATED) // if negated then we want the complement of all facts not just the delta
  //                        Scan(getKnownDeltaDB(r), r)
  //                      else
  //                        Scan(getKnownDerivedDB(r), r)
  //                    else
  //                      Scan(getKnownDerivedDB(r), r)
  //                    typ match
  //                      case PredicateType.NEGATED =>
  //                        val arity = k.atoms(i + 1).terms.length
  //                        val compl = getComplement(r, arity)
  //                        val res = Diff(Seq(Scan(compl, r), q))
  //                        debug(s"found negated relation, rule=", () => s"${printer.ruleToString(k.atoms)}\n\tarity=$arity")
  //                        res
  //                      case _ => q
  //                  }),
  //                  k.varIndexes,
  //                  k.constIndexes
  //                ),
  //                k.projIndexes
  //              )
  //            })
  //          )
  //      ).toSeq
  //    )
  //    plan.toList()
  //  }
//}
