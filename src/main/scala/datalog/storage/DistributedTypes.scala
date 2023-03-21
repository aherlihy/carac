package datalog.storage

import org.apache.spark.sql.DataFrame

type DistributedRow = org.apache.spark.sql.Row with Row[StorageTerm]

object DistributedCasts {
  def asDistributedEDB(to: Relation[StorageTerm]): DistributedEDB = to.asInstanceOf[DistributedEDB]
}

case class DistributedEDB(df: DataFrame) extends EDB {
  override def iterator: Iterator[DistributedRow] = df.collect().iterator.asInstanceOf[Iterator[DistributedRow]]
  override def factToString: String = df.collect().map(s => s.mkString("(", ", ", ")")).mkString("[", ", ", "]")
  override def length: Int = df.count().toInt
}
