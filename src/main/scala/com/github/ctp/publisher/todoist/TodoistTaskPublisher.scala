package com.github.ctp.publisher.todoist

import com.github.ctp.domain.{Task, UserData}
import com.github.ctp.publisher.TaskPublisher
import com.typesafe.scalalogging.LazyLogging

class TodoistTaskPublisher extends TaskPublisher with LazyLogging {
  private val todoistApiUrl = ""
  override def publish(userData: UserData, task: Task): Unit = {
  }
}
