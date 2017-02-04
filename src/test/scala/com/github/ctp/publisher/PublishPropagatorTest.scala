package com.github.ctp.publisher

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import com.github.ctp.config.domain.{Task, UserData}
import org.scalatest.FlatSpecLike

import scala.concurrent.duration._

class PublishPropagatorTest extends TestKit(ActorSystem("test")) with FlatSpecLike {
  val userData = UserData("name")
  val task = Task("desc", "project")

  val sut = new PublishPropagator {}

  "PublishPropagator" should "do nothing when there is no publishers left" in {
    val publish = Publish(userData, task, PublisherSequence(List()))
    sut.propagate(publish)
  }

  it should "send to next actor if there is one actor left" in {
    val publisher = TestProbe()
    val publish = Publish(userData, task, PublisherSequence(List(publisher.ref)))

    sut.propagate(publish)

    publisher.expectMsg(Publish(userData, task, PublisherSequence(List())))
  }

  it should "send to next actor if there are many actors left" in {
    val publisher1 = TestProbe()
    val publisher2 = TestProbe()
    val publish = Publish(userData, task, PublisherSequence(List(publisher1.ref, publisher2.ref)))

    sut.propagate(publish)

    publisher1.expectMsg(Publish(userData, task, PublisherSequence(List(publisher2.ref))))
    publisher2.expectNoMsg(100 millis)
  }
}
