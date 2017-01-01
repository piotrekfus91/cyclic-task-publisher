package com.github.ctp.state

import java.time.ZonedDateTime

import com.github.ctp.state.dto.{State, StateTask}
import net.jcazevedo.moultingyaml.{DefaultYamlProtocol, YamlFormat, YamlString, YamlValue}
import spray.json.DeserializationException

object StateMoultingFormats extends DefaultYamlProtocol {
  implicit object ZonedDateTimeFormat extends YamlFormat[ZonedDateTime] {
    override def write(zonedDateTime: ZonedDateTime): YamlValue = {
      YamlString(zonedDateTime.toString)
    }

    override def read(yaml: YamlValue): ZonedDateTime = {
      yaml match {
        case YamlString(str) => ZonedDateTime.parse(str)
        case _ => throw DeserializationException("cannot deserialize date")
      }
    }
  }
  implicit val stateTaskFormat = yamlFormat3(StateTask)
  implicit val stateFormat = yamlFormat1(State)
}
