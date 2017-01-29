package com.github.ctp.publisher.todoist

import akka.actor.Actor
import com.github.ctp.config.domain.{Task, UserData}
import com.github.ctp.logger.CtpLogger
import com.github.ctp.publisher.Publish
import com.github.ctp.publisher.todoist.dto.{Command, Project, TodoistTask}
import com.github.ctp.publisher.todoist.service.TodoistJsonProtocol._
import com.github.ctp.publisher.todoist.service.{TodoistHttpRunner, ProjectListManager}
import com.github.ctp.util.UuidGenerator
import com.google.inject.{BindingAnnotation, Inject}
import com.typesafe.scalalogging.LazyLogging
import spray.json._

import scala.annotation.StaticAnnotation

class TodoistTaskPublisherActor @Inject()(private val projectListManager: ProjectListManager,
                                          private val httpRunner: TodoistHttpRunner,
                                          private val uuidGenerator: UuidGenerator,
                                          private val ctpLogger: CtpLogger) extends Actor with LazyLogging {

  override def receive: Receive = {
    case Publish(userData, task) => publish(userData, task)
  }

  private def publish(userData: UserData, task: Task): Unit = {
    logger.info(s"publishing $task for ${userData.name}")
    projectListManager.refreshUserProjects(userData)
    val maybeProject = projectListManager.getUserProjectByName(userData, task.project)
    maybeProject.foreach(publishTask(userData, _, task))
  }

  private def publishTask(userData: UserData, project: Project, task: Task) = {
    val commands = List(
      Command(
        commandType = "item_add",
        tempId = uuidGenerator.uuid(),
        uuid = uuidGenerator.uuid(),
        todoistTask = TodoistTask(task.description, project.id)
      )
    )

    val json = commands.toJson.prettyPrint
    val msg = httpRunner.publishTask(userData, json) match {
      case Right(_) => s"task ${task.description} of user ${userData.name} published"
      case Left(_) => s"task ${task.description} of user ${userData.name} not published"
    }
    ctpLogger.log(msg)
  }
}

@BindingAnnotation
class TodoistTaskPublisher extends StaticAnnotation