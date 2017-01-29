package com.github.ctp.state

import java.time.ZonedDateTime

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.github.ctp.state.dto.{State, StateTask}
import com.github.ctp.util.FileHelper
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpecLike, Matchers}

import scala.concurrent.duration._

class StateSaverActorTest extends TestKit(ActorSystem("test")) with ImplicitSender with FlatSpecLike with Matchers with MockFactory {
  val filePath = "/tmp/stateSaver.yml"
  val fileHelper = stub[FileHelper]
  val stateSerializer = TestProbe()

  "StateSaver" should "flush to disk" in {
    (fileHelper.read _).when(filePath).returns("some state")

    val sut = system.actorOf(Props(new StateSaverActor(stateSerializer.ref, fileHelper, filePath)))
    stateSerializer.expectMsg(DeserializeState("some state"))
    sut ! State(List(StateTask("user", "desc", Map())))

    sut ! Flush

    stateSerializer.expectMsg(State(List(StateTask("user", "desc", Map()))))
  }

  it should "handle incoming state tasks of single user" in {
    (fileHelper.read _).when(filePath).returns("")

    val sut = system.actorOf(Props(new StateSaverActor(stateSerializer.ref, fileHelper, filePath)))
    stateSerializer.expectMsg(DeserializeState(""))

    sut ! State(List())

    val dateTime1 = ZonedDateTime.now
    val dateTime2 = ZonedDateTime.now.plusDays(1)
    val dateTime3 = ZonedDateTime.now.plusDays(2)
    val stateTask1 = StateTask("user", "desc 1", Map("type1" -> dateTime1, "type2" -> dateTime2))
    val stateTask2 = StateTask("user", "desc 2", Map("type2" -> dateTime3))

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

    val dateTime1 = ZonedDateTime.now
    val dateTime2 = ZonedDateTime.now.plusDays(1)
    val dateTime3 = ZonedDateTime.now.plusDays(2)
    val stateTask1 = StateTask("user", "desc 1", Map("type1" -> dateTime1, "type2" -> dateTime2))
    val stateTask2 = StateTask("user", "desc 2", Map("type2" -> dateTime3))
    val stateTask3 = StateTask("user2", "desc 1", Map("type1" -> dateTime1, "type2" -> dateTime2))
    val stateTask4 = StateTask("user2", "desc 3", Map("type2" -> dateTime3))

    List(stateTask1, stateTask2, stateTask3, stateTask4).foreach(sut ! _)

    sut ! Flush

    stateSerializer.expectMsg(State(List(stateTask1, stateTask2, stateTask3, stateTask4)))
  }

  it should "update last times of tasks" in {
    (fileHelper.read _).when(filePath).returns("")

    val sut = system.actorOf(Props(new StateSaverActor(stateSerializer.ref, fileHelper, filePath)))
    stateSerializer.expectMsg(DeserializeState(""))

    sut ! State(List())

    val dateTime1 = ZonedDateTime.now
    val dateTime2 = ZonedDateTime.now.plusDays(1)
    val dateTime3 = ZonedDateTime.now.plusDays(2)
    val dateTime4 = ZonedDateTime.now.plusDays(3)
    val stateTask1 = StateTask("user", "desc 1", Map("type1" -> dateTime1, "type2" -> dateTime2))
    val stateTask3 = StateTask("user2", "desc 1", Map("type1" -> dateTime1, "type2" -> dateTime2))
    val stateTask2 = StateTask("user", "desc 1", Map("type2" -> dateTime3))
    val stateTask4 = StateTask("user2", "desc 1", Map("type2" -> dateTime1))
    val stateTask5 = StateTask("user2", "desc 2", Map("type2" -> dateTime2))
    val stateTask6 = StateTask("user2", "desc 1", Map("type1" -> dateTime3))
    val stateTask7 = StateTask("user2", "desc 2", Map("type2" -> dateTime4))

    List(stateTask1, stateTask2, stateTask3, stateTask4, stateTask5, stateTask6, stateTask7).foreach(sut ! _)

    sut ! Flush

    stateSerializer.expectMsg(State(List(
      StateTask("user", "desc 1", Map("type1" -> dateTime1, "type2" -> dateTime3)),
      StateTask("user2", "desc 1", Map("type1" -> dateTime3, "type2" -> dateTime1)),
      StateTask("user2", "desc 2", Map("type2" -> dateTime4))
    )))
  }

  it should "not send anything if task hasn't been added" in {
    (fileHelper.read _).when(filePath).returns("")

    val sut = system.actorOf(Props(new StateSaverActor(stateSerializer.ref, fileHelper, filePath)))
    stateSerializer.expectMsg(DeserializeState(""))

    sut ! State(List())

    sut ! GetLastExecutionTime("user", "desc")

    expectNoMsg(100 millis)
  }

  it should "not respond with last execution time" in {
    (fileHelper.read _).when(filePath).returns("")

    val sut = system.actorOf(Props(new StateSaverActor(stateSerializer.ref, fileHelper, filePath)))
    stateSerializer.expectMsg(DeserializeState(""))

    sut ! State(List())

    val now = ZonedDateTime.now
    sut ! StateTask("user", "desc", Map("type" -> now))

    sut ! GetLastExecutionTime("user", "desc")

    expectMsg(LastExecutionTime("user", "desc", Map("type" -> now)))
  }

  it should "not respond with last execution time even if updated" in {
    (fileHelper.read _).when(filePath).returns("")

    val sut = system.actorOf(Props(new StateSaverActor(stateSerializer.ref, fileHelper, filePath)))
    stateSerializer.expectMsg(DeserializeState(""))

    sut ! State(List())

    val yesterday = ZonedDateTime.now.minusDays(1)
    sut ! StateTask("user", "desc", Map("type" -> yesterday))
    val now = ZonedDateTime.now
    sut ! StateTask("user", "desc", Map("type" -> now))

    sut ! GetLastExecutionTime("user", "desc")

    expectMsg(LastExecutionTime("user", "desc", Map("type" -> now)))
  }
}
