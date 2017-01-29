package com.github.ctp.state

import java.time.LocalDateTime

import com.github.ctp.state.dto.{State, StateTask}
import net.jcazevedo.moultingyaml.{DefaultYamlProtocol, YamlFormat, YamlString, YamlValue}
import spray.json.DeserializationException

object StateMoultingFormats extends DefaultYamlProtocol {
  implicit object LocalDateTimeFormat extends YamlFormat[LocalDateTime] {
    override def write(localDateTime: LocalDateTime): YamlValue = {
      YamlString(localDateTime.toString)
    }

    override def read(yaml: YamlValue): LocalDateTime = {
      yaml match {
        case YamlString(str) => LocalDateTime.parse(str)
        case _ => throw DeserializationException("cannot deserialize date")
      }
    }
  }
  implicit val stateTaskFormat = yamlFormat3(StateTask)
  implicit val stateFormat = yamlFormat1(State)
}
