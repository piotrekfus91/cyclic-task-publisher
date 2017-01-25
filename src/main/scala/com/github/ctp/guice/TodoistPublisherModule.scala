package com.github.ctp.guice

import akka.actor.{Actor, ActorSystem}
import com.github.ctp.publisher.todoist.{TodoistTaskPublisher, TodoistTaskPublisherActor}
import com.github.ctp.publisher.todoist.service.{ProjectListManager, ProjectListManagerImpl}
import com.google.inject.{AbstractModule, Inject, Provides}
import net.codingwell.scalaguice.ScalaModule

class TodoistPublisherModule extends AbstractModule with ScalaModule with GuiceAkkaActorRefProvider {
  override def configure(): Unit = {
    bind[ProjectListManager].to[ProjectListManagerImpl]
    bind[Actor].annotatedWithName(ActorName[TodoistTaskPublisher]).to[TodoistTaskPublisherActor]
  }

  @Provides
  @TodoistTaskPublisher
  def todoistTaskPublisher(@Inject() system: ActorSystem) = provideActorRef(system, ActorName[TodoistTaskPublisherActor])
}
