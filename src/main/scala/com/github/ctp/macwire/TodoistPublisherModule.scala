package com.github.ctp.macwire

import com.github.ctp.publisher.todoist.TodoistTaskPublisher
import com.github.ctp.publisher.todoist.service.{HttpRunner, ProjectListFetcher, ProjectListManagerImpl}
import com.softwaremill.macwire._

trait TodoistPublisherModule extends UtilModule {
  lazy val httpRunner = wire[HttpRunner]
  lazy val projectListFetcher = wire[ProjectListFetcher]
  lazy val projectListManager = wire[ProjectListManagerImpl]
  lazy val todoistTaskPublisher = wire[TodoistTaskPublisher]
}
