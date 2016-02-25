package com.ajjpj.asysmon

import akka.actor.ActorSystem

/**
 * @author arno
 */
trait ASysMon {
  def shutdown(): Unit
}

object ASysMon {
  lazy val theInstance = ASysMon()
  def apply(): ASysMon = new ASysMonImpl()

  class ASysMonImpl extends ASysMon {
    private val system = ActorSystem ("ASysMon")

    override def shutdown() {
      system.terminate()
    }
  }
}

