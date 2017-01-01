package com.github.ctp.publisher.todoist

import java.util.UUID

import com.github.ctp.domain.{Task, TodoistUser, UserData}
import com.github.ctp.macwire.TodoistPublisherModule
import com.github.ctp.test.TodoistIntegrationTestContext._
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

class TodoistTaskPublisherIntegrationTest extends FlatSpec with Matchers with BeforeAndAfterEach {
  val modules = new TodoistPublisherModule {}
  val projectListManager = modules.projectListManager
  val todoistTaskPublisher = modules.todoistTaskPublisher

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
    todoistTaskPublisher.publish(userData, task)

    getAllTasks should include(taskName)
  }

  override protected def afterEach(): Unit = deleteTestProject()
}
