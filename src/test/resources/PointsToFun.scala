package pointstofun

abstract class A

class A1() extends A
class A2() extends A

object Main {
  def main() = {
    val p = PointsToFun()
    p.fun1()
    p.fun2()
  }
}

class PointsToFun {
  def fun1() = {
    val a1 = A1() // A1()#0
    val b1 = this.id(a1)
  }

  def fun2() = {
    val a2 = A2() // A2()#0
    val b2 = this.id(a2)
  }

  def id(a: A): A = a
}
