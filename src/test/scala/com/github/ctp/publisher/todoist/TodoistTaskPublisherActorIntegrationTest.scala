package com.github.ctp.publisher.todoist

import java.util.UUID
import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import com.github.ctp.config.domain.{Task, TodoistUser, UserData}
import com.github.ctp.guice._
import com.github.ctp.publisher.todoist.service.ProjectListManager
import com.github.ctp.publisher.{Publish, PublisherSequence}
import com.github.ctp.test.TodoistIntegrationTestContext._
import com.google.inject.Guice
import org.scalatest._
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Millis, Seconds, Span}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class TodoistTaskPublisherActorIntegrationTest extends FlatSpec with Matchers with BeforeAndAfterEach
    with BeforeAndAfterAll with Eventually {

  val guice = Guice.createInjector(
    new AkkaModule,
    new LoggerModule,
    new ConfigModule,
    new UtilModule,
    new TodoistPublisherModule
  )

  val actorSystem = guice.getInstance(classOf[ActorSystem])

  val projectListManager = guice.getInstance(classOf[ProjectListManager])
  val todoistTaskPublisher = actorSystem.actorOf(GuiceAkkaExtension(actorSystem).props(ActorName[TodoistTaskPublisher]))

  val userData = UserData("user", Some(TodoistUser(enabled = true, apiToken = Some(todoistApiToken.getOrElse("")))))
  val taskName = UUID.randomUUID().toString
  val task = Task(taskName, todoistTestProject)

  override protected def beforeEach(): Unit = {
    createTestProject()
  }

  "Todoist task publisher" should "create project" taggedAs TodoistMustBeAvailable in {
    projectListManager.refreshUserProjects(userData)
    projectListManager.getUserProjectByName(userData, todoistTestProject) shouldNot be(empty)
  }

  it should "create task in real Todoist" taggedAs TodoistMustBeAvailable in {
    todoistTaskPublisher ! Publish(userData, task, PublisherSequence(List()))

    eventually(timeout(Span(2, Seconds)), interval(Span(100, Millis))) {
      getAllTasks should include(taskName)
    }
  }

  override protected def afterEach(): Unit = {
    deleteTestProject()
  }

  override protected def afterAll(): Unit = {
    actorSystem.terminate()
    Await.ready(actorSystem.whenTerminated, Duration(1, TimeUnit.SECONDS))
  }
}

object TodoistMustBeAvailable extends Tag(if(todoistAvailable) "" else classOf[Ignore].getName)