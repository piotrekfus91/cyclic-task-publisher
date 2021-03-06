package com.github.ctp.state

import java.time.LocalDateTime

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.github.ctp.state.dto.{State, StateTask}
import com.github.ctp.test.TestDateTimeProvider
import com.github.ctp.util.FileHelper
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpecLike, Matchers}

class StateSaverActorTest extends TestKit(ActorSystem("test")) with ImplicitSender with FlatSpecLike with Matchers
    with MockFactory with TestDateTimeProvider {

  val filePath = "/tmp/stateSaver.yml"
  val fileHelper = stub[FileHelper]
  val stateSerializer = TestProbe()

  "StateSaver" should "flush to disk" in {
    (fileHelper.read _).when(filePath).returns("some state")

    val sut = system.actorOf(Props(new StateSaverActor(stateSerializer.ref, fileHelper, filePath)))
    stateSerializer.expectMsg(DeserializeState("some state"))
    sut ! State(List(StateTask("user", "desc", nowDateTime)))

    sut ! Flush

    stateSerializer.expectMsg(State(List(StateTask("user", "desc", nowDateTime))))
  }

  it should "handle incoming state tasks of single user" in {
    (fileHelper.read _).when(filePath).returns("")

    val sut = system.actorOf(Props(new StateSaverActor(stateSerializer.ref, fileHelper, filePath)))
    stateSerializer.expectMsg(DeserializeState(""))

    sut ! State(List())

    val stateTask1 = StateTask("user", "desc 1", LocalDateTime.now)
    val stateTask2 = StateTask("user", "desc 2", LocalDateTime.now.plusDays(1))

    sut ! stateTask1
    sut ! stateTask2

    sut ! Flush

    stateSerializer.expectMsg(State(List(stateTask1, stateTask2)))
  }

  it should "handle incoming state tasks of multiple users" in {
    (fileHelper.read _).when(filePath).returns("")

    val sut = system.actorOf(Props(new StateSaverActor(stateSerializer.ref, fileHelper, filePath)))
    stateSerializer.expectMsg(DeserializeState(""))

    sut ! State(List())

    val dateTime1 = LocalDateTime.now
    val dateTime2 = LocalDateTime.now.plusDays(1)
    val dateTime3 = LocalDateTime.now.plusDays(2)
    val stateTask1 = StateTask("user", "desc 1", dateTime1)
    val stateTask2 = StateTask("user", "desc 2", dateTime3)
    val stateTask3 = StateTask("user2", "desc 1", dateTime2)
    val stateTask4 = StateTask("user2", "desc 3", dateTime3)

    List(stateTask1, stateTask2, stateTask3, stateTask4).foreach(sut ! _)

    sut ! Flush

    stateSerializer.expectMsg(State(List(stateTask1, stateTask2, stateTask3, stateTask4)))
  }

  it should "update last times of tasks" in {
    (fileHelper.read _).when(filePath).returns("")

    val sut = system.actorOf(Props(new StateSaverActor(stateSerializer.ref, fileHelper, filePath)))
    stateSerializer.expectMsg(DeserializeState(""))

    sut ! State(List())

    val dateTime1 = LocalDateTime.now
    val dateTime2 = LocalDateTime.now.plusDays(1)
    val dateTime3 = LocalDateTime.now.plusDays(2)
    val dateTime4 = LocalDateTime.now.plusDays(3)
    val stateTask1 = StateTask("user", "desc 1", dateTime1)
    val stateTask3 = StateTask("user2", "desc 1", dateTime1)
    val stateTask2 = StateTask("user", "desc 1", dateTime3)
    val stateTask4 = StateTask("user2", "desc 1", dateTime1)
    val stateTask5 = StateTask("user2", "desc 2", dateTime2)
    val stateTask6 = StateTask("user2", "desc 1", dateTime3)
    val stateTask7 = StateTask("user2", "desc 2", dateTime4)

    List(stateTask1, stateTask2, stateTask3, stateTask4, stateTask5, stateTask6, stateTask7).foreach(sut ! _)

    sut ! Flush

    stateSerializer.expectMsg(State(List(
      StateTask("user", "desc 1", dateTime3),
      StateTask("user2", "desc 1", dateTime3),
      StateTask("user2", "desc 2", dateTime4)
    )))
  }

  it should "send no execution info if task hasn't been added" in {
    (fileHelper.read _).when(filePath).returns("")

    val sut = system.actorOf(Props(new StateSaverActor(stateSerializer.ref, fileHelper, filePath)))
    stateSerializer.expectMsg(DeserializeState(""))

    sut ! State(List())

    sut ! GetLastExecutionTime("user", "desc")

    expectMsg(NoExecutionYet("user", "desc"))
  }

  it should "respond with last execution time" in {
    (fileHelper.read _).when(filePath).returns("")

    val sut = system.actorOf(Props(new StateSaverActor(stateSerializer.ref, fileHelper, filePath)))
    stateSerializer.expectMsg(DeserializeState(""))

    sut ! State(List())

    val now = LocalDateTime.now
    sut ! StateTask("user", "desc", now)

    sut ! GetLastExecutionTime("user", "desc")

    expectMsg(LastExecutionTime("user", "desc", now))
  }

  it should "respond with last execution time even if updated" in {
    (fileHelper.read _).when(filePath).returns("")

    val sut = system.actorOf(Props(new StateSaverActor(stateSerializer.ref, fileHelper, filePath)))
    stateSerializer.expectMsg(DeserializeState(""))

    sut ! State(List())

    val yesterday = LocalDateTime.now.minusDays(1)
    sut ! StateTask("user", "desc", yesterday)
    val now = LocalDateTime.now
    sut ! StateTask("user", "desc", now)

    sut ! GetLastExecutionTime("user", "desc")

    expectMsg(LastExecutionTime("user", "desc", now))
  }
}
