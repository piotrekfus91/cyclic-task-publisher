package com.github.ctp.publisher.todoist.service

import com.github.ctp.publisher.todoist.dto.{Command, Project, TodoistResponse, TodoistTask}
import spray.json._

object TodoistJsonProtocol extends DefaultJsonProtocol {
  implicit val projectFormat = jsonFormat2(Project)
  implicit val todoistResponseFormat = jsonFormat1(TodoistResponse)
  implicit val todoistTaskFormat = jsonFormat2(TodoistTask)
  implicit val taskCommandFormat = jsonFormat4(Command[TodoistTask])
}
