package com.github.ctp.macwire

import com.github.ctp.publisher.todoist.TodoistTaskPublisher
import com.github.ctp.publisher.todoist.service.{HttpRunner, ProjectListFetcher, ProjectListManager}
import com.softwaremill.macwire._

trait TodoistPublisherModule {
  lazy val httpRunner = wire[HttpRunner]
  lazy val projectListFetcher = wire[ProjectListFetcher]
  lazy val projectListManager = wire[ProjectListManager]
  lazy val todoistTaskPublisher = wire[TodoistTaskPublisher]
}
