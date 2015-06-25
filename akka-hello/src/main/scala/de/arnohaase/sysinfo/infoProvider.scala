package de.arnohaase.sysinfo

import akka.actor.Actor
import akka.actor.Actor.Receive

/**
 * @author arno
 */

case object GiveMeData

class InfoProviderActor extends Actor {
  val curTimeType = ScalarType ("Current time", "ms", 0)

  override def receive = {
    case GiveMeData => sender ! Map (curTimeType -> ScalarValue (System.currentTimeMillis(), curTimeType))
  }
}
