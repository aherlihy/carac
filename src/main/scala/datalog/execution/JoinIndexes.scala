package datalog.execution
import datalog.dsl.Constant

/**
 * Wrapper object for join keys for IDB rules
 *
 * @param varIndexes - indexes of repeated variables within the body
 * @param constIndexes - indexes of constants within the body
 * @param projIndexes - for each term in the head, either ("c", the constant value) or ("v", the first index of the variable within the body)
 * @param deps - set of relations directly depended upon by this rule
 * @param edb - for rules that have EDBs defined on the same predicate, just read
 */
case class JoinIndexes(varIndexes: IndexedSeq[IndexedSeq[Int]],
                       constIndexes: Map[Int, Constant],
                       projIndexes: IndexedSeq[(String, Constant)],
                       deps: Seq[Int],
                       edb: Boolean = false) {
  override def toString: String =
    "{ variables:" + varIndexes.map(s => s.mkString("(", ", ", ")")).mkString("[", ", ", "]") +
      ", consts:" + constIndexes.mkString("[", ", ", "]") +
      ", project:" + projIndexes.mkString("[", ", ", "]") +
      ", deps:" + deps.mkString("[", ", ", "]") +
      ", edb:" + edb +
      " }"
  def varToString() = varIndexes.map(v => v.mkString("$", "==$", "")).mkString("[", ",", "]")
  def constToString() = constIndexes.map((k, v) => k + "==" + v).mkString("{", "&&", "}")
  def projToString() = projIndexes.map((typ, v) => f"$typ$v").mkString("[", " ", "]")
}
