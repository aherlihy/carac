package datalog

class Program(using ee: ExecutionEngine) extends AbstractProgram {

  var varCounter = 0
  case class Variable() extends datalog.Variable {
    val oid = varCounter
    varCounter += 1
    override def toString = "v" + oid
  }


  var relCounter = 0

  /**
   * TODO: will need unique name for users to identify relations read from elsewhere
   * TODO: let relations take lists of types
   *
   * @tparam T
   */
  case class Relation[T <: Constant](/* uniqueName: String = "" */) {
    type RelTerm = T | Variable

    val oid = relCounter
    relCounter += 1
    ee.initRelation(oid)

    case class RelAtom(terms: IndexedSeq[RelTerm]) extends Atom { // extend Atom so :- can accept atom of any Relation
      val rId = oid
      // IDB tuple
      def :-(body: Atom*): Unit = {
        ee.insertIDB(this, body.toIndexedSeq)
      }

      // EDB tuple
      def :-(body: Unit): Unit = ee.storageManager.insertEDB(this)

      override def toString = oid + terms.mkString("(", ", ", ")")
    }
    // Create tuple of relation
    def apply(ts: RelTerm*): RelAtom = {
      new RelAtom(ts.toIndexedSeq)
    }
  }
  def solve(): Any = ee.solve() // TODO: get rid of any
}

// TODO: do i need a separate trait + class here?
//class SimpleProgram(using ee: ExecutionEngine) extends Program {}

@main def main = {
  given engine: ExecutionEngine = new SimpleEE

  val program = Program()
  val e = program.Relation[Int]()
  val p = program.Relation[Int]()

  val x = program.Variable()
  val y = program.Variable()
  val z = program.Variable()

  e(1, 2) :- () // TODO: remove () and use context param?
  e(2, 3) :- ()
  p(x, y) :- e(x, y)
  p(x, z) :- ( e(x, y), p(y, z) )

  program.solve()

  val program2 = Program()
}
