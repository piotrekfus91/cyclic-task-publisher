package com.github.ctp.publisher.todoist

import java.util.UUID

import akka.actor.ActorSystem
import com.github.ctp.domain.{Task, TodoistUser, UserData}
import com.github.ctp.guice._
import com.github.ctp.publisher.Publish
import com.github.ctp.publisher.todoist.service.ProjectListManager
import com.github.ctp.test.TodoistIntegrationTestContext._
import com.google.inject.Guice
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

class TodoistTaskPublisherActorIntegrationTest extends FlatSpec with Matchers with BeforeAndAfterEach {
  val guice = Guice.createInjector(
    new AkkaModule,
    new LoggerModule,
    new ConfigModule,
    new TodoistPublisherModule
  )

  val actorSystem = guice.getInstance(classOf[ActorSystem])

  val projectListManager = guice.getInstance(classOf[ProjectListManager])
  val todoistTaskPublisher = actorSystem.actorOf(GuiceAkkaExtension(actorSystem).props(ActorName[TodoistTaskPublisher]))

  val userData = UserData("user", Some(TodoistUser(enabled = true, apiToken = Some(todoistApiToken.getOrElse("")))))
  val taskName = UUID.randomUUID().toString
  val task = Task(taskName, todoistTestProject)

  override protected def beforeEach(): Unit = {
    assume(todoistAvailable)

    createTestProject()
  }

  "Todoist task publisher" should "create project" in {
    projectListManager.refreshUserProjects(userData)
    projectListManager.getUserProjectByName(userData, todoistTestProject) shouldNot be(empty)
  }

  it should "create task in real Todoist" in {
    todoistTaskPublisher ! Publish(userData, task)

    actorSystem.stop(todoistTaskPublisher)

    getAllTasks should include(taskName)
  }

  override protected def afterEach(): Unit = {
    assume(todoistAvailable)

    deleteTestProject()
  }
}
