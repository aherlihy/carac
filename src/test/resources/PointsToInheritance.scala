package pointstoinheritance

abstract class MyObject
class MyObjectA extends MyObject
class MyObjectB extends MyObject

trait AA {
  val x: MyObject
  val traitVal: MyObject = MyObjectA()
}

class A {
  val a_instance = MyObjectA()

  val x: MyObject = a_instance
  val xx: MyObject = a_instance

  def test(): MyObject = a_instance

  def test2: MyObject = a_instance

  private def privateTest(): MyObject = a_instance

  def withPrivateCall(): MyObject = privateTest()

  def withNonPrivateCall(): MyObject = test()

  // we use another version when calling from B to make testing more convenient
  def withPrivateCallB(): MyObject = privateTest()

  def withNonPrivateCallB(): MyObject = test()

  def withPrivateCallC(): MyObject = privateTest()

  def withNonPrivateCallC(): MyObject = test()

  def withPrivateCallD(): MyObject = privateTest()

  def withNonPrivateCallD(): MyObject = test()
}

class B extends A with AA {
  val b_instance = MyObjectB()

  override val x = b_instance // overrides two values
  // no override for xx
  val sup = super.test()

  override def test(): MyObject = b_instance

  override val test2 = b_instance

  private def privateTest(): MyObject = b_instance
}

class C extends A {
  // make sure everything is from A
}

class D extends B {
  override val traitVal: MyObject = b_instance
  // make sure everything is from B and not A
}

object Main {
  def main(): Unit = {
    val a = A()
    val b = B()
    val c = C()
    val d = D()

    val a_instance = a.a_instance
    val b_instance = b.b_instance
    
    val a_x = a.x
    val a_xx = a.xx
    val a_t = a.test()
    val a_t2 = a.test2
    val a_pc = a.withPrivateCall()
    val a_npc = a.withNonPrivateCall()

    val b_x = b.x
    val b_xx = b.xx
    val b_t = b.test()
    val b_t2 = b.test2
    val b_pc = b.withPrivateCallB()
    val b_npc = b.withNonPrivateCallB()
    val b_sup = b.sup
    val b_traitVal = b.traitVal

    val c_x = c.x
    val c_xx = c.xx
    val c_t = c.test()
    val c_pc = c.withPrivateCallC()
    val c_npc = c.withNonPrivateCallC()

    val d_x = d.x
    val d_xx = d.xx
    val d_t = d.test()
    val d_pc = d.withPrivateCallD()
    val d_npc = d.withNonPrivateCallD()
    val d_sup = d.sup
    val d_traitVal = d.traitVal

  }
}