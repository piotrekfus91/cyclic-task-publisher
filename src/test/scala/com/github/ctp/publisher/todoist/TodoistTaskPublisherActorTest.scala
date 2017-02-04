package com.github.ctp.publisher.todoist

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import com.github.ctp.config.domain.{Task, TodoistUser, UserData}
import com.github.ctp.logger.CtpLogger
import com.github.ctp.publisher.{Publish, PublisherSequence}
import com.github.ctp.publisher.todoist.dto.Project
import com.github.ctp.publisher.todoist.service.{ProjectListManager, TodoistHttpRunner}
import com.github.ctp.scheduler.Retrigger
import com.github.ctp.test.TestDateTimeProvider
import com.github.ctp.util.UuidGenerator
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpecLike

class TodoistTaskPublisherActorTest extends TestKit(ActorSystem("test")) with FlatSpecLike with MockFactory
    with TestDateTimeProvider {

  val uuid = "c1c8d910-cf92-11e6-bf26-cec0c932ce01"
  val userData = UserData("userName", Some(TodoistUser(enabled = true, Some("12345678"))))
  val task = Task("desc", "test project")
  val ctpLogger = stub[CtpLogger]

  "Todoist task publisher" should "build correct JSON" in {
    val httpRunner = mock[TodoistHttpRunner]
    val uuidGenerator = stub[UuidGenerator]
    val projectListManager = stub[ProjectListManager]
    val ctpLogger = stub[CtpLogger]
    val retriggeringService = TestProbe()

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


    val sut = system.actorOf(Props(new TodoistTaskPublisherActor(
        projectListManager, httpRunner, uuidGenerator, dateTimeProvider, retriggeringService.ref, ctpLogger)))

    sut ! Publish(userData, task, PublisherSequence(List()))

    Thread.sleep(100)
  }

  it should "send retriggering message" in {
    val httpRunner = stub[TodoistHttpRunner]
    val uuidGenerator = stub[UuidGenerator]
    val projectListManager = stub[ProjectListManager]
    val ctpLogger = stub[CtpLogger]
    val retriggeringService = TestProbe()

    projectListManager.getUserProjectByName _ when(userData, "test project") returns Some(Project("test project", 123456L))
    (uuidGenerator.uuid _).when().returns(uuid)
    (httpRunner.publishTask _).when(*, *).returns(Right())

    val sut = system.actorOf(Props(new TodoistTaskPublisherActor(
      projectListManager, httpRunner, uuidGenerator, dateTimeProvider, retriggeringService.ref, ctpLogger)))

    sut ! Publish(userData, task, PublisherSequence(List()))

    retriggeringService.expectMsg(Retrigger("userName", "desc", "todoist", dateTimeProvider.dateTime))
  }

  it should "propagate publishing to next actor" in {
    val httpRunner = stub[TodoistHttpRunner]
    val uuidGenerator = stub[UuidGenerator]
    val projectListManager = stub[ProjectListManager]
    val ctpLogger = stub[CtpLogger]
    val retriggeringService = TestProbe()
    val nextPublisher = TestProbe()

    projectListManager.getUserProjectByName _ when(userData, "test project") returns Some(Project("test project", 123456L))
    (uuidGenerator.uuid _).when().returns(uuid)
    (httpRunner.publishTask _).when(*, *).returns(Right())

    val sut = system.actorOf(Props(new TodoistTaskPublisherActor(
      projectListManager, httpRunner, uuidGenerator, dateTimeProvider, retriggeringService.ref, ctpLogger)))

    sut ! Publish(userData, task, PublisherSequence(List(nextPublisher.ref)))

    nextPublisher.expectMsg(Publish(userData, task, PublisherSequence(List())))
  }
}
