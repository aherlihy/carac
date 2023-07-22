package pointstoarguments

class MyObject

trait T {
  def test(): MyObject
}

class Pair[A, B](val v1: A, val v2: B) {
  def first: A = v1
  def second: B = v2
  def swap: Pair[B, A] = Pair(v2, v1)
}

class TPair[A <: T, B <: T](v1: A)(v2: B) extends Pair(v1, v2) {
  def meth1 = v1.test()
  def meth2 = v2.test()
}

class C1 extends T {
  override def test(): MyObject = MyObject()
}

class C2 extends T {
  override def test(): MyObject = MyObject()
}


object Main {
  def main(): Unit = {
    def fun[A <: T, B <: T, C <: T](x: A)(y: B)(z: C) = {
      val t1 = z.test()
      val t2 = y.test()
      val t3 = x.test()
      t1
    }

    val c1 = C1()
    val c2 = C2()

    val res = fun(c1)(c2)(c2)

    val p1 = Pair(0, 0)
    val p2 = Pair(c1, c2)
    val p3 = TPair(c1)(c2)
    // val swap = p2.swap

    val p1_v1 = p1.first
    val p1_v2 = p2.second

    val p2_v1 = p2.first
    val p2_v2 = p2.second

    val p3_v1 = p3.first
    val p3_v2 = p3.second
    val p3_t1 = p3.meth1
    val p3_t2 = p3.meth2

    // val swap_v1 = swap.v1
    // val swap_v2 = swap.v2

    val default = C1()
    val c11 = C1()

    def fun1(x: T = default) = x
    def fun2(x: T = default, y: T) = x
    def fun3(x: T = default, y: T) = x

    val res1 = fun1()
    val res2 = fun2(y = C1(), x = default)
    val res3 = fun3(c11, default)
  }  
}
