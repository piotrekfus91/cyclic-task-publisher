package com.github.ctp.macwire

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait AkkaModule {
  def actorSystem: ActorSystem

  def stopAkka(): Unit = {
    actorSystem.terminate()
    Await.result(actorSystem.whenTerminated, Duration(1000, TimeUnit.MILLISECONDS))
  }
}
