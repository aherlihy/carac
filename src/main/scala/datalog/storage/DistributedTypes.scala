package datalog.storage

import org.apache.spark.sql.DataFrame

case class DistributedRow[T](row: Seq[T]) extends Row[T]

object DistributedCasts {
  def asDistributedEDB(to: Relation[StorageTerm]): DistributedEDB = to.asInstanceOf[DistributedEDB]
}

case class DistributedEDB(df: Option[DataFrame]) extends EDB {
  override def iterator: Iterator[DistributedRow[StorageTerm]] =
    df.map(_.collect()).getOrElse(Array.empty[org.apache.spark.sql.Row]).iterator.map(a => DistributedRow(a.toSeq.map(_.asInstanceOf[StorageTerm])))

  override def factToString: String =
    df.map(_.collect().map(s => s.mkString("(", ", ", ")")))
      .getOrElse(Array.empty[String]).mkString("[", ", ", "]")

  override def length: Int =
    df.map(_.count().toInt).getOrElse(0)

  def union(other: DistributedEDB): DistributedEDB =
    DistributedEDB(df.map(l => other.df.map(r => l union r).getOrElse(l)).orElse(other.df))

  def except(other: DistributedEDB): DistributedEDB =
    DistributedEDB(df.map(l => other.df.map(r => l except r).getOrElse(l)))

  def distinct: DistributedEDB = DistributedEDB(df.map(_.distinct()))
}
