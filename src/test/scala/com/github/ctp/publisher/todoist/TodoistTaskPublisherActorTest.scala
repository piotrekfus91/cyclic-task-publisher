package com.github.ctp.publisher.todoist

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import com.github.ctp.config.domain.{Task, TodoistUser, UserData}
import com.github.ctp.logger.CtpLogger
import com.github.ctp.publisher.Publish
import com.github.ctp.publisher.todoist.dto.Project
import com.github.ctp.publisher.todoist.service.{TodoistHttpRunner, ProjectListManager}
import com.github.ctp.util.UuidGenerator
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class TodoistTaskPublisherActorTest extends FlatSpec with MockFactory {
  "Todoist task publisher" should "build correct JSON" in {
    val uuid = "c1c8d910-cf92-11e6-bf26-cec0c932ce01"
    val userData = UserData("userName", Some(TodoistUser(enabled = true, Some("12345678"))))

    val httpRunner = mock[TodoistHttpRunner]
    val uuidGenerator = stub[UuidGenerator]
    val projectListManager = stub[ProjectListManager]
    val ctpLogger = stub[CtpLogger]

    projectListManager.getUserProjectByName _ when(userData, "test project") returns Some(Project("test project", 123456L))
    (uuidGenerator.uuid _).when().returns(uuid)
    (httpRunner.publishTask _).expects(userData,
      """[{
        |  "type": "item_add",
        |  "temp_id": "c1c8d910-cf92-11e6-bf26-cec0c932ce01",
        |  "uuid": "c1c8d910-cf92-11e6-bf26-cec0c932ce01",
        |  "args": {
        |    "content": "desc",
        |    "project_id": 123456
        |  }
        |}]""".stripMargin).returns(Right())

    val actorSystem = ActorSystem("test")

    val sut = actorSystem.actorOf(Props(new TodoistTaskPublisherActor(projectListManager, httpRunner, uuidGenerator, ctpLogger)))

    sut ! Publish(userData, Task("desc", "test project"))

    Thread.sleep(100)

    actorSystem.terminate()
    Await.result(actorSystem.whenTerminated, Duration(100, TimeUnit.MILLISECONDS))
  }
}
