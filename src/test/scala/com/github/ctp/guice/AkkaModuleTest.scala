package com.github.ctp.guice

import akka.actor.Actor
import org.scalatest.{FlatSpec, Matchers}

import scala.annotation.StaticAnnotation

class AkkaModuleTest extends FlatSpec with Matchers {
  "ActorName" should "return correct actor name" in {
    ActorName[Test] shouldBe "TestActor"
  }
}

abstract class TestActor extends Actor
class Test extends StaticAnnotation