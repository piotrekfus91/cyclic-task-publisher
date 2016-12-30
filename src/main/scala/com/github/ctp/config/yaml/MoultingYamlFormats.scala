package com.github.ctp.config.yaml

import com.github.ctp.domain._
import net.jcazevedo.moultingyaml._

object MoultingYamlFormats extends DefaultYamlProtocol {
  implicit val taskFormat = yamlFormat1(Task)
  implicit val userTasksFormat = yamlFormat1(UserTasks)
  implicit val todoistUserFormat = yamlFormat2(TodoistUser)
  implicit val userDataFormat = yamlFormat1(UserData)
  implicit val configFormat = yamlFormat2(Config)
}
