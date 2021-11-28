package datalog

trait AbstractProgram {

}
type Constant = Int | String
trait Variable {
  val oid: Int
}
type Term = Constant | Variable
trait Atom {
  def :- (body: Atom*): Unit
  def :- (body: Unit): Unit
  val rId: Int
  val terms: IndexedSeq[Term]
}
