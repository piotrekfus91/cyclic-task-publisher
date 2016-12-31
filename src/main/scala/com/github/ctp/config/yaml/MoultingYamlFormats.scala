package com.github.ctp.config.yaml

import com.github.ctp.domain._
import net.jcazevedo.moultingyaml._

object MoultingYamlFormats extends DefaultYamlProtocol {
  implicit val taskFormat = yamlFormat2(Task)
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
