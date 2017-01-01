package com.github.ctp.state

import akka.actor.Actor
import com.github.ctp.state.dto.State
import com.typesafe.scalalogging.LazyLogging

class StateSerializerActor extends Actor with LazyLogging {
  override def receive: Receive = {
    case state: State =>
      val stateString = StateMoultingFormats.stateFormat.write(state).prettyPrint
      logger.debug(s"serialized state is $stateString")
      sender ! SerializedState(stateString)
  }
}
