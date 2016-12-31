package com.github.ctp.publisher.todoist

import com.github.ctp.domain.{Task, UserData}
import com.github.ctp.publisher.TaskPublisher
import com.github.ctp.publisher.todoist.dto.{Command, Project, TodoistTask}
import com.github.ctp.publisher.todoist.service.TodoistJsonProtocol._
import com.github.ctp.publisher.todoist.service.{HttpRunner, ProjectListManager}
import com.github.ctp.util.UuidGenerator
import com.typesafe.scalalogging.LazyLogging
import spray.json._

class TodoistTaskPublisher(private val projectListManager: ProjectListManager,
                           private val httpRunner: HttpRunner,
                           private val uuidGenerator: UuidGenerator) extends TaskPublisher with LazyLogging {

  override def publish(userData: UserData, task: Task): Unit = {
    logger.info(s"publishing $task for ${userData.name}")
    projectListManager.refreshUserProjects(userData)
    val maybeProject = projectListManager.getUserProjectByName(userData, task.project)
    maybeProject.foreach(publishTask(userData, _, task))
  }

  private def publishTask(userData: UserData, project: Project, task: Task) = {
    val commands = List(
      Command(
        commandType = "item_add",
        temp_id = uuidGenerator.uuid(),
        uuid = uuidGenerator.uuid(),
        args = TodoistTask(task.description, project.id)
      )
    )

    val json = commands.toJson.prettyPrint
    httpRunner.publishTask(userData, json) match {
      case Right => logger.info(s"task ${task.description} of user ${userData.name} published")
      case Left => logger.error(s"task ${task.description} of user ${userData.name} not published")
    }
  }
}
