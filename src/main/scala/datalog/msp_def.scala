package datalog

import scala.quoted.*

//inline def power(x: Double, inline n: Int) = ${ powerCode('x, 'n) }
//
//def powerCode(x: Expr[Double], n: Expr[Int])(using Quotes): Expr[Double] =
//  powerCode(x, n.valueOrError)
//
//def powerCode(x: Expr[Double], n: Int)(using Quotes): Expr[Double] =
//  if n == 0 then '{ 1.0 }
//  else if n % 2 == 0 then '{ val y = $x * $x; ${ powerCode('y, n / 2) } }
//  else '{ $x * ${ powerCode(x, n - 1) } }

//package datalog

//import scala.quoted.*

inline def power(x: Double, inline n: Int): Double = ${ powerCode('x, 'n) }

def powerN(n: Int): Double => Double =
  staging.run('{ (x: Double) => ${ efficientPowerCode('x, n) } })

/** Generate code to compute x^n (possibly optimized) */
def powerCode(x: Expr[Double], n: Expr[Int])(using Quotes): Expr[Double] =
  n.value match
    case Some(knownN) => efficientPowerCode(x, knownN)
    case None => '{ Math.pow($x, $n.toDouble) }

/** Generate code to compute x^n for a particular n */
def efficientPowerCode(x: Expr[Double], n: Int)(using Quotes): Expr[Double] =
  if n == 0 then '{ 1.0 }
  else '{ $x * ${ efficientPowerCode(x, n - 1) } } // x * ... * x * 1
