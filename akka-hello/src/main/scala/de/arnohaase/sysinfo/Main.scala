package de.arnohaase.sysinfo

import akka.actor.{Props, Inbox, ActorSystem}
import scala.concurrent.duration._



/**
 * @author arno
 */
object Main extends App {

  // Create the actor system
  val system = ActorSystem("SystemInfoSystem")
  val inbox = Inbox.create(system)
  val currentTimeProvider = system.actorOf(Props[CurrentTimeActor] , "CurrentTime")
  val infoProvider = system.actorOf(Props (new InfoProviderActor (List (currentTimeProvider))) , "InfoProvider")

  inbox.send(infoProvider, GiveMeData)
  try {
    val data = inbox.receive(5 seconds)
    data match {
      case m: Map[ScalarType, ScalarValue] => println ("Yeah! " + m)
    }
  }
  finally {
    system.shutdown()
  }
}
