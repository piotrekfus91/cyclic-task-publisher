package com.github.ctp.scheduler

import akka.actor.{Actor, ActorRef}
import com.github.ctp.state.LastExecutionTime
import com.google.inject.BindingAnnotation

import scala.annotation.StaticAnnotation

class RetriggeringServiceActor(@AkkaSchedulerEntryPoint akkaSchedulerEntryPoint: ActorRef) extends Actor {
  override def receive: Receive = {
    case Retrigger(userName, description, system, lastExecutionTime) =>
      akkaSchedulerEntryPoint ! LastExecutionTime(userName, description, Map(system -> lastExecutionTime))
  }
}

@BindingAnnotation
class RetriggeringService extends StaticAnnotation
