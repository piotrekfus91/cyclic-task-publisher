package com.github.ctp.state

import akka.actor.{Actor, ActorRef}
import com.github.ctp.state.dto.{State, StateTask}
import com.github.ctp.util.FileHelper
import com.google.inject.BindingAnnotation
import com.typesafe.scalalogging.LazyLogging

import scala.annotation.StaticAnnotation

class StateSaverActor(@StateSerializer private val stateSerializer: ActorRef, fileHelper: FileHelper,
                      filePath: String) extends Actor with LazyLogging {
  private var state: State = State(List())

  override def preStart(): Unit = {
    super.preStart()
    stateSerializer ! DeserializeState(fileHelper.read(filePath))
  }

  override def receive: Receive = {
    case GetLastExecutionTime(user, description) =>
      state.tasks.find(stateTask => stateTask.user == user && stateTask.description == description) match {
        case None =>sender ! NoExecutionYet(user, description)
        case Some(stateTask) => sender ! LastExecutionTime(user, description, stateTask.last)
      }
    case newState: State =>
      state = newState
    case Flush =>
      stateSerializer ! state
    case SerializedState(stateStr) =>
      fileHelper.write(stateStr, filePath)
    case stateTask: StateTask =>
      state.tasks.find(it => it.user == stateTask.user && it.description == stateTask.description) match {
        case None => state = state.copy(tasks = state.tasks :+ stateTask)
        case Some(existingStateTask) => existingStateTask.last = stateTask.last
      }
  }
}

@BindingAnnotation
class StateSaver extends StaticAnnotation