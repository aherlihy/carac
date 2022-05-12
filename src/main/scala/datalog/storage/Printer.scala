package datalog.storage

import scala.collection.{mutable, immutable}

// Keep pretty print stuff separate bc long and ugly
class Printer(val s: StorageManager, ns: mutable.Map[Int, String]) {
  def printIncrementDB(i: Int) = {
    println("INCREMENT:" + edbToString(s.incrementalDB(i)))
  }

  def printIncrementDB() = {
    println("INCREMENT:" +
      s.incrementalDB.map((i, db) => ("queryId: " + i, edbToString(db))).mkString("[\n", ",\n", "]"))
  }

  def printDeltaDB(i: Int) = {
    println("DELTA:" + edbToString(s.deltaDB(i)))
  }

  def printDeltaDB() = {
    println("DELTA:" +
      s.deltaDB.map((i, db) => ("queryId: " + i, edbToString(db))).mkString("[\n", ",\n", "]"))
  }

  def factToString(r: s.EDB): String = {
    r.map(s => s.mkString("(", ", ", ")")).mkString("[", ", ", "]")
  }
  def ruleToString(r: s.IDB): String = {
    r.map(s => if (s.isEmpty) "<empty>" else s.head.toString + s.drop(1).mkString(" :- ", ",", ""))
      .mkString("[", "; ", "]")
  }
  def edbToString(db: s.FactDatabase): String = {
    immutable.ListMap(db.toSeq.sortBy(_._1):_*)
      .map((k, v) => (ns(k), factToString(v)))
      .mkString("[\n  ", ",\n  ", "]")
  }
  def idbToString(db: s.RuleDatabase): String = {
    immutable.ListMap(db.toSeq.sortBy(_._1):_*)
      .map((k, v) => (ns(k), ruleToString(v)))
      .mkString("[\n  ", ",\n  ", "]")
  }
  def planToString(keys: s.Table[s.JoinIndexes]): String = {
    "Union( " +
      keys.map(k =>
        "Project" + k.projIndexes.mkString("[", " ", "]") + "( " +
          "JOIN" +
          k.varIndexes.map(v => v.mkString("$", "==$", "")).mkString("[", ",", "]") +
          k.constIndexes.map((k, v) => k + "==" + v).mkString("{", "&&", "}") +
          k.deps.map(ns).mkString("(", "*", ")") +
          " )"
      ).mkString("[ ", ", ", " ]") +
      " )"
  }

  def snPlanToString(keys: s.Table[s.JoinIndexes]): String = {
    "UNION( " +
      keys.map(k =>
        "UNION(" +
          k.deps.map(d =>
            "PROJECT" + k.projIndexes.mkString("[", " ", "]") + "( " +
              "JOIN" +
              k.varIndexes.map(v => v.mkString("$", "==$", "")).mkString("[", ",", "]") +
              k.constIndexes.map((k, v) => k + "==" + v).mkString("{", "&&", "}") +
              k.deps.map(n =>
                if (n == d)
                  "delta-" + ns(n)
                else
                  ns(n)
              ).mkString("(", "*", ")") +
              " )"
          ).mkString("[ ", ", ", " ]") + " )"
      ).mkString("[ ", ", ", " ]") +
      " )"
  }

  override def toString = {
    "+++++\n" +
      "EDB:" + edbToString(s.edbs) +
      "\nIDB:" + idbToString(s.idbs) +
      "\nINCREMENT:" + s.incrementalDB.map((i, db) => (i, edbToString(db))).mkString("[", ", ", "]") +
      "\nDELTA:" + s.deltaDB.map((i, db) => (i, edbToString(db))).mkString("[", ", ", "]") +
      "\n+++++"
  }
}
