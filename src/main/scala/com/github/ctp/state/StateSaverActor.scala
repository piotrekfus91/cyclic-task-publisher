package com.github.ctp.state

import akka.actor.{Actor, ActorRef}
import com.github.ctp.state.dto.State
import com.github.ctp.util.FileHelper
import com.softwaremill.tagging.@@

class StateSaverActor(private val fileHelper: FileHelper, private val filePath: String @@ StateFilePath,
                      stateSerializer: ActorRef @@ StateSerializer) extends Actor {
  private var state: Option[State] = None

  override def preStart(): Unit = {
    super.preStart()
    stateSerializer ! DeserializeState(fileHelper.read(filePath))
  }

  override def receive: Receive = {
    case state: State =>
      this.state = Some(state)
    case Flush =>
      stateSerializer ! State
    case SerializedState(stateStr) =>
      fileHelper.write(stateStr, filePath)
  }
}

trait StateSaver
trait StateFilePath