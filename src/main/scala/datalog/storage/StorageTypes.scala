package datalog.storage

import datalog.dsl.{Constant, Term, Variable}
import datalog.dsl.Variable
import datalog.execution.PredicateType

import scala.collection.mutable

type RelationId = Int
type KnowledgeId = Int
enum DB:
  case Derived, Delta
enum KNOWLEDGE:
  case New, Known

/* TODO: expand to other types, right now we just support Variables + Constants that are either Int or String.
 * When expended, may want the DSL types and the storage types to diverge, so use type aliases (for now) instead of DSL types. */
type StorageTerm = Term
type StorageVariable = Variable
type StorageConstant = Constant

/* All methods used within storage managers for now, so methods only defined with precise types */
trait Row[T]

/* Most methods used within storage manager so are defined on the implementation, other than length (execution) and factToString (printer) */
trait Relation[T] extends IterableOnce[Row[T]] {
  def length: Int
  def factToString: String
}

/* EDBs always operate on StorageTerms only */
type EDB = Relation[StorageTerm]

/* All but toSeq can be defined on the precise type */
trait Database[T <: EDB] {
  def toSeq: Seq[(RelationId, T)]
}

/**
 * Quick BiMap for namespaces
 */
class NS() {
  private val nameToRid = mutable.Map[String, RelationId]()
  private val rIdToName = mutable.Map[RelationId, String]()
  def apply(name: String): RelationId =
    nameToRid.getOrElse(name, -1)
  def apply(rId: RelationId): String =
    rIdToName.getOrElse(rId, s"<$rId>")

  def apply(tup: (PredicateType, RelationId)): String =
    val name = rIdToName.getOrElse(tup._2, s"<${tup._2}>")
    s"${if (tup._1 == PredicateType.NEGATED) "!" else ""}$name"

  def update(key: String, value: RelationId): Unit = {
    nameToRid(key) = value
    rIdToName(value) = key
  }
  def update(key: RelationId, value: String): Unit = {
    rIdToName(key) = value
    nameToRid(value) = key
  }
  def contains(key: String): Boolean = nameToRid.contains(key)
  def contains(key: RelationId): Boolean = rIdToName.contains(key)
  def rIds(): Iterable[RelationId] = rIdToName.keys
  def hashToAtom(hash: String): String =
    val h = hash.split("\\.").toSeq
    val head = h.head.replace("!", "")
    val neg = if (h.head.contains("!")) "!" else ""
//    s"${rIdToName(h.head.toInt)}${h.drop(1).mkString("(", ", ", ")")}"
    s"$neg${rIdToName(head.toInt)}.${h(1)}"
}

enum StorageAggOp:
  case SUM, COUNT, MIN, MAX

enum StorageComparison:
  case EQ, NEQ, LT, LTE, GT, GTE

enum StorageExpression:
  case One(t: Either[StorageConstant, Int])
  case Add(l: StorageExpression, r: Either[StorageConstant, Int])
  case Sub(l: StorageExpression, r: Either[StorageConstant, Int])
  case Mul(l: StorageExpression, r: Either[StorageConstant, Int])
  case Div(l: StorageExpression, r: Either[StorageConstant, Int])
  case Mod(l: StorageExpression, r: Either[StorageConstant, Int])


inline def getType(x: StorageConstant): Char = x match
    case _: Int => 'i'
    case _: String => 's'

def buildComparison(sc: StorageComparison, tpe: Char): (StorageConstant, StorageConstant) => Boolean =
  import StorageComparison.*
  sc match
    case EQ =>
      tpe match
        case 'i' => (x, y) => x.asInstanceOf[Int] == y.asInstanceOf[Int]
        case 's' => (x, y) => x.asInstanceOf[String] == y.asInstanceOf[String]
    case NEQ =>
      tpe match
        case 'i' => (x, y) => x.asInstanceOf[Int] != y.asInstanceOf[Int]
        case 's' => (x, y) => x.asInstanceOf[String] != y.asInstanceOf[String]
    case LT =>
      tpe match
        case 'i' => (x, y) => x.asInstanceOf[Int] < y.asInstanceOf[Int]
        case 's' => (x, y) => x.asInstanceOf[String] < y.asInstanceOf[String]
    case LTE =>
      tpe match
        case 'i' => (x, y) => x.asInstanceOf[Int] <= y.asInstanceOf[Int]
        case 's' => (x, y) => x.asInstanceOf[String] <= y.asInstanceOf[String]
    case GT =>
      tpe match
        case 'i' => (x, y) => x.asInstanceOf[Int] > y.asInstanceOf[Int]
        case 's' => (x, y) => x.asInstanceOf[String] > y.asInstanceOf[String]
    case GTE =>
      tpe match
        case 'i' => (x, y) => x.asInstanceOf[Int] >= y.asInstanceOf[Int]
        case 's' => (x, y) => x.asInstanceOf[String] >= y.asInstanceOf[String]

def buildExpression(se: StorageExpression, tpe: Char): (Int => StorageTerm) => StorageConstant =
  import StorageExpression.*
  tpe match
    case 'i' =>
      def aux(se: StorageExpression, get: Int => StorageTerm): Int =
        se match
          case One(t) => t.fold(x => x.asInstanceOf[Int], x => get(x).asInstanceOf[Int])
          case Add(l, r) => aux(l, get) + r.fold(x => x.asInstanceOf[Int], x => get(x).asInstanceOf[Int])
          case Sub(l, r) => aux(l, get) - r.fold(x => x.asInstanceOf[Int], x => get(x).asInstanceOf[Int])
          case Mul(l, r) => aux(l, get) * r.fold(x => x.asInstanceOf[Int], x => get(x).asInstanceOf[Int])
          case Div(l, r) => aux(l, get) / r.fold(x => x.asInstanceOf[Int], x => get(x).asInstanceOf[Int])
          case Mod(l, r) => aux(l, get) % r.fold(x => x.asInstanceOf[Int], x => get(x).asInstanceOf[Int])
      g => aux(se, g)
    case 's' =>
      def aux(se: StorageExpression, get: Int => StorageTerm): String =
        se match
          case One(t) => t.fold(x => x.asInstanceOf[String], x => get(x).asInstanceOf[String])
          case Add(l, r) => aux(l, get) + r.fold(x => x.asInstanceOf[String], x => get(x).asInstanceOf[String])
          case Sub(l, r) => ???
          case Mul(l, r) => ???
          case Div(l, r) => ???
          case Mod(l, r) => ???
      g => aux(se, g)
            
