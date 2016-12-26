package com.github.ctp.config.yaml

import com.github.ctp.domain.{AllTasks, Config, Task, UserTasks}
import net.jcazevedo.moultingyaml._

object MoultingYamlFormats extends DefaultYamlProtocol {
  implicit val taskFormat = yamlFormat1(Task)
  implicit val userTasksFormat = yamlFormat1(UserTasks)
  implicit val allTasks = yamlFormat1(AllTasks)
  implicit val configFormat = yamlFormat1(Config)
}
