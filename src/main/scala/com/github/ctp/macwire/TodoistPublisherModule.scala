package com.github.ctp.macwire

import akka.actor.{ActorRef, Props}
import com.github.ctp.publisher.todoist.service.{HttpRunner, ProjectListFetcher, ProjectListManager, ProjectListManagerImpl}
import com.github.ctp.publisher.todoist.{TodoistTaskPublisher, TodoistTaskPublisherActor}
import com.softwaremill.macwire._
import com.softwaremill.tagging._

trait TodoistPublisherModule extends UtilModule with LoggerModule with AkkaModule {
  lazy val httpRunner: HttpRunner = wire[HttpRunner]
  lazy val projectListFetcher: ProjectListFetcher = wire[ProjectListFetcher]
  lazy val projectListManager: ProjectListManager = wire[ProjectListManagerImpl]

  def todoistTaskPublisher(): @@[ActorRef, TodoistTaskPublisher] = actorSystem
    .actorOf(Props(wire[TodoistTaskPublisherActor]))
    .taggedWith[TodoistTaskPublisher]
}
