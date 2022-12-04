package datalog.storage

import scala.collection.{mutable, immutable}

// Keep pretty print stuff separate bc long and ugly, mb put it in a macro
class Printer[S <: StorageManager](val s: S) {
  var known = 0

  def factToString(r: s.EDB): String = {
    r.map(s => s.mkString("(", ", ", ")")).mkString("[", ", ", "]")
  }
  def ruleToString(r: s.IDB): String = {
    r.map(s => if (s.isEmpty) "<empty>" else s.head.toString + s.drop(1).mkString(" :- ", ",", ""))
      .mkString("[", "; ", "]")
  }
  def edbToString(db: s.FactDatabase): String = {
    immutable.ListMap(db.toSeq.sortBy(_._1):_*)
      .map((k, v) => (s.ns(k), factToString(v)))
      .mkString("[\n  ", ",\n  ", "]")
  }
  def idbToString(db: s.RuleDatabase): String = {
    immutable.ListMap(db.toSeq.sortBy(_._1):_*)
      .map((k, v) => (s.ns(k), ruleToString(v)))
      .mkString("[\n  ", ",\n  ", "]")
  }
  def planToString(keys: s.Table[s.JoinIndexes]): String = {
    "Union( " +
      keys.map(k =>
        "Project" + k.projIndexes.map((typ, v) => f"$typ$v").mkString("[", " ", "]") + "( " +
          "JOIN" +
          k.varIndexes.map(v => v.mkString("$", "==$", "")).mkString("[", ",", "]") +
          k.constIndexes.map((k, v) => k + "==" + v).mkString("{", "&&", "}") +
          k.deps.map(n => if(s.edbs.contains(n)) "edbs-" + s.ns(n) else "" + s.ns(n)).mkString("(", "*", ")") +
          " )"
      ).mkString("", ", ", "") +
      " )"
  }

  def snPlanToString(keys: s.Table[s.JoinIndexes]): String = {
    "UNION( " +
      keys.map(k =>
        var idx = -1
        "UNION(" +
          k.deps.map(d => {
            var found = false
            "PROJECT" + k.projIndexes.map((typ, v) => f"$typ$v").mkString("[", " ", "]") + "( " +
              "JOIN" +
              k.varIndexes.map(v => v.mkString("$", "==$", "")).mkString("[", ",", "]") +
              k.constIndexes.map((k, v) => k + "==" + v).mkString("{", "&&", "}") +
              k.deps.zipWithIndex.map((n, i) => {
                if (n == d && !found && i > idx)
                  found = true
                  idx = i
                  "delta[known][" + s.ns(n) + "]"
                else
                  if(s.edbs.contains(n)) "edbs[" + s.ns(n) + "]" else "derived[known][" + s.ns(n) + "]"
              }).mkString("(", "*", ")") +
              " )"
          }).mkString("[ ", ", ", " ]") + " )"
      ).mkString("[ ", ", ", " ]") +
      " )"
  }

  override def toString() = {
    def printHelperRelation(i: Int, db: s.FactDatabase): String = {
      val name = if (i == known) "known" else "new"
      "\n" + name + ": " + edbToString(db)
    }
    "+++++\n" +
      "EDB:" + edbToString(s.edbs) +
      "\nIDB:" + idbToString(s.idbs) +
      "\nDERIVED:" + s.derivedDB.map(printHelperRelation).mkString("[", ", ", "]") +
      "\nDELTA:" + s.deltaDB.map(printHelperRelation).mkString("[", ", ", "]") +
      "\n+++++"
  }
}
