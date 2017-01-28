package com.github.ctp.publisher.todoist.service

import com.github.ctp.domain.UserData
import com.github.ctp.publisher.todoist.TodoistConsts
import com.typesafe.scalalogging.LazyLogging

import scalaj.http.Http

class TodoistHttpRunner extends LazyLogging {
  def publishTask(userData: UserData, commands: String): Either[Unit, Unit] = {
    val response = basicRequest(userData.todoist.map(_.apiToken.get).get)
      .param("commands", commands)
      .postForm
      .asString
    logger.debug(s"result code: ${response.code}")
    response.code match {
      case 200 => Right()
      case _ => Left()
    }
  }

  def getProjects(userData: UserData): String = {
    basicRequest(userData.todoist.map(_.apiToken.get).get)
      .param("resource_types", "[\"projects\"]")
      .postForm
      .asString
      .body
  }

  private def basicRequest(apiToken: String) = {
    Http(TodoistConsts.apiUrl)
      .param("token", apiToken)
      .param("sync_token", "*")
  }
}
