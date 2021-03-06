package com.github.ctp.publisher.todoist.service

import com.github.ctp.config.domain.UserData
import com.github.ctp.publisher.todoist.dto.{Project, TodoistResponse}
import com.github.ctp.publisher.todoist.service.TodoistJsonProtocol._
import com.google.inject.Inject
import spray.json._

class ProjectListFetcher @Inject() (private val httpRunner: TodoistHttpRunner) {
  def fetchProjectsOfUser(userData: UserData): List[Project] = {
    val response = httpRunner.getProjects(userData)
    val json = response.parseJson
    val todoistResponse = json.convertTo[TodoistResponse]
    todoistResponse.projects.getOrElse(List())
  }
}


