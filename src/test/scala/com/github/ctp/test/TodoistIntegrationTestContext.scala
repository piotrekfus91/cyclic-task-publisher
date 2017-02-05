package com.github.ctp.test

import java.util.UUID

import com.github.ctp.publisher.todoist.TodoistConsts

import scalaj.http.Http

object TodoistIntegrationTestContext {
  val todoistTestProject = "CTP test project"
  val todoistApiToken = Option(System.getProperty("todoistApiToken"))
  var projectId = ""

  def todoistAvailable: Boolean = {
    todoistApiToken.isDefined
  }

  def createTestProject(): Unit = {
    projectId = uuid
    Http(TodoistConsts.apiUrl)
      .param("token", todoistApiToken.get)
      .param("commands",
        s"""
          |[
          |   {
          |      "type":"project_add",
          |      "temp_id":"$projectId",
          |      "uuid":"$uuid",
          |      "args":{
          |         "name":"$todoistTestProject"
          |      }
          |   }
          |]
        """.stripMargin)
      .postForm
      .execute()
  }

  def deleteTestProject(): Unit = {
    Http(TodoistConsts.apiUrl)
      .param("token", todoistApiToken.get)
      .param("commands",
        s"""
           |[{"type": "project_delete", "uuid": "$uuid", "args": {"ids": ["$projectId"]}}]
        """.stripMargin)
      .postForm
      .execute()
  }

  def getAllTasks: String = {
    Http(TodoistConsts.apiUrl)
      .param("token", todoistApiToken.get)
      .param("sync_token", "*")
      .param("resource_types", "[\"items\"]")
      .postForm
      .asString
      .body
  }

  private def uuid = UUID.randomUUID().toString
}
