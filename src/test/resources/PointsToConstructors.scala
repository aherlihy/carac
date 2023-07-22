package pointstoconstructors

class MyObject

trait T(arg: MyObject) {
  def get() = arg
}

class A(val argA: MyObject) extends T(argA) {
  val x = argA
  val y = x
}

class B(override val argA: MyObject, val argB: MyObject) extends A(argB)

object Main {
  def main(): Unit = {
    val o1 = MyObject()
    val o2 = MyObject()

    val b = B(o1, o2)

    val b_argA = b.argA // o1 (cannot be o2 because it is overriden)
    val b_argB = b.argB // o2

    // here the constructor argument argA of A is used, not the (overriden) field
    val b_get = b.get() // o2
  }
}