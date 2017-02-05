package com.github.ctp.state

import akka.actor.Actor
import com.github.ctp.state.dto.State
import com.google.inject.BindingAnnotation
import com.typesafe.scalalogging.LazyLogging
import net.jcazevedo.moultingyaml._

import scala.annotation.StaticAnnotation

class StateSerializerActor extends Actor with LazyLogging {
  override def receive: Receive = {
    case state: State =>
      val stateString = StateMoultingFormats.stateFormat.write(state).prettyPrint
      logger.debug(s"serialized state is $stateString")
      sender ! SerializedState(stateString)
    case DeserializeState(str) =>
      logger.debug(s"deserializing state: $str")
      val state = str match {
        case "" => State(List())
        case _ => StateMoultingFormats.stateFormat.read(str.parseYaml)
      }
      sender ! state
  }
}

@BindingAnnotation
class StateSerializer extends StaticAnnotation