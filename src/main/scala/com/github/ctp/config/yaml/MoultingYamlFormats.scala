package com.github.ctp.config.yaml

import com.github.ctp.config.domain._
import net.jcazevedo.moultingyaml._

object MoultingYamlFormats extends DefaultYamlProtocol {
  implicit object TaskFormat extends YamlFormat[Task] {
    private def fromYamlObject(map: Map[YamlValue, YamlValue]) = {
      val description = map(YamlString("description")).asInstanceOf[YamlString].value
      val project = map(YamlString("project")).asInstanceOf[YamlString].value
      val schedule = map(YamlString("schedule")) match {
        case YamlNull => Map[String, String]()
        case scheduleMap => scheduleMap.convertTo[Map[String, String]]
      }
      val publishers = map.get(YamlString("publishers")) match {
        case None => List()
        case Some(list) => list.convertTo[List[String]]
      }
      Task(description, project, schedule, publishers)
    }

    override def read(yaml: YamlValue): Task = {
      yaml match {
        case YamlObject(map) => fromYamlObject(map)
        case _ => deserializationError("cannot deserialize task")
      }
    }

    override def write(obj: Task): YamlValue = ???
  }

  implicit val userTasksFormat = yamlFormat1(UserTasks)
  implicit val todoistUserFormat = yamlFormat2(TodoistUser)

  implicit object UserDataFormat extends YamlFormat[UserData] {
    private def fromYamlObject(map: Map[YamlValue, YamlValue]) = {
      UserData("", map.get(YamlString("todoist")).map(todoistUserFormat.read))
    }

    override def read(yaml: YamlValue): UserData = {
      yaml match {
        case YamlObject(map) => fromYamlObject(map)
        case YamlNull => UserData("")
        case _ => deserializationError("cannot deserialize user")
      }
    }

    override def write(obj: UserData): YamlValue = {
      obj.todoist match {
        case None => YamlObject()
        case Some(todoist) => YamlObject((YamlString("todoist"), todoistUserFormat.write(todoist)))
      }
    }
  }

  implicit val configFormat = yamlFormat2(Config)
}
