package datalog
import scala.collection.mutable.{ArrayBuffer, Map}

//trait Row[E] {
//  def getElementAt(idx: Int): Option[E]
//}
//
//trait Table[R] {
//  def getRow(idx: Int): Option[R]
//  def count(): Int
//  def insert(row: R): Int
//}

// Row and Table have Iterable API
type Row[T] = IndexedSeq[T] //extends Seq[T]
type Table[T] = ArrayBuffer[T] // extends ArrayBuffer[T]

trait StorageManager {
  type EDBElement
  type EDBRow = Row[EDBElement]
  type EDBTable = Table[EDBRow]

  type IDBElement
  type IDBRow = Row[IDBElement]
  type IDBTable = Table[IDBRow]

  def initRelation(rId: Int): Unit
  def insertEDB(rule: Atom): Unit
  def insertIDB(head: Atom, body: IndexedSeq[Atom]): Unit

  def idb(rId: Int): IDBTable
  def edb(rId: Int): EDBTable

//  val edbs: Map[Int, EDBTable] // TODO: maybe abstract Map too for future
//  val idbs: Map[Int, IDBTable]
//  def insertBulkEDB(rId: Int, terms: Seq[Seq[Any]]): Unit = {}
}

/**
 * This one uses the same constructs as the API, in future versions will specialize
 */
class SimpleStorage extends StorageManager {
  type EDBElement = Term
  type IDBElement = Atom

  type Row[E] = IndexedSeq[E]
  type Table[R] = ArrayBuffer[R]

  val edbs = Map[Int, EDBTable]()
  val idbs = Map[Int, IDBTable]()

//  val edbs = Map[Int, ArrayBuffer[Seq[Term]]]()
//  val idbs = Map[Int, ArrayBuffer[(Atom, Seq[Atom])]]()

  def idb(rId: Int): IDBTable = idbs(rId)
  def edb(rId: Int): EDBTable = edbs(rId)

  // store all relations
  def initRelation(rId: Int): Unit = {
    edbs.addOne(rId, ArrayBuffer[EDBRow]())
    idbs.addOne(rId, ArrayBuffer[IDBRow]())
  }
  // TODO: For now store IDB and EDB separately
  def insertEDB(rule: Atom): Unit = {
    edbs(rule.rId).addOne(rule.terms)
  }
  def insertIDB(head: Atom, body: IndexedSeq[Atom]): Unit = {
    idbs(head.rId).addOne(head +: body)
  }

  override def toString = {
    "EDB:" + edbs.mkString("[", ", ", "]") + "\nIDB:" + idbs.mkString("[", ", ", "]")
  }
}
