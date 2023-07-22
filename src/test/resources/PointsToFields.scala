package pointstofields

class A

class MyObject(var x: A) {
  var a1 = A()
  var a2 = A()

  def fun1(): Unit =
    this.x = a1

  def fun2(): Unit =
    this.x = a2
}

object Main {
  def main(): Unit = {
    val initX = A()

    val o1 = MyObject(initX)
    val o2 = MyObject(initX)

    o1.fun1()
    o2.fun2()

    val a1 = o1.a1
    val a2 = o1.a2 

    val x1 = o1.x // We have [x1 -> a1] but do we also have [x1 -> a2]? Probably not
    val x2 = o2.x // 
  }
}