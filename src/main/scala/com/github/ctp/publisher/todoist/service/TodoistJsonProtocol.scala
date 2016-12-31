package com.github.ctp.publisher.todoist.service

import com.github.ctp.publisher.todoist.dto.{Project, TodoistResponse}
import spray.json._

object TodoistJsonProtocol extends DefaultJsonProtocol {
  implicit val projectFormat = jsonFormat2(Project)
  implicit val todoistResponseFormat = jsonFormat1(TodoistResponse)
}
