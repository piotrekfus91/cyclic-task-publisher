package com.github.ctp.state

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.TestKit
import com.github.ctp.util.FileHelper
import com.softwaremill.tagging._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpecLike, Matchers}

class StateSaverActorTest extends TestKit(ActorSystem("test")) with FlatSpecLike with Matchers with MockFactory {
  "StateSaver" should "flush to disk" in {
    val filePath = "/tmp/stateSaver.yml"

    val fileHelper = mock[FileHelper]
    (fileHelper.write _).expects(filePath, "")
    val stateSerializer = mock[ActorRef].taggedWith[StateSerializer]
    val sut = system.actorOf(Props(new StateSaverActor(fileHelper, "".taggedWith[StateFilePath], stateSerializer.taggedWith[StateSerializer])))

    sut ! Flush
  }
}
