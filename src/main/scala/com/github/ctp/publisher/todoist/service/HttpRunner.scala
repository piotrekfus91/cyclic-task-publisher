package com.github.ctp.publisher.todoist.service

import com.github.ctp.domain.UserData
import com.github.ctp.publisher.todoist.TodoistConsts
import com.typesafe.scalalogging.LazyLogging

import scalaj.http.Http

class HttpRunner extends LazyLogging {
  def publishTask(userData: UserData, commands: String) = {
    val code = basicRequest(userData.todoist.map(_.apiToken.get).get)
      .param("commands", commands)
      .postForm
      .asString
      .code
    code match {
      case 200 => Right
      case _ => Left
    }
  }

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
  }
}
