package de.arnohaase.sysinfo

trait HasUserRepresentation{
   def userRepresentation : String
}

case class ScalarType (name: String, unit: String, numFracDigits: Int) {
  val factor = Math.pow (10, -numFracDigits)
  def format (v: Long) = s"$name: ${v*factor} $unit"
}


case class ScalarValue (value: Long, tpe: ScalarType) extends HasUserRepresentation{
  override def userRepresentation = tpe.format (value)
}
