package de.arnohaase.sysinfo

import akka.actor.{Props, ActorRef, Actor}
import akka.actor.Actor.Receive
import akka.actor.FSM.Failure
import akka.pattern.Patterns

import scala.concurrent.duration.FiniteDuration
import scala.util.Success
import scala.concurrent.duration._

/**
 * @author arno
 */

case object GiveMeData
case object Finished
case class SystemDataRequest (client: ActorRef, timeout: FiniteDuration)

class InfoProviderActor (scalarProviders: List[ActorRef]) extends Actor { //TODO dynamic (un)registration of scalar providers?
  override def receive = {
    case GiveMeData =>
      val client = sender()
      val props = Props(new InfoDataCollector (client, scalarProviders, 1 seconds)) //TODO clean up timeout
      context.system.actorOf (props)
  }
}

class InfoDataCollector (client: ActorRef, scalarProviders: List[ActorRef], timeout: FiniteDuration) extends Actor {
  var numResponses = 0
  var result = Map[ScalarType, ScalarValue]()

  scalarProviders.foreach (_ ! GiveMeData)

  val timeoutHandle = context.system.scheduler.scheduleOnce(timeout, context.self, Finished)(context.system.dispatcher)

  override def receive = {
    case m: Map[ScalarType, ScalarValue] =>
      println ("received partial data: " + m)
      result ++= m
      numResponses += 1
      if (numResponses == scalarProviders.size) {
        timeoutHandle.cancel()
        self ! Finished
      }
    case Finished =>
      println ("finished - sending response to " + client)
      client ! result
      context.stop(self)
  }
}

class CurrentTimeActor extends Actor {
  val curTimeType = ScalarType ("Current time", "ms", 0)

  override def receive = {
    case GiveMeData =>
      println ("received request for time data")
      sender ! Map (curTimeType -> ScalarValue (System.currentTimeMillis(), curTimeType))
  }
}
