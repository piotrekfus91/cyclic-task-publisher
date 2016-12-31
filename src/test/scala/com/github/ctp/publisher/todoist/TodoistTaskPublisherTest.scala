package com.github.ctp.publisher.todoist

import com.github.ctp.domain.{Task, TodoistUser, UserData}
import com.github.ctp.publisher.todoist.dto.Project
import com.github.ctp.publisher.todoist.service.{HttpRunner, ProjectListManager}
import com.github.ctp.util.UuidGenerator
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec

class TodoistTaskPublisherTest extends FlatSpec with MockFactory {
  "Todoist task publisher" should "build correct JSON" in {
    val uuid = "c1c8d910-cf92-11e6-bf26-cec0c932ce01"
    val userData = UserData("userName", Some(TodoistUser(enabled = true, Some("12345678"))))

    val httpRunner = mock[HttpRunner]
    val uuidGenerator = stub[UuidGenerator]
    val projectListManager = stub[ProjectListManager]

    projectListManager.getUserProjectByName _ when(userData, "test project") returns Some(Project("test project", 123456L))
    (uuidGenerator.uuid _).when().returns(uuid)
    (httpRunner.publishTask _).expects(userData,
      """[{
        |  "commandType": "item_add",
        |  "temp_id": "c1c8d910-cf92-11e6-bf26-cec0c932ce01",
        |  "uuid": "c1c8d910-cf92-11e6-bf26-cec0c932ce01",
        |  "args": {
        |    "content": "desc",
        |    "project_id": 123456
        |  }
        |}]""".stripMargin).returns(Right)

    val sut = new TodoistTaskPublisher(projectListManager, httpRunner, uuidGenerator)

    sut.publish(userData, Task("desc", "test project"))

  }
}
