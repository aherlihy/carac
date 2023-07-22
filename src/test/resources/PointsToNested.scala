package pointstonested

class A()

object Main {
  def main() = {
    def fun() = {
      def fun() = A()
      fun()
    }

    val a = fun()

    val b = {
      def fun() = A()
      fun()
    }

    val c = fun()
  }
}