package com.github.ctp.state

import java.time.{ZoneId, ZonedDateTime}

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.github.ctp.state.dto.{State, StateTask}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike}

class StateSerializerTest extends TestKit(ActorSystem("test")) with ImplicitSender with FlatSpecLike with BeforeAndAfterAll {
  override protected def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  "State serializer" should "serialize state" in {
    val stateSerializer = TestActorRef[StateSerializer]
    stateSerializer ! State(List(
      StateTask("piotrek", "task one", Map("todoist" -> ZonedDateTime.of(2017, 1, 1, 21, 46, 37, 0, ZoneId.systemDefault()))),
      StateTask("piotrek", "task two", Map("todoist" -> ZonedDateTime.of(2017, 1, 1, 21, 46, 38, 0, ZoneId.systemDefault())))
    ))
    expectMsg(SerializedState(
      """tasks:
        |- user: piotrek
        |  description: task one
        |  last:
        |    todoist: 2017-01-01T21:46:37+01:00[Europe/Warsaw]
        |- user: piotrek
        |  description: task two
        |  last:
        |    todoist: 2017-01-01T21:46:38+01:00[Europe/Warsaw]
        |""".stripMargin
    ))
  }
}
