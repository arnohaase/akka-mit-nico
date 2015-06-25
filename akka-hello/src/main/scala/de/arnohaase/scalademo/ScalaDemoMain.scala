package de.arnohaase.scalademo

/**
 * @author arno
 */

import java.nio.charset.Charset

object Expr {
  implicit def intAsExpr (i: Int): Expr = Literal (i)
  implicit def stringAsExpr (v: String): Expr = VarExpr (v)

}
sealed trait Expr
case class Literal(v: Int) extends Expr
case class Binary(e1: Expr, op: String, e2: Expr) extends Expr
case class VarExpr(v: String) extends Expr


/**
 * @author arno
 */
object ScalaDemoMain extends App {
  import Expr._

  val e = Binary (Binary (1, "+", 7), "*", Binary ("x", "+", 3))
  val e2 = simplify (e)

  println (asString(e)  + " = " + eval (e,  Map ("x" -> 2)))
  println (asString(e2) + " = " + eval (e2, Map ("x" -> 2)))



  def asString (e: Expr) : String = {
    def optParen (e: Expr) = e match {
      case Binary (_,_,_) => "(" + asString (e) + ")"
      case _              => asString (e)
    }

    e match {
      case Literal (i) => i.toString
      case VarExpr (v) => v
      case Binary (left, oper, right) => optParen(left) +" "+oper+" "+optParen(right)
    }
  }



  def simplify (e: Expr): Expr = e match {
    case Binary (Literal (i1), "+", Literal (i2)) => Literal (i1+i2)
    case Binary (left, op, right) => Binary (simplify (left), op, simplify (right))
    case _ => e
  }

  def eval (e: Expr, varValues: Map[String, Int]): Int = e match {
    case Literal (i) => i
    case VarExpr (v) => varValues (v)
    case Binary (left, "+", right) => eval (left, varValues) + eval (right, varValues)
    case Binary (left, "*", right) => eval (left, varValues) * eval (right, varValues)
  }

  implicit val defaultCharset = Charset.forName("iso-8859-15")

  withEncoding (Charset.defaultCharset())
  withEncoding

  def withEncoding (implicit encoding: Charset) = {
    println ("processing with " + encoding)
  }


  def a[T] (v: T)(implicit t: ClassManifest[T]): Unit = {
    println (" with type " + t.runtimeClass)
  }

  a("a")
  a(1)


}

