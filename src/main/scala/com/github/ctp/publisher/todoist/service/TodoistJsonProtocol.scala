package com.github.ctp.publisher.todoist.service

import com.github.ctp.publisher.todoist.dto.{Command, Project, TodoistResponse, TodoistTask}
import spray.json._

object TodoistJsonProtocol extends DefaultJsonProtocol {
  implicit val projectFormat = jsonFormat2(Project)
  implicit val todoistResponseFormat = jsonFormat1(TodoistResponse)
  implicit val todoistTaskFormat = jsonFormat2(TodoistTask)
  implicit object TaskCommandFormat extends JsonFormat[Command] {
    override def write(command: Command): JsValue = {
      JsObject(
        "type" -> JsString(command.commandType),
        "temp_id" -> JsString(command.tempId),
        "uuid" -> JsString(command.uuid),
        "args" -> todoistTaskFormat.write(command.todoistTask)
      )
    }

    override def read(json: JsValue): Command = {
      json.asJsObject.getFields("type", "temp_id", "uuid", "args") match {
        case Seq(JsString(commandType), JsString(tempId), JsString(uuid), JsObject(todoistTask)) =>
          Command(commandType, tempId, uuid, todoistTaskFormat.read(todoistTask("args")))
        case _ => throw DeserializationException("cannot deserialize command")
      }
    }
  }
}
