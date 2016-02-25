package com.ajjpj.asysmon.data

import java.text.DecimalFormat

/**
 * @author arno
 */
case class AFixedPointNumber (integer: Long, numFracDigits: Int) {
  val value = integer / AFixedPointNumber.pow (numFracDigits)
  def asString = {
    val nf = new DecimalFormat()
    nf.setMinimumFractionDigits (numFracDigits)
    nf.setMaximumFractionDigits (numFracDigits)
    nf.format (value)
  }
}

object AFixedPointNumber {
  private val pow = (0 to 30).map (Math.pow (10, _))
}
