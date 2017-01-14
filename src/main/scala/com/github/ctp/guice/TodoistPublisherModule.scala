package com.github.ctp.guice

import akka.actor.Actor
import com.github.ctp.publisher.todoist.TodoistTaskPublisher
import com.github.ctp.publisher.todoist.service.{ProjectListManager, ProjectListManagerImpl}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class TodoistPublisherModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ProjectListManager].to[ProjectListManagerImpl]
    bind[Actor].annotatedWithName(TodoistTaskPublisher.actorName).to[TodoistTaskPublisher]
  }
}
