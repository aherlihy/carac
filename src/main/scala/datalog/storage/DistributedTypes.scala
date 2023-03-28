package datalog.storage

import org.apache.spark.sql.DataFrame

case class DistributedRow[T](row: Seq[T]) extends Row[T]

object DistributedCasts {
  def asDistributedEDB(to: Relation[StorageTerm]): DistributedEDB = to.asInstanceOf[DistributedEDB]
}

case class DistributedEDB(df: DataFrame) extends EDB {
  override def iterator: Iterator[DistributedRow[StorageTerm]] = df.collect().iterator.map(a => DistributedRow(a.toSeq.map(_.asInstanceOf[StorageTerm])))
  override def factToString: String = df.collect().map(s => s.mkString("(", ", ", ")")).mkString("[", ", ", "]")
  override def length: Int = df.count().toInt
}
