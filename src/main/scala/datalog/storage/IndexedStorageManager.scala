package datalog.storage

import datalog.dsl.{Atom, Constant, Variable}
import datalog.execution.{AllIndexes, JoinIndexes, PredicateType}
import datalog.storage.IndexedCollectionsCasts.*
import datalog.tools.Debug.debug

import scala.collection.{immutable, mutable}

/**
 * This is a storage manager that uses push-based operators that operate over the IndexedCollectionsTypes,
 * which are essentially just wrapped Scala collections.
 * @param ns
 */
class IndexedStorageManager(ns: NS = new NS()) extends IndexedCollectionsStorageManager(ns) {

  override def joinProjectHelper_withHash(inputsEDB: Seq[EDB], rId: Int, hash: String, onlineSort: Boolean): IndexedCollectionsEDB = {
    val originalK = allRulesAllIndexes(rId)(hash)
    val inputs = asIndexedCollectionsSeqEDB(inputsEDB)
//    println(s"Rule: ${printer.ruleToString(originalK.atoms)}")
//    println(s"input aritys: ${inputs.map(e => s"${e.name}|${e.arity}|").mkString("[", "*", "]")}")
//    println(s"input rels: ${inputs.map(e => e.factToString).mkString("[", "*", "]")}")
//    println(s"input indexes: ${inputs.map(IndexedCollectionsEDB.allIndexesToString).mkString("\n[", ",\n", "]")}")

//    var intermediateCardinalities = Seq[Int]()
    val fResult = if (inputs.length == 1) {// just filter + project
      inputs.head.projectFilterWithIndex(
        originalK.constIndexes,
        originalK.projIndexes,
        ns(rId),
        indexCandidates.getOrElse(rId, mutable.Set[Int]()),
        0
      )
    } else {
      val result = inputs
        .foldLeft(
          (IndexedCollectionsEDB.empty(0), 0, originalK) // initialize intermediate indexed-collection
        )((combo: (IndexedCollectionsEDB, Int, JoinIndexes), innerT: IndexedCollectionsEDB) =>
          val outerT = combo._1
          val atomI = combo._2
          var k = combo._3
          if (atomI == 0) // not a monad :(
            (innerT, atomI + 1, k)
          else
//            val (inner, outer) = // on the fly swapping of join order
//              if (atomI > 1 && onlineSort && outerT.length > innerT.length)
//                val body = k.atoms.drop(1)
//                val newerHash = JoinIndexes.getRuleHash(Seq(k.atoms.head, body(atomI)) ++ body.dropRight(body.length - atomI) ++ body.drop(atomI + 1))
//                k = allRulesAllIndexes(rId).getOrElseUpdate(newerHash, JoinIndexes(originalK.atoms.head +: body, Some(originalK.cxns)))
//                (outerT, innerT)
//              else
//                (innerT, outerT)
            // outer = outer relation, inner = inner relation
            val edbResult = outerT.joinFilterWithIndex(k, 0, innerT)
            //            intermediateCardinalities = intermediateCardinalities :+ edbResult.length
            (edbResult, atomI + 1, k)
        )
      IndexedCollectionsEDB(result._1.wrapped.mapInPlace(_.project(result._3.projIndexes)), indexCandidates.getOrElse(rId, mutable.Set()), ns(rId), result._3.projIndexes.length)
    }
//    println(s"=> SPJU result: ${fResult.factToString}")
    fResult
  }

  // TODO: for now ignore shallow embedding
  override def joinHelper(inputEDB: Seq[EDB], k: JoinIndexes): IndexedCollectionsEDB = ???
  override def projectHelper(input: EDB, k: JoinIndexes): IndexedCollectionsEDB = ???
  override def joinProjectHelper(inputsEDB: Seq[EDB], originalK: JoinIndexes, onlineSort: Boolean): IndexedCollectionsEDB = ???
  override def SPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): IndexedCollectionsEDB = ???
  override def naiveSPJU(rId: RelationId, keys: mutable.ArrayBuffer[JoinIndexes]): IndexedCollectionsEDB = ???
}
