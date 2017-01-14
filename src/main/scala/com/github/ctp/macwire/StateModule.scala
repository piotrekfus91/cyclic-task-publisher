package com.github.ctp.macwire

import akka.actor.{ActorRef, Props}
import com.github.ctp.state._
import com.softwaremill.macwire._
import com.softwaremill.tagging._

trait StateModule extends AkkaModule with UtilModule {
  lazy val stateFilePath = "~/.ctpState".taggedWith[StateFilePath]

  def stateSerializer(): @@[ActorRef, StateSerializer] = actorSystem
    .actorOf(Props(wire[StateSerializerActor]))
    .taggedWith[StateSerializer]

  def stateSaver(stateSerializer: ActorRef @@ StateSerializer): @@[ActorRef, StateSaver] = actorSystem
    .actorOf(Props(wire[StateSaverActor]))
    .taggedWith[StateSaver]
}
