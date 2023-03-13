package datalog.storage

import datalog.execution.JoinIndexes
import datalog.dsl.{Atom, Constant, Variable}

import scala.collection.{View, immutable, mutable}
import datalog.tools.Debug.debug

/**
 * Try out reduce vs. fold, or view vs. no view for benchmarking
 * @param ns
 */
class CollectionsStorageManagerReduceView() extends CollectionsStorageManager() {
  override def joinProjectHelper(inputs: Seq[EDB], originalK: JoinIndexes, sortOrder: (Int, Int, Int)): EDB = {
    if (inputs.size == 1) // just filter
      inputs.view.head
        .filter(e =>
          val filteredC = originalK.constIndexes.filter((ind, _) => ind < e.size)
          prefilter(filteredC, 0, e) && filteredC.size == originalK.constIndexes.size)
        .map(t =>
          originalK.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }).toIndexedSeq)
        .to(mutable.ArrayBuffer)
    else
      val k = originalK
      inputs.view
        .map(i => i.view)
        .reduceLeft((outer: View[Row[StorageTerm]], inner: View[Row[StorageTerm]]) =>
          outer
            .filter(o =>
              prefilter(k.constIndexes.filter((i, _) => i < o.size), 0, o)
            ) // filter outer tuple
            .flatMap(outerTuple =>
              inner
                .filter(i =>
                  prefilter(k.constIndexes.filter((ind, _) => ind >= outerTuple.size && ind < (outerTuple.size + i.size)), outerTuple.size, i) && toJoin(k, outerTuple, i)
                )
                .map(innerTuple => outerTuple ++ innerTuple))
        )
        .filter(edb => k.constIndexes.filter((i, _) => i >= edb.size).isEmpty)
        .map(t =>
          k.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }).toIndexedSeq
        )
        .to(mutable.ArrayBuffer)
  }

}
class CollectionsStorageManagerReduceNoView() extends CollectionsStorageManager() {
  override def joinProjectHelper(inputs: Seq[EDB], originalK: JoinIndexes, sortOrder: (Int, Int, Int)): EDB = {
    if (inputs.size == 1) // just filter
      inputs.head
        .filter(e =>
          val filteredC = originalK.constIndexes.filter((ind, _) => ind < e.size)
          prefilter(filteredC, 0, e) && filteredC.size == originalK.constIndexes.size)
        .map(t =>
          originalK.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }).toIndexedSeq)
    else
      val k = originalK
      inputs
        .reduceLeft((outer: EDB, inner: EDB) =>
          outer
            .filter(o =>
              prefilter(k.constIndexes.filter((i, _) => i < o.size), 0, o)
            ) // filter outer tuple
            .flatMap(outerTuple =>
              inner
                .filter(i =>
                  prefilter(k.constIndexes.filter((ind, _) => ind >= outerTuple.size && ind < (outerTuple.size + i.size)), outerTuple.size, i) && toJoin(k, outerTuple, i)
                )
                .map(innerTuple => outerTuple ++ innerTuple))
        )
        .filter(edb => k.constIndexes.filter((i, _) => i >= edb.size).isEmpty)
        .map(t =>
          k.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }).toIndexedSeq
        )
  }
}
class CollectionsStorageManagerFoldView() extends CollectionsStorageManager() {
  override def joinProjectHelper(inputs: Seq[EDB], originalK: JoinIndexes, sortOrder: (Int, Int, Int)): EDB = {
    if (inputs.size == 1) // just filter
      inputs.view.head
        .filter(e =>
          val filteredC = originalK.constIndexes.filter((ind, _) => ind < e.size)
          prefilter(filteredC, 0, e) && filteredC.size == originalK.constIndexes.size)
        .map(t =>
          originalK.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }).toIndexedSeq)
        .to(mutable.ArrayBuffer)
    else
      val result = inputs.view
        .map(i => i.view)
        .foldLeft(
          (EDB().view, 0, originalK)
        )((combo: (View[Row[StorageTerm]], Int, JoinIndexes), innerT: View[Row[StorageTerm]]) =>
          val outerT = combo._1
          val atomI = combo._2
          var k = combo._3
          if (atomI == 0) // not a monad :(
            (innerT, atomI + 1, k)
          else
            val (inner, outer) = (innerT, outerT)
            val edbResult = outer
              .filter(o =>
                prefilter(k.constIndexes.filter((i, _) => i < o.size), 0, o)
              ) // filter outer tuple
              .flatMap(outerTuple =>
                inner
                  .filter(i =>
                    prefilter(k.constIndexes.filter((ind, _) => ind >= outerTuple.size && ind < (outerTuple.size + i.size)), outerTuple.size, i) && toJoin(k, outerTuple, i)
                  )
                  .map(innerTuple => outerTuple ++ innerTuple))
            (edbResult, atomI + 1, k)
        )
      result._1
        .filter(edb => result._3.constIndexes.filter((i, _) => i >= edb.size).isEmpty)
        .map(t =>
          result._3.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }).toIndexedSeq
        )
        .to(mutable.ArrayBuffer)
  }
}

class CollectionsStorageManagerFoldNoView() extends CollectionsStorageManager() {
  override def joinProjectHelper(inputs: Seq[EDB], originalK: JoinIndexes, sortOrder: (Int, Int, Int)): EDB = {
    if (inputs.size == 1) // just filter
      inputs.head
        .filter(e =>
          val filteredC = originalK.constIndexes.filter((ind, _) => ind < e.size)
          prefilter(filteredC, 0, e) && filteredC.size == originalK.constIndexes.size)
        .map(t =>
          originalK.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }).toIndexedSeq)
        .to(mutable.ArrayBuffer)
    else
      val result = inputs
        .foldLeft(
          (EDB(), 0, originalK)
        )((combo: (EDB, Int, JoinIndexes), innerT: EDB) =>
          val outerT = combo._1
          val atomI = combo._2
          var k = combo._3
          if (atomI == 0) // not a monad :(
            (innerT, atomI + 1, k)
          else
            val (inner, outer) = (innerT, outerT)
            val edbResult = outer
              .filter(o =>
                prefilter(k.constIndexes.filter((i, _) => i < o.size), 0, o)
              ) // filter outer tuple
              .flatMap(outerTuple =>
                inner
                  .filter(i =>
                    prefilter(k.constIndexes.filter((ind, _) => ind >= outerTuple.size && ind < (outerTuple.size + i.size)), outerTuple.size, i) && toJoin(k, outerTuple, i)
                  )
                  .map(innerTuple => outerTuple ++ innerTuple))
            (edbResult, atomI + 1, k)
        )
      result._1
        .filter(edb => result._3.constIndexes.filter((i, _) => i >= edb.size).isEmpty)
        .map(t =>
          result._3.projIndexes.flatMap((typ, idx) =>
            typ match {
              case "v" => t.lift(idx.asInstanceOf[Int])
              case "c" => Some(idx)
              case _ => throw new Exception("Internal error: projecting something that is not a constant nor a variable")
            }).toIndexedSeq
        )
  }
}

