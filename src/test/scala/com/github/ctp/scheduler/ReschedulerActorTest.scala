package com.github.ctp.scheduler

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import com.github.ctp.config.domain.{Task, UserData}
import com.github.ctp.publisher.{Publish, PublisherSequence}
import com.github.ctp.state.LastExecutionTime
import com.github.ctp.test.TestDateTimeProvider
import org.scalatest.FlatSpecLike

class ReschedulerActorTest extends TestKit(ActorSystem("test")) with FlatSpecLike with TestDateTimeProvider {
  "Rescheduler" should "propagate schedule" in {
    val akkaSchedulerEntryPoint = TestProbe()
    val sut = system.actorOf(Props(new ReschedulerActor(akkaSchedulerEntryPoint.ref, dateTimeProvider)))

    sut ! Publish(UserData("name", None), Task("desc", "project"), PublisherSequence(List()))

    akkaSchedulerEntryPoint.expectMsg(LastExecutionTime("name", "desc", nowDateTime))
  }
}
