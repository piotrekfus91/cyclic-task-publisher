package com.github.ctp.publisher.todoist.service

import com.github.ctp.domain.UserData
import com.github.ctp.publisher.todoist.TodoistConsts

import scalaj.http.Http

class HttpRunner {
  def getProjects(userData: UserData): String = {
    basicRequest(userData.todoist.map(_.apiToken.get).get)
      .param("resource_types", "projects")
      .asString
      .body
  }

  private def basicRequest(apiToken: String) = {
    Http(TodoistConsts.apiUrl)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .param("token", apiToken)
      .param("sync_token", "*")
  }
}
