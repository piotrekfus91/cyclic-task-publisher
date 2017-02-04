package com.github.ctp.scheduler

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import com.github.ctp.state.LastExecutionTime
import com.github.ctp.test.TestDateTimeProvider
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpecLike

class RetriggeringServiceActorTest extends TestKit(ActorSystem("test")) with FlatSpecLike with MockFactory
    with TestDateTimeProvider {

  "RetriggeringService" should "retrigger task" in {
    val akkaSchedulerEntryPoint = TestProbe()
    val sut = system.actorOf(Props(new RetriggeringServiceActor(akkaSchedulerEntryPoint.ref)))

    sut ! Retrigger("user", "desc", "system", dateTimeProvider.dateTime)

    akkaSchedulerEntryPoint.expectMsg(LastExecutionTime(
      "user", "desc", Map("system" -> dateTimeProvider.dateTime)
    ))
  }
}
