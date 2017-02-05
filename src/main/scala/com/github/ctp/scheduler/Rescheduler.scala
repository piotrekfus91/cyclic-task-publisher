package com.github.ctp.scheduler

import akka.actor.{Actor, ActorRef}
import com.github.ctp.publisher.Publish
import com.github.ctp.state.LastExecutionTime
import com.github.ctp.util.DateTimeProvider
import com.google.inject.BindingAnnotation

import scala.annotation.StaticAnnotation

class ReschedulerActor(@AkkaSchedulerEntryPoint akkaSchedulerEntryPoint: ActorRef,
                       dateTimeProvider: DateTimeProvider) extends Actor {
  override def receive: Receive = {
    case Publish(userData, task, _) =>
      akkaSchedulerEntryPoint ! LastExecutionTime(userData.name, task.description, dateTimeProvider.now)
  }
}

@BindingAnnotation
class Rescheduler extends StaticAnnotation
